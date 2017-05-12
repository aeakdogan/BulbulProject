###################
#   Fetches all spotify track information usrin spotify track ids
#   For more information ask @ mesutgurlek
#   Important: You should check the output file paths and update them
###################

import requests
import json
import base64
import atexit
import sys
from os import listdir, mkdir
from time import sleep

global machine
global slack_webhook_url

client_id = '4e262c64ec3a472e935cb6506dd661bf'
client_secret = '88aaa1ebc11d4cffa99d4bb4bc37e0ac'
slack_webhook_url = 'https://hooks.slack.com/services/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'
machine = sys.argv[2]


# Read the spotify ids of the tracks and put into list
def get_spotify_uris(filename):
    file = open(filename, 'r')
    content = file.read()
    file.close()

    data = [x.split(' <-> ')[1] for x in content.splitlines()]
    data = [x for x in data if x != 'None']
    return data


# Downloads the track info from spotify
def get_spotify_tracks(filename, m_no):
    data = get_spotify_uris(filename)

    if 'spotify_tracks' not in listdir():
        mkdir('spotify_tracks')

    dirs = listdir('spotify_tracks')
    downloaded_files = []
    for filename in dirs:
        downloaded_files.append(filename.split('_')[0])

    skipped_file_count = 0
    cnt = 0
    notify_slack('{} - Started tracks running script'.format(m_no))

    # Make requests for every track id
    for id in data:
        if id in downloaded_files:
            skipped_file_count += 1
            continue

        url = 'https://api.spotify.com/v1/tracks/{}'.format(id)
        resp = requests.get(url)
        sleep(0.1)

        with open('spotify_tracks/{}_spotify'.format(id), 'w') as f:
            f.write(resp.text)

        cnt += 1
        if cnt % 1000 == 0:
            notify_slack('{} - ({}) # of Spotify tracks skipped!'.format(m_no, skipped_file_count))
            notify_slack('{} - ({}) # of Spotify tracks downloaded!'.format(m_no, cnt))


# Downloads track audio analysis and features from spotify
def get_audio_analysis_and_features(filename, m_no):
    data = get_spotify_uris(filename)

    if 'audio_features' not in listdir():
        mkdir('audio_features')

    dirs = listdir('audio_features')
    downloaded_files = []
    for filename in dirs:
        downloaded_files.append(filename.split('_')[0])


    counter = 0
    skipped_file_count = 0
    notify_slack('{} - Started audio features running script'.format(m_no))

    for id in data:
        if id in downloaded_files:
            skipped_file_count += 1
            continue

        # analysis_url = 'https://api.spotify.com/v1/audio-analysis/{}'.format(id)
        features_url = 'https://api.spotify.com/v1/audio-features/{}'.format(id)

        # analysis_resp = get_authenticated(analysis_url)
        features_resp = get_authenticated(features_url)
        sleep(0.1)

        # with open('track_audio_analysis/{}_audio_analysis'.format(id), 'w') as f:
        #     f.write(analysis_resp.text)

        with open('audio_features/{}_audioFeatures'.format(id), 'w') as f:
            f.write(features_resp.text)

        counter += 1
        if counter % 1000 == 0:
            notify_slack('{} - ({}) # of Spotify audio features skipped!'.format(m_no, skipped_file_count))
            notify_slack('{} - ({}) # of Spotify audio features downloaded!'.format(m_no, counter))

    notify_slack('{} - Finished script'.format(m_no))


def refresh_auth_token():
    global token
    url = 'https://accounts.spotify.com/api/token'

    payload = {
        'grant_type': 'client_credentials',
    }

    resp = requests.post(url, data=payload, headers={'Authorization': 'Basic {}'.format(credentials)})

    if resp.status_code == 200:
        print(resp.json())
        json_response = resp.json()
        token = json_response['access_token']
    else:
        print(resp.json())
        report_to_slack(resp)


def get_authenticated(url):
    response = requests.get(url, headers={'Authorization': 'Bearer {}'.format(token),
                                          'Accept': 'application/json'})
    if response.status_code == 429:
        print('Sleeping for {} seconds'.format(response.headers['Retry-After']))
        sleep(response.headers['Retry-After'])
        return get_authenticated(url)

    elif response.status_code == 401:
        refresh_auth_token()
        print(response.json())
        return get_authenticated(url)

    elif 200 <= response.status_code < 300:
        return response

    else:
        report_to_slack(response)


def notify_slack(message):
    payload = {
        'payload': json.dumps({'text': message})
    }
    print(message)
    resp = requests.post(slack_webhook_url, data=payload)


def report_to_slack(response):
    notify_slack('{} - Script stopped \n{} : \n{}'.format(machine, response.json(), response.url))
    exit(0)


notify_slack('{} - Spotify Script started'.format(machine))

# filename = '../Data/matched_lastfm_spotify_ids.txt'

credentials = base64.b64encode(bytes('{}:{}'.format(client_id, client_secret), 'utf-8')).decode('utf-8')
atexit.register(notify_slack, message='{} - Exited script'.format(machine))

token = ''
refresh_auth_token()
get_spotify_tracks(filename=sys.argv[1], m_no=machine)
# get_audio_analysis_and_features(filename=sys.argv[1], m_no=machine)


# scope=user-read-private&scope=user-read-birthdate&scope=user-read-email&scope=playlist-read-private&scope=playlist-read-collaborative&scope=playlist-modify-public&scope=playlist-modify-private&scope=user-library-read&scope=user-library-modify&scope=user-follow-read&scope=user-follow-modify&scope=user-top-read

###################
#   Fetches all spotify track information usrin spotify track ids
#   For more information ask @ mesutgurlek
#   Important: You should check the output file paths and update them
###################

import requests
import json
import base64
import atexit
from time import sleep


# Read the spotify ids of the tracks and put into list
def get_spotify_uris(filename):
    file = open(filename, 'r')
    content = file.read()
    file.close()

    data = [x.split(' <-> ')[1] for x in content.splitlines()]
    data = [x for x in data if x != 'None']
    return data


# Downloads the track info from spotify
def get_spotify_tracks(filename):
    data = get_spotify_uris(filename)

    cnt = 0
    # Make requests for every track id
    for id in data:
        url = 'https://api.spotify.com/v1/tracks/{}'.format(id)
        resp = requests.get(url)
        with open('spotify_tracks/{}_spotify'.format(id), 'w') as f:
            f.write(resp.text)
        cnt += 1
        print('Downloaded File: {}'.format(cnt))


# Downloads track audio analysis and features from spotify
def get_audio_analysis_and_features(filename):
    data = get_spotify_uris(filename)
    counter = 0

    notify_slack('Started running script')

    for id in data:
        analysis_url = 'https://api.spotify.com/v1/audio-analysis/{}'.format(id)
        features_url = 'https://api.spotify.com/v1/audio-features/{}'.format(id)

        analysis_resp = get_authenticated(analysis_url)
        sleep(0.2)
        features_resp = get_authenticated(features_url)

        with open('track_audio_analysis/{}_audio_analysis'.format(id), 'w') as f:
            f.write(analysis_resp.text)

        with open('track_audio_features/{}_audio_features'.format(id), 'w') as f:
            f.write(features_resp.text)

        counter += 1
        print(counter)

    notify_slack('Finished script')


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
    notify_slack('Script stopped \n{} : \n{}'.format(response.json(), response.url))
    exit(0)


client_id = '4e262c64ec3a472e935cb6506dd661bf'
client_secret = '88aaa1ebc11d4cffa99d4bb4bc37e0ac'
filename = '../Data/matched_lastfm_spotify_ids.txt'
slack_webhook_url = 'https://hooks.slack.com/services/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'
credentials = base64.b64encode(bytes('{}:{}'.format(client_id, client_secret), 'utf-8')).decode('utf-8')
atexit.register(notify_slack, message='Exited script')

token = ''
refresh_auth_token()
get_audio_analysis_and_features(filename)

# get_spotify_tracks(filename)
# scope=user-read-private&scope=user-read-birthdate&scope=user-read-email&scope=playlist-read-private&scope=playlist-read-collaborative&scope=playlist-modify-public&scope=playlist-modify-private&scope=user-library-read&scope=user-library-modify&scope=user-follow-read&scope=user-follow-modify&scope=user-top-read

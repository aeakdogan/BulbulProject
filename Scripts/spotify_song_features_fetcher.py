###################
#   Fetches all spotify track information usrin spotify track ids
#   For more information ask @ mesutgurlek
#   Important: You should check the output file paths and update them
###################

import requests
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
    auth_token = 'BQAq8MiRRjac2k-ZspoBCZ1e0lpmE0WbMgfVnnSZZ0tlcCTgJBFSIvr8i3XSbKofP2EWwASBO7hi-vmumqIDEgUN6EbNtSKvvdPxL1wLmDo5Z3MjFgMdfxytv6sw9fMrl_Myz8stKPB1BjU9'
    counter = 0

    for id in data:
        analysis_url = 'https://api.spotify.com/v1/audio-analysis/{}'.format(id)
        features_url = 'https://api.spotify.com/v1/audio-features/{}'.format(id)

        analysis_resp = requests.get(analysis_url, headers={'Authorization': 'Bearer {}'.format(auth_token),
                                                            'Accept': 'application/json'})
        sleep(0.2)
        features_resp = requests.get(features_url, headers={'Authorization': 'Bearer {}'.format(auth_token),
                                                            'Accept': 'application/json'})

        with open('track_audio_analysis/{}_audio_analysis'.format(id), 'w') as f:
            f.write(analysis_resp.text)

        with open('track_audio_features/{}_audio_features'.format(id), 'w') as f:
            f.write(features_resp.text)

        counter += 1
        print(counter)


filename = 'matched_lastfm_spotify_ids.txt'
# get_audio_analysis_and_features(filename)
# get_spotify_tracks(filename)

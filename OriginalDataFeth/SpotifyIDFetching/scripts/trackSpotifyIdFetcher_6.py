#!/usr/bin/env python3
import json
import requests
import difflib
import re
import atexit
import time

# Slack Functions...
slack_webhook_url = 'https://hooks.slack.com/services/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'
error_codes = [401, 403, 429, 500, 502, 503]
script_no = 6

def notify_slack(message):
    payload = {
        'payload': json.dumps({'text': message})
    }
    print(message)
    resp = requests.post(slack_webhook_url, data=payload)


def get_checked(url):
    response = requests.get(url=url)
    if response.status_code in error_codes:
        notify_slack(response.json())
        time.sleep(2)
        exit(0)
    elif response.status_code == 404:
        notify_slack('({})[ERROR] We got an 404 error!!'.format(script_no))
    else:
        return response


# Gets a json tracks file and return (track_name, track_artist) tuple per track)
def get_track_info_top_tracks(json_dict):
    names_artist_id = []
    for i in json_dict['toptracks']['track']:
        names_artist_id.append((i['name'], i['artist']['name'], i['mbid']))
    return names_artist_id


def get_track_info_recent_tracks(json_dict):
    names_artist_id = []
    for i in json_dict['recenttracks']['track']:
        names_artist_id.append((i['name'], i['artist']['#text'], i['mbid'], i['album']['#text']))
    return names_artist_id


# Takes track_name, track_artist and returns spotify track uri
def get_spotify_uri(track_name, track_artist):
    try:
        new_track_name = '+'.join(track_name.split(" "))
        new_track_artist = '+'.join(track_artist.split(" "))
        # uri_request_url = 'https://api.spotify.com/v1/search?q="{}"&type=track'.format(new_track_name)
        uri_request_url = 'https://api.spotify.com/v1/search?q=track:{}%20artist:{}&type=track'.format(new_track_name, new_track_artist)
        resp = get_checked(uri_request_url)
        data = json.loads(resp.text)

        for i in data['tracks']['items']:
            seq1 = difflib.SequenceMatcher(a=track_name.lower(), b=i['name'].lower())
            seq2 = difflib.SequenceMatcher(a=track_artist.lower(), b=i['artists'][0]['name'].lower())
            if (seq1.ratio() > 0.4 and seq2.ratio() > 0.4) or (track_artist.lower() in i['artists'][0]['name'].lower() and seq2.ratio() > 0.4):
                return i['uri']
            # if track_name.lower() == i['name'].lower() and track_artist.lower() == i['artists'][0]['name'].lower():
            #     return i['uri']
            else:
                print('***DEBUG*** :',  track_name.lower(), ' :: ', i['name'].lower(), ' --- ', track_artist.lower(), ' :: ', i['artists'][0]['name'].lower())
    except ValueError:
        print('You have failed this city')
    except KeyError:
        print('No more Exceptions')
    return None


def get_spotify_uri_by_album(track_name, track_artist, album_name):
    try:
        new_track_name = '+'.join(track_name.split(" "))
        new_track_artist = '+'.join(track_artist.split(" "))
        fixed_album_name = re.sub(r'\(.*\)', '', album_name)
        fixed_album_name2 = re.sub(r'\[.*\]', '', fixed_album_name)
        fixed_album_name3 = re.sub(r':', '', fixed_album_name2)
        new_album_name = '+'.join(fixed_album_name3.split(" "))

        album_request_url = 'https://api.spotify.com/v1/search?q=album:{}&type=album'.format(new_album_name)
        resp = get_checked(album_request_url)
        data = json.loads(resp.text)

        if 'items' in data['albums']:
            album_id = data['albums']['items'][0]['uri']

        uri_request_url = 'https://api.spotify.com/v1/albums/{}/tracks'.format(album_id.split(':')[2])
        resp2 = get_checked(url=uri_request_url)
        data2 = json.loads(resp2.text)

        for i in data2['items']:
            seq = difflib.SequenceMatcher(a=track_name.lower(), b=i['name'].lower())
            if seq.ratio() > 0.4:
                return i['uri']

    except ValueError:
        print('You have failed this city')
    except KeyError:
        print('KEY ERROR...')
    except IndexError:
        print('Album name ::: ', fixed_album_name2, ' artis ::: ', track_artist, ' track ::: ', track_name)
        print('You Are out of Index...')
    return None


# Takes Last FM track jsons and fetches Spotify track ids
def import_lastfm_tracks(lastfm_file, output_file):
    tracks_uris = {}
    with open(lastfm_file, mode='r') as file:
        counter = 0
        data = json.loads(file.read())
        name_artist = get_track_info_recent_tracks(data)
        for tuple in name_artist:
            spotify_uri = get_spotify_uri(tuple[0], tuple[1])
            if spotify_uri is None:
                spotify_uri = get_spotify_uri_by_album(tuple[0], tuple[1], tuple[3])
            tracks_uris[(tuple[0], tuple[2])] = spotify_uri
            print('Processing...', counter)
            counter += 1

    with open(output_file, 'a') as file:
        for item, key in tracks_uris.items():
            line = '{} <********> {} <*******> {}'.format(item[0], item[1], key)
            file.write(line)
            file.write('\n')


# Takes a a in the following format and generates an output  file which includes lastfm and spotify id of the track
# Input File Format: has_album_name - lastfm_id - track_name - album_name - artist_name
# Output File Format: lastfm_id - spotify_id
def fetch_spotify_uris_from_list(track_info_list, output_file):
    # Check the output folder for fetched IDs
    fo = open(output_file)
    content_out = fo.read()
    fo.close()
    prev_ids = content_out.splitlines()
    prev_lastfm_ids = [x.split(' <-> ')[0] for x in prev_ids]

    cnt = 0
    f = open(track_info_list, 'r')
    content = f.read()
    f.close()
    lines = content.splitlines()

    skipped_line = 0
    for line in lines:
        info = line.split(' - ')
        has_album, lastfm_id, track_name, album_name, artist_name = info[0], info[1], info[2], info[3], info[4]

        # Check whether we fetched the spotify id of corresponding lastfm id or not
        if lastfm_id in prev_lastfm_ids:
            cnt += 1
            skipped_line += 1
            continue

        # Fetch sportify id from API
        time.sleep(0.1)
        spotify_uri = get_spotify_uri(track_name, artist_name)
        if spotify_uri is None and has_album is not 'None':
            spotify_uri = get_spotify_uri_by_album(track_name, artist_name, album_name)

        # Generate a string of ids to write into file
        if spotify_uri is not None:
            matched_ids = '{} <-> {}\n'.format(lastfm_id, spotify_uri.split(':')[-1])
        else:
            matched_ids = '{} <-> {}\n'.format(lastfm_id, spotify_uri)

        # Writes lastfm and spotify ids of to the file
        with open(output_file, 'a') as file:
            file.write(matched_ids)
        cnt += 1

        if cnt % 1000 == 0:
            notify_slack('({}) Skipped Line : {}'.format(script_no, skipped_line))
            notify_slack('({}) Spotify Track ID Matcher has matched {} LastFM ids'.format(script_no, cnt))

notify_slack('Spotify Track ID Matcher started ({})'.format(script_no))
atexit.register(notify_slack, message='Spotify Track ID Matcher finished ({})'.format(script_no))

file_name = 'unique_{}.txt'.format(script_no)
output_file = 'matched_lastfm_spotify_ids_{}.txt'.format(script_no)
fetch_spotify_uris_from_list(file_name, output_file)


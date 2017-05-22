###################
#   Matches lastfm ids and spotify ids of the tracks using search feature of spotifyt web api
#   For more information ask @ mesutgurlek
#   Important: You should check the output file paths and update them
###################

import json, requests
import difflib
import re


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
        resp = requests.get(url=uri_request_url)
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
        resp = requests.get(url=album_request_url)
        data = json.loads(resp.text)

        if 'items' in data['albums']:
            album_id = data['albums']['items'][0]['uri']

        uri_request_url = 'https://api.spotify.com/v1/albums/{}/tracks'.format(album_id.split(':')[2])
        resp2 = requests.get(url=uri_request_url)
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
    cnt = 0
    f = open(track_info_list, 'r')
    content = f.read()
    f.close()
    lines = content.splitlines()
    for line in lines:
        info = line.split(' - ')
        has_album, lastfm_id, track_name, album_name, artist_name = info[0], info[1], info[2], info[3], info[4]

        # Fetch sportify id from API
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
        print(cnt)


file_name = 'track_info_list.txt'
output_file = 'matched_lastfm_spotify_ids.txt'
fetch_spotify_uris_from_list(file_name, output_file)


# output_file = 'track_uri.txt'
#
# files = os.listdir('recenttracks')
# for file in files:
#     input_file = 'recenttracks/{}'.format(file)
#     try:
#         import_lastfm_tracks(input_file, output_file)
#     except ValueError:
#         print('EXCEPTIONNNNNNNNNNN')

# input_file = 'recent_tracks'
# try:
#     import_lastfm_tracks(input_file, output_file)
# except ValueError:
#     print('EXCEPTIONNNNNNNNNNN')

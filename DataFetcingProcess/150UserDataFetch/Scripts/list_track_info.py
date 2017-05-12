###################
#   Generates a file from lastfm track info json files in the following form:
#   Track name, Artist name, Album name
#   This file is generated to use in matching lastfm and spotify ids
#   Important: You should check the output file paths and update them
###################

from os import listdir
import json, requests


for file in listdir('track_info'):
    try:
        has_album = 1
        file_dir = 'track_info/{}'.format(file)
        with open(file_dir, 'r') as f:
            data = json.loads(f.read())
            lastfm_id = file
            track_name = data['track']['name']
            artist_name = data['track']['artist']['name']
            album_name = data['track']['album']['title']
    except KeyError:
        has_album = 0
        album_name = None
        print("Got an exception")

    # Writes in the file in the following format:
    # Has Album(Boolean) - LastFM Track Id - Track Name - Album Name - Artist Name
    line = '{} - {} - {} - {} - {}\n'.format(has_album, lastfm_id, track_name, album_name, artist_name)
    with open('track_info_list.txt', mode='a') as out:
        out.write(line)

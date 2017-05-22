from os import listdir
import json, requests

counter = 0
cnt = 0
for file in listdir('track_info'):
    try:
        cnt += 1
        has_album = 1
        file_dir = 'track_info/{}'.format(file)
        with open(file_dir, 'r') as f:
            data = json.loads(f.read())
            lastfm_id = file
            track_name = data['track']['name']
            artist_name = data['track']['artist']['name']
            album_name = data['track']['album']['title']
        print("File processed {}".format(cnt))
    except KeyError:
        cnt += 1
        counter += 1
        has_album = 0
        album_name = None
        print("Got an exception {}".format(counter))


    # Writes in the file in the following format:
    # Has Album(Boolean) - LastFM Track Id - Track Name - Album Name - Artist Name
    line = '{} - {} - {} - {} - {}\n'.format(has_album, lastfm_id, track_name, album_name, artist_name)
    with open('unique_tracks.txt', mode='a') as out:
        out.write(line)

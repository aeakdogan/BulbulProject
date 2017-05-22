###################
#   Fetches lastm aritst information using lastfm artist mbid:
#   Artist Info, Top Tracks of Artists, Top Albums of Artists
#   You should provide a file with lastfm artist mbids
#   Important: You should check the output file paths and update them
###################

import requests
from time import sleep


def fetch_artists(filename, output_folder):
    file_content = open(filename, 'r').read()
    album_ids = file_content.splitlines()

    counter = 0
    for id in album_ids:
        info_url = 'http://ws.audioscrobbler.com/2.0?method=artist.getinfo&api_key=7d67bf49e1c3743c1e2ce02eefd530ee&mbid={}&format=json'.format(id)
        toptracks_url = 'http://ws.audioscrobbler.com/2.0?method=artist.gettoptracks&api_key=7d67bf49e1c3743c1e2ce02eefd530ee&mbid={}&format=json'.format(id)
        topalbums_url = 'http://ws.audioscrobbler.com/2.0?method=artist.gettopalbums&api_key=7d67bf49e1c3743c1e2ce02eefd530ee&mbid={}&format=json'.format(id)

        info_resp = requests.get(info_url)
        sleep(0.1)
        toptracks_resp = requests.get(toptracks_url)
        sleep(0.1)
        topalbums_resp = requests.get(topalbums_url)

        with open('{}/{}_artist_info'.format(output_folder, id), 'w') as f:
            f.write(info_resp.text)
        with open('{}/{}_artist_toptracks'.format(output_folder, id), 'w') as f:
            f.write(toptracks_resp.text)
        with open('{}/{}_artist_topalbums'.format(output_folder, id), 'w') as f:
            f.write(topalbums_resp.text)
        counter += 1
        print(counter)

filename = 'song_fetching/outputs/unique_artists.txt'
output_folder = 'lastfm_artist_files'
fetch_artists(filename, output_folder)

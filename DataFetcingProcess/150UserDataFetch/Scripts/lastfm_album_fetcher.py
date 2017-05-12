###################
#   Fetches lastm album information using lastfm album mbid:
#   You should provide a file with lastfm album mbids
#   Important: You should check the output file paths and update them
###################


import requests


def fetch_albums(filename, output_folder):
    file_content = open(filename, 'r').read()
    album_ids = file_content.splitlines()

    counter = 0
    for id in album_ids:
        url = 'http://ws.audioscrobbler.com/2.0?method=album.getinfo&api_key=7d67bf49e1c3743c1e2ce02eefd530ee&mbid={}&format=json'.format(id)
        resp = requests.get(url)
        with open('{}/{}_album_info'.format(output_folder, id), 'w') as f:
            f.write(resp.text)
        counter += 1
        print(counter)

filename = 'song_fetching/outputs/unique_albums.txt'
output_folder = 'lastfm_album_info'
fetch_albums(filename, output_folder)

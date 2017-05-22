###################
#   Fetches lastm track information using lastfm track mbid:
#   You should provide a file with lastfm track mbids
#   Important: You should check the output file paths and update them
###################


import requests
import time

dir_name = '100_user_detail/'
api_key = '4f8e6432dbbb90f1c0a75cac9448902d'

with open( "outputs/unique_tracks.txt", 'r') as file:
    for i,line in enumerate(file):
        if len(line) > 5:
            friends_url = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key={}&mbid={}&format=json".format(api_key, line[:-1])
            top_albums = requests.get(url=friends_url)
            time.sleep(1.7)
            # save the top tracks of user
            with open("track_info/" + line[:-1], 'w+') as resp_file:
                resp_file.write(top_albums.text)

            with open("track_count", 'w+') as resp_file:
                resp_file.write(str(i))
                print(i)

import json, csv
import os


def main():
    for folderName, subfolders, filenames in os.walk('user_detail'):
        with open('user-to-user.csv', 'w') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames=['username1', 'username2'])
            writer.writeheader()

            for filename in [ fi for fi in filenames if fi.endswith("_friends_resp") ]:
                with open('%s/%s' % (folderName, filename)) as file:
                    try:
                        obj = json.load(file)
                        for friend in obj["friends"]["user"]:
                                username = filename[:filename.find('_friends_resp')]
                                writer.writerow({'username1': username,
                                            'username2': friend['name']})
                    except:
                        print('error in friends of ', filename)

        with open('user-to-artist.csv', 'w') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames=['username', 'artist_mbid'])
            writer.writeheader()

            for filename in [ fi for fi in filenames if fi.endswith("_top_artists") ]:
                with open('%s/%s' % (folderName, filename)) as file:
                    try:
                        obj = json.load(file)
                        for artist in obj["topartists"]["artist"]:
                            if 'mbid' in artist and artist['mbid'] != '':
                                username = filename[:filename.find('_top_artists')]
                                writer.writerow({'username': username,
                                                    'artist_mbid': artist['mbid']})
                    except:
                        print('error in artist of ', filename)

        with open('user-to-track.csv', 'w') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames=['username', 'track_mbid'])
            writer.writeheader()

            for filename in [ fi for fi in filenames if fi.endswith("_loved_tracks") ]:
                with open('%s/%s' % (folderName, filename)) as file:
                    try:
                        obj = json.load(file)
                        for track in obj["lovedtracks"]["track"]:
                            if 'mbid' in track and track['mbid'] != '':
                                username = filename[:filename.find('_loved_tracks')]
                                writer.writerow({'username': username,
                                                'track_mbid': track['mbid']})
                    except:
                        print('error in tracks of ', filename)

            for filename in [ fi for fi in filenames if fi.endswith("_recent_tracks") ]:
                with open('%s/%s' % (folderName, filename)) as file:
                    try:
                        obj = json.load(file)
                        for track in obj["recenttracks"]["track"]:
                            if 'mbid' in track and track['mbid'] != '':
                                username = filename[:filename.find('_recent_tracks')]
                                writer.writerow({'username': username,
                                                'track_mbid': track['mbid']})
                    except:
                        print('error in tracks of ', filename)

            for filename in [ fi for fi in filenames if fi.endswith("_top_tracks") ]:
                with open('%s/%s' % (folderName, filename)) as file:
                    try:
                        obj = json.load(file)
                        for track in obj["toptracks"]["track"]:
                            if 'mbid' in track and track['mbid'] != '':
                                username = filename[:filename.find('_top_tracks')]
                                writer.writerow({'username': username,
                                                'track_mbid': track['mbid']})
                    except:
                        print('error in tracks of ', filename)

        with open('user-to-album.csv', 'w') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames=['username', 'album_mbid'])
            writer.writeheader()

            for filename in [ fi for fi in filenames if fi.endswith("_top_albums") ]:
                with open('%s/%s' % (folderName, filename)) as file:
                    try:
                        obj = json.load(file)
                        for album in obj["topalbums"]["album"]:
                            if 'mbid' in album and album['mbid'] != '':
                                username = filename[:filename.find('_top_albums')]
                                writer.writerow({'username': username,
                                                'album_mbid': album['mbid']})
                    except:
                        print('error in albums of ', filename)

        with open('user-info.csv', 'w') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames=['username', 'gender', 'country', 'image'])
            writer.writeheader()

            for filename in [ fi for fi in filenames if fi.endswith("_info") ]:
                with open('%s/%s' % (folderName, filename)) as file:
                    try:
                        obj = json.load(file)
                        username = filename[:filename.find('_info')]
                        writer.writerow({'username': username,
                                        'gender': obj["user"]['gender'],
                                        'country': obj["user"]['country'],
                                        'image': obj["user"]['image'][2]['#text']})
                    except:
                        print('error in user-info of ', filename)


main()

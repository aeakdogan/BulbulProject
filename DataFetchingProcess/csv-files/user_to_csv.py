import json, csv
import os


def main():
    for folderName, subfolders, filenames in os.walk('../user_detail'):
        with open('../generated_csv/user-to-user.csv', 'w') as csvfile:
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

        with open('../generated_csv/user-to-artist.csv', 'w') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames=['username', 'artist_mbid', 'play_count'])
            writer.writeheader()

            for filename in [ fi for fi in filenames if fi.endswith("_top_artists") ]:
                with open('%s/%s' % (folderName, filename)) as file:
                    try:
                        obj = json.load(file)
                        for artist in obj["topartists"]["artist"]:
                            if 'mbid' in artist and artist['mbid'] != '':
                                username = filename[:filename.find('_top_artists')]
                                writer.writerow({'username': username,
                                                'artist_mbid': artist['mbid'],
                                                 'play_count': artist.get('playcount', 0)})
                    except:
                        print('error in artist of ', filename)

        user_to_track = {}
        with open('../generated_csv/user-to-track.csv', 'w') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames=['username', 'track_mbid', 'loved',
                                                         'play_count', 'rating'])
            writer.writeheader()

            for filename in [ fi for fi in filenames if fi.endswith("_loved_tracks") ]:
                with open('%s/%s' % (folderName, filename)) as file:
                    try:
                        obj = json.load(file)
                        for track in obj["lovedtracks"]["track"]:
                            mbid = track.get('mbid', None)
                            username = filename[:filename.find('_loved_tracks')]
                            if mbid not in [None, '']:
                                if username in user_to_track:
                                    # if this track was added before, only add related field
                                    if mbid in user_to_track[username]:
                                        user_to_track[username][mbid]['loved'] = True
                                    else: # put the track with its loved attr. to user for the first time
                                        user_to_track[username][mbid] = {'loved': True, 'rating': -1, 'play_count': 0}
                                else:  # if there hasn't been any tracks related to that user, init inner dict for user to track
                                    user_to_track[username] = {}
                                    user_to_track[username][mbid] = {'loved': True, 'rating': -1, 'play_count': 0}
                            else:
                                continue  # skip if mbid is empty
                    except:
                        print('error in tracks of ', filename)

            for filename in [ fi for fi in filenames if fi.endswith("_recent_tracks") ]:
                with open('%s/%s' % (folderName, filename)) as file:
                    try:
                        obj = json.load(file)
                        for track in obj["recenttracks"]["track"]:
                            mbid = track.get('mbid', None)
                            username = filename[:filename.find('_recent_tracks')]
                            if mbid not in [None, '']:
                                if username in user_to_track:
                                    # if this track was added before, only add related field
                                    if mbid in user_to_track[username]:
                                        user_to_track[username][mbid]['play_count'] = 0
                                    else:  # put the track to user for the first time
                                        user_to_track[username][mbid] = {'loved': False, 'rating': -1, 'play_count': 0}
                                else:  # if there hasn't been any tracks related to that user, init inner dict for user to track
                                    user_to_track[username] = {}
                                    user_to_track[username][mbid] = {'loved': False, 'rating': -1, 'play_count': 0}
                            else:
                                continue  # skip if mbid is empty
                    except:
                        print('error in tracks of ', filename)

            for filename in [ fi for fi in filenames if fi.endswith("_top_tracks") ]:
                with open('%s/%s' % (folderName, filename)) as file:
                    try:
                        obj = json.load(file)
                        for track in obj["toptracks"]["track"]:
                            mbid = track.get('mbid', None)

                            username = filename[:filename.find('_top_tracks')]
                            playcount = track.get('playcount', 0)
                            if mbid not in [None, '']:
                                if username in user_to_track:
                                    # if this track was added before, only add related field
                                    if mbid in user_to_track[username]:
                                        user_to_track[username][mbid]['play_count'] = playcount
                                    else:  # put the track to user for the first time
                                        user_to_track[username][mbid] = {'loved': False, 'rating': -1, 'play_count': playcount}
                                else:  # if there hasn't been any tracks related to that user, init inner dict for user to track
                                    user_to_track[username] = {}
                                    user_to_track[username][mbid] = {'loved': False, 'rating': -1, 'play_count': playcount}
                            else:
                                continue  # skip if mbid is empty
                    except:
                        print('error in tracks of ', filename)

            # =['username', 'track_mbid', 'loved',
            #   'play_count', 'rating'])
            flattened = []
            for username, tracks_dict in user_to_track.items():
                for mbid, track_fields in tracks_dict.items():
                    obj = {'username': username, 'track_mbid': mbid}
                    obj.update(track_fields)
                    flattened.append(obj)

            writer.writerows(flattened)

        with open('../generated_csv/user-to-album.csv', 'w') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames=['username', 'album_mbid', 'play_count'])
            writer.writeheader()

            for filename in [ fi for fi in filenames if fi.endswith("_top_albums") ]:
                with open('%s/%s' % (folderName, filename)) as file:
                    try:
                        obj = json.load(file)
                        for album in obj["topalbums"]["album"]:
                            if 'mbid' in album and album['mbid'] != '':
                                username = filename[:filename.find('_top_albums')]
                                writer.writerow({'username': username,
                                                'album_mbid': album['mbid'],
                                                 'play_count': album.get('playcount', 0)})
                    except:
                        print('error in albums of ', filename)

        with open('../generated_csv/user-info.csv', 'w') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames=['username', 'gender', 'country', 'image',
                                                         'age', 'play_count','password'])
            writer.writeheader()

            for filename in [ fi for fi in filenames if fi.endswith("_info") ]:
                with open('%s/%s' % (folderName, filename)) as file:
                    try:
                        obj = json.load(file)
                        username = filename[:filename.find('_info')]
                        writer.writerow({'username': username,
                                        'gender': obj["user"]['gender'],
                                        'country': obj["user"]['country'],
                                        'image': obj["user"]['image'][2]['#text'],
                                        'age': obj['user']['age'],
                                        'play_count': obj['user']['playcount'],
                                        'password': '123456'})
                    except Exception as e:
                        print('error in user-info of ', filename)


main()


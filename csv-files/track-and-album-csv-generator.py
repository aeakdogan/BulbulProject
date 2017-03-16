import json, csv
import os

# ALI BURAK ERDOGAN - 14 MARCH 2017
# WARNING: RUN THIS SCRIPT IN 'Data' FOLDER

def main():
    generate_track_related_csv_files()
    generate_artist_related_csv_files()


def generate_track_related_csv_files():
    album_artist = {} # dictionary that matches album mbid to artist mbid, respectively
    album_song = {} # dictionary that matches album mbid to array of song ids
    albums_meta = {} # keeps metadata of albums, fetching from track info

    # FIRST TRAVERSE ALL TRACKS IN track_info FOLDER AND GET REQUIRED INFO
    inserted_rows = [] # FOR SPEEDING UP WITH BATCH INSERTION
    for folderName, subfolders, filenames in os.walk('track_info'):
        for filename in filenames:
            # OPEN THE ALBUM INFO FILE
            with open('%s/%s' % (folderName, filename)) as file:
                obj = json.load(file)

                if 'track' in obj:
                    inserted = { field: obj['track'][field] for field in ['mbid', 'name', 'duration']}
                    inserted['lastfm_url'] = obj['track']['url']

                    # APPEND THE OBJECT TO BATCH
                    inserted_rows.append(inserted)

                    # FOR ALBUM-TRACK RELATIONS
                    album_mbid = obj['track']['album']['mbid']
                    if album_mbid in album_song and type(album_song[album_mbid]) == list :
                        album_song[album_mbid].append(obj['track']['mbid'])
                    else:
                        album_song[album_mbid] = [obj['track']['mbid']]

                    # FOR KEEPING ALBUM METADATA
                    if album_mbid not in albums_meta:
                        albums_meta[album_mbid] = {
                            'mbid': album_mbid,
                            'name': obj['track']['album']['title'],
                            'image': obj['track']['album']['image'][2]['#text'],
                            'lastfm_url': obj['track']['album']['url'],
                        }

                    # FOR KEEPING ALBUM ARTIST RELATIONS
                    # i'm assuming that artist name field and album artist fields are consistent
                    if obj['track']['artist']['name'] == obj['track']['album']['artist']:
                        artist_mbid = obj['track']['artist']['mbid']
                        # dictionary that matches album mbid to artist mbid
                        album_artist[obj['track']['album']['mbid']] = artist_mbid
                    else:
                        raise Exception('artist name field and album artist fields are NOT consistent')

                else:
                    raise Exception('No track field')

    # FETCH ALL SPOTIFY MATCHES FROM FILE
    with open('matched_lastfm_spotify_ids.txt') as spotifile:
        # Read the spotify ids of the tracks and put into list
        content = spotifile.read()
        spotify_matches = [tuple(line.split(' <-> ')) for line in content.splitlines()]
        spotify_matches = {x: y for x, y in spotify_matches if y != 'None'} # LASTFM->SPOTIFY

    # IF A TRACK'S SPOTIFY INFO EXISTS, APPEND ADDITIONAL INFO TO OUR OBJECT
    for index, track_obj in enumerate(inserted_rows):
        if inserted_rows[index]['mbid'] in spotify_matches: # do hashtable search
            spotify_id = spotify_matches[inserted_rows[index]['mbid']]
            try:
                # OPEN BOTH SPOTIFY METADATA AND AUDIO FEATURE FILES
                with open('spotify_tracks/%s_spotify.txt' % spotify_id, 'r' ) as spotifile:
                    obj = json.load(spotifile)
                    additional_data = {
                                        'spotify_artist_id': obj['artists'][0]['id'],
                                        'spotify_artist_url': obj['artists'][0]['href'],
                                        'spotify_album_id': obj['album']['id'],
                                        'spotify_album_url': obj['album']['href'],
                                        'spotify_album_img': obj['album']['images'][0]['url'],
                                        'spotify_track_id': obj['id'],
                                        'spotify_track_url': obj['href'],
                                        'spotify_track_preview_url': obj['preview_url'],
                                        'spotify_track_popularity': obj['popularity']}

                    inserted_rows[index].update(additional_data)
            except FileNotFoundError:
                print('spotify metadata file of %s not found' % spotify_id)
                additional_data = {'spotify_artist_id': '', 'spotify_artist_url': '', 'spotify_album_id': '',
                                    'spotify_album_url': '', 'spotify_album_img': '', 'spotify_track_id': '', 'spotify_track_url': '',
                                    'spotify_track_preview_url': '', 'spotify_track_popularity': ''}
                # APPEND EMPTY DATA FOR CONSISTENT FIELDS
                inserted_rows[index].update(additional_data)

            try:
                with open('track_audio_features/%s_audio_features.txt' % spotify_id, 'r' ) as feature_file:
                    feature_obj = json.load(feature_file)
                    additional_data = {"audio_features_danceability" : feature_obj['danceability'],
                                        "audio_features_energy" : feature_obj['energy'],
                                        "audio_features_key" : feature_obj['key'],
                                        "audio_features_loudness" : feature_obj['loudness'],
                                        "audio_features_mode" : feature_obj['mode'],
                                        "audio_features_speechiness" : feature_obj['speechiness'],
                                        "audio_features_acousticness" : feature_obj['acousticness'],
                                        "audio_features_instrumentalness" : feature_obj['instrumentalness'],
                                        "audio_features_liveness" : feature_obj['liveness'],
                                        "audio_features_valence" : feature_obj['valence'],
                                        "audio_features_tempo" : feature_obj['tempo'],
                                        "audio_features_analysis_url" : feature_obj['analysis_url'],
                                        "audio_features_duration_ms" : feature_obj['duration_ms'],
                                        "audio_features_time_signature" : feature_obj['time_signature']
                                       }
                    inserted_rows[index].update(additional_data)
            except FileNotFoundError:
                print('spotify file of %s not found' % spotify_id)
                additional_data = {'spotify_artist_id': '', 'spotify_artist_url': '', 'spotify_album_id': '',
                                    'spotify_album_url': '', 'spotify_album_img': '', 'spotify_track_id': '', 'spotify_track_url': '',
                                    'spotify_track_preview_url': '', 'spotify_track_popularity': ''}
                # APPEND EMPTY DATA FOR CONSISTENT FIELDS
                inserted_rows[index].update(additional_data)

    with open('tracks.csv', 'w') as csvfile:
        fieldnames = ['mbid', 'name', 'duration', ]
        custom_fieldnames = ['lastfm_url',  'spotify_artist_url',
                             'spotify_artist_id', 'spotify_album_id',
                             'spotify_album_url', 'spotify_album_img',
                             'spotify_track_id', 'spotify_track_url',
                             'spotify_track_preview_url', 'spotify_track_popularity',
                            "audio_features_danceability", "audio_features_energy", "audio_features_key", "audio_features_loudness",
                               "audio_features_mode", "audio_features_speechiness", "audio_features_acousticness",
                               "audio_features_instrumentalness", "audio_features_liveness", "audio_features_valence",
                               "audio_features_tempo", "audio_features_analysis_url", "audio_features_duration_ms",
                               "audio_features_time_signature"]

        writer = csv.DictWriter(csvfile, fieldnames=fieldnames+custom_fieldnames)

        writer.writeheader()
        writer.writerows(inserted_rows) # PERFORM BATCH INSERTION

    with open('album-artist.csv', 'w') as csvfile:
        fieldnames = ['album_mbid', 'artist_mbid',]
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()

        inserted_rows = []
        for key, val in album_artist.items():
            inserted_rows.append({'album_mbid': key, 'artist_mbid': val})

        writer.writerows(inserted_rows) # PERFORM BATCH INSERTION

    with open('album-track.csv', 'w') as csvfile:
        fieldnames = ['album_mbid', 'track_mbid',]
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()

        inserted_rows = []
        for album, tracks in album_song.items():
            for track in tracks:
                inserted_rows.append({'album_mbid': album, 'track_mbid': track})

        writer.writerows(inserted_rows) # PERFORM BATCH INSERTION

    with open('albums.csv', 'w') as csvfile:
        fieldnames = ['mbid', 'name', ]
        custom_fieldnames = ['image', 'lastfm_url']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames+custom_fieldnames)

        writer.writeheader()

        inserted_rows = []
        for key, val in albums_meta.items():
            inserted_rows.append({'mbid': key,
                            'name': val['name'],
                            'image': val['image'],
                            'lastfm_url': val['lastfm_url']})

        writer.writerows(inserted_rows) # PERFORM BATCH INSERTION

def generate_artist_related_csv_files():
    artist_tags = []

    with open('artists.csv', 'w') as csvfile:
        fieldnames = ['name', 'mbid',]
        custom_fieldnames = ['lastfm_url', 'image', 'listener_count', 'play_count', 'biography_text', 'biography_url']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames+custom_fieldnames)

        writer.writeheader()
        inserted_rows = []

        # TRAVERSE ARTIST INFO FILES
        for folderName, subfolders, filenames in os.walk('lastfm_artist_files'):
            for filename in filter(lambda fname: fname.find('artist_info') != -1, filenames): # only open artist_info files
                # OPEN THE ARTIST INFO FILE
                with open('%s/%s' % (folderName, filename)) as file:
                    obj = json.load(file)

                    if 'artist' in obj:
                        inserted = { field: obj['artist'][field] for field in fieldnames }
                        inserted['lastfm_url'] = obj['artist']['url']
                        inserted['image'] = obj['artist']['image'][2]['#text']
                        inserted['listener_count'] = obj['artist']['stats']['listeners']
                        inserted['play_count'] = obj['artist']['stats']['playcount']
                        inserted['biography_text'] = obj['artist']['bio']['content']
                        inserted['biography_url'] = obj['artist']['bio']['links']['link']['href']

                        for tag in obj['artist']['tags']['tag']:
                            artist_tags.append((obj['artist']['mbid'], tag))

                        # for similar in obj['artist']['similar']['artist']:
                        #     similar_artists.append((obj['artist']['mbid'], similar))

                        # ADD TO BATCH FOR LATER BATCH INSERTION
                        inserted_rows.append(inserted)

        writer.writerows(inserted_rows) # PERFORM BATCH INSERTION

        ####### DISCARDED SINCE SIMILARITY DOES NOT CONTAIN MBID
        # WRITE SIMILAR ARTISTS DATA
        # with open('similar-artists.csv', 'w') as csvfile:
        #     fieldnames = ['artist_mbid', 'similar_artist_',]
        #     writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        #
        #     writer.writeheader()
        #     for mbid, similar_artist in similar_artists:


        # WRITE ARTIST TAG DATA
        with open('artist-tags.csv', 'w') as csvfile:
            fieldnames = ['artist_mbid', 'tag_name', 'tag_url']
            writer = csv.DictWriter(csvfile, fieldnames=fieldnames)

            writer.writeheader()
            inserted_rows = []
            for mbid, tag in artist_tags:
                inserted_rows.append({'artist_mbid': mbid, 'tag_name': tag['name'], 'tag_url': tag['url']})

            writer.writerows(inserted_rows) # PERFORM BATCH INSERTION

main()

# def generate_album_csv_files():
#     album_artist = {} # dictionary that matches album mbid to artist mbid, respectively
#
#     with open('albums.csv', 'w') as csvfile:
#         fieldnames = ['mbid', 'name', 'artist', ]
#         custom_fieldnames = ['image', 'lastfm_url']
#         writer = csv.DictWriter(csvfile, fieldnames=fieldnames+custom_fieldnames)
#
#         writer.writeheader()
#
#         for folderName, subfolders, filenames in os.walk('lastfm_album_info'):
#             for filename in filenames:
#                 # OPEN THE ALBUM INFO FILE
#                 with open('%s/%s' % (folderName, filename)) as file:
#                     obj = json.load(file)
#
#                     if 'album' in obj:
#                         inserted = { field: obj['album'][field] for field in fieldnames }
#                         inserted['image'] = obj['album']['image'][2]['#text'] #select one image
#                         inserted['lastfm_url'] = obj['album']['url']
#
#                         # WRITE TO ALBUMS.CSV FILE
#                         writer.writerow(inserted)
#
#                         # i'm assuming that artist name field and album artist fields are consistent
#                         if obj['album']['tracks']['track'][0]['artist']['name'] == obj['album']['artist']:
#                             artist_mbid = obj['album']['tracks']['track'][0]['artist']['mbid']
#                             # dictionary that matches album mbid to artist mbid
#                             album_artist[obj['album']['mbid']] = artist_mbid
#                         else:
#                             raise Exception('artist name field and album artist fields are NOT consistent')
#                     else:
#                         raise Exception('No album field')
#
#     with open('album-artist.csv', 'w') as csvfile:
#         fieldnames = ['album_mbid', 'artist_mbid',]
#         writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
#         writer.writeheader()
#
#         for key, val in album_artist.items():
#             writer.writerow({'album_mbid': key, 'artist_mbid': val})

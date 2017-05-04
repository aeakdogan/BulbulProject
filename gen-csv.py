import json, csv
import os

def generate_album_csv_files():
    album_artist = {} # dictionary that matches album mbid to artist mbid, respectively

    with open('albums.csv', 'w') as csvfile:
        fieldnames = ['mbid', 'name', 'artist', ]
        custom_fieldnames = ['image', 'lastfm_url']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames+custom_fieldnames)

        writer.writeheader()

        for folderName, subfolders, filenames in os.walk('lastfm_album_info'):
            for filename in filenames:
                # OPEN THE ALBUM INFO FILE
                with open('%s/%s' % (folderName, filename)) as file:
                    obj = json.load(file)

                    if 'album' in obj:
                        inserted = { field: obj['album'][field] for field in fieldnames }
                        inserted['image'] = obj['album']['image'][2]['#text'] #select one image
                        inserted['lastfm_url'] = obj['album']['url']

                        # WRITE TO ALBUMS.CSV FILE
                        writer.writerow(inserted)

                        # i'm assuming that artist name field and album artist fields are consistent
                        if obj['album']['tracks']['track'][0]['artist']['name'] == obj['album']['artist']:
                            artist_mbid = obj['album']['tracks']['track'][0]['artist']['mbid']
                            # dictionary that matches album mbid to artist mbid
                            album_artist[obj['album']['mbid']] = artist_mbid
                        else:
                            raise Exception('artist name field and album artist fields are NOT consistent')
                    else:
                        raise Exception('No album field')

    with open('album-artist.csv', 'w') as csvfile:
        fieldnames = ['album_mbid', 'artist_mbid',]
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()

        for key, val in album_artist.items():
            writer.writerow({'album_mbid': key, 'artist_mbid': val})


def generate_track_csv_files():
    album_artist = {} # dictionary that matches album mbid to artist mbid, respectively
    album_song = {} # dictionary that matches album mbid to array of song ids
    albums_meta = {} # keeps metadata of albums, fetching from track info

    with open('tracks.csv', 'w') as csvfile:
        fieldnames = ['mbid', 'name', 'duration', 'url', ]
        custom_fieldnames = []
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames+custom_fieldnames)

        writer.writeheader()

        for folderName, subfolders, filenames in os.walk('track_info'):
            for filename in filenames:
                # OPEN THE ALBUM INFO FILE
                with open('%s/%s' % (folderName, filename)) as file:
                    obj = json.load(file)

                    if 'track' in obj:
                        inserted = { field: obj['track'][field] for field in fieldnames }

                        # WRITE TO ALBUMS.CSV FILE
                        writer.writerow(inserted)

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

    with open('album-artist.csv', 'w') as csvfile:
        fieldnames = ['album_mbid', 'artist_mbid',]
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()

        for key, val in album_artist.items():
            writer.writerow({'album_mbid': key, 'artist_mbid': val})

    with open('album-track.csv', 'w') as csvfile:
        fieldnames = ['album_mbid', 'track_mbid',]
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()

        for album, tracks in album_song.items():
            for track in tracks:
                writer.writerow({'album_mbid': album, 'track_mbid': track})

    with open('albums.csv', 'w') as csvfile:
        fieldnames = ['mbid', 'name', ]
        custom_fieldnames = ['image', 'lastfm_url']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames+custom_fieldnames)

        writer.writeheader()

        for key, val in albums_meta.items():
            writer.writerow({'mbid': key,
                            'name': val['name'],
                            'image': val['image'],
                            'lastfm_url': val['lastfm_url']})



def main():
    # generate_album_csv_files()
    generate_track_csv_files()

    # with open('sample-album.json') as file:
    #     obj = json.load(file)
    #     print(flattenjson(obj, '__'))
    #
    #     with open( 'sample-album.csv', 'wb' ) as out_file:
    #         csv_w = csv.writer( out_file )
    #         csv_w.writerow( columns )
    #
    #         for i_r in input:
    #             csv_w.writerow( map( lambda x: i_r.get( x, "" ), columns ) )

main()
from os import listdir
import json
import time
from math import *
import pickle
from i2itest import generate_umatrix_tmatrix


path = '../BulbulData/audio_features/'

# features = ['danceability', 'energy', 'key', 'loudness', 'mode', 'speechiness',
# 'acousticness', 'instrumentalness', 'liveness', 'valence', 'tempo']
features = ['danceability', 'energy', 'speechiness', 'acousticness', 'instrumentalness', 'liveness', 'valence', 'tempo']

def scale_feature(feature_type, value):
    if feature_type == 'key':
        return value / 11
    elif feature_type == 'loudness':
        return value / -60
    elif feature_type == 'tempo':
        return value / 250
    elif feature_type == 'speechiness':
        if value > 0.3:
            return 1
        else:
            return value / 0.3
    return value


# user_item_vector[feature] = feature value
# filtered_item_vector[feature] = feature value
def cosine_sim(user_item_vector, filtered_item_vector):
    numerator = 0
    for feature in features:
        if user_item_vector[feature] is not None and  filtered_item_vector[feature] is not None:
            numerator += scale_feature(feature, user_item_vector[feature]) * scale_feature(feature, filtered_item_vector[feature])

    denominator = 0
    comp_a, comp_b = 0, 0
    for f in features:
        if user_item_vector[feature] is not None and  filtered_item_vector[feature] is not None:
            comp_a += scale_feature(feature, user_item_vector[f]) * scale_feature(feature, user_item_vector[f])
    comp_a = sqrt(comp_a)
    for f in features:
        if user_item_vector[feature] is not None and  filtered_item_vector[feature] is not None:
            comp_b += scale_feature(feature, filtered_item_vector[f]) * scale_feature(feature, filtered_item_vector[f])
    comp_b = sqrt(comp_b)
    denominator = comp_a * comp_b
    return float(numerator) / denominator

def euclidean_sim(user_item_vector, filtered_item_vector):
    distance = 0
    for feature in features:
        if user_item_vector[feature] is not None and  filtered_item_vector[feature] is not None:
            distance += (scale_feature(feature, user_item_vector[feature]) - scale_feature(feature, filtered_item_vector[feature])) ** 2
    distance = 1 / sqrt(distance)
    print('distance: ', distance)
    return distance

def convert_key_to_tuple(key):
    first = key.split(',')[0][1:]
    second = key.split(',')[1][:-1]
    return (first, second)

def get_mbid_spotify_match():
    if 'mbid_spotifyId_match' in listdir('.'):
        mbid_spotifyId_match = pickle.load( open( 'mbid_spotifyId_match', 'rb' ) )
    else:
        mbid_spotifyId_match = {}
        with open('all_matched_lastfm_spotify.txt', 'r') as f:
            for line in f:
                mbid_spotifyId_match[line.split(' <-> ')[0]] = line.split(' <-> ')[1][:-1]
        pickle.dump( mbid_spotifyId_match, open( 'mbid_spotifyId_match', 'wb' ) )
    return mbid_spotifyId_match

def get_spotifyid_mbid_match():
    if 'spotifyId_mbid_match' in listdir('.'):
        spotifyId_mbid_match = pickle.load( open( 'spotifyId_mbid_match', 'rb' ) )
    else:
        spotifyId_mbid_match = {}
        with open('all_matched_lastfm_spotify.txt', 'r') as f:
            for line in f:
                if line.split(' <-> ')[1] is not 'None':
                    spotifyId_mbid_match[line.split(' <-> ')[1][:-1]] = line.split(' <-> ')[0]
        pickle.dump( spotifyId_mbid_match, open( 'spotifyId_mbid_match', 'wb' ) )
    return spotifyId_mbid_match

# user_tracks: track mbid list which user rated
# filtered_tracks: track mbid list which
def get_similar_tracks(user_tracks, filtered_tracks):
    mbid_spotifyId_match = get_mbid_spotify_match()
    spotifyId_mbid_match = get_spotifyid_mbid_match()

    if 'spotify_features_id' in listdir('.'):
        spotify_features_id = pickle.load( open( 'spotify_features_id', 'rb' ) )
    else:
        spotify_features_id = []
        for feature in listdir(path):
            spotify_features_id.append(feature.split('_audioFeatures')[0])
        pickle.dump( spotify_features_id, open( 'spotify_features_id', 'wb' ) )

    # Change mbid with spotify id
    print('Change mbid with spotify id')
    for idx, mbid in enumerate(filtered_tracks):
        if mbid in mbid_spotifyId_match:
            filtered_tracks[idx] = mbid_spotifyId_match[mbid]

    for idx, mbid in enumerate(user_tracks):
        if mbid in mbid_spotifyId_match:
            user_tracks[idx] = mbid_spotifyId_match[mbid]

    print('user tracks spotify - filtered_tracks_spotify')
    user_tracks_spotify = [x for x in user_tracks if x in spotify_features_id]
    filtered_tracks_spotify = [x for x in filtered_tracks if x in spotify_features_id]

    print('Started user_spotify_features generation')
    # user_spotify_features[t][i] = data[i] : t = track id, i=feature name, data[i]: feature value
    user_spotify_features = {}
    filtered_spotify_features = {}
    for t in user_tracks_spotify:
        user_spotify_features[t] = {}
        with open('{}{}_audioFeatures'.format(path, t), 'r') as f:
            data = json.load(f)
            for i in features:
                if i in data and data[i] is not None:
                    user_spotify_features[t][i] = float(data[i])
                else:
                    user_spotify_features[t][i] = None
    print('Finished user_spotify_features generation')

    print('Started filtered_spotify_features generation')
    for t in filtered_tracks_spotify:
        filtered_spotify_features[t] = {}
        print(idx)
        idx += 1
        with open('{}{}_audioFeatures'.format(path, t), 'r') as f:
            data = json.load(f)
            for i in features:
                if i in data and data[i] is not None:
                    filtered_spotify_features[t][i] = float(data[i])
                else:
                    filtered_spotify_features[t][i] = None
    print('Finished filtered_spotify_features generation')

    # Calculate the similarities
    similarities = {}
    for i in user_spotify_features.keys():
        for j in filtered_spotify_features.keys():
            key = '({},{})'.format(i, j)
            sim = euclidean_sim(user_spotify_features[i], filtered_spotify_features[j])
            similarities[key] = sim

    sorted_similarities = sorted(similarities.items(), key=lambda x: x[1], reverse=True)

    x = 0
    for items in sorted_similarities:
        # print('items: ', spotifyId_mbid_match[convert_key_to_tuple(items[0])[0]], spotifyId_mbid_match[convert_key_to_tuple(items[0])[1]])
        user_t = spotifyId_mbid_match[convert_key_to_tuple(items[0])[0]]
        if user_t in umatrix['omerfs']:
            print(user_t, umatrix['omerfs'][user_t])
            print('Spotify Ids: ', items[0])
            print('similarity: ', items[1])
        print('----------------------------------')
        if x == 100:
            break
        x += 1


username_input = 'omerfs'
user_tracks = ["0a950f6b-20c1-461a-8385-8335d5f2668a", "176d887e-054e-4a48-b8fb-9d5614372f20", "11b7c3d2-8a49-4812-95dc-aef93c4cec37", "0afdf0bb-cf31-456b-814e-afc42b26da4b", "f1e57531-e0df-4b3e-938f-1ae30c5b1a11"]
umatrix, tmatrix = generate_umatrix_tmatrix()
for i in user_tracks:
    if i in umatrix[username_input]:
        del umatrix[username_input][i]
        del tmatrix[i][username_input]
# filtered_tracks = list(umatrix['omerfs'].keys())
filtered_tracks = list(tmatrix.keys())[:5000]
umatrix, tmatrix = None, None
umatrix, tmatrix = generate_umatrix_tmatrix()
a = time.time()
get_similar_tracks(user_tracks, filtered_tracks)
print('Time: ', time.time() - a)

from os import listdir
import json
import time
from math import *
import pickle
from i2itest import generate_umatrix_tmatrix


path = '../BulbulData/audio_features/'
storage = './content_based_storage/'

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

	if sqrt(distance):
		distance = 1 / sqrt(distance)
	else:
		distance = 0
	return distance

def convert_key_to_tuple(key):
	first = key.split(',')[0][1:]
	second = key.split(',')[1][:-1]
	return (first, second)

def get_mbid_spotify_match():
	if 'mbid_spotifyId_match' in listdir(storage):
		mbid_spotifyId_match = pickle.load( open( storage + 'mbid_spotifyId_match', 'rb' ) )
	else:
		mbid_spotifyId_match = {}
		with open('all_matched_lastfm_spotify.txt', 'r') as f:
			for line in f:
				mbid_spotifyId_match[line.split(' <-> ')[0]] = line.split(' <-> ')[1][:-1]
		pickle.dump( mbid_spotifyId_match, open( storage + 'mbid_spotifyId_match', 'wb+' ) )
	return mbid_spotifyId_match

def get_spotifyid_mbid_match():
	if 'spotifyId_mbid_match' in listdir(storage):
		spotifyId_mbid_match = pickle.load( open( storage + 'spotifyId_mbid_match', 'rb' ) )
	else:
		spotifyId_mbid_match = {}
		with open('all_matched_lastfm_spotify.txt', 'r') as f:
			for line in f:
				if line.split(' <-> ')[1] is not 'None':
					spotifyId_mbid_match[line.split(' <-> ')[1][:-1]] = line.split(' <-> ')[0]
		pickle.dump( spotifyId_mbid_match, open( storage + 'spotifyId_mbid_match', 'wb+' ) )
	return spotifyId_mbid_match


def get_spotify_features_id():
	if 'spotify_features_id' in listdir(storage):
		spotify_features_id = pickle.load( open( storage + 'spotify_features_id', 'rb' ) )
	else:
		spotify_features_id = []
		for feature in listdir(path):
			spotify_features_id.append(feature.split('_audioFeatures')[0])
		pickle.dump( spotify_features_id, open( storage + 'spotify_features_id', 'wb+' ) )

	return spotify_features_id


# user_tracks: track mbid list which user rated
# filtered_tracks: track mbid list which is sent as filtered
def get_similar_tracks(rated_songs, filtered_tracks, N, mbid2spotifyid=0, spotifyid2mbid=0, features_id=0):
	a1 = time.time()
	mbid_spotifyId_match = mbid2spotifyid #get_mbid_spotify_match(mbid2spotifyid)
	spotifyId_mbid_match = spotifyid2mbid #get_spotifyid_mbid_match(spotifyid2mbid)
	print('Match matrices loading time: ', time.time() - a1, ' secs!')

	a2 = time.time()
	user_tracks = [x[0] for x in rated_songs]
	print('User tracks generating time: ', time.time() - a2, ' secs!')

	a3 = time.time()
	spotify_features_id = features_id
	print('Spotify features id loading time: ', time.time() - a3, ' secs!')

	a4 = time.time()
	# Change mbid with spotify id
	print('Change mbid with spotify id')
	for idx, mbid in enumerate(filtered_tracks):
		if mbid in mbid_spotifyId_match:
			filtered_tracks[idx] = mbid_spotifyId_match[mbid]
	print('Change mbid with spotify id time: ', time.time() - a4, ' secs!')

	a5 = time.time()
	# also generate user tracks ratings.
	spotify_ratings = {}
	for idx, mbid in enumerate(user_tracks):
		if mbid in mbid_spotifyId_match:
			user_tracks[idx] = mbid_spotifyId_match[mbid]
			spotify_ratings[mbid_spotifyId_match[mbid]] = rated_songs[idx][1]

	#print('sporify matched and ranked: ', list(spotify_ratings.items())[:10])
	print('Spotify ratings and user tracks generate:', time.time() - a5, ' secs!')

	a6 = time.time()
	user_tracks_spotify = list(set(user_tracks).intersection(set(spotify_features_id)))
	filtered_tracks_spotify = list(set(filtered_tracks).intersection(set(spotify_features_id)))
	print('user_tracks_spotify and filtered_tracks_spotify time: ', time.time() - a6, ' secs!')
	# print('user tracks spotify - filtered_tracks_spotify')
	# user_tracks_spotify = [x for x in user_tracks if x in spotify_features_id]
	# filtered_tracks_spotify = [x for x in filtered_tracks if x in spotify_features_id]


	a7 = time.time()
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
	print('Started user_spotify_features generation time: ', time.time() - a7, ' secs!')

	a8 = time.time()
	print('Started filtered_spotify_features generation')
	for t in filtered_tracks_spotify:
		filtered_spotify_features[t] = {}
		with open('{}{}_audioFeatures'.format(path, t), 'r') as f:
			data = json.load(f)
			for i in features:
				if i in data and data[i] is not None:
					filtered_spotify_features[t][i] = float(data[i])
				else:
					filtered_spotify_features[t][i] = None
	print('Finished filtered_spotify_features generation')
	print('Started filtered_spotify_features generation: ', time.time() - a8, ' secs!')

	a9 = time.time()
	# Calculate the similarities
	similarities = []
	for i in user_spotify_features.keys():
		for j in filtered_spotify_features.keys():
			key = '({},{})'.format(i, j)
			sim = euclidean_sim(user_spotify_features[i], filtered_spotify_features[j]) * spotify_ratings[i] #we should add the rating of the song to this.
			similarities.append((key, sim))
	print('Calculate the similarities time: ', time.time() - a9, ' secs!')

	a10 = time.time()
	#sorted_similarities = sorted(similarities.items(), key=lambda x: x[1], reverse=True)
	similarities.sort(key = lambda x : x[1], reverse = True)
	print('Sort similarities: ', time.time() - a10, ' secs!')
	return [(spotifyId_mbid_match[convert_key_to_tuple(x[0])[1]], x[1]) for x in similarities][:N]

	#print('sorted similarities size: ', len(sorted_similarities))
	#print('len:: ', len(dict([(spotifyId_mbid_match[convert_key_to_tuple(x[0])[1]], x[1]) for x in sorted_similarities][:N])))
	#return dict([(spotifyId_mbid_match[convert_key_to_tuple(x[0])[1]], x[1]) for x in sorted_similarities][:N])


def get_recommendations_outside(mode, username, rated_songs, filtered_songs, N, umatrix, tmatrix, l, sim_matrix, mbid2spotifyid=0, spotifyid2mbid=0, spotify_features_id=0):
	a = time.time()
	sim_tracks_resp = get_similar_tracks(rated_songs, filtered_songs, N, mbid2spotifyid, spotifyid2mbid, spotify_features_id)
	print('TYPE OF DICTIONARY: ', type(sim_tracks_resp))
	print('CONTENT BASED TIME: ', time.time() - a, ' secs')
	# print(sorted(sim_tracks_resp.items(), key=lambda x: x[1], reverse = True))
	print('Similar tracks response: ', len(sim_tracks_resp))
	return sim_tracks_resp

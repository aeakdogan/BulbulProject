#item to item collaborative filtering.
#authors mesut, omer
#############################
# TO DO LIST
# 	- Eliminate songs that are not similar during rating calculation
#
#############################


from os import listdir
import os
import numpy as np
import json
import pickle
import time
import math
import sys

def generate_umatrix_tmatrix():
	path = '/root/Bulbul/BulbulData/user_detail/'
	storage_path = '/root/Bulbul/brain/binary_recommendation/matrix_storage/'


	if os.path.isfile( storage_path + 'umatrix.txt'):
		if os.path.isfile( storage_path + 'tmatrix.txt'):
			if os.path.isfile( storage_path + 'lmatrix.txt'):

				umatrix = pickle.load(open( storage_path + 'umatrix.txt', 'rb'))
				tmatrix = pickle.load(open( storage_path + 'tmatrix.txt', 'rb'))
				lmatrix = pickle.load(open( storage_path + 'lmatrix.txt', 'rb'))

				return umatrix, tmatrix

	top_tracks = []
	for i, item in enumerate(listdir(path)):
		if 'top_tracks' in item:
			top_tracks.append(item)

	loved_tracks = []
	for i, item in enumerate(listdir(path)):
		if 'loved_tracks' in item:
			loved_tracks.append(item)

	errcnt = 0
	umatrix = {} # umatrix[username][mbid] = playcount
	tmatrix = {} # tmatrix[mbid][username] = palycount
	lmatrix = {} # lmatrix[username] = loved tracks mbid list

	for loved in loved_tracks:
		f = open(path + loved)
		data = json.loads(f.read())
		f.close()
		# Generate lmatrix
		user = loved.split('_loved_tracks')[0]
		lmatrix[user] = []
		for track_no in range(len(data['lovedtracks']['track'])):
			lmatrix[user].append(data['lovedtracks']['track'][track_no]['mbid'])

	cnt = 0
	for top in top_tracks:
		print('cnt: ', cnt)
		cnt += 1
		f = open(path + top)
		data = json.loads(f.read())
		f.close()
		username = top.split('_top_tracks')[0]
		# Generate umatrix
		try:
			top = top.split('_top_tracks')[0]
			tracks = {}
			countlist = []
			# Finding min and max playcount values for rating scaling
			for count in range(len(data['toptracks']['track'])):
				if data['toptracks']['track'][count]['mbid'] is not None and data['toptracks']['track'][count]['mbid'].strip():
					countlist.append(float(data['toptracks']['track'][count]['playcount']))
			# If list is empty continue
			if not countlist:
				continue
			maxval = max(countlist)
			minval = min(countlist)

			try:
				# total_playcount = sum(countlist)
				# average_playcount = total_playcount / len(countlist)
				median = sorted(countlist)[int(len(countlist)/2)]
			except ZeroDivisionError as e:
				print('Zero divison error druing average playcount: ', str(e))
				average_playcount = 0

			#Generate umatrix
			for count in range(len(data['toptracks']['track'])):
				if data['toptracks']['track'][count]['mbid'] is not None and data['toptracks']['track'][count]['mbid'].strip():
					if data['toptracks']['track'][count]['mbid'] in lmatrix[username]:
						rating = 1
					elif minval == 1 and maxval == 1:
						rating = 0
					else:
						playcount = float(data['toptracks']['track'][count]['playcount'])
						rating = 1 if playcount > median else 0
					tracks[data['toptracks']['track'][count]['mbid']] = rating
		except ZeroDivisionError as e:
			print('Got exception during umatrix: ', str(e))
			# print(countlist)
			errcnt += 1
		except KeyError as e:
			print('Got exception during umatrix: ', str(e))
		umatrix[top] = tracks

	# Generate tmatrix
	for uname in umatrix.items():
		for mbid in uname[1].keys():
			tmatrix[mbid] = {}

	for uname in umatrix.items():
		name = uname[0]
		for mbid in uname[1].keys():
			tmatrix[mbid][name] =  umatrix[name][mbid]

	pickle.dump(umatrix, open( storage_path + 'umatrix.txt', 'wb+'))
	pickle.dump(tmatrix, open( storage_path + 'tmatrix.txt', 'wb+'))
	pickle.dump(lmatrix, open( storage_path + 'lmatrix.txt', 'wb+'))

	return umatrix, tmatrix

def calculate_overall_mean(umatrix):
	# Calculate overall rating mean
	counter = 0
	total_rating = 0
	for uname in umatrix.items():
		for mbid in uname[1]:
			total_rating += (float)(umatrix[uname[0]][mbid])
			counter += 1
	overall_mean = float(total_rating)/counter
	return overall_mean

def calculate_baseline(music_id, username, umatrix, tmatrix, overall_mean):
	# Calculate baseline estimate:
	# calculate Rating deviation of user x
	user_x = username
	tracks = umatrix[user_x]
	sum_of_ratings_x = 0
	for t in tracks.items():
		sum_of_ratings_x += t[1]
	average_rating_of_user_x = float(sum_of_ratings_x) / len(tracks)
	rating_daviation_of_user_x = average_rating_of_user_x - overall_mean

	# calculate the rating deviation of music i
	music_i = music_id
	music = tmatrix[music_i]
	sum_of_music_ratings_i = 0
	for m in music.items():
		sum_of_music_ratings_i += m[1]
	average_rating_of_music_i = float(sum_of_music_ratings_i) / len(music)
	rating_daviation_of_music_i = average_rating_of_music_i - overall_mean

	# baseline estimate of music i for user x
	baseline_estimate = overall_mean + rating_daviation_of_user_x + rating_daviation_of_music_i

	return baseline_estimate

# userlist: complete list of all users in a paticular ordeer
# music_id the mbid of the mtrack to vectorize
def create_music_vector(userlist, music_id, tmatrix):
	myratings = tmatrix[music_id]
	myvector = [0] * len(userlist)

	for username in myratings:
		myvector[userlist[username]] = myratings[username]

	return myvector

def quick_cosine_sim(tmatrix, music_id, target_track_id):
	target_track_ratings = tmatrix[target_track_id]
	current_track_ratings = tmatrix[music_id]

	common_usernames = list(set(target_track_ratings.keys()).intersection(set(current_track_ratings.keys())))

	#numerator of the cosine sim formula
	tot_sum = 0
	for comu in common_usernames:
		tot_sum += target_track_ratings[comu] * current_track_ratings[comu]

	# denominator of the cosine sim formula

	comp_a = math.sqrt(sum( i*i for i in target_track_ratings.values()))
	comp_b = math.sqrt(sum( i*i for i in current_track_ratings.values()))
	#complete formulation
	return tot_sum / (comp_a * comp_b)

def jaccard_sim(tmatrix, music_id, target_track_id):
	target_track_ratings = tmatrix[target_track_id]
	current_track_ratings = tmatrix[music_id]

	common_usernames = list(set(target_track_ratings.keys()).intersection(set(current_track_ratings.keys())))
	total_distinct_username = len(target_track_ratings.keys()) + len(current_track_ratings.keys()) - len(common_usernames)

	numerator = 0.0
	for comu in common_usernames:
		if target_track_ratings[comu] == current_track_ratings[comu]:
			numerator += 1

	if len(common_usernames) >= 1:
		return 100 * (numerator / total_distinct_username)
	return 0

def get_top_N_similar_music(username, umatrix, tmatrix, N, target_track_id):
	mbid_similarity = {}
	user_tracks = umatrix[username]

	for music_id in user_tracks.keys():
		mbid_similarity[music_id] = jaccard_sim(tmatrix, music_id, target_track_id)

	sorted_mbid_similarity = sorted(mbid_similarity.items(), key=lambda x: x[1], reverse=True)
	return list(sorted_mbid_similarity)[:N]


def get_estimation(music_id, username, umatrix, tmatrix, N, overall_mean):
	a1 = time.time()
	baseline_i = calculate_baseline(music_id, username, umatrix, tmatrix, overall_mean)
	print('Baseline i time: ', time.time() - a1)
	a2 = time.time()
	top_similar = get_top_N_similar_music(username, umatrix, tmatrix, N, music_id)
	print('Top similar time: ', time.time()-a2, ' n = ', N)

	a3 = time.time()
	#denominator
	weighted_sum = 0
	for similar in top_similar:
		rxj = umatrix[username][similar[0]]
		bxj = calculate_baseline(similar[0], username, umatrix, tmatrix, overall_mean)
		sij = similar[1]
		weighted_sum += sij * (rxj - bxj)
	print('Rating calculation time: ', time.time()-a3)

	overall_sum = sum([x[1] for x in top_similar])
	print('base: ', baseline_i, ' weighted_sum: ', weighted_sum, ' overall_sum: ', overall_sum)
	if overall_sum == 0:
		rating = 0
	else:
		rating = baseline_i + weighted_sum / overall_sum
	return rating

def get_estimation_binary(music_id, username, umatrix, tmatrix, N):
	top_similar = get_top_N_similar_music(username, umatrix, tmatrix, N, music_id)
	loved = 0
	notloved = 0
	for similar in top_similar:
		if umatrix[username][similar[0]] == 1:
			loved += 1
		else:
			notloved += 1
	if loved >= notloved:
		return 1
	return 0

def get_recommendations_binary(username, track_list, n_recommendation, umatrix, tmatrix):
	estimated_binary_ratings = {}
	i = 0
	for track_id in track_list:
		print('value: ', i)
		i += 1
		estimated_binary_ratings[track_id] = get_estimation_binary(track_id, username, umatrix, tmatrix, 20)
	sorted_estimation_list = sorted(estimated_binary_ratings.items(), key=lambda x: x[1], reverse=True)[:n_recommendation]
	return sorted_estimation_list

def get_recommendations(username, track_list, n_recommendation, umatrix, tmatrix):
	overall_mean = calculate_overall_mean(umatrix)
	estimated_track_ratings = {}
	for track_id in track_list:
		estimated_track_ratings[track_id] = get_estimation(track_id, username, umatrix, tmatrix, 20, overall_mean)

	sorted_rating_list = sorted(estimated_track_ratings.items(), key=lambda x: x[1], reverse=True)[:n_recommendation]
	return sorted_rating_list




# with open('umatrix.txt', 'wb') as handle:
#   pickle.dump(umatrix, handle)
#
# with open('tmatrix.txt', 'wb') as handle:
#   pickle.dump(tmatrix, handle)


# Genrate a use case to test algorithm
# #mbid: 5e03ae75-edfa-4bf4-a30a-0c93372a5743, username: tucnak4eva
# print(umatrix['tucnak4eva']['5e03ae75-edfa-4bf4-a30a-0c93372a5743'])
# print('---------------------------------------------------')
# print(tmatrix['5e03ae75-edfa-4bf4-a30a-0c93372a5743']['tucnak4eva'])
# print('total error: ' + str(errcnt))
#
# username = 'tucnak4eva'
# music_id = '5e03ae75-edfa-4bf4-a30a-0c93372a5743'

umatrix, tmatrix = generate_umatrix_tmatrix()
#user 1
user1 = 'zzuba'
music1 = ["9d2a1187-e2eb-492d-ae25-d47e1660e776", "2598d956-5812-4b10-a2ed-c6bbd848a9b5", "d082fa6c-13d3-49ea-bde8-6dc1e28386ef", "549de12c-bdda-4f34-9f6a-3a11ef4855e3", "e1748d2a-8315-4577-9196-b6a6b96fec3b"]
user2 = 'aaronnk'
music2 = ["38f529e9-92d5-407a-82e3-0c87b0ae906c", "9e0fd1aa-4ef2-46b2-bef3-fb975d6dd3dd", "7d527556-0b84-461f-9239-da3e33c4ad8f", "1243e386-d49c-48b8-aea7-ea14383eb67f", "1b7d1d45-3529-4bd3-b2c4-4cf6160054f0"]
user3 = 'omerfs'
music3 = ["0a950f6b-20c1-461a-8385-8335d5f2668a", "176d887e-054e-4a48-b8fb-9d5614372f20", "11b7c3d2-8a49-4812-95dc-aef93c4cec37", "0afdf0bb-cf31-456b-814e-afc42b26da4b", "f1e57531-e0df-4b3e-938f-1ae30c5b1a11"]

print('---USERNAME: ', user1, '---')
for i in music1:
	print('MusicID: ', i, ' Rating: ', umatrix[user1][i])
	del umatrix[user1][i]
	del tmatrix[i][user1]
print('-----------------------------')

print('---USERNAME: ', user2, '---')
for i in music2:
	print('MusicID: ', i, ' Rating: ', umatrix[user2][i])
	del umatrix[user2][i]
	del tmatrix[i][user2]
print('-----------------------------')

print('---USERNAME: ', user3, '---')
for i in music3:
	print('MusicID: ', i, ' Rating: ', umatrix[user3][i])
	del umatrix[user3][i]
	del tmatrix[i][user3]
print('-----------------------------')

# print('DEBUG------------')
# print('Umatrix: ', umatrix[user1])
# print('Tmatrix: ', tmatrix[music1[0]])
# print('------------------')

a1 = time.time()
recommendation_1 = get_recommendations_binary(user1, music1, 5, umatrix, tmatrix)
print('Recommendation 1 time: ', time.time() - a1)
a2 = time.time()
recommendation_2 = get_recommendations_binary(user2, music2, 5, umatrix, tmatrix)
print('Recommendation 2 time: ', time.time() - a2)
a3 = time.time()
recommendation_3 = get_recommendations_binary(user3, music3, 5, umatrix, tmatrix)
print('Recommendation 3 time: ', time.time() - a3)


print('Recommendation for User 1')
print(recommendation_1)
print('Recommendation for User 2')
print(recommendation_2)
print('Recommendation for User 3')
print(recommendation_3)

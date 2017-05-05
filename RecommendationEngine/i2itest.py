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

debug = False

def generate_umatrix_tmatrix():
	path = '/root/Bulbul/BulbulData/user_detail/'
	storage_path = '/root/Bulbul/brain/matrix_storage/'


	if os.path.isfile( storage_path + 'umatrix.txt'):
		if os.path.isfile( storage_path + 'tmatrix.txt'):
			if os.path.isfile( storage_path + 'lmatrix.txt'):

				umatrix = pickle.load(open( storage_path + 'umatrix.txt', 'rb'))
				tmatrix = pickle.load(open( storage_path + 'tmatrix.txt', 'rb'))
				lmatrix = pickle.load(open( storage_path + 'lmatrix.txt', 'rb'))



				return umatrix, tmatrix, lmatrix

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
			
	for top in top_tracks:
		f = open(path + top)
		data = json.loads(f.read())
		f.close()
		# Generate umatrix
		try:
			top = top.split('_top_tracks')[0]
			tracks = {}
			countlist = []

			user_loved = []
			if top in lmatrix:
				user_loved = lmatrix[top]

			# Finding min and max playcount values for rating scaling
			for count in range(len(data['toptracks']['track'])):
				if data['toptracks']['track'][count]['mbid'] is not None and data['toptracks']['track'][count]['mbid'].strip():
					countlist.append(float(data['toptracks']['track'][count]['playcount']))
			# If list is empty continue
			if not countlist:
				continue
			maxval = max(countlist)
			minval = min(countlist)
			#Generate umatrix
			for count in range(len(data['toptracks']['track'])):
				if data['toptracks']['track'][count]['mbid'] is not None and data['toptracks']['track'][count]['mbid'].strip():
					if minval == maxval:
						rating = 50.0
					else:
						rating = 25 * (float(data['toptracks']['track'][count]['playcount']) - minval) / (maxval - minval) + 50

					if data['toptracks']['track'][count]['mbid'] in user_loved:
						rating += 25

					if debug:
						print(rating)


					tracks[data['toptracks']['track'][count]['mbid']] = rating
		except Exception as e:
			if debug:
				print('Got exception during umatrix: ', str(e))
				print(countlist)
			errcnt += 1
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
	# user x = tucnak4eva
	# calculate Rating deviation of user x
	user_x = username
	tracks = umatrix[user_x]
	sum_of_ratings_x = 0
	for t in tracks.items():
		sum_of_ratings_x += t[1]
	if len(tracks):
		average_rating_of_user_x = float(sum_of_ratings_x) / len(tracks)
	else:
		average_rating_of_user_x = overall_mean
	rating_daviation_of_user_x = average_rating_of_user_x - overall_mean

	# music i = '5e03ae75-edfa-4bf4-a30a-0c93372a5743'
	# calculate the rating deviation of music i
	music_i = music_id

	if music_i in tmatrix:
		music = tmatrix[music_i]

		if len(music) <= 0:
			return overall_mean + rating_daviation_of_user_x + 0

		sum_of_music_ratings_i = 0
		for m in music.items():
			sum_of_music_ratings_i += m[1]
		average_rating_of_music_i = float(sum_of_music_ratings_i) / len(music)
		rating_daviation_of_music_i = average_rating_of_music_i - overall_mean
	else:
		rating_daviation_of_music_i = 0

	# baseline estimate of music i for user x
	baseline_estimate = overall_mean + rating_daviation_of_user_x + rating_daviation_of_music_i

	# print('overall_mean: ', overall_mean)
	# print('rating deviation of user {}'.format(user_x), rating_daviation_of_user_x)
	# print('rating deviation of music {}'.format(music_i), rating_daviation_of_music_i)
	# print('baseline estime: ', baseline_estimate)
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

	common_usernames = list(set(target_track_ratings.keys()).intersection(list(current_track_ratings.keys())))

	#numerator of the cosine sim formula
	tot_sum = 0
	for comu in common_usernames:
		tot_sum += target_track_ratings[comu] * current_track_ratings[comu]

	# denominator of the cosine sim formula

	comp_a = math.sqrt(sum( i*i for i in target_track_ratings.values()))
	comp_b = math.sqrt(sum( i*i for i in current_track_ratings.values()))
	#complete formulation
	return tot_sum / (comp_a * comp_b)


def get_top_N_similar_music(username, umatrix, tmatrix, N, target_track_id):
	mbid_similarity = {}
	user_tracks = umatrix[username]

	for music_id in user_tracks.keys():
		try:
			mbid_similarity[music_id] = quick_cosine_sim(tmatrix, music_id, target_track_id)
		except Exception as e:
			#skip that bad song
			print('got exception {}'.format(str(e)))

	sorted_mbid_similarity = sorted(mbid_similarity.items(), key=lambda x: x[1], reverse=True)
	return list(sorted_mbid_similarity)[:N]


def get_estimate(music_id, username, umatrix, tmatrix, N, overall_mean):
	a1 = time.time()
	baseline_i = calculate_baseline(music_id, username, umatrix, tmatrix, overall_mean)
	if debug:
		print('Baseline i time: ', time.time() - a1)
		a2 = time.time()
	top_similar = get_top_N_similar_music(username, umatrix, tmatrix, N, music_id)
	if debug:
		print('Top similar time: ', time.time()-a2, ' n = ', N)

	a3 = time.time()
	#denominator
	weighted_sum = 0
	for similar in top_similar:
		rxj = umatrix[username][similar[0]]
		bxj = calculate_baseline(similar[0], username, umatrix, tmatrix, overall_mean)
		sij = similar[1]
		weighted_sum += sij * (rxj - bxj)

	if debug:
		print('Rating calculation time: ', time.time()-a3)

	overall_sum = sum([x[1] for x in top_similar])

	if debug:
		print('base: ', baseline_i, ' weighted_sum: ', weighted_sum, ' overall_sum: ', overall_sum)

	try:
		rating = baseline_i + weighted_sum / overall_sum
	except ZeroDivisionError as e:
		print('got zero devision exception {} '.format(str(e)))
		rating = baseline_i
	return rating

def get_recommendations(username, track_list, n_recommendation, umatrix, tmatrix):
	overall_mean = calculate_overall_mean(umatrix)
	estimated_track_ratings = {}
	for track_id in track_list:
		print('.........................')
		estimated_track_ratings[track_id] = get_estimate(track_id, username, umatrix, tmatrix, 20, overall_mean)

	sorted_rating_list = sorted(estimated_track_ratings.items(), key=lambda x: x[1], reverse=True)[:n_recommendation]
	global debug
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

'''
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

recommendation_1 = get_recommendations(user1, music1, 5, umatrix, tmatrix)
recommendation_2 = get_recommendations(user2, music2, 5, umatrix, tmatrix)
recommendation_3 = get_recommendations(user3, music3, 5, umatrix, tmatrix)


print('Recommendation for User 1')
print(recommendation_1)
print('Recommendation for User 2')
print(recommendation_2)
print('Recommendation for User 3')
print(recommendation_3)

'''

def main():
    pass


#mode 0 -> use the whole song list
#mode 1 -> use the given filtered_songs list
#username -> name of the new user we want recommendations.
#rated_songs -> tuple list of songs and their given ids in the app.
#filtered
#returns a dictionary with N keys. The ratings are given for each key.
def get_recommendations_outside(mode, username, rated_songs, filtered_songs, N):

	global debug

	umatrix, tmatrix, l = generate_umatrix_tmatrix()

	username_input = username

	if mode == 1:
		song_list = filtered_songs
	elif mode == 0:
		song_list = list(tmatrix.keys())

	#if the user exits i.e. test, delete the ratings.
	userlist = list(umatrix.keys())

	if username_input in userlist:
		print('----test initiated----')

		#delete ratings.

		for i in song_list:
			if i in umatrix[username_input]:
				if debug:
					print(i)
					print(umatrix[username_input][i])
				del umatrix[username_input][i]
				del tmatrix[i][username_input]


		#check if we lost any songs: a song might be listened by only one user. in this case we cannot make a
		#rating prediction of this song so, eliminate it.

		for i in song_list:
			if i not in tmatrix:
				song_list.remove(i)




	else:
		print('regular mode')

		new_user = username
		#liked_songs = ["0a950f6b-20c1-461a-8385-8335d5f2668a", "176d887e-054e-4a48-b8fb-9d5614372f20", "11b7c3d2-8a49-4812-95dc-aef93c4cec37", "0afdf0bb-cf31-456b-814e-afc42b26da4b", "f1e57531-e0df-4b3e-938f-1ae30c5b1a11"]

		# to be developed...

		#add to umatrix and tmatrix
		umatrix[new_user] = {}
		for song in rated_songs:
			umatrix[new_user][song[0]] = song[1]
			tmatrix[song[0]] = {new_user:song[1]}



	#estimate ratings.
	a = time.time()
	recommendation_1 = get_recommendations(username_input, song_list, N, umatrix, tmatrix)
	print('Recommendation time: ', time.time() - a)
	return dict(recommendation_1)


if __name__ == "__main__":
	# stuff only to run when not called via 'import' here

	umatrix, tmatrix, l = generate_umatrix_tmatrix()

	username_input = sys.argv[1]
	song_list_file = sys.argv[2]
	out_file = sys.argv[3]

	song_list = []
	with open(song_list_file) as songs:
		for line in songs:
			song_list.append(line[:-1])

	#if the user exits i.e. test, delete the ratings.
	userlist = list(umatrix.keys())
	if username_input in userlist:
		print('----test initiated----')

		#delete ratings.

		for i in song_list:
			if i in umatrix[username_input]:
				if debug:
					print(i)
					print(umatrix[username_input][i])
				del umatrix[username_input][i]
				del tmatrix[i][username_input]


		#check if we lost any songs: a song might be listened by only one user. in this case we cannot make a
		#rating prediction of this song so, eliminate it.

		for i in song_list:
			if i not in tmatrix:
				song_list.remove(i)




	else:
		print('regular mode')

		new_user = 'omer58'
		liked_songs = ["0a950f6b-20c1-461a-8385-8335d5f2668a", "176d887e-054e-4a48-b8fb-9d5614372f20", "11b7c3d2-8a49-4812-95dc-aef93c4cec37", "0afdf0bb-cf31-456b-814e-afc42b26da4b", "f1e57531-e0df-4b3e-938f-1ae30c5b1a11"]

		# to be developed...

		#add to umatrix and tmatrix
		umatrix['omer58'] = {}
		for song in liked_songs:
			umatrix['omer58'][song] = 80
			tmatrix[song] = {'omer58':80}


		#do recommendations.
		recommendation_1 = get_recommendations(username_input, song_list, len(song_list), umatrix, tmatrix)




	#estimate ratings.
	recommendation_1 = get_recommendations(username_input, song_list, len(song_list), umatrix, tmatrix)

	with open(out_file, 'w+') as out:
		for rec in recommendation_1:
			out.write(rec[0] + ' ' + str(rec[1]) + '\n')


	print(recommendation_1)


	main()

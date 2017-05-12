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
similarity_time = 0
baseline_time = 0
i2i_calc_time = 0
hit = 0
call = 0
tot_sim = 0
missed_sim = 0


def generate_sim_matrix():
	storage_path = '/root/Bulbul/RecommendationEngine/matrix_storage/'
	return pickle.load(open(storage_path + 'similarity_matrix2.txt', 'rb'))

def generate_umatrix_tmatrix():
	path = '/root/Bulbul/BulbulData/user_detail/'
	storage_path = '/root/Bulbul/RecommendationEngine/matrix_storage/'


	if os.path.isfile( storage_path + 'umatrix.txt'):
		if os.path.isfile( storage_path + 'tmatrix.txt'):
			if os.path.isfile( storage_path + 'lmatrix.txt'):
				umatrix_file = open( storage_path + 'umatrix.txt', 'rb')
				tmatrix_file = open( storage_path + 'tmatrix.txt', 'rb')
				lmatrix_file = open( storage_path + 'lmatrix.txt', 'rb')

				umatrix = pickle.load(umatrix_file)
				tmatrix = pickle.load(tmatrix_file)
				lmatrix = pickle.load(lmatrix_file)

				umatrix_file.close()
				tmatrix_file.close()
				lmatrix_file.close()

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

	umatrix_file = open( storage_path + 'umatrix.txt', 'wb+')
	tmatrix_file = open( storage_path + 'tmatrix.txt', 'wb+')
	lmatrix_file = open( storage_path + 'lmatrix.txt', 'wb+')

	pickle.dump(umatrix, open(umatrix_file))
	pickle.dump(tmatrix, open(tmatrix_file))
	pickle.dump(lmatrix, open(lmatrix_file))

	umatrix_file.close()
	tmatrix_file.close()
	lmatrix_file.close()

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

		if len(music):
			average_rating_of_music_i = float(sum_of_music_ratings_i) / len(music)
		else:
			average_rating_of_music_i = overall_mean
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

def quick_cosine_sim(tmatrix, music_id, target_track_id, sim_matrix):

	#print("LEN SIM MATRIX", len(sim_matrix))

	global call
	global hit
	call += 1
	if (music_id, target_track_id) in sim_matrix:
		hit += 1
		return sim_matrix[(music_id, target_track_id)]
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


def get_top_N_similar_music(username, umatrix, tmatrix, N, target_track_id, sim_matrix):
	mbid_similarity = {}
	user_tracks = umatrix[username]

	global tot_sim
	global missed_sim
	for music_id in user_tracks.keys():
		tot_sim += 1
		try:
			mbid_similarity[music_id] = quick_cosine_sim(tmatrix, music_id, target_track_id, sim_matrix)
		except Exception as e:
			#skip that bad song
			if debug:
				print('got exception {}'.format(str(e)))
			missed_sim += 1

	sorted_mbid_similarity = sorted(mbid_similarity.items(), key=lambda x: x[1], reverse=True)
	return list(sorted_mbid_similarity)[:N]


def get_estimate(music_id, username, umatrix, tmatrix, N, overall_mean, sim_matrix):
	a1 = time.time()
	baseline_i = calculate_baseline(music_id, username, umatrix, tmatrix, overall_mean)
	global baseline_time
	global similarity_time
	global i2i_calc_time
	baseline_time += time.time() -a1

	if debug:
		print('Baseline i time: ', time.time() - a1)
	a2 = time.time()
	top_similar = get_top_N_similar_music(username, umatrix, tmatrix, N, music_id, sim_matrix)
	if debug:
		print('Top similar time: ', time.time()-a2, ' n = ', N)
	similarity_time += time.time()- a2

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
	i2i_calc_time += time.time()-a3

	overall_sum = sum([x[1] for x in top_similar])

	if debug:
		print('base: ', baseline_i, ' weighted_sum: ', weighted_sum, ' overall_sum: ', overall_sum)

	try:
		rating = baseline_i + weighted_sum / overall_sum
	except ZeroDivisionError as e:
		if debug:
			print('got zero devision exception {} '.format(str(e)))
		rating = baseline_i
	return rating

def get_recommendations(username, track_list, n_recommendation, umatrix, tmatrix, sim_matrix):
	overall_mean = calculate_overall_mean(umatrix)
	estimated_track_ratings = {}
	for track_id in track_list:
		if debug:
			print('.........................')
		estimated_track_ratings[track_id] = get_estimate(track_id, username, umatrix, tmatrix, 20, overall_mean, sim_matrix)

	sorted_rating_list = sorted(estimated_track_ratings.items(), key=lambda x: x[1], reverse=True)[:n_recommendation]
	return sorted_rating_list


def main():
    pass

#mode 0 -> use the whole song list
#mode 1 -> use the given filtered_songs list
#username -> name of the new user we want recommendations.
#rated_songs -> tuple list of songs and their given ids in the app.
#filtered
#returns a dictionary with N keys. The ratings are given for each key.
def get_recommendations_outside(mode, username, rated_songs, filtered_songs, N, umatrix, tmatrix, l, sim_matrix):

	global debug
	global similarity_time
	global baseline_time
	global i2i_calc_time
	global hit
	global call

	similarity_time = 0
	baseline_time = 0

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

			try:
				tmatrix[song[0]][new_user] = song[1]
			except KeyError:
				tmatrix[song[0]] = {new_user:song[1]}

	#estimate ratings.
	a = time.time()
	recommendation_1 = get_recommendations(username_input, song_list, N, umatrix, tmatrix, sim_matrix)
	recommendation_1 = [x[0] for x in recommendation_1]
	print('Recommendation time: ', time.time() - a)
	print('total similarity time: ', similarity_time)
	print('total baseline time: ', baseline_time)
	print('total i2i calculation time; ', i2i_calc_time)
	print('total hits / calls: ', hit/call)
	print('total sim missed / calls: ', missed_sim / tot_sim)

	# Remove user from umatrix and tmatrix
	del umatrix[new_user]
	for song in rated_songs:
		del tmatrix[song[0]][new_user]

	return recommendation_1


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

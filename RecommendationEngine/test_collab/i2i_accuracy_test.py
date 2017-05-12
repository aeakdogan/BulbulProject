import sys
import os
import random

sys.path.insert(0, '../')

from i2itest import generate_umatrix_tmatrix
from i2itest import get_recommendations_outside
from i2itest import generate_sim_matrix

test_user_count = 1
test_spu = 20 #song per user
train_spu = 20

sim_matrix = generate_sim_matrix()
umatrix, tmatrix, l = generate_umatrix_tmatrix()
#print(umatrix['zzuba']['9d2a1187-e2eb-492d-ae25-d47e1660e776'])

#determine test users.
#users = ['omerfs']
rand_user = list(umatrix.keys())
random.shuffle(rand_user)
users = rand_user[:test_user_count]


#determine test songs
filtered_songs = {}
rated_songs = {}
#songs['omerfs'] = ["0a950f6b-20c1-461a-8385-8335d5f2668a", "176d887e-054e-4a48-b8fb-9d5614372f20", "11b7c3d2-8a49-4812-95dc-aef93c4cec37", "0afdf0bb-cf31-456b-814e-afc42b26da4b", "f1e57531-e0df-4b3e-938f-1ae30c5b1a11"]

for user in users:
	rand_song = list(umatrix[user].keys())
	random.shuffle(rand_song)

	filtered_songs[user] = rand_song[:test_spu]
	rated_songs[user] = rand_song[test_spu:test_spu+train_spu]





#save the ratings
ratings = {}
for user in users:
	ratings[user] = {}
	for song in filtered_songs[user]:

		ratings[user][song] = umatrix[user][song]
		#print(song)
		#print(umatrix[user][song])
		#exit()





#run script
print('start')


print(len(set(tmatrix.keys())))
print(len(tmatrix.items()))

for user in users:

	output = get_recommendations_outside(1, user, rated_songs[user], filtered_songs[user], int(len(filtered_songs[user])), umatrix, tmatrix, l, sim_matrix)


	total = 0
	#print('estimate \t real value')
	for song in filtered_songs[user]:
		total += abs(output[song] - ratings[user][song])
		#print('{}  {}'.format(output[song], ratings[user][song]))


	print('for user {} the accuracy is on average {} off the original.'.format(user, total/(len(filtered_songs[user]))))



print('end')

#get resp


#check accuracy.
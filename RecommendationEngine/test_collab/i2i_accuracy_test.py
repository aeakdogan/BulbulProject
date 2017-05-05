import sys
import os
import random

sys.path.insert(0, '../')

from i2itest import generate_umatrix_tmatrix

test_user_count = 1
test_spu = 500 #song per user


umatrix, tmatrix, l = generate_umatrix_tmatrix()
print(umatrix['zzuba']['9d2a1187-e2eb-492d-ae25-d47e1660e776'])

#determine test users.
#users = ['omerfs']
rand_user = list(umatrix.keys())
random.shuffle(rand_user)
users = rand_user[:test_user_count]


#determine test songs
songs = {}
#songs['omerfs'] = ["0a950f6b-20c1-461a-8385-8335d5f2668a", "176d887e-054e-4a48-b8fb-9d5614372f20", "11b7c3d2-8a49-4812-95dc-aef93c4cec37", "0afdf0bb-cf31-456b-814e-afc42b26da4b", "f1e57531-e0df-4b3e-938f-1ae30c5b1a11"]

for user in users:
	rand_song = list(umatrix[user].keys())
	random.shuffle(rand_song)
	songs[user] = rand_song[:test_spu]



#save the ratings
ratings = {}
for user in users:
	ratings[user] = {}
	for song in songs[user]:

		ratings[user][song] = umatrix[user][song]
		print(song)
		print(umatrix[user][song])
		#exit()

umatrix, tmatrix = None, None


#save test songs
for user in users:
	with open(user + '_songs', 'w+') as save_file:
		for song in songs[user]:
			save_file.write(song + '\n')

#run script
print('start')

for user in users:
	os.system('python3 ../i2itest.py ' + user + ' ' + user + '_songs ' + user + '.out')

print('end')

#get response
resp = {}
for user in users:
	with open(user + '.out', 'r') as output:
		resp[user] = output.read()


for user in users:
	raw_output = resp[user]
	with_blanks = raw_output.split('\n')
	output = {}
	for line in with_blanks[:-1]:
		output[line.split(' ')[0]] = float(line.split(' ')[1])


	total = 0
	print('estimate \t real value')
	for song in songs[user]:
		total += abs(output[song] - ratings[user][song])
		print('{}  {}'.format(output[song], ratings[user][song]))


	print('for user {} the accuracy is on average {} off the original.'.format(user, total/(len(songs[user]))))



#remove all the tmp files.
for user in users:
	os.system('rm ' + user + '_songs ' + user + '.out')


#check accuracy.
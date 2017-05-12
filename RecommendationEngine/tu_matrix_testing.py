from i2itest import generate_umatrix_tmatrix

u, t, l = generate_umatrix_tmatrix()

#print(u.keys())
print(len(u.keys()))


unique_songs = []
for user in u.keys():

	unique_songs += list(u[user].keys())
	#print(u[user])


print(len(unique_songs))
print(len(set(unique_songs)))

if 'ba32a675-ef9e-4545-ad71-780708e85437' in unique_songs:
	print("HELL TO THE YES")


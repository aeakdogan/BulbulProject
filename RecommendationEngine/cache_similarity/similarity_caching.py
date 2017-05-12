from neo4j.v1 import GraphDatabase, basic_auth
import pickle
import math

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


driver = GraphDatabase.driver("bolt://139.59.154.22:7687", auth=basic_auth("neo4j", "cilebulbulum"))
session = driver.session()

genres = session.run("MATCH (n:Genre) RETURN n.name as name")
genre_list = [g['name'] for g in genres]
print(len(genre_list), genre_list)

popular_tracks = set()
print('Fething popular tracks from Database ...')
for g in genre_list:
	result = session.run("MATCH p=(t:Track)-[r:IN]->(g:Genre) "
						"where g.name={genre_name} "
						"RETURN t.mbid as mbid, t.name as name, t.playcount as playcount "
						"order by t.playcount desc limit 150",
						{"genre_name": g})
	for i in result:
		popular_tracks.add(i['mbid'])
print('Popular tracks are fetched ...')

session.close()
popular_tracks = list(popular_tracks)
similarity_matrix = {}
tmatrix = pickle.load(open('../matrix_storage/tmatrix.txt', 'rb'))

# Delete tracks from popular tracks which are not in tmatrix
deleted_tracks = []
for track in popular_tracks:
	if track not in tmatrix:
		deleted_tracks.append(track)
		popular_tracks.remove(track)
print('deleted_tracks: ', len(deleted_tracks), deleted_tracks)
print('Popular tracks len: ', len(popular_tracks))

print('Similarity matrix generation started ... ')
size = len(popular_tracks)
current = 0
for track1 in popular_tracks:
	print('Current, ', current, ' size: ', size)
	current += 1
	for track2 in popular_tracks:
		if track1 != track2 and (track2, track1) in similarity_matrix:
			similarity_matrix[(track1, track2)] = similarity_matrix[(track2, track1)]
		else:
			similarity_matrix[(track1, track2)] = quick_cosine_sim(tmatrix, track1, track2)
print('Similarity matrix generation finished ... ')
pickle.dump(similarity_matrix, open( "../matrix_storage/similarity_matrix2.txt", "wb+" ))

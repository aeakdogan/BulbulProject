import redis
import json
from neo4j.v1 import GraphDatabase, basic_auth
import sys
sys.path.insert(0, 'binary_recommendation/')
#from i2itest import get_recommendations_outside
#from i2itest import generate_umatrix_tmatrix
#from i2itest import generate_sim_matrix
from i2itest_binary import generate_umatrix_tmatrix
from i2itest_binary import get_recommendations_outside
import time
import i2itest_binary



r = redis.StrictRedis(host='localhost', port=6379, db=0)

driver = GraphDatabase.driver("bolt://139.59.154.22:7687", auth=basic_auth("neo4j", "cilebulbulum"))
session = driver.session()


# Adds tracks with given track_ids as recommended tracks to recommendation with recommendation_id
def save_to_database(recommendation_id, mbids):
    session.run(
        "MATCH (r:Recommendation ), (t:Track) WHERE ID(r)={recommendation_id} AND t.mbid IN {mbids}" +
        " CREATE(r) - [:RECOMMENDED]->(t)  SET r.status = {status}"
        ,
        {"recommendation_id": recommendation_id, "mbids": mbids, "status": "READY"})


# Fetches a recommendation request from redis queue (It blocks until it receives a non nill value)
def get_recommendation_request():
    response = r.brpop('recommendation')
    return json.loads(response[1].decode('unicode_escape'))


if __name__ == "__main__":
	print('start')
	#umatrix, tmatrix, l = generate_umatrix_tmatrix()
	#sim_matrix = generate_sim_matrix()

	umatrix, tmatrix = i2itest_binary.generate_umatrix_tmatrix()

	print('u,t,l,s matrix loaded')
	print('RECOMMENDATION ENGINE READY')
	while True:
		rr = get_recommendation_request()
		print(rr['id'])
		ratings = rr['ratings']
		for rating in ratings:
			print("track:{} rating:{}".format(rating['mbid'], rating['value']))

		user_ratings = [(mydict['mbid'], mydict['value']*10) for mydict in ratings]

		user_r = [x[0] for x in user_ratings]
		filtered_r = rr['mbids']
		inter = set(user_r).intersection(set(filtered_r))
		filtered_r = list(set(filtered_r) - inter)

		a = time.time()
		#recommendations = get_recommendations_outside(1, 'omer58', user_ratings, rr['mbids'], 10, umatrix, tmatrix, l, sim_matrix)
		recommendations = get_recommendations_outside(1, 'omer58', user_ratings, filtered_r, 10, umatrix, tmatrix)

		save_to_database(recommendation_id=rr['id'], mbids=list(recommendations.keys()))
		print('time to recommend and save: ', time.time()-a)

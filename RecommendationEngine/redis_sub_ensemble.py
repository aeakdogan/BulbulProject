import redis
import json
from neo4j.v1 import GraphDatabase, basic_auth
import sys
sys.path.insert(0, 'i2itest.py')
import content_based
import i2itest
from i2itest import generate_umatrix_tmatrix
from i2itest import generate_sim_matrix
import time
import pickle
from random import shuffle



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

def filter_ratings(user_ratings):
    filtered_ratings = []

    for rating in user_ratings:
        if rating[1] > 85 or rating[1] < 15:
            filtered_ratings.append(rating)

    if len(filtered_ratings) > 100:
        shuffle(filtered_ratings)
        return filtered_ratings[:100]

    return filtered_ratings


if __name__ == "__main__":
    print('start')
    umatrix, tmatrix, l = generate_umatrix_tmatrix()
    sim_matrix = generate_sim_matrix()

    mbid2spotifyid = content_based.get_mbid_spotify_match()
    spotifyid2mbid = content_based.get_spotifyid_mbid_match()

    spotify_features_id = content_based.get_spotify_features_id()#pickle.load( open( content_based.storage + 'spotify_features_id', 'rb' ) )

    print('u,t,l matrix loaded')
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
        print('new request')
        print('rating length before filter: ', len(user_ratings))
        user_ratings = filter_ratings(user_ratings)

        print('input length: ', len(filtered_r))
        print('rating length: ', len(user_ratings))



        recommendations1 = content_based.get_recommendations_outside(1, 'omer58', user_ratings, filtered_r, 200, umatrix, tmatrix, l, sim_matrix,
                                                                        mbid2spotifyid, spotifyid2mbid, spotify_features_id)
        recommendations1 = [x[0] for x in recommendations1]
        print('Siinti burda; ', type(recommendations1))
        print('sikinti2', recommendations1[:10])
        print('content out lenght: ', len(recommendations1))
        recommendations2 = i2itest.get_recommendations_outside(1, 'omer58', user_ratings, recommendations1, 10, umatrix, tmatrix, l, sim_matrix)
        print('i2i out: ', len(recommendations2))
        print(type(recommendations2), recommendations2)
        save_to_database(recommendation_id=rr['id'], mbids=recommendations2)
        print('time to recommend and save: ', time.time()-a)

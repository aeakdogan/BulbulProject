import redis
import json
from neo4j.v1 import GraphDatabase, basic_auth
import sys
sys.path.insert(0, 'i2itest.py')
from i2itest import get_recommendations_outside


r = redis.StrictRedis(host='localhost', port=6379, db=0)

driver = GraphDatabase.driver("bolt://localhost:7687", auth=basic_auth("neo4j", "cilebulbulum"))
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
    while True:
        rr = get_recommendation_request()
        print(rr['id'])
        ratings = rr['ratings']
        for rating in ratings:
            print("track:{} rating:{}".format(rating['mbid'], rating['value']))

        user_ratings = [(mydict['mbid'], mydict['value']*10) for mydict in ratings]
        recommendations = get_recommendations_outside(0, 'omer58', user_ratings, None, 10)

        save_to_database(recommendation_id=rr['id'], mbids=list(recommendations.keys()))

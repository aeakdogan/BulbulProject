<?php

namespace App\GraphQL\Mutation;

use App\Rating;
use App\Recommendation;
use App\Track;
use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Mutation;
use Illuminate\Support\Facades\Redis;
use JWTAuth;
use Log;
use Everyman\Neo4j\Cypher\Query as DBQuery;
use DB;


class RequestRecommendationMutation extends Mutation
{
    protected $attributes = [
        'name' => 'requestRecommendation'
    ];

    public function type()
    {
        return GraphQL::type('Recommendation');
    }

    public function args()
    {
        return [
            'ratings' => ['name' => 'ratings', 'type' => Type::nonNull(Type::listOf(Type::int()))],
            'track_ids' => ['name' => 'track_ids', 'type' => Type::nonNull(Type::listOf(Type::int()))],
            'artist_ids' => ['name' => 'artist_ids', 'type' => Type::nonNull(Type::listOf(Type::int()))],
            'genre_ids' => ['name' => 'genre_ids', 'type' => Type::nonNull(Type::listOf(Type::int()))],
            'token' => ['name' => 'token', 'type' => Type::nonNull(Type::string())]
        ];
    }

    public function resolve($root, $args)
    {
        $r = Recommendation::create(
            [
                'status' => 'NOT_READY',
                'type' => 'REGULAR',
                'artist_ids' => $args['artist_ids'],
                'genre_ids' => $args['genre_ids']
            ]);
        $user = JWTAuth::authenticate($args['token']);
        $user->recommendations()->attach($r);
        $user_ratings = $user->ratings;

        $queries = [];
        foreach($args['genre_ids'] as $genre_id){
            $queries[] = 'MATCH (t:Track)-[:IN]->(genre:Genre) WHERE NOT (id(t) IN {track_ids}) AND id(genre)='.$genre_id.' RETURN t.mbid as mbid ORDER BY t.playcount DESC LIMIT {limit}';
        }

        $queryString = implode(' UNION ', $queries);
        $client = DB::getClient();
        $query = new DBQuery($client, $queryString, ['track_ids'=>$args['track_ids'] ,'limit' => round(2000/count($args['genre_ids']))]);
        $result = $query->getResultSet();
        $mbids = [];

        foreach ($result as $row) $mbids[] = $row['mbid'];

        $r->mbids = $mbids;
        $rating_mbids = Track::find($args['track_ids'])->pluck('mbid');
        foreach ($args['ratings'] as $key => $rating) {

            $rating = Rating::create(
                [
                    'track_id' => $args['track_ids'][$key],
                    'mbid' => $rating_mbids[$key],
                    'value' => $args['ratings'][$key]
                ]);

            $r->ratings()->attach($rating);

            $existing_ratings = $user_ratings->where('track_id', $args['track_ids'][$key]);
            if ($existing_ratings->count() > 0) {
                $rating = $existing_ratings->first();
                $rating->value = $args['ratings'][$key];
                $rating->save();
            } else {
                $user->ratings()->attach($rating);
            }
        }

        Redis::command('lpush', ['recommendation', [$r->load('ratings')]]);
        return $r;
    }
}
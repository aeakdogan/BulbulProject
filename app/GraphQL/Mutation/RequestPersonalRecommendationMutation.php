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
use Event;
use Everyman\Neo4j\Cypher\Query as DBQuery;
use DB;


class RequestPersonalRecommendationMutation extends Mutation
{
    protected $attributes = [
        'name' => 'requestPersonalRecommendation'
    ];

    public function type()
    {
        return GraphQL::type('Recommendation');
    }

    public function args()
    {
        return [
            'genre_ids' => ['name' => 'genre_ids', 'type' => Type::nonNull(Type::listOf(Type::int()))],
            'token' => ['name' => 'token', 'type' => Type::nonNull(Type::string())]
        ];
    }

    public function resolve($root, $args)
    {
        $user = JWTAuth::authenticate($args['token']);
        if (!$user) {
            return null;
        }
        $r = Recommendation::create(
            [
                'status' => 'NOT_READY',
                'type' => 'PERSONAL',
                'genre_ids' => $args['genre_ids']
            ]
        );
        $user->recommendations()->attach($r);
        $r->ratings = $user->ratings;
        $track_ids = $r->ratings->pluck('track_id')->toArray();

        $queries = [];
        foreach($args['genre_ids'] as $genre_id){
            $queries[] = 'MATCH (t:Track)-[:IN]->(genre:Genre) WHERE NOT (id(t) IN {track_ids}) AND id(genre)='.$genre_id.' AND EXISTS(t.audio_features_acousticness) RETURN t.mbid as mbid ORDER BY t.playcount DESC LIMIT {limit}';
        }

        $queryString = implode(' UNION ', $queries);
        $client = DB::getClient();
        $query = new DBQuery($client, $queryString, ['track_ids'=>$track_ids ,'limit' => round(2000/count($args['genre_ids']))]);
        $result = $query->getResultSet();
        $mbids = [];

        foreach ($result as $row) $mbids[] = $row['mbid'];

        $r->mbids = $mbids;

        Redis::command('lpush', ['recommendation', [$r]]);
        return $r;
    }
}
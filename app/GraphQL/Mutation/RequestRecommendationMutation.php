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
                'artist_ids' => $args['artist_ids'],
                'genre_ids' => $args['genre_ids']
            ]);
        $user = JWTAuth::authenticate($args['token']);
        $user->recommendations()->attach($r);
        $r->mbids = Track::with('genres')->whereNotIn('id', $args['track_ids'])->whereHas('genres',
            function ($query) use ($args){$query->whereIn('id', $args['genre_ids']);})
            ->orderBy('playcount', 'DESC')->take(2000/count($args['genre_ids']))->get()->pluck('mbid');
        $rating_mbids = Track::find($args['track_ids'])->pluck('mbid');

        foreach ($args['ratings'] as $key => $rating) {

            $rating = Rating::create(
                [
                    'track_id' => $args['track_ids'][$key],
                    'mbid' => $rating_mbids[$key],
                    'value' => $args['ratings'][$key]
                ]);

            $r->ratings()->attach($rating);
        }

        Redis::command('lpush', ['recommendation', [$r->load('ratings')]]);
        return $r;
    }
}
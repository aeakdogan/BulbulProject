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
            'genre_id' => ['name' => 'genre_id', 'type' => Type::nonNull(Type::int())],
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
                'genre_id' => $args['genre_id']
            ]
        );
        $user->recommendations()->attach($r);
        $r->ratings = $user->ratings;
        $track_ids = $r->ratings->pluck('track_id')->toArray();
        $r->mbids = Track::with('genres')->whereNotIn('id', $track_ids)->whereHas('genres',
            function ($query) use ($args) {
                $query->where('id', $args['genre_id']);
            })
            ->orderBy('playcount', 'DESC')->take(1000)->get()->pluck('mbid')->toArray();

        Redis::command('lpush', ['recommendation', [$r]]);
        return $r;
    }
}
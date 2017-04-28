<?php

namespace App\GraphQL\Mutation;

use App\Rating;
use App\Recommendation;
use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Mutation;
use App\Artist;
use App\Tag;
use Illuminate\Contracts\Logging\Log;
use Illuminate\Support\Facades\Redis;
use JWTAuth;
use Event;
use App\Events\RecommendationRequested;


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
            'ratings' => ['name' => 'ratings', 'type' => Type::listOf(Type::int())],
            'track_ids' => ['name' => 'track_ids', 'type' => Type::listOf(Type::int())],
            'token' => ['name' => 'token', 'type' => Type::nonNull(Type::string())]
        ];
    }

    public function resolve($root, $args)
    {
        $r = Recommendation::create(['status' => 'NOT_READY']);
        $user = JWTAuth::authenticate($args['token']);
        $user->recommendations()->attach($r);
        foreach ($args['ratings'] as $key => $rating) {
            $rating = Rating::create(
                [
                    'track_id' => $args['track_ids'][$key],
                    'value' => $args['ratings'][$key]
                ]);

            $r->ratings()->attach($rating);
        }
//        Event::fire(new RecommendationRequested($r));
        Redis::command('lpush',['recommendation',[$r->load('ratings')]]);
        return $r;
    }
}
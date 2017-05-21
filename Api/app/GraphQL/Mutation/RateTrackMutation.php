<?php

namespace App\GraphQL\Mutation;

use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Mutation;
use JWTAuth;
use App\Track;
use App\Rating;

class RateTrackMutation extends Mutation
{
    protected $attributes = [
        'name' => 'rateTrackMutation'
    ];

    public function type()
    {
        return GraphQL::type('Track');
    }

    public function args()
    {
        return [
            'token' => ['name' => 'token', 'type' => Type::nonNull(Type::string())],
            'track_id' => ['name' => 'track_id', 'type' => Type::nonNull(Type::int())],
            'rating' => ['name' => 'rating', 'type' => Type::nonNull(Type::int())],
        ];
    }

    public function resolve($root, $args)
    {
        $user = JWTAuth::authenticate($args['token']);
        $track = Track::find($args['track_id']);
        if (!$user || !$track) {
            return null;
        }
        $rating = $user->ratings()->where('track_id', $args['track_id'])->first();

        if ($rating) {
            $rating->value = $args['rating'];
            $rating->save();
        } else {
            $rating = Rating::create(
                [
                    'track_id' => $args['track_id'],
                    'mbid' => $track->mbid,
                    'value' => $args['rating']
                ]);
            $user->ratings()->attach($rating);
        }
        $track->rating = $rating;
        return $track;
    }
}
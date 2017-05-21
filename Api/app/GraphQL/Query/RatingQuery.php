<?php
namespace App\GraphQL\Query;

use GraphQL;
use GraphQL\Type\Definition\ResolveInfo;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Query;
use JWTAuth;

class RatingQuery extends Query
{
    public function type()
    {
        return Type::listOf(GraphQL::type('Rating'));
    }

    public function args()
    {
        return [
            'token' => ['name' => 'token', 'type' => Type::nonNull(Type::string())],
            'track_id' => ['name' => 'track_id', 'type' => Type::int()],
            'track_ids' => ['name' => 'track_ids', 'type' => Type::listOf(Type::int())]
        ];
    }

    public function resolve($root, $args, $context, ResolveInfo $info)
    {
        $user = JWTAuth::authenticate($args['token']);
        if (!$user) {
            return null;
        }
        $rating = null;
        if (isset($args['track_ids'])) {
            $rating = $user->ratings()->whereIn('track_id', $args['track_ids'])->get();
        } elseif (isset($args['track_id'])) {
            $rating = $user->ratings()->where('track_id', $args['track_id'])->get();
        }
        return $rating;
    }

}
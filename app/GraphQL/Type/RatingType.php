<?php

namespace App\GraphQL\Type;

use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Type as GraphQLType;


class RatingType extends GraphQLType
{

    public function fields()
    {
        return [
            'id' => [
                'type' => Type::nonNull(Type::int()),
                'description' => 'The id of the tag'
            ],
            'value' => [
                'type' => Type::int(),
                'description' => 'The value of rating'
            ],
            'track_id' => [
                'type' => Type::int(),
                'description' => 'The track id of rating'
            ],
            'track' => [
                'type' => GraphQL::type('Track'),
                'description' => 'The track id of rating'
            ]
        ];
    }

}
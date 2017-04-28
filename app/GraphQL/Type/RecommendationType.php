<?php

namespace App\GraphQL\Type;

use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Type as GraphQLType;


class RecommendationType extends GraphQLType
{

    public function fields()
    {
        return [
            'id' => [
                'type' => Type::nonNull(Type::int()),
                'description' => 'The id of the tag'
            ],
            'status' => [
                'type' => Type::string(),
                'description' => 'The status of recommendation'
            ],
            'ratings' => [
                'type' => Type::listOf(GraphQL::type('Rating')),
                'description' => 'Ratings provided by user for this recommendation'
            ],
            'tracks' => [
                'type' => Type::listOf(GraphQL::type('Track')),
                'description' => 'Recommended tracks'
            ],
            'user' => [
                'type' => GraphQL::type('BulbulUser'),
                'description' => 'Owner of the recommendation'
            ]

        ];
    }

}
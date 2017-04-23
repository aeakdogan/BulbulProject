<?php

namespace App\GraphQL\Type;

use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Type as GraphQLType;


class TagType extends GraphQLType
{

    public function fields()
    {
        return [
            'id' => [
                'type' => Type::nonNull(Type::int()),
                'description' => 'The id of the tag'
            ],
            'tag_name' => [
                'type' => Type::nonNull(Type::string()),
                'description' => 'The name of tag'
            ],
            'tag_url' => [
                'type' => Type::string(),
                'description' => 'The url of tag'
            ],
            'artists' => [
                'type' => Type::listOf(GraphQL::type('Artist')),
                'description' => 'Artists tagged by this tag'
            ],
            'tracks' => [
                'type' => Type::listOf(GraphQL::type('Track')),
                'description' => 'Tracks tagged by this tag'
            ]

        ];
    }

}
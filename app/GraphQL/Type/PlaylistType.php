<?php

namespace App\GraphQL\Type;

use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Type as GraphQLType;


class PlaylistType extends GraphQLType
{

    public function fields()
    {
        return [
            'id' => [
                'type' => Type::nonNull(Type::int()),
                'description' => 'The id of the playlist'
            ],
            'name' => [
                'type' => Type::nonNull(Type::string()),
                'description' => 'The name of playlist'
            ],
            'songs' => [
                'type' => Type::listOf(GraphQL::type('Song')),
                'description' => 'Songs of playlist'
            ],
            'followers' => [
                'type' => Type::listOf(GraphQL::type('BulbulUser')),
                'description' => 'Followers of playlist'
            ],
            'creator' => [
                'type' => GraphQL::type('BulbulUser'),
                'description' => 'The creator of playlist'
            ]

        ];
    }

}
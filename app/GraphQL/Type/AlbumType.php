<?php

namespace App\GraphQL\Type;

use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Type as GraphQLType;


class AlbumType extends GraphQLType
{

    public function fields()
    {
        return [
            'id' => [
                'type' => Type::nonNull(Type::int()),
                'description' => 'The id of the album'
            ],
            'name' => [
                'type' => Type::nonNull(Type::string()),
                'description' => 'The name of album'
            ],
            'year' => [
                'type' => Type::int(),
                'description' => 'The year of album'
            ],
            'coverPhotoUrl' => [
                'type' => Type::string(),
                'description' => 'The url of cover photo'
            ],
            'songs' => [
                'type' => Type::listOf(GraphQL::type('Song')),
                'description' => 'Songs of album'
            ],
            'artists' => [
                'type' => Type::listOf(GraphQL::type('Artist')),
                'description' => 'Artists of album'
            ]
        ];
    }

}
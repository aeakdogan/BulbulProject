<?php

namespace App\GraphQL\Type;

use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Type as GraphQLType;


class ArtistType extends GraphQLType
{

    public function fields()
    {
        return [
            'id' => [
                'type' => Type::nonNull(Type::int()),
                'description' => 'The id of the artist'
            ],
            'name' => [
                'type' => Type::nonNull(Type::string()),
                'description' => 'The name of artist'
            ],
            'songs' => [
                'type' => Type::listOf(GraphQL::type('Song')),
                'description' => 'Songs of artist'
            ],
            'albums' => [
                'type' => Type::listOf(GraphQL::type('Album')),
                'description' => 'Albums of artist'
            ],
        ];
    }

}
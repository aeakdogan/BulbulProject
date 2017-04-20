<?php

namespace App\GraphQL\Type;

use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Type as GraphQLType;
use App\GraphQL\Fields\TracksField;


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
            'mbid' => [
                'type' => Type::string(),
                'description' => 'The mbid of album'
            ],
            'image' => [
                'type' => Type::string(),
                'description' => 'The url of album image'
            ],
            'lastfm_url' => [
                'type' => Type::string(),
                'description' => 'The lastfm url of album'
            ],
            'tracks' => TracksField::class,
            'tracksCount' =>[
                'type' => Type::int(),
                'description' => 'The number of tracks'
            ],
            'artists' => [
                'type' => Type::listOf(GraphQL::type('Artist')),
                'description' => 'Artists of album'
            ]
        ];
    }

}
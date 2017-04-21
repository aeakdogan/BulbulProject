<?php

namespace App\GraphQL\Type;

use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Type as GraphQLType;
use App\GraphQL\Fields\TracksField;


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
            'tracks' => TracksField::class,
            'tracksCount' =>[
                'type' => Type::int(),
                'description' => 'The number of tracks'
            ],
            'albums' => [
                'type' => Type::listOf(GraphQL::type('Album')),
                'description' => 'Albums of artist'
            ],
            'albumsCount' =>[
                'type' => Type::int(),
                'description' => 'The number of albums'
            ],
            'tags'=>[
                'type' => Type::listOf(GraphQL::type('Tag')),
                'description' => 'Tags of artist'
            ],
            'image' => [
                'type' => Type::string(),
                'description' => 'The url of album image'
            ],
            'biography_text' => [
                'type' => Type::string(),
                'description' => 'The biography of artist'
            ],
            'lastfm_url' => [
                'type' => Type::string(),
                'description' => 'The lastfm url of artist'
            ],
            'mbid' => [
                'type' => Type::string(),
                'description' => 'The mbid of artist'
            ],
            'listener_count' => [
                'type' => Type::int(),
                'description' => 'The listener count of artist'
            ],
            'play_count' => [
                'type' => Type::int(),
                'description' => 'The play count of artist'
            ],
            'biography_url' => [
                'type' => Type::string(),
                'description' => 'The url of artist biography'
            ],
        ];
    }

}
<?php

namespace App\GraphQL\Type;

use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Type as GraphQLType;


class BulbulUserType extends GraphQLType
{

    public function fields()
    {
        return [
            'id' => [
                'type' => Type::nonNull(Type::int()),
                'description' => 'The id of the user'
            ],
            'name' => [
                'type' => Type::nonNull(Type::string()),
                'description' => 'The name of user'
            ],
            'lastActivePlaylist' => [
                'type' => Type::nonNull(GraphQL::type('Playlist')),
                'description' => 'The last active playlist of user'
            ],
            'followedTracks' => [
                'type' => Type::listOf(GraphQL::type('Track')),
                'description' => 'The followed songs of user'
            ],
            'followedArtists' => [
                'type' => Type::listOf(GraphQL::type('Artist')),
                'description' => 'The followed artists of user'
            ],
            'followedPlaylists' => [
                'type' => Type::listOf(GraphQL::type('Playlist')),
                'description' => 'The followed playlists of user'
            ],
            'listenedTracks' => [
                'type' => Type::listOf(GraphQL::type('Track')),
                'description' => 'The listened songs of user'
            ],
            'listenedPlaylists' => [
                'type' => Type::listOf(GraphQL::type('Playlist')),
                'description' => 'The listened playlists of user'
            ],
            'playlists' => [
                'type' => Type::listOf(GraphQL::type('Playlist')),
                'description' => 'Playists created by user'
            ]
        ];
    }

}
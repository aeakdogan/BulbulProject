<?php

namespace App\GraphQL\Type;

use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Type as GraphQLType;


class TrackType extends GraphQLType
{

    public function fields()
    {
        return [
            'id' => [
                'type' => Type::nonNull(Type::int()),
                'description' => 'The id of the track'
            ],
            'name' => [
                'type' => Type::nonNull(Type::string()),
                'description' => 'The name of track'
            ],
            'albums' => [
                'type' => Type::listOf(GraphQL::type('Album')),
                'description' => 'Albums the track appears in'
            ],
            'artists' => [
                'type' => Type::listOf(GraphQL::type('Artist')),
                'description' => 'Artists of album'
            ],
            'mbid' => [
                'type' => Type::string(),
                'description' => 'The name of track'
            ],
            'spotify_album_id' => [
                'type' => Type::string(),
                'description' => 'Spotify album id'
            ],
            'spotify_album_img' => [
                'type' => Type::string(),
                'description' => 'Album image url'
            ],
            'spotify_artist_url' => [
                'type' => Type::string(),
                'description' => 'Spotify url of artist'
            ],
            'spotify_track_preview_url' => [
                'type' => Type::string(),
                'description' => 'Spotify track preview url'
            ],
            'duration' => [
                'type' => Type::int(),
                'description' => 'Spotify track url'
            ],
            'spotify_track_url' => [
                'type' => Type::string(),
                'description' => 'Spotify track url'
            ],
            'spotify_artist_id' => [
                'type' => Type::string(),
                'description' => 'Spotify artist id'
            ],
            'lastfm_url' => [
                'type' => Type::string(),
                'description' => 'Lastfm url'
            ],
            'spotify_track_id' => [
                'type' => Type::string(),
                'description' => 'Spotify track id'
            ],
            'spotify_track_popularity' => [
                'type' => Type::int(),
                'description' => 'Spotify track popularity'
            ],
            'spotify_album_url' => [
                'type' => Type::string(),
                'description' => 'Spotify album url'
            ],
        ];
    }

}
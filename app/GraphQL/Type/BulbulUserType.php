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
            'username' => [
                'type' => Type::nonNull(Type::string()),
                'description' => 'The name of user'
            ],
            'image' => [
                'type' => Type::nonNull(Type::string()),
                'description' => 'The image of user'
            ],
            'lastActivePlaylist' => [
                'type' => Type::nonNull(GraphQL::type('Playlist')),
                'description' => 'The last active playlist of user'
            ],
            'followedTracks' => [
                'type' => Type::listOf(GraphQL::type('Track')),
                'description' => 'The followed songs of user',
                'args' => [
                    'limit' => ['description' => 'Number of tracks to be fetched', 'type' => Type::int()],
                    'skip' => ['description' => 'Number of tracks to be skipped', 'type' => Type::int()],
                ]
            ],
            'followedArtists' => [
                'type' => Type::listOf(GraphQL::type('Artist')),
                'description' => 'The followed artists of user',
                'args' => [
                    'limit' => ['description' => 'Number of artists to be fetched', 'type' => Type::int()],
                    'skip' => ['description' => 'Number of artists to be skipped', 'type' => Type::int()],
                ]
            ],
            'followedAlbums' => [
                'type' => Type::listOf(GraphQL::type('Album')),
                'description' => 'The followed albums of user',
                'args' => [
                    'limit' => ['description' => 'Number of albums to be fetched', 'type' => Type::int()],
                    'skip' => ['description' => 'Number of albums to be skipped', 'type' => Type::int()],
                ]
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
            ],
            'followers' => [
                'type' => Type::listOf(GraphQL::type('BulbulUser')),
                'description' => 'Followers of user',
                'args' => [
                    'limit' => ['description' => 'Number of followers to be fetched', 'type' => Type::int()],
                    'skip' => ['description' => 'Number of followers to be skipped', 'type' => Type::int()],
                ]
            ],
            'followersCount' => [
                'type' => Type::int(),
                'description' => 'The number of followers'
            ],
            'followedUsers' => [
                'type' => Type::listOf(GraphQL::type('BulbulUser')),
                'description' => 'Followed users of user',
                'args' => [
                    'limit' => ['description' => 'Number of followed users to be fetched', 'type' => Type::int()],
                    'skip' => ['description' => 'Number of followed users to be skipped', 'type' => Type::int()],
                ]
            ],
            'followedUsersCount' => [
                'type' => Type::int(),
                'description' => 'The number of followedUsers'
            ]
        ];
    }

    protected function resolveFollowersField($root, $args)
    {
        if (!isset($args['limit']) && !isset($args['skip'])) {
            if (!$root->relationLoaded('followers')) $root->load('followers');
            return $root->getRelation('followers');
        }
        $limit = isset($args['limit']) ? $args['limit'] : 100;
        $skip = isset($args['skip']) ? $args['skip'] : 0;
        return $root->followers()->take($limit)->skip($skip)->get();
    }

    protected function resolveFollowedUsersField($root, $args)
    {
        if (!isset($args['limit']) && !isset($args['skip'])) {
            if (!$root->relationLoaded('followedUsers')) $root->load('followedUsers');
            return $root->getRelation('followedUsers');
        }
        $limit = isset($args['limit']) ? $args['limit'] : 100;
        $skip = isset($args['skip']) ? $args['skip'] : 0;
        return $root->followedUsers()->take($limit)->skip($skip)->get();
    }

    protected function resolveFollowedTracksField($root, $args)
    {
        if (!isset($args['limit']) && !isset($args['skip'])) {
            if (!$root->relationLoaded('followedTracks')) $root->load('followedTracks');
            return $root->getRelation('followedTracks');
        }
        $limit = isset($args['limit']) ? $args['limit'] : 100;
        $skip = isset($args['skip']) ? $args['skip'] : 0;
        return $root->followedTracks()->take($limit)->skip($skip)->get();
    }

    protected function resolveFollowedArtistsField($root, $args)
    {
        if (!isset($args['limit']) && !isset($args['skip'])) {
            if (!$root->relationLoaded('followedArtists')) $root->load('followedArtists');
            return $root->getRelation('followedArtists');
        }
        $limit = isset($args['limit']) ? $args['limit'] : 100;
        $skip = isset($args['skip']) ? $args['skip'] : 0;
        return $root->followedArtists()->take($limit)->skip($skip)->get();
    }

    protected function resolveFollowedAlbumsField($root, $args)
    {
        if (!isset($args['limit']) && !isset($args['skip'])) {
            if (!$root->relationLoaded('followedAlbums')) $root->load('followedAlbums');
            return $root->getRelation('followedAlbums');
        }
        $limit = isset($args['limit']) ? $args['limit'] : 100;
        $skip = isset($args['skip']) ? $args['skip'] : 0;
        return $root->followedAlbums()->take($limit)->skip($skip)->get();
    }


}
<?php

namespace App\GraphQL\Mutation;
use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Mutation;
use JWTAuth;
use App\Playlist;

class CreatePlaylistMutation extends Mutation
{
    protected $attributes = [
        'name' => 'createPlaylist'
    ];

    public function type()
    {
        return GraphQL::type('Artist');
    }

    public function args()
    {
        return [
            'name' => ['name' => 'name', 'type' => Type::nonNull(Type::string())],
            'token' => ['name' => 'token', 'type' => Type::nonNull(Type::string())]
        ];
    }

    public function resolve($root, $args)
    {
        $user = JWTAuth::authenticate($args['token']);
        $playlist = Playlist::create(['name'=>$args['name']]);
        if(!$user || !$playlist)
        {
            return null;
        }

        $user->playlists()->attach($playlist);

        return $playlist;
    }
}
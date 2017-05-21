<?php

namespace App\GraphQL\Mutation;

use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Mutation;
use JWTAuth;
use App\Playlist;
use App\Track;

class AddTrackToPlaylist extends Mutation
{
    protected $attributes = [
        'name' => 'addTrackToPlaylist'
    ];

    public function type()
    {
        return GraphQL::type('Playlist');
    }

    public function args()
    {
        return [
            'id' => ['name' => 'id', 'type' => Type::nonNull(Type::int())],
            'token' => ['name' => 'token', 'type' => Type::nonNull(Type::string())],
            'track_id' => ['name' => 'track_id', 'type' => Type::int()],
            'track_ids' => ['name' => 'track_ids', 'type' => Type::listOf(Type::int())]
        ];
    }

    public function resolve($root, $args)
    {
        $user = JWTAuth::authenticate($args['token']);
        $playlist = Playlist::find($args['id']);

        if (!$user || !$playlist || $user->id != $playlist->creator->id) {
            return null;
        }
        $tracks = $playlist->tracks;
        if (isset($args['track_ids'])) {
            $removeTracks = $tracks->whereIn('id', $args['track_ids'])->pluck('id')->toArray();
            $diff = array_diff($args['track_ids'], $removeTracks );
            $playlist->tracks()->attach($diff);
        } elseif (isset($args['track_id'])) {
            if($tracks->where('id', $args['track_id'])->count()==0){
                $playlist->tracks()->attach($args['track_id']);
            }
        }
        $playlist->load('tracks.artists');
        return $playlist;
    }
}
<?php

namespace App\GraphQL\Mutation;
use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Mutation;
use JWTAuth;
use App\Playlist;
use App\Recommendation;
use App\Genre;

class SaveAsPlaylistMutation extends Mutation
{
    protected $attributes = [
        'name' => 'saveAsPlaylist'
    ];

    public function type()
    {
        return GraphQL::type('Playlist');
    }

    public function args()
    {
        return [
            'recommendation_id' => ['name' => 'recommendation_id', 'type' => Type::nonNull(Type::int())],
            'token' => ['name' => 'token', 'type' => Type::nonNull(Type::string())]
        ];
    }

    public function resolve($root, $args)
    {
        $user = JWTAuth::authenticate($args['token']);
        $recommendation = Recommendation::with('tracks')->find($args['recommendation_id']);
        if(!$recommendation || !$user){
            return null;
        }
        $genres = Genre::whereIn('id',$recommendation->genre_ids)->get()->pluck('name')->toArray();
        $playlist = Playlist::create([
            'name'=>'Recommended '.implode(' ',$genres).'tracks for '.$user->username.' ',
            'type'=>'AUTO_GENERATED'
        ]);
        $playlist->tracks()->attach($recommendation->tracks);

        $user->playlists()->attach($playlist);
        $playlist->load('tracks');
        return $playlist;

    }
}
<?php
namespace App\GraphQL\Query;

use GraphQL;
use GraphQL\Type\Definition\ResolveInfo;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Query;
use App\Playlist;

class PlaylistQuery extends Query
{
    public function type()
    {
        return Type::listOf(GraphQL::type('Playlist'));
    }

    public function args()
    {
        return [
            'id' => ['name' => 'id', 'type' => Type::int()],
            'limit' => ['name' => 'limit', 'type' => Type::int()],
            'skip' => ['name' => 'skip', 'type' => Type::int()],
            'ids' => ['name' => 'ids', 'type' => Type::listOf(Type::int())],
        ];
    }

    public function resolve($root, $args,$context, ResolveInfo $info)
    {
        $playlists = Playlist::query();
        $fields = $info->getFieldSelection($depth = 1);

        foreach ($fields as $field => $keys) {
            if ($field === 'tracks') {
                $playlists->with('tracks.artists');
            }

            if ($field === 'tracksCount') {
                $playlists->with('tracks');
            }

        }

        if (isset($args['id'])) {
            $playlists->where('id', $args['id']);
        } else if (isset($args['ids'])) {
            $playlists->whereIn('id',$args['ids']);
        } else {
            $limit = isset($args['limit']) ? $args['limit'] : 100;
            $skip = isset($args['skip']) ? $args['skip'] : 0;
            $playlists->take($limit)->skip($skip);
        }
        return $playlists->get();
    }

}
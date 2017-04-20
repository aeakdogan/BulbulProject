<?php
namespace App\GraphQL\Query;

use GraphQL;
use GraphQL\Type\Definition\ResolveInfo;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Query;
use App\Album;

class AlbumQuery extends Query
{
    public function type()
    {
        return Type::listOf(GraphQL::type('Album'));
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

    public function resolve($root, $args, $context, ResolveInfo $info)
    {
        $albums = Album::query();
        $fields = $info->getFieldSelection($depth = 1);

        foreach ($fields as $field => $keys) {
            if ($field === 'artists') {
                $albums->with('artists.tracks');
            }
            if ($field === 'tracks') {
                 $albums->with('tracks.artists');
            }
            if ($field === 'tracksCount') {
                $albums->with('tracks');
            }

        }

        if (isset($args['id'])) {
            $albums->where('id', $args['id']);
        } else if (isset($args['ids'])) {
            $albums->whereIn('id', $args['ids']);
        } else {
            $limit = isset($args['limit']) ? $args['limit'] : 100;
            $skip = isset($args['skip']) ? $args['skip'] : 0;
            $albums->take($limit)->skip($skip);
        }
        return $albums->get();
    }

}
<?php
namespace App\GraphQL\Query;

use GraphQL;
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

    public function resolve($root, $args)
    {

        if (isset($args['id'])) {
            return Playlist::where('id', $args['id'])->get();
        } else if (isset($args['ids'])) {
            return Playlist::findMany($args['ids']);
        } else {
            $limit = isset($args['limit']) ? $args['limit'] : 100;
            $skip = isset($args['skip']) ? $args['skip'] : 0;
            return Playlist::take($limit)->skip($skip)->get();
        }
    }

}
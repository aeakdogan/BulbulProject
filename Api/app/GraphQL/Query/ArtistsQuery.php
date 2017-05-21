<?php
namespace App\GraphQL\Query;

use GraphQL;
use GraphQL\Type\Definition\ResolveInfo;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Query;
use App\Artist;


class ArtistsQuery extends Query
{
    public function type()
    {
        return Type::listOf(GraphQL::type('Artist'));
    }

    public function args()
    {
        return [
            'id' => ['name' => 'id', 'type' => Type::int()],
            'limit' => ['name' => 'limit', 'type' => Type::int()],
            'skip' => ['name' => 'skip', 'type' => Type::int()],
            'ids' => ['name' => 'ids', 'type' => Type::listOf(Type::int())],
            'query' => ['name' => 'query', 'type' => Type::string()]
        ];
    }

    public function resolve($root, $args, $context, ResolveInfo $info)
    {
        $artists = Artist::query();
        $fields = $info->getFieldSelection($depth = 1);

        foreach ($fields as $field => $keys) {
            if ($field === 'albums') {
                $artists->with('albums.artists','albums.tracks');
            }
            if ($field === 'tracks') {
                $artists->with('tracks.artists');
            }
            if ($field === 'tags') {
                $artists->with('tags');
            }
        }
        if (isset($args['id'])) {
            $artists->where('id', $args['id']);
        } else if (isset($args['ids'])) {
            $artists->whereIn('id',$args['ids']);
        } else {
            $limit = isset($args['limit']) ? $args['limit'] : 100;
            $skip = isset($args['skip']) ? $args['skip'] : 0;
            if(isset($args['query'])) Artist::search($artists, $args['query'],$limit,$skip);
            $artists->take($limit)->skip($skip);
        }
        return $artists->get();
    }

}
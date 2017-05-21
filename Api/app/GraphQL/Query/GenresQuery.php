<?php
namespace App\GraphQL\Query;

use GraphQL;
use GraphQL\Type\Definition\ResolveInfo;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Query;
use App\Genre;

class GenresQuery extends Query
{
    public function type()
    {
        return Type::listOf(GraphQL::type('Genre'));
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

        $genres = Genre::query();

        if (isset($args['id'])) {
             $genres->where('id', $args['id']);
        } else if (isset($args['ids'])) {
             $genres->whereIn('id',$args['ids']);
        } else {
            if(isset($args['query'])) $genres->where('name','CONTAINS',$args['query'] );
            $limit = isset($args['limit']) ? $args['limit'] : 100;
            $skip = isset($args['skip']) ? $args['skip'] : 0;
             $genres->take($limit)->skip($skip);
        }
        $genres->orderBy('name','ASC');
        return $genres->get();
    }

}
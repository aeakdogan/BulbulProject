<?php
namespace App\GraphQL\Query;
use GraphQL;
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
        ];
    }

    public function resolve($root, $args)
    {

        if(isset($args['id']))
        {
            return Album::where('id' , $args['id'])->get();
        }

        else
        {
            return Album::all();
        }
    }

}
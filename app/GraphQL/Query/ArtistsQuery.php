<?php
namespace App\GraphQL\Query;
use GraphQL;
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
        ];
    }

    public function resolve($root, $args)
    {

        if(isset($args['id']))
        {
            return Artist::where('id' , $args['id'])->get();
        }

        else
        {
            return Artist::all();
        }
    }

}
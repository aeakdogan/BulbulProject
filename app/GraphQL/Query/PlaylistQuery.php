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
        ];
    }

    public function resolve($root, $args)
    {

        if(isset($args['id']))
        {
            return Playlist::where('id' , $args['id'])->get();
        }

        else
        {
            return Playlist::all();
        }
    }

}
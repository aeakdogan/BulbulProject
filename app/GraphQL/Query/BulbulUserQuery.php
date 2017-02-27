<?php
namespace App\GraphQL\Query;
use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Query;
use App\BulbulUser;

class BulbulUserQuery extends Query
{
    public function type()
    {
        return Type::listOf(GraphQL::type('BulbulUser'));
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
            return BulbulUser::where('id' , $args['id'])->get();
        }

        else
        {
            return BulbulUser::all();
        }
    }

}
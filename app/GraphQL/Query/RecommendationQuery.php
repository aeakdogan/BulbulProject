<?php
namespace App\GraphQL\Query;

use App\Recommendation;
use GraphQL;
use GraphQL\Type\Definition\ResolveInfo;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Query;
use App\Tag;

class RecommendationQuery extends Query
{
    public function type()
    {
        return Type::listOf(GraphQL::type('Recommendation'));
    }

    public function args()
    {
        return [
            'id' => ['name' => 'id', 'type' => Type::int()],
            'ids' => ['name' => 'ids', 'type' => Type::listOf(Type::int())],

        ];
    }

    public function resolve($root, $args, $context, ResolveInfo $info)
    {
        $recommendations = Recommendation::query();

        if (isset($args['id'])) {
            return $recommendations->where('id', $args['id'])->get();
        } else if (isset($args['ids'])) {
            return $recommendations->whereIn('id',$args['ids'])->get();
        }
        return null;
    }

}
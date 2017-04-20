<?php
namespace App\GraphQL\Query;

use GraphQL;
use GraphQL\Type\Definition\ResolveInfo;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Query;
use App\Track;


class TrackQuery extends Query
{
    public function type()
    {
        return Type::listOf(GraphQL::type('Track'));
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
        $tracks = Track::query();
        $fields = $info->getFieldSelection($depth = 1);

        foreach ($fields as $field => $keys) {
            if ($field === 'artists') {
                $tracks->with('artists');
            }
            if ($field === 'tracks') {
                $tracks->with('tracks');
            }
        }
        if (isset($args['id'])) {
            $tracks->where('id', $args['id']);
        } else if (isset($args['ids'])) {
            $tracks->whereIn('id',$args['ids']);
        } else {
            $limit = isset($args['limit']) ? $args['limit'] : 100;
            $skip = isset($args['skip']) ? $args['skip'] : 0;
            $tracks->take($limit)->skip($skip);
        }

        return $tracks->get();
    }
}
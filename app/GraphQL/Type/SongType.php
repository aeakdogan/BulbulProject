<?php

namespace App\GraphQL\Type;

use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Type as GraphQLType;


class SongType extends GraphQLType
{

    public function fields()
    {
        return [
            'id' => [
                'type' => Type::nonNull(Type::int()),
                'description' => 'The id of the song'
            ],
            'name' => [
                'type' => Type::nonNull(Type::string()),
                'description' => 'The name of song'
            ],
            'year' =>[
                'type' =>Type::int(),
                'description' => 'The year of song'
            ],
            'duration' =>[
                'type' =>Type::int(),
                'description' => 'The duration of song'
            ],
            'genre' =>[
                'type' =>Type::int(),
                'description' => 'The genre of song'
            ],
        ];
    }

}
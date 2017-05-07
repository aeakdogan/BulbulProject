<?php

namespace App\GraphQL\Type;

use App\Genre;
use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Type as GraphQLType;
use App\GraphQL\Fields\TracksField;


class GenreType extends GraphQLType
{

    public function fields()
    {
        return [
            'id' => [
                'type' => Type::nonNull(Type::int()),
                'description' => 'The id of the genre'
            ],
            'name' => [
                'type' => Type::nonNull(Type::string()),
                'description' => 'The name of genre'
            ],
            'icon_url' => [
                'type' => Type::string(),
                'description' => 'The name of genre'
            ],
            'tracks' => TracksField::class,
            'artists' => [
                'type' => Type::listOf(GraphQL::type('Artist')),
                'description' => 'Artists of genre',
                'args' => [
                    'limit' => ['description' => 'Number of artists to be fetched', 'type' => Type::int()],
                    'skip' => ['description' => 'Number of artists to be skipped', 'type' => Type::int()],
                ]
            ],
            'topArtists' => [
                'type' => Type::listOf(GraphQL::type('Artist')),
                'description' => 'Top artists of genre',
                'args' => [
                    'limit' => ['description' => 'Number of artists to be fetched', 'type' => Type::int()],
                    'skip' => ['description' => 'Number of artists to be skipped', 'type' => Type::int()],
                ]
            ],

        ];
    }

    protected function resolveIconUrlField($root, $args){
        return env('APP_URL', 'http://207.154.244.207/').'i/'.$root->icon_url;
    }

    protected function resolveTracksField($root, $args)
    {
        if (!isset($args['limit']) && !isset($args['skip'])) {
            if (!$root->relationLoaded('tracks')) $root->load('tracks');
            return $root->tracks;
        }
        $limit = isset($args['limit']) ? $args['limit'] : 100;
        $skip = isset($args['skip']) ? $args['skip'] : 0;
        return $root->tracks()->take($limit)->skip($skip)->get();
    }

    protected function resolveArtistsField($root, $args)
    {
        if (!isset($args['limit']) && !isset($args['skip'])) {
            if (!$root->relationLoaded('artists')) $root->load('artists');
            return $root->getRelation('artists');
        }
        $limit = isset($args['limit']) ? $args['limit'] : 100;
        $skip = isset($args['skip']) ? $args['skip'] : 0;
        return $root->artists()->take($limit)->skip($skip)->get();
    }

    protected function resolveTopArtistsField($root, $args)
    {
        $limit = isset($args['limit']) ? $args['limit'] : 100;
        $skip = isset($args['skip']) ? $args['skip'] : 0;
        return $root->getTopArtists($limit, $skip);
    }

}
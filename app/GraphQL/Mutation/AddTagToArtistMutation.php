<?php

namespace App\GraphQL\Mutation;
use GraphQL;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Mutation;
use App\Artist;
use App\Tag;

class AddTagToArtistMutation extends Mutation
{
    protected $attributes = [
        'name' => 'addTagToArtist'
    ];

    public function type()
    {
        return GraphQL::type('Artist');
    }

    public function args()
    {
        return [
            'id' => ['name' => 'id', 'type' => Type::nonNull(Type::int())],
            'tag_id' => ['name' => 'tag_id', 'type' => Type::nonNull(Type::int())],
        ];
    }

    public function resolve($root, $args)
    {
        $artist = Artist::find($args['id']);
        $tag = Tag::find($args['tag_id']);
        if(!$artist || !$tag)
        {
            return null;
        }

        $tag->artists()->attach($artist);

        return Artist::find($args['id']);
    }
}
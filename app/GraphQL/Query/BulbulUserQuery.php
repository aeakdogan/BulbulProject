<?php
namespace App\GraphQL\Query;

use GraphQL;
use GraphQL\Type\Definition\ResolveInfo;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Query;
use App\BulbulUser;
use JWTAuth;

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
            'limit' => ['name' => 'limit', 'type' => Type::int()],
            'skip' => ['name' => 'skip', 'type' => Type::int()],
            'ids' => ['name' => 'ids', 'type' => Type::listOf(Type::int())],
            'token' => ['name' => 'token', 'type' => Type::string()]
        ];
    }

    public function resolve($root, $args, $context, ResolveInfo $info)
    {
        $users = BulbulUser::query();

        $fields = $info->getFieldSelection($depth = 1);

        foreach ($fields as $field => $keys) {
            if ($field === 'listenedTracks') {
                $users->with('listenedTracksRelation.artists', 'listenedTracksRelation.albums');
            }
            if ($field === 'listenedArtists') {
                $users->with('listenedArtists.albums');
            }
            if ($field === 'listenedAlbums') {
                $users->with('listenedAlbums.artists', 'listenedAlbums.tracks');
            }
            if ($field === 'followers') {
                $users->with('followers');
            }
            if ($field === 'followedUsers') {
                $users->with('followedUsers');
            }

        }

        if (isset($args['token'])){
            $user = JWTAuth::authenticate($args['token']);
            $users->where('id', $user->id);
        } else if (isset($args['id'])) {
            $users->where('id', $args['id']);
        } else if (isset($args['ids'])) {
            $users->whereIn('id',['ids']);
        } else {
            $limit = isset($args['limit']) ? $args['limit'] : 100;
            $skip = isset($args['skip']) ? $args['skip'] : 0;
            $users->take($limit)->skip($skip);
        }
        return $users->get();
    }

}
<?php
namespace App\GraphQL\Query;

use GraphQL;
use GraphQL\Type\Definition\ResolveInfo;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Query;
use App\Track;
use Illuminate\Support\Facades\Log;


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
            'min_acousticness' => ['name' => 'min_acousticness', 'type' => Type::float()],
            'max_acousticness' => ['name' => 'max_acousticness', 'type' => Type::float()],
            'min_liveness' => ['name' => 'min_liveness', 'type' => Type::float()],
            'max_liveness' => ['name' => 'max_liveness', 'type' => Type::float()],
            'min_speechiness' => ['name' => 'min_speechiness', 'type' => Type::float()],
            'max_speechiness' => ['name' => 'max_speechiness', 'type' => Type::float()],
            'min_valence' => ['name' => 'min_valence', 'type' => Type::float()],
            'max_valence' => ['name' => 'max_valence', 'type' => Type::float()],
            'min_danceability' => ['name' => 'min_danceability', 'type' => Type::float()],
            'max_danceability' => ['name' => 'max_danceability', 'type' => Type::float()],
            'min_instrumentalness' => ['name' => 'min_instrumentalness', 'type' => Type::float()],
            'max_instrumentalness' => ['name' => 'max_instrumentalness', 'type' => Type::float()],
            'min_tempo' => ['name' => 'min_tempo', 'type' => Type::float()],
            'max_tempo' => ['name' => 'max_tempo', 'type' => Type::float()],
            'min_energy' => ['name' => 'min_energy', 'type' => Type::float()],
            'max_energy' => ['name' => 'max_energy', 'type' => Type::float()],
            'min_loudness' => ['name' => 'min_loudness', 'type' => Type::float()],
            'max_loudness' => ['name' => 'max_loudness', 'type' => Type::float()],
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
            if ($field === 'albums') {
                $tracks->with('albums');
            }
            if ($field === 'tags') {
                $tracks->with('tags');
            }
        }
        if (isset($args['id'])) {
            $tracks->where('id', $args['id']);
        } else if (isset($args['ids'])) {
            $tracks->whereIn('id',$args['ids']);
        } else {
            if(isset($args['max_acousticness'])) {
                $tracks->where('audio_features_acousticness', '<=', $args['max_acousticness']);
            }
            if(isset($args['max_liveness'])) {
                $tracks->where('audio_features_liveness', '<=', $args['max_liveness']);
            }
            if(isset($args['max_speechiness'])) {
                $tracks->where('audio_features_speechiness', '<=', $args['max_speechiness']);
            }
            if(isset($args['max_valence'])) {
                $tracks->where('audio_features_valence', '<=', $args['max_valence']);
            }
            if(isset($args['max_danceability'])) {
                $tracks->where('audio_features_danceability', '<=', $args['max_danceability']);
            }
            if(isset($args['max_instrumentalness'])) {
                $tracks->where('audio_features_instrumentalness', '<=', $args['max_instrumentalness']);
            }
            if(isset($args['max_tempo'])) {
                $tracks->where('audio_features_tempo', '<=', $args['max_tempo']);
            }
            if(isset($args['max_energy'])) {
                $tracks->where('audio_features_energy', '<=', $args['max_energy']);
            }
            if(isset($args['max_loudness'])) {
                $tracks->where('audio_features_loudness', '<=', $args['max_loudness']);
            }

            if(isset($args['min_acousticness'])) {
                $tracks->where('audio_features_acousticness', '>=', $args['min_acousticness']);
            }
            if(isset($args['min_liveness'])) {
                $tracks->where('audio_features_liveness', '>=', $args['min_liveness']);
            }
            if(isset($args['min_speechiness'])) {
                $tracks->where('audio_features_speechiness', '>=', $args['min_speechiness']);
            }
            if(isset($args['min_valence'])) {
                $tracks->where('audio_features_valence', '>=', $args['min_valence']);
            }
            if(isset($args['min_danceability'])) {
                $tracks->where('audio_features_danceability', '>=', $args['min_danceability']);
            }
            if(isset($args['min_instrumentalness'])) {
                $tracks->where('audio_features_instrumentalness', '>=', $args['min_instrumentalness']);
            }
            if(isset($args['min_tempo'])) {
                $tracks->where('audio_features_tempo', '>=', $args['min_tempo']);
            }
            if(isset($args['min_energy'])) {
                $tracks->where('audio_features_energy', '>=', $args['min_energy']);
            }
            if(isset($args['min_loudness'])) {
                $tracks->where('audio_features_loudness', '>=', $args['min_loudness']);
            }

            $limit = isset($args['limit']) ? $args['limit'] : 100;
            $skip = isset($args['skip']) ? $args['skip'] : 0;
            $tracks->take($limit)->skip($skip);
        }

        return $tracks->get();
    }
}
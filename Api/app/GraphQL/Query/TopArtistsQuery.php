<?php
namespace App\GraphQL\Query;

use GraphQL;
use GraphQL\Type\Definition\ResolveInfo;
use GraphQL\Type\Definition\Type;
use Folklore\GraphQL\Support\Query;
use Everyman\Neo4j\Cypher\Query as DBQuery;
use DB;
use App\Artist;

class TopArtistsQuery extends Query
{
    public function type()
    {
        return Type::listOf(GraphQL::type('Artist'));
    }

    public function args()
    {
        return [
            'genre_ids' => ['name' => 'genre_ids', 'type' => Type::nonNull(Type::listOf(Type::int()))],
            'limit' => ['name' => 'limit', 'type' => Type::int()],
            'skip' => ['name' => 'skip', 'type' => Type::int()],
        ];
    }

    public function resolve($root, $args, $context, ResolveInfo $info)
    {

        $limit = isset($args['limit']) ? $args['limit'] : 100;
        $skip = isset($args['skip']) ? $args['skip'] : 0;
        $queryString = 'WITH {genre_ids} as searched_genres MATCH (a:Artist)-[:IN]->(g:Genre) WITH a, filter(genre IN collect(id(g)) WHERE genre IN searched_genres) as matched_genres, searched_genres WHERE size(matched_genres) = size(searched_genres) OR size(matched_genres) = 3 RETURN id(a) as id ORDER BY a.play_count DESC SKIP{skip} LIMIT {limit} 
UNION WITH {genre_ids} as searched_genres MATCH (a:`Artist`), (a)-[rel_in_genre:IN]->(genre:`Genre`) WHERE id(genre) IN searched_genres RETURN id(a) as id ORDER BY a.play_count DESC SKIP {skip} LIMIT {limit}';
        $client = DB::getClient();
        $query = new DBQuery($client, $queryString, ['genre_ids' => $args['genre_ids'], 'limit' => $limit, 'skip' => $skip]);
        $result = $query->getResultSet();
        $s = [];
        foreach ($result as $r) $s[] = $r['id'];
        return Artist::whereIn('id', $s)->get();
    }

}
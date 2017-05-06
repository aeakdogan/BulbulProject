<?php

namespace App;

use Vinelab\NeoEloquent\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;
use Everyman\Neo4j\Cypher\Query;
use DB;

class Album extends Model
{

    protected $label = 'Album';
    protected $fillable = ['id', 'coverPhotoUrl', 'name', 'year'];

    public function tracks()
    {
        return $this->hasMany('App\Track', 'HAS');
    }

    public function getTracksCountAttribute()
    {
        if (!$this->relationLoaded('tracks')) $this->load('tracks');
        return $this->tracks->count();
    }

    public function artists()
    {
        return $this->hasMany('App\Artist', 'BY');
    }

    public static function search(Builder $b,$t, $limit, $skip){
        $searchText = addslashes($t);
        $queryString = 'MATCH (n:Album) WHERE toLower(n.name) CONTAINS toLower("'.$searchText.'") RETURN ID(n) SKIP '.intval($skip).' LIMIT '. intval($limit);
        $client = DB::getClient();
        $query = new Query($client, $queryString);
        $result = $query->getResultSet();
        $s = [];
        foreach($result as $r) $s[] = $r['n'];
        return $b->whereIn('id', $s);
    }
}
<?php

namespace App;

use Illuminate\Database\Eloquent\Builder;
use Vinelab\NeoEloquent\Eloquent\Model;
use Everyman\Neo4j\Cypher\Query;
use DB;

class Artist extends Model
{

    protected $label = 'Artist';
    protected $fillable = ['id', 'name'];
    public function albums()
    {
        return $this->belongsToMany('App\Album', 'BY');
    }

    public function tracks()
    {
        return $this->belongsToMany('App\Track','BY');
    }

    public function tags()
    {
        return $this->hasMany('App\Tag', 'TAGGED');
    }

    public function getTracksCountAttribute()
    {
        if (!$this->relationLoaded('tracks')) $this->load('tracks');
        return $this->tracks->count();
    }

    public function getAlbumsCountAttribute()
    {
        if (!$this->relationLoaded('albums')) $this->load('albums');
        return $this->albums->count();
    }

    public function genres(){
        return $this->hasMany('App\Genre', 'IN');
    }

    public function getTopTracks($limit, $skip)
    {
        return Track::with('artists')->whereHas('artists',
            function ($query) {
                $query->where('id', $this->id);
            })->orderBy('playcount', 'DESC')->skip($skip)->take($limit)->get();
    }

    public static function search(Builder $b,$t, $limit, $skip){
        $searchText = addslashes($t);
        $queryString = 'MATCH (n:Artist) WHERE toLower(n.name) CONTAINS toLower("'.$searchText.'") RETURN ID(n) ORDER BY n.play_count DESC SKIP '.intval($skip).' LIMIT '. intval($limit);
        $client = DB::getClient();
        $query = new Query($client, $queryString);
        $result = $query->getResultSet();
        $s = [];
        foreach($result as $r) $s[] = $r['n'];

        return $b->whereIn('id', $s);
    }

}
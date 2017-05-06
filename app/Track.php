<?php

namespace App;

use Vinelab\NeoEloquent\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;
use Everyman\Neo4j\Cypher\Query;
use DB;


class Track extends Model
{

    protected $label = 'Track';
    protected $fillable = ['id', 'name','year','duration','genre'];
    public function artists()
    {
        return $this->hasMany('App\Artist','BY');
    }

    public function albums(){
        return $this->belongsToMany('App\Album', 'HAS');
    }

    public function tags(){
        return $this->hasMany('App\Tag','TAGGED');
    }

    public function genres(){
        return $this->hasMany('App\Genre','IN');
    }

    public function listenedBy(){
        return $this->belongsToMany('App\BulbulUser', 'LISTENS');
    }

    public function getEdgeInfoAttribute(){
        return $this->listenedBy()->edges()->rating;
    }

    public static function search(Builder $b,$t, $limit, $skip){
        $searchText = addslashes($t);
        $queryString = 'MATCH (n:Track) WHERE toLower(n.name) CONTAINS toLower("'.$searchText.'") RETURN ID(n) ORDER BY n.playcount DESC SKIP '.intval($skip).' LIMIT '. intval($limit);
        $client = DB::getClient();
        $query = new Query($client, $queryString);
        $result = $query->getResultSet();
        $s = [];
        foreach($result as $r) $s[] = $r['n'];
        return $b->whereIn('id', $s);
    }
}
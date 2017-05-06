<?php

namespace App;

use Vinelab\NeoEloquent\Eloquent\Model;


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
}
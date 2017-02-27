<?php

namespace App;

use Vinelab\NeoEloquent\Eloquent\Model;


class Playlist extends Model
{
    protected $label = 'Playlist';
    protected $fillable = ['id', 'name'];

    public function songs(){
        return $this->hasMany('App\Song', 'HAS');
    }

    public function creator(){
        return $this->belongsTo('App\BulbulUser','BY');
    }

    public function followers(){
        return $this->hasMany('App\BulbulUser','FOLLOWS');
    }
}
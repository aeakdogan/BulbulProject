<?php

namespace App;

use Vinelab\NeoEloquent\Eloquent\Model;


class Playlist extends Model
{
    protected $label = 'Playlist';
    protected $fillable = ['id', 'name'];

    public function tracks(){
        return $this->hasMany('App\Track', 'HAS');
    }

    public function creator(){
        return $this->belongsTo('App\BulbulUser','BY');
    }

    public function followers(){
        return $this->hasMany('App\BulbulUser','FOLLOWS');
    }
}
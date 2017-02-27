<?php

namespace App;

use Vinelab\NeoEloquent\Eloquent\Model;


class Album extends Model
{

    protected $label = 'Album';
    protected $fillable = ['id','coverPhotoUrl','name','year'];

    public function songs(){
        return $this->hasMany('App\Song','HAS');
    }

    public function artist(){
        return $this->belongsToMany('App\Artist','BY');
    }

}
<?php

namespace App;

use Vinelab\NeoEloquent\Eloquent\Model;


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

}
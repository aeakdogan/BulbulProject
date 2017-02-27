<?php

namespace App;

use Vinelab\NeoEloquent\Eloquent\Model;


class Artist extends Model
{

    protected $label = 'Artist';
    protected $fillable = ['id', 'name'];

    public function albums()
    {
        return $this->hasMany('App\Album', 'BY');
    }

    public function songs()
    {
        return $this->hasMany('App\Song','BY');
    }

}
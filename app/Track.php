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

}
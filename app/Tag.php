<?php

namespace App;

use Vinelab\NeoEloquent\Eloquent\Model;

class Tag extends Model
{

    protected $label = 'Tag';
    protected $fillable = ['id', 'tag_name', 'tag_url'];

    public function artists()
    {
        return $this->belongsToMany('App\Artist', 'TAGGED');
    }

}
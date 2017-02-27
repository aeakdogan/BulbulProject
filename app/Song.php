<?php

namespace App;

use Vinelab\NeoEloquent\Eloquent\Model;


class Song extends Model
{

    protected $label = 'Song';
    protected $fillable = ['id', 'name','year','duration','genre'];
}
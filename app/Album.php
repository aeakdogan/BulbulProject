<?php

namespace App;

use Illuminate\Support\Facades\DB;
use Vinelab\NeoEloquent\Eloquent\Model;


class Album extends Model
{

    protected $label = 'Album';
    protected $fillable = ['id', 'coverPhotoUrl', 'name', 'year'];

    public function tracks()
    {
        return $this->hasMany('App\Track', 'HAS');
    }

    public function getTracksCountAttribute()
    {
        if (!$this->relationLoaded('tracks')) $this->load('tracks');
        return $this->tracks->count();
    }

    public function artists()
    {
        return $this->hasMany('App\Artist', 'BY');
    }


}
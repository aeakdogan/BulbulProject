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

    public function getTracksCountAttribute()
    {
        if (!$this->relationLoaded('tracks')) $this->load('tracks');
        return $this->tracks->count();
    }

    public function getAlbumsCountAttribute()
    {
        if (!$this->relationLoaded('albums')) $this->load('albums');
        return $this->albums->count();
    }

    public function genres(){
        return $this->hasMany('App\Genre', 'IN');
    }

    public function getTopTracks($limit, $skip)
    {
        return Track::with('artists')->whereHas('artists',
            function ($query) {
                $query->where('id', $this->id);
            })->orderBy('playcount', 'DESC')->skip($skip)->take($limit)->get();
    }

}
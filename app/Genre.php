<?php

namespace App;


use Vinelab\NeoEloquent\Eloquent\Model;

class Genre extends Model
{
    protected $label = 'Genre';
    protected $fillable = ['id', 'name','icon_url'];

    public function artists()
    {
        return $this->belongsToMany('App\Artist', 'IN');
    }

    public function tracks()
    {
        return $this->belongsToMany('App\Track', 'IN');
    }

    public function getTopArtists($limit, $skip)
    {
        return Artist::with('genres')->whereHas('genres',  function ($query) {                $query->where('id', $this->id);            })->orderBy('play_count', 'DESC')->skip($skip)->take($limit)->get();
    }

}
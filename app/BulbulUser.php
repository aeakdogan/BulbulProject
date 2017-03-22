<?php

namespace App;

use Vinelab\NeoEloquent\Eloquent\Model;


class BulbulUser extends Model
{

    protected $label = 'BulbulUser';
    protected $fillable = ['id', 'name'];

    public function playlists(){
        return $this->hasMany('App\Playlist','BY');
    }

    public function followedAlbums(){
        return $this->hasMany('App\Album', 'FOLLOWS');
    }

    public function followedTracks(){
        return $this->hasMany('App\Track', 'FOLLOWS');
    }

    public function followedArtists(){
        return $this->hasMany('App\Artist', 'FOLLOWS');
    }

    public function followedPlaylists(){
        return $this->hasMany('App\Playlist', 'FOLLOWS');
    }

    public function listenedTracks(){
        return $this->hasMany('App\Track','LISTENS');
    }

    public function listenedPlaylists(){
        return $this->hasMany('App\Playlist','LISTENS');
    }

}
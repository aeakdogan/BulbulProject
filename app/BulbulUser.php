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

    public function followedSongs(){
        return $this->hasMany('App\Song', 'FOLLOWS');
    }

    public function followedArtists(){
        return $this->hasMany('App\Artist', 'FOLLOWS');
    }

    public function followedPlaylists(){
        return $this->hasMany('App\Playlist', 'FOLLOWS');
    }

    public function listenedSongs(){
        return $this->hasMany('App\Song','LISTENS');
    }

    public function listenedPlaylists(){
        return $this->hasMany('App\Playlist','LISTENS');
    }

}
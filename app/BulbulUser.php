<?php

namespace App;

use Vinelab\NeoEloquent\Eloquent\Model;
use Illuminate\Contracts\Auth\Access\Authorizable as AuthorizableContract;
use Illuminate\Contracts\Auth\Authenticatable as AuthenticatableContract;
use Illuminate\Contracts\Auth\CanResetPassword as CanResetPasswordContract;
use Illuminate\Auth\Authenticatable;
use Illuminate\Auth\Passwords\CanResetPassword;
use Illuminate\Foundation\Auth\Access\Authorizable;


class BulbulUser extends Model implements AuthenticatableContract,
    AuthorizableContract,
    CanResetPasswordContract
{

    use Authenticatable, Authorizable, CanResetPassword;

    protected $label = 'BulbulUser';
    protected $fillable = ['email', 'username','password'];
    private $listenedTrackEdges = null;
    public function playlists()
    {
        return $this->hasMany('App\Playlist', 'BY');
    }

    public function followedUsers()
    {
        return $this->hasMany('App\BulbulUser', 'FOLLOWS');
    }

    public function followers()
    {
        return $this->belongsToMany('App\BulbulUser', 'FOLLOWS');
    }

    public function getFollowedUsersCountAttribute()
    {
        if (!$this->relationLoaded('followedUsers')) $this->load('followedUsers');
        return $this->followedUsers->count();
    }

    public function getFollowersCountAttribute()
    {
        if (!$this->relationLoaded('followers')) $this->load('followers');
        return $this->followers->count();
    }

    public function recommendations(){
        return $this->hasMany('App\Recommendation','REQUESTS');
    }

    public function listenedAlbums()
    {
        return $this->hasMany('App\Album', 'LISTENS');
    }

    public function listenedTracksRelation()
    {
        return $this->hasMany('App\Track', 'LISTENS');
    }

    public function listenedArtists()
    {
        return $this->hasMany('App\Artist', 'LISTENS');
    }

    public function followedPlaylists()
    {
        return $this->hasMany('App\Playlist', 'FOLLOWS');
    }

    public function listenedPlaylists()
    {
        return $this->hasMany('App\Playlist', 'LISTENS');
    }

    public function getListenEdgesAttribute(){
        if($this->listenedTrackEdges != null) return $this->listenedTrackEdges;
        return $this->listenedTrackEdges = $this->listenedTracksRelation()->edges();
    }

    public function getListenedTracksAttribute(){
        $a = $this->listenEdges;

        $results = [];
        foreach($a as $i) {
            $r = $i->related();
            $r->rating = $i->rating;
            $r->loved = $i->loved;
            $r->play_count = $i->play_count;
            $results[] = $r;
        }
        return $results;
    }

}
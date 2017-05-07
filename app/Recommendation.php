<?php
/**
 * Created by PhpStorm.
 * User: fatih
 * Date: 27/04/2017
 * Time: 21:11
 */

namespace App;

use Vinelab\NeoEloquent\Eloquent\Model;

class Recommendation extends Model
{
    protected $label = 'Recommendation';
    protected $fillable = ['id','status', 'type'];


    public function ratings()
    {
        return $this->hasMany('App\Rating', 'HAS');
    }

    public function user()
    {
        return $this->belongsTo('App\User', 'REQUESTS');
    }

    public function tracks()
    {
        return $this->hasMany('App\Track', 'RECOMMENDED');
    }

    public function scopeReady($query)
    {
        return $query->where('status', '!=', 'NOT_READY');
    }

    public function scopePersonal($query)
    {
        return $query->where('type', 'PERSONAL');
    }

    public function scopeRegular($query)
    {
        return $query->where('type', 'PERSONAL');
    }
}
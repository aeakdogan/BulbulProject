<?php
/**
 * Created by PhpStorm.
 * User: fatih
 * Date: 27/04/2017
 * Time: 21:19
 */

namespace App;


use Vinelab\NeoEloquent\Eloquent\Model;

class Rating extends Model
{
    protected $label = 'Rating';
    protected $fillable = ['id','track_id', 'value', 'mbid'];
    protected $hidden = ['id','track_id', 'created_at', 'updated_at'];

    public function creator(){
        return $this->belongsTo('App\BulbulUser','HAS');
    }

}
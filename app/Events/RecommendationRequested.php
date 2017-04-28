<?php

namespace App\Events;

use Illuminate\Broadcasting\Channel;
use Illuminate\Queue\SerializesModels;
use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use App\Recommendation;

class RecommendationRequested
{
    use SerializesModels;

    public $recommendation;

    public function __construct(Recommendation $recommendation)
    {
        $this->recommendation = $recommendation;
    }

//    public function broadcastOn()
//    {
//        return new Channel('recommendation');
//    }
//
//    public function broadcastAs()
//    {
//        return 'bulbul.recommendation_requested';
//    }
//
//    public function broadcastWith()
//    {
//
//        return [
//            'id' => $this->recommendation->id,
//            'ratings' => $this->recommendation->ratings,
//        ];
//    }
}

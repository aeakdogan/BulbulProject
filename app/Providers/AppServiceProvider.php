<?php

namespace App\Providers;

use App\Events\RecommendationRequested;
use Illuminate\Support\ServiceProvider;
use Illuminate\Support\Facades\Event;
use App\Recommendation;

class AppServiceProvider extends ServiceProvider
{
    /**
     * Bootstrap any application services.
     *
     * @return void
     */
    public function boot()
    {

    }

    /**
     * Register any application services.
     *
     * @return void
     */
    public function register()
    {
//        Database logger
//        Event::listen('Illuminate\Database\Events\QueryExecuted', function($query)
//        {
//            Log::info($query->sql);
//        });
    }
}

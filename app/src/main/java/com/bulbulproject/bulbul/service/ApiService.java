package com.bulbulproject.bulbul.service;


import retrofit2.http.GET;
import retrofit2.http.Query;
import com.bulbulproject.TrackQuery;


public interface ApiService {
    @GET("/graphql") io.reactivex.Observable<TrackQuery.Data> trackQuery(@Query("query") String query);
}


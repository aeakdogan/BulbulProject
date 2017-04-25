package com.bulbulproject.bulbul;

import android.app.Application;
import com.apollographql.android.ApolloCall;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import com.apollographql.android.impl.ApolloClient;


public class App extends Application {
    private static String BASE_URL = "http://104.236.48.28/graphql";
    private ApolloClient apolloClient;

    @Override
    public void onCreate(){
        super.onCreate();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .build();
        apolloClient = ApolloClient.<ApolloCall>builder().serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();

    }

    public ApolloClient apolloClient() {
        return apolloClient;
    }

}

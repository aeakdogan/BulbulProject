package com.bulbulproject.bulbul;

import android.app.Application;
import com.apollographql.apollo.ApolloClient;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class App extends Application {
    private static String BASE_URL = "http://207.154.244.207/graphql";
    private ApolloClient apolloClient;

    @Override
    public void onCreate() {
        super.onCreate();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .build();

        apolloClient = ApolloClient.builder().serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();

    }

    public ApolloClient apolloClient() {
        return apolloClient;
    }

}

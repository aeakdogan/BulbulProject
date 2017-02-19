package com.bulbulproject.bulbul.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.util.Log;


import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.service.PlayerService;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;


public class SpotifyConnectionActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "4e262c64ec3a472e935cb6506dd661bf";
    private static final String REDIRECT_URI = "bulbul-app://callback";

    // Request code that will be used to verify if the result comes from correct activity
    private static final int REQUEST_CODE = 4567;
    private SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_connection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences),Context.MODE_PRIVATE);

        Button spotifyConnectButton = (Button) findViewById(R.id.buttonSpotifyConnect);
        spotifyConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticate();
            }
        });

        String spotifyToken = sharedPref.getString("SPOTIFY_TOKEN", "");
        if(spotifyToken.compareTo("") != 0){
            authenticate();
        }
    }
    private void authenticate(){
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("SPOTIFY_TOKEN", response.getAccessToken());
                editor.apply();
                Log.d("STOken",response.getAccessToken());

                Intent in = new Intent(this, MainActivity.class);
                startActivity(in);
            }
            else {
                Log.d("Error", "Login error");
            }
        }
    }

}

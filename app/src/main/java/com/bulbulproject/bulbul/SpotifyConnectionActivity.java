package com.bulbulproject.bulbul;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.util.Log;


import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;


public class SpotifyConnectionActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "4e262c64ec3a472e935cb6506dd661bf";
    private static final String REDIRECT_URI = "bulbul-app://callback";

    // Request code that will be used to verify if the result comes from correct activity
    private static final int REQUEST_CODE = 4567;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_connection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button spotifyConnectButton = (Button) findViewById(R.id.buttonSpotifyConnect);
        final Activity _this = this;

        spotifyConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                        AuthenticationResponse.Type.TOKEN,
                        REDIRECT_URI);
                builder.setScopes(new String[]{"user-read-private", "streaming"});
                AuthenticationRequest request = builder.build();
                AuthenticationClient.openLoginActivity(_this, REQUEST_CODE, request);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Intent in = new Intent(this, MainActivity.class);
                in.putExtra("SPOTIFY_TOKEN", response.getAccessToken());
                startActivity(in);
            }
            else {
                Log.d("Error", "Login error");
            }
        }
    }

}

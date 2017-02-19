package com.bulbulproject.bulbul.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.bulbulproject.bulbul.R;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Connectivity;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class PlayerService extends Service implements ConnectionStateCallback,  Player.NotificationCallback{


    private static final String CLIENT_ID = "4e262c64ec3a472e935cb6506dd661bf";
    private static final String REDIRECT_URI = "bulbul-app://callback";
    private static final int REQUEST_CODE = 4567;

    private String mToken;
    private String mUri;
    private LocalBroadcastManager mBroadcastManager;
    private SpotifyPlayer mPlayer;
    private final Binder mBinder = new PlayerBinder();
    private SharedPreferences sharedPref;



    private final Player.OperationCallback oc = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
//            updateUI();
        }

        @Override
        public void onError(Error error) {
            Log.d("Error", error.toString());
        }
    };

    public PlayerService(){
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences),Context.MODE_PRIVATE);
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        mToken = sharedPref.getString("SPOTIFY_TOKEN","");
        Config playerConfig = new Config(getApplicationContext(), mToken, CLIENT_ID);

        mPlayer = Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(SpotifyPlayer player) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                Connectivity connectivity =  Connectivity.fromNetworkType(connectivityManager.getActiveNetworkInfo().getType());
                mPlayer.setConnectivityStatus(null, connectivity);
                mPlayer.addConnectionStateCallback(PlayerService.this);
                mPlayer.addNotificationCallback(PlayerService.this);
            }

            @Override
            public void onError(Throwable error) {
                Log.d("Spotify Player Error", error.getMessage());
            }
        });
    }

    public Metadata getMetadata(){
        return mPlayer.getMetadata();
    }

    public SpotifyPlayer getSpotifyPlayer(){
        return mPlayer;
    }

    public void playUri(String uri){
        mPlayer.playUri(null, uri,0,0);
    }

    @Override
    public void onLoggedIn() {
    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("EROROROROR",error.name());
    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("Event",playerEvent.name());
        if(PlayerEvent.kSpPlaybackNotifyPause.equals(playerEvent)){
            Intent intent = new Intent();
            intent.setAction("bulbul.player");
            intent.putExtra("type","pause");
            mBroadcastManager.sendBroadcast(intent);
        }
        else if(PlayerEvent.kSpPlaybackNotifyPlay.equals(playerEvent)){
            Intent intent = new Intent();
            intent.setAction("bulbul.player");
            intent.putExtra("type","play");
            mBroadcastManager.sendBroadcast(intent);
        }
        else if(PlayerEvent.kSpPlaybackNotifyTrackChanged.equals(playerEvent)){
            Intent intent = new Intent();
            intent.setAction("bulbul.player");
            intent.putExtra("type","track_changed");
            mBroadcastManager.sendBroadcast(intent);
        }
        else if(PlayerEvent.kSpPlaybackNotifyTrackDelivered.equals(playerEvent)){
            Intent intent = new Intent();
            intent.setAction("bulbul.player");
            intent.putExtra("type","track_delivered");
            mBroadcastManager.sendBroadcast(intent);
        }

    }

    public void onPlaybackError(Error error) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class PlayerBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

}

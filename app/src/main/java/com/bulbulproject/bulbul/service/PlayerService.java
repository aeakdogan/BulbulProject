package com.bulbulproject.bulbul.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.util.List;

public class PlayerService extends Service implements ConnectionStateCallback, Player.NotificationCallback {


    private static final String CLIENT_ID = "4e262c64ec3a472e935cb6506dd661bf";
    private static final String REDIRECT_URI = "bulbul-app://callback";
    private static final int REQUEST_CODE = 4567;

    private List<String> songs;
    private List<Integer> songIds;

    private int position = 0;

    private String mToken;
    private LocalBroadcastManager mBroadcastManager;
    private SpotifyPlayer mPlayer;
    private final Binder mBinder = new PlayerBinder();
    private SharedPreferences sharedPref;


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("type");
            if (type.equals("pause")) {
                mPlayer.pause(null);
            } else if (type.equals("resume")) {
                mPlayer.resume(null);
            } else if (type.equals("next_song")) {
                mPlayer.skipToNext(null);
            } else if (type.equals("prev_song")) {
                mPlayer.skipToPrevious(null);
            } else if (type.equals("play_songs")) {
                mPlayer.playUri(null, "spotify:user:spotify:playlist:2yLXxKhhziG2xzy7eyD4TD", 0, 0);
            }
        }
    };


    public PlayerService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        mBroadcastManager.registerReceiver(receiver, new IntentFilter("bulbul.player_service"));
        mToken = sharedPref.getString("SPOTIFY_TOKEN", "");
        Config playerConfig = new Config(getApplicationContext(), mToken, CLIENT_ID);

        mPlayer = Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(SpotifyPlayer player) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                Connectivity connectivity = Connectivity.fromNetworkType(connectivityManager.getActiveNetworkInfo().getType());
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

    public Metadata getMetadata() {
        return mPlayer.getMetadata();
    }

    public SpotifyPlayer getSpotifyPlayer() {
        return mPlayer;
    }


    public List<String> getSongs() {
        return songs;
    }

    public void setSongs(List<String> songs) {
        if (mPlayer.getPlaybackState().isPlaying) {
            mPlayer.pause(null);
        }
        this.songs = songs;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Integer> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<Integer> songIds) {
        this.songIds = songIds;
    }

    public void playUri(String uri) {
        songs.clear();
        songs.add(uri);
        position = 0;
        mPlayer.playUri(null, uri, 0, 0);
    }

    public void playPause() {
        if (mPlayer.getPlaybackState().isPlaying) {
            mPlayer.pause(null);
        } else {
            if (mPlayer.getMetadata().currentTrack != null) {
                mPlayer.resume(null);
            } else {
                play();
            }
        }
    }

    public void play() {
        if (songs != null && songs.size() > 0)
            mPlayer.playUri(null, songs.get(position), 0, 0);
    }

    public void next() {
        if (songs != null && songs.size() > 0) {
            position = (position == songs.size() - 1) ? 0 : (position + 1);
            mPlayer.playUri(null, songs.get(position), 0, 0);
        }
    }

    public void previous() {
        if (songs != null && songs.size() > 0) {
            position = position == 0 ? songs.size() - 1 : (position - 1);
            mPlayer.playUri(null, songs.get(position), 0, 0);
        }
    }

    @Override
    public void onLoggedIn() {
    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("EROROROROR", error.name());
    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("Event", playerEvent.name());
        if (PlayerEvent.kSpPlaybackNotifyPause.equals(playerEvent)) {
            Intent intent = new Intent();
            intent.setAction("bulbul.player");
            intent.putExtra("type", "pause");
            mBroadcastManager.sendBroadcast(intent);
        } else if (PlayerEvent.kSpPlaybackNotifyPlay.equals(playerEvent)) {
            Intent intent = new Intent();
            intent.setAction("bulbul.player");
            intent.putExtra("type", "play");
            mBroadcastManager.sendBroadcast(intent);
        } else if (PlayerEvent.kSpPlaybackNotifyTrackChanged.equals(playerEvent)) {
            Intent intent = new Intent();
            intent.setAction("bulbul.player");
            intent.putExtra("type", "track_changed");
            intent.putExtra("position", position);
            mBroadcastManager.sendBroadcast(intent);
        } else if (PlayerEvent.kSpPlaybackNotifyTrackDelivered.equals(playerEvent)) {
            next();
            Intent intent = new Intent();
            intent.setAction("bulbul.player");
            intent.putExtra("type", "track_delivered");
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

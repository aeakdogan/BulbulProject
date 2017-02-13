package com.bulbulproject.bulbul;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Connectivity;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link StreamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StreamFragment extends Fragment implements ConnectionStateCallback,  Player.NotificationCallback {

    private static final String CLIENT_ID = "4e262c64ec3a472e935cb6506dd661bf";

    private static final String ARG_TOKEN = "token";
    private static final String ARG_URI = "uri";

    private String mToken;
    private String mUri;

    private SpotifyPlayer mPlayer;
    private ImageButton mActionButton, mPreviousButton, mNextButton;
    private TextView mListName, mSongTitle, mArtistName, mSeekbarCurrentPos, mSeekbarDuration;
    private SeekBar mSeekBar;
    private ImageView mImage;

    private Timer mTimer;
    private boolean mScheduled = false;


    final Player.OperationCallback oc = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
            updateUI();
        }

        @Override
        public void onError(Error error) {
            Log.d("Error", error.toString());
        }
    };

    public StreamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param token OAuth token.
     * @param uri   Spotify uri.
     * @return A new instance of fragment StreamFragment.
     */
    public static StreamFragment newInstance(String token, String uri) {
        StreamFragment fragment = new StreamFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TOKEN, token);
        args.putString(ARG_URI, uri);
        fragment.setArguments(args);
        return fragment;
    }

    public static StreamFragment newInstance(String token) {
        StreamFragment fragment = new StreamFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TOKEN, token);
        args.putString(ARG_URI, "");
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mToken = getArguments().getString(ARG_TOKEN);
            mUri = getArguments().getString(ARG_URI);
        }

        Config playerConfig = new Config(getActivity().getApplicationContext(), mToken, CLIENT_ID);
        mPlayer = Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(SpotifyPlayer player) {
                mPlayer.setConnectivityStatus(null, getNetworkConnectivity(getActivity().getApplicationContext()));
                mPlayer.addConnectionStateCallback(StreamFragment.this);
                mPlayer.addNotificationCallback(StreamFragment.this);
            }

            @Override
            public void onError(Throwable error) {
                Log.d("Spotify Player Error", error.getMessage());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stream, container, false);
        mActionButton = (ImageButton) view.findViewById(R.id.button_action);
        mPreviousButton = (ImageButton) view.findViewById(R.id.button_previous);
        mNextButton = (ImageButton) view.findViewById(R.id.button_next);
        mListName = (TextView) view.findViewById(R.id.player_list_name);
        mSongTitle = (TextView) view.findViewById(R.id.player_song_title);
        mArtistName = (TextView) view.findViewById(R.id.player_artist_name);
        mSeekbarCurrentPos = (TextView) view.findViewById(R.id.player_seekbar_currentPos);
        mSeekbarDuration = (TextView) view.findViewById(R.id.player_seekbar_duration);
        mImage = (ImageView) view.findViewById(R.id.player_image);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);

        mActionButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (mPlayer.getPlaybackState().isPlaying) {
                    mPlayer.pause(oc);
                } else {
                    mPlayer.resume(oc);
                }
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.skipToPrevious(oc);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.skipToNext(oc);
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    mPlayer.seekToPosition(null, progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    private void updateUI() {
        Metadata.Track track = null;
        if (mPlayer.getMetadata() != null) {
            track = mPlayer.getMetadata().currentTrack;
        }
        if (track != null) {
            FetchImageTask imageTask = new FetchImageTask(mImage);
            imageTask.execute(track.albumCoverWebUrl);
            updateSeekbarCurrentPos();
            updateSeekbarDuration();
            mListName.setText(track.albumName);
            mSongTitle.setText(track.name);
            if (mPlayer.getPlaybackState().isPlaying) {
                mActionButton.setImageResource(android.R.drawable.ic_media_pause);
            } else {
                mActionButton.setImageResource(android.R.drawable.ic_media_play);
            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    private Connectivity getNetworkConnectivity(Context context) {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return Connectivity.fromNetworkType(activeNetwork.getType());
        } else {
            return Connectivity.OFFLINE;
        }
    }

    public static String getDateTime(long milliseconds) {
        int seconds = (int) milliseconds / 1000;
        int hours = seconds / 3600;
        int remainder = seconds % 3600;
        int mins = remainder / 60;
        int secs = remainder % 60;
        return (hours >0 ? hours + " : ":"") + mins + " : " + secs;
    }

    private void updateSeekbarCurrentPos() {
        long position = mPlayer.getPlaybackState().positionMs;
        long duration = mPlayer.getMetadata().currentTrack.durationMs;
        mSeekBar.setMax((int)duration/1000);
        mSeekBar.setProgress((int) position/1000);
        mSeekbarCurrentPos.setText(getDateTime(position));
    }

    private void updateSeekbarDuration() {
        long duration = mPlayer.getMetadata().currentTrack.durationMs;
        mSeekbarDuration.setText(getDateTime(duration));
    }

    public void setUri(String uri){
        mUri = uri;
        mPlayer.playUri(oc,mUri,0,0);
    }

    @Override
    public void onLoggedIn() {
        Log.d("Login","Success");
        if(!mUri.equals("")) {
            mPlayer.playUri(oc, mUri, 0, 0);
        }
    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("LOGIN",error.toString());
    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {

        if(playerEvent.ordinal() == PlayerEvent.kSpPlaybackNotifyPlay.ordinal()){
            mScheduled = true;
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateSeekbarCurrentPos();
                        }
                    });
                }
            },0,1000);
        }
        else if(playerEvent.ordinal() == PlayerEvent.kSpPlaybackNotifyPause.ordinal()){
            if(mScheduled) {
                mTimer.cancel();
                mTimer.purge();
                mScheduled = false;
            }
        }
            updateUI();
    }

    @Override
    public void onPlaybackError(Error error) {

    }
}

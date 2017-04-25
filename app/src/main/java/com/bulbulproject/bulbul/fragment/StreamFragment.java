package com.bulbulproject.bulbul.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bulbulproject.bulbul.activity.StreamActivity;
import com.bulbulproject.bulbul.service.PlayerService;
import com.bulbulproject.bulbul.R;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.SpotifyPlayer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class StreamFragment extends Fragment {

    private static final String TEST_SONG_URI = "spotify:user:spotify:playlist:2yLXxKhhziG2xzy7eyD4TD";
    private PlayerService mPlayerService;
    private Intent mPlayerIntent;
    private boolean mBound;
    private String mUri;
    private SpotifyPlayer mPlayer;
    private ImageButton mActionButton, mPreviousButton, mNextButton;
    private TextView  mSongTitle, mArtistName;
    private RelativeLayout mPlayerContainer;


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");
            if (type.equals("pause")) {
                updateUI();
            } else if (type.equals("play")) {
                updateUI();
            } else if (type.equals("track_changed")) {
                updateUI();
            } else if (type.equals("track_delivered")) {
                updateUI();
            }
        }
    };


    public StreamFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPlayer();
    }

    private void initPlayer() {
        if (mPlayerIntent == null) {
            mPlayerIntent = new Intent(getActivity().getApplicationContext(), PlayerService.class);
            mBound = getActivity().getApplicationContext().bindService(mPlayerIntent, mConnection, Context.BIND_AUTO_CREATE);
            getActivity().getApplicationContext().startService(mPlayerIntent);
            LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(receiver, new IntentFilter("bulbul.player"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stream, container, false);
        mActionButton = (ImageButton) view.findViewById(R.id.button_action);
        mPreviousButton = (ImageButton) view.findViewById(R.id.button_previous);
        mNextButton = (ImageButton) view.findViewById(R.id.button_next);
        mSongTitle = (TextView) view.findViewById(R.id.player_song_title);
        mArtistName = (TextView) view.findViewById(R.id.player_artist_name);
        mPlayerContainer = (RelativeLayout) view.findViewById(R.id.player);

        mPlayerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), StreamActivity.class);
                startActivity(intent);
            }
        });
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mPlayerService.playPause();
            }

        });

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerService.previous();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerService.next();
            }
        });

        return view;
    }

    public void updateUI() {
        Metadata.Track track = null;
        if (mPlayer.getMetadata() != null) {
            track = mPlayer.getMetadata().currentTrack;
        }
        if (track != null) {
            mSongTitle.setText(track.name);
            mArtistName.setText(track.artistName);
            if (mPlayer.getPlaybackState().isPlaying) {
                mActionButton.setImageResource(android.R.drawable.ic_media_pause);
            } else {
                mActionButton.setImageResource(android.R.drawable.ic_media_play);
            }
        }

    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mPlayer != null) {
            mPlayer = mPlayerService.getSpotifyPlayer();
            updateUI();
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
        if (mBound) {
            getActivity().getApplicationContext().unbindService(mConnection);
        }
        super.onDestroy();
    }

    public static String getDateTime(long milliseconds) {
        int seconds = (int) milliseconds / 1000;
        int hours = seconds / 3600;
        int remainder = seconds % 3600;
        int mins = remainder / 60;
        int secs = remainder % 60;
        String hoursString = hours <= 9 ? "0" + hours : "" + hours;
        String minsString = mins <= 9 ? "0" + mins : "" + mins;
        String secsString = secs <= 9 ? "0" + secs : "" + secs;
        return (hours > 0 ? hoursString + " : " : "") + minsString + " : " + secsString;
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder) service;
            mPlayerService = binder.getService();
            mPlayer = mPlayerService.getSpotifyPlayer();
            mBound = true;


        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            mPlayer = null;
        }
    };

}

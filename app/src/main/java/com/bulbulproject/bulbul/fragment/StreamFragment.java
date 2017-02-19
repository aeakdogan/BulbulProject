package com.bulbulproject.bulbul.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bulbulproject.bulbul.service.PlayerService;
import com.bulbulproject.bulbul.task.FetchImageTask;
import com.bulbulproject.bulbul.R;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.Player;
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
    private TextView mListName, mSongTitle, mArtistName, mSeekbarCurrentPos, mSeekbarDuration;
    private SeekBar mSeekBar;
    private ImageView mImage;

    private Handler mHandler = new Handler();


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

    final Player.OperationCallback oc = new Player.OperationCallback() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onError(Error error) {
            Log.d("Error", error.toString());
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

    private void initPlayer(){
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
                if (mUri != null) {
                    if (mPlayer.getPlaybackState().isPlaying) {
                        mPlayer.pause(null);
                    } else {
                        mPlayer.resume(oc);
                    }
                } else {
                    mUri = TEST_SONG_URI;
                    mPlayer.playUri(oc, mUri, 0, 0);
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
                if (fromUser) {
                    mPlayer.seekToPosition(null, progress * 1000);
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

    public void updateUI() {
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
        if(mPlayer != null) {
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
        String hoursString = hours <=9 ? "0" + hours : ""+hours;
        String minsString = mins <=9 ? "0" + mins : ""+mins;
        String secsString = secs <=9 ? "0" + secs : ""+secs;
        return (hours > 0 ? hoursString + " : " : "") + minsString + " : " + secsString;
    }

    private void updateSeekbarCurrentPos() {
        mHandler.postDelayed(mUpdateTimeTask,200);
    }

    private void updateSeekbarDuration() {
        long duration = mPlayer.getMetadata().currentTrack.durationMs;
        mSeekbarDuration.setText(getDateTime(duration));
    }

    public void setUri(String uri) {
        mUri = uri;
        mPlayer.playUri(oc, mUri, 0, 0);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            long position = mPlayer.getPlaybackState().positionMs;
            long duration = mPlayer.getMetadata().currentTrack.durationMs;
            mSeekBar.setMax((int) duration / 1000);
            mSeekBar.setProgress((int) position / 1000);
            mSeekbarCurrentPos.setText(getDateTime(position));
            mHandler.postDelayed(this,200);
        }
    };

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

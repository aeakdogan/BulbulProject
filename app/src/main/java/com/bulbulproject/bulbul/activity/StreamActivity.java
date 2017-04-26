package com.bulbulproject.bulbul.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.service.PlayerService;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.SpotifyPlayer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StreamActivity extends AppCompatActivity {

    private static final String TEST_SONG_URI = "spotify:user:spotify:playlist:2yLXxKhhziG2xzy7eyD4TD";
    private PlayerService mPlayerService;
    private Intent mPlayerIntent;
    private boolean mBound;
    private String mUri;
    private boolean mAutoplay = false;
    private Toolbar toolbar;
    private SpotifyPlayer mPlayer;
    private ImageButton mActionButton, mPreviousButton, mNextButton;
    private TextView mListName, mSongTitle, mArtistName, mSeekbarCurrentPos, mSeekbarDuration;
    private SeekBar mSeekBar;
    private ImageView mImage;
    private int position;
    private List<String> songs;
    private int targetProgress = 0;


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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPlayer();
        setContentView(R.layout.activity_stream);

        setTitle("Bulbul");

        //Setup Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mActionButton = (ImageButton) findViewById(R.id.button_action);
        mPreviousButton = (ImageButton) findViewById(R.id.button_previous);
        mNextButton = (ImageButton) findViewById(R.id.button_next);
        mListName = (TextView) findViewById(R.id.player_list_name);
        mSongTitle = (TextView) findViewById(R.id.player_song_title);
        mArtistName = (TextView) findViewById(R.id.player_artist_name);
        mSeekbarCurrentPos = (TextView) findViewById(R.id.player_seekbar_currentPos);
        mSeekbarDuration = (TextView) findViewById(R.id.player_seekbar_duration);
        mImage = (ImageView) findViewById(R.id.player_image);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);


        Intent intent = getIntent();
        if (intent.hasExtra("song_uri")) {
            mAutoplay = true;
            mUri = intent.getStringExtra("song_uri");
        } else if (intent.hasExtra("songs")) {
            position = intent.getIntExtra("position", 0);
            songs = intent.getStringArrayListExtra("songs");
            mAutoplay = true;
        }

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

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    targetProgress = progress * 1000;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlayer.seekToPosition(null, targetProgress);
                updateSeekbarCurrentPos();
            }
        });
    }

    private void initPlayer() {
        if (mPlayerIntent == null) {
            mPlayerIntent = new Intent(getApplicationContext(), PlayerService.class);
            mBound = getApplicationContext().bindService(mPlayerIntent, mConnection, Context.BIND_AUTO_CREATE);
            getApplicationContext().startService(mPlayerIntent);
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, new IntentFilter("bulbul.player"));
        }
    }


    public void updateUI() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        Metadata.Track track = null;
        if (mPlayer.getMetadata() != null) {
            track = mPlayer.getMetadata().currentTrack;
        }
        if (track != null) {
            Picasso.with(StreamActivity.this).load(track.albumCoverWebUrl)
                    .placeholder(R.drawable.cover_picture)
                    .error(R.drawable.cover_picture)
                    .into(mImage);
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
        if (mPlayer != null) {
            mPlayer = mPlayerService.getSpotifyPlayer();
            updateUI();
        }
    }


    @Override
    public void onDestroy() {
        if (mBound) {
            getApplicationContext().unbindService(mConnection);
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

    private void updateSeekbarCurrentPos() {
        mHandler.postDelayed(mUpdateTimeTask, 200);
    }

    private void updateSeekbarDuration() {
        long duration = mPlayer.getMetadata().currentTrack.durationMs;
        mSeekbarDuration.setText(getDateTime(duration));
    }

    public void setUri(String uri) {
        mUri = uri;
        mPlayerService.playUri(mUri);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            if (mPlayer != null && mPlayer.getPlaybackState() != null && mPlayer.getMetadata() != null) {
                try {
                    long position = mPlayer.getPlaybackState().positionMs;
                    long duration = mPlayer.getMetadata().currentTrack.durationMs;
                    mSeekBar.setMax((int) duration / 1000);
                    mSeekBar.setProgress((int) position / 1000);
                    mSeekbarCurrentPos.setText(getDateTime(position));
                    mHandler.postDelayed(this, 200);
                } catch (NullPointerException e) {
                    mHandler.removeCallbacks(this);
                }
            } else {
                mHandler.removeCallbacks(this);
            }
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
            if (mAutoplay) {
                if (songs != null) {
                    mPlayerService.setSongs(songs);
                    mPlayerService.setPosition(position);
                    mPlayerService.play();
                } else if (mUri != null) {
                    mPlayerService.playUri(mUri);
                }
            }
            updateUI();

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            mPlayer = null;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

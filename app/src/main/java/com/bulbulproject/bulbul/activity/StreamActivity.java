package com.bulbulproject.bulbul.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.AddTrackToPlaylistMutation;
import com.bulbulproject.CreatePlaylistMutation;
import com.bulbulproject.UserPlaylistsQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.model.Playlist;
import com.bulbulproject.bulbul.service.PlayerService;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.SpotifyPlayer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

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
    private int mSongID;
    ArrayList<Playlist> mPlaylists = new ArrayList<>();

    private ArrayList<String> playlistNames;

    private Handler mHandler = new Handler();
    String token;
    Dialog playlistDialog;

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
        mSongID = intent.getIntExtra("trackID", 0);

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

        findViewById(R.id.button_add_playlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToPlaylist();

            }
        });
    }
    void createPlaylist(final String mytoken, final String playlistName){
        ((App) getApplication()).apolloClient().newCall(CreatePlaylistMutation.builder().token(mytoken).name(playlistName).build()).enqueue(new ApolloCall.Callback<CreatePlaylistMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<CreatePlaylistMutation.Data> response) {
                if (response.isSuccessful()) {
//                    response.data().
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),  playlistName +" playlist created", Toast.LENGTH_SHORT).show();
                        }
                    });
                    addTrackToPlaylist(mytoken, mSongID, new Playlist(playlistName, response.data().createPlaylist().id()));
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                final String text = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    void createPlaylistDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setMessage("Enter Playlist Name");
        alert.setTitle("Create New Playlist");

        alert.setView(edittext);

        alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String playlistName = edittext.getText().toString();
                playlistDialog.dismiss();
                createPlaylist(token, playlistName);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                return;
            }
        });

        alert.show();
    }
    void addTrackToPlaylist(String mytoken, int trackID, final Playlist playlist){
        ((App) getApplication()).apolloClient().newCall(AddTrackToPlaylistMutation.builder().token(mytoken).track_id(trackID).id(playlist.getId()).build()).enqueue(new ApolloCall.Callback<AddTrackToPlaylistMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<AddTrackToPlaylistMutation.Data> response) {
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Song added to playlist of " + playlist.getName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                final String text = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    void addToPlaylist(){

        playlistDialog = new Dialog(this);
        playlistDialog.setContentView(R.layout.dialog_playlists);
        playlistDialog.setTitle("Add to Playlist");
        ((TextView) playlistDialog.findViewById(R.id.title_dialog)).setText("Add to Playlist");
        ListView listViewPlaylists = (ListView) playlistDialog.findViewById(R.id.listview_playlists);

        playlistNames = new ArrayList<>();
        final ArrayAdapter<String> playlistAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, playlistNames);

        listViewPlaylists.setAdapter(playlistAdapter);
        listViewPlaylists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    createPlaylistDialog();
                    //Toast.makeText(getApplicationContext(), "New Playlist is created", Toast.LENGTH_SHORT).show();
                }
                else if(position > 0) {
                    playlistDialog.dismiss();
                    addTrackToPlaylist(token, mSongID, mPlaylists.get(position - 1));
                }

            }
        });

        token = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE).getString("AUTH_TOKEN", "");

        ((App) getApplication()).apolloClient().newCall(UserPlaylistsQuery.builder().token(token).build()).enqueue(new ApolloCall.Callback<UserPlaylistsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<UserPlaylistsQuery.Data> response) {
                if (response.isSuccessful()) {
                    playlistNames.clear();
                    mPlaylists.clear();
                    playlistNames.add("(Create New Playlist)");
//                    mPlaylists.add(new Playlist("new_playlist", -1));
                    UserPlaylistsQuery.Data.User user = response.data().users().get(0);
                    if (user.playlists() != null) {
                        for (UserPlaylistsQuery.Data.Playlist playlist : user.playlists()) {
                            mPlaylists.add(new Playlist(playlist.name(), playlist.id()));
                            playlistNames.add(playlist.name());
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                        mProgressView.setVisibility(View.GONE);
                        playlistAdapter.notifyDataSetChanged();
                        playlistDialog.show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                final String text = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void clicked_spotify(View v) {
        final Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(mPlayer.getMetadata().currentTrack.uri));
        startActivity(intent);
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

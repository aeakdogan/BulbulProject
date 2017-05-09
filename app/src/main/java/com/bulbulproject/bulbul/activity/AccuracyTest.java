package com.bulbulproject.bulbul.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.RecommendationsQuery;
import com.bulbulproject.RequestRecommendationMutation;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.Song;
import com.bulbulproject.bulbul.service.Globals;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class AccuracyTest extends AppCompatActivity {

    int currentOrder = 0;

    TextView textViewSongCounter;
    TextView textViewArtistName;
    TextView textViewAlbumName;
    TextView textViewSongName;
    ImageView imageViewAlbumImage;
    ImageView imageViewMusicControl;
    MediaPlayer mMediaPlayer;

    Handler mHandler;
    Runnable fetcher;

    ArrayList<Song> mSongs;
    private View mProgressView;
    boolean isPlaying;
    private ImageView backgroundImage;
    private int recommendationId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        setContentView(R.layout.activity_accuracy_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Recommended Tracks");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mProgressView = findViewById(R.id.login_progress);
        mProgressView.setVisibility(View.VISIBLE);

        mSongs = new ArrayList<>();
        isPlaying = false;

        backgroundImage = (ImageView) findViewById(R.id.bg_image);
        textViewArtistName = (TextView) findViewById(R.id.artist_name);
        textViewAlbumName = (TextView) findViewById(R.id.album_name);
        textViewSongName = (TextView) findViewById(R.id.song_name);
        textViewSongCounter = (TextView) findViewById(R.id.text_song_counter);
        imageViewAlbumImage = (ImageView) findViewById(R.id.album_img);
        imageViewMusicControl = (ImageView) findViewById(R.id.icon_music_control);

        requestRecommendation();
    }

    public void clicked_icon(View v) {
        if (v.getId() == R.id.icon_music_control) {
            if (!isPlaying) {
                ((ImageView) v).setImageResource(R.drawable.icon_pause);
                playSong(mSongs.get(currentOrder));
                isPlaying = true;
            } else {
                ((ImageView) v).setImageResource(R.drawable.icon_play);
                pauseSong(mSongs.get(currentOrder));
                isPlaying = false;
            }
            return;
        }

        if (v.getId() == R.id.icon_bad || v.getId() == R.id.icon_neutral || v.getId() == R.id.icon_good) {
            if (v.getId() == R.id.icon_bad) {
                mSongs.get(currentOrder).setTestResult(-1);
            } else if (v.getId() == R.id.icon_neutral) {
                mSongs.get(currentOrder).setTestResult(0);
            } else if (v.getId() == R.id.icon_good) {
                mSongs.get(currentOrder).setTestResult(1);
            }
            if (currentOrder == mSongs.size() - 1) {
                if(Globals.mSongs !=null) {
                    Globals.mSongs.clear();
                    Globals.mSongs.addAll(mSongs);
                }else{
                    Globals.mSongs = mSongs;
                }
                Intent intent = new Intent(AccuracyTest.this, SongListActivity.class);
                intent.putExtra("recommendation_id", recommendationId);
                startActivity(intent);
                return;
            }
            currentOrder++;
            isPlaying = false;
            updateUI();
            releasePlayer();
        }
    }

    void playSong(Song song) {
        if (isPlaying)
            return;
        if (mMediaPlayer != null && mMediaPlayer.getAudioSessionId() > 0) {
            mMediaPlayer.start();
            return;
        }
        if (song.getPreviewUrl() != null && !song.getPreviewUrl().isEmpty()) {
            mMediaPlayer = new MediaPlayer();
            try {
                mMediaPlayer.setDataSource(song.getPreviewUrl());
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Preview is unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    void pauseSong(Song song) {
        if (song.getPreviewUrl() != null && isPlaying) {
            mMediaPlayer.pause();
        } else {
            Toast.makeText(getApplicationContext(), "Pause is unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    void releasePlayer() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mMediaPlayer = null;
    }

    @Override
    public void finish() {
        super.finish();
        releasePlayer();
    }

    void updateUI() {
        textViewSongCounter.setText("Song: " + (currentOrder + 1) + "/" + mSongs.size());

        textViewArtistName.setText(mSongs.get(currentOrder).getFirstArtistName());
        textViewAlbumName.setText(mSongs.get(currentOrder).getFirstAlbumName());
        textViewSongName.setText(mSongs.get(currentOrder).getName());
        imageViewMusicControl.setImageResource(R.drawable.icon_play);

        Picasso.with(this).load(mSongs.get(currentOrder).getImageUrl())
                .error(R.drawable.cover_picture)
                .resize(300,300)
                .onlyScaleDown()
                .transform(new BlurTransformation(this,23))
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .into(backgroundImage);
        Picasso.with(this)
                .load(mSongs.get(currentOrder).getImageUrl())
                .placeholder(R.drawable.cover_picture)
                .error(R.drawable.cover_picture)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .into(imageViewAlbumImage);
    }

    private void requestRecommendation() {
        Intent intent = getIntent();
        final SharedPreferences sp = getApplication().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        if (intent.hasExtra("track_ids") && intent.hasExtra("ratings") && intent.hasExtra("category_ids") && intent.hasExtra("artist_ids")) {
            ((App) getApplication()).apolloClient().newCall(RequestRecommendationMutation.builder()
                    .artist_ids(intent.getIntegerArrayListExtra("artist_ids"))
                    .genre_ids(intent.getIntegerArrayListExtra("category_ids"))
                    .track_ids(intent.getIntegerArrayListExtra("track_ids"))
                    .token(sp.getString("AUTH_TOKEN", ""))
                    .ratings(intent.getIntegerArrayListExtra("ratings"))
                    .build())
                    .enqueue(new ApolloCall.Callback<RequestRecommendationMutation.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<RequestRecommendationMutation.Data> response) {
                            if (response.isSuccessful()) {
                                int id = response.data().requestRecommendation().id();
                                fetchRecommendation(id);
                            }
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {
                            final String text = e.getMessage();
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AccuracyTest.this, text, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
        }
    }

    private void fetchRecommendation(final int id) {
        if (fetcher != null) {
            mHandler.removeCallbacks(fetcher);
        }
        ((App) getApplication()).apolloClient().newCall(RecommendationsQuery.builder().id(id).build()).enqueue(new ApolloCall.Callback<RecommendationsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<RecommendationsQuery.Data> response) {
                if (response.isSuccessful() && response.data().recommendations() != null && response.data().recommendations().get(0).status().equals("READY")) {
                    RecommendationsQuery.Data.Recommendation recommendation = response.data().recommendations().get(0);
                    if (recommendation.tracks() != null) {
                        recommendationId = id;
                        List<RecommendationsQuery.Data.Track> tracks = recommendation.tracks();
                        for (RecommendationsQuery.Data.Track track : tracks) {
                            Song mSong = new Song(track.id(),
                                    track.name(),
                                    0,
                                    track.spotify_album_img(),
                                    track.spotify_track_preview_url(),
                                    track.spotify_track_id()

                            );
                            if (track.artists() != null) {
                                for (RecommendationsQuery.Data.Artist artist : track.artists()) {
                                    mSong.getArtists().add(new Artist(artist.name()));
                                }
                            }

                            if (track.albums() != null) {
                                for (RecommendationsQuery.Data.Album album : track.albums()) {
                                    mSong.getAlbums().add(new Album(album.name(), album.image()));
                                }
                            }
                            mSongs.add(mSong);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressView.setVisibility(View.GONE);
                                updateUI();
                        }
                            });
                    }
                } else {
                    if (fetcher == null) {
                        fetcher = new Runnable() {
                            @Override
                            public void run() {
                                fetchRecommendation(id);
                            }
                        };
                    }
                    mHandler.postDelayed(fetcher, 2500);
                }
            }


            @Override
            public void onFailure(@Nonnull ApolloException e) {
                final String text = e.getMessage();
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AccuracyTest.this, text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AccuracyTest.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }
}

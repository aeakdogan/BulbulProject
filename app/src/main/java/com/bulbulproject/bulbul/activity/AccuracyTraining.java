package com.bulbulproject.bulbul.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.ArtistTopTracks;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.Song;
import com.bulbulproject.bulbul.service.Globals;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class AccuracyTraining extends AppCompatActivity {

    int currentOrder = 0;

    RatingBar ratingBarSong;
    TextView textViewRateSong;
    TextView textViewSongCounter;
    TextView textViewArtistName;
    TextView textViewAlbumName;
    TextView textViewSongName;
    ImageView imageViewAlbumImage;
    MediaPlayer mMediaPlayer;


    List<Song> mSongs;
    ArrayList<Integer> artistIds;
    ArrayList<Integer> categoryIds;
    private View mProgressView;
    boolean isPlaying;
    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accuracy_training);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Rate tracks");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mProgressView = findViewById(R.id.progress);
        mProgressView.setVisibility(View.VISIBLE);

        mSongs = new ArrayList<>();
        isPlaying = false;

        textViewArtistName = (TextView) findViewById(R.id.artist_name);
        textViewAlbumName = (TextView) findViewById(R.id.album_name);
        textViewSongName = (TextView) findViewById(R.id.song_name);
        textViewSongCounter = (TextView) findViewById(R.id.text_song_counter);
        textViewRateSong = (TextView) findViewById(R.id.rate_song);
        ratingBarSong = (RatingBar) findViewById(R.id.ratingbar_song);
        imageViewAlbumImage = (ImageView) findViewById(R.id.album_img);
        backgroundImage = (ImageView) findViewById(R.id.bg_image);

        ratingBarSong.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (mSongs.get(currentOrder).getRating() == 0 && rating != 0) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clicked_icon(findViewById(R.id.icon_next));
                        }
                    }, 100);
                }
                mSongs.get(currentOrder).setRating(rating);
                textViewRateSong.setText(String.valueOf(rating));
            }
        });

        artistIds = getIntent().getIntegerArrayListExtra("artist_ids");
        categoryIds = getIntent().getIntegerArrayListExtra("category_ids");

        //Fetch data and update ui

        fetchArtistTopTracks(artistIds, categoryIds);

    }

    public void fetchArtistTopTracks(List<Integer> artist_ids, List<Integer> genre_ids) {
        ((App) getApplication()).apolloClient().newCall(ArtistTopTracks.builder().ids(artist_ids).limit(Math.round(25/artist_ids.size())).build()).enqueue(new ApolloCall.Callback<ArtistTopTracks.Data>() {
            @Override
            public void onResponse(@Nonnull Response<ArtistTopTracks.Data> response) {
                if (response.isSuccessful() && response.data().artists() != null) {
                    List<ArtistTopTracks.Data.Artist> artistList = response.data().artists();
                    for (ArtistTopTracks.Data.Artist artist : artistList) {
                        if (artist.topTracks() != null) {
                            for (ArtistTopTracks.Data.TopTrack track : artist.topTracks()) {
                                Song mSong = new Song(track.id(),
                                        track.name(),
                                        0,
                                        track.spotify_album_img(),
                                        track.spotify_track_preview_url()

                                );
                                if(track.artists() != null) {
                                    for (ArtistTopTracks.Data.Artist1 trackArtist : track.artists()) {
                                        mSong.getArtists().add(new Artist(trackArtist.name()));
                                    }
                                }

                                if(track.albums()!=null) {
                                    for (ArtistTopTracks.Data.Album album : track.albums()) {
                                        mSong.getAlbums().add(new Album(album.name(), album.image()));
                                    }
                                }
                                mSongs.add(mSong);
                            }
                        }
                    }
                    //Update ui for adding new elements to list
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressView.setVisibility(View.GONE);
                            updateUI();
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
                        Toast.makeText(AccuracyTraining.this, text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
        if (v.getId() == R.id.icon_prev) {
            if (currentOrder == 0)
                return;
            currentOrder--;

        } else if (v.getId() == R.id.icon_next) {
            if (currentOrder >= mSongs.size() - 1) {
                releasePlayer();
                Intent intent = new Intent(getApplicationContext(), AccuracyTest.class);
                ArrayList<Integer> trackIds = new ArrayList<Integer>();
                ArrayList<Integer> ratings = new ArrayList<Integer>();
                for (Song song : mSongs) {
                    if (song.getRating() > -1) {
                        trackIds.add(song.getId());
                        ratings.add(Math.round(song.getRating()) * 2);
                    }
                }
                intent.putIntegerArrayListExtra("track_ids", trackIds);
                intent.putIntegerArrayListExtra("ratings", ratings);
                intent.putIntegerArrayListExtra("category_ids", categoryIds);
                intent.putIntegerArrayListExtra("artist_ids", artistIds);
                startActivity(intent);
                return;
            }
            currentOrder++;
        }
        isPlaying = false;

        updateUI();
        releasePlayer();
    }

    void updateUI() {
        if (mSongs != null)
            textViewSongCounter.setText("Song: " + (currentOrder + 1) + "/" + mSongs.size());

        ratingBarSong.setRating(mSongs.get(currentOrder).getRating());
        textViewRateSong.setText(String.valueOf(mSongs.get(currentOrder).getRating()));

        textViewArtistName.setText(mSongs.get(currentOrder).getFirstArtistName());
        textViewAlbumName.setText(mSongs.get(currentOrder).getFirstAlbumName());
        textViewSongName.setText(mSongs.get(currentOrder).getName());
        ((ImageView) findViewById(R.id.icon_music_control)).setImageResource(R.drawable.icon_play);

        Picasso.with(this).load(mSongs.get(currentOrder).getImageUrl())
                .placeholder(R.drawable.cover_picture)
                .error(R.drawable.cover_picture)
                .transform(new BlurTransformation(this,23))
                .into(backgroundImage);
        Picasso.with(getApplicationContext())
                .load(mSongs.get(currentOrder).getImageUrl())
                .placeholder(R.drawable.cover_picture)
                .error(R.drawable.album)
                .into(imageViewAlbumImage);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AccuracyTraining.this, CategorySelectorActivity.class);
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

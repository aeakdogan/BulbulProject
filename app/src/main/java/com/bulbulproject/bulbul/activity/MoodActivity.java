package com.bulbulproject.bulbul.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.TrackQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.model.Song;
import com.bulbulproject.bulbul.service.Globals;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;


public class MoodActivity extends AppCompatActivity {

    SeekBar seekBarAcousticness;
    SeekBar seekBarLiveness;
    SeekBar seekBarSpeechiness;
    SeekBar seekBarValence;
    SeekBar seekBarDanceability;
    SeekBar seekBarInstrumentalness;
    SeekBar seekBarTempo;
    SeekBar seekBarEnergy;
    SeekBar seekBarLoudness;


    View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        seekBarAcousticness = (SeekBar) findViewById(R.id.seekBar_acousticness);
        seekBarLiveness = (SeekBar) findViewById(R.id.seekBar_liveness);
        seekBarSpeechiness = (SeekBar) findViewById(R.id.seekBar_speechiness);
        seekBarValence = (SeekBar) findViewById(R.id.seekBar_valence);
        seekBarDanceability = (SeekBar) findViewById(R.id.seekBar_danceability);
        seekBarInstrumentalness = (SeekBar) findViewById(R.id.seekBar_instrumentalness);
        seekBarTempo = (SeekBar) findViewById(R.id.seekBar_tempo);
        seekBarEnergy = (SeekBar) findViewById(R.id.seekBar_energy);
        seekBarLoudness = (SeekBar) findViewById(R.id.seekBar_loudness);


        findViewById(R.id.button_get_songs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_songs();
            }
        });
    }

    void get_songs(){
        mProgressView = findViewById(R.id.login_progress);
        mProgressView.setVisibility(View.VISIBLE);

        //Snackbar.make(seekBarAcousticness, "" + seekBarAcousticness.getProgress(), Snackbar.LENGTH_SHORT).show();

        int progressSeekBarAcousticness = seekBarAcousticness.getProgress();
        int progressSeekBarLiveness = seekBarLiveness.getProgress();
        int progressSeekBarSpeechiness = seekBarSpeechiness.getProgress();
        int progressSeekBarValence = seekBarValence.getProgress();
        int progressSeekBarDanceability = seekBarDanceability.getProgress();
        int progressSeekBarInstrumentalness = seekBarInstrumentalness.getProgress();
        int progressSeekBarTempo = seekBarTempo.getProgress();
        int progressSeekBarEnergy = seekBarEnergy.getProgress();
        int progressSeekBarLoudness = seekBarLoudness.getProgress();

        double min_acousticness = 0;
        double max_acousticness = 1;
        double min_liveness = 0;
        double max_liveness = 1;
        double min_speechiness = 0;
        double max_speechiness = 1;
        double min_valence = 0;
        double max_valence = 1;
        double min_danceability = 0;
        double max_danceability = 1;
        double min_instrumentalness = 0;
        double max_instrumentalness = 1;
        double min_tempo = 0;
        double max_tempo = 250;
        double min_energy = 0;
        double max_energy = 1;
        double min_loudness = 0;
        double max_loudness = -60;

        if(progressSeekBarAcousticness == 0)
            max_acousticness = 0.33;
        else if(progressSeekBarAcousticness == 2)
            min_acousticness = 0.67;

        if(progressSeekBarLiveness == 0)
            max_liveness = 0.33;
        else if(progressSeekBarLiveness == 2)
            min_liveness = 0.67;

        if(progressSeekBarSpeechiness == 0)
            max_speechiness = 0.33;
        else if(progressSeekBarSpeechiness == 2)
            min_speechiness = 0.67;

        if(progressSeekBarValence == 0)
            max_valence = 0.33;
        else if(progressSeekBarValence == 2)
            min_valence = 0.67;

        if(progressSeekBarDanceability == 0)
            max_danceability = 0.33;
        else if(progressSeekBarDanceability == 2)
            min_danceability = 0.67;

        if(progressSeekBarInstrumentalness == 0)
            max_instrumentalness = 0.33;
        else if(progressSeekBarInstrumentalness == 2)
            min_instrumentalness = 0.67;

        if(progressSeekBarTempo == 0)
            max_tempo= 80;
        else if(progressSeekBarTempo == 2)
            min_tempo = 150;

        if(progressSeekBarEnergy == 0)
            max_energy = 0.33;
        else if(progressSeekBarEnergy == 2)
            min_energy = 0.67;

        if(progressSeekBarLoudness == 0)
            max_loudness = -20;
        else if(progressSeekBarLoudness == 2)
            min_loudness = -40;

        Globals.mSongs = new ArrayList<Song>();
        ((App) getApplication()).apolloClient().newCall(
                TrackQuery.builder()
                        .limit(10)
                        .min_acousticness(min_acousticness)
                        .max_acousticness(max_acousticness)
                        .min_liveness(min_liveness)
                        .max_liveness(max_liveness)
                        .min_speechiness(min_speechiness)
                        .max_speechiness(max_speechiness)
                        .min_valence(min_valence)
                        .max_valence(max_valence)
                        .min_danceability(min_danceability)
                        .max_danceability(max_danceability)
                        .min_instrumentalness(min_instrumentalness)
                        .max_instrumentalness(max_instrumentalness)
                        .min_tempo(min_tempo)
                        .max_tempo(max_tempo)
                        .min_energy(min_energy)
                        .max_energy(max_energy)
                        .min_loudness(max_loudness)
                        .max_loudness(min_loudness)
                        .build())
                .enqueue(
                        new ApolloCall.Callback<TrackQuery.Data>() {
                            @Override
                            public void onResponse(@Nonnull com.apollographql.apollo.api.Response<TrackQuery.Data> response) {
                                if (response.isSuccessful()) {
                                    List<TrackQuery.Data.Track> trackList = response.data().tracks();
                                    for (TrackQuery.Data.Track track : trackList) {
                                        //Mapping api's track model to existing Song model

                                        Song mSong = new Song(
                                                track.id(),
                                                track.name(),
                                                track.spotify_track_id(),
                                                0,
                                                track.spotify_album_img()
                                        );
                                        Globals.mSongs.add(mSong);
                                    }
                                    //Update ui for adding new elements to list
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(getApplicationContext(), SongListActivity.class);
                                            startActivity(intent);
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
                        }
                );

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mProgressView != null && mProgressView.getVisibility()==View.VISIBLE)
            mProgressView.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MoodActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*
            audio_features_acousticness
            audio_features_liveness
            audio_features_speechiness
            audio_features_valence
            audio_features_danceability
            audio_features_instrumentalness
            audio_features_tempo
            audio_features_energy
            audio_features_loudness

            min_acousticness
            max_acousticness
            min_liveness
            max_liveness
            min_speechiness
            max_speechiness
            min_valence
            max_valence
            min_danceability
            max_danceability
            min_instrumentalness
            max_instrumentalness
            min_tempo
            max_tempo
            min_energy
            max_energy
            min_loudness
            max_loudness
     */
}

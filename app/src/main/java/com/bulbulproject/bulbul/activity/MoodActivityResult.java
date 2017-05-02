package com.bulbulproject.bulbul.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.apollographql.android.api.graphql.Response;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.TrackQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.MoodActivityRVAdapter;
import com.bulbulproject.bulbul.model.MySong;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class MoodActivityResult extends AppCompatActivity {

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    MoodActivityRVAdapter rvAdapter;
    View mProgressView;
    ArrayList<MySong> mSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSongs = new ArrayList<>();
        get_songs();
    }

    void get_songs(){
        mProgressView = findViewById(R.id.login_progress);
        mProgressView.setVisibility(View.VISIBLE);

        //Snackbar.make(seekBarAcousticness, "" + seekBarAcousticness.getProgress(), Snackbar.LENGTH_SHORT).show();

        int progressSeekBarAcousticness = getIntent().getIntExtra("acousticness",1);
        int progressSeekBarLiveness = getIntent().getIntExtra("liveness",1);
        int progressSeekBarSpeechiness = getIntent().getIntExtra("speechiness",1);
        int progressSeekBarValence = getIntent().getIntExtra("valence",1);
        int progressSeekBarDanceability = getIntent().getIntExtra("danceability",1);
        int progressSeekBarInstrumentalness = getIntent().getIntExtra("intumentalness",1);
        int progressSeekBarTempo = getIntent().getIntExtra("tempo",1);
        int progressSeekBarEnergy = getIntent().getIntExtra("energy",1);
        int progressSeekBarLoudness = getIntent().getIntExtra("loudness",1);

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
                                if (response.data() != null) {
                                    List<TrackQuery.Data.Track> trackList = response.data().tracks();
                                    for (TrackQuery.Data.Track track : trackList) {
                                        //Mapping api's track model to existing Song model

                                        MySong mSong = new MySong(
                                                track.id(),
                                                track.spotify_track_id(),
                                                track.name(),
                                                (track.albums().size()>0)?track.albums().get(0).name():"Album",
                                                (track.artists().size() > 0) ? track.artists().get(0).name() : "Unknown Artist",
//                                                "Artist",
                                                0,
                                                track.spotify_album_img()
                                        );
                                        mSongs.add(mSong);
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
                                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
        );

    }
    void updateUI(){
        rvAdapter = new MoodActivityRVAdapter(mSongs, getApplicationContext());
        mRecyclerView.setAdapter(rvAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MoodActivityResult.this, MoodActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

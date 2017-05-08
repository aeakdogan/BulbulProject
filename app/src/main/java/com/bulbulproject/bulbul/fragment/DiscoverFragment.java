package com.bulbulproject.bulbul.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.TrackQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.activity.SongListActivity;
import com.bulbulproject.bulbul.adapter.SongsRVAdapter;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.Song;
import com.bulbulproject.bulbul.service.Globals;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by mesutgurlek on 2/12/17.
 */

public class DiscoverFragment extends Fragment {

//    List<Song> mSongs;
//    private SongsRVAdapter rvAdapter;
//    private LinearLayoutManager mLayoutManager;
//    private RecyclerView mRecyclerView;
//    private LocalBroadcastManager mBroadcastManager;

    private View mProgressView;
    private View rootView;
    View linLayoutView;

    SeekBar seekBarAcousticness;
    SeekBar seekBarLiveness;
    SeekBar seekBarSpeechiness;
    SeekBar seekBarValence;
    SeekBar seekBarDanceability;
    SeekBar seekBarInstrumentalness;
    SeekBar seekBarTempo;
    SeekBar seekBarEnergy;
    SeekBar seekBarLoudness;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        seekBarAcousticness = (SeekBar) rootView.findViewById(R.id.seekBar_acousticness);
        seekBarLiveness = (SeekBar) rootView.findViewById(R.id.seekBar_liveness);
        seekBarSpeechiness = (SeekBar) rootView.findViewById(R.id.seekBar_speechiness);
        seekBarValence = (SeekBar) rootView.findViewById(R.id.seekBar_valence);
        seekBarDanceability = (SeekBar) rootView.findViewById(R.id.seekBar_danceability);
        seekBarInstrumentalness = (SeekBar) rootView.findViewById(R.id.seekBar_instrumentalness);
        seekBarTempo = (SeekBar) rootView.findViewById(R.id.seekBar_tempo);
        seekBarEnergy = (SeekBar) rootView.findViewById(R.id.seekBar_energy);
        seekBarLoudness = (SeekBar) rootView.findViewById(R.id.seekBar_loudness);

        rootView.findViewById(R.id.button_get_songs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_songs();
                linLayoutView.setVisibility(View.VISIBLE);
                mProgressView.setVisibility(View.GONE);

            }
        });

        return rootView;
    }

    void get_songs(){
        mProgressView = rootView.findViewById(R.id.progress);
        linLayoutView = rootView.findViewById(R.id.linear_layout);
        linLayoutView.setVisibility(View.GONE);
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
        ((App) getActivity().getApplication()).apolloClient().newCall(
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
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(getActivity(), SongListActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(@Nonnull ApolloException e) {

                                final String text = e.getMessage();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }
                );

    }
}


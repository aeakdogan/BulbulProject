package com.bulbulproject.bulbul.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.TrackQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.SongsRVAdapter;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.Song;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by mesutgurlek on 2/12/17.
 */

public class DiscoverFragment extends Fragment {


    // Array of strings for ListView Title
    List<Song> mSongs;
    private LocalBroadcastManager mBroadcastManager;
    private View mProgressView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SongsRVAdapter rvAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {


        mBroadcastManager = LocalBroadcastManager.getInstance(getActivity().getApplicationContext());
        mSongs = new ArrayList<Song>();
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        rvAdapter = new SongsRVAdapter(mSongs, getContext());
        mRecyclerView.setAdapter(rvAdapter);
        final ArrayList<String> songsList = new ArrayList<String>();

        mProgressView = rootView.findViewById(R.id.progress);
        mProgressView.setVisibility(View.VISIBLE);

        //Fetch data and update ui
        ((App) getActivity().getApplication()).apolloClient().newCall(
                TrackQuery.builder()
                        .limit(10)
                        .build())
                .enqueue(
                        new ApolloCall.Callback<TrackQuery.Data>() {
                            @Override
                            public void onResponse(@Nonnull Response<TrackQuery.Data> response) {
                                if (response.isSuccessful()) {
                                    List<TrackQuery.Data.Track> trackList = response.data().tracks();
                                    if (trackList != null) {
                                        for (TrackQuery.Data.Track track : trackList) {
                                            //Mapping api's track model to existing Song model
                                            Song mSong = new Song(track.id(),
                                                    track.name(),
                                                    track.spotify_track_id(),
                                                    0,
                                                    track.spotify_album_img()

                                            );
                                            if(track.artists() != null) {
                                                for (TrackQuery.Data.Artist artist : track.artists()) {
                                                    mSong.getArtists().add(new Artist(artist.name()));
                                                }
                                            }

                                            if(track.albums()!=null) {
                                                for (TrackQuery.Data.Album album : track.albums()) {
                                                    mSong.getAlbums().add(new Album(album.name(), album.image()));
                                                }
                                            }
                                            mSongs.add(mSong);
                                        }
                                    }

                                    for (Song song : mSongs) {
                                        songsList.add(song.getSpotifyUrl());
                                    }
                                    //Update ui for adding new elements to list
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mProgressView.setVisibility(View.GONE);
                                            rvAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(@Nonnull ApolloException e) {
                                e.printStackTrace();
                                final String text = e.getMessage();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

        return rootView;
    }
}

package com.bulbulproject.bulbul.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.UserSongsQuery;
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
 * Created by burak on 13.02.2017.
 */
public class MySongsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View mProgressView;

    private List<Song> mSongs;

    public MySongsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSongs = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_songs, container, false);
        mProgressView = rootView.findViewById(R.id.progress);
        mProgressView.setVisibility(View.VISIBLE);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        rvAdapter = new SongsRVAdapter(mSongs, getContext());
        mRecyclerView.setAdapter(rvAdapter);

        String token = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE).getString("AUTH_TOKEN", "");
        ((App) getActivity().getApplication()).apolloClient().newCall(UserSongsQuery.builder().token(token).build()).enqueue(new ApolloCall.Callback<UserSongsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<UserSongsQuery.Data> response) {
                if (response.isSuccessful()) {
                    UserSongsQuery.Data.User user = response.data().users().get(0);
                    if (user.listenedTracks() != null) {

                        for (UserSongsQuery.Data.ListenedTrack track : user.listenedTracks()) {
                            Song mSong = new Song(
                                    track.id(),
                                    track.name(),
                                    track.spotify_album_img(),
                                    track.spotify_track_id()
                            );
                            Log.d("bulbul","track name: " + mSong.getName() + " album_img: " + mSong.getImageUrl());
                            if (track.artists() != null) {
                                for (UserSongsQuery.Data.Artist artist : track.artists()) {
                                    mSong.getArtists().add(new Artist(artist.id(), artist.name(), artist.image()));
                                }
                            }

                            if (track.albums() != null) {
                                for (UserSongsQuery.Data.Album album : track.albums()) {
                                    mSong.getAlbums().add(new Album(album.name(), album.image()));
                                }
                            }
                            mSongs.add(mSong);
                        }
                    }
                    if (getActivity() != null) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressView.setVisibility(View.GONE);
                                rvAdapter.notifyDataSetChanged();
                            }
                        });
                    }
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
        });

        return rootView;
    }

}

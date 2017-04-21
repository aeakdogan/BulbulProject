package com.bulbulproject.bulbul.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apollographql.android.ApolloCall;
import com.apollographql.android.api.graphql.Response;
import com.bulbulproject.UserSongsQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.SongsRVAdapter;
import com.bulbulproject.bulbul.adapter.SongsRVAdapter;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.Song;
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
    private List<Song> mSongs;

    public MySongsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSongs = new ArrayList<Song>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_songs, container, false);

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
                    if (user.followedTracks() != null) {
                        for (UserSongsQuery.Data.User.FollowedTrack track : user.followedTracks()) {
                            Song song = new Song(track.id(), track.name(), 0, track.spotify_track_id());

                            if (track.artists() != null) {
                                for (UserSongsQuery.Data.User.FollowedTrack.Artist artist : track.artists()) {
                                    song.getArtists().add(new Artist(artist.name()));
                                }
                            }
                            mSongs.add(song);
                        }
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rvAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@Nonnull Throwable t) {
                final String text = t.getMessage();
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

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
import com.bulbulproject.UserArtistsQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.ArtistsRVAdapter;
import com.bulbulproject.bulbul.model.Artist;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by burak on 13.02.2017.
 */
public class MyArtistsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Artist> artists;

    public MyArtistsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artists = new ArrayList<Artist>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_artists, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        rvAdapter = new ArtistsRVAdapter(artists, getContext());
        mRecyclerView.setAdapter(rvAdapter);
        String token = getActivity().getApplication().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE).getString("AUTH_TOKEN", "");
        ((App) getActivity().getApplication()).apolloClient().newCall(UserArtistsQuery.builder().token(token).build()).enqueue(new ApolloCall.Callback<UserArtistsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<UserArtistsQuery.Data> response) {
                if (response.isSuccessful()) {
                    UserArtistsQuery.Data.User user = response.data().users().get(0);
                    if (user.followedArtists() != null) {
                        for (UserArtistsQuery.Data.User.FollowedArtist artist : user.followedArtists()) {
                            Artist newArtist = new Artist(artist.name());
                            newArtist.setId(artist.id());
                            newArtist.setImageUrl(artist.image());
                            newArtist.setAlbumsCount(artist.albumsCount());
                            artists.add(newArtist);
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

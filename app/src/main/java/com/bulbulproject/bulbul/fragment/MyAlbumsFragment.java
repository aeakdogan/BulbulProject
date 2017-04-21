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
import com.bulbulproject.UserAlbumsQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.AlbumsRVAdapter;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Artist;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by burak on 13.02.2017.
 */
public class MyAlbumsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Album> albums;

    public MyAlbumsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albums = new ArrayList<Album>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_albums, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        rvAdapter = new AlbumsRVAdapter(albums, getContext());
        mRecyclerView.setAdapter(rvAdapter);
        String token = getActivity().getApplication().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE).getString("AUTH_TOKEN", "");
        ((App) getActivity().getApplication()).apolloClient().newCall(UserAlbumsQuery.builder().token(token).build()).enqueue(new ApolloCall.Callback<UserAlbumsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<UserAlbumsQuery.Data> response) {
                if (response.isSuccessful()) {
                    UserAlbumsQuery.Data.User user = response.data().users().get(0);
                    if (user.followedAlbums() != null) {
                        for (UserAlbumsQuery.Data.User.FollowedAlbum album : user.followedAlbums()) {
                            Album newAlbum = new Album(album.name(), 0, album.image());
                            newAlbum.setId(album.id());
                            newAlbum.setSongsCount(album.tracksCount());
                            if (album.artists() != null) {
                                for (UserAlbumsQuery.Data.User.FollowedAlbum.Artist artist : album.artists()) {
                                    newAlbum.getArtists().add(new Artist(artist.name()));
                                }
                            }
                            albums.add(newAlbum);
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

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

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
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
    private View mProgressView;

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

        mProgressView = rootView.findViewById(R.id.progress);
        mProgressView.setVisibility(View.VISIBLE);

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
                    if (user.listenedAlbums() != null) {
                        for (UserAlbumsQuery.Data.ListenedAlbum album : user.listenedAlbums()) {
                            Album newAlbum = new Album(album.name(), 0, album.image());
                            newAlbum.setId(album.id());
                            newAlbum.setSongsCount(album.tracksCount());
                            if (album.artists() != null) {
                                for (UserAlbumsQuery.Data.Artist artist : album.artists()) {
                                    newAlbum.getArtists().add(new Artist(artist.name()));
                                }
                            }
                            albums.add(newAlbum);
                        }
                    }
                    if(getActivity()!= null) {
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

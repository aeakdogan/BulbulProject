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
import com.bulbulproject.UserPlaylistsQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.activity.AlbumActivity;
import com.bulbulproject.bulbul.adapter.PlaylistsRVAdapter;
import com.bulbulproject.bulbul.model.Playlist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by burak on 13.02.2017.
 */
public class MyPlaylistsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View mProgressView;

    private List<Playlist> mPlaylists;

    public MyPlaylistsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlaylists = new ArrayList<Playlist>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_playlists, container, false);

        mProgressView = rootView.findViewById(R.id.progress);
        mProgressView.setVisibility(View.VISIBLE);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        rvAdapter = new PlaylistsRVAdapter(mPlaylists, getContext());
        mRecyclerView.setAdapter(rvAdapter);

        String token = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE).getString("AUTH_TOKEN", "");

        ((App) getActivity().getApplication()).apolloClient().newCall(UserPlaylistsQuery.builder().token(token).build()).enqueue(new ApolloCall.Callback<UserPlaylistsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<UserPlaylistsQuery.Data> response) {
                if (response.isSuccessful()) {
                    mPlaylists.clear();
                    UserPlaylistsQuery.Data.User user = response.data().users().get(0);
                    if (user.playlists() != null) {
                        for (UserPlaylistsQuery.Data.Playlist playlist : user.playlists()) {
                            Playlist newPlaylist = new Playlist(playlist.name(), playlist.id());
                            newPlaylist.setSongsCount(playlist.tracksCount());
                            mPlaylists.add(newPlaylist);
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

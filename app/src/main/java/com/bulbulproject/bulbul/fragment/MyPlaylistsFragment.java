package com.bulbulproject.bulbul.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.PlaylistsRVAdapter;
import com.bulbulproject.bulbul.model.Playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burak on 13.02.2017.
 */
public class MyPlaylistsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Playlist> playlists;

    public MyPlaylistsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_my_playlists, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        rvAdapter = new PlaylistsRVAdapter(playlists, getContext());
        mRecyclerView.setAdapter(rvAdapter);

        return rootView;
    }

    private void initializeData() {
        playlists = new ArrayList<>();
        playlists.add(new Playlist("Playlist 1", R.drawable.playlist));
        playlists.add(new Playlist("Playlist 2", R.drawable.playlist));
        playlists.add(new Playlist("Playlist 3", R.drawable.playlist));
        playlists.add(new Playlist("Playlist 4", R.drawable.playlist));
        playlists.add(new Playlist("Playlist 5", R.drawable.playlist));
        playlists.add(new Playlist("Playlist 6", R.drawable.playlist));
        playlists.add(new Playlist("Playlist 7", R.drawable.playlist));
        playlists.add(new Playlist("Playlist 8", R.drawable.playlist));
        playlists.add(new Playlist("Playlist 9", R.drawable.playlist));
        playlists.add(new Playlist("Playlist 10", R.drawable.playlist));
    }

}

package com.bulbulproject.bulbul.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.AlbumsRVAdapter;
import com.bulbulproject.bulbul.model.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burak on 13.02.2017.
 */
public class MyAlbumsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Album> albums;

    public MyAlbumsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_my_albums, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        rvAdapter = new AlbumsRVAdapter(albums, getContext());
        mRecyclerView.setAdapter(rvAdapter);

        return rootView;
    }

    private void initializeData() {
        albums = new ArrayList<>();
        albums.add(new Album("Album 1", 1995, R.drawable.album));
        albums.add(new Album("Album 2", 2000, R.drawable.album));
        albums.add(new Album("Album 3", 2002, R.drawable.album));
        albums.add(new Album("Album 4", 2002, R.drawable.album));
        albums.add(new Album("Album 5", 2002, R.drawable.album));
        albums.add(new Album("Album 6", 2002, R.drawable.album));
        albums.add(new Album("Album 7", 2002, R.drawable.album));
        albums.add(new Album("Album 8", 2002, R.drawable.album));
        albums.add(new Album("Album 9", 2002, R.drawable.album));
        albums.add(new Album("Album 10", 2002, R.drawable.album));
    }

}

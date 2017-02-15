package com.bulbulproject.bulbul.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.ArtistsRVAdapter;
import com.bulbulproject.bulbul.model.Artist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burak on 13.02.2017.
 */
public class MyArtistsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Artist> artists;

    public MyArtistsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_my_artists, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        rvAdapter = new ArtistsRVAdapter(artists, getContext());
        mRecyclerView.setAdapter(rvAdapter);

        return rootView;
    }

    private void initializeData() {
        artists = new ArrayList<>();
        artists.add(new Artist("Artist 1", R.drawable.artist));
        artists.add(new Artist("Artist 2", R.drawable.artist));
        artists.add(new Artist("Artist 3", R.drawable.artist));
        artists.add(new Artist("Artist 4", R.drawable.artist));
        artists.add(new Artist("Artist 5", R.drawable.artist));
        artists.add(new Artist("Artist 6", R.drawable.artist));
        artists.add(new Artist("Artist 7", R.drawable.artist));
        artists.add(new Artist("Artist 8", R.drawable.artist));
        artists.add(new Artist("Artist 9", R.drawable.artist));
        artists.add(new Artist("Artist 10", R.drawable.artist));
    }

}

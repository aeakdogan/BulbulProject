package com.bulbulproject.bulbul.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.MoodsAdapter;
import com.bulbulproject.bulbul.adapter.PlaylistsRVAdapter;
import com.bulbulproject.bulbul.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class MoodsGenresActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Playlist> playlists;

    public MoodsGenresActivity() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_moods_genres);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        initializeData();

        rvAdapter = new MoodsAdapter(playlists, getApplicationContext());
        mRecyclerView.setAdapter(rvAdapter);





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

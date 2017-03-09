package com.bulbulproject.bulbul.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.AlbumsRVAdapter;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Artist;

import java.util.ArrayList;
import java.util.List;

public class ArtistActivity extends AppCompatActivity {
    private Artist mArtist;
    private RecyclerView.Adapter mRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        initializeData();

        ImageView artistImage = (ImageView) findViewById(R.id.artist_image);
        RecyclerView listView = (RecyclerView) findViewById(R.id.artist_album_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(mArtist.getName());
        artistImage.setImageResource(mArtist.getPhotoId());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRVAdapter = new AlbumsRVAdapter(mArtist.getAlbums(), this);
        listView.setAdapter(mRVAdapter);
        listView.setLayoutManager(layoutManager);
    }



    private void initializeData() {
        mArtist = new Artist("Artist 1", R.drawable.artist);
        List<Album> mAlbums;
        mAlbums = new ArrayList<>();
        mAlbums.add(new Album("Album 1", 1995, R.drawable.album));
        mAlbums.add(new Album("Album 2", 2000, R.drawable.album));
        mAlbums.add(new Album("Album 3", 2002, R.drawable.album));
        mAlbums.add(new Album("Album 4", 2002, R.drawable.album));
        mAlbums.add(new Album("Album 5", 2002, R.drawable.album));
        mAlbums.add(new Album("Album 6", 2002, R.drawable.album));
        mAlbums.add(new Album("Album 7", 2002, R.drawable.album));
        mAlbums.add(new Album("Album 8", 2002, R.drawable.album));
        mAlbums.add(new Album("Album 9", 2002, R.drawable.album));
        mAlbums.add(new Album("Album 10", 2002, R.drawable.album));

        mArtist.setAlbums(mAlbums);

    }
}

package com.bulbulproject.bulbul.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.SongsRVAdapter;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Song;

import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {
    private Album mAlbum;
    private RecyclerView.Adapter mRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        initializeData();

        ImageView albumImage = (ImageView) findViewById(R.id.album_image);
        TextView albumArtist = (TextView) findViewById(R.id.album_artist);
        RecyclerView listView = (RecyclerView) findViewById(R.id.album_song_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(mAlbum.getName());
        albumImage.setImageResource(mAlbum.getPhotoId());
        albumArtist.setText(mAlbum.getArtistsString());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRVAdapter = new SongsRVAdapter(mAlbum.getSongs(), this);
        listView.setAdapter(mRVAdapter);
        listView.setLayoutManager(layoutManager);
    }

    private void initializeData() {
        mAlbum = new Album("Album 1", 1995, R.drawable.album);

        List<Song> songList = new ArrayList<Song>();

        songList.add(new Song(1, "Song 1", R.drawable.cover_picture, 0));
        songList.add(new Song(2, "Song 2", R.drawable.cover_picture, 2));
        songList.add(new Song(3, "Song 3", R.drawable.cover_picture, 1));
        songList.add(new Song(4, "Song 4", R.drawable.cover_picture, 3.4f));
        songList.add(new Song(5, "Song 5", R.drawable.cover_picture, 4.6f));
        songList.add(new Song(6, "Song 6", R.drawable.cover_picture, 2.7f));

        mAlbum.setSongs(songList);
    }
}

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
import com.bulbulproject.bulbul.model.Playlist;
import com.bulbulproject.bulbul.model.Song;

import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {
    private Playlist mPlaylist;
    private RecyclerView.Adapter mRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        initializeData();

        RecyclerView listView = (RecyclerView) findViewById(R.id.playlist_song_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView playlistImage = (ImageView) findViewById(R.id.playlist_image);

        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(mPlaylist.getName());
        playlistImage.setImageResource(mPlaylist.getPhotoId());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRVAdapter = new SongsRVAdapter(mPlaylist.getSongs(), this);
        listView.setAdapter(mRVAdapter);
        listView.setLayoutManager(layoutManager);
    }

    private void initializeData() {
        mPlaylist =  new Playlist("Playlist 1", R.drawable.playlist);

        List<Song> songList = new ArrayList<Song>();

        songList.add(new Song(1, "Song 1", R.drawable.cover_picture, 0));
        songList.add(new Song(2, "Song 2", R.drawable.cover_picture, 2));
        songList.add(new Song(3, "Song 3", R.drawable.cover_picture, 1));
        songList.add(new Song(4, "Song 4", R.drawable.cover_picture, 3.4f));
        songList.add(new Song(5, "Song 5", R.drawable.cover_picture, 4.6f));
        songList.add(new Song(6, "Song 6", R.drawable.cover_picture, 2.7f));

        mPlaylist.setSongs(songList);
    }
}

package com.bulbulproject.bulbul.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.AlbumQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.SongsRVAdapter;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.Song;
import com.squareup.picasso.Picasso;


import javax.annotation.Nonnull;

public class AlbumActivity extends AppCompatActivity {
    private Album mAlbum;
    private int mAlbumId;
    private RecyclerView.Adapter mRVAdapter;
    private ImageView albumImage;
    private TextView albumArtist;
    private RecyclerView listView;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView.LayoutManager layoutManager;
    private View mProgressView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        albumImage = (ImageView) findViewById(R.id.album_image);
        albumArtist = (TextView) findViewById(R.id.album_artist);
        listView = (RecyclerView) findViewById(R.id.album_song_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setSupportActionBar(toolbar);
        mAlbum = new Album("Loading...", 0, "");
        mProgressView = findViewById(R.id.progress);
        mProgressView.setVisibility(View.VISIBLE);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(mAlbum.getName());

        layoutManager = new LinearLayoutManager(this);
        mRVAdapter = new SongsRVAdapter(mAlbum.getSongs(), this);
        listView.setAdapter(mRVAdapter);
        listView.setLayoutManager(layoutManager);


        mAlbumId = getIntent().getIntExtra("id", -1);
        if (mAlbumId > -1) {
            ((App) getApplication()).apolloClient().newCall(AlbumQuery.builder().id(mAlbumId).build())
                    .enqueue(new ApolloCall.Callback<AlbumQuery.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<AlbumQuery.Data> response) {
                            if (response.isSuccessful()) {

                                AlbumQuery.Data.Album album = response.data().albums().get(0);
                                mAlbum.setName(album.name());
                                mAlbum.setImageUrl(album.image());

                                if(album.artists() != null) {
                                    for (AlbumQuery.Data.Artist artist : album.artists()) {
                                        mAlbum.getArtists().add(new Artist(artist.name()));
                                    }
                                }

                                if(album.tracks()!=null) {
                                    for (AlbumQuery.Data.Track track : album.tracks()) {
                                        Song song = new Song(track.id(), track.name(), track.spotify_album_img(), track.spotify_track_id());
                                        if(track.artists() != null) {
                                            for (AlbumQuery.Data.Artist1 artist : track.artists()) {
                                                song.getArtists().add(new Artist(artist.id(), artist.name(), artist.image()));
                                            }
                                        }
                                        mAlbum.getSongs().add(song);
                                    }
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressView.setVisibility(View.GONE);
                                        Picasso.with(AlbumActivity.this).load(mAlbum.getImageUrl()).into(albumImage);
                                        albumArtist.setText(mAlbum.getArtistsString());
                                        collapsingToolbarLayout.setTitle(mAlbum.getName());
                                        mRVAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {
                            final String text = e.getMessage();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AlbumActivity.this, text, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
        }
        else {
            Toast.makeText(AlbumActivity.this, R.string.album_activity_fetch_error, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

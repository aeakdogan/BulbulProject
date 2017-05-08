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
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.PlaylistsQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.SongsRVAdapter;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.Song;
import com.bulbulproject.bulbul.model.Playlist;

import javax.annotation.Nonnull;

public class PlaylistActivity extends AppCompatActivity {
    private Playlist mPlaylist;
    private int mPlaylistId;
    private RecyclerView.Adapter mRVAdapter;
    private ImageView playlistImage;
    private RecyclerView listView;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView.LayoutManager layoutManager;
    private View mProgressView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        mProgressView = findViewById(R.id.progress);
        mProgressView.setVisibility(View.VISIBLE);
        listView = (RecyclerView) findViewById(R.id.playlist_song_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        playlistImage = (ImageView) findViewById(R.id.playlist_image);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mPlaylist = new Playlist("Loading...", R.drawable.playlist);

        playlistImage.setImageResource(mPlaylist.getPhotoId());
        layoutManager = new LinearLayoutManager(this);
        mRVAdapter = new SongsRVAdapter(mPlaylist.getSongs(), this);
        listView.setAdapter(mRVAdapter);
        listView.setLayoutManager(layoutManager);

        collapsingToolbarLayout.setTitle(mPlaylist.getName());

        mPlaylistId = getIntent().getIntExtra("id", -1);

        if (mPlaylistId > -1) {
            ((App) getApplication()).apolloClient().newCall(PlaylistsQuery.builder().id(mPlaylistId).build()).enqueue(new ApolloCall.Callback<PlaylistsQuery.Data>() {
                @Override
                public void onResponse(@Nonnull Response<PlaylistsQuery.Data> response) {
                    if (response.isSuccessful()) {
                        final PlaylistsQuery.Data.Playlist playlist = response.data().playlists().get(0);
                        mPlaylist.setName(playlist.name());
                        mPlaylist.setId(playlist.id());
                        if (playlist.tracks() != null) {
                            for (PlaylistsQuery.Data.Track track : playlist.tracks()) {
                                Song song = new Song(track.id(), track.name(), track.spotify_album_img(), track.spotify_track_id());
                                if (track.artists() != null) {
                                    for (PlaylistsQuery.Data.Artist trackArtist : track.artists()) {
                                        song.getArtists().add(new Artist(trackArtist.id(), trackArtist.name(), trackArtist.image()));
                                    }
                                }
                                mPlaylist.getSongs().add(song);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressView.setVisibility(View.GONE);
                                collapsingToolbarLayout.setTitle(mPlaylist.getName());
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
                            Toast.makeText(PlaylistActivity.this, text, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            });
        } else {
            Toast.makeText(PlaylistActivity.this, R.string.playlist_activity_fetch_error, Toast.LENGTH_SHORT).show();
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

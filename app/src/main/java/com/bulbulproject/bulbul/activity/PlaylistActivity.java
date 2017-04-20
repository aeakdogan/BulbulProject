package com.bulbulproject.bulbul.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.android.ApolloCall;
import com.apollographql.android.api.graphql.Response;
import com.bulbulproject.PlaylistQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.SongsRVAdapter;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.Playlist;
import com.bulbulproject.bulbul.model.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        listView = (RecyclerView) findViewById(R.id.playlist_song_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        playlistImage = (ImageView) findViewById(R.id.playlist_image);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mPlaylist =  new Playlist("Loading...", R.drawable.playlist);

        playlistImage.setImageResource(mPlaylist.getPhotoId());
        layoutManager = new LinearLayoutManager(this);
        mRVAdapter = new SongsRVAdapter(mPlaylist.getSongs(), this);
        listView.setAdapter(mRVAdapter);
        listView.setLayoutManager(layoutManager);

        collapsingToolbarLayout.setTitle(mPlaylist.getName());

        mPlaylistId = getIntent().getIntExtra("id",-1);

        if(mPlaylistId > -1){
            ((App)getApplication()).apolloClient().newCall(PlaylistQuery.builder().id(mPlaylistId).build()).enqueue(new ApolloCall.Callback<PlaylistQuery.Data>() {
                @Override
                public void onResponse(@Nonnull Response<PlaylistQuery.Data> response) {
                    if(response.isSuccessful()){
                        final PlaylistQuery.Data.Playlist playlist = response.data().playlists().get(0);
                        mPlaylist.setName(playlist.name());
                        mPlaylist.setId(playlist.id());
                        if(playlist.tracks()!=null){
                            for(PlaylistQuery.Data.Playlist.Track track : playlist.tracks()){
                                Song song = new Song(track.id(),track.name(), 0, track.spotify_track_id());
                                if(track.artists() != null){
                                    for(PlaylistQuery.Data.Playlist.Track.Artist trackArtist: track.artists()){
                                        song.getArtists().add(new Artist(trackArtist.name()));
                                    }
                                }
                                mPlaylist.getSongs().add(song);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                collapsingToolbarLayout.setTitle(mPlaylist.getName());
                                mRVAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(@Nonnull Throwable t) {
                    final String text = t.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PlaylistActivity.this, text, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else{
            Toast.makeText(PlaylistActivity.this, R.string.playlist_activity_fetch_error, Toast.LENGTH_SHORT).show();
        }

    }

}

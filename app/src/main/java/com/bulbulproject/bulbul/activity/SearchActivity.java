package com.bulbulproject.bulbul.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.AlbumQuery;
import com.bulbulproject.ArtistQuery;
import com.bulbulproject.TrackQuery;
import com.bulbulproject.UserAlbumsQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.SearchResultRVAdapter;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.MySong;
import com.bulbulproject.bulbul.model.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class SearchActivity extends AppCompatActivity {
    RecyclerView recyclerViewSearchResults;
    EditText editTextSearch;
    private LinearLayoutManager mLayoutManager;
    private SearchResultRVAdapter mRVAdapter;

    ArrayList<MySong> mSongs;
    ArrayList<Artist> mArtists;
    ArrayList<Album> mAlbums;
    private View mProgressView;
    int downloadCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSongs = new ArrayList<>();
        mArtists = new ArrayList<>();
        mAlbums = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        editTextSearch = (EditText) findViewById(R.id.editText_search);
        recyclerViewSearchResults = (RecyclerView) findViewById(R.id.search_results);

        mLayoutManager = new LinearLayoutManager(this);
        mRVAdapter = new SearchResultRVAdapter(this, mSongs, mArtists, mAlbums);
        recyclerViewSearchResults.setAdapter(mRVAdapter);
        recyclerViewSearchResults.setLayoutManager(mLayoutManager);

        mProgressView = findViewById(R.id.progress);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 3)
                    getSearchResult(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findViewById(R.id.icon_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearchResult(editTextSearch.getText().toString());
            }
        });
        mProgressView.setVisibility(View.GONE);
    }
    void updateSearchResult(){
        mRVAdapter.notifyDataSetChanged();
        mProgressView.setVisibility(View.GONE);
    }
    void getSearchResult(String searchText){
        mProgressView.setVisibility(View.VISIBLE);
        downloadCounter = 0;
        ((App) getApplication()).apolloClient().newCall(
                TrackQuery.builder()
                        .limit(3)
                        .build())
                .enqueue(
                        new ApolloCall.Callback<TrackQuery.Data>() {
                            @Override
                            public void onResponse(@Nonnull com.apollographql.apollo.api.Response<TrackQuery.Data> response) {
                                if (response.isSuccessful()) {
                                    List<TrackQuery.Data.Track> trackList = response.data().tracks();
                                    for (TrackQuery.Data.Track track : trackList) {

                                        MySong mSong = new MySong(
                                                track.id(),
                                                track.spotify_track_id(),
                                                track.name(),
                                                (track.albums().size()>0)?track.albums().get(0).name():"Album",
                                                (track.artists().size() > 0) ? track.artists().get(0).name() : "Unknown Artist",
                                                0,
                                                track.spotify_album_img()
                                        );
                                        mSongs.add(mSong);
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("bulbul","songs finished");
                                            if(++downloadCounter == 3){
                                                updateSearchResult();
                                            }
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
                                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                );

        ((App) getApplication()).apolloClient().newCall(ArtistQuery.builder().limit(2).build()).enqueue(new ApolloCall.Callback<ArtistQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<ArtistQuery.Data> response) {
                if (response.isSuccessful()) {
                    if (response.data().artists() != null) {
                        for (ArtistQuery.Data.Artist artist : response.data().artists()) {
                            mArtists.add(new Artist(artist.id(), artist.name(), artist.image()));
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("bulbul","artists finished");
                            if(++downloadCounter == 3){
                                updateSearchResult();
                            }
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
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        ((App) getApplication()).apolloClient().newCall(AlbumQuery.builder().limit(2).build())
                .enqueue(new ApolloCall.Callback<AlbumQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<AlbumQuery.Data> response) {
                        if (response.isSuccessful()) {

                            for (AlbumQuery.Data.Album album : response.data().albums()) {
                                Album mAlbum;
                                mAlbum = new Album("Loading...", 0, "");
                                mAlbum.setId(album.id());
                                mAlbum.setName(album.name());
                                mAlbum.setImageUrl(album.image());

                                if (album.artists() != null) {
                                    for (AlbumQuery.Data.Artist artist : album.artists()) {
                                        mAlbum.getArtists().add(new Artist(artist.name()));
                                    }
                                }

                                if (album.tracks() != null) {
                                    for (AlbumQuery.Data.Track track : album.tracks()) {
                                        Song song = new Song(track.id(), track.name(), 0, track.spotify_track_id());

                                        if (track.artists() != null) {
                                            for (AlbumQuery.Data.Artist1 artist : track.artists()) {
                                                song.getArtists().add(new Artist(artist.name()));
                                            }
                                        }
                                        mAlbum.getSongs().add(song);
                                    }
                                }
                                mAlbums.add(mAlbum);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("bulbul","albums finished");
                                    if(++downloadCounter == 3){
                                        updateSearchResult();
                                    }
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
                                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
}

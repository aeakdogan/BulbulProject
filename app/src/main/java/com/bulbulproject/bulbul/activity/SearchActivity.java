package com.bulbulproject.bulbul.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.SearchQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.SearchResultRVAdapter;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.Song;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class SearchActivity extends AppCompatActivity {
    RecyclerView recyclerViewSearchResults;
    EditText editTextSearch;
    private LinearLayoutManager mLayoutManager;
    private SearchResultRVAdapter mRVAdapter;

    ArrayList<Song> mSongs;
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

        findViewById(R.id.icon_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearchResult(editTextSearch.getText().toString());
            }
        });
        mProgressView.setVisibility(View.GONE);
    }

    void updateSearchResult() {
        mRVAdapter.notifyDataSetChanged();
        mProgressView.setVisibility(View.GONE);
    }

    void getSearchResult(String searchText) {
        mSongs.clear();
        mArtists.clear();
        mAlbums.clear();
        mProgressView.setVisibility(View.VISIBLE);
        downloadCounter = 0;

        ((App) getApplication()).apolloClient().newCall(SearchQuery.builder().limit(2).query(searchText).build()).enqueue(new ApolloCall.Callback<SearchQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<SearchQuery.Data> response) {
                if (response.isSuccessful()) {


                    List<SearchQuery.Data.Track> trackList = response.data().tracks();
                    for (SearchQuery.Data.Track track : trackList) {

                        Song mSong = new Song(
                                track.id(),
                                track.name(),
                                track.spotify_track_id(),
                                0,
                                track.spotify_album_img()
                        );
                        if (track.artists() != null) {
                            for (SearchQuery.Data.Artist artist : track.artists()) {
                                mSong.getArtists().add(new Artist(artist.name()));
                            }
                        }

                        if (track.albums() != null) {
                            for (SearchQuery.Data.Album album : track.albums()) {
                                mSong.getAlbums().add(new Album(album.name(), album.image()));
                            }
                        }
                        mSongs.add(mSong);
                    }

                    for (SearchQuery.Data.Artist1 artist : response.data().artists()) {
                        mArtists.add(new Artist(artist.id(), artist.name(), artist.image()));
                    }

                    for (SearchQuery.Data.Album2 album : response.data().albums()) {
                        Album mAlbum;
                        mAlbum = new Album("Loading...", 0, "");
                        mAlbum.setId(album.id());
                        mAlbum.setName(album.name());
                        mAlbum.setImageUrl(album.image());

                        if (album.artists() != null) {
                            for (SearchQuery.Data.Artist3 artist : album.artists()) {
                                mAlbum.getArtists().add(new Artist(artist.name()));
                            }
                        }

                        if (album.tracks() != null) {
                            for (SearchQuery.Data.Track1 track : album.tracks()) {
                                Song song = new Song(track.id(), track.name(), track.spotify_album_img(), track.spotify_track_id());

                                if (track.artists() != null) {
                                    for (SearchQuery.Data.Artist4 artist : track.artists()) {
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
                        updateSearchResult();
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

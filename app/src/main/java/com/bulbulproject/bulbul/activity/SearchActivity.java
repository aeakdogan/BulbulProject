package com.bulbulproject.bulbul.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.ArtistQuery;
import com.bulbulproject.TrackQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.SearchResultRVAdapter;
import com.bulbulproject.bulbul.adapter.UsersRVAdapter;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.MySong;
import com.bulbulproject.bulbul.model.Song;
import com.bulbulproject.bulbul.service.Globals;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSongs = new ArrayList<MySong>();
        mArtists = new ArrayList<Artist>();

        editTextSearch = (EditText) findViewById(R.id.editText_search);
        recyclerViewSearchResults = (RecyclerView) findViewById(R.id.search_results);

        mLayoutManager = new LinearLayoutManager(this);
        mRVAdapter = new SearchResultRVAdapter(this, mSongs, mArtists);
        recyclerViewSearchResults.setAdapter(mRVAdapter);
        recyclerViewSearchResults.setLayoutManager(mLayoutManager);


        ((App) getApplication()).apolloClient().newCall(
                TrackQuery.builder()
                        .limit(10)
                        .build())
                .enqueue(
                        new ApolloCall.Callback<TrackQuery.Data>() {
                            @Override
                            public void onResponse(@Nonnull com.apollographql.apollo.api.Response<TrackQuery.Data> response) {
                                if (response.isSuccessful()) {
                                    List<TrackQuery.Data.Track> trackList = response.data().tracks();
                                    for (TrackQuery.Data.Track track : trackList) {
                                        //Mapping api's track model to existing Song model

                                        MySong mSong = new MySong(
                                                track.id(),
                                                track.spotify_track_id(),
                                                track.name(),
                                                (track.albums().size()>0)?track.albums().get(0).name():"Album",
                                                (track.artists().size() > 0) ? track.artists().get(0).name() : "Unknown Artist",
//                                                "Artist",
                                                0,
                                                track.spotify_album_img()
                                        );
                                        mSongs.add(mSong);
                                    }
                                    //Update ui for adding new elements to list
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
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
                                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                );

        ((App) getApplication()).apolloClient().newCall(ArtistQuery.builder().limit(5).build()).enqueue(new ApolloCall.Callback<ArtistQuery.Data>() {
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
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

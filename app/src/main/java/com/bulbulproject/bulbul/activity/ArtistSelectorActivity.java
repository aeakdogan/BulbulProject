package com.bulbulproject.bulbul.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.GenreQuery;
import com.bulbulproject.TopArtistsQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.ArtistSelectorAdapter;
import com.bulbulproject.bulbul.interfaces.AdapterCallbackInterface;
import com.bulbulproject.bulbul.model.Artist;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class ArtistSelectorActivity extends AppCompatActivity {
    private List<Artist> artistList;
    private ArrayList<Integer> mCategoryIds;
    private BaseAdapter mAdapter;
    private GridView mGrid;
    private View mProgressView;
    private Button button_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra("category_ids")) {
            mCategoryIds = getIntent().getIntegerArrayListExtra("category_ids");
        }
        setContentView(R.layout.activity_artist_selector);

        mProgressView = findViewById(R.id.progress);
        mProgressView.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        button_next = (Button) findViewById(R.id.button_next);
        
        artistList = new ArrayList<Artist>();
//        initDummyData();
        fetchArtists(5,0);

        mGrid = (GridView) findViewById(R.id.grid_layout);
        mAdapter = new ArtistSelectorAdapter(artistList, ArtistSelectorActivity.this, new AdapterCallbackInterface() {
            @Override
            public void onSelectedItemCountChanged(int selectedItemCount) {
                if(selectedItemCount < 2)
                    button_next.setEnabled(false);
                else
                    button_next.setEnabled(true);
            }
        });
        mGrid.setAdapter(mAdapter);
        mGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTrackSelectorActivity();
            }
        });

    }

    private void fetchArtists() {
        fetchArtists(5, 0);
    }


    private void fetchArtists(int limit, int skip) {

        ((App) getApplication()).apolloClient().newCall(TopArtistsQuery.builder().genre_ids(mCategoryIds).limit(limit).skip(skip).build()).enqueue(new ApolloCall.Callback<TopArtistsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<TopArtistsQuery.Data> response) {
                if (response.isSuccessful()) {
                    if (response.data().topArtistsQuery() != null) {
                        for (TopArtistsQuery.Data.TopArtistsQuery1 artist : response.data().topArtistsQuery()) {
                            artistList.add(new Artist(artist.id(), artist.name(), artist.image()));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                mProgressView.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                final String text = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ArtistSelectorActivity.this, text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initDummyData() {
        artistList.add(new Artist(1, "Mahmut Tuncer", "http://media.sinematurk.com/person/8/a0/bbde8e5fe9a3/mahooo_1.jpg"));
        artistList.add(new Artist(2, "Mahmut", "http://media.sinematurk.com/person/8/a0/bbde8e5fe9a3/mahooo_1.jpg"));
        artistList.add(new Artist(3, "Tuncer", "http://media.sinematurk.com/person/8/a0/bbde8e5fe9a3/mahooo_1.jpg"));
        artistList.add(new Artist(4, "Maho", "http://media.sinematurk.com/person/8/a0/bbde8e5fe9a3/mahooo_1.jpg"));
        artistList.add(new Artist(5, "Tunci", "http://media.sinematurk.com/person/8/a0/bbde8e5fe9a3/mahooo_1.jpg"));
    }

    private ArrayList<Integer> getSelectedArtistIds() {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (Artist artist : artistList) {
            if (artist.isSelected()) ids.add(artist.getId());
        }
        return ids;
    }

    private void startTrackSelectorActivity() {
        Intent intent = new Intent(ArtistSelectorActivity.this, AccuracyTraining.class);
        intent.putIntegerArrayListExtra("artist_ids", getSelectedArtistIds());
        intent.putIntegerArrayListExtra("category_ids",  mCategoryIds);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ArtistSelectorActivity.this, CategorySelectorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

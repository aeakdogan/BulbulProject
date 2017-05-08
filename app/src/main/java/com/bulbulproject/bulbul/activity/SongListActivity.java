package com.bulbulproject.bulbul.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.SaveAsPlaylistMutation;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.SongsRVAdapter;
import com.bulbulproject.bulbul.service.Globals;

import javax.annotation.Nonnull;

public class SongListActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    SongsRVAdapter rvAdapter;
    View mProgressView;
    Button mSaveAsPlaylistButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if (getIntent().hasExtra("recommendation_id")) {
            final int id = getIntent().getIntExtra("recommendation_id", -1);
            mSaveAsPlaylistButton = (Button) findViewById(R.id.button_save_as_playlist);
            mSaveAsPlaylistButton.setVisibility(View.VISIBLE);
            mSaveAsPlaylistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveAsPlaylist(id);
                }
            });
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        rvAdapter = new SongsRVAdapter(Globals.mSongs, getApplicationContext());
        mRecyclerView.setAdapter(rvAdapter);
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

    private void saveAsPlaylist(int recommendationId) {
        String token = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE).getString("AUTH_TOKEN", "");

        ((App) getApplication()).apolloClient().newCall(
                SaveAsPlaylistMutation.builder()
                        .token(token)
                        .recommendation_id(recommendationId)
                        .build()).enqueue(new ApolloCall.Callback<SaveAsPlaylistMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<SaveAsPlaylistMutation.Data> response) {
                if (response.isSuccessful()) {
                    final String text = response.data().saveAsPlaylist().name() + " created";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SongListActivity.this, text, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                final String text = e.getMessage();
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SongListActivity.this, text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

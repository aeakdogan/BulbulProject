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
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.FollowersQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.UsersRVAdapter;
import com.bulbulproject.bulbul.model.BulbulUser;

import javax.annotation.Nonnull;

public class Followers extends AppCompatActivity {

    private RecyclerView mFollowerList;
    private RecyclerView.Adapter mRVAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar mToolbar;
    private BulbulUser mUser;
    private View mProgressView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        
        mProgressView = findViewById(R.id.progress);
        mProgressView.setVisibility(View.VISIBLE);
        mUser = new BulbulUser("Loading...");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Followers");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mFollowerList = (RecyclerView) findViewById(R.id.followers_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRVAdapter = new UsersRVAdapter(mUser.getFollowers(), this);
        mFollowerList.setAdapter(mRVAdapter);
        mFollowerList.setLayoutManager(mLayoutManager);

        ApolloCall.Callback<FollowersQuery.Data> callback = new ApolloCall.Callback<FollowersQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<FollowersQuery.Data> response) {
                if (response.isSuccessful()) {
                    FollowersQuery.Data.User user = response.data().users().get(0);
                    if (user.followers() != null) {
                        for (FollowersQuery.Data.Follower follower : user.followers()) {
                            BulbulUser newFollower = new BulbulUser(follower.username());
                            newFollower.setProfilePhoto(follower.image());
                            newFollower.setId(follower.id());
                            mUser.getFollowers().add(newFollower);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressView.setVisibility(View.GONE);
                            mRVAdapter.notifyDataSetChanged();
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
                        Toast.makeText(Followers.this, text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };


        if (getIntent().hasExtra("id")) {
            ((App) getApplication()).apolloClient().newCall(FollowersQuery.builder().id(getIntent().getIntExtra("id", 1)).build()).enqueue(callback);
        } else {
            String token = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE).getString("AUTH_TOKEN", "");
            ((App) getApplication()).apolloClient().newCall(FollowersQuery.builder().token(token).build()).enqueue(callback);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Followers.this, MyProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

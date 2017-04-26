package com.bulbulproject.bulbul.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.ProfileQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.model.BulbulUser;
import com.squareup.picasso.Picasso;

import javax.annotation.Nonnull;

public class MyProfile extends AppCompatActivity {
    private BulbulUser mUser;
    private TextView mFollowingsText;
    private TextView mFollowersText;
    private TextView mName;
    private ImageView mProfilePhoto;


    private Button myPlaylists;
    private Button myArtists;
    private Button mySongs;
    private Button myAlbums;
    private Button mLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mUser = new BulbulUser("Loading");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mName = (TextView) findViewById(R.id.name);
        mProfilePhoto = (ImageView) findViewById(R.id.profile_picture);
        mFollowersText = (TextView) findViewById(R.id.follower_count);
        mFollowersText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Followers.class);
                startActivity(intent);
            }
        });

        mFollowingsText = (TextView) findViewById(R.id.following_count);
        mFollowingsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Followings.class);
                startActivity(intent);
            }
        });

        myPlaylists = (Button) findViewById(R.id.my_playlists);
        myPlaylists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyAlbumArtistPlaylistActivity.class);
                intent.putExtra("selected_index", 0);
                startActivity(intent);
            }
        });

        myArtists = (Button) findViewById(R.id.my_artists);
        myArtists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyAlbumArtistPlaylistActivity.class);
                intent.putExtra("selected_index", 1);
                startActivity(intent);
            }
        });

        myAlbums = (Button) findViewById(R.id.my_albums);
        myAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyAlbumArtistPlaylistActivity.class);
                intent.putExtra("selected_index", 2);
                startActivity(intent);
            }
        });

        mySongs = (Button) findViewById(R.id.my_songs);
        mySongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyAlbumArtistPlaylistActivity.class);
                intent.putExtra("selected_index", 3);
                startActivity(intent);
            }
        });

        final SharedPreferences sp = getApplication().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        mLogout = (Button) findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().remove("AUTH_TOKEN").commit();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        String token = sp.getString("AUTH_TOKEN", "");
        ((App) getApplication()).apolloClient().newCall(ProfileQuery.builder().token(token).build()).enqueue(new ApolloCall.Callback<ProfileQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<ProfileQuery.Data> response) {
                if (response.isSuccessful()) {
                    ProfileQuery.Data.User user = response.data().users().get(0);
                    mUser.setUsername(user.username());
                    mUser.setId(user.id());
                    mUser.setProfilePhoto(user.image());
                    mUser.setFollowersCount(user.followersCount());
                    mUser.setFollowingsCount(user.followedUsersCount());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mName.setText(mUser.getUsername());
                            mFollowersText.setText("" + mUser.getFollowersCount() + " Followers");
                            mFollowingsText.setText("" + mUser.getFollowingsCount() + " Following");
                            Picasso.with(MyProfile.this).load(mUser.getProfilePhoto()).into(mProfilePhoto);
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
                        Toast.makeText(MyProfile.this, text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MyProfile.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

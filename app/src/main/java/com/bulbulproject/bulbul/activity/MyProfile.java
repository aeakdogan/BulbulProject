package com.bulbulproject.bulbul.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.activity.Followers;
import com.bulbulproject.bulbul.activity.Followings;

public class MyProfile extends AppCompatActivity {

    TextView followingsText;
    TextView followersText;
    Button myPlaylists;
    Button myArtists;
    Button mySongs;
    Button myAlbums;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        followersText = (TextView) findViewById(R.id.follower_count);
        followersText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Followers.class);
                startActivity(intent);
            }
        });

        followingsText = (TextView) findViewById(R.id.following_count);
        followingsText.setOnClickListener(new View.OnClickListener() {
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
    }

}

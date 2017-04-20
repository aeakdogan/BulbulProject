package com.bulbulproject.bulbul.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.apollographql.android.ApolloCall;
import com.apollographql.android.api.graphql.Response;
import com.bulbulproject.ArtistQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.AlbumsRVAdapter;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Artist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class ArtistActivity extends AppCompatActivity {
    private Artist mArtist;
    private int mArtistId;
    private RecyclerView.Adapter mRVAdapter;
    private ImageView artistImage;
    private RecyclerView listView;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        artistImage = (ImageView) findViewById(R.id.artist_image);
        listView = (RecyclerView) findViewById(R.id.artist_album_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mArtist = new Artist("Loading...");

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("Loading...");
        layoutManager = new LinearLayoutManager(this);
        mRVAdapter = new AlbumsRVAdapter(mArtist.getAlbums(), this);
        listView.setAdapter(mRVAdapter);
        listView.setLayoutManager(layoutManager);

        mArtistId = getIntent().getIntExtra("id", 9410);
        if (mArtistId > -1) {

            ((App) getApplication()).apolloClient().newCall(ArtistQuery.builder().id(mArtistId).build())
                    .enqueue(new ApolloCall.Callback<ArtistQuery.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<ArtistQuery.Data> response) {
                            if (response.isSuccessful()) {
                                final ArtistQuery.Data.Artist artist = response.data().artists().get(0);
                                mArtist.setName(artist.name());
                                mArtist.setImageUrl(artist.image());

                                if (artist.albums() != null) {
                                    for (ArtistQuery.Data.Artist.Album album : artist.albums()) {
                                        Album newAlbum = new Album(album.name(), 0, album.image());
                                        newAlbum.setId(album.id());
                                        newAlbum.setSongsCount(album.tracksCount());
                                        if(album.artists() != null){
                                            for(ArtistQuery.Data.Artist.Album.Artist1 albumArtist: album.artists()){
                                                newAlbum.getArtists().add(new Artist(albumArtist.name()));
                                            }
                                        }
                                        mArtist.getAlbums().add(newAlbum);
                                    }
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Picasso.with(ArtistActivity.this).load(mArtist.getImageUrl()).into(artistImage);
                                        collapsingToolbarLayout.setTitle(mArtist.getName());
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
                                    Toast.makeText(ArtistActivity.this, text, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
            collapsingToolbarLayout.setTitle(mArtist.getName());
        }
        else {
            Toast.makeText(ArtistActivity.this, R.string.album_activity_fetch_error, Toast.LENGTH_SHORT).show();
        }
    }

}

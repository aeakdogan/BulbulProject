package com.bulbulproject.bulbul.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.SearchQuery;
import com.bulbulproject.UserPlaylistsQuery;
import com.bulbulproject.UserSummaryQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.activity.AccuracyTraining;
import com.bulbulproject.bulbul.activity.CategorySelectorActivity;
import com.bulbulproject.bulbul.activity.MoodActivity;
import com.bulbulproject.bulbul.activity.SearchActivity;
import com.bulbulproject.bulbul.adapter.SearchResultRVAdapter;
import com.bulbulproject.bulbul.adapter.UserSummaryRVAdapter;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.Playlist;
import com.bulbulproject.bulbul.model.Song;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by mesutgurlek on 2/12/17.
 */

public class HomeFragment extends Fragment {
    ArrayList<Song> mSongs;
    ArrayList<Artist> mArtists;
    ArrayList<Album> mAlbums;
    ArrayList<Playlist> mPlaylists;
    private UserSummaryRVAdapter mRVAdapter;
    private RecyclerView recyclerViewUserSummary;
    private LinearLayoutManager mLayoutManager;
    private String token;

    public HomeFragment(){
        mSongs = new ArrayList<>();
        mArtists = new ArrayList<>();
        mAlbums = new ArrayList<>();
        mPlaylists = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        rootView.findViewById(R.id.test_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategorySelectorActivity.class);
                startActivity(intent);
            }
        });

        recyclerViewUserSummary = (RecyclerView) rootView.findViewById(R.id.rv_user_summary);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRVAdapter = new UserSummaryRVAdapter(getActivity(), mSongs, mArtists, mAlbums, mPlaylists);
        recyclerViewUserSummary.setAdapter(mRVAdapter);
        recyclerViewUserSummary.setLayoutManager(mLayoutManager);

        token = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE).getString("AUTH_TOKEN", "");


        ((App) getActivity().getApplication()).apolloClient().newCall(UserSummaryQuery.builder().token(token).limit(2).build()).enqueue(new ApolloCall.Callback<UserSummaryQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<UserSummaryQuery.Data> response) {
                if (response.isSuccessful()) {
                    mSongs.clear();
                    mAlbums.clear();
                    mArtists.clear();
                    mPlaylists.clear();

                    List<UserSummaryQuery.Data.ListenedTrack> trackList = response.data().users().get(0).listenedTracks();
                    for (UserSummaryQuery.Data.ListenedTrack track : trackList) {

                        Song mSong = new Song(
                                track.id(),
                                track.name(),
                                track.spotify_track_id(),
                                0,
                                track.spotify_album_img()
                        );
                        if (track.artists() != null) {
                            for (UserSummaryQuery.Data.Artist1 artist : track.artists()) {
                                mSong.getArtists().add(new Artist(artist.id(), artist.name(), artist.image()));
                            }
                        }

                        if (track.albums() != null) {
                            for (UserSummaryQuery.Data.Album album : track.albums()) {
                                mSong.getAlbums().add(new Album(album.name(), album.image()));
                            }
                        }
                        mSongs.add(mSong);
                    }

                    for (UserSummaryQuery.Data.ListenedArtist artist : response.data().users().get(0).listenedArtists()) {
                        Artist mArtist = new Artist(artist.id(), artist.name(), artist.image());
                        mArtist.setAlbumsCount(artist.albumsCount());
                        mArtists.add(mArtist);
                    }

                    for (UserSummaryQuery.Data.ListenedAlbum album : response.data().users().get(0).listenedAlbums()) {
                        Album mAlbum;
                        mAlbum = new Album("Loading...", 0, "");
                        mAlbum.setId(album.id());
                        mAlbum.setName(album.name());
                        mAlbum.setImageUrl(album.image());
                        mAlbum.setSongsCount(album.tracksCount());

                        if (album.artists() != null) {
                            for (UserSummaryQuery.Data.Artist artist : album.artists()) {
                                mAlbum.getArtists().add(new Artist(artist.name()));
                            }
                        }
                        mAlbums.add(mAlbum);
                    }

                    for (UserSummaryQuery.Data.Playlist playlist : response.data().users().get(0).playlists()) {
                        Playlist newPlaylist = new Playlist(playlist.name(), playlist.id());
                        newPlaylist.setSongsCount(playlist.tracksCount());
                        mPlaylists.add(newPlaylist);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            updateSearchResult();
                            mRVAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                final String text = e.getMessage();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return rootView;
    }
}

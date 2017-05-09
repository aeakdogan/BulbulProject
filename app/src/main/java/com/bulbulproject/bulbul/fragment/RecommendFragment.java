package com.bulbulproject.bulbul.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.GenreQuery;
import com.bulbulproject.RecommendationsQuery;
import com.bulbulproject.RequestPersonalRecommendationMutation;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.activity.SongListActivity;
import com.bulbulproject.bulbul.adapter.CategorySelectorAdapter;
import com.bulbulproject.bulbul.interfaces.AdapterCallbackInterface;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.Category;
import com.bulbulproject.bulbul.model.Song;
import com.bulbulproject.bulbul.service.Globals;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by mesutgurlek on 2/12/17.
 */

public class RecommendFragment extends Fragment {
    GridView grid;
    Button requestButton;
    View mProgressView;
    List<Category> categories = new ArrayList<Category>();
    Handler mHandler;
    Runnable fetcher;
    CategorySelectorAdapter gridAdapter;
    ApolloCall<RequestPersonalRecommendationMutation.Data> recommendationCall;

    public RecommendFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        Globals.mSongs = new ArrayList<Song>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recommend, container, false);
        grid = (GridView) rootView.findViewById(R.id.grid);
        requestButton = (Button) rootView.findViewById(R.id.request_button);
        mProgressView = rootView.findViewById(R.id.progress);


        gridAdapter = new CategorySelectorAdapter(categories, rootView.getContext(), new AdapterCallbackInterface() {
            @Override
            public void onSelectedItemCountChanged(int selectedItemCount) {
                if (selectedItemCount < 2)
                    requestButton.setEnabled(false);
                else
                    requestButton.setEnabled(true);
            }
        });

        grid.setAdapter(gridAdapter);

        if (categories.size() == 0) {
            ((App) getActivity().getApplication()).apolloClient().newCall(
                    GenreQuery.builder()
                            .build())
                    .enqueue(
                            new ApolloCall.Callback<GenreQuery.Data>() {
                                @Override
                                public void onResponse(@Nonnull Response<GenreQuery.Data> response) {
                                    if (response.isSuccessful()) {
                                        categories.clear();
                                        List<GenreQuery.Data.Genre> genreList = response.data().genres();
                                        for (GenreQuery.Data.Genre genre : genreList) {
                                            categories.add(new Category(genre.name(), genre.icon_url(), genre.id()));
                                        }

                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                gridAdapter.notifyDataSetChanged();
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
        }


        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Snackbar.make(view, "You Clicked at " + categories.get(position), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grid.setVisibility(View.GONE);
                requestButton.setVisibility(View.GONE);
                mProgressView.setVisibility(View.VISIBLE);
                requestRecommendation();
            }
        });

        return rootView;
    }

    private ArrayList<Integer> getSelectedCategoryIds() {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (Category category : categories) {
            if (category.isSelected()) ids.add(category.getId());
        }
        return ids;
    }

    @Override
    public void onPause() {
        if (recommendationCall != null) {
            recommendationCall.cancel();
        }
        if(mHandler != null){
            mHandler.removeCallbacks(fetcher);
        }

        mProgressView.setVisibility(View.GONE);
        requestButton.setVisibility(View.VISIBLE);
        requestButton.setEnabled(false);
        grid.setVisibility(View.VISIBLE);
        if(categories != null)
        for (Category category : categories)
            category.setSelected(false);
        gridAdapter.notifyDataSetChanged();

        super.onPause();
    }

    private void requestRecommendation() {
        final SharedPreferences sp = getActivity().getApplication().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        if (recommendationCall != null) {
            recommendationCall.cancel();
        }
        recommendationCall = ((App) getActivity().getApplication()).apolloClient().newCall(RequestPersonalRecommendationMutation
                .builder()
                .token(sp.getString("AUTH_TOKEN", ""))
                .genre_ids(getSelectedCategoryIds())
                .build());
        recommendationCall.enqueue(new ApolloCall.Callback<RequestPersonalRecommendationMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<RequestPersonalRecommendationMutation.Data> response) {
                if(response.isSuccessful()) {
                    int id = response.data().requestPersonalRecommendation().id();
                    fetchRecommendation(id);
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                final String text = e.getMessage();
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void fetchRecommendation(final int id) {

        if (fetcher != null) {
            mHandler.removeCallbacks(fetcher);
        }
        ((App) getActivity().getApplication()).apolloClient().newCall(RecommendationsQuery.builder().id(id).build()).
                enqueue(new ApolloCall.Callback<RecommendationsQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<RecommendationsQuery.Data> response) {
                        if (response.isSuccessful() && response.data().recommendations() != null && response.data().recommendations().get(0).status().equals("READY")) {
                            RecommendationsQuery.Data.Recommendation recommendation = response.data().recommendations().get(0);
                            if (recommendation.tracks() != null) {
                                Globals.mSongs = new ArrayList<Song>();

                                List<RecommendationsQuery.Data.Track> tracks = recommendation.tracks();
                                for (RecommendationsQuery.Data.Track track : tracks) {
                                    Song mSong = new Song(track.id(),
                                            track.name(),
                                            track.spotify_track_id(),
                                            0,
                                            track.spotify_album_img()

                                    );
                                    if (track.artists() != null) {
                                        for (RecommendationsQuery.Data.Artist artist : track.artists()) {
                                            mSong.getArtists().add(new Artist(artist.name()));
                                        }
                                    }

                                    if (track.albums() != null) {
                                        for (RecommendationsQuery.Data.Album album : track.albums()) {
                                            mSong.getAlbums().add(new Album(album.name(), album.image()));
                                        }
                                    }
                                    Globals.mSongs.add(mSong);
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressView.setVisibility(View.GONE);
                                        requestButton.setVisibility(View.VISIBLE);
                                        requestButton.setEnabled(false);
                                        grid.setVisibility(View.VISIBLE);
                                        for (Category category : categories)
                                            category.setSelected(false);
                                        gridAdapter.notifyDataSetChanged();

                                        Intent intent = new Intent(getActivity(), SongListActivity.class);
                                        intent.putExtra("recommendation_id", id);
                                        startActivity(intent);

                                    }
                                });
                            }
                        } else {
                            if (fetcher == null) {
                                fetcher = new Runnable() {
                                    @Override
                                    public void run() {
                                        fetchRecommendation(id);
                                    }
                                };
                            }
                            mHandler.postDelayed(fetcher, 2500);
                        }
                    }


                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        final String text = e.getMessage();
                        e.printStackTrace();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


    }


}

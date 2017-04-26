package com.bulbulproject.bulbul.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.TrackQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.activity.StreamActivity;
import com.bulbulproject.bulbul.adapter.DiscoverListAdapter;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.model.Song;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by mesutgurlek on 2/12/17.
 */

public class DiscoverFragment extends Fragment {


    // Array of strings for ListView Title
    List<Song> songList;
    private LocalBroadcastManager mBroadcastManager;
    private View mProgressView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {


        mBroadcastManager = LocalBroadcastManager.getInstance(getActivity().getApplicationContext());
        songList = new ArrayList<Song>();
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);

        final DiscoverListAdapter adapter = new DiscoverListAdapter(songList, getActivity().getBaseContext());
        final ListView androidListView = (ListView) rootView.findViewById(R.id.list_view_discover);
        androidListView.setAdapter(adapter);
        final ArrayList<String> songsList = new ArrayList<String>();
        mProgressView = rootView.findViewById(R.id.progress);
        mProgressView.setVisibility(View.VISIBLE);

        //Fetch data and update ui
        ((App) getActivity().getApplication()).apolloClient().newCall(
                TrackQuery.builder()
                        .limit(10)
                        .build())
                .enqueue(
                        new ApolloCall.Callback<TrackQuery.Data>() {
                            @Override
                            public void onResponse(@Nonnull Response<TrackQuery.Data> response) {
                                if (response.data() != null) {
                                    List<TrackQuery.Data.Track> trackList = response.data().tracks();
                                    for (TrackQuery.Data.Track track : trackList) {
                                        //Mapping api's track model to existing Song model
                                        songList.add(new Song(track.id(),
                                                track.name(),
                                                R.drawable.cover_picture,
                                                0,
                                                track.spotify_track_id()
                                        ));
                                    }
                                    for (Song song : songList) {
                                        songsList.add(song.getSpotifyUrl());
                                    }
                                    androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                            Intent intent = new Intent(getActivity().getApplicationContext(), StreamActivity.class);
                                            intent.putExtra("position", position);
                                            intent.putStringArrayListExtra("songs", songsList);
                                            startActivity(intent);
                                        }
                                    });
                                    //Update ui for adding new elements to list
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mProgressView.setVisibility(View.GONE);
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(@Nonnull ApolloException e) {
                                e.printStackTrace();
                                final String text = e.getMessage();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

        return rootView;
    }
}

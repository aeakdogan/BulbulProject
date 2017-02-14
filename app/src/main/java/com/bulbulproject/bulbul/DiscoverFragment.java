package com.bulbulproject.bulbul;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mesutgurlek on 2/12/17.
 */

public class DiscoverFragment extends Fragment {


    // Array of strings for ListView Title
    List<Song> songList;

    public DiscoverFragment(){
        songList = new ArrayList<Song>();

        songList.add(new Song(1, "Song 1", "Artist 1", R.drawable.cover_picture, 0));
        songList.add(new Song(2, "Song 2", "Artist 2", R.drawable.cover_picture, 2));
        songList.add(new Song(3, "Song 3", "Artist 3", R.drawable.cover_picture, 1));
        songList.add(new Song(4, "Song 4", "Artist 4", R.drawable.cover_picture, 3.4f));
        songList.add(new Song(5, "Song 5", "Artist 5", R.drawable.cover_picture, 4.6f));
        songList.add(new Song(6, "Song 6", "Artist 6", R.drawable.cover_picture, 2.7f));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);

        DiscoverListAdapter adapter = new DiscoverListAdapter(songList, getActivity().getBaseContext());
        ListView androidListView = (ListView) rootView.findViewById(R.id.list_view_discover);
        androidListView.setAdapter(adapter);
        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Song song = songList.get(position);

                Snackbar.make(view, "Song is " + song.getName() + " and " + song.getRating() + " stars ", Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });


        return rootView;
    }
}

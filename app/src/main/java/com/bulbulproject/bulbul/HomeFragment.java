package com.bulbulproject.bulbul;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mesutgurlek on 2/12/17.
 */

public class HomeFragment extends Fragment {
    List<Song> songList;

    public HomeFragment(){
        songList = new ArrayList<>();

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

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(rootView.getContext());
        rv.setLayoutManager(llm);

        RVAdapter adapter = new RVAdapter(songList);
        rv.setAdapter(adapter);

        return rootView;
    }

    class RVAdapter extends RecyclerView.Adapter<RVAdapter.SongsViewHolder>{
        List<Song> songs;

        RVAdapter(List<Song> songs){
            this.songs = songs;
        }

        @Override
        public SongsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_home, parent, false);
            SongsViewHolder pvh = new SongsViewHolder(v);

            return pvh;
        }

        @Override
        public void onBindViewHolder(SongsViewHolder holder, final int position) {
            holder.title.setText(songs.get(position).getName());
            holder.description.setText(songs.get(position).getArtist());
            holder.photo.setImageResource(songs.get(position).getImageId());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Song song = songList.get(position);

                    Snackbar.make(view, "Song is " + song.getName() + " song id: " + song.getId(), Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return songs.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class SongsViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView title;
            TextView description;
            ImageView photo;

            SongsViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.cv);
                title = (TextView)itemView.findViewById(R.id.home_card_title);
                description = (TextView)itemView.findViewById(R.id.home_card_description);
                photo = (ImageView)itemView.findViewById(R.id.home_album_photo);
            }
        }

    }
}


package com.bulbulproject.bulbul.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.activity.StreamActivity;
import com.bulbulproject.bulbul.model.Song;
import com.bulbulproject.bulbul.service.Globals;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SongsRVAdapter extends RecyclerView.Adapter<SongsRVAdapter.MyCardViewHolder>{
    private List<Song> mSongs;
    private Context context;

    public SongsRVAdapter(List<Song> mSongs, Context context){
        this.mSongs = mSongs;
        this.context = context;
    }

    public static class MyCardViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView songTitle;
        public TextView songArtists;
        public ImageView songPhoto;

        public MyCardViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cv);
            songTitle = (TextView) mCardView.findViewById(R.id.song_title);
            songArtists = (TextView) mCardView.findViewById(R.id.song_artists);
            songPhoto = (ImageView) mCardView.findViewById(R.id.song_photo);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public SongsRVAdapter.MyCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        CardView cv = (CardView) LayoutInflater.from(context)
                .inflate(R.layout.cv_song, parent, false);
        MyCardViewHolder cvh = new MyCardViewHolder(cv);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final SongsRVAdapter.MyCardViewHolder holder, int position) {
        final Song tmpSong = mSongs.get(position);
        final ArrayList<String> songsList = new ArrayList<String>();
        final ArrayList<Integer> songIdsList = new ArrayList<>();
        for (Song song : mSongs) {
            songsList.add(song.getSpotifyUrl());
            songIdsList.add(song.getId());
        }

        holder.songTitle.setText(tmpSong.getName());
        holder.songArtists.setText(tmpSong.getFirstArtistName());
        Picasso.with(context)
                .load(tmpSong.getImageUrl())
                .fit()
                .placeholder(R.drawable.cover_picture)
                .error(R.drawable.album)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(holder.songPhoto);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), StreamActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putIntegerArrayListExtra("songIds", songIdsList);
                intent.putStringArrayListExtra("songs", songsList);
                intent.putExtra("position", holder.getAdapterPosition());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mSongs.size();
    }
}

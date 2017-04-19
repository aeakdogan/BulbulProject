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
import com.bulbulproject.bulbul.activity.AlbumActivity;
import com.bulbulproject.bulbul.activity.StreamActivity;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Song;

import java.util.List;

public class SongsRVAdapter extends RecyclerView.Adapter<SongsRVAdapter.MyCardViewHolder>{
    private List<Song> songs;
    private Context context;

    public SongsRVAdapter(List<Song> songs, Context context){
        this.songs = songs;
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
    public void onBindViewHolder(SongsRVAdapter.MyCardViewHolder holder, final int position) {
        final Song tmpSong = songs.get(position);
        holder.songTitle.setText(tmpSong.getName());
        holder.songArtists.setText(tmpSong.getArtistsString());
        holder.songPhoto.setImageResource(tmpSong.getPhotoId());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), StreamActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("song_uri", tmpSong.getSpotifyUrl());
                context.getApplicationContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return songs.size();
    }
}

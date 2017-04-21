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
import android.widget.Toast;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.activity.PlaylistActivity;
import com.bulbulproject.bulbul.model.Playlist;

import java.util.List;

/**
 * Created by burak on 10.02.2017.
 */
public class PlaylistsRVAdapter extends RecyclerView.Adapter<PlaylistsRVAdapter.MyCardViewHolder>{
    private List<Playlist> playlists;
    private Context context;

    public PlaylistsRVAdapter(List<Playlist> playlists, Context context){
        this.playlists = playlists;
        this.context = context;
    }

    public static class MyCardViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView playlistName;
        public TextView playlistMeta;
        public ImageView playlistPhoto;

        public MyCardViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cv_playlist);
            playlistName = (TextView) mCardView.findViewById(R.id.playlist_name);
            playlistMeta = (TextView) mCardView.findViewById(R.id.playlist_meta);
            playlistPhoto = (ImageView) mCardView.findViewById(R.id.playlist_photo);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PlaylistsRVAdapter.MyCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.cv_playlist, parent, false);

        MyCardViewHolder cvh = new MyCardViewHolder(cv);
        return cvh;
    }

    @Override
    public void onBindViewHolder(PlaylistsRVAdapter.MyCardViewHolder holder, final int position) {
        holder.playlistName.setText(playlists.get(position).getName());
        holder.playlistMeta.setText("" + playlists.get(position).getSongs().size() + " songs");
        holder.playlistPhoto.setImageResource(playlists.get(position).getPhotoId());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), PlaylistActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", playlists.get(position).getId());
                context.getApplicationContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return playlists.size();
    }
}

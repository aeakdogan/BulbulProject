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
import com.bulbulproject.bulbul.activity.AlbumActivity;
import com.bulbulproject.bulbul.model.Album;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by burak on 10.02.2017.
 */
public class AlbumsRVAdapter extends RecyclerView.Adapter<AlbumsRVAdapter.MyCardViewHolder>{
    private List<Album> albums;
    private Context context;

    public AlbumsRVAdapter(List<Album> albums, Context context){
        this.albums = albums;
        this.context = context;
    }

    public static class MyCardViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView albumName;
        public TextView albumMeta;
        public ImageView albumPhoto;

        public MyCardViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cv);
            albumName = (TextView) mCardView.findViewById(R.id.album_name);
            albumMeta = (TextView) mCardView.findViewById(R.id.album_meta);
            albumPhoto = (ImageView) mCardView.findViewById(R.id.album_photo);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public AlbumsRVAdapter.MyCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.cv_album, parent, false);
        MyCardViewHolder cvh = new MyCardViewHolder(cv);
        return cvh;
    }

    @Override
    public void onBindViewHolder(AlbumsRVAdapter.MyCardViewHolder holder, final int position) {
        final Album tmpAlbum = albums.get(position);
        holder.albumName.setText(tmpAlbum.getName());
        String albumMeta = tmpAlbum.getArtistsString() + "\n" +
                            tmpAlbum.getSongsCount() + " songs" ;
        holder.albumMeta.setText(albumMeta);
        Picasso.with(context).load(tmpAlbum.getImageUrl()).into(holder.albumPhoto);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), AlbumActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",tmpAlbum.getId());
                context.getApplicationContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return albums.size();
    }
}

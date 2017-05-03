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
import com.bulbulproject.bulbul.activity.ArtistActivity;
import com.bulbulproject.bulbul.model.Artist;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by burak on 10.02.2017.
 */
public class ArtistsRVAdapter extends RecyclerView.Adapter<ArtistsRVAdapter.MyCardViewHolder> {
    private List<Artist> artists;
    private Context context;

    public ArtistsRVAdapter(List<Artist> artists, Context context) {
        this.artists = artists;
        this.context = context;
    }

    public static class MyCardViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView artistName;
        public TextView artistMeta;
        public ImageView artistPhoto;

        public MyCardViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cv);
            artistName = (TextView) mCardView.findViewById(R.id.artist_name);
            artistMeta = (TextView) mCardView.findViewById(R.id.artist_meta);
            artistPhoto = (ImageView) mCardView.findViewById(R.id.artist_photo);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ArtistsRVAdapter.MyCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cv_artist, parent, false);

        MyCardViewHolder cvh = new MyCardViewHolder(cv);
        return cvh;
    }

    @Override
    public void onBindViewHolder(MyCardViewHolder holder, final int position) {
        Artist artist = artists.get(position);
        holder.artistName.setText(artist.getName());
        holder.artistMeta.setText("" + artist.getAlbumsCount() + " albums");
        if (artist.getImageUrl() != null && artist.getImageUrl().length() > 0) {
            Picasso.with(context).load(artist.getImageUrl())
                    .placeholder(R.drawable.artist)
                    .error(R.drawable.artist)
                    .into(holder.artistPhoto);
        }
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), ArtistActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", artists.get(position).getId());
                context.getApplicationContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return artists.size();
    }
}

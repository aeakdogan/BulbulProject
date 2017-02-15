package com.bulbulproject.bulbul.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.model.Artist;

import java.util.List;

/**
 * Created by burak on 10.02.2017.
 */
public class ArtistsRVAdapter extends RecyclerView.Adapter<ArtistsRVAdapter.MyCardViewHolder>{
    private List<Artist> artists;
    private Context context;

    public ArtistsRVAdapter(List<Artist> artists, Context context){
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
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.cv_artist, parent, false);

        MyCardViewHolder cvh = new MyCardViewHolder(cv);
        return cvh;
    }

    @Override
    public void onBindViewHolder(MyCardViewHolder holder, final int position) {
        holder.artistName.setText(artists.get(position).getName());
        holder.artistMeta.setText("" + artists.get(position).getAlbums().size() + " albums");
        holder.artistPhoto.setImageResource(artists.get(position).getPhotoId());;
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You clicked on " +
                                artists.get(position).getName(),
                                    Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return artists.size();
    }
}

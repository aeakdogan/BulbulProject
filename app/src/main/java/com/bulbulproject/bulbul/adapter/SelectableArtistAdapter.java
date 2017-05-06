package com.bulbulproject.bulbul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by fatih on 05/05/2017.
 */

public class SelectableArtistAdapter extends BaseAdapter {
    private List<Artist> artistList;
    private Context mContext;

    public SelectableArtistAdapter(List<Artist> artistList, Context context) {
        this.artistList = artistList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return artistList.size();
    }

    @Override
    public Object getItem(int position) {
        return artistList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((Category) getItem(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView i;
        TextView t;
        View v;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Artist artist = artistList.get(position);
        if (convertView == null) {
            v = inflater.inflate(R.layout.grid_single, null);
            i = (ImageView) v.findViewById(R.id.grid_image);
            t = (TextView) v.findViewById(R.id.grid_text);

            t.setText(artist.getName());
            Picasso.with(mContext).load(artist.getImageUrl()).placeholder(R.drawable.image4).into(i);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (artist.isSelected()) {
                        v.findViewById(R.id.grid_selected).setVisibility(View.INVISIBLE);
                        artist.setSelected(false);
                    } else {
                        v.findViewById(R.id.grid_selected).setVisibility(View.VISIBLE);
                        artist.setSelected(true);

                    }
                }
            });
        } else {
            v = convertView;
        }

        return v;
    }

}

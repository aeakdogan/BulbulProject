package com.bulbulproject.bulbul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.interfaces.AdapterCallbackInterface;
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
    AdapterCallbackInterface adapterCallbackInterface;
    int selectedCount;

    public SelectableArtistAdapter(List<Artist> artistList, Context context, AdapterCallbackInterface adapterCallbackInterface) {
        this.artistList = artistList;
        this.mContext = context;
        this.adapterCallbackInterface = adapterCallbackInterface;
        selectedCount = 0;
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
        return ((Artist) getItem(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView i;
        ImageView c;
        TextView t;
        View v;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Artist artist = artistList.get(position);
        if (convertView == null) {
            v = inflater.inflate(R.layout.grid_single, null);
        } else {
            v = convertView;
        }
        i = (ImageView) v.findViewById(R.id.grid_image);
        t = (TextView) v.findViewById(R.id.grid_text);
        c = (ImageView) v.findViewById(R.id.grid_selected);
        if(artist.isSelected()){
            c.setVisibility(View.VISIBLE);
        }else{
            c.setVisibility(View.INVISIBLE);
        }

        t.setText(artist.getName());
        Picasso.with(mContext).load(artist.getImageUrl()).placeholder(R.drawable.artist).into(i);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (artist.isSelected()) {
                    selectedCount--;
                    v.findViewById(R.id.grid_selected).setVisibility(View.INVISIBLE);
                    artist.setSelected(false);
                } else {
                    selectedCount++;
                    v.findViewById(R.id.grid_selected).setVisibility(View.VISIBLE);
                    artist.setSelected(true);
                }
                adapterCallbackInterface.onSelectedItemCountChanged(selectedCount);
            }
        });


        return v;
    }

}

package com.bulbulproject.bulbul;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mesutgurlek on 2/13/17.
 */

public class DiscoverListAdapter extends ArrayAdapter<Song> {

    private List<Song> songList;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView songName;
        TextView songArtist;
        ImageView albumImage;
        RatingBar ratingBar;
    }

    public DiscoverListAdapter(List<Song> data, Context context) {
        super(context, R.layout.discover_listview_item, data);
        this.songList = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Song song = (Song) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.discover_listview_item, parent, false);
            viewHolder.songName = (TextView) convertView.findViewById(R.id.listview_item_title);
            viewHolder.songArtist = (TextView) convertView.findViewById(R.id.listview_item_artist);
            viewHolder.albumImage = (ImageView) convertView.findViewById(R.id.listview_image);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.simpleRatingBar);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        lastPosition = position;

        viewHolder.songName.setText(song.getName());
        viewHolder.songArtist.setText(song.getArtist());
        viewHolder.albumImage.setImageResource(song.getImageId());
        viewHolder.ratingBar.setRating(song.getRating());

        viewHolder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                songList.get(position).setRating(v);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}

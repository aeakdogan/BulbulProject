package com.bulbulproject.bulbul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.interfaces.AdapterCallbackInterface;
import com.bulbulproject.bulbul.model.Category;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by fatih on 05/05/2017.
 */

public class CategoryAdapter extends BaseAdapter {
    private List<Category> categoryList;
    private Context mContext;

    public CategoryAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((Category) getItem(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView i;
        ImageView c;
        TextView t;
        View v;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Category category = categoryList.get(position);
        if (convertView == null) {
            v = inflater.inflate(R.layout.grid_single, null);
        } else {
            v = convertView;
        }
        i = (ImageView) v.findViewById(R.id.grid_image);
        t = (TextView) v.findViewById(R.id.grid_text);
        c = (ImageView) v.findViewById(R.id.grid_selected);
        if(category.isSelected()){
            c.setVisibility(View.VISIBLE);
        }else{
            c.setVisibility(View.INVISIBLE);
        }
        t.setText(category.getName());
        if (category.getImageUrl().length() > 0) {
            Picasso.with(mContext)
                    .load(category.getImageUrl())
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .placeholder(R.drawable.cover_picture)
                    .fit()
                    .into(i);
        }
        return v;
    }


}

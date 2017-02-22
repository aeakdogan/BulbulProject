package com.bulbulproject.bulbul.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.CustomGrid;

/**
 * Created by mesutgurlek on 2/12/17.
 */

public class RecommendFragment extends Fragment{
    GridView grid;
    String[] elements = {
            "Meal",
            "Study",
            "Sports",
            "Nature",
            "Fire",
            "Camping",
            "Meal",
            "Study",
            "Sports",
            "Nature",
            "Fire",
            "Camping",
            "Meal",
            "Study",
            "Sports",
            "Nature",
            "Fire",
            "Camping",
    } ;
    int[] imageId = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
    };
    public RecommendFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recommend, container, false);
        CustomGrid adapter = new CustomGrid(rootView.getContext(), elements, imageId);
        grid=(GridView) rootView.findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Snackbar.make(view, "You Clicked at " + elements[+ position], Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                Intent intent = new Intent(getContext(), com.bulbulproject.bulbul.activity.MoodsGenresActivity.class);
                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(intent);
            }
        });

        return rootView;
    }
}

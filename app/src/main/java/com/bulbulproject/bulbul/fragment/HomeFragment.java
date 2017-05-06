package com.bulbulproject.bulbul.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.activity.AccuracyTraining;
import com.bulbulproject.bulbul.activity.CategorySelectorActivity;
import com.bulbulproject.bulbul.activity.MoodActivity;
import com.bulbulproject.bulbul.activity.SearchActivity;

/**
 * Created by mesutgurlek on 2/12/17.
 */

public class HomeFragment extends Fragment {
    public HomeFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        rootView.findViewById(R.id.test_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategorySelectorActivity.class);
                startActivity(intent);
            }
        });

        rootView.findViewById(R.id.mood_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MoodActivity.class);
                startActivity(intent);
            }
        });

        rootView.findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}

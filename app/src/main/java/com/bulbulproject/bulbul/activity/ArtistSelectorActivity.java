package com.bulbulproject.bulbul.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.CategoryAdapter;
import com.bulbulproject.bulbul.adapter.SelectableArtistAdapter;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.Category;

import java.util.ArrayList;
import java.util.List;

public class ArtistSelectorActivity extends AppCompatActivity {
    List<Artist> artistList;
    GridView mGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        artistList = new ArrayList<Artist>();
        initDummyData();

        mGrid = (GridView) findViewById(R.id.grid_layout);
        mGrid.setAdapter(new SelectableArtistAdapter(artistList, ArtistSelectorActivity.this));
        mGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        ((Button)findViewById(R.id.button_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTrackSelectorActivity();
            }
        });

    }

    private void initDummyData(){
        artistList.add(new Artist("Mahmut Tuncer" , "http://media.sinematurk.com/person/8/a0/bbde8e5fe9a3/mahooo_1.jpg"));
        artistList.add(new Artist("Mahmut" , "http://media.sinematurk.com/person/8/a0/bbde8e5fe9a3/mahooo_1.jpg"));
        artistList.add(new Artist("Tuncer" , "http://media.sinematurk.com/person/8/a0/bbde8e5fe9a3/mahooo_1.jpg"));
        artistList.add(new Artist("Maho" , "http://media.sinematurk.com/person/8/a0/bbde8e5fe9a3/mahooo_1.jpg"));
        artistList.add(new Artist("Tunci" , "http://media.sinematurk.com/person/8/a0/bbde8e5fe9a3/mahooo_1.jpg"));
    }

    private List<Integer> getSelectedArtistIds(){
        List<Integer> ids = new ArrayList<Integer>();
        for(Artist artist: artistList){
            if(artist.isSelected()) ids.add(artist.getId());
        }
        return ids;
    }

    private void startTrackSelectorActivity(){
        Intent intent = new Intent(ArtistSelectorActivity.this, MainActivity.class);

        startActivity(intent);
    }
}

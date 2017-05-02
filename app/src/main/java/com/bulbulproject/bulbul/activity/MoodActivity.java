package com.bulbulproject.bulbul.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import com.bulbulproject.bulbul.R;



public class MoodActivity extends AppCompatActivity {

    SeekBar seekBarAcousticness;
    SeekBar seekBarLiveness;
    SeekBar seekBarSpeechiness;
    SeekBar seekBarValence;
    SeekBar seekBarDanceability;
    SeekBar seekBarInstrumentalness;
    SeekBar seekBarTempo;
    SeekBar seekBarEnergy;
    SeekBar seekBarLoudness;


    View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        seekBarAcousticness = (SeekBar) findViewById(R.id.seekBar_acousticness);
        seekBarLiveness = (SeekBar) findViewById(R.id.seekBar_liveness);
        seekBarSpeechiness = (SeekBar) findViewById(R.id.seekBar_speechiness);
        seekBarValence = (SeekBar) findViewById(R.id.seekBar_valence);
        seekBarDanceability = (SeekBar) findViewById(R.id.seekBar_danceability);
        seekBarInstrumentalness = (SeekBar) findViewById(R.id.seekBar_instrumentalness);
        seekBarTempo = (SeekBar) findViewById(R.id.seekBar_tempo);
        seekBarEnergy = (SeekBar) findViewById(R.id.seekBar_energy);
        seekBarLoudness = (SeekBar) findViewById(R.id.seekBar_loudness);


        findViewById(R.id.button_get_songs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_songs();
            }
        });
    }
    void get_songs(){
        Intent intent = new Intent(getApplicationContext(), MoodActivityResult.class);
        intent.putExtra("acousticness", seekBarAcousticness.getProgress());
        intent.putExtra("liveness", seekBarLiveness.getProgress());
        intent.putExtra("speechiness", seekBarSpeechiness.getProgress());
        intent.putExtra("valence", seekBarValence.getProgress());
        intent.putExtra("danceability", seekBarDanceability.getProgress());
        intent.putExtra("instrumentalness", seekBarInstrumentalness.getProgress());
        intent.putExtra("tempo", seekBarTempo.getProgress());
        intent.putExtra("energy", seekBarEnergy.getProgress());
        intent.putExtra("loudness", seekBarLoudness.getProgress());
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MoodActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*
            audio_features_acousticness
            audio_features_liveness
            audio_features_speechiness
            audio_features_valence
            audio_features_danceability
            audio_features_instrumentalness
            audio_features_tempo
            audio_features_energy
            audio_features_loudness

            min_acousticness
            max_acousticness
            min_liveness
            max_liveness
            min_speechiness
            max_speechiness
            min_valence
            max_valence
            min_danceability
            max_danceability
            min_instrumentalness
            max_instrumentalness
            min_tempo
            max_tempo
            min_energy
            max_energy
            min_loudness
            max_loudness
     */
}

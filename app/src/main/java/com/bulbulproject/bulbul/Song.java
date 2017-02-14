package com.bulbulproject.bulbul;

import android.widget.ImageView;

/**
 * Created by mesutgurlek on 2/13/17.
 */

public class Song {
    private int id;
    private String name;
    private String artist;
    private int imageResourceId;
    private float rating;

    public Song(int id, String name, String artist, int image, float rating){
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.imageResourceId = image;
    }


    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageResourceId;
    }

    public void setImageId(int image) {
        this.imageResourceId = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}

package com.bulbulproject.bulbul.model;

/**
 * Created by aeakdogan on 22/04/2017.
 */

public class MySong {
    private int id;
    private String name;
    private String albumName;
    private String artistName;
    private float rating;
    private String imageUrl;
    int testResult;


    public MySong(int id, String name, String albumName, String artistName, float rating, String imageUrl) {
        this.id = id;
        this.name = name;
        this.albumName = albumName;
        this.artistName = artistName;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
    public String getAlbumName() {
        return albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getTestResult() {
        return testResult;
    }

    public void setTestResult(int testResult) {
        this.testResult = testResult;
    }
}

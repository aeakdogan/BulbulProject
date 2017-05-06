package com.bulbulproject.bulbul.model;

/**
 * Created by aeakdogan on 22/04/2017.
 */

public class MySong {
    private int id;
    private String name;
    private String albumName;
    private String artistName;
    private float rating = -1;
    private String imageUrl;
    private String previewUrl;
    private String spotifyUrl;

    int testResult;


    public MySong(int id, String name, String albumName, String artistName, float rating, String imageUrl, String previewUrl) {
        this.id = id;
        this.name = name;
        this.albumName = albumName;
        this.artistName = artistName;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.previewUrl = previewUrl;
    }

    public MySong(int id, String spotifyUrl, String name, String albumName, String artistName, float rating, String imageUrl) {
        this.id = id;
        this.name = name;
        this.albumName = albumName;
        this.artistName = artistName;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.spotifyUrl =  "spotify:track:"+spotifyUrl;
    }

    public MySong(int id, String name){
        this.id = id;
        this.name = name;
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

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getSpotifyUrl() { return spotifyUrl;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

package com.bulbulproject.bulbul.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aeakdogan on 22/04/2017.
 */

public class Song {
    private int id;
    private String name;
//    private String albumName;
//    private String artistName;
    private float rating = -1;
    private String imageUrl;
    private String previewUrl;
    private String spotifyUrl;
    private List<Album> albums;
    private List<Artist> artists;
    int testResult;

    public Song(){
        this.albums = new ArrayList<Album>();
        this.artists = new ArrayList<Artist>();
    }
    public Song(int id, String name, String imageUrl, String spotifyUrl){
        this();
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.spotifyUrl =  "spotify:track:"+spotifyUrl;
    }
    public Song(int id, String name, float rating, String imageUrl, String previewUrl) {
        this();
        this.id = id;
        this.name = name;
//        this.albumName = albumName;
//        this.artistName = artistName;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.previewUrl = previewUrl;
    }

    public Song(int id, String name, String spotifyUrl, float rating, String imageUrl) {
        this();
        this.id = id;
        this.name = name;
//        this.albumName = albumName;
//        this.artistName = artistName;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.spotifyUrl =  "spotify:track:"+spotifyUrl;
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
//    public String getAlbumName() {
//        return albumName;
//    }

//    public String getArtistName() {
//        return artistName;
//    }

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

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getFirstAlbumName(){
        if(albums.size() > 0)
            return albums.get(0).getName();
        return "Unknown Album";
    }
    public String getFirstArtistName(){
        if(artists.size() > 0)
            return artists.get(0).getName();
        return "Unknown Artist";
    }
}

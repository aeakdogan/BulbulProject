package com.bulbulproject.bulbul.model;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mesutgurlek on 2/13/17.
 */

public class Song {
    private int id;
    private String name;
    private int year;
    private int durationSeconds;
    private String genre;
    private List<Album> albums;



    private List<Artist> artists;
    private int photoId;
    private String spotifyUrl;
    private float rating;

    public Song(int id, String name, int photoId, float rating) {
        this.id = id;
        this.name = name;
        this.photoId = photoId;
        this.rating = rating;
        this.albums = new ArrayList<Album>();
        this.artists = new ArrayList<Artist>();
    }

    public Song(int id, String name, int photoId, float rating, String spotifyTrackId) {
        this.id = id;
        this.name = name;
        this.photoId = photoId;
        this.rating = rating;
        this.albums = new ArrayList<Album>();
        this.artists = new ArrayList<Artist>();
        this.spotifyUrl = "spotify:track:"+spotifyTrackId;
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


    public void setName(String name) {
        this.name = name;
    }

    public String getArtistsString() {
        if( this.artists == null || this.artists.size() == 0)
            return "Unknown Artist";

        String str = "";
        for( Artist artist : this.artists)
            str += artist.toString() + ", ";

        return str;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}

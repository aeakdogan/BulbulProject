package com.bulbulproject.bulbul.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burak on 14.02.2017.
 */
public class Album {
    private int id;
    private String name;
    private int year;
    private List<Song> songs;
    private List<Artist> artists;
    private int photoId;
    private String imageUrl;
    private int songsCount = -1;

    public Album(String name, int year, int photoId) {
        this.name = name;
        this.year = year;
        this.photoId = photoId;
        this.songs = new ArrayList<Song>();
        this.artists = new ArrayList<Artist>();
    }

    public Album(String name, int year, String imageUrl) {
        this.name = name;
        this.year = year;
        this.imageUrl = imageUrl;
        this.songs = new ArrayList<Song>();
        this.artists = new ArrayList<Artist>();
    }

    public int getSongsCount() {
        if (songsCount == -1) return songs.size();
        return songsCount;
    }

    public void setSongsCount(int songsCount) {
        this.songsCount = songsCount;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public String getArtistsString() {
        if (this.artists == null || this.artists.size() == 0)
            return "Unknown Artist";

        String str = "";
        for (int i = 0; i < artists.size(); i++) {
            str += artists.get(i).getName() + (i == artists.size() - 1 ? "" : ", ");
        }

        return str;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

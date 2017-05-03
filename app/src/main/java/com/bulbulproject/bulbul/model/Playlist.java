package com.bulbulproject.bulbul.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burak on 14.02.2017.
 */
public class Playlist {
    private int id;
    private int userId;
    private List<Song> songs;
    private String name;
    private int photoId;
    private int songsCount = -1;

    public Playlist(String name, int id) {
        this.id = id;
        this.name = name;
        this.songs = new ArrayList<Song>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public int getSongsCount() {
        if(songsCount == -1) return songs.size();
        return songsCount;
    }

    public void setSongsCount(int songsCount) {
        this.songsCount = songsCount;
    }

}

package com.bulbulproject.bulbul.model;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by burak on 14.02.2017.
 */
public class Artist {
    private int id;
    private String name;
    private List<Album> albums;
    private int photoId;
    private String imageUrl;
    private int albumsCount = -1;
    private boolean selected = false;

    public Artist(String name, int photoId) {
        this.name = name;
        this.photoId = photoId;
        this.albums = new ArrayList<Album>();
    }

    public Artist(String name) {

        this.name = name;
        this.albums = new ArrayList<Album>();
    }

    public Artist(String name, String imageUrl) {

        this.name = name;
        this.albums = new ArrayList<Album>();
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public int getAlbumsCount() {
        if (albumsCount == -1) return albums.size();
        return albumsCount;
    }

    public void setAlbumsCount(int albumsCount) {
        this.albumsCount = albumsCount;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

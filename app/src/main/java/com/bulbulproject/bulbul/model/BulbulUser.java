package com.bulbulproject.bulbul.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burak on 14.02.2017.
 */
public class BulbulUser {
    private int id;
    private String username;
    private String profilePhoto;
    private List<BulbulUser> followers;
    private List<BulbulUser> followings;
    private int followersCount = -1;
    private int followingsCount = -1;

    public BulbulUser(String username) {
        this.username = username;
        this.followers = new ArrayList<BulbulUser>();
        this.followings = new ArrayList<BulbulUser>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public int getFollowersCount() {
        if(followersCount == -1) return followers.size();
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingsCount() {
        if(followingsCount == -1) return followings.size();
        return followingsCount;
    }

    public void setFollowingsCount(int followingsCount) {
        this.followingsCount = followingsCount;
    }

    public List<BulbulUser> getFollowers() {
        return followers;
    }

    public void setFollowers(List<BulbulUser> followers) {
        this.followers = followers;
    }

    public List<BulbulUser> getFollowings() {
        return followings;
    }

    public void setFollowings(List<BulbulUser> followings) {
        this.followings = followings;
    }
}

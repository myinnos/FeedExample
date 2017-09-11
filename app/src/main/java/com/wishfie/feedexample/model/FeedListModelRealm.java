package com.wishfie.feedexample.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by myinnos on 07/09/17.
 */

public class FeedListModelRealm extends RealmObject {

    private String id;
    private String thumbnail_image;
    private String event_name;
    private int event_timestamp;
    private int views;
    private int likes;
    private int shares;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public int getEvent_timestamp() {
        return event_timestamp;
    }

    public void setEvent_timestamp(int event_timestamp) {
        this.event_timestamp = event_timestamp;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }
}

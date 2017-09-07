package com.wishfie.feedexample.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by myinnos on 07/09/17.
 */

public class FeedModel {

    @SerializedName("posts")
    List<FeedListModel> posts;
    @SerializedName("page")
    int page;

    public FeedModel(List<FeedListModel> posts, int page) {
        this.posts = posts;
        this.page = page;
    }

    public List<FeedListModel> getPosts() {
        return posts;
    }

    public void setPosts(List<FeedListModel> posts) {
        this.posts = posts;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}

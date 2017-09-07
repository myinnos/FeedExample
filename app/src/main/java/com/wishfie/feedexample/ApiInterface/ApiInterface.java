package com.wishfie.feedexample.ApiInterface;

/**
 * Created by myinnos on 07/09/17.
 */

import com.wishfie.feedexample.model.FeedModel;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiInterface {

    @GET("v2/59ac28a9100000ce0bf9c236")
    Call<FeedModel> getFeeds();

    @GET("v2/59ac293b100000d60bf9c239")
    Call<FeedModel> getSecondFeed();

}
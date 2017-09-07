package com.wishfie.feedexample.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wishfie.feedexample.ApiInterface.ApiClient;
import com.wishfie.feedexample.ApiInterface.ApiInterface;
import com.wishfie.feedexample.R;
import com.wishfie.feedexample.adapter.FeedItemsAdapter;
import com.wishfie.feedexample.functions.InfiniteScrollRecycler.InfiniteScrollProvider;
import com.wishfie.feedexample.functions.InfiniteScrollRecycler.OnLoadMoreListener;
import com.wishfie.feedexample.functions.Remember;
import com.wishfie.feedexample.functions.FXConstants;
import com.wishfie.feedexample.functions.FXHelperClass;
import com.wishfie.feedexample.model.FeedListModel;
import com.wishfie.feedexample.model.FeedModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    LinearLayoutManager mLayoutManager;
    Toolbar toolbar;
    ImageView toolbar_sort;
    TextView toolbar_title;
    RecyclerView recyclerView;
    List<FeedListModel> ytVideosItems = new ArrayList<>();
    FeedItemsAdapter feedItemsAdapter;
    AlertDialog rtAlertDialog;
    CharSequence[] values = {"default", "likes", "views", "shares"};

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InfiniteScrollProvider infiniteScrollProvider = new InfiniteScrollProvider();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_sort = (ImageView) findViewById(R.id.toolbar_sort);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        feedItemsAdapter = new FeedItemsAdapter(ytVideosItems,
                R.layout.feed_item, this, recyclerView);

        recyclerView.setAdapter(feedItemsAdapter);
        setRecycleView("");

        infiniteScrollProvider.attach(recyclerView, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                setRecycleView(FXHelperClass.getNextPageToken());
            }
        });

        // toolbar attributes
        toolbar_title.setText("Feed Example");
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }

    public void setRecycleView(String nextPageToken) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<FeedModel> call = null;
        if (nextPageToken.equals("2")) {
            call = apiService.getSecondFeed();
        } else {
            call = apiService.getFeeds();
        }

        call.enqueue(new Callback<FeedModel>() {
            @Override
            public void onResponse(Call<FeedModel> call, Response<FeedModel> response) {

                int statusCode = response.code();
                Log.e(TAG, String.valueOf(statusCode));

                FeedModel ytVideos = response.body();
                Remember.putString(FXConstants.NEXT_PAGE_TOKEN, String.valueOf(ytVideos.getPage()));

                ytVideosItems.addAll(response.body().getPosts());
                feedItemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<FeedModel> call, Throwable t) {
                Log.e(TAG, String.valueOf(t));
            }
        });
    }

    public void chooseSortingOptions(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("CHOOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rtAlertDialog.dismiss();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rtAlertDialog.dismiss();
            }
        });
        builder.setSingleChoiceItems(values, 0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                break;
                            case 1:
                                Collections.sort(ytVideosItems, new Comparator<FeedListModel>() {
                                    @Override
                                    public int compare(FeedListModel lhs, FeedListModel rhs) {
                                        return String.valueOf(lhs.getLikes()).compareTo(String.valueOf(rhs.getLikes()));
                                    }
                                });

                                feedItemsAdapter.notifyDataSetChanged();
                                break;
                            case 2:

                                Collections.sort(ytVideosItems, new Comparator<FeedListModel>() {
                                    @Override
                                    public int compare(FeedListModel lhs, FeedListModel rhs) {
                                        return String.valueOf(lhs.getViews()).compareTo(String.valueOf(rhs.getViews()));
                                    }
                                });

                                feedItemsAdapter.notifyDataSetChanged();
                                break;
                            case 3:
                                Collections.sort(ytVideosItems, new Comparator<FeedListModel>() {
                                    @Override
                                    public int compare(FeedListModel lhs, FeedListModel rhs) {
                                        return String.valueOf(lhs.getShares()).compareTo(String.valueOf(rhs.getShares()));
                                    }
                                });

                                feedItemsAdapter.notifyDataSetChanged();
                                break;

                        }
                    }
                });
        rtAlertDialog = builder.create();
        rtAlertDialog.show();

    }
}
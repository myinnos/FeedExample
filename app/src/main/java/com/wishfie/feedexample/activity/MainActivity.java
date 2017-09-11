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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wishfie.feedexample.ApiInterface.ApiClient;
import com.wishfie.feedexample.ApiInterface.ApiInterface;
import com.wishfie.feedexample.R;
import com.wishfie.feedexample.adapter.FeedItemsAdapter;
import com.wishfie.feedexample.adapter.RealmFeedsAdapter;
import com.wishfie.feedexample.controller.RealmController;
import com.wishfie.feedexample.functions.InfiniteScrollRecycler.InfiniteScrollProvider;
import com.wishfie.feedexample.functions.InfiniteScrollRecycler.OnLoadMoreListener;
import com.wishfie.feedexample.functions.Remember;
import com.wishfie.feedexample.functions.FXConstants;
import com.wishfie.feedexample.functions.FXHelperClass;
import com.wishfie.feedexample.model.FeedListModel;
import com.wishfie.feedexample.model.FeedListModelRealm;
import com.wishfie.feedexample.model.FeedModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    LinearLayoutManager mLayoutManager;
    Toolbar toolbar;
    ImageView toolbar_sort;
    TextView toolbar_title, txError;
    RecyclerView recyclerView;
    ArrayList<FeedListModelRealm> ytVideosItems = new ArrayList<>();
    FeedItemsAdapter feedItemsAdapter;
    AlertDialog rtAlertDialog;
    ProgressBar progressBar;
    CharSequence[] values = {"default", "likes", "views", "shares"};
    private Realm realm;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get realm instance
        this.realm = RealmController.with(this).getRealm();

        InfiniteScrollProvider infiniteScrollProvider = new InfiniteScrollProvider();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_sort = (ImageView) findViewById(R.id.toolbar_sort);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        txError = (TextView) findViewById(R.id.txError);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        feedItemsAdapter = new FeedItemsAdapter(R.layout.feed_item, this, recyclerView);
        recyclerView.setAdapter(feedItemsAdapter);

        if (!Remember.getBoolean(FXConstants.DATA_PRE_LOADED, false)) {
            setRecycleView("");
        }

        // refresh the realm instance
        RealmController.with(this).refresh();
        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
        setRealmAdapter(RealmController.with(this).getFeeds());

        //recyclerView.setAdapter(feedItemsAdapter);

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

    public void setRealmAdapter(RealmResults<FeedListModelRealm> feedListModelRealms) {

        RealmFeedsAdapter realmAdapter = new RealmFeedsAdapter(
                this.getApplicationContext(), feedListModelRealms, true);
        // Set the data and tell the RecyclerView to draw
        feedItemsAdapter.setRealmAdapter(realmAdapter);
        feedItemsAdapter.notifyDataSetChanged();
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
                txError.setVisibility(View.GONE);

                FeedModel ytVideos = response.body();

                FeedListModelRealm feedListModel = new FeedListModelRealm();

                for (int i = 0; i < ytVideos.getPosts().size(); i++) {
                    feedListModel = new FeedListModelRealm();
                    feedListModel.setId(ytVideos.getPosts().get(i).getId());
                    feedListModel.setEvent_name(ytVideos.getPosts().get(i).getEvent_name());
                    feedListModel.setEvent_timestamp(ytVideos.getPosts().get(i).getEvent_timestamp());
                    feedListModel.setLikes(ytVideos.getPosts().get(i).getLikes());
                    feedListModel.setShares(ytVideos.getPosts().get(i).getShares());
                    feedListModel.setViews(ytVideos.getPosts().get(i).getViews());
                    feedListModel.setThumbnail_image(ytVideos.getPosts().get(i).getThumbnail_image());
                    ytVideosItems.add(feedListModel);
                }

                Remember.putString(FXConstants.NEXT_PAGE_TOKEN, String.valueOf(ytVideos.getPage()));

                //ytVideosItems.addAll(response.body().getPosts());
                //feedItemsAdapter.notifyDataSetChanged();

                for (FeedListModelRealm b : ytVideosItems) {
                    // Persist your data easily
                    realm.beginTransaction();
                    realm.copyToRealm(b);
                    realm.commitTransaction();
                }

                Remember.putBoolean(FXConstants.DATA_PRE_LOADED, true);
                feedItemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<FeedModel> call, Throwable t) {
                Log.e(TAG, String.valueOf(t));
                progressBar.setVisibility(View.GONE);
                txError.setVisibility(View.VISIBLE);
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
                                Collections.sort(ytVideosItems, new Comparator<FeedListModelRealm>() {
                                    @Override
                                    public int compare(FeedListModelRealm lhs, FeedListModelRealm rhs) {
                                        return String.valueOf(lhs.getLikes()).compareTo(String.valueOf(rhs.getLikes()));
                                    }
                                });

                                feedItemsAdapter.notifyDataSetChanged();
                                break;
                            case 2:

                                Collections.sort(ytVideosItems, new Comparator<FeedListModelRealm>() {
                                    @Override
                                    public int compare(FeedListModelRealm lhs, FeedListModelRealm rhs) {
                                        return String.valueOf(lhs.getViews()).compareTo(String.valueOf(rhs.getViews()));
                                    }
                                });

                                feedItemsAdapter.notifyDataSetChanged();
                                break;
                            case 3:
                                Collections.sort(ytVideosItems, new Comparator<FeedListModelRealm>() {
                                    @Override
                                    public int compare(FeedListModelRealm lhs, FeedListModelRealm rhs) {
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
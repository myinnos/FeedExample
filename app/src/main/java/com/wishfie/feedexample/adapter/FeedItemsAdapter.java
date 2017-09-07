package com.wishfie.feedexample.adapter;

/**
 * Created by myinnos on 07/09/17.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;
import com.wishfie.feedexample.R;
import com.wishfie.feedexample.functions.RecyclerViewAnimator;
import com.wishfie.feedexample.model.FeedListModel;

import java.util.List;

public class FeedItemsAdapter extends RecyclerView.Adapter<FeedItemsAdapter.MovieViewHolder> {

    private List<FeedListModel> feedListModelList;
    private int rowLayout;
    private Activity activity;
    private RecyclerViewAnimator mAnimator;

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView heading, timeStamp, txLikes, txViews, txShares;

        public MovieViewHolder(View v) {
            super(v);
            heading = (TextView) v.findViewById(R.id.heading);
            imageView = (ImageView) v.findViewById(R.id.imageView);
            timeStamp = (TextView) v.findViewById(R.id.timeStamp);
            txLikes = (TextView) v.findViewById(R.id.txLikes);
            txViews = (TextView) v.findViewById(R.id.txViews);
            txShares = (TextView) v.findViewById(R.id.txShares);
        }
    }

    public FeedItemsAdapter(List<FeedListModel> feedListModelList,
                            int rowLayout, Activity activity, RecyclerView recyclerView) {
        this.feedListModelList = feedListModelList;
        this.rowLayout = rowLayout;
        this.activity = activity;
        mAnimator = new RecyclerViewAnimator(recyclerView);
    }

    @Override
    public FeedItemsAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        /**
         * First item's entrance animations.
         */
        mAnimator.onCreateViewHolder(view);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        holder.heading.setText(feedListModelList.get(position).getEvent_name());

        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(String.valueOf(feedListModelList.get(position).getEvent_timestamp())),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.timeStamp.setText(timeAgo.toString());
        //holder.timeStamp.setText(getTimeAgo(feedListModelList.get(position).getEvent_timestamp()));

        holder.txLikes.setText(String.format("%s Likes",
                String.valueOf(feedListModelList.get(position).getLikes())));
        holder.txViews.setText(String.format("%s Views",
                String.valueOf(feedListModelList.get(position).getViews())));
        holder.txShares.setText(String.format("%s Shares",
                String.valueOf(feedListModelList.get(position).getShares())));

        Picasso.with(activity)
                .load(feedListModelList.get(position).getThumbnail_image())
                .into(holder.imageView);

        /**
         * Item's entrance animations during scroll are performed here.
         */
        mAnimator.onBindViewHolder(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return feedListModelList.size();
    }

    /*
      returns the time in - how much time ago
   */
    public static String getTimeAgo(long milsec) {
        String timeAgo = "";

        if (milsec < 0) {
            milsec = milsec + 18180000;
        }

        if (milsec <= 60000) {
            timeAgo = "Few Seconds ago";
        } else if (milsec <= 3600000) {
            long timeinMin = milsec / (1000 * 60);
            timeAgo = String.valueOf(timeinMin) + " Minutes ago";
        } else if (milsec <= 86400000) {
            long timeinHr = milsec / (1000 * 3600);
            timeAgo = String.valueOf(timeinHr) + " Hours ago";
        } else if (milsec <= 86400000 * 24) {

            long timeinDay = milsec / (1000 * 3600 * 24);
            timeAgo = String.valueOf(timeinDay) + " Days ago";
        } else {
            long timeinMonth = milsec / (1000 * 3600 * 24 * 30);
            timeAgo = String.valueOf(timeinMonth).replace("-", "") + " Months ago";
        }

        return timeAgo;
    }
}
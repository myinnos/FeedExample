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
import com.wishfie.feedexample.controller.RealmController;
import com.wishfie.feedexample.functions.RecyclerViewAnimator;
import com.wishfie.feedexample.model.FeedListModel;
import com.wishfie.feedexample.model.FeedListModelRealm;

import java.util.List;

import io.realm.Realm;

public class FeedItemsAdapter extends RealmRecyclerViewAdapter<FeedListModelRealm> {

    private int rowLayout;
    private Activity activity;
    private RecyclerViewAnimator mAnimator;
    private Realm realm;

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

    public FeedItemsAdapter(int rowLayout, Activity activity, RecyclerView recyclerView) {
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
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        realm = RealmController.getInstance().getRealm();
        final FeedListModelRealm feedListModelList = getItem(position);
        // cast the generic view holder to our specific one
        final MovieViewHolder holder = (MovieViewHolder) viewHolder;

        holder.heading.setText(feedListModelList.getEvent_name());

        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(String.valueOf(feedListModelList.getEvent_timestamp())),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.timeStamp.setText(timeAgo.toString());
        //holder.timeStamp.setText(getTimeAgo(feedListModelList.get(position).getEvent_timestamp()));

        holder.txLikes.setText(String.format("%s Likes",
                String.valueOf(feedListModelList.getLikes())));
        holder.txViews.setText(String.format("%s Views",
                String.valueOf(feedListModelList.getViews())));
        holder.txShares.setText(String.format("%s Shares",
                String.valueOf(feedListModelList.getShares())));

        Picasso.with(activity)
                .load(feedListModelList.getThumbnail_image())
                .into(holder.imageView);

        /**
         * Item's entrance animations during scroll are performed here.
         */
        mAnimator.onBindViewHolder(holder.itemView, position);
    }


    public int getItemCount() {

        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }
}
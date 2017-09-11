package com.wishfie.feedexample.adapter;

/**
 * Created by myinnos on 11/09/17.
 */

import android.content.Context;

import com.wishfie.feedexample.model.FeedListModelRealm;

import io.realm.RealmResults;

public class RealmFeedsAdapter extends RealmModelAdapter<FeedListModelRealm> {

    public RealmFeedsAdapter(Context context, RealmResults<FeedListModelRealm> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}
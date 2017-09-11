package com.wishfie.feedexample.controller;

/**
 * Created by myinnos on 11/09/17.
 */

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.wishfie.feedexample.model.FeedListModelRealm;

import io.realm.Realm;
import io.realm.RealmResults;


public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        realm.clear(FeedListModelRealm.class);
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<FeedListModelRealm> getFeeds() {

        return realm.where(FeedListModelRealm.class).findAll();
    }

    //query a single item with the given id
    public FeedListModelRealm getFeed(String id) {

        return realm.where(FeedListModelRealm.class).equalTo("id", id).findFirst();
    }

    public boolean hasFeeds() {
        return !realm.allObjects(FeedListModelRealm.class).isEmpty();
    }
}
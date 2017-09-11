package com.wishfie.feedexample;

/**
 * Created by myinnos on 07/09/17.
 */

import android.app.Application;

import com.wishfie.feedexample.functions.Remember;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Remember.init(getApplicationContext(), BuildConfig.APPLICATION_ID);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

}

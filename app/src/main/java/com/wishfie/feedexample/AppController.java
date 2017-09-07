package com.wishfie.feedexample;

/**
 * Created by myinnos on 07/09/17.
 */

import android.app.Application;

import com.wishfie.feedexample.functions.Remember;

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Remember.init(getApplicationContext(), BuildConfig.APPLICATION_ID);
    }

}

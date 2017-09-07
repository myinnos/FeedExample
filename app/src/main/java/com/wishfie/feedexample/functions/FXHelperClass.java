package com.wishfie.feedexample.functions;

/**
 * Created by myinnos on 07/09/17.
 */

public class FXHelperClass {

    public static String getNextPageToken() {
        return Remember.getString(FXConstants.NEXT_PAGE_TOKEN, "");
    }
}

package com.dev.mohamed.samacard.contentProvider;

import android.net.Uri;

import com.dev.mohamed.samacard.sqliteDb.DbContract;


/**
 * Created by moham on 2/28/2018.
 */

public class ContentProviderContract {



    public static final String AUTHORITY="com.dev.mohamed.samacard";
    public static final Uri BASE_CONTENT_URL=Uri.parse("content://"+AUTHORITY);
    public static final String PATH_CARD = DbContract.CardDataEntry.TABLE_NAME;
    public static final String PATH_ACTIVITY = DbContract.CardDataEntry.ACTIVITY;

    public static class CardEntry
    {
        public static final Uri CONTENT_URI=BASE_CONTENT_URL.buildUpon().appendPath(PATH_CARD).build();
    }



}

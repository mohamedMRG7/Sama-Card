package com.dev.mohamed.samacard.contentProvider;

import android.net.Uri;

public class ContentProviderContract {
    public static final String AUTHORITY = "com.dev.mohamed.samacard";
    private static final Uri BASE_CONTENT_URL = Uri.parse("content://com.dev.mohamed.samacard");
    public static final String PATH_ACTIVITY = "activity";
    public static final String PATH_CARD = "cards";

    public static class CardEntry {
        public static final Uri CONTENT_URI = ContentProviderContract.BASE_CONTENT_URL.buildUpon().appendPath("cards").build();
    }
}

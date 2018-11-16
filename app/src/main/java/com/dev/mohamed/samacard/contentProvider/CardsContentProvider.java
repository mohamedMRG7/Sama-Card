package com.dev.mohamed.samacard.contentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dev.mohamed.samacard.CommonStaticKeys;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.addCard.AddCardActivity;
import com.dev.mohamed.samacard.contentProvider.ContentProviderContract.CardEntry;
import com.dev.mohamed.samacard.sqliteDb.DbContract.CardDataEntry;
import com.dev.mohamed.samacard.sqliteDb.DbHelper;
import com.dev.mohamed.samacard.user.UserCardData;

public class CardsContentProvider extends ContentProvider {
    private static final int ALLCARDS = 100;
    private static final int CARD_WITH_ACTIVITY = 102;
    private static final int CARD_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DbHelper dp;

    public boolean onCreate() {
        this.dp = new DbHelper(getContext());
        return true;
    }

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(-1);
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "cards", ALLCARDS);
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "cards/*", CARD_WITH_ID);
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "cards/activity", CARD_WITH_ACTIVITY);
        return uriMatcher;
    }

    @Nullable
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor = null;
        int matcher = sUriMatcher.match(uri);
        SQLiteDatabase database = this.dp.getReadableDatabase();
        switch (matcher) {
            case ALLCARDS:
                cursor = database.query("cards", strings, s, strings1, null, null, s1);
                break;
            case CARD_WITH_ID:
                String str = "cards";
                cursor = database.query(str, null, "userid =? ", new String[]{uri.getPathSegments().get(1)}, null, null, null);
                break;
            case CARD_WITH_ACTIVITY:
                String str2 = "cards";
                SQLiteDatabase sQLiteDatabase = database;
                cursor = sQLiteDatabase.query(str2, null, "activity =? ", new String[]{uri.getPathSegments().get(2)}, null, null, null);
                break;
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Nullable
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database = this.dp.getWritableDatabase();
        Uri uri1 = null;
        switch (sUriMatcher.match(uri)) {
            case ALLCARDS:
                long id = database.insertWithOnConflict("cards", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                if (id > 0) {
                    uri1 = ContentUris.withAppendedId(CardEntry.CONTENT_URI, id);
                    break;
                }
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri1;
    }

    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int matcher = sUriMatcher.match(uri);
        SQLiteDatabase database = this.dp.getWritableDatabase();
        int result = 0;
        switch (matcher) {
            case ALLCARDS:
                result = database.delete("cards", null, null);
                break;
            case CARD_WITH_ID:
                String str = "cards";
                result = database.delete(str, "userid=?", new String[]{uri.getPathSegments().get(1)});
                break;
        }
        if (result != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return 0;
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    public static void insertCardToDb(UserCardData cardData, Context context) {
        ContentValues values = new ContentValues();
        values.put(CardDataEntry.USER_ID, cardData.getUserId());
        values.put(CardDataEntry.ABOUT_ACTITIY, cardData.getAboutActivity());
        values.put("activity", cardData.getActivity());
        values.put(CardDataEntry.ADDRESS, cardData.getAddress());
        values.put(CardDataEntry.CARDTYPE, cardData.getCardType());
        values.put(CardDataEntry.COMPANY_NAME, cardData.getCompanyName());
        values.put(CardDataEntry.COUNTRY, cardData.getCountry());
        values.put(CardDataEntry.DIRECT_NUMBER, cardData.getDirectCallNum());
        values.put("email", cardData.getEmail());
        values.put(CardDataEntry.FACEBOOK_PAGE, cardData.getFacebookAcount());
        values.put(CardDataEntry.GOVERNORATE, cardData.getGovernorate());
        values.put(CardDataEntry.OFFICIAL_NUMBER, cardData.getOfficeNumber());
        values.put(CardDataEntry.PHOTO_LINK, cardData.getPhotoLink());
        values.put(CardDataEntry.POSITION, cardData.getPositionName());
        values.put(CardDataEntry.USER_NAME, cardData.getUserName());
        values.put(CardDataEntry.WEBSITE, cardData.getWebSite());
        values.put(CardDataEntry.WHATSAPP, cardData.getWhatsApp());
        values.put(CardDataEntry.IS_ACCEPTED, cardData.getIsAccepted());
        values.put(CardDataEntry.DATE, cardData.getDate());
        values.put(CardDataEntry.OFFER_IMAGE, cardData.getOfferImg());
        try {
            context.getContentResolver().insert(CardEntry.CONTENT_URI, values);
        } catch (Exception e) {
        }
    }

    public static Cursor getSearchResult(String activity, String governorate, String area, Context context, String isAccepted) {
        SQLiteDatabase database = new DbHelper(context).getReadableDatabase();
        if (area.equals("كل المحافظه")) {
            return database.query("cards", null, "activity =?  AND country =?  AND isAccepted =? ", new String[]{activity, governorate, isAccepted}, null, null, null);
        }
        return database.query("cards", null, "activity =?  AND country =?  AND governorate =?  AND isAccepted =? ", new String[]{activity, governorate, area, isAccepted}, null, null, null);
    }

    public static Cursor getSpecialCards(Context context, String isAccepted) {
        return new DbHelper(context).getReadableDatabase().query("cards", null, "cardType =?  AND isAccepted =? ", new String[]{CommonStaticKeys.SPECIAL_CARD, isAccepted}, null, null, null);
    }

    public static Cursor getOffers(Context context, String isAccepted) {
        SQLiteDatabase database = new DbHelper(context).getReadableDatabase();
        String notAvailable = context.getResources().getString(R.string.dp_notavailble);
        return database.query("cards", null, "cardType =?  AND isAccepted =?  AND offer !=? ", new String[]{CommonStaticKeys.SPECIAL_CARD, isAccepted, notAvailable}, null, null, null);
    }

    public static Cursor getActivity(String activity, Context context, String isAccepted) {
        return new DbHelper(context).getReadableDatabase().query("cards", null, "activity =?  AND isAccepted =? ", new String[]{activity, isAccepted}, null, null, null);
    }

    public static Cursor getUserWithId(Context context, String id) {
        return context.getContentResolver().query(CardEntry.CONTENT_URI.buildUpon().appendPath(id).build(), null, null, null, null);
    }

    public static boolean isHaveSpecialcard(Context context, String uId) {
        SQLiteDatabase database = new DbHelper(context).getReadableDatabase();
        String str = "cards";
        Cursor cursor = database.query(str, null, "cardType =?  AND isAccepted =?  AND userid =? ", new String[]{CommonStaticKeys.SPECIAL_CARD, "true", uId}, null, null, null);
        if (cursor.getCount() != 0) {
            if (!cursor.isClosed()) {
                cursor.close();
            }
            if (database.isOpen()) {
                database.close();
            }
            return true;
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        if (database.isOpen()) {
            database.close();
        }
        return false;
    }

    public static boolean isHaveOffer(Context context, String uId) {
        SQLiteDatabase database = new DbHelper(context).getReadableDatabase();
        String notAvailable = context.getResources().getString(R.string.dp_notavailble);
        String str = "cards";
        Cursor cursor = database.query(str, null, "cardType =?  AND isAccepted =?  AND offer !=?  AND userid =? ", new String[]{CommonStaticKeys.SPECIAL_CARD, "true", notAvailable, uId}, null, null, null);
        if (cursor.getCount() != 0) {
            if (!cursor.isClosed()) {
                cursor.close();
            }
            if (database.isOpen()) {
                database.close();
            }
            return true;
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        if (database.isOpen()) {
            database.close();
        }
        return false;
    }

    public static void deleteUserCard(String id, Context context) {
        context.getContentResolver().delete(CardEntry.CONTENT_URI.buildUpon().appendPath(id).build(), null, null);
    }

    public static void clearDatabase(Context context) {
        context.getContentResolver().delete(CardEntry.CONTENT_URI, null, null);
    }
}

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
import android.util.Log;

import com.dev.mohamed.samacard.sqliteDb.DbContract;
import com.dev.mohamed.samacard.sqliteDb.DbHelper;
import com.dev.mohamed.samacard.user.UserCardData;

/**
 * Created by moham on 2/28/2018.
 */

public class CardsContentProvider extends ContentProvider {

    private DbHelper dp;

    public static final int ALLCARDS =100;
    public static final int CARD_WITH_ID =101;
    public static final int CARD_WITH_ACTIVITY=102;
    public static final UriMatcher sUriMatcher =buildUriMatcher();

    @Override
    public boolean onCreate() {

        dp=new DbHelper(getContext());
        return true;
    }


    public static UriMatcher buildUriMatcher (  )
    {
        UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(ContentProviderContract.AUTHORITY,ContentProviderContract.PATH_CARD, ALLCARDS);
        uriMatcher.addURI(ContentProviderContract.AUTHORITY,ContentProviderContract.PATH_CARD +"/*", CARD_WITH_ID);
        uriMatcher.addURI(ContentProviderContract.AUTHORITY,ContentProviderContract.PATH_CARD +"/"+ContentProviderContract.PATH_ACTIVITY, CARD_WITH_ACTIVITY);

        Log.d("cool",ContentProviderContract.AUTHORITY+"/"
                +ContentProviderContract.PATH_CARD +"/"+ContentProviderContract.PATH_ACTIVITY+"/*");


    return uriMatcher;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor=null;
        int matcher=sUriMatcher.match(uri);
        SQLiteDatabase database=dp.getReadableDatabase();
        Log.e("data",matcher+" "+uri);
        switch (matcher)
        {
            case ALLCARDS:
                cursor=database.query(DbContract.CardDataEntry.TABLE_NAME,strings,s,strings1,null,null,s1);
                break;
            case CARD_WITH_ID:
                String id=uri.getPathSegments().get(1);
                String msellction=DbContract.CardDataEntry.USER_ID+" =? ";
                String [] msellctionargs=new String[]{id};

                cursor=database.query(DbContract.CardDataEntry.TABLE_NAME,
                        null,msellction,msellctionargs,null,null,null);
                break;

            case CARD_WITH_ACTIVITY:
                String activiy=uri.getPathSegments().get(2);
                String sellection=DbContract.CardDataEntry.ACTIVITY+" =? ";
                String [] sellctionArg=new String[]{activiy};
                Log.e("data",activiy);

                cursor=database.query(DbContract.CardDataEntry.TABLE_NAME,
                        null,sellection,sellctionArg,null,null,null);
        }


        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }


        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        SQLiteDatabase database=dp.getWritableDatabase();
        Uri uri1 = null;
        switch (sUriMatcher.match(uri)) {
            case ALLCARDS:
             long id= database.insertWithOnConflict(DbContract.CardDataEntry.TABLE_NAME, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
             if (id>0) {
                 uri1 = ContentUris.withAppendedId(ContentProviderContract.CardEntry.CONTENT_URI, id);
             }
                break;
             }
        getContext().getContentResolver().notifyChange(uri,null);
        return uri1;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        int matcher=sUriMatcher.match(uri);
        SQLiteDatabase database=dp.getWritableDatabase();
        String id=uri.getPathSegments().get(1);
        String msellction=DbContract.CardDataEntry.USER_ID+ "=?";
        String [] msellctionargs=new String []{id};
        int result=0;

        switch (matcher)
        {
            case CARD_WITH_ID:
               result= database.delete(DbContract.CardDataEntry.TABLE_NAME,msellction,msellctionargs);
                break;
            case ALLCARDS:
              result=  database.delete(DbContract.CardDataEntry.TABLE_NAME,null,null);
        }

    if (result!=0)
        getContext().getContentResolver().notifyChange(uri, null);

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


    public static void insertCardToDb(UserCardData cardData, Context context)
    {
     ContentValues values=new ContentValues();
        values.put(DbContract.CardDataEntry.USER_ID,cardData.getUserId());
        values.put(DbContract.CardDataEntry.ABOUT_ACTITIY,cardData.getAboutActivity());
        values.put(DbContract.CardDataEntry.ACTIVITY,cardData.getActivity());
        values.put(DbContract.CardDataEntry.ADDRESS,cardData.getAddress());
        values.put(DbContract.CardDataEntry.CARDTYPE,cardData.getCardType());
        values.put(DbContract.CardDataEntry.COMPANY_NAME,cardData.getCompanyName());
        values.put(DbContract.CardDataEntry.COUNTRY,cardData.getCountry());
        values.put(DbContract.CardDataEntry.DIRECT_NUMBER,cardData.getDirectCallNum());
        values.put(DbContract.CardDataEntry.EMAIL,cardData.getEmail());
        values.put(DbContract.CardDataEntry.FACEBOOK_PAGE,cardData.getFacebookAcount());
        values.put(DbContract.CardDataEntry.GOVERNORATE,cardData.getGovernorate());
        values.put(DbContract.CardDataEntry.OFFICIAL_NUMBER,cardData.getOfficeNumber());
        values.put(DbContract.CardDataEntry.PHOTO_LINK,cardData.getPhotoLink());
        values.put(DbContract.CardDataEntry.POSITION,cardData.getPositionName());
        values.put(DbContract.CardDataEntry.USER_NAME,cardData.getUserName());
        values.put(DbContract.CardDataEntry.WEBSITE,cardData.getWebSite());
        values.put(DbContract.CardDataEntry.WHATSAPP,cardData.getWhatsApp());


     context.getContentResolver().insert(ContentProviderContract.CardEntry.CONTENT_URI,values);


    }

    public static Cursor getActivity(String activity,Context context)

    {
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase database=dbHelper.getReadableDatabase();
        String sellection=DbContract.CardDataEntry.ACTIVITY+" =? ";
        String [] sellctionArg=new String[]{activity};


       Cursor cursor=database.query(DbContract.CardDataEntry.TABLE_NAME,
                null,sellection,sellctionArg,null,null,null);
       Log.e("cursor",cursor.getCount()+"");
      if (cursor!=null)
        cursor.close();

      return cursor;
    }
}

package com.dev.mohamed.samacard.sqliteDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mohamed on 3/19/18.
 */

public class DbHelper extends SQLiteOpenHelper{


   private static final String DATABASE_NAME="cards.db";
   private static final int DATABASE_VERSION=1;



    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE="CREATE TABLE "+ DbContract.CardDataEntry.TABLE_NAME +"( "
                + DbContract.CardDataEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DbContract.CardDataEntry.USER_ID+" TEXT UNIQUE NOT NULL,"
                + DbContract.CardDataEntry.ACTIVITY+" TEXT NOT NULL,"
                + DbContract.CardDataEntry.ABOUT_ACTITIY+" TEXT,"
                + DbContract.CardDataEntry.ADDRESS+" TEXT NOT NULL,"
                + DbContract.CardDataEntry.CARDTYPE+" TEXT NOT NULL,"
                + DbContract.CardDataEntry.COUNTRY+" TEXT NOT NULL,"
                + DbContract.CardDataEntry.COMPANY_NAME+" TEXT ,"
                + DbContract.CardDataEntry.DIRECT_NUMBER+" TEXT ,"
                + DbContract.CardDataEntry.EMAIL+" TEXT NOT NULL,"
                + DbContract.CardDataEntry.FACEBOOK_PAGE+" TEXT ,"
                + DbContract.CardDataEntry.GOVERNORATE+" TEXT NOT NULL,"
                + DbContract.CardDataEntry.PHOTO_LINK+" TEXT NOT NULL,"
                + DbContract.CardDataEntry.OFFICIAL_NUMBER+" TEXT NOT NULL,"
                + DbContract.CardDataEntry.POSITION+" TEXT ,"
                + DbContract.CardDataEntry.USER_NAME+" TEXT NOT NULL,"
                + DbContract.CardDataEntry.WEBSITE+" TEXT ,"
                + DbContract.CardDataEntry.WHATSAPP+" TEXT "+" )";


        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS "+ DbContract.CardDataEntry.TABLE_NAME);
        onCreate(db);
    }
}

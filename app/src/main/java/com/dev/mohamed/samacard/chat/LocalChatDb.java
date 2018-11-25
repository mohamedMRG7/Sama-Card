package com.dev.mohamed.samacard.chat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalChatDb extends SQLiteOpenHelper {


    private static final String DB_NAME="chat.db";
    private static final int DB_VERSION=1;
    public static final String TABLE_NAME="chat";

    public LocalChatDb(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table="CREATE TABLE IF NOT EXISTS "+TABLE_NAME +"("
                +ChatDbContract._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +ChatDbContract.SENDER+" NVARCHAR  NOT NULL,"
                +ChatDbContract.RESEVER+" NVARCHAR NOT NULL,"
                +ChatDbContract.MESSAGE+" TEXT ,"
                +ChatDbContract.MESSAGE_ID+ " NVARCHAR UNIQUE,"
                +ChatDbContract.DATE_AND_TIME+ " TEXT NOT NULL,"
                +ChatDbContract.SEEN+" BOOLEAN NOT NULL"
                +");";


        db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}

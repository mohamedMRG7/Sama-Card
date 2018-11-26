package com.dev.mohamed.samacard.sqliteDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cards.db";
    private static final int DATABASE_VERSION = 2;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS cards( _id INTEGER PRIMARY KEY AUTOINCREMENT,userid TEXT UNIQUE NOT NULL,activity TEXT NOT NULL,aboutActivity TEXT,adress TEXT NOT NULL,cardType TEXT NOT NULL,country TEXT NOT NULL,companyName TEXT ,directNumber TEXT ,email TEXT ,facebookPage TEXT ,governorate TEXT NOT NULL,photoLink TEXT NOT NULL,officeNumber TEXT NOT NULL,position TEXT ,userName TEXT NOT NULL,webSite TEXT ,date TEXT ,offer TEXT ,isAccepted TEXT NOT NULL,whatsapp TEXT  )");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cards");
        onCreate(db);
    }
}

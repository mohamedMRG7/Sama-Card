package com.dev.mohamed.samacard.chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.dev.mohamed.samacard.chat.LocalChatDb.TABLE_NAME;


public class LocalDbUtalis {

    static LocalChatDb db;

    public static void insertChat(Context context, Chat chat)
    {
        db=new LocalChatDb(context);
        SQLiteDatabase database=db.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(ChatDbContract.SENDER,chat.getSender());
        values.put(ChatDbContract.RESEVER,chat.getReciver());
        values.put(ChatDbContract.MESSAGE,chat.getMeassage());
        values.put(ChatDbContract.MESSAGE_ID,chat.getMessageId());
        values.put(ChatDbContract.DATE_AND_TIME,chat.getDateAndTime());
        values.put(ChatDbContract.SEEN,chat.isSeen());

        database.insertWithOnConflict(TABLE_NAME,null,values,SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static void makeSeen(Context context, String messageID)
    {
        db=new LocalChatDb(context);
        SQLiteDatabase database=db.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(ChatDbContract.SEEN,"1");
        database.update(TABLE_NAME,values,ChatDbContract.MESSAGE_ID +" =? ",new String[]{messageID});
    }

    public static Cursor getAllChat(Context context, String chatWith)
    {
        db=new LocalChatDb(context);
        SQLiteDatabase database= db.getReadableDatabase();

        return database.query(TABLE_NAME,null,ChatDbContract.SENDER+" =? OR "+ChatDbContract.RESEVER+" =? ",new String[]{chatWith,chatWith},null,null,ChatDbContract.DATE_AND_TIME+" DESC");
    }

    //stoped here
    public static int getUnSeenMessagesNum(Context context , String chatWith)
    {
        db=new LocalChatDb(context);
        SQLiteDatabase database= db.getReadableDatabase();
        Cursor cursor=database.query(TABLE_NAME,new String[]{ChatDbContract.SENDER},ChatDbContract.SEEN+"=? AND "+ChatDbContract.SENDER+" =? ",new String[]{"0",chatWith},null,null,null);//database.rawQuery("SELECT COUNT ("+ChatDbContract.SEEN+") AS SENDER FROM "+LocalChatDb.TABLE_NAME+ " WHERE "+ChatDbContract.SENDER+" =? "+" AND "+ChatDbContract.SEEN +" =?",new String[]{chatWith,"0"});
        int unSeenMessages=cursor.getCount();
        cursor.close();
        return unSeenMessages;
    }


    public static void clear(Context context)
    {
        db=new LocalChatDb(context);
        SQLiteDatabase database= db.getWritableDatabase();
        database.delete(LocalChatDb.TABLE_NAME,null,null);


    }
  /*  public static String [] getTheUnSeenMessages(Context context,String chatWith)
    {
        db=new LocalChatDb(context);
        SQLiteDatabase database= db.getReadableDatabase();
        Cursor cursor=database.query(TABLE_NAME,new String[]{ChatDbContract.MESSAGE_ID},ChatDbContract.SENDER+ "=?",new String[]{chatWith},null,null,null);
        String[] unSeenMessagesIDS=new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int i=0;i<cursor.getCount();i++)
        {
            unSeenMessagesIDS[i]=cursor.getString(0);
        }
        cursor.close();
        return unSeenMessagesIDS;

    }*/

    public static Cursor getListOfMessagedUSers(Context context , String uID)
    {
        db=new LocalChatDb(context);
        SQLiteDatabase database= db.getReadableDatabase();

    return         database.rawQuery("SELECT DISTINCT "+ChatDbContract.SENDER +" FROM "+TABLE_NAME +" WHERE "+ChatDbContract.SENDER+" NOT LIKE '"+uID
                +"' UNION "+
                "SELECT DISTINCT "+ChatDbContract.RESEVER +" FROM "+TABLE_NAME+" WHERE "+ChatDbContract.RESEVER+" NOT LIKE '"+uID+"'",null);
    }

    public static String lastMessage(Context context, String chatWith)
    {
        db=new LocalChatDb(context);
        SQLiteDatabase database= db.getReadableDatabase();

        Cursor cursor= database.query(TABLE_NAME,null,ChatDbContract.SENDER+" =? OR "+ChatDbContract.RESEVER+" =? ",new String[]{chatWith,chatWith},null,null,ChatDbContract.DATE_AND_TIME+" DESC","1");
        cursor.moveToFirst();
        String lastMessage=cursor.getString(cursor.getColumnIndex(ChatDbContract.MESSAGE));

        cursor.close();
        return lastMessage;
    }





}

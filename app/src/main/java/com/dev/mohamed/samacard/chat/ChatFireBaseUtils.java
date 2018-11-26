package com.dev.mohamed.samacard.chat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dev.mohamed.samacard.MainActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class ChatFireBaseUtils {


    private static final String MESSAGE_CHILD="Messages";
    public static void sendMessage(String from, String to , String message)
    {

        String key=String.valueOf(new Random().nextInt());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        FirebaseDatabase.getInstance().getReference()
                .child(MESSAGE_CHILD).child(from).child(key).setValue(new Chat(from,to,message,false,key,currentDateandTime));
        FirebaseDatabase.getInstance().getReference()
                .child(MESSAGE_CHILD).child(to).child(key).setValue(new Chat(from,to,message,false,key,currentDateandTime));

    }

    public static void setSeen(String key, String from , String to)
    {
        Map map=new HashMap<>();
        map.put("seen",true);
        FirebaseDatabase.getInstance().getReference().child(MESSAGE_CHILD).child(from).child(key).updateChildren(map);
        FirebaseDatabase.getInstance().getReference().child(MESSAGE_CHILD).child(to).child(key).updateChildren(map);
    }


    public static void ReciveMessages(final OnMessageResive resive)
    {
        String myID=MainActivity.getUserData().getUserId();
        FirebaseDatabase.getInstance().getReference().child(MESSAGE_CHILD).child(myID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                resive.message(dataSnapshot.getValue(Chat.class));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                resive.seen(dataSnapshot.getValue(Chat.class).getMessageId());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



  public interface OnMessageResive
    {
        void message(Chat chat);
        void seen(String chatID);
    }
}

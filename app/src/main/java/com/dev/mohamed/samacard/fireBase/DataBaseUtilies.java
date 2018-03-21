package com.dev.mohamed.samacard.fireBase;

import android.content.ContentProvider;
import android.content.Context;
import android.database.DatabaseUtils;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.contentProvider.CardsContentProvider;
import com.dev.mohamed.samacard.user.UserCardData;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by mohamed on 3/20/18.
 */

public class DataBaseUtilies {


    private static String CARDS_CHILD="cards";
    public static void insert(UserCardData data, final Context context)
    {
        FirebaseDatabase.getInstance().getReference().child(CARDS_CHILD).child(data.getUserId())
                .setValue(data)

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, R.string.add_card_error,Toast.LENGTH_SHORT).show();

                    }
                })

                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, R.string.add_card_succes,Toast.LENGTH_SHORT).show();

            }
        });

    }

    public static void getDataFromDb(final Context context, final onresiveData data)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(CARDS_CHILD);
        ChildEventListener listener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                data.resiveData(dataSnapshot.getValue(UserCardData.class));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                data.resiveData(dataSnapshot.getValue(UserCardData.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reference.addChildEventListener(listener);

    }


    public  interface onresiveData
    {
        void resiveData(UserCardData data);
    }

}

package com.dev.mohamed.samacard.fireBase;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;


import com.dev.mohamed.samacard.CommonStaticKeys;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.addCard.AddCardActivity;

import com.dev.mohamed.samacard.auth.AuthinticationActivity;
import com.dev.mohamed.samacard.contentProvider.CardsContentProvider;
import com.dev.mohamed.samacard.sqliteDb.DbContract.CardDataEntry;
import com.dev.mohamed.samacard.user.UserCardData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.onurkaganaldemir.ktoastlib.KToast;

import java.util.HashMap;
import java.util.Map;

public class DataBaseUtilies {
    private static final String CARDS_CHILD = "cards";
    public static final int CARD_ADDTION = 1;
    private static final int OFFER_ADDITION = 2;

    public interface onResiveData {
        void deleteUser(UserCardData userCardData);

        void lastReseved();

        void resiveData(UserCardData userCardData);
    }


    //INSERT A CARD TO DATABASE AND THEN SHOW MESSAGE THAT INSERTED AND WAITING FOR ACCEPT
    public static void insert(UserCardData data, final Activity context, final FragmentManager manager) {
        //CHECK IF THE USER ALREADY HAD A CARD AND IF HE HAD JUST UPDATE THE DATA
        if (CardsContentProvider.getUserWithId(context, data.getUserId()).getCount() != 0) {
            Cursor cursor = CardsContentProvider.getUserWithId(context, data.getUserId());
            cursor.moveToFirst();
            String offerlink = cursor.getString(cursor.getColumnIndex(CardDataEntry.OFFER_IMAGE));
            String photolink = cursor.getString(cursor.getColumnIndex(CardDataEntry.PHOTO_LINK));
            String isAccepted = cursor.getString(cursor.getColumnIndex(CardDataEntry.IS_ACCEPTED));

            if (!(photolink.equals(CommonStaticKeys.APP_LOGO) || !photolink.contains("https://firebasestorage") || photolink.equals(data.getPhotoLink()))) {
                StorageUtilies.deleteImage(photolink);
            }
            data.setIsAccepted(isAccepted);
            if (data.getCardType().equals(CommonStaticKeys.SPECIAL_CARD)) {
                data.setOfferImg(offerlink);
            }
        }
        FirebaseDatabase.getInstance().getReference().child(CARDS_CHILD).child(data.getUserId()).setValue(data).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, R.string.add_card_error, Toast.LENGTH_SHORT).show();
                context.finish();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            public void onSuccess(Void aVoid) {
                FragmentAddedMessage addedMessage = new FragmentAddedMessage();
                addedMessage.setOfferMessage(1);
                manager.beginTransaction().replace(R.id.addCard_container, addedMessage).commit();
            }
        });
    }

    public static void testInsert(UserCardData data) {
        FirebaseDatabase.getInstance().getReference().child(CARDS_CHILD).child(data.getUserId()).setValue(data);
    }

    //ACCEPT THE CARD AND MAKE IT VISIBLE AT THE MAINSCREEN
    public static void acceptCard(String uId, final Context context) {
        Map value = new HashMap();
        value.put(CardDataEntry.IS_ACCEPTED, "true");
        FirebaseDatabase.getInstance().getReference().child(CARDS_CHILD).child(uId).updateChildren(value).addOnSuccessListener(new OnSuccessListener() {
            public void onSuccess(Object o) {
                Toast.makeText(context, R.string.succes_card_addition, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, R.string.faied_card_addition,  Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void updateOffer(String uId, String offerImageLink, final Context context, final FragmentManager manager) {
        Map value = new HashMap();
        value.put("offerImg", offerImageLink);
        FirebaseDatabase.getInstance().getReference().child("cards").child(uId).updateChildren(value).addOnSuccessListener(new OnSuccessListener() {
            public void onSuccess(Object o) {
                Toast.makeText(context, R.string.offer_succesadded_message,  Toast.LENGTH_SHORT).show();
                FragmentAddedMessage addedMessage = new FragmentAddedMessage();
                addedMessage.setOfferMessage(2);
                manager.beginTransaction().replace(R.id.addCard_container, addedMessage).commit();
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, R.string.faied_card_addition, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void deleteOffer(String uId, final String offerImageLink, final Activity context) {
        Map value = new HashMap();
        value.put("offerImg", context.getString(R.string.dp_notavailble));
        FirebaseDatabase.getInstance().getReference().child(CARDS_CHILD).child(uId).updateChildren(value).addOnSuccessListener(new OnSuccessListener() {
            public void onSuccess(Object o) {
                KToast.successToast(context, context.getString(R.string.offer_remove_message), 80, KToast.LENGTH_LONG);
                if (!offerImageLink.equals(context.getString(R.string.dp_notavailble)) && offerImageLink.contains("https://firebasestorage")) {
                    StorageUtilies.deleteImage(offerImageLink);
                }
            }
        });
    }

    //DELETE THE CARD FORM DB
    public static void deleteCard(String uId, final Activity context, final String photoLink, final String offerLink) {
        FirebaseDatabase.getInstance().getReference().child(CARDS_CHILD).child(uId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            public void onSuccess(Void aVoid) {
                KToast.successToast(context, context.getString(R.string.succes_removing_card), 80, KToast.LENGTH_LONG);
               //HANDLE DELETING THE USER AVATAR IF HE HAS
                if (!photoLink.equals(CommonStaticKeys.APP_LOGO) && photoLink.contains("https://firebasestorage")) {
                    StorageUtilies.deleteImage(photoLink);
                }
                if (!offerLink.equals(context.getString(R.string.dp_notavailble)) && offerLink.contains("https://firebasestorage")) {
                    StorageUtilies.deleteImage(offerLink);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, R.string.failed_removing_card, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //GET THE DATA FROM THE DB
    public static void getDataFromDb(Context context, final onResiveData data) {
        FirebaseDatabase.getInstance().getReference().child(CARDS_CHILD).addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    if (dataSnapshot.getKey().equals("zzzzzzzzzzzzz")) {
                        data.lastReseved();
                    } else {
                        data.resiveData(dataSnapshot.getValue(UserCardData.class));
                    }
                } catch (Exception e) {
                }
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                try {
                    data.resiveData(dataSnapshot.getValue(UserCardData.class));
                } catch (Exception e) {
                }
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                data.deleteUser(dataSnapshot.getValue(UserCardData.class));
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

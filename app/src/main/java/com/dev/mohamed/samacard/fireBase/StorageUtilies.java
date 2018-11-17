package com.dev.mohamed.samacard.fireBase;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;


import com.dev.mohamed.samacard.CommonStaticKeys;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.addCard.FragmentPlaceholder;
import com.dev.mohamed.samacard.auth.AuthinticationActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

public class StorageUtilies {

    public interface OnImageUploaded {
        void loaded(String str, String str2);

        void offerLoaded(String str);
    }


    //UPLOAD THE IMAGE TO FIREBASE STORAGE AND RETURN THE IMAGE LINK TO ADD IT TO DB
    public static void getImageDonloadUrl(final String cardType, final Activity context, Uri file, final OnImageUploaded uploadedLisner, FragmentManager manager) {
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        //CHECK IF THE USER ADDED AN AVATAR OR NOT , IF NOT WILL USE THE APP LOGO AS AVATAR
        if (file == null) {
            uploadedLisner.loaded(CommonStaticKeys.APP_LOGO, cardType);
        } else if (file.getScheme().equals(Param.CONTENT)) {
            manager.beginTransaction().replace(R.id.addCard_container, new FragmentPlaceholder()).commit();
            UploadTask uploadTask = reference.child("logos").child(file.getLastPathSegment()).putFile(file);
            uploadTask.addOnSuccessListener(context, new OnSuccessListener<TaskSnapshot>() {
                public void onSuccess(TaskSnapshot taskSnapshot) {
                    uploadedLisner.loaded(taskSnapshot.getDownloadUrl().toString(), cardType);
                }
            });
            uploadTask.addOnFailureListener(context, new OnFailureListener() {
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context.getApplicationContext(), R.string.add_card_error, Toast.LENGTH_SHORT).show();
                    context.finish();
                }
            });
        } else if (file.getScheme().equals("https")) {
            uploadedLisner.loaded(file.toString(), cardType);
        }
    }

    //UPLOAD THE OFFER AND RETURN ITS URL TO ADD IT TO DB
    public static void getOfferImageUrl(final Activity context, Uri file, final OnImageUploaded uploadedLisner, FragmentManager manager) {
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        if (file != null && file.getScheme().equals(Param.CONTENT)) {
            manager.beginTransaction().replace(R.id.addCard_container, new FragmentPlaceholder()).commit();
            UploadTask uploadTask = reference.child("offers").child(file.getLastPathSegment()).putFile(file);
            uploadTask.addOnSuccessListener(context, new OnSuccessListener<TaskSnapshot>() {
                public void onSuccess(TaskSnapshot taskSnapshot) {
                    uploadedLisner.offerLoaded(taskSnapshot.getDownloadUrl().toString());
                }
            });
            uploadTask.addOnFailureListener(context, new OnFailureListener() {
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context.getApplicationContext(), R.string.add_card_error, Toast.LENGTH_SHORT).show();
                    context.finish();
                }
            });
        }
    }

    //USE WHEN DELETING CARD OR OFFER
    public static void deleteImage(String mImageUrl) {
        if (mImageUrl != null) {
            FirebaseStorage.getInstance().getReferenceFromUrl(mImageUrl).delete();
        }
    }
}

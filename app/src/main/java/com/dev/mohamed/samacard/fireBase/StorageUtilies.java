package com.dev.mohamed.samacard.fireBase;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.dev.mohamed.samacard.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by mohamed on 3/17/18.
 */

public class StorageUtilies {


    public static void getImageDonloadUrl(final Activity context, Uri file, final OnImageUploaded uploadedLisner) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
         StorageReference reference = storage.getReference();

        if (file != null) {

            if (file.getScheme().equals("content")) {
                StorageReference photoref = reference.child("logos").child(file.getLastPathSegment());

                UploadTask uploadTask = photoref.putFile(file);
                uploadTask.addOnSuccessListener(context, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploadedLisner.loaded(taskSnapshot.getDownloadUrl().toString());


                    }
                });
                uploadTask.addOnFailureListener(context, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("failluer", e.getMessage());
                        Toast.makeText(context.getApplicationContext(), R.string.add_card_error,Toast.LENGTH_SHORT).show();

                    }
                });


            }else  if (file.getScheme().equals("https")) uploadedLisner.loaded(file.toString());


        }else  uploadedLisner.loaded("Sama app logo");

    }




    public static void deleteImage(final String mImageUrl)
    {
        if (mImageUrl!=null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference photoRef = storage.getReferenceFromUrl(mImageUrl);

            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                }
            });
        }
    }

    public interface OnImageUploaded
    {
        void loaded(String url);
    }
}

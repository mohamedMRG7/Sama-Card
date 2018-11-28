package com.dev.mohamed.samacard.card;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.dev.mohamed.samacard.CommonStaticKeys;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.addCard.AddCardActivity;
import com.dev.mohamed.samacard.contentProvider.CardsContentProvider;
import com.dev.mohamed.samacard.databinding.ActivityNormalcardBinding;
import com.dev.mohamed.samacard.fireBase.DataBaseUtilies;
import com.dev.mohamed.samacard.sqliteDb.DbContract.CardDataEntry;
import com.squareup.picasso.Picasso;

public class NormalCardActvitiy extends AppCompatActivity implements OnClickListener {
    private static final int REQUEST_CALL_PERMISSION = 1;
    ActivityNormalcardBinding normalcardBinding;
    private String offerLink;
    private String photoLink;
    private String uId;
    private String offcialNumber;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_normalcard);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
        }

        normalcardBinding = (ActivityNormalcardBinding) DataBindingUtil.setContentView(this, R.layout.activity_normalcard);
        String userId = getIntent().getStringExtra(CommonStaticKeys.NORMAL_CARD);
        String useremail = getIntent().getStringExtra(CommonStaticKeys.EMAIL_KEY);
        Cursor cursor = CardsContentProvider.getUserWithId(this, userId);
        cursor.moveToFirst();
        String userName = cursor.getString(cursor.getColumnIndex(CardDataEntry.USER_NAME));
        photoLink = cursor.getString(cursor.getColumnIndex(CardDataEntry.PHOTO_LINK));
        offerLink = cursor.getString(cursor.getColumnIndex(CardDataEntry.OFFER_IMAGE));
        String commercialName = cursor.getString(cursor.getColumnIndex(CardDataEntry.COMPANY_NAME));
        String adress = cursor.getString(cursor.getColumnIndex(CardDataEntry.ADDRESS));
        offcialNumber = cursor.getString(cursor.getColumnIndex(CardDataEntry.OFFICIAL_NUMBER));
        uId = cursor.getString(cursor.getColumnIndex(CardDataEntry.USER_ID));
        normalcardBinding.tvAdress.setText(adress);
        normalcardBinding.tvCommercialName.setText(commercialName);
        normalcardBinding.tvName.setText(userName);

        if (useremail != null && useremail.equals(CommonStaticKeys.ADMIN_EMAIL)) {
            normalcardBinding.btAccept.setVisibility(View.VISIBLE);
            normalcardBinding.btAccept.setOnClickListener(this);
            normalcardBinding.btDeny.setVisibility(View.VISIBLE);
            normalcardBinding.btDeny.setOnClickListener(this);
        }
       // Glide.with((FragmentActivity) this).load(photoLink).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.ic_error)).into(normalcardBinding.imgLogo);
        Picasso.with(this).load(photoLink).placeholder(R.drawable.loading).error(R.drawable.ic_error).into(normalcardBinding.imgLogo);


    }

    public void call(View view) {
        if (ContextCompat.checkSelfPermission(this, "android.permission.CALL_PHONE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CALL_PHONE"}, 1);
            return;
        }
        callNumber();
    }

    private void callNumber() throws SecurityException {

        Intent intent = new Intent("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + offcialNumber));
        startActivity(intent);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 1) {
            return;
        }
        if (grantResults.length <= 0 || grantResults[0] != 0) {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        } else {
            callNumber();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_accept:
                DataBaseUtilies.acceptCard(uId, this);
                finish();
                return;
            case R.id.bt_deny:
                DataBaseUtilies.deleteCard(uId, this, photoLink, offerLink);
                finish();
                return;
            default:
                return;
        }
    }
}

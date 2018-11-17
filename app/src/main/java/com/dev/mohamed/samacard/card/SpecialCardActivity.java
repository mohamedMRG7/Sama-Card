package com.dev.mohamed.samacard.card;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.mohamed.samacard.CommonStaticKeys;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.addCard.AddCardActivity;
import com.dev.mohamed.samacard.contentProvider.CardsContentProvider;
import com.dev.mohamed.samacard.databinding.ActivitySpecialcardBinding;
import com.dev.mohamed.samacard.fireBase.DataBaseUtilies;
import com.dev.mohamed.samacard.sqliteDb.DbContract.CardDataEntry;
import com.facebook.share.internal.ShareConstants;
import com.squareup.picasso.Picasso;

public class SpecialCardActivity extends AppCompatActivity implements OnClickListener {
    private static final int REQUEST_CALL_PERMISSION = 1;
    private static final int REQUEST_OFFICE_CALL_PERMISSION = 2;
    private String email;
    private String facebook;
    private String offerLink;
    private String photoLink;
    ActivitySpecialcardBinding specialcardBinding;
    private String uId;
    private String webSite;
    private String whatapp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialcard);
        specialcardBinding = (ActivitySpecialcardBinding) DataBindingUtil.setContentView(this, R.layout.activity_specialcard);
        String userId = getIntent().getStringExtra(CommonStaticKeys.SPECIAL_CARD);
        String userEmail = getIntent().getStringExtra(CommonStaticKeys.EMAIL_KEY);
        Cursor cursor = CardsContentProvider.getUserWithId(this, userId);
        cursor.moveToFirst();
        String userName = cursor.getString(cursor.getColumnIndex(CardDataEntry.USER_NAME));
        photoLink = cursor.getString(cursor.getColumnIndex(CardDataEntry.PHOTO_LINK));
        offerLink = cursor.getString(cursor.getColumnIndex(CardDataEntry.OFFER_IMAGE));
        String commercialName = cursor.getString(cursor.getColumnIndex(CardDataEntry.COMPANY_NAME));
        String adress = cursor.getString(cursor.getColumnIndex(CardDataEntry.ADDRESS));
        String offcialNumber = cursor.getString(cursor.getColumnIndex(CardDataEntry.OFFICIAL_NUMBER));
        String position = cursor.getString(cursor.getColumnIndex(CardDataEntry.POSITION));
        String directNumber = cursor.getString(cursor.getColumnIndex(CardDataEntry.DIRECT_NUMBER));
        String aboutActiviy = cursor.getString(cursor.getColumnIndex(CardDataEntry.ABOUT_ACTITIY));
        facebook = cursor.getString(cursor.getColumnIndex(CardDataEntry.FACEBOOK_PAGE));
        whatapp = cursor.getString(cursor.getColumnIndex(CardDataEntry.WHATSAPP));
        webSite = cursor.getString(cursor.getColumnIndex(CardDataEntry.WEBSITE));
        email = cursor.getString(cursor.getColumnIndex(CommonStaticKeys.EMAIL_KEY));
        uId = cursor.getString(cursor.getColumnIndex(CardDataEntry.USER_ID));
        specialcardBinding.tvAdress.setText(adress);
        specialcardBinding.tvCommercialName.setText(commercialName);
        specialcardBinding.tvName.setText(userName);
        specialcardBinding.tvTelephon.setText(offcialNumber);
        specialcardBinding.tvAboutactivity.setText(aboutActiviy);
        specialcardBinding.tvDirecttelephon.setText(directNumber);
        specialcardBinding.tvPosition.setText(position);
        isNotAvailable(facebook, specialcardBinding.imgFacebook);
        isNotAvailable(webSite, specialcardBinding.imgWebsite);
        isNotAvailable(email, specialcardBinding.imgEmail);
        isNotAvailable(whatapp, specialcardBinding.imgWhatsapp);
        if (userEmail != null && userEmail.equals(CommonStaticKeys.ADMIN_EMAIL)) {
            specialcardBinding.btAccept.setVisibility(View.VISIBLE);
            specialcardBinding.btAccept.setOnClickListener(this);
            specialcardBinding.btAccept.setOnClickListener(this);
            specialcardBinding.btDeny.setVisibility(View.VISIBLE);
            specialcardBinding.btDeny.setOnClickListener(this);
        }
   //     Glide.with((FragmentActivity) this).load(photoLink).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.ic_error)).into(specialcardBinding.imgLogo);
        Picasso.with(this).load(photoLink).placeholder(R.drawable.loading).error(R.drawable.ic_error).into(specialcardBinding.imgLogo);

    }

    private static void isNotAvailable(String value, ImageView view) {
        if (value.equals("Not Available")) {
            view.setVisibility(View.GONE);
        }
    }

    public void officialCall(View view) {
        if (ContextCompat.checkSelfPermission(this, "android.permission.CALL_PHONE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CALL_PHONE"}, 1);
            return;
        }
        callNumber(specialcardBinding.tvTelephon);
    }

    private void callNumber(TextView tv) throws SecurityException {
        String number = tv.getText().toString();
        Intent intent = new Intent("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void openSite(String url) {
        if (!url.equals(getString(R.string.not_available))) {
            Uri uri;
            if (url.contains("http")) {
                uri = Uri.parse(url);
            } else {
                uri = Uri.parse("https:/").buildUpon().appendPath(url).build();
            }
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private void copyText(String text) {
        if (!text.equals(getString(R.string.not_available))) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(ShareConstants.WEB_DIALOG_PARAM_DATA, text);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, R.string.copy_message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void directCall(View view) {
        if (!specialcardBinding.tvDirecttelephon.getText().toString().equals(getString(R.string.not_available))) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.CALL_PHONE") != 0) {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.CALL_PHONE"}, 1);
                return;
            }
            callNumber(specialcardBinding.tvDirecttelephon);
        }
    }

    public void openFaceBook(View view) {
        openSite(facebook);
    }

    public void openSite(View view) {
        openSite(webSite);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length <= 0 || grantResults[0] != 0) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            } else {
                callNumber(specialcardBinding.tvDirecttelephon);
            }
        }
        if (requestCode != 2) {
            return;
        }
        if (grantResults.length <= 0 || grantResults[0] != 0) {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        } else {
            callNumber(specialcardBinding.tvTelephon);
        }
    }

    public void whatsapp(View view) {
        copyText(whatapp);
    }

    public void email(View view) {
        copyText(email);
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

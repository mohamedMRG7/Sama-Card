package com.dev.mohamed.samacard.user;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;


import com.dev.mohamed.samacard.CommonStaticKeys;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.MainActivity;
import com.dev.mohamed.samacard.addCard.AddCardActivity;
import com.dev.mohamed.samacard.card.NormalCardActvitiy;
import com.dev.mohamed.samacard.card.SpecialCardActivity;
import com.dev.mohamed.samacard.connection.CheckConnection;
import com.dev.mohamed.samacard.contentProvider.CardsContentProvider;
import com.dev.mohamed.samacard.databinding.ActivityMyCardAndOffersBinding;
import com.dev.mohamed.samacard.fireBase.DataBaseUtilies;
import com.dev.mohamed.samacard.sqliteDb.DbContract.CardDataEntry;
import com.onurkaganaldemir.ktoastlib.KToast;
import com.squareup.picasso.Picasso;

import static com.dev.mohamed.samacard.CommonStaticKeys.NORMAL_CARD;
import static com.dev.mohamed.samacard.CommonStaticKeys.SPECIAL_CARD;

public class MyCardAndOffersActivity extends AppCompatActivity implements OnClickListener {
    private String cardType;
    ActivityMyCardAndOffersBinding mBinding;
    private String offerImage;
    private String photoLink;
    private String uId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card_and_offers);
        mBinding = (ActivityMyCardAndOffersBinding) DataBindingUtil.setContentView(this, R.layout.activity_my_card_and_offers);
        uId = getIntent().getExtras().getString(CommonStaticKeys.KEY_UID);
        Cursor mCursor = CardsContentProvider.getUserWithId(this, uId);
        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            String userName = mCursor.getString(mCursor.getColumnIndex(CardDataEntry.USER_NAME));
            String activity = mCursor.getString(mCursor.getColumnIndex("activity"));
            photoLink = mCursor.getString(mCursor.getColumnIndex(CardDataEntry.PHOTO_LINK));
            cardType = mCursor.getString(mCursor.getColumnIndex(CardDataEntry.CARDTYPE));
            offerImage = mCursor.getString(mCursor.getColumnIndex(CardDataEntry.OFFER_IMAGE));
            String isAccepted = mCursor.getString(mCursor.getColumnIndex(CardDataEntry.IS_ACCEPTED));
            mBinding.tvUserName.setText(userName);
            mBinding.tvActivity.setText(activity);
            mBinding.tvCardType.setText(cardType);
            if (isAccepted.equals(CommonStaticKeys.ACCEPTED)) {
                mBinding.tvIsaccepted.setText(R.string.accepted);
                mBinding.tvIsaccepted.setTextColor(getResources().getColor(R.color.green));
            } else {
                mBinding.tvIsaccepted.setText(R.string.pending);
                mBinding.tvIsaccepted.setTextColor(getResources().getColor(R.color.red));
            }
           // Glide.with((FragmentActivity) this).load(photoLink).into(mBinding.imgPhoto);
            Picasso.with(this).load(photoLink).into(mBinding.imgPhoto);

            if (!offerImage.equals(getString(R.string.dp_notavailble))) {
               // Glide.with((FragmentActivity) this).load(offerImage).into(mBinding.imgOfferImage);
                Picasso.with(this).load(offerImage).into(mBinding.imgOfferImage);
            } else if (offerImage.equals(getString(R.string.dp_notavailble)) && cardType.equals(SPECIAL_CARD)) {
                mBinding.tvNoOffer.setVisibility(View.VISIBLE);
                mBinding.tvNoOffer.setText(getString(R.string.you_dont_have_offer_yet));
                mBinding.offerLayout.setVisibility(View.GONE);
            } else {
                mBinding.tvNoOffer.setVisibility(View.VISIBLE);
                mBinding.tvNoOffer.setText(getString(R.string.you_dont_have_specialcard));
                mBinding.offerLayout.setVisibility(View.GONE);
            }
        }
        mBinding.btDeleteCard.setOnClickListener(this);
        mBinding.btDeleteOffer.setOnClickListener(this);
        mBinding.linear.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_deleteCard:
                if (checkConnection()) {
                    DataBaseUtilies.deleteCard(uId, this, photoLink, offerImage);
                    finish();
                    return;
                }
                return;
            case R.id.bt_deleteOffer:
                if (checkConnection()) {
                    DataBaseUtilies.deleteOffer(uId, offerImage, this);
                    mBinding.offerLayout.setVisibility(View.GONE);
                    mBinding.tvNoOffer.setVisibility(View.VISIBLE);
                    mBinding.tvNoOffer.setText(getString(R.string.you_dont_have_offer_yet));
                    return;
                }
                return;
            case R.id.linear:
                Intent intent;
                if (cardType.equals(SPECIAL_CARD)) {
                    intent = new Intent(this, SpecialCardActivity.class);
                    intent.putExtra(SPECIAL_CARD, uId);
                    startActivity(intent);
                    return;
                } else if (cardType.equals(NORMAL_CARD)) {
                    intent = new Intent(this, NormalCardActvitiy.class);
                    intent.putExtra(NORMAL_CARD, uId);
                    startActivity(intent);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    private boolean checkConnection() {
        if (CheckConnection.isOnline(this)) {
            return true;
        }
        KToast.warningToast(this, getString(R.string.dissconnection_message), 80, KToast.LENGTH_LONG);
        return false;
    }
}

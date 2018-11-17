package com.dev.mohamed.samacard.addCard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dev.mohamed.samacard.CommonStaticKeys;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.addCard.AddNormalCardFragment.AddNormalCard;
import com.dev.mohamed.samacard.addCard.AddSpecialCardFragment.AddSpecialCard;
import com.dev.mohamed.samacard.addCard.ChooseCardFragment.Choosecard;
import com.dev.mohamed.samacard.auth.AuthinticationActivity;
import com.dev.mohamed.samacard.connection.CheckConnection;
import com.dev.mohamed.samacard.contentProvider.CardsContentProvider;
import com.dev.mohamed.samacard.databinding.FragmentAddNormalcardBinding;
import com.dev.mohamed.samacard.databinding.FragmentAddOfferBinding;
import com.dev.mohamed.samacard.databinding.FragmentAddSpecialcardBinding;
import com.dev.mohamed.samacard.fireBase.DataBaseUtilies;
import com.dev.mohamed.samacard.fireBase.StorageUtilies;
import com.dev.mohamed.samacard.fireBase.StorageUtilies.OnImageUploaded;
import com.dev.mohamed.samacard.user.UserCardData;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static com.dev.mohamed.samacard.CommonStaticKeys.ADMIN_EMAIL;
import static com.dev.mohamed.samacard.CommonStaticKeys.NOT_ACCEPTED;

public class AddCardActivity extends AppCompatActivity implements Choosecard, AddNormalCard, AddSpecialCard, OnClickListener, OnImageUploaded, AddOfferFragment.OnAddOffer {



    private static final int RC_OFFERPICK = 3;
    private static final int RC_LOGOPICK = 2;
    private static final int RC_NORMALLOGOPICK = 1;

    private Uri imageLocalUrl;
    FragmentAddOfferBinding mOfferBinding;
    private FragmentManager manager;
    FragmentAddNormalcardBinding normalcardBinding;
    private Uri offerImageUrl;
    FragmentAddSpecialcardBinding specialCardBinding;
    private UserCardData userData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcard);
        setFinishOnTouchOutside(false);
        userData = getIntent().getParcelableExtra(CommonStaticKeys.USER_DATA_KEY);

        //Check if the user picked an avatar or not ,case PICKED uplaoad it and get link ,CASE NOT PICKED user app logo as avatar
        if (userData.getPhotoLink() != null) {
            imageLocalUrl = Uri.parse(userData.getPhotoLink());
        } else {
            imageLocalUrl = Uri.parse(CommonStaticKeys.APP_LOGO);
        }

        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.addCard_container, new ChooseCardFragment()).commit();
    }

    public void cardType(View view) {
        view.findViewById(R.id.img_specialcard).setOnClickListener(this);
        view.findViewById(R.id.img_normalCard).setOnClickListener(this);
        view.findViewById(R.id.img_specialIllustrat).setOnClickListener(this);
        ImageView offer = view.findViewById(R.id.img_offer);
        TextView tvOffer = view.findViewById(R.id.tv_offer);
        LinearLayout offerspliter = view.findViewById(R.id.offer_spliter);

        //CHECK IF THE USER HAVE A SPECIAL CARD TO SHOW THE OFFER ADD OPTION
        if (!CardsContentProvider.isHaveSpecialcard(this, userData.getUserId()) || userData.getEmail().equals(CommonStaticKeys.ADMIN_EMAIL) || CardsContentProvider.isHaveOffer(this, userData.getUserId())) {
            offer.setVisibility(View.GONE);
            tvOffer.setVisibility(View.GONE);
            offerspliter.setVisibility(View.GONE);
            return;
        }
        offer.setVisibility(View.VISIBLE);
        offer.setOnClickListener(this);
        tvOffer.setVisibility(View.VISIBLE);
        offerspliter.setVisibility(View.VISIBLE);
    }

    //HANDLE LISNERS AT NORMAL CARD ADD
    public void normalCard(FragmentAddNormalcardBinding binding) {
        normalcardBinding = binding;
        binding.btCancel.setOnClickListener(this);
        binding.btNormaDone.setOnClickListener(this);
        binding.imgNormaLogo.setOnClickListener(this);
        binding.edUserName.setText(userData.getUserName());
        Glide.with(this).load(imageLocalUrl).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.ic_error)).into(binding.imgNormaLogo);
    }

    //HANDLE LISNERS AT SPECIAL CARD ADD
    public void specialCard(FragmentAddSpecialcardBinding binding) {
        specialCardBinding = binding;
        binding.btCancel.setOnClickListener(this);
        binding.btDone.setOnClickListener(this);
        binding.imgLogo.setOnClickListener(this);
        binding.edUserName.setText(userData.getUserName());
        binding.edEmail.setText(userData.getEmail());
        Glide.with(this).load(imageLocalUrl).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.ic_error)).into(specialCardBinding.imgLogo);
    }

    //HANDLE LISNERS AT OFFER  ADD
    public void addOffer(FragmentAddOfferBinding binding) {
        mOfferBinding = binding;
        mOfferBinding.btAddOffer.setOnClickListener(this);
        mOfferBinding.btCancel.setOnClickListener(this);
        mOfferBinding.btAcceptOffer.setOnClickListener(this);
        mOfferBinding.imgOffer.setOnClickListener(this);
    }

    //APPLY ACTTION AT EVERY LISNER DECLARED ABOVE
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_acceptOffer:
                if (offerImageUrl != null) {
                    StorageUtilies.getOfferImageUrl(this, offerImageUrl, this, manager);
                    return;
                } else {
                    Toast.makeText(this, R.string.offer_nullwarning_message, Toast.LENGTH_SHORT).show();
                    return;
                }
            case R.id.bt_addOffer:
                Intent intentoffer = new Intent("android.intent.action.GET_CONTENT");
                intentoffer.setType("image/*");
                intentoffer.putExtra("android.intent.extra.LOCAL_ONLY", true);
                startActivityForResult(Intent.createChooser(intentoffer, "Choose Image Using"), 3);
                return;
            case R.id.bt_cancel:
                finish();
                return;
            case R.id.bt_done:
                if (!CheckConnection.isOnline(this)) {
                    Toast.makeText(this, R.string.dissconnection_message, Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isRecommendedFiledsNull(CommonStaticKeys.SPECIAL_CARD)) {
                    StorageUtilies.getImageDonloadUrl(CommonStaticKeys.SPECIAL_CARD, this, imageLocalUrl, this, manager);
                    return;
                } else {
                    return;
                }
            case R.id.bt_normaDone:
                if (!CheckConnection.isOnline(this)) {
                    Toast.makeText(this, R.string.dissconnection_message, Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isRecommendedFiledsNull(CommonStaticKeys.NORMAL_CARD)) {
                    StorageUtilies.getImageDonloadUrl(CommonStaticKeys.NORMAL_CARD, this, imageLocalUrl, this, manager);
                    return;
                } else {
                    return;
                }
            case R.id.img_logo:
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                intent.putExtra("android.intent.extra.LOCAL_ONLY", true);
                startActivityForResult(Intent.createChooser(intent, "Choose Image Using"), 2);
                return;
            case R.id.img_normaLogo:
                Intent intentnormal = new Intent("android.intent.action.GET_CONTENT");
                intentnormal.setType("image/*");
                intentnormal.putExtra("android.intent.extra.LOCAL_ONLY", true);
                startActivityForResult(Intent.createChooser(intentnormal, "Choose Image Using"), 1);
                return;
            case R.id.img_normalCard:
                manager.beginTransaction().replace(R.id.addCard_container, new AddNormalCardFragment()).commit();
                return;
            case R.id.img_offer:
                manager.beginTransaction().replace(R.id.addCard_container, new AddOfferFragment()).commit();
                return;
            case R.id.img_specialIllustrat:
                manager.beginTransaction().replace(R.id.addCard_container, new SpecialCardIllustrateFragment()).commit();
                return;
            case R.id.img_specialcard:
                manager.beginTransaction().replace(R.id.addCard_container, new AddSpecialCardFragment()).commit();
                return;
            default:
                return;
        }
    }


    //AFTER PICK AN AVATAR GET THE PATH OF IMAGE AND DISPLAY IT
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOGOPICK && resultCode == RESULT_OK) {
            imageLocalUrl = data.getData();
            Glide.with(this).load(imageLocalUrl).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.ic_error)).into(specialCardBinding.imgLogo);
        }
        if (requestCode == RC_NORMALLOGOPICK && resultCode == RESULT_OK) {
            imageLocalUrl = data.getData();
            Glide.with(this).load(imageLocalUrl).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.ic_error)).into(normalcardBinding.imgNormaLogo);
        }
        //OFFER IMAGE PICK
        if (requestCode == RC_OFFERPICK && resultCode == RESULT_OK) {
            offerImageUrl = data.getData();
            mOfferBinding.imgOffer.setVisibility(View.VISIBLE);
            mOfferBinding.btAddOffer.setVisibility(View.GONE);
            Glide.with(this).load(offerImageUrl).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.ic_error)).into(mOfferBinding.imgOffer);
        }
    }

    //GET THE TEXT IN THE EDITE TEXT
    private String getEditeTextData(EditText editText) {
        String data = editText.getText().toString();
        if (!data.isEmpty()) {
            return data;
        }
        editText.setHintTextColor(getResources().getColor(R.color.red));
        return null;
    }
    //MANAGE THE OPTIONAL DATA SET
    private String getOptinalData(EditText editText) {
        String data = editText.getText().toString();
        if (data.isEmpty()) {
            return getString(R.string.not_available);
        }
        return data;
    }

    private String getExpiredDate() {
        SimpleDateFormat currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        SimpleDateFormat dayAndMonthe = new SimpleDateFormat("MM/dd", Locale.ENGLISH);
        String fulldate = String.valueOf(Integer.parseInt(currentYear.format(new Date())) + 1) + "/" + dayAndMonthe.format(new Date());

        return fulldate;
    }

    private String getSpinnerData(Spinner spinner) {
        String data = spinner.getSelectedItem().toString();
        if (spinner.getSelectedItemPosition() != 0) {
            return data;
        }
        spinner.setBackgroundColor(getResources().getColor(R.color.lightRed));
        return null;
    }


    //AFTER THE IMAGE LOAD IN FIREBASE STORAGE AND RETURNED THE URL ,ADDING THE URL AND OTHE CARD DATA TO DB
    public void loaded(String photoLink, String cardType) {
        String expierdDate;
        if (cardType.equals(CommonStaticKeys.SPECIAL_CARD)) {
            String name = getEditeTextData(specialCardBinding.edUserName);
            String adress = getEditeTextData(specialCardBinding.edAdress);
            String officialNum = getEditeTextData(specialCardBinding.edOfficialNum);
            String activiy = getSpinnerData(specialCardBinding.spActivity);
            String email = getEditeTextData(specialCardBinding.edEmail);
            String country = getSpinnerData(specialCardBinding.spCountry);
            String governorate = getSpinnerData(specialCardBinding.spGovernorate);
            expierdDate = getExpiredDate();
            String companyName = getOptinalData(specialCardBinding.edCompanyName);
            String positionName = getOptinalData(specialCardBinding.edPositionName);
            String directNum = getOptinalData(specialCardBinding.edDirectNum);
            String aboutActiviy = getOptinalData(specialCardBinding.edAboutActiviy);
            String faceBookPage = getOptinalData(specialCardBinding.edFacebookPage);
            String webSite = getOptinalData(specialCardBinding.edWebSite);
            String whatsApp = getOptinalData(specialCardBinding.whatsapp);
            if (!CheckConnection.isOnline(this)) {
                Toast.makeText(this, R.string.no_connection_trying_to_reconnect, Toast.LENGTH_SHORT).show();
            } else if (!isRecommendedFiledsNull(cardType)) {
                UserCardData cardData = new UserCardData(userData.getUserId(), name, companyName, positionName, adress, officialNum, directNum, activiy, aboutActiviy, email, faceBookPage, webSite, whatsApp, country, governorate, photoLink, expierdDate, getString(R.string.dp_notavailble), cardType, NOT_ACCEPTED);
                if (userData.getEmail().equals(ADMIN_EMAIL)) {
                    cardData.setUserId(String.valueOf(new Random().nextInt()));
                }
                DataBaseUtilies.insert(cardData, this, manager);
            }
        }
        if (cardType.equals(CommonStaticKeys.NORMAL_CARD)) {
            String name1 = getEditeTextData(normalcardBinding.edUserName);
            String adress1 = getEditeTextData(normalcardBinding.edAdress);
            String officialNum1 = getEditeTextData(normalcardBinding.edOfficialNum);
            String activiy1 = getSpinnerData(normalcardBinding.spActivity);
            String country1 = getSpinnerData(normalcardBinding.spCountry);
            String governorate1 = getSpinnerData(normalcardBinding.spGovernorate);
            expierdDate = getExpiredDate();
            String companyName1 = getOptinalData(normalcardBinding.edCompanyName);
            if (!CheckConnection.isOnline(this)) {
                Toast.makeText(this, R.string.no_connection_trying_to_reconnect, Toast.LENGTH_SHORT).show();
            } else if (!isRecommendedFiledsNull(cardType)) {
                UserCardData userCardData = new UserCardData(name1, companyName1, adress1, officialNum1, activiy1, country1, governorate1, cardType, photoLink, expierdDate, getString(R.string.dp_notavailble), userData.getUserId(), NOT_ACCEPTED);
                if (userData.getEmail().equals(ADMIN_EMAIL)) {
                    userCardData.setUserId(String.valueOf(new Random().nextInt()));
                }
                DataBaseUtilies.insert(userCardData, this, manager);
            }
        }
    }

    //AFTER UPLOADING THE OFFER IMAGE TO FIREBASE STORAGE AND RETURNED URL , ADD URL TO DB
    public void offerLoaded(String url) {
        DataBaseUtilies.updateOffer(userData.getUserId(), url, this, manager);
    }

    //CHECK IF THE USER LET A FIELD NULL AND WORN HIM
    private boolean isRecommendedFiledsNull(String cardType) {
        if (cardType.equals(CommonStaticKeys.NORMAL_CARD)) {
            String name = getEditeTextData(specialCardBinding.edUserName);
            String adress = getEditeTextData(specialCardBinding.edAdress);
            String officialNum = getEditeTextData(specialCardBinding.edOfficialNum);
            String activiy = getSpinnerData(specialCardBinding.spActivity);
            String email = getEditeTextData(specialCardBinding.edEmail);
            String country = getSpinnerData(specialCardBinding.spCountry);
            String governorate = getSpinnerData(specialCardBinding.spGovernorate);
            if (name != null && adress != null && officialNum != null && country != null && activiy != null && email != null && governorate != null) {
                return false;
            }
            Toast.makeText(this, R.string.null_recommended_message, Toast.LENGTH_LONG).show();
            return true;
        }
        if (!cardType.equals(CommonStaticKeys.NORMAL_CARD)) {
            return true;
        }
        String name1 = getEditeTextData(normalcardBinding.edUserName);
        String adress1 = getEditeTextData(normalcardBinding.edAdress);
        String officialNum1 = getEditeTextData(normalcardBinding.edOfficialNum);
        String activiy1 = getSpinnerData(normalcardBinding.spActivity);
        String country1 = getSpinnerData(normalcardBinding.spCountry);
        String governorate1 = getSpinnerData(normalcardBinding.spGovernorate);
        if (name1 != null && adress1 != null && officialNum1 != null && country1 != null && activiy1 != null && governorate1 != null) {
            return false;
        }
        Toast.makeText(this, R.string.null_recommended_message, Toast.LENGTH_LONG).show();
        return true;
    }
}

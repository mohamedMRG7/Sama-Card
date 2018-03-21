package com.dev.mohamed.samacard.addCard;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.auth.AuthinticationActivity;
import com.dev.mohamed.samacard.databinding.FragmentAddSpecialcardBinding;
import com.dev.mohamed.samacard.fireBase.DataBaseUtilies;
import com.dev.mohamed.samacard.fireBase.StorageUtilies;
import com.dev.mohamed.samacard.user.UserCardData;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AddCardActivity extends AppCompatActivity implements ChooseCardFragment.Choosecard,
                                                                    AddNormalCardFragment.AddNormalCard,
                                                                                    AddSpecialCardFragment.AddSpecialCard,View.OnClickListener,
                                                                                                                  StorageUtilies.OnImageUploaded  {

    private FragmentManager manager;
    private FragmentAddSpecialcardBinding specialCardBinding;

    private int RC_LOGOPICK=2;

    Uri imageLocalUrl;
    public UserCardData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addcard);
        this.setFinishOnTouchOutside(false);

        userData =getIntent().getParcelableExtra(AuthinticationActivity.USER_DATA_KEY);

        if (userData.getPhotoLink()!=null)
        imageLocalUrl= Uri.parse(userData.getPhotoLink());
        else imageLocalUrl= Uri.parse("logo");



        manager=getSupportFragmentManager();

        ChooseCardFragment chooseCardFragment=new ChooseCardFragment();

        manager.beginTransaction().add(R.id.addCard_container,chooseCardFragment).commit();



    }

    @Override
    public void cardType(View view) {


        ImageView specialcard=view.findViewById(R.id.img_specialcard);
        specialcard.setOnClickListener(this);
        ImageView normalCard=view.findViewById(R.id.img_normalCard);
        normalCard.setOnClickListener(this);
        ImageView cardIllurstarion=view.findViewById(R.id.img_specialIllustrat);
        cardIllurstarion.setOnClickListener(this);
    }

    @Override
    public void normalCard(View view) {

    }

    @Override
    public void specialCard(FragmentAddSpecialcardBinding binding) {

        specialCardBinding =binding;
        binding.btCancel.setOnClickListener(this);
        binding.btDone.setOnClickListener(this);
        binding.imgLogo.setOnClickListener(this);
        binding.edUserName.setText(userData.getUserName());
        binding.edEmail.setText(userData.getEmail());

        Picasso.with(getApplicationContext()).load(imageLocalUrl).into(specialCardBinding.imgLogo);
    }


    @Override
    public void onClick(View view) {
        int id=view.getId();

        switch (id)
        {
            case R.id.img_specialcard:
                AddSpecialCardFragment addSpecialCardFragment=new AddSpecialCardFragment();
                manager.beginTransaction().replace(R.id.addCard_container,addSpecialCardFragment).commit();
                break;

            case R.id.img_normalCard:
               AddNormalCardFragment addNormalCardFragment=new AddNormalCardFragment();
               manager.beginTransaction().replace(R.id.addCard_container,addNormalCardFragment).commit();
               break;

            case R.id.img_specialIllustrat:
                SpecialCardIllustrateFragment specialCardIllustrateFragment=new SpecialCardIllustrateFragment();
                manager.beginTransaction().replace(R.id.addCard_container,specialCardIllustrateFragment).commit();
                break;

            case R.id.img_logo:
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent,"Choose Image Using"),RC_LOGOPICK);
                break;

            case R.id.bt_done:

                if (!isRecommendedFiledsNull())
                StorageUtilies.getImageDonloadUrl(this,imageLocalUrl,this);

                break;

            case R.id.bt_cancel:

                finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RC_LOGOPICK)
          if ( resultCode==RESULT_OK)
            {
                imageLocalUrl=data.getData();
                Picasso.with(getApplicationContext()).load(imageLocalUrl).into(specialCardBinding.imgLogo);

               // Log.e("data",photoLink);

            }
    }





    private String getEditeTextData(EditText editText)
    {
        String data=editText.getText().toString();
        if (data.isEmpty())
        {
            editText.setHintTextColor(getResources().getColor(R.color.red));
            return null;
        }
        else
            {


                return data;
            }
    }


    private String getOptinalData(EditText editText)
    {
        String data=editText.getText().toString();
        if (data.isEmpty())
        {
            return getString(R.string.not_available);
        }
        else
        {
            return data;
        }

    }

    private String getSpinnerData(Spinner spinner)
    {
        String data=spinner.getSelectedItem().toString();
        int position=spinner.getSelectedItemPosition();

        if (position==0)
        {
            spinner.setBackgroundColor(getResources().getColor(R.color.lightRed));

            return null;
        }
        else return data;

    }

    @Override
    public void loaded(String photoLink) {

        //recommended data set

        String name=getEditeTextData(specialCardBinding.edUserName);
        String adress=getEditeTextData(specialCardBinding.edAdress);
        String officialNum=getEditeTextData(specialCardBinding.edOfficialNum);
        String activiy=getSpinnerData(specialCardBinding.spActivity);
        String email=getEditeTextData(specialCardBinding.edEmail);
        String country=getSpinnerData(specialCardBinding.spCountry);
        String governorate=getSpinnerData(specialCardBinding.spGovernorate);
        String cardType=getString(R.string.special_type);

        //optional data set
        String companyName=getOptinalData(specialCardBinding.edCompanyName);
        String positionName=getOptinalData(specialCardBinding.edPositionName);
        String directNum=getOptinalData(specialCardBinding.edDirectNum);
        String aboutActiviy=getOptinalData(specialCardBinding.edAboutActiviy);
        String faceBookPage=getOptinalData(specialCardBinding.edFacebookPage);
        String webSite=getOptinalData(specialCardBinding.edWebSite);
        String whatsApp=getOptinalData(specialCardBinding.whatsapp);


       if (!isRecommendedFiledsNull())
        {
            UserCardData cardData=new UserCardData(userData.getUserId(),name,companyName,positionName,adress,officialNum,directNum,activiy,aboutActiviy,email,
                    faceBookPage,webSite,whatsApp,country,governorate,photoLink,cardType);

            DataBaseUtilies.insert(cardData,this);






        }

    }


    public boolean isRecommendedFiledsNull()
    {
        String name=getEditeTextData(specialCardBinding.edUserName);
        String adress=getEditeTextData(specialCardBinding.edAdress);
        String officialNum=getEditeTextData(specialCardBinding.edOfficialNum);
        String activiy=getSpinnerData(specialCardBinding.spActivity);
        String email=getEditeTextData(specialCardBinding.edEmail);
        String country=getSpinnerData(specialCardBinding.spCountry);
        String governorate=getSpinnerData(specialCardBinding.spGovernorate);



        if (name==null || adress==null || officialNum==null ||country==null || activiy==null|| email==null|| governorate==null) {
            Toast.makeText(this, "Please Enter All Recommended Fields", Toast.LENGTH_LONG).show();
            return true;
        }
        else return false;

        }
}

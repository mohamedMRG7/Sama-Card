package com.dev.mohamed.samacard.addCard;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.databinding.FragmentAddSpecialcardBinding;
import com.dev.mohamed.samacard.user.UserCardData;

public class AddCardActivity extends AppCompatActivity implements ChooseCardFragment.Choosecard,
                                                                    AddNormalCardFragment.AddNormalCard,
                                                                                    AddSpecialCardFragment.AddSpecialCard,View.OnClickListener{

    private FragmentManager manager;
    private FragmentAddSpecialcardBinding specialCardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addcard);
        this.setFinishOnTouchOutside(false);


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

            case R.id.bt_done:

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


                if (name==null || adress==null || officialNum==null ||country==null || activiy==null|| email==null|| governorate==null)
                {
                    Toast.makeText(this,"Please Enter All Recommended Fields",Toast.LENGTH_LONG).show();

                }else
                {
                    Toast.makeText(this,"Card Added",Toast.LENGTH_LONG).show();
                    UserCardData cardData=new UserCardData("",name,companyName,positionName,adress,officialNum,directNum,activiy,aboutActiviy,email,
                            faceBookPage,webSite,whatsApp,country,governorate,"photolinke",cardType);
                }

                break;

            case R.id.bt_cancel:
                finish();
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
}

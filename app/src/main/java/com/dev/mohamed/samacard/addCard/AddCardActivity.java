package com.dev.mohamed.samacard.addCard;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dev.mohamed.samacard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddCardActivity extends AppCompatActivity implements ChooseCardFragment.Choosecard,
                                                                    AddNormalCardFragment.AddNormalCard,
                                                                                    AddSpecialCardFragment.AddSpecialCard,View.OnClickListener{

    FragmentManager manager;


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
    public void specialCard(View view) {

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

        }
    }
}

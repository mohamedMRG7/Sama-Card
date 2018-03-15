package com.dev.mohamed.samacard.addCard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dev.mohamed.samacard.R;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_choose_cardtype);
        this.setFinishOnTouchOutside(false);


    }
}

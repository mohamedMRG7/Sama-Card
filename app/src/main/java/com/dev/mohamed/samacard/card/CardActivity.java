package com.dev.mohamed.samacard.card;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.dev.mohamed.samacard.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CardActivity extends AppCompatActivity {

    @BindView(R.id.img_logo)ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        ButterKnife.bind(this);
        Picasso.with(this).load("https://scontent-cai1-1.xx.fbcdn.net/v/t1.0-9/18485817_1741335819215943_5407189630743605678_n.jpg?oh=3aefc25f3cddfbf88f01128d6d5f4e05&oe=5B3BE923").into(logo);

    }
}

package com.dev.mohamed.samacard;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.mohamed.samacard.auth.AuthinticationActivity;
import com.dev.mohamed.samacard.connection.CheckConnection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplachScreenActivity extends AppCompatActivity {

    @BindView(R.id.tv_splach)
    TextView tvSplach;
    @BindView(R.id.imageView2)
    ImageView imgLogo;
    @BindView(R.id.pb_noconnection)
    ProgressBar pbNoConnectoin;
    @BindView(R.id.tv_no_connection)
    TextView tvNoconnection;

    ObjectAnimator noConnAnimator;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach_screen);
        ButterKnife.bind(this);

        handler = new Handler();
        noConnAnimator = ObjectAnimator.ofInt(tvNoconnection, "textColor", Color.RED);
        noConnAnimator.setDuration(1000);


        ObjectAnimator logoAnimator = ObjectAnimator.ofFloat(tvSplach, "alpha", 0f, 1f);
        logoAnimator.setDuration(3000);
        logoAnimator.start();

        logoAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }


            @Override
            public void onAnimationEnd(Animator animator) {
                if (CheckConnection.isOnline(SplachScreenActivity.this)) {
                    startActivity(new Intent(SplachScreenActivity.this, AuthinticationActivity.class));
                } else {
                    tvNoconnection.setAlpha(1);
                    pbNoConnectoin.setAlpha(1);
                    checConnection();

                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


        ObjectAnimator messageAnimator = ObjectAnimator.ofFloat(imgLogo, "translationX", -300, 0);
        messageAnimator.setDuration(1000);
        messageAnimator.start();

        messageAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    private void checConnection() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (CheckConnection.isOnline(SplachScreenActivity.this))
                    startActivity(new Intent(SplachScreenActivity.this, AuthinticationActivity.class));
                else {

                    noConnAnimator.start();
                    checConnection();
                }

            }
        }, 3000);

    }
}

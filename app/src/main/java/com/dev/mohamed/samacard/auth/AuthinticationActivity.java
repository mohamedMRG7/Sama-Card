package com.dev.mohamed.samacard.auth;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.MainActivity;

import com.dev.mohamed.samacard.databinding.ActivityAuthBinding;
import com.dev.mohamed.samacard.user.UserCardData;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.firebase.ui.auth.AuthUI.IdpConfig.Builder;
import com.firebase.ui.auth.AuthUI.SignInIntentBuilder;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration;
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener;
import com.zplesac.connectionbuddy.models.ConnectivityEvent;
import com.zplesac.connectionbuddy.models.ConnectivityState;

import java.io.Serializable;
import java.util.Arrays;

import static com.dev.mohamed.samacard.CommonStaticKeys.APP_LOGO;
import static com.dev.mohamed.samacard.CommonStaticKeys.USER_DATA_KEY;

public class AuthinticationActivity extends AppCompatActivity implements Serializable, ConnectivityChangeListener {
    private static final int RC_SIGN_IN = 1;
    private AuthStateListener authStateListener;
    ActivityAuthBinding binding;
    private FirebaseAuth firebaseAuth;
    private boolean isConnected = false;


        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Intent intent = new Intent(AuthinticationActivity.this, MainActivity.class);
                UserCardData cardData = new UserCardData();
                cardData.setEmail(user.getEmail());
                havePhoto(cardData, user);
                cardData.setUserId(user.getUid());
                cardData.setUserName(user.getDisplayName());
                intent.putExtra(USER_DATA_KEY, cardData);
                startActivity(intent);
                finish();
                return;
            }
           startActivityForResult(((SignInIntentBuilder) ((SignInIntentBuilder) AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false)).setTheme(R.style.CardThem)).setLogo(R.drawable.logo)
                   .setAvailableProviders(Arrays.asList(new Builder(EmailAuthProvider.PROVIDER_ID).build(), new Builder(AuthUI.GOOGLE_PROVIDER).build(), new Builder(AuthUI.FACEBOOK_PROVIDER).build())).build(), 1);
        }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        firebaseAuth = FirebaseAuth.getInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        sign_In();
        ConnectionBuddy.getInstance().init(new ConnectionBuddyConfiguration.Builder(this).build());
    }

    private void sign_In() {
        authStateListener.onAuthStateChanged(firebaseAuth);
    }

    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    private void havePhoto(UserCardData data, FirebaseUser user) {
        try {
            data.setPhotoLink(user.getPhotoUrl().toString());
        } catch (Exception e) {
            data.setPhotoLink(APP_LOGO);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 1) {
            return;
        }
        if (resultCode == -1) {
            try {
                FirebaseUser firebaseuser = firebaseAuth.getCurrentUser();
                Intent intent = new Intent(this, MainActivity.class);
                UserCardData cardData = new UserCardData();
                cardData.setEmail(firebaseuser.getEmail());
                havePhoto(cardData, firebaseuser);
                cardData.setUserId(firebaseuser.getUid());
                cardData.setUserName(firebaseuser.getDisplayName());
                intent.putExtra(USER_DATA_KEY, cardData);
                startActivity(intent);
                finish();
            } catch (Exception e) {
            }
        } else if (resultCode == 0 && isConnected) {
            finish();
        }
    }

    protected void onStart() {
        super.onStart();
        ConnectionBuddy.getInstance().registerForConnectivityEvents(this, this);
        if (isConnected) {
            binding.imgDisconnect.setVisibility(View.GONE);
            binding.imgMessage.setVisibility(View.GONE);
            binding.pbLoading.setVisibility(View.GONE);
            return;
        }
        binding.imgDisconnect.setVisibility(View.VISIBLE);
        binding.imgMessage.setVisibility(View.VISIBLE);
        binding.pbLoading.setVisibility(View.VISIBLE);

    }

    protected void onStop() {
        super.onStop();
        ConnectionBuddy.getInstance().unregisterFromConnectivityEvents(this);
    }

    public void onConnectionChange(ConnectivityEvent event) {
        if (event.getState().getValue() == ConnectivityState.CONNECTED) {
            isConnected = true;
        } else if (event.getState().getValue() == ConnectivityState.DISCONNECTED) {
            isConnected = false;
        }
    }
}

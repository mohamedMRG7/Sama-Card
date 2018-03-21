package com.dev.mohamed.samacard.auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.mohamed.samacard.user.UserCardData;
import com.firebase.ui.auth.AuthUI;

import com.dev.mohamed.samacard.MainActivity;
import com.dev.mohamed.samacard.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration;
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener;
import com.zplesac.connectionbuddy.models.ConnectivityEvent;
import com.zplesac.connectionbuddy.models.ConnectivityState;

import java.io.Serializable;
import java.util.Arrays;

import timber.log.Timber;

public class AuthinticationActivity extends AppCompatActivity implements Serializable, ConnectivityChangeListener {

    private FirebaseAuth.AuthStateListener authStateListener;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth firebaseAuth;

    FirebaseUser firebaseuser;
    public static final String USER_DATA_KEY="userdata";

    private boolean isConnected=false;
    private boolean trytoOut=false;
    TextView connectiontxt;
    ImageView connectionIamge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        firebaseAuth=FirebaseAuth.getInstance();

        sign_In();
        ConnectionBuddyConfiguration networkInspectorConfiguration = new ConnectionBuddyConfiguration.Builder(this).build();
        ConnectionBuddy.getInstance().init(networkInspectorConfiguration);
       // connectiontxt=findViewById(R.id.tv_noconnection);
       // connectionIamge=findViewById(R.id.img_connection_logo);

    }

    public void sign_In()
    {
        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();

                if(user !=null)
                {


                    Intent intent=new Intent(AuthinticationActivity.this,MainActivity.class);
                    UserCardData cardData=new UserCardData();
                    cardData.setEmail(user.getEmail());
                    cardData.setPhotoLink(user.getPhotoUrl().toString());
                    cardData.setUserId(user.getUid());
                    cardData.setUserName(user.getDisplayName());
                    intent.putExtra(USER_DATA_KEY,cardData);
                    startActivity(intent);
                    finish();
                }else
                {

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    //.setTheme(R.style.splaceThem)
                                    //.setLogo(R.drawable.logo)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }


    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==1)
            if (resultCode==RESULT_OK)
            {

                firebaseuser=firebaseAuth.getCurrentUser();

                Intent intent=new Intent(AuthinticationActivity.this,MainActivity.class);
                UserCardData cardData=new UserCardData();
                cardData.setEmail(firebaseuser.getEmail());
                cardData.setPhotoLink(firebaseuser.getPhotoUrl().toString());
                cardData.setUserId(firebaseuser.getUid());
                cardData.setUserName(firebaseuser.getDisplayName());
                intent.putExtra(USER_DATA_KEY,cardData);

                startActivity(intent);
                finish();

                //i will send the userjoke with data but will make it seriazable first and modify constractor
            }else if (resultCode==RESULT_CANCELED)  if (isConnected==true)finish();




    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectionBuddy.getInstance().registerForConnectivityEvents(this, this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("ONCreat","onRestart");

    }


    @Override
    protected void onStop() {
        super.onStop();
        ConnectionBuddy.getInstance().unregisterFromConnectivityEvents(this);


    }

    @Override
    public void onConnectionChange(ConnectivityEvent event) {
       if(event.getState().getValue()== ConnectivityState.CONNECTED)
       {
           // connectiontxt.setVisibility(View.INVISIBLE);
           // connectionIamge.setVisibility(View.INVISIBLE);
            isConnected=true;
       }else if (event.getState().getValue()==ConnectivityState.DISCONNECTED)
       {
          // connectiontxt.setVisibility(View.VISIBLE);
          // connectionIamge.setVisibility(View.VISIBLE);
           isConnected=false;
       }
    }
}

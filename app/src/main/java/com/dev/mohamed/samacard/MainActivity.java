package com.dev.mohamed.samacard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import com.dev.mohamed.samacard.adapters.MainRecyclerAdapter;
import com.dev.mohamed.samacard.addCard.AddCardActivity;
import com.dev.mohamed.samacard.ads.FragmentAnnoncement;
import com.dev.mohamed.samacard.ads.FragmentAnnoncement.AnnoncementAction;
import com.dev.mohamed.samacard.ads.FragmentOfferAnoncement;
import com.dev.mohamed.samacard.auth.AuthinticationActivity;
import com.dev.mohamed.samacard.card.SpecialCardActivity;
import com.dev.mohamed.samacard.chat.LocalDbUtalis;
import com.dev.mohamed.samacard.chat.MessagesListActivity;
import com.dev.mohamed.samacard.contentProvider.CardsContentProvider;
import com.dev.mohamed.samacard.contentProvider.ContentProviderContract.CardEntry;
import com.dev.mohamed.samacard.fireBase.DataBaseUtilies;

import com.dev.mohamed.samacard.search.FragmentSearch;
import com.dev.mohamed.samacard.search.FragmentSearch.OnSearch;
import com.dev.mohamed.samacard.search.SpiceficUsersActivity;
import com.dev.mohamed.samacard.sqliteDb.DbContract.CardDataEntry;
import com.dev.mohamed.samacard.user.MyCardAndOffersActivity;
import com.dev.mohamed.samacard.user.UserCardData;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.onurkaganaldemir.ktoastlib.KToast;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener;
import com.zplesac.connectionbuddy.models.ConnectivityEvent;
import com.zplesac.connectionbuddy.models.ConnectivityState;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.dev.mohamed.samacard.CommonStaticKeys.ADMIN_EMAIL;
import static com.dev.mohamed.samacard.CommonStaticKeys.KEY_ACTIVITY;
import static com.dev.mohamed.samacard.CommonStaticKeys.KEY_AREA;
import static com.dev.mohamed.samacard.CommonStaticKeys.KEY_GOVERNORATE;
import static com.dev.mohamed.samacard.CommonStaticKeys.KEY_UID;
import static com.dev.mohamed.samacard.CommonStaticKeys.NOT_ACCEPTED;
import static com.dev.mohamed.samacard.CommonStaticKeys.SPECIAL_CARD;
import static com.dev.mohamed.samacard.CommonStaticKeys.USER_DATA_KEY;

public class MainActivity extends AppCompatActivity implements DataBaseUtilies.onResiveData, OnSearch, AnnoncementAction, ConnectivityChangeListener, LoaderCallbacks<Cursor> {
    private static final String KEY_ISFIRST_PREF = "isfirst";
    private MainRecyclerAdapter commercialAdapter;
    private   static UserCardData data;
    private MainRecyclerAdapter farmingAdapter;
    private FragmentAnnoncement fragmentAnnoncement;
    private MainRecyclerAdapter generalAdapter;
    private MainRecyclerAdapter hopyAdapter;
    private MainRecyclerAdapter industryAdapter;
    private boolean isConnected;
    private boolean isFragmentOpen;
    private List<String> loadedUsersList;
    private FragmentOfferAnoncement mOfferAnoncement;
    @BindView(R.id.rv_commercial)
    RecyclerView rvCommercial;
    @BindView(R.id.rv_farming)
    RecyclerView rvFarming;
    @BindView(R.id.rv_general)
    RecyclerView rvGeneral;
    @BindView(R.id.rv_hoppy)
    RecyclerView rvHoppy;
    @BindView(R.id.rv_indestry)
    RecyclerView rvIndustry;
    @BindView(R.id.rv_services)
    RecyclerView rvServices;

    @BindView(R.id.toolbar)
    Toolbar toolBar;
    @BindView(R.id.tv_general)
    TextView tvGeneral;
    @BindView(R.id.vw_genralLine)
    View vwGeneralLine;
    private FragmentSearch search;
    private MainRecyclerAdapter servicisAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_main_modyfied);

        ButterKnife.bind( this);
       setSupportActionBar(toolBar);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collasping);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();

                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Sama Card");
                    isShow = true;
                    tvGeneral.setTextColor(Color.BLACK);
                    vwGeneralLine.setBackgroundColor(Color.BLACK);
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    tvGeneral.setTextColor(Color.WHITE);
                    vwGeneralLine.setBackgroundColor(Color.WHITE);
                    isShow = false;
                }


            }

        });


        data = (UserCardData) getIntent().getParcelableExtra(USER_DATA_KEY);
        loadedUsersList = new ArrayList();
        setupCommercialrv();
        setupFarmingrv();
        setupGeneralrv();
        setupHoppyyrv();
        setupIndustryrv();
        setupServicesrv();
        if (data.getEmail().equals(ADMIN_EMAIL)) {
            Toast.makeText(this, "Welcom Mr Rabiee", Toast.LENGTH_SHORT).show();
        }
        if (!(isFirst() || data.getEmail().equals(ADMIN_EMAIL))) {
            showOffer();
        }
        isFragmentOpen = false;
        search = new FragmentSearch();
        DataBaseUtilies.getDataFromDb(this, this);
    }

    private void setupGeneralrv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, 0, false);
        generalAdapter = new MainRecyclerAdapter(data.getEmail());
        rvGeneral.setLayoutManager(layoutManager);
        rvGeneral.setAdapter(generalAdapter);
    }

    private void setupServicesrv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, 0, false);
        servicisAdapter = new MainRecyclerAdapter(data.getEmail());
        rvServices.setLayoutManager(layoutManager);
        rvServices.setAdapter(servicisAdapter);
    }

    private void setupCommercialrv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, 0, false);
        commercialAdapter = new MainRecyclerAdapter(data.getEmail());
        rvCommercial.setLayoutManager(layoutManager);
        rvCommercial.setAdapter(commercialAdapter);
    }

    private void setupFarmingrv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, 0, false);
        farmingAdapter = new MainRecyclerAdapter(data.getEmail());
        rvFarming.setLayoutManager(layoutManager);
        rvFarming.setAdapter(farmingAdapter);
    }

    private void setupIndustryrv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, 0, false);
        industryAdapter = new MainRecyclerAdapter(data.getEmail());
        rvIndustry.setLayoutManager(layoutManager);
        rvIndustry.setAdapter(industryAdapter);
    }

    private void setupHoppyyrv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, 0, false);
        hopyAdapter = new MainRecyclerAdapter(data.getEmail());
        rvHoppy.setLayoutManager(layoutManager);
        rvHoppy.setAdapter(hopyAdapter);
    }

    private void loadData() {
        getSupportLoaderManager().initLoader(1, null, this).forceLoad();
        getSupportLoaderManager().initLoader(2, null, this).forceLoad();
        getSupportLoaderManager().initLoader(3, null, this).forceLoad();
        getSupportLoaderManager().initLoader(4, null, this).forceLoad();
        getSupportLoaderManager().initLoader(5, null, this).forceLoad();
        getSupportLoaderManager().initLoader(6, null, this).forceLoad();
    }

    private static String checkAdmin(String email) {
        if (email.equals(ADMIN_EMAIL)) {
            return NOT_ACCEPTED;
        }
        return CommonStaticKeys.ACCEPTED;
    }

    private boolean isFirst() {
        SharedPreferences annoncementPrefrence = getPreferences(View.VISIBLE);
        boolean isFirst = annoncementPrefrence.getBoolean(KEY_ISFIRST_PREF, true);
        if (isFirst && isConnected) {
            Editor editor = annoncementPrefrence.edit();
            editor.putBoolean(KEY_ISFIRST_PREF, false);
            editor.apply();
        }
        return isFirst;
    }

    private void showAnnocement() {
        if (CardsContentProvider.getSpecialCards(this, CommonStaticKeys.ACCEPTED).getCount() > 0) {
            fragmentAnnoncement = new FragmentAnnoncement();
            addFragment(fragmentAnnoncement);
        }
    }

    private void showOffer() {
        if (CardsContentProvider.getOffers(this, CommonStaticKeys.ACCEPTED).getCount() > 0) {
            mOfferAnoncement = new FragmentOfferAnoncement();
            addFragment(mOfferAnoncement);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        if (isConteinthisId(this, data.getUserId())) {
            menu.getItem(2).setIcon(R.drawable.ic_editecard);
            menu.getItem(View.VISIBLE).setVisible(true);
        }
        return true;
    }

    private static boolean isConteinthisId(Context context, String id) {
        return CardsContentProvider.getUserWithId(context, id).getCount() != 0;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_aboutus:
                Intent intent2 = new Intent(this, SpecialCardActivity.class);
                intent2.putExtra(SPECIAL_CARD, "-614069815");
                startActivity(intent2);
                break;
            case R.id.menu_add:

                Intent intent=new Intent(this,MessagesListActivity.class);
                startActivity(intent);

                break;
            case R.id.menu_search:
                addFragment(search);
                break;
            case R.id.menu_signout:
                signOut();
                break;
            case R.id.menu_usercard:
                Intent intent3 = new Intent(this, MyCardAndOffersActivity.class);
                intent3.putExtra(KEY_UID, data.getUserId());
                startActivity(intent3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exite, R.anim.pop_enter, R.anim.pop_exite);
        if (isFragmentOpen) {
            transaction.remove(fragment).commit();
            isFragmentOpen = false;
            return;
        }
        transaction.add(R.id.container_search, fragment).commit();
        isFragmentOpen = true;
    }

    private void removeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exite, R.anim.pop_enter, R.anim.pop_exite);
        transaction.remove(fragment).commit();
        isFragmentOpen = false;
    }

    private boolean isCardExpierd(String date) {
        String[] x = date.split("/");
        String expYear = x[0];
        String expmonth = x[1];
        String expDay = x[2];
        String[] y = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH).format(Long.valueOf(new Date().getTime())).split("/");
        String currntYear = y[0];
        String currentmonth = y[1];
        String currentDay = y[2];
        if (Integer.parseInt(currntYear) > Integer.parseInt(expYear)) {
            return true;
        }
        if (Integer.parseInt(currntYear) == Integer.parseInt(expYear)) {
            if (Integer.parseInt(currentmonth) > Integer.parseInt(expmonth)) {
                return true;
            }
            if (Integer.parseInt(currentmonth) == Integer.parseInt(expmonth)) {
                return Integer.parseInt(currentDay) >= Integer.parseInt(expDay);
            }
        }
        return false;
    }

    public void resiveData(UserCardData data) {
        CardsContentProvider.insertCardToDb(data, this);
        loadedUsersList.add(data.getUserId());
        if (isCardExpierd(data.getDate()) && data.getEmail().equals(ADMIN_EMAIL)) {
            DataBaseUtilies.deleteCard(data.getUserId(), this, data.getPhotoLink(), data.getOfferImg());
        }
    }

    public void lastReseved() {
        Cursor cursor = getContentResolver().query(CardEntry.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                String id = cursor.getString(cursor.getColumnIndex(CardDataEntry.USER_ID));
                if (!loadedUsersList.contains(id)) {
                    CardsContentProvider.deleteUserCard(id, this);
                    invalidateOptionsMenu();
                }
                if (cursor.isLast()) {
                    loadData();
                    cursor.close();
                    invalidateOptionsMenu();
                }
            }
        }
    }

    public void deleteUser(UserCardData data) {
        CardsContentProvider.deleteUserCard(data.getUserId(), this);
        loadData();
        invalidateOptionsMenu();
    }

    public void search(String activity, String governorate, String area) {
        Intent intent = new Intent(this, SpiceficUsersActivity.class);
        intent.putExtra(KEY_ACTIVITY, activity);
        intent.putExtra(KEY_GOVERNORATE, governorate);
        intent.putExtra(KEY_AREA, area);
        startActivity(intent);
        removeFragment(search);
    }

    public static String getMyEmail()
    {
        return data.getEmail();
    }

    public void exit() {
        removeFragment(search);
    }

    public void action(int id, String userId) {
        switch (id) {
            case R.id.bt_cancel:
                removeFragment(mOfferAnoncement);
                return;
            case R.id.bt_open:
                Intent intent = new Intent(this, SpecialCardActivity.class);
                intent.putExtra(SPECIAL_CARD, userId);
                startActivity(intent);
                removeFragment(mOfferAnoncement);
                return;
            default:
                return;
        }
    }

    private void signOut() {
        AuthUI.getInstance().signOut(getApplicationContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(MainActivity.this,AuthinticationActivity.class));
            }
        });
    }

    public void onConnectionChange(ConnectivityEvent event) {
        if (event.getState().getValue() == ConnectivityState.CONNECTED) {
            isConnected = true;
        } else if (event.getState().getValue() == ConnectivityState.DISCONNECTED) {
            isConnected = false;
        }
    }

    protected void onStart() {
        super.onStart();
        ConnectionBuddy.getInstance().registerForConnectivityEvents(this, this);
        if (!isConnected) {
            KToast.normalToast(this, getString(R.string.dissconnection_message), 80, KToast.LENGTH_LONG, R.drawable.ic_warning);
        }
        isFirst();
        invalidateOptionsMenu();
    }

    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        loadData();
    }

    protected void onStop() {
        super.onStop();
        ConnectionBuddy.getInstance().unregisterFromConnectivityEvents(this);
    }

    private void updateCommercial(Cursor data) {
        commercialAdapter.updateCursor(data);
    }

    private void updateFarming(Cursor data) {
        farmingAdapter.updateCursor(data);
    }

    private void updateHoppy(Cursor data) {
        hopyAdapter.updateCursor(data);
    }

    private void updateGeneral(Cursor data) {
        generalAdapter.updateCursor(data);
    }

    private void updateServices(Cursor data) {
        servicisAdapter.updateCursor(data);
    }

    private void updateIndustry(Cursor data) {
        industryAdapter.updateCursor(data);
    }

    public Loader<Cursor> onCreateLoader(final int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            @Nullable
            public Cursor loadInBackground() {
                return CardsContentProvider.getActivity(getResources().getStringArray(R.array.activities)[id], MainActivity.this, MainActivity.checkAdmin(data.getEmail()));
            }
        };
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case 1:
                updateCommercial(data);
                return;
            case 2:
                updateIndustry(data);
                return;
            case 3:
                updateFarming(data);
                return;
            case 4:
                updateServices(data);
                return;
            case 5:
                updateHoppy(data);
                return;
            case 6:
                updateGeneral(data);
                return;
            default:
                return;
        }
    }

    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public void addCard(View view) {


        Intent intent = new Intent(this, AddCardActivity.class);
        intent.putExtra(USER_DATA_KEY, data);
        startActivity(intent);
        if (isConteinthisId(this, data.getUserId())) {
            KToast.normalToast(this, getString(R.string.aleardy_have_card_message), 80, KToast.LENGTH_LONG, R.drawable.ic_warning);

        }
    }


    public static UserCardData getUserData()
    {
        return data;
    }
}

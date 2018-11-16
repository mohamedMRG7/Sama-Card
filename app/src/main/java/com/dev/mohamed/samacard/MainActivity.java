package com.dev.mohamed.samacard;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.dev.mohamed.samacard.adapters.MainRecyclerAdapter;
import com.dev.mohamed.samacard.addCard.AddCardActivity;
import com.dev.mohamed.samacard.auth.AuthinticationActivity;
import com.dev.mohamed.samacard.contentProvider.CardsContentProvider;
import com.dev.mohamed.samacard.fireBase.DataBaseUtilies;
import com.dev.mohamed.samacard.user.UserCardData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements DataBaseUtilies.onResiveData {


    @BindView(R.id.rv_services) RecyclerView rvServices;
    @BindView(R.id.rv_commercial)RecyclerView rvCommercial;
    @BindView(R.id.rv_indestry)RecyclerView rvIndustry;
    @BindView(R.id.rv_farming)RecyclerView rvFarming;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        String[] activitys=getResources().getStringArray(R.array.activities);
        setupRecycler(rvCommercial,activitys[1]);
        setupRecycler(rvFarming,activitys[3]);
        setupRecycler(rvIndustry,activitys[2]);
        setupRecycler(rvServices,activitys[4]);

        DataBaseUtilies.getDataFromDb(this,this);


    }

    public void setupRecycler(RecyclerView recyclerView,String activity)
    {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        Cursor cursor= CardsContentProvider.getActivity(activity,this);
      //  cursor.moveToPosition(0);
       // String photo=cursor.getString(cursor.getColumnIndex(DbContract.CardDataEntry.PHOTO_LINK));
       // Log.e("photo",photo);
        MainRecyclerAdapter adapter=new MainRecyclerAdapter(cursor);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    void updateCursors()
    {
        String[] activitys=getResources().getStringArray(R.array.activities);
        setupRecycler(rvCommercial,activitys[1]);
        setupRecycler(rvFarming,activitys[3]);
        setupRecycler(rvIndustry,activitys[2]);
        setupRecycler(rvServices,activitys[4]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

       getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id=item.getItemId();
        switch (id)
        {
            case R.id.menu_add:
                UserCardData data=getIntent().getParcelableExtra(AuthinticationActivity.USER_DATA_KEY);
                Intent intent=new Intent(MainActivity.this, AddCardActivity.class);
                intent.putExtra(AuthinticationActivity.USER_DATA_KEY,data);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void resiveData(UserCardData data) {
        CardsContentProvider.insertCardToDb(data,this);

        updateCursors();
    }
}

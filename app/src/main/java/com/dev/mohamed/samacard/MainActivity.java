package com.dev.mohamed.samacard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.dev.mohamed.samacard.adapters.MainRecyclerAdapter;
import com.dev.mohamed.samacard.addCard.AddCardActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.rv_services) RecyclerView rvServices;
    @BindView(R.id.rv_commercial)RecyclerView rvCommercial;
    @BindView(R.id.rv_indestry)RecyclerView rvIndustry;
    @BindView(R.id.rv_farming)RecyclerView rvFarming;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupRecycler(rvCommercial);
        setupRecycler(rvFarming);
        setupRecycler(rvIndustry);
        setupRecycler(rvServices);

    }

    public void setupRecycler(RecyclerView recyclerView)
    {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        MainRecyclerAdapter adapter=new MainRecyclerAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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
                startActivity(new Intent(MainActivity.this, AddCardActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}

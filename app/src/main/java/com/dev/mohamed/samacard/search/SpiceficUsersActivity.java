package com.dev.mohamed.samacard.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.dev.mohamed.samacard.CommonStaticKeys;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.MainActivity;
import com.dev.mohamed.samacard.contentProvider.CardsContentProvider;

public class SpiceficUsersActivity extends AppCompatActivity {
    @BindView(R.id.rv_specificUsers)
    RecyclerView rvUsers;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spicefic_users);
        ButterKnife.bind(this);
        String activiy = getIntent().getStringExtra(CommonStaticKeys.KEY_ACTIVITY);
        String governorate = getIntent().getStringExtra(CommonStaticKeys.KEY_GOVERNORATE);
        String area = getIntent().getStringExtra(CommonStaticKeys.KEY_AREA);
        setTitle(activiy);
        setupRecycler(rvUsers, activiy, governorate, area);
    }

    private void setupRecycler(RecyclerView recyclerView, String activity, String governorate, String area) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        SpiceficUsersAdapter adapter = new SpiceficUsersAdapter(CardsContentProvider.getSearchResult(activity, governorate, area, this, CommonStaticKeys.ACCEPTED), this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}

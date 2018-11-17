package com.dev.mohamed.samacard.ads;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;


import com.dev.mohamed.samacard.CommonStaticKeys;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.contentProvider.CardsContentProvider;
import com.dev.mohamed.samacard.databinding.FragmentAnnocementBinding;
import com.dev.mohamed.samacard.sqliteDb.DbContract.CardDataEntry;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class FragmentAnnoncement extends Fragment implements OnClickListener {
    private AnnoncementAction action;
    FragmentAnnocementBinding annocementBinding;
    private Context context;
    private String uId;

    public interface AnnoncementAction {
        void action(int i, String str);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        context = context;
        action = (AnnoncementAction) context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        annocementBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_annocement, container, false);
        View view = annocementBinding.getRoot();
        Random ruser = new Random();
        Cursor mCursor = CardsContentProvider.getSpecialCards(context, CommonStaticKeys.ACCEPTED);
        if (mCursor.getCount() == 0) {
            return null;
        }
        mCursor.moveToPosition(ruser.nextInt(mCursor.getCount()));
        String name = mCursor.getString(mCursor.getColumnIndex(CardDataEntry.USER_NAME));
        String aboutActiviy = mCursor.getString(mCursor.getColumnIndex(CardDataEntry.ABOUT_ACTITIY));
        String activity = mCursor.getString(mCursor.getColumnIndex("activity"));
        String adress = mCursor.getString(mCursor.getColumnIndex(CardDataEntry.ADDRESS));
        String governorate = mCursor.getString(mCursor.getColumnIndex(CardDataEntry.GOVERNORATE));
        String photo = mCursor.getString(mCursor.getColumnIndex(CardDataEntry.PHOTO_LINK));
        uId = mCursor.getString(mCursor.getColumnIndex(CardDataEntry.USER_ID));
        annocementBinding.tvActivity.setText(activity);
        annocementBinding.tvUserName.setText(name);
//        Glide.with(this).load(photo).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.ic_error)).into(annocementBinding.imgPhoto);
        Picasso.with(context).load(photo).placeholder(R.drawable.loading).error(R.drawable.ic_error).into(annocementBinding.imgPhoto);

        if (aboutActiviy.equals(context.getResources().getString(R.string.not_available))) {
            annocementBinding.tvAboutactivity.setText(governorate + " , " + adress);
        } else {
            annocementBinding.tvAboutactivity.setText(aboutActiviy);
        }
        annocementBinding.btOpen.setOnClickListener(this);
        annocementBinding.btCancel.setOnClickListener(this);
        return view;
    }

    public void onClick(View v) {
        action.action(v.getId(), uId);
    }
}

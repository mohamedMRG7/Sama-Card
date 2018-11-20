package com.dev.mohamed.samacard.ads;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.dev.mohamed.samacard.CommonStaticKeys;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.ads.FragmentAnnoncement.AnnoncementAction;
import com.dev.mohamed.samacard.contentProvider.CardsContentProvider;
import com.dev.mohamed.samacard.databinding.FragmentOfferAnnocementBinding;
import com.dev.mohamed.samacard.sqliteDb.DbContract.CardDataEntry;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class FragmentOfferAnoncement extends Fragment implements OnClickListener {
    private AnnoncementAction action;
    FragmentOfferAnnocementBinding binding;
    private Context context;
    private String uId;

    public void onAttach(Context context) {
        super.onAttach(context);
        action = (AnnoncementAction) context;
       this.context = context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offer_annocement, container, false);
        View view = binding.getRoot();
        Cursor cursor = CardsContentProvider.getOffers(context, CommonStaticKeys.ACCEPTED);
        Random rOffer = new Random();

        if (cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToPosition(rOffer.nextInt(cursor.getCount()));
        String offerLink = cursor.getString(cursor.getColumnIndex(CardDataEntry.OFFER_IMAGE));
        Log.e(CardDataEntry.OFFER_IMAGE, offerLink + "");
        uId = cursor.getString(cursor.getColumnIndex(CardDataEntry.USER_ID));
     //   Glide.with(this).load(offerLink).into(binding.imgOffer);
        Picasso.with(context).load(offerLink).placeholder(R.drawable.loading).into(binding.imgOffer);

        binding.btOpen.setOnClickListener(this);
        binding.btCancel.setOnClickListener(this);
        return view;
    }

    public void onClick(View v) {
        action.action(v.getId(), uId);
    }
}

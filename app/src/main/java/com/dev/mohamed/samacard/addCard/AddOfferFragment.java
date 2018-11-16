package com.dev.mohamed.samacard.addCard;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.databinding.FragmentAddOfferBinding;

public class AddOfferFragment extends Fragment {
    FragmentAddOfferBinding mOfferBinding;
    private OnAddOffer mOnAddOffer;

    interface OnAddOffer {
        void addOffer(FragmentAddOfferBinding fragmentAddOfferBinding);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        mOnAddOffer = (OnAddOffer) context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mOfferBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_offer, container, false);
        View view = mOfferBinding.getRoot();
        mOnAddOffer.addOffer(mOfferBinding);
        return view;
    }
}

package com.dev.mohamed.samacard.addCard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dev.mohamed.samacard.R;

public class ChooseCardFragment extends Fragment {
    private Choosecard mcChoosecard;

    public interface Choosecard {
        void cardType(View view);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        mcChoosecard = (Choosecard) context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_cardtype, container, false);
        mcChoosecard.cardType(view);
        return view;
    }
}

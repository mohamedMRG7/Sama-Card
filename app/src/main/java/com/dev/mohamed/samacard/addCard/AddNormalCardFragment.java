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

/**
 * Created by mohamed on 3/15/18.
 */

public class AddNormalCardFragment extends Fragment{


    AddNormalCard mAddNormalCard;
    public AddNormalCardFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAddNormalCard=(AddNormalCard) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_add_normalcard,container,false);

        mAddNormalCard.normalCard(view);


        return view;
    }


    public interface AddNormalCard
    {
        void normalCard(View view);
    }
}

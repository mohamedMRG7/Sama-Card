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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.databinding.FragmentAddSpecialcardBinding;

/**
 * Created by mohamed on 3/15/18.
 */

public class AddSpecialCardFragment extends Fragment {



    AddSpecialCard mAddspecialCard;
    FragmentAddSpecialcardBinding specialcardBinding;
    public AddSpecialCardFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAddspecialCard =(AddSpecialCard) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        specialcardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_specialcard, container, false);

        View view = specialcardBinding.getRoot();

        mAddspecialCard.specialCard(specialcardBinding);
        specialcardBinding.spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0)
                parent.setBackgroundColor(getResources().getColor(R.color.wihte));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        specialcardBinding.edUserName.setText("Mohamed");

        return view;
    }




    public interface AddSpecialCard
    {
        void specialCard(FragmentAddSpecialcardBinding binding);
    }



}

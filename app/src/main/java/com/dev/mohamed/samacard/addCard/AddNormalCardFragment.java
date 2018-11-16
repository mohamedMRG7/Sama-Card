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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.databinding.FragmentAddNormalcardBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class AddNormalCardFragment extends Fragment {
    FragmentAddNormalcardBinding addNormalcardBinding;
    private Context context;
    private AddNormalCard mAddNormalCard;


    //USE THIS PASS THE VIEW ELEMENTS WITH IT AS BINDING VARIABLE AND USE IT AT THE ACTIVITY
    public interface AddNormalCard {
        void normalCard(FragmentAddNormalcardBinding fragmentAddNormalcardBinding);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        mAddNormalCard = (AddNormalCard) context;
        this.context = context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addNormalcardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_normalcard, container, false);
        View view = addNormalcardBinding.getRoot();
        mAddNormalCard.normalCard(addNormalcardBinding);
        setUpspinnerLisner(addNormalcardBinding.spGovernorate);
        setUpspinnerLisner(addNormalcardBinding.spCountry);
        setUpspinnerLisner(addNormalcardBinding.spActivity);
        return view;
    }


    //MANAGE SPINNERS AT THE VIEW AND THE COLOR OF NULL DATA
    private void setUpspinnerLisner(final Spinner spinner) {
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    parent.setBackgroundColor(context.getResources().getColor(R.color.wihte));
                }
                if (spinner.getId() == R.id.sp_country) {
                    String[] list = AddSpecialCardFragment.getAreaList(position, context);
                    if (list != null) {
                        ArrayList<String> arrayList = new ArrayList(Arrays.asList(list));
                        arrayList.remove(1);
                        addNormalcardBinding.spGovernorate.setAdapter(new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, arrayList));
                    }
                    if (position == 0) {
                        addNormalcardBinding.spGovernorate.setVisibility(View.GONE);
                    } else {
                        addNormalcardBinding.spGovernorate.setVisibility(View.VISIBLE);
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}

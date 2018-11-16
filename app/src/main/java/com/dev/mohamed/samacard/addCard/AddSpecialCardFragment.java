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
import com.dev.mohamed.samacard.databinding.FragmentAddSpecialcardBinding;
import java.util.ArrayList;
import java.util.Arrays;

public class AddSpecialCardFragment extends Fragment {
    private Context context;
    private AddSpecialCard mAddspecialCard;
    FragmentAddSpecialcardBinding specialcardBinding;

    public interface AddSpecialCard {
        void specialCard(FragmentAddSpecialcardBinding fragmentAddSpecialcardBinding);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        mAddspecialCard = (AddSpecialCard) context;
        this.context = context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        specialcardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_specialcard, container, false);
        View view = specialcardBinding.getRoot();
        mAddspecialCard.specialCard(specialcardBinding);
        setUpspinnerLisner(specialcardBinding.spGovernorate);
        setUpspinnerLisner(specialcardBinding.spCountry);
        setUpspinnerLisner(specialcardBinding.spActivity);
        return view;
    }

    //MANAGE SPINNER TEXT COLOR AND VISIBILITY
    private void setUpspinnerLisner(final Spinner spinner) {
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    parent.setBackgroundColor(getResources().getColor(R.color.wihte));
                }
                if (spinner.getId() == R.id.sp_country) {
                    String[] list = AddSpecialCardFragment.getAreaList(position, context);
                    ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(list));
                    arrayList.remove(1);
                    if (list != null) {
                        specialcardBinding.spGovernorate.setAdapter(new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, arrayList));
                    }
                    if (position == 0) {
                        specialcardBinding.spGovernorate.setVisibility(View.GONE);
                    } else {
                        specialcardBinding.spGovernorate.setVisibility(View.VISIBLE);
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public static String[] getAreaList(int position, Context context) {
        switch (position) {
            case 1:
                return context.getResources().getStringArray(R.array.cairo);
            case 2:
                return context.getResources().getStringArray(R.array.giza);
            case 3:
                return context.getResources().getStringArray(R.array.qaliobya);
            case 7:
                return context.getResources().getStringArray(R.array.alex);
            default:
                return context.getResources().getStringArray(R.array.other);
        }
    }
}

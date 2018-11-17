package com.dev.mohamed.samacard.search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.addCard.AddSpecialCardFragment;
import com.dev.mohamed.samacard.databinding.FragmentSearchBinding;

public class FragmentSearch extends Fragment implements OnClickListener {
    private Context context;
    private OnSearch msearch;
    FragmentSearchBinding searchBinding;

    public interface OnSearch {
        void exit();

        void search(String str, String str2, String str3);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        msearch = (OnSearch) context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        searchBinding = (FragmentSearchBinding) DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        View view = searchBinding.getRoot();
        setUpspinnerLisner(searchBinding.spGovernorate);
        setUpspinnerLisner(searchBinding.spArea);
        searchBinding.btSearch.setOnClickListener(this);
        searchBinding.btOut.setOnClickListener(this);
        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_out:
                msearch.exit();
                return;
            case R.id.bt_search:
                if (searchBinding.spGovernorate.getSelectedItemPosition() == 0 || searchBinding.spActivity.getSelectedItemPosition() == 0 || searchBinding.spArea.getSelectedItemPosition() == 0) {
                    Toast.makeText(context, R.string.filter_message, Toast.LENGTH_SHORT).show();
                    return;
                }
                msearch.search(searchBinding.spActivity.getItemAtPosition(searchBinding.spActivity.getSelectedItemPosition()).toString(), searchBinding.spGovernorate.getItemAtPosition(searchBinding.spGovernorate.getSelectedItemPosition()).toString(), searchBinding.spArea.getItemAtPosition(searchBinding.spArea.getSelectedItemPosition()).toString());
                return;
            default:
                return;
        }
    }

    private void setUpspinnerLisner(final Spinner spinner) {
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (spinner.getId() != R.id.sp_governorate) {
                    return;
                }
                if (position == 0) {
                    searchBinding.spArea.setVisibility(View.GONE);
                    return;
                }
                String[] list = AddSpecialCardFragment.getAreaList(position, context);
                if (list != null) {
                  searchBinding.spArea.setAdapter(new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, list));
                  searchBinding.spArea.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}

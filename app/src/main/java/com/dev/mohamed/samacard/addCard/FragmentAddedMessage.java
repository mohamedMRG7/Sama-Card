package com.dev.mohamed.samacard.addCard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.dev.mohamed.samacard.R;

public class FragmentAddedMessage extends Fragment implements OnClickListener {
    private int messagType;

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_after_add_message, container, false);
        Button ok = view.findViewById(R.id.bt_done);
        TextView tvMessage = view.findViewById(R.id.tv_added_complet_message);
        if (messagType == 1) {
            tvMessage.setText(getString(R.string.pennding_message));
        } else {
            tvMessage.setText(getString(R.string.offer_succesadded_message));
        }
        ok.setOnClickListener(this);
        return view;
    }

    public void onClick(View v) {
        getActivity().finish();
    }

    public void setOfferMessage(int messageType) {
        this.messagType = messageType;
    }
}

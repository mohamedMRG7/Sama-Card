package com.dev.mohamed.samacard.search;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;

import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.addCard.AddCardActivity;
import com.dev.mohamed.samacard.card.NormalCardActvitiy;
import com.dev.mohamed.samacard.card.SpecialCardActivity;
import com.dev.mohamed.samacard.sqliteDb.DbContract.CardDataEntry;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import static com.dev.mohamed.samacard.CommonStaticKeys.NORMAL_CARD;
import static com.dev.mohamed.samacard.CommonStaticKeys.SPECIAL_CARD;

public class SpiceficUsersAdapter extends Adapter<SpiceficUsersAdapter.SpicifcAdapterViewHolder> {
    private Context context;
    private Cursor cursor;

    class SpicifcAdapterViewHolder extends ViewHolder implements OnClickListener {
        @BindView(R.id.tv_adress)
        TextView adress;
        @BindView(R.id.img_photo)
        CircularImageView logo;
        @BindView(R.id.tv_userName)
        TextView userName;

        public SpicifcAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind((Object) this, itemView);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            cursor.moveToPosition(getAdapterPosition());
            String userId = cursor.getString(cursor.getColumnIndex(CardDataEntry.USER_ID));
            String cardType = cursor.getString(cursor.getColumnIndex(CardDataEntry.CARDTYPE));
            Intent intent = new Intent(context, SpecialCardActivity.class);
            if (cardType.equals(SPECIAL_CARD)) {

                intent.putExtra(SPECIAL_CARD, userId);
                context.startActivity(intent);
            }
            if (cardType.equals(NORMAL_CARD)) {
                intent = new Intent(context, NormalCardActvitiy.class);
                intent.putExtra(NORMAL_CARD, userId);
                context.startActivity(intent);
            }
        }
    }



    public SpiceficUsersAdapter(Cursor cursor, Context context) {
        this.cursor = cursor;
        this.context = context;
        if (cursor.getCount() == 0) {
            Toast.makeText(context, R.string.nocards, Toast.LENGTH_LONG).show();
        }
    }

    public SpicifcAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SpicifcAdapterViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_specific_user_item, viewGroup, false));
    }

    public void onBindViewHolder(SpicifcAdapterViewHolder spicifcAdapterViewHolder, int i) {
        cursor.moveToPosition(i);
        String name = cursor.getString(cursor.getColumnIndex(CardDataEntry.USER_NAME));
        String photo = cursor.getString(cursor.getColumnIndex(CardDataEntry.PHOTO_LINK));
        String address = cursor.getString(cursor.getColumnIndex(CardDataEntry.ADDRESS));
        if (cursor.getString(cursor.getColumnIndex(CardDataEntry.CARDTYPE)).equals(SPECIAL_CARD)) {
            spicifcAdapterViewHolder.userName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_black_gallon, 0);
            spicifcAdapterViewHolder.userName.setCompoundDrawablePadding(View.GONE);
        }
        spicifcAdapterViewHolder.userName.setText(name);
        spicifcAdapterViewHolder.adress.setText(address);
     //   Glide.with(context).load(photo).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.ic_error)).into(spicifcAdapterViewHolder.logo);
        Picasso.with(context).load(photo).placeholder(R.drawable.loading).error(R.drawable.ic_error).into(spicifcAdapterViewHolder.logo);

    }

    public int getItemCount() {
        return cursor.getCount();
    }
}

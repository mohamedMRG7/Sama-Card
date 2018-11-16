package com.dev.mohamed.samacard.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dev.mohamed.samacard.CommonStaticKeys;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.card.NormalCardActivity;
import com.dev.mohamed.samacard.card.SpecialCardActivity;
import com.dev.mohamed.samacard.sqliteDb.DbContract.CardDataEntry;
import com.mikhaellopez.circularimageview.CircularImageView;


//Main adapter displaying cards in the main screen

public class MainRecyclerAdapter extends Adapter<MainRecyclerAdapter.MainRecycelerAdapterViewholder> {
    private Context context;
    private Cursor cursor;
    private String email;

    class MainRecycelerAdapterViewholder extends ViewHolder implements OnClickListener {
        @BindView(R.id.img_banner)
        ImageView banner;
        @BindView(R.id.img_logo)
        CircularImageView logo;
        @BindView(R.id.tv_userName)
        TextView userName;

        public MainRecycelerAdapterViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {

            //Get THE POSITIONED CLICKED DATA AND PASS IT TO ITS ACTIVITY (NORMA CARD ACTIVITY OR SPEACIAL CARD ACTIVITY)
            cursor.moveToPosition(getAdapterPosition());
            String positionUserId = cursor.getString(cursor.getColumnIndex(CardDataEntry.USER_ID));
            String cardType = cursor.getString(cursor.getColumnIndex(CardDataEntry.CARDTYPE));
            Intent intent;


            //PASSING EMAIL TO THE ACTIVITY
            if (cardType.equals(CommonStaticKeys.SPECIAL_CARD)) {
                intent = new Intent(context, SpecialCardActivity.class);
                intent.putExtra(CommonStaticKeys.SPECIAL_CARD, positionUserId);
                intent.putExtra(CommonStaticKeys.EMAIL_KEY, MainRecyclerAdapter.this.email);
                context.startActivity(intent);
            }
            if (cardType.equals(CommonStaticKeys.NORMAL_CARD)) {
                intent = new Intent(context, NormalCardActivity.class);
                intent.putExtra(CommonStaticKeys.NORMAL_CARD, positionUserId);
                intent.putExtra(CommonStaticKeys.EMAIL_KEY, email);
               context.startActivity(intent);
            }
        }
    }



    public MainRecyclerAdapter(String email) {
        this.email = email;
    }

    public void updateCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public MainRecycelerAdapterViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new MainRecycelerAdapterViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false));
    }

    public void onBindViewHolder(MainRecycelerAdapterViewholder holder, int position) {
        this.cursor.moveToPosition(position);
        String logo = this.cursor.getString(this.cursor.getColumnIndex(CardDataEntry.PHOTO_LINK));
        String userName = this.cursor.getString(this.cursor.getColumnIndex(CardDataEntry.USER_NAME));

        //MANAGE VISIBILITY OF THE SPECIAL BANNER IMAGE
        if (this.cursor.getString(this.cursor.getColumnIndex(CardDataEntry.CARDTYPE)).equals(CommonStaticKeys.SPECIAL_CARD)) {
            holder.banner.setVisibility(View.VISIBLE);
        }
        Glide.with(this.context).load(logo).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.ic_error)).into(holder.logo);
        holder.userName.setText(userName);
    }

    public int getItemCount() {
        if (this.cursor == null) {
            return 0;
        }
        return this.cursor.getCount();
    }
}

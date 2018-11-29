package com.dev.mohamed.samacard.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
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


import com.dev.mohamed.samacard.CommonStaticKeys;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.card.NormalCardActvitiy;
import com.dev.mohamed.samacard.card.SpecialCardActivity;
import com.dev.mohamed.samacard.sqliteDb.DbContract.CardDataEntry;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


//Main adapter displaying cards in the main screen

public class MainRecyclerAdapter extends Adapter<MainRecyclerAdapter.MainRecycelerAdapterViewholder> {
    private Activity context;
    private Cursor cursor;
    private String email;

    class MainRecycelerAdapterViewholder extends ViewHolder implements OnClickListener {
        @BindView(R.id.img_banner)
        ImageView banner;
        @BindView(R.id.img_logo)
        ImageView logo;
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    context.startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
                }
                else  context.startActivity(intent);
            }
            if (cardType.equals(CommonStaticKeys.NORMAL_CARD)) {
                intent = new Intent(context, NormalCardActvitiy.class);
                intent.putExtra(CommonStaticKeys.NORMAL_CARD, positionUserId);
                intent.putExtra(CommonStaticKeys.EMAIL_KEY, email);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    context.startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
                }
                else  context.startActivity(intent);
            }
        }
    }



    public MainRecyclerAdapter(Activity activity,String email) {
        this.email = email;
        this.context=activity;
    }

    public void updateCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public MainRecycelerAdapterViewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MainRecycelerAdapterViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_modifyed, parent, false));
    }

    public void onBindViewHolder(MainRecycelerAdapterViewholder holder, int position) {
        this.cursor.moveToPosition(position);
        String logo = this.cursor.getString(this.cursor.getColumnIndex(CardDataEntry.PHOTO_LINK));
        String userName = this.cursor.getString(this.cursor.getColumnIndex(CardDataEntry.USER_NAME));

        //MANAGE VISIBILITY OF THE SPECIAL BANNER IMAGE
        if (this.cursor.getString(this.cursor.getColumnIndex(CardDataEntry.CARDTYPE)).equals(CommonStaticKeys.SPECIAL_CARD)) {
            holder.banner.setVisibility(View.VISIBLE);
        }

       // Glide.with(this.context).load(logo).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.ic_error)).into(holder.logo);
        Picasso.with(context).load(logo).placeholder(R.drawable.loading).error(R.drawable.ic_error).into(holder.logo);
        holder.userName.setText(userName);
    }

    public int getItemCount() {
        if (this.cursor == null) {
            return 0;
        }else if (cursor.getCount()>15)
        return 15;
        else
            return cursor.getCount();
    }
}

package com.dev.mohamed.samacard.chat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.mohamed.samacard.CommonStaticKeys;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.contentProvider.CardsContentProvider;
import com.dev.mohamed.samacard.sqliteDb.DbContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageslistAdapter extends RecyclerView.Adapter<MessageslistAdapter.MessagesListAdapterViewHolder>{



    private Cursor cursor;
    private Context context;
    @NonNull
    @Override
    public MessagesListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        return new MessagesListAdapterViewHolder(inflater.inflate(R.layout.rv_messageslist_item,viewGroup,false));
    }

    public MessageslistAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesListAdapterViewHolder holder, int i) {

        cursor.moveToPosition(i);


        String uID =cursor.getString(cursor.getColumnIndex(ChatDbContract.SENDER));
        String userName=CardsContentProvider.getSpecificData(context,DbContract.CardDataEntry.USER_NAME,uID);
        String avatar=CardsContentProvider.getSpecificData(context,DbContract.CardDataEntry.PHOTO_LINK,uID);
        String lastMessage=LocalDbUtalis.lastMessage(context,uID);
        int unSeenMessagesNum=LocalDbUtalis.getUnSeenMessagesNum(context,uID);


        if (unSeenMessagesNum>0)
            holder.unSeenViewContainer.setVisibility(View.VISIBLE);
        else
            holder.unSeenViewContainer.setVisibility(View.GONE);


        holder.tvMessage.setText(lastMessage);
        holder.tvUnReadedMessages.setText(String.valueOf(unSeenMessagesNum));
        if (!userName.isEmpty() && !avatar.isEmpty()) {
            holder.tvUserName.setText(userName);
            Picasso.with(context).load(avatar).placeholder(R.drawable.loading).into(holder.imgAvatar);
        }else
        {
            //if user dont have card any more
            holder.tvUserName.setTextColor(Color.RED);
            holder.tvUserName.setText("User");
            Picasso.with(context).load(R.drawable.bsetlogo).into(holder.imgAvatar);
        }
    }

    public void setUsrsChatList(Cursor cursor)
    {
        this.cursor=cursor;
    }

    @Override
    public int getItemCount() {
        if (cursor!=null)
           return cursor.getCount();
        else return 0;
    }

    class MessagesListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img_avatar)
        ImageView imgAvatar;
        @BindView(R.id.tv_userName)
        TextView tvUserName;
        @BindView(R.id.tv_message)
        TextView tvMessage;
        @BindView(R.id.tv_unReadedMessages)
        TextView tvUnReadedMessages;
        @BindView(R.id.unSeenViewContainer)
        Group unSeenViewContainer;
        public MessagesListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            cursor.moveToPosition(getAdapterPosition());
            String uId =cursor.getString(cursor.getColumnIndex(ChatDbContract.SENDER));

            Intent intent =new Intent(context,ChatActivity.class);
            intent.putExtra(CommonStaticKeys.KEY_UID,uId);


            context.startActivity(intent);

        }
    }
}

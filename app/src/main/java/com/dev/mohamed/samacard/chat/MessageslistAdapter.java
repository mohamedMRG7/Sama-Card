package com.dev.mohamed.samacard.chat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
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

        String avatar;
        String email =cursor.getString(cursor.getColumnIndex(ChatDbContract.SENDER));
        String userName=CardsContentProvider.getUserName(context,email);
        String lastMessage=LocalDbUtalis.lastMessage(context,email);
        int unSeenMessagesNum=LocalDbUtalis.getUnSeenMessagesNum(context,email);

        holder.tvUserName.setText(userName);
        holder.tvMessage.setText(lastMessage);
        holder.tvUnReadedMessages.setText(String.valueOf(unSeenMessagesNum));


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
        public MessagesListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            cursor.moveToPosition(getAdapterPosition());
            String email =cursor.getString(cursor.getColumnIndex(ChatDbContract.SENDER));

            Intent intent =new Intent(context,ChatActivity.class);
            intent.putExtra(CommonStaticKeys.EMAIL_KEY,email);
            context.startActivity(intent);

        }
    }
}

package com.dev.mohamed.samacard.chat;

import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.mohamed.samacard.MainActivity;
import com.dev.mohamed.samacard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatAdapterPlaceHolder>{


    private Cursor cursor;
    @NonNull
    @Override
    public ChatAdapterPlaceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());

        return new ChatAdapterPlaceHolder(inflater.inflate(i,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapterPlaceHolder chatAdapterPlaceHolder, int i) {

        cursor.moveToPosition(i);
        String sender=cursor.getString(cursor.getColumnIndex(ChatDbContract.SENDER));
        String resever=cursor.getString(cursor.getColumnIndex(ChatDbContract.RESEVER));
        String message=cursor.getString(cursor.getColumnIndex(ChatDbContract.MESSAGE));
        int seen=cursor.getInt(cursor.getColumnIndex(ChatDbContract.SEEN));
        String dateAndTime=cursor.getString(cursor.getColumnIndex(ChatDbContract.DATE_AND_TIME));
        String[]dateTime=dateAndTime.split(" ");

        chatAdapterPlaceHolder.tvDate.setText(dateTime[0]);
        chatAdapterPlaceHolder.tvTime.setText(dateTime[1]);

        if (sender.equals(MainActivity.getUserData().getUserId()))
            chatAdapterPlaceHolder.message.setTextColor(Color.CYAN);
        else
            chatAdapterPlaceHolder.message.setTextColor(Color.BLACK);


        if (seen==1)
            chatAdapterPlaceHolder.imgSeen.setImageResource(R.drawable.ic_seen);
        else
            chatAdapterPlaceHolder.imgSeen.setImageResource(R.drawable.ic_notseen);

        chatAdapterPlaceHolder.message.setText(message);


    }

    @Override
    public int getItemViewType(int i) {
        cursor.moveToPosition(i);
        String sender=cursor.getString(cursor.getColumnIndex(ChatDbContract.SENDER));
            if (sender.equals(MainActivity.getUserData().getUserId())) {
                 return R.layout.rv_chatadapter_item;

            }else
                 return R.layout.rv_chatadapter_item2;

    }

    public void updateChatList(Cursor cursor)
    {
        this.cursor=cursor;
    }
    @Override
    public int getItemCount() {
        if (cursor!=null)
            return cursor.getCount();
        else
            return 0;
    }



    class ChatAdapterPlaceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvMessage)
        TextView message;
        @BindView(R.id.img_seen)
        ImageView imgSeen;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_time)
        TextView tvTime;
         ChatAdapterPlaceHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

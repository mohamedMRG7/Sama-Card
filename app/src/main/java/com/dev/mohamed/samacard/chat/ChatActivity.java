package com.dev.mohamed.samacard.chat;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.mohamed.samacard.CommonStaticKeys;
import com.dev.mohamed.samacard.MainActivity;
import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.contentProvider.CardsContentProvider;
import com.dev.mohamed.samacard.sqliteDb.DbContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity implements ChatFireBaseUtils.OnMessageResive ,View.OnClickListener {

    private String myID;

    ChatAdapter adapter;
    String chatWithID;
    String avatar;

    @BindView(R.id.rvChat)
    RecyclerView rvChat;
    @BindView(R.id.ed_message)
    EditText edMessage;
    @BindView(R.id.img_send)
    ImageView imgSendMessage;
    @BindView(R.id.img_avatar)
    ImageView imgAvatar;
    @BindView(R.id.tv_chatwith_name)
    TextView tvChatWithName;

    ArrayList<Chat> chatList;
    LinearLayoutManager manager;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        myID =MainActivity.getUserData().getUserId();

        chatWithID =getIntent().getExtras().getString(CommonStaticKeys.KEY_UID);

        avatar=CardsContentProvider.getSpecificData(this,DbContract.CardDataEntry.PHOTO_LINK,chatWithID);
        String chatWithName =CardsContentProvider.getSpecificData(this,DbContract.CardDataEntry.USER_NAME,chatWithID);
        if (avatar.isEmpty() && chatWithName.isEmpty())
        {
            edMessage.setEnabled(false);
            imgSendMessage.setEnabled(false);
            chatWithName="user";
            edMessage.setHint("User Dont Have Card Any More To Chat with");
            Picasso.with(this).load(R.drawable.bsetlogo).into(imgAvatar);
        }else
        {
            Picasso.with(this).load(avatar).into(imgAvatar);
        }

        tvChatWithName.setText(chatWithName);

        handler = new Handler();

        chatList = new ArrayList<>();
        adapter = new ChatAdapter();
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        rvChat.setLayoutManager(manager);
        rvChat.setAdapter(adapter);
        manager.setStackFromEnd(true);
        ChatFireBaseUtils.ReciveMessages(this);

        edMessage.requestFocus();
        edMessage.setOnClickListener(this);





    }


    @Override
    public void message(Chat chat) {
        String from = chat.getSender();
        String to = chat.getReciver();


            LocalDbUtalis.insertChat(this,chat);
            Cursor cursor=LocalDbUtalis.getAllChat(this, chatWithID);
            adapter.updateChatList(cursor);
            adapter.notifyDataSetChanged();
            manager.scrollToPosition(0);


        if (from.equals(chatWithID)&&!chat.isSeen())
            ChatFireBaseUtils.setSeen(chat.getMessageId(),from,to);


    }

    @Override
    public void seen(String messageID) {

        LocalDbUtalis.makeSeen(this,messageID);
        Cursor cursor=LocalDbUtalis.getAllChat(this, chatWithID);
        adapter.updateChatList(cursor);
        adapter.notifyDataSetChanged();

    }

    public void sendMessage(View view) {
        String message=edMessage.getText().toString();
        edMessage.setText("");


        ChatFireBaseUtils.sendMessage(myID, chatWithID,message);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id==edMessage.getId()) {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    manager.scrollToPosition(chatList.size()-1);
                }
            },1000);

        }

    }
}

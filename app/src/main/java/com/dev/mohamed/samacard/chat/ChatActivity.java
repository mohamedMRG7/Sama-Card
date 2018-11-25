package com.dev.mohamed.samacard.chat;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.dev.mohamed.samacard.CommonStaticKeys;
import com.dev.mohamed.samacard.MainActivity;
import com.dev.mohamed.samacard.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity implements ChatFireBaseUtils.OnMessageResive ,View.OnClickListener {

    private String myEmail ;

    ChatAdapter adapter;
    String chatWithEmail;

    @BindView(R.id.rvChat)
    RecyclerView rvChat;
    @BindView(R.id.ed_message)
    EditText edMessage;
    @BindView(R.id.img_send)
    ImageView imgSendMessage;

    ArrayList<Chat> chatList;
    LinearLayoutManager manager;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        myEmail=MainActivity.getMyEmail();
        chatWithEmail=getIntent().getExtras().getString(CommonStaticKeys.EMAIL_KEY);

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
        Cursor cursor=LocalDbUtalis.getAllChat(this,chatWithEmail);

        if (from.equals(myEmail)||to.equals(myEmail))
        {
            chat.setSender(from.replace("1","."));
            chat.setReciver(to.replace("1","."));
            LocalDbUtalis.insertChat(this,chat);
            adapter.updateChatList(cursor);
            adapter.notifyDataSetChanged();
            manager.scrollToPosition(0);

        }
        if (from.equals(chatWithEmail)&&!chat.isSeen())
            ChatFireBaseUtils.setSeen(chat.getMessageId(),from,to);





    }

    @Override
    public void seen(String messageID) {

        LocalDbUtalis.makeSeen(this,messageID);

    }

    public void sendMessage(View view) {
        String message=edMessage.getText().toString();
        edMessage.setText("");
        chatWithEmail=chatWithEmail.replace(".","1");
        myEmail=myEmail.replace(".","1");
        ChatFireBaseUtils.sendMessage(myEmail,chatWithEmail,message);
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

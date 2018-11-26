package com.dev.mohamed.samacard.chat;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dev.mohamed.samacard.MainActivity;
import com.dev.mohamed.samacard.R;

import butterknife.BindView;
import butterknife.ButterKnife;




public class MessagesListActivity extends AppCompatActivity implements ChatFireBaseUtils.OnMessageResive{

    MessageslistAdapter adapter;
    LinearLayoutManager manager;
    @BindView(R.id.rvMessagesList)
    RecyclerView rvMessagesList;

    private String myEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);
        ButterKnife.bind(this);

        myEmail=MainActivity.getMyEmail();
        adapter=new MessageslistAdapter(this);
        manager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvMessagesList.setLayoutManager(manager);
        rvMessagesList.setAdapter(adapter);


        ChatFireBaseUtils.ReciveMessages(this);

    }

    @Override
    public void message(Chat chat) {
        String from = chat.getSender().replace("+",".");
        String to = chat.getReciver().replace("+",".");

        Cursor cursor=LocalDbUtalis.getListOfMessagedUSers(this,myEmail);
        chat.setSender(from);
        chat.setReciver(to);
        if (from.equals(myEmail)||to.equals(myEmail))
        {
            LocalDbUtalis.insertChat(this,chat);
            adapter.setUsrsChatList(cursor);
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void seen(String messageID) {
        LocalDbUtalis.makeSeen(this,messageID);
    }
}

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

    private String myID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);
        ButterKnife.bind(this);

        myID =MainActivity.getUserData().getUserId();
        adapter=new MessageslistAdapter(this);
        manager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvMessagesList.setLayoutManager(manager);
        rvMessagesList.setAdapter(adapter);


        ChatFireBaseUtils.ReciveMessages(this);

    }

    @Override
    public void message(Chat chat) {


        Cursor cursor=LocalDbUtalis.getListOfMessagedUSers(this, myID);


            LocalDbUtalis.insertChat(this,chat);
            adapter.setUsrsChatList(cursor);
            adapter.notifyDataSetChanged();


    }

    @Override
    public void seen(String messageID) {
        LocalDbUtalis.makeSeen(this,messageID);
        Cursor cursor=LocalDbUtalis.getListOfMessagedUSers(this, myID);
        adapter.setUsrsChatList(cursor);
        adapter.notifyDataSetChanged();
    }
}

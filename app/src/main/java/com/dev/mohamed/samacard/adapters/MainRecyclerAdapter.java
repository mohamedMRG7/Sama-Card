package com.dev.mohamed.samacard.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.mohamed.samacard.R;
import com.dev.mohamed.samacard.card.CardActivity;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by moham on 3/6/2018.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainRecycelerAdapterViewholder>{


    String [] images= {"http://brandmark.io/logo-rank/random/pepsi.png"
            ,"https://upload.wikimedia.org/wikipedia/commons/4/4c/Logo-Free.jpg"
            ,"https://www.freelogodesign.org/img/logo-ex-7.png"
            ,"https://upload.wikimedia.org/wikipedia/commons/a/ab/Logo_TV_2015.png"};

    String [] names={"Mohamed Ragab","Ahmed bo7sen","Mahmoud ahmed","Mos3ad ebrahem","Saud elsayed"};
    Context context;
    @Override
    public MainRecycelerAdapterViewholder onCreateViewHolder(ViewGroup parent, int viewType) {


        context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.rv_item,parent,false);


        return new MainRecycelerAdapterViewholder(view);
    }

    @Override
    public void onBindViewHolder(MainRecycelerAdapterViewholder holder, int position) {

        Picasso.with(context).load(images[position]).into(holder.logo);
        holder.userName.setText(names[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    class MainRecycelerAdapterViewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.img_logo)CircularImageView logo;
        @BindView(R.id.tv_userName)TextView userName;

        public MainRecycelerAdapterViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent=new Intent(context, CardActivity.class);
            context.startActivity(intent);
        }
    }
}

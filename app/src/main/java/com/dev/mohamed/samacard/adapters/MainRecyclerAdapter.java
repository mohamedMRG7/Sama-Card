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


    String [] images= {"https://scontent-cai1-1.xx.fbcdn.net/v/t1.0-9/18485817_1741335819215943_5407189630743605678_n.jpg?oh=3aefc25f3cddfbf88f01128d6d5f4e05&oe=5B3BE923"
            ,"https://scontent-cai1-1.xx.fbcdn.net/v/t1.0-9/21077517_1575516205840605_3574721175345761590_n.jpg?oh=9078b01c7154ce3fd380f9aab52b14b3&oe=5B436D0D"
            ,"https://scontent-cai1-1.xx.fbcdn.net/v/t1.0-9/14516611_570362563148747_2537644147402450349_n.jpg?oh=3bea032219763f5239d935bd7a78093d&oe=5B4A52B4"
            ,"https://scontent-cai1-1.xx.fbcdn.net/v/t1.0-9/20663974_1420864141324114_7824265645260496196_n.jpg?oh=17b77b029c750fcd14d0508130c8a749&oe=5B0B9592"};

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

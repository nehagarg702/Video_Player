package com.example.myapplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.Video;
import com.example.myapplication.clickinterface.onClickListener;

import java.util.ArrayList;

public class RelatedAdapter extends RecyclerView.Adapter<RelatedAdapter.ViewHolder> {

    private static onClickListener clickListener;
    private ArrayList<Video> articles;
    private Context mContext;
    private String id;
    private int lastPosition = -1;

    public RelatedAdapter(Context mContext, ArrayList<Video> articles, String Id) {
        this.mContext = mContext;
        this.articles = articles;
        this.id=Id;
        for(int i=0;i<this.articles.size();i++)
        {
            Video v=articles.get(i);
            if(Id.equals(v.getid()))
            {
                articles.remove(v);
                break;
            }
        }
    }

    public void setOnItemClickListener(onClickListener myClickListener) {
        RelatedAdapter.clickListener = myClickListener;
    }

    @NonNull
    @Override
    public RelatedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_row, parent, false);
        return new RelatedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedAdapter.ViewHolder holder, int position) {
        String title = articles.get(position).getTitle();
        String id1=articles.get(position).getid();
        String desc=articles.get(position).getDescription();
        if(desc.length()>80)
            desc=desc.subSequence(0,80).toString()+"...";
        if(!(id1.equals(id))){
        holder.tv_card_main_title.setText(title);
        holder.description.setText(desc);
        Glide.with(mContext)
                .load(articles.get(position).getUrlToImage())
                .thumbnail(0.1f)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.img_card_main);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(articles.get(position), position);
            }
        });}


        if (position > lastPosition) {
            lastPosition = position;
        }

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_card_main_title;
        private TextView description;
        private ImageView img_card_main;
        private RelativeLayout cardView;

        public ViewHolder(View view) {
            super(view);
            tv_card_main_title = view.findViewById(R.id.textView4);
            description=view.findViewById(R.id.textView2);
            img_card_main = view.findViewById(R.id.imageView);
            cardView = view.findViewById(R.id.relative);
        }
    }
}

package com.example.myapplication.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.myapplication.database.MobioticsDb;
import com.example.myapplication.R;
import com.example.myapplication.model.Video;
import com.example.myapplication.clickinterface.onClickListener;

import java.util.ArrayList;


/*
** Adapter for adding data to Recycle View. Also used Glide to load the Image from url.
**/
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private static onClickListener clickListener;
    private ArrayList<Video> articles;
    private Context mContext;
    private int lastPosition = -1;

    public DataAdapter(Context mContext, ArrayList<Video> articles) {
        this.mContext = mContext;
        this.articles = articles;
    }

    public void setOnItemClickListener(onClickListener myClickListener) {
        DataAdapter.clickListener = myClickListener;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, final int position) {

        new MobioticsDb(mContext).addVideo(articles.get(position),"0");
        String title = articles.get(position).getTitle();
        String desc = articles.get(position).getDescription();
        if (desc.length() > 40)
            desc = desc.subSequence(0, 40).toString() + "...";
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
        });

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
        private CardView cardView;

        public ViewHolder(View view) {
            super(view);
            tv_card_main_title = view.findViewById(R.id.tv_card_main_title);
            description = view.findViewById(R.id.textView);
            img_card_main = view.findViewById(R.id.img_card_main);
            cardView = view.findViewById(R.id.card);
        }
    }
}

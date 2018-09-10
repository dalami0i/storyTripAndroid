package com.tripkorea.on.ontripkorea.tabs.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.tabs.list.ListDetailActivity;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;

import java.util.ArrayList;


/**
 * Created by Edward Won on 2018-08-21.
 */

public class SearchedRecyclerViewAdapter extends RecyclerView.Adapter<SearchedRecyclerViewAdapter.ViewHolder>{

    SearchActivity context;

    public SearchedRecyclerViewAdapter(Context context){
        this.context = (SearchActivity) context;
    }

    ArrayList<AttractionSimple> ltemList = new ArrayList<>();

    public void addListView(AttractionSimple obj) {//, String link_content
        ltemList.add(obj);
    }
    @Override
    public SearchedRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_searched_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchedRecyclerViewAdapter.ViewHolder holder, int position) {
        final AttractionSimple attractionSimple = ltemList.get(position);

        holder.tvSearchedTitle.setText(attractionSimple.getName()+"");
        holder.tvSearchedTag1.setText("#"+attractionSimple.getIdx());
        holder.tvSearchedTag2.setText("#"+attractionSimple.getIdx());
        holder.tvSearchedTag3.setText("#"+attractionSimple.getIdx());
//        RequestOptions myOptions = new RequestOptions().override(diviceSizeW, 900);
        Glide.with(MyApplication.getContext())
                .load(attractionSimple.getThumnailAddr())
                .into(holder.ivSearchedMain);
        //.apply(myOptions)

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListDetailActivity.class);
                intent.putExtra("attractionIdx",attractionSimple.getIdx());
                intent.putExtra("attractionType",attractionSimple.getType());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ltemList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivSearchedMain;
        private final TextView tvSearchedTitle;
        private final TextView tvSearchedTag1;
        private final TextView tvSearchedTag2;
        private final TextView tvSearchedTag3;
        private final View mView;

        private ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ivSearchedMain = mView.findViewById(R.id.iv_searched_main);
            tvSearchedTitle = mView.findViewById(R.id.tv_searched_title);
            tvSearchedTag1 = mView.findViewById(R.id.tv_searched_tag1);
            tvSearchedTag2 = mView.findViewById(R.id.tv_searched_tag2);
            tvSearchedTag3 = mView.findViewById(R.id.tv_searched_tag3);
        }
    }
}

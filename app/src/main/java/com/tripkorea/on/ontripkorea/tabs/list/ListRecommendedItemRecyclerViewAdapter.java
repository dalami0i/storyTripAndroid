package com.tripkorea.on.ontripkorea.tabs.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;

import java.util.ArrayList;


/**
 * Created by Edward Won on 2018-08-21.
 */

public class ListRecommendedItemRecyclerViewAdapter extends RecyclerView.Adapter<ListRecommendedItemRecyclerViewAdapter.ViewHolder>{

    MainActivity main;

    public ListRecommendedItemRecyclerViewAdapter(Context context){
        this.main = (MainActivity) context;
    }

    ArrayList<AttractionSimple> ltemList = new ArrayList<>();

    public void addListView(AttractionSimple obj) {//, String link_content
        ltemList.add(obj);
    }
    @Override
    public ListRecommendedItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rvmain_recommendation, parent, false));
    }

    @Override
    public void onBindViewHolder(ListRecommendedItemRecyclerViewAdapter.ViewHolder holder, int position) {
        final AttractionSimple attractionSimple = ltemList.get(position);

        holder.recommededTitle.setText(attractionSimple.getName()+"");
        holder.recommededTag.setText("#"+attractionSimple.getIdx());
//        RequestOptions myOptions = new RequestOptions().override(diviceSizeW, 900);
        Glide.with(MyApplication.getContext())
                .load(attractionSimple.getThumnailAddr())
                .into(holder.recommendedImg);
        //.apply(myOptions)

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main, ListDetailActivity.class);
                intent.putExtra("attractionIdx",attractionSimple.getIdx());
                intent.putExtra("attractionType",attractionSimple.getType());
                main.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ltemList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private final RoundedImageView recommendedImg;
        private final TextView recommededTag;
        private final TextView recommededTitle;
        private final View mView;

        private ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            recommendedImg = mView.findViewById(R.id.iv_recommended);
            recommededTag = mView.findViewById(R.id.tv_recommended_tag);
            recommededTitle = mView.findViewById(R.id.tv_recommend_title);
        }
    }
}

package com.tripkorea.on.ontripkorea.tabs.mypage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.tabs.list.ListDetailActivity;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;

import java.util.ArrayList;


///**
// * Created by Edward Won on 2018-08-21.
// */

public class MypageLikeRecyclerViewAdapter extends RecyclerView.Adapter<MypageLikeRecyclerViewAdapter.ViewHolder>{

    MainActivity context;
    ArrayList<AttractionSimple> itemList = new ArrayList<>();

    public MypageLikeRecyclerViewAdapter(){}

    public void addContext(Context context){this.context = (MainActivity) context;}
    public void addListView(AttractionSimple obj) {//, String link_content
        itemList.add(obj);
    }

    @NonNull
    @Override
    public MypageLikeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_mypage_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MypageLikeRecyclerViewAdapter.ViewHolder holder, int position) {
        final AttractionSimple attractionSimple = itemList.get(position);

        holder.tvSearchedTitle.setText(attractionSimple.getName());
        String tmpTag;
        if(attractionSimple.getTagSet() != null && attractionSimple.getTagSet().size() > 0){
            for(int i=0; i<attractionSimple.getTagSet().size(); i++) {
                switch (i){
                    case 0:
                        tmpTag = "#" + attractionSimple.getTagSet().get(i);
                        holder.tvSearchedTag1.setText(tmpTag);
                        holder.tvSearchedTag2.setVisibility(View.GONE);
                        holder.tvSearchedTag3.setVisibility(View.GONE);
                        break;
                    case 1:
                        tmpTag = "#" + attractionSimple.getTagSet().get(i);
                        holder.tvSearchedTag2.setText(tmpTag);
                        holder.tvSearchedTag2.setVisibility(View.VISIBLE);
                        holder.tvSearchedTag3.setVisibility(View.GONE);
                        break;
                    case 2:
                        tmpTag = "#" + attractionSimple.getTagSet().get(i);
                        holder.tvSearchedTag3.setText(tmpTag);
                        holder.tvSearchedTag3.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }else{
            String tmpTitle = "#"+attractionSimple.getName();
            holder.tvSearchedTag1.setText(tmpTitle);
            holder.tvSearchedTag2.setVisibility(View.GONE);
            holder.tvSearchedTag3.setVisibility(View.GONE);
        }
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
        return itemList.size();
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

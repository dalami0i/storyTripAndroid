package com.tripkorea.on.ontripkorea.tabs.map;

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
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.tabs.list.ListDetailActivity;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;

import java.util.ArrayList;


/**
 * Created by Edward Won on 2018-08-21.
 */

public class MapBottomRecyclerViewAdapter extends RecyclerView.Adapter<MapBottomRecyclerViewAdapter.ViewHolder>{

    Context context;

    public MapBottomRecyclerViewAdapter(){

    }

    ArrayList<AttractionSimple> itemList = new ArrayList<>();

    public void clearMapList(){ itemList.clear(); }
    public void addContext(MainActivity main){context = main;}
    public void addMapListView(AttractionSimple obj) {//, String link_content
        itemList.add(obj);
    }
    @Override
    public MapBottomRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_map_bottom_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MapBottomRecyclerViewAdapter.ViewHolder holder, int position) {
        final AttractionSimple attraction = itemList.get(position);
        new LogManager().LogManager("지도 맵 바닥 아이템",attraction.getName());
        holder.itemImgName.setText(attraction.getName());
        holder.itemTag1.setText("#"+attraction.getIdx());
        holder.itemTag2.setText("#"+attraction.getIdx());
        holder.itemTag3.setText("#"+attraction.getIdx());
//        RequestOptions myOptions = new RequestOptions().override(diviceSizeW, 900);
        Glide.with(MyApplication.getContext())
                .load(attraction.getThumnailAddr())
                .into(holder.itemImg);
        //.apply(myOptions)

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogManager().LogManager("ListItemRVAdapter","item clicked : "+attraction.getIdx());
                Intent intent = new Intent(context, ListDetailActivity.class);
                intent.putExtra("attractionIdx",attraction.getIdx());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView itemImg;
        private final TextView itemImgName;
        private final TextView itemTag1;
        private final TextView itemTag2;
        private final TextView itemTag3;
        private final View mView;

        private ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            itemImg = mView.findViewById(R.id.iv_map_bottom);
            itemImgName = mView.findViewById(R.id.tv_map_bottom_title);
            itemTag1 = mView.findViewById(R.id.tv_map_bottom_tag1);
            itemTag2 = mView.findViewById(R.id.tv_map_bottom_tag2);
            itemTag3 = mView.findViewById(R.id.tv_map_bottom_tag3);
        }
    }
}

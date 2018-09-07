package com.tripkorea.on.ontripkorea.tabs.list;

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
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;

import java.util.ArrayList;


/**
 * Created by Edward Won on 2018-08-21.
 */

public class ListItemRecyclerViewAdapter extends RecyclerView.Adapter<ListItemRecyclerViewAdapter.ViewHolder>{

    MainActivity main;

    ArrayList<AttractionSimple> ltemList = new ArrayList<>();

    public void clearList(){ltemList.clear();}
    public void addContext(MainActivity main){this.main = main;}
    public void addListView(AttractionSimple obj) {//, String link_content
        ltemList.add(obj);
    }
    @Override
    public ListItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_listitems, parent, false));
    }

    @Override
    public void onBindViewHolder(ListItemRecyclerViewAdapter.ViewHolder holder, int position) {
        final AttractionSimple attractionSimple = ltemList.get(position);

        holder.itemImgName.setText(attractionSimple.getName()+"");
        holder.itemTag1.setText("#"+attractionSimple.getIdx());
        holder.itemTag2.setText("#"+attractionSimple.getIdx());
        holder.itemTag3.setText("#"+attractionSimple.getIdx());
//        RequestOptions myOptions = new RequestOptions().override(diviceSizeW, 900);
        Glide.with(MyApplication.getContext())
                .load(attractionSimple.getThumnailAddr())
                .into(holder.itemImg);
        //.apply(myOptions)

        holder.itemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogManager().LogManager("ListItemRVAdapter","item clicked : "+attractionSimple.getIdx());
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
        private final RoundedImageView itemImg;
        private final TextView itemImgName;
        private final TextView itemTag1;
        private final TextView itemTag2;
        private final TextView itemTag3;
        private final View mView;

        private ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            itemImg = mView.findViewById(R.id.item_img);
            itemImgName = mView.findViewById(R.id.item_img_name);
            itemTag1 = mView.findViewById(R.id.item_tag1);
            itemTag2 = mView.findViewById(R.id.item_tag2);
            itemTag3 = mView.findViewById(R.id.item_tag3);
        }
    }
}

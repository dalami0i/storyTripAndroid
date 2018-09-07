package com.tripkorea.on.ontripkorea.tabs.food;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.tabs.around.detail.AroundDetailMapActivity;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleListSet;

import java.util.List;

///**
// * Created by YangHC on 2018-06-18.
// */

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//voice guide 이미지를 위한 RecyclerView
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class FoodPhotoRecyclerViewAdapter extends RecyclerView.Adapter<FoodPhotoRecyclerViewAdapter.ViewHolder> {
    public static final int VIEW_TYPE_ODD = 1;
    public static final int VIEW_TYPE_EVEN = 2;

    AttractionSimpleListSet foodPhotolistSet = new AttractionSimpleListSet();
    int diviceSizeW;
    static Context context;

    public void addFoodPhotoList(AttractionSimpleList obj) {//, String link_content
        foodPhotolistSet.getItems().add(obj);
    }

    public void setFoodPhotoList(List<AttractionSimple> addTemp){
        for(int i=0;i<addTemp.size(); i++){
            new LogManager().LogManager("푸드리싸", "if(x) 포토프레그먼트 이미지 배치: " + addTemp.get(i).getThumnailAddr() + " | i :" + i);
            int tmp = i;
            if(tmp+5 < addTemp.size()) {
                new LogManager().LogManager("푸드리싸", "전 포토프레그먼트 이미지 배치: " + addTemp.get(i).getThumnailAddr() + " | i :" + i);
                AttractionSimpleList tempList = new AttractionSimpleList();
                tempList.getItems().add(addTemp.get(i));
                tempList.getItems().add(addTemp.get(i+1));
                tempList.getItems().add(addTemp.get(i+2));
                tempList.getItems().add(addTemp.get(i+3));
                tempList.getItems().add(addTemp.get(i+4));
                tempList.getItems().add(addTemp.get(i+5));
                this.foodPhotolistSet.getItems().add(tempList);

                i = i + 5;
                new LogManager().LogManager("푸드리싸", "후 포토프레그먼트 이미지 배치: " + addTemp.get(i).getThumnailAddr() + " | i :" + i);
            }
        }

    }

    public FoodPhotoRecyclerViewAdapter() {    }

    public FoodPhotoRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView firstPhoto;
        private final ImageView secondPhoto;
        private final ImageView thirdPhoto;
        private final ImageView forthPhoto;
        private final ImageView fifthPhoto;
        private final ImageView sixthPhoto;
        private final View mView;

        private ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            firstPhoto = mView.findViewById(R.id.first_img);
            secondPhoto = mView.findViewById(R.id.second_img);
            thirdPhoto = mView.findViewById(R.id.third_img);
            forthPhoto = mView.findViewById(R.id.forth_img);
            fifthPhoto = mView.findViewById(R.id.fifth_img);
            sixthPhoto = mView.findViewById(R.id.sixth_img);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ODD) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_photo_layout, parent, false));
        }else{
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_photo_layout2, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AttractionSimpleList attractionSimpleList = foodPhotolistSet.getItems().get(position);
        Log.e("Food onBindViewHolder", position + " | " + attractionSimpleList.getItems().get(0).getThumnailAddr());

        RequestOptions myOptions = new RequestOptions().override(diviceSizeW, 900);
        Glide.with(MyApplication.getContext())
                .load(attractionSimpleList.getItems().get(0).getThumnailAddr())
                .apply(myOptions)
                .into(holder.firstPhoto);
        Glide.with(MyApplication.getContext())
                .load(attractionSimpleList.getItems().get(1).getThumnailAddr())
                .apply(myOptions)
                .into(holder.secondPhoto);
        Glide.with(MyApplication.getContext())
                .load(attractionSimpleList.getItems().get(2).getThumnailAddr())
                .apply(myOptions)
                .into(holder.thirdPhoto);
        Glide.with(MyApplication.getContext())
                .load(attractionSimpleList.getItems().get(3).getThumnailAddr())
                .apply(myOptions)
                .into(holder.forthPhoto);
        Glide.with(MyApplication.getContext())
                .load(attractionSimpleList.getItems().get(4).getThumnailAddr())
                .apply(myOptions)
                .into(holder.fifthPhoto);
        Glide.with(MyApplication.getContext())
                .load(attractionSimpleList.getItems().get(5).getThumnailAddr())
                .apply(myOptions)
                .into(holder.sixthPhoto);




        holder.firstPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AroundDetailMapActivity.class);
                intent.putExtra("attractionIdx",attractionSimpleList.getItems().get(0).getIdx());
                context.startActivity(intent);
            }
        });
        holder.secondPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AroundDetailMapActivity.class);
                intent.putExtra("attractionIdx",attractionSimpleList.getItems().get(1).getIdx());
                context.startActivity(intent);
            }
        });
        holder.thirdPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AroundDetailMapActivity.class);
                intent.putExtra("attractionIdx",attractionSimpleList.getItems().get(2).getIdx());
                context.startActivity(intent);
            }
        });
        holder.forthPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AroundDetailMapActivity.class);
                intent.putExtra("attractionIdx",attractionSimpleList.getItems().get(3).getIdx());
                context.startActivity(intent);
            }
        });
        holder.fifthPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AroundDetailMapActivity.class);
                intent.putExtra("attractionIdx",attractionSimpleList.getItems().get(4).getIdx());
                context.startActivity(intent);
            }
        });
        holder.sixthPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AroundDetailMapActivity.class);
                intent.putExtra("attractionIdx",attractionSimpleList.getItems().get(5).getIdx());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodPhotolistSet.getItems().size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position%2 == 1){
            return VIEW_TYPE_ODD;
        }else{
            return VIEW_TYPE_EVEN;
        }
    }
}
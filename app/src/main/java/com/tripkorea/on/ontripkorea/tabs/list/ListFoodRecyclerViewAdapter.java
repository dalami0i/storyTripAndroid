package com.tripkorea.on.ontripkorea.tabs.list;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.retrofit.message.ApiMessage;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.dto.LikeDTO;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


///**
// * Created by Edward Won on 2018-08-21.
// */

public class ListFoodRecyclerViewAdapter extends RecyclerView.Adapter<ListFoodRecyclerViewAdapter.ViewHolder>{

    MainActivity main;

    ArrayList<AttractionSimple> itemList = new ArrayList<>();

    public void clearList(){itemList.clear();}
    public void addContext(MainActivity main){ this.main = main;}
    public void addListView(AttractionSimple obj) {//, String
        itemList.add(obj);
    }
    @NonNull
    @Override
    public ListFoodRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_listitems, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ListFoodRecyclerViewAdapter.ViewHolder holder, int position) {
        final AttractionSimple attractionSimple = itemList.get(position);
        final int pos = position;

        holder.itemImgName.setText(attractionSimple.getName());
        String tmpTag;
        if(attractionSimple.getTagSet() != null && attractionSimple.getTagSet().size() > 0){
            for(int i=0; i<attractionSimple.getTagSet().size(); i++) {
                switch (i){
                    case 0:
                        tmpTag = "#" + attractionSimple.getTagSet().get(i);
                        holder.itemTag1.setText(tmpTag);
                        holder.itemTag2.setVisibility(View.GONE);
                        holder.itemTag3.setVisibility(View.GONE);
                        break;
                    case 1:
                        tmpTag = "#" + attractionSimple.getTagSet().get(i);
                        holder.itemTag2.setText(tmpTag);
                        holder.itemTag2.setVisibility(View.VISIBLE);
                        holder.itemTag3.setVisibility(View.GONE);
                        break;
                    case 2:
                        tmpTag = "#" + attractionSimple.getTagSet().get(i);
                        holder.itemTag3.setText(tmpTag);
                        holder.itemTag3.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }else{
            holder.itemTag1.setText(attractionSimple.getName());
            holder.itemTag2.setVisibility(View.GONE);
            holder.itemTag3.setVisibility(View.GONE);
        }

        if(attractionSimple.isLiked()) {
            holder.itemLike.setImageResource(R.drawable.icon_heart_image);
        }else{
            holder.itemLike.setImageResource(R.drawable.icon_heart_empty_image);
        }

        holder.itemLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attractionSimple.isLiked()) {
                    ApiClient.getInstance().getApiService()
                            .cancelLike(MyApplication.APP_VERSION, new LikeDTO(attractionSimple.getIdx()))
                            .enqueue(new Callback<ApiMessage>(){
                                @Override
                                public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                                    if (response.body() != null) {
                                        Alert.makeText("좋아요 취소!");
                                        holder.itemLike.setImageResource(R.drawable.icon_heart_empty_image);
                                        attractionSimple.setLiked(false);
                                        MainActivity.foodList.getItems().get(pos).setLiked(false);
                                    } else {
                                        Alert.makeText("좋아요 취소 에러 발생" + response.errorBody().toString());
                                        try {
                                            Log.e("LIKE", "error : " + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiMessage> call, Throwable t) {
                                    Alert.makeText(MyApplication.getContext().getResources().getString(R.string.network_error));
                                }
                            });
                }else{
                    ApiClient.getInstance().getApiService()
                            .like(MyApplication.APP_VERSION, new LikeDTO(attractionSimple.getIdx()))
                            .enqueue(new Callback<ApiMessage>(){
                                @Override
                                public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                                    if (response.body() != null) {
                                        Alert.makeText("좋아요!");
                                        holder.itemLike.setImageResource(R.drawable.icon_heart_image);
                                        attractionSimple.setLiked(true);
                                        MainActivity.foodList.getItems().get(pos).setLiked(true);
                                    } else {
                                        Alert.makeText("좋아요 에러 발생" + response.errorBody().toString());
                                        try {
                                            Log.e("LIKE", "error : " + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiMessage> call, Throwable t) {
                                    Alert.makeText(MyApplication.getContext().getResources().getString(R.string.network_error));
                                }
                            });

                }
            }
        });
//        RequestOptions myOptions = new RequestOptions().override(diviceSizeW, 900);
        Glide.with(MyApplication.getContext())
                .load(attractionSimple.getThumnailAddr())
                .into(holder.itemImg);
        //.apply(myOptions)

        holder.itemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogManager().LogManager("ListItemFoodRVAdapter","item clicked : "+attractionSimple.getIdx());
                Intent intent = new Intent(main, ListDetailActivity.class);
                intent.putExtra("attractionIdx",attractionSimple.getIdx());
                intent.putExtra("attractionType",attractionSimple.getType());
                main.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {
        private final RoundedImageView itemImg;
        private final TextView itemImgName;
        private final TextView itemTag1;
        private final TextView itemTag2;
        private final TextView itemTag3;
        private final ImageView itemLike;
        private final View mView;

        private ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            itemImg = mView.findViewById(R.id.item_img);
            itemImgName = mView.findViewById(R.id.item_img_name);
            itemTag1 = mView.findViewById(R.id.item_tag1);
            itemTag2 = mView.findViewById(R.id.item_tag2);
            itemTag3 = mView.findViewById(R.id.item_tag3);
            itemLike = mView.findViewById(R.id.iv_item_heart);
        }
    }


}

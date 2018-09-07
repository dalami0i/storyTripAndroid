package com.tripkorea.on.ontripkorea.tabs.toon;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.util.MyApplication;

import java.util.ArrayList;

///**
// * Created by YangHC on 2018-06-18.
// */

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//voice guide 이미지를 위한 RecyclerView
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class ToonShowImageRecyclerViewAdapter extends RecyclerView.Adapter<ToonShowImageRecyclerViewAdapter.ViewHolder> {
    ArrayList<String> toonIMGlist = new ArrayList<>();
    int diviceSizeW;
    Context context;

    public void addToonImgList(String obj) {//, String link_content
        toonIMGlist.add(obj);
    }

    public ToonShowImageRecyclerViewAdapter() {
    }

    public ToonShowImageRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView toonImgItem;
//        private final TextView voiceguide_img_text;
        private final TextView rightArrow;
        private final TextView leftArrow;
        private final View mView;

        private ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            toonImgItem = mView.findViewById(R.id.detailed_toon_view);
//            voiceguide_img_text = mView.findViewById(R.id.txt_voiceguide);
            rightArrow = mView.findViewById(R.id.toon_right_arrow);
            leftArrow = mView.findViewById(R.id.toon_left_arrow);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_toon_detail_img, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String toonIMGEntity = toonIMGlist.get(position);
        Log.e("맵onBindViewHolder", position + " | " + toonIMGEntity);
//        if(toonIMGEntity.getImgAddress().length() < 50) {
//            toonIMGEntity.setImgAddress("http://13.209.61.27:8080/resources/images/img/" + toonIMGEntity.getImgAddress() + ".jpg");
//        }\
        /*holder.voiceguide_img_text.setText(voiceIMGEntity.getDescription());
        holder.voiceguide_img_item.setCornerRadius((float) 10);*/

        RequestOptions myOptions = new RequestOptions().fitCenter().override(diviceSizeW, 900);

        String imgAddr = "http://13.125.83.183:8080/resources/guides/cartoon/"+toonIMGEntity;
        Glide.with(MyApplication.getContext())
                .load(imgAddr)
                .apply(myOptions)
                .into(holder.toonImgItem);

//            Glide.get(VoiceGuideMapActivity.this).clearMemory();
//            new AsyncTaskGlideCacheManager().doInBackground();
//            holder.voiceguide_img_item.setImageURI(Uri.parse(voiceIMGEntity.imgAddr));
        if (toonIMGlist.size() == 1) {
            holder.rightArrow.setVisibility(View.GONE);
            holder.leftArrow.setVisibility(View.GONE);
        } else if (position == 0) {
            holder.rightArrow.setVisibility(View.GONE);
            holder.leftArrow.setVisibility(View.VISIBLE);
        } else if (position == toonIMGlist.size() - 1) {
            holder.rightArrow.setVisibility(View.VISIBLE);
            holder.leftArrow.setVisibility(View.GONE);
        } else {
            holder.rightArrow.setVisibility(View.VISIBLE);
            holder.leftArrow.setVisibility(View.VISIBLE);
        }





    }

    @Override
    public int getItemCount() {
        return toonIMGlist.size();
    }


}
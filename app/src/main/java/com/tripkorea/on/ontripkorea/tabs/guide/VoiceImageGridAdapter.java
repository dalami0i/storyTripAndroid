package com.tripkorea.on.ontripkorea.tabs.guide;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.voiceguide.Guide;
import com.tripkorea.on.ontripkorea.vo.voiceguide.GuideImage;
import com.tripkorea.on.ontripkorea.vo.voiceguide.VoiceGuideImageEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward Won on 2018-07-30.
 */

public class VoiceImageGridAdapter extends BaseAdapter {

    Context context;
    List<GuideImage> imgListEntity = new ArrayList<>();//VoiceGuideImageEntity
    int layout;
    LayoutInflater layoutInflater;

    public VoiceImageGridAdapter(Context context, int layout, Guide imgListEntity){//VoiceGuideImageEntity
        this.context = context;
        this.imgListEntity = imgListEntity.getGuideImageList();
        new LogManager().LogManager("VoiceImageGridAdapter","imgList.size(): "+imgListEntity.getGuideImageList().size());
        this.layout = layout;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imgListEntity.size();
    }

    @Override
    public Object getItem(int position) {
        return imgListEntity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) convertView = layoutInflater.inflate(layout,null);
        ImageView iv = convertView.findViewById(R.id.voice_img_in_grid);

        RequestOptions myOptions = new RequestOptions().fitCenter().override(900);

        String imgAddr = "http://13.125.83.183:8080/resources/guides/voice/img/" + imgListEntity.get(position).getImgAddress() + ".jpg";

        new LogManager().LogManager("VoiceImageGridAdapter","imgAddr: "+imgAddr);
        Glide.with(MyApplication.getContext())
                .load(imgAddr)
                .apply(myOptions)
                .into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voiceIMGDetail = new Intent(context, GuideDetailImageActivity.class);
                VoiceGuideImageEntity imageEntity = new VoiceGuideImageEntity();
                imageEntity.setVoiceGuideList(imgListEntity);
                voiceIMGDetail.putExtra("images",imageEntity);
                context.startActivity(voiceIMGDetail);
            }
        });



        return convertView;
    }
}

package com.tripkorea.on.ontripkorea.tabs.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.voiceguide.VoiceGuideImageEntity;

///**
// * Created by Edward Won on 2018-07-16.
// */

public class GuideDetailImageActivity extends AppCompatActivity {

    VoiceGuideImageEntity voiceGuideImageEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_detail_img);

        Intent intent = getIntent();
        Bundle bundleExtra = intent.getExtras();
        voiceGuideImageEntity = bundleExtra.getParcelable("images");

        RecyclerView showImgRV = findViewById(R.id.rv_detailed_image);

        LinearLayoutManager linkLayoutManager
                = new LinearLayoutManager(MyApplication.getContext(), LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        showImgRV.setLayoutManager(linkLayoutManager);
        snapHelper.attachToRecyclerView(showImgRV);
        VoiceShowImageRecyclerViewAdapter showImageRecyclerViewAdapter
                = new VoiceShowImageRecyclerViewAdapter(this);
        for(int i=0;i<voiceGuideImageEntity.getVoiceGuideList().size(); i++){
            showImageRecyclerViewAdapter.addVoiceImgList(voiceGuideImageEntity.getVoiceGuideList().get(i));
        }
        showImgRV.setAdapter(showImageRecyclerViewAdapter);

    }
}

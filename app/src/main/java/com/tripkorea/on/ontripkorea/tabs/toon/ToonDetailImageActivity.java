package com.tripkorea.on.ontripkorea.tabs.toon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.toonguide.ToonImageEntity;

///**
// * Created by Edward Won on 2018-07-16.
// */

public class ToonDetailImageActivity extends AppCompatActivity {

//    ToonImageEntity toon;
    ToonImageEntity toonImages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toon_detail_img);

        Intent intent = getIntent();
        Bundle bundleExtra = intent.getExtras();
        toonImages = bundleExtra.getParcelable("toons");
        if(toonImages != null){
            if(toonImages != null) {
                new LogManager().LogManager("ToonDetailImageActivity-listSize", toonImages.getToonList().size() + "|");
            }else{
                new LogManager().LogManager("ToonDetailImageActivity","toon.getCartoonImageAddressList() is null");
            }
        }else{
            new LogManager().LogManager("ToonDetailImageActivity","toon is null");
        }
        RecyclerView showImgRV = findViewById(R.id.rv_toon_image);

        LinearLayoutManager linkLayoutManager
                = new LinearLayoutManager(MyApplication.getContext(), LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        showImgRV.setLayoutManager(linkLayoutManager);
        snapHelper.attachToRecyclerView(showImgRV);
        ToonShowImageRecyclerViewAdapter showToonImageRecyclerViewAdapter
                = new ToonShowImageRecyclerViewAdapter(this);
        for(int i=0;i<toonImages.getToonList().size(); i++){
            showToonImageRecyclerViewAdapter.addToonImgList(toonImages.getToonList().get(i));
        }
        showImgRV.setAdapter(showToonImageRecyclerViewAdapter);

    }
}

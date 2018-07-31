package com.tripkorea.on.ontripkorea.tabs.total;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.util.OnNetworkErrorListener;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;

/**
 * Created by Edward Won on 2018-07-24.
 */

public class PhotoTotalFragment extends Fragment {

    int lastTab;
    MainActivity main;
    AttractionSimpleList totalList;
    RecyclerView photoTotalRV;


    public Fragment photoTotalFragmentNewInstance(MainActivity main, int lastTab, AttractionSimpleList totalList){
        this.lastTab = lastTab;
        this.main = main;
        this.totalList = totalList;
        return new PhotoTotalFragment();

    }

    public OnNetworkErrorListener onNetworkErrorListener;

    public void setOnNetworkErrorListener(OnNetworkErrorListener onNetworkErrorListener) {
        this.onNetworkErrorListener = onNetworkErrorListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_photo, container, false);
        new LogManager().LogManager("메엑티비티","포토프레그먼트진입 list size: "+totalList.getItems().size());
        photoTotalRV = view.findViewById(R.id.photo_rv_layout);

        LinearLayoutManager linkLayoutManager
                = new LinearLayoutManager(MyApplication.getContext(), LinearLayoutManager.VERTICAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        photoTotalRV.setLayoutManager(linkLayoutManager);
//        snapHelper.attachToRecyclerView(photoFoodRV);
        TotalPhotoRecyclerViewAdapter totalPhotoRecyclerViewAdapter
                = new TotalPhotoRecyclerViewAdapter(main);
        for(int i=0;i<totalList.getItems().size(); i++){
            new LogManager().LogManager("메엑티비티", "if(x) 포토프레그먼트 이미지 배치: " + totalList.getItems().get(i).getThumnailAddr() + " | i :" + i);
            int tmp = i;
            if(tmp+5 < totalList.getItems().size()) {
                new LogManager().LogManager("메엑티비티", "전 포토프레그먼트 이미지 배치: " + totalList.getItems().get(i).getThumnailAddr() + " | i :" + i);
                AttractionSimpleList tempList = new AttractionSimpleList();
                tempList.getItems().add(totalList.getItems().get(i));
                tempList.getItems().add(totalList.getItems().get(i + 1));
                tempList.getItems().add(totalList.getItems().get(i + 2));
                tempList.getItems().add(totalList.getItems().get(i + 3));
                tempList.getItems().add(totalList.getItems().get(i + 4));
                tempList.getItems().add(totalList.getItems().get(i + 5));
                totalPhotoRecyclerViewAdapter.addTotalPhotoList(tempList);

                i = i + 5;
                new LogManager().LogManager("메엑티비티", "후 포토프레그먼트 이미지 배치: " + totalList.getItems().get(i).getThumnailAddr() + " | i :" + i);
            }
        }
        photoTotalRV.setAdapter(totalPhotoRecyclerViewAdapter);

        return view;
    }
}

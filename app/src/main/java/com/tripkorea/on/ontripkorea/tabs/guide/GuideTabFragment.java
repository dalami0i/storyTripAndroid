package com.tripkorea.on.ontripkorea.tabs.guide;

import android.app.ProgressDialog;
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
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.util.OnNetworkErrorListener;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;

/**
 * Created by Edward Won on 2018-07-24.
 */

public class GuideTabFragment extends Fragment {

    int lastTab;
    MainActivity main;
    RecyclerView guideMainRV;
    AttractionSimpleList guideList;
    ProgressDialog loadingGuide = null;


    public Fragment guideTabFragmentNewInstance(MainActivity main, int lastTab, AttractionSimpleList guideList){
        this.lastTab = lastTab;
        this.main = main;
        this.guideList = guideList;
        return new GuideTabFragment();

    }

    public OnNetworkErrorListener onNetworkErrorListener;

    public void setOnNetworkErrorListener(OnNetworkErrorListener onNetworkErrorListener) {
        this.onNetworkErrorListener = onNetworkErrorListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loadingGuide = ProgressDialog.show(main, "", "Getting images...", true);
        View view = inflater.inflate(R.layout.fragment_guide_main, container, false);
        guideMainRV = view.findViewById(R.id.rv_guide_main);

        LinearLayoutManager linkLayoutManager
                = new LinearLayoutManager(MyApplication.getContext(), LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        guideMainRV.setLayoutManager(linkLayoutManager);
        snapHelper.attachToRecyclerView(guideMainRV);
        final GuideTabRecyclerViewAdapter guideTabRecyclerViewAdapter
                = new GuideTabRecyclerViewAdapter(main);
        guideList = null;
        if(guideList!=null && guideList.getItems().size() > 0){
            for(int i=0; i<guideList.getItems().size(); i++) {
                guideTabRecyclerViewAdapter.addGuideTabPhotoList(guideList.getItems().get(i));
            }
        }else{
            AttractionSimple tmpGuide1 = new AttractionSimple();
            tmpGuide1.setIdx(8344);
            tmpGuide1.setName("Changdeokgung-Toon");
            tmpGuide1.setThumnailAddr("http://tong.visitkorea.or.kr/cms/resource/40/1568040_image2_1.jpg");
            tmpGuide1.setGuideType(1000);
            guideTabRecyclerViewAdapter.addGuideTabPhotoList(tmpGuide1);
            AttractionSimple tmpGuide2 = new AttractionSimple();
            tmpGuide2.setIdx(8344);
            tmpGuide2.setName("Changdeokgung-Voice");
            tmpGuide2.setThumnailAddr("http://tong.visitkorea.or.kr/cms/resource/49/1954549_image2_1.jpg");
            tmpGuide2.setGuideType(2000);
            guideTabRecyclerViewAdapter.addGuideTabPhotoList(tmpGuide2);
        }

        guideMainRV.setAdapter(guideTabRecyclerViewAdapter);


        loadingGuide.dismiss();

        return view;
    }
}

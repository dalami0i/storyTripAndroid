package com.tripkorea.on.ontripkorea.tabs.mypage;

import android.os.Bundle;
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
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;

/**
 * Created by Edward Won on 2018-09-12.
 */

public class MypageVisitedFragment extends Fragment {

    private AttractionSimpleList visitedList;
    private int language;
    private int page;


    private final MypageVisitedRecyclerViewAdapter mypageVisitedRecyclerViewAdapter
            = new MypageVisitedRecyclerViewAdapter();

    public Fragment newInstanceMypageVisitedFragment(int language,  MainActivity main, AttractionSimpleList visitedList){
        this.language = language;
        mypageVisitedRecyclerViewAdapter.addContext(main);
        this.visitedList = visitedList;
        return new MypageVisitedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_rv, container, false);
        RecyclerView listRV = view.findViewById(R.id.item_list);
        LinearLayoutManager linkLayoutManager
                = new LinearLayoutManager(MyApplication.getContext(), LinearLayoutManager.VERTICAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        listRV.setLayoutManager(linkLayoutManager);
//        snapHelper.attachToRecyclerView(photoFoodRV);

        for (AttractionSimple attractionSimple:visitedList.getItems()) {
            mypageVisitedRecyclerViewAdapter.addListView(attractionSimple);
        }
        listRV.setAdapter(mypageVisitedRecyclerViewAdapter);

        return view;
    }



}

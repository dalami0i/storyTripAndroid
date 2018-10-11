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

public class MypageLikeFragment extends Fragment {

    private AttractionSimpleList likeList;
    private int language;
    private int page;


    private final MypageLikeRecyclerViewAdapter mypageLikeRecyclerViewAdapter
            = new MypageLikeRecyclerViewAdapter();

    public Fragment newInstanceMypageLikeFragment(int language,  MainActivity main, AttractionSimpleList likeList){
        this.language = language;
        mypageLikeRecyclerViewAdapter.addContext(main);
        this.likeList = likeList;
        return new MypageLikeFragment();
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

        for (AttractionSimple attractionSimple:likeList.getItems()) {
            mypageLikeRecyclerViewAdapter.addListView(attractionSimple);
        }
        listRV.setAdapter(mypageLikeRecyclerViewAdapter);

        return view;
    }



}

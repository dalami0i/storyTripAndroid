package com.tripkorea.on.ontripkorea.tabs.map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward Won on 2018-09-12.
 */

public class MapTourBottomPagerAdapter extends FragmentStatePagerAdapter {

    List<AttractionSimple> itemList = new ArrayList<>();
    private MainActivity main;

    public MapTourBottomPagerAdapter(FragmentManager fm, MainActivity main, AttractionSimpleList mapList) {
        super(fm);
        new LogManager().LogManager("MapTourBottomPagerAdapter","MapTourBottomPagerAdapter 생성 ");
        this.main = main;
        itemList = mapList.getItems();
    }
    public void clearMapList(){ itemList.clear(); }
    public void changeList(AttractionSimpleList mapList){ itemList = mapList.getItems(); }



    @Override
    public Fragment getItem(int position) {
        MapTourBottomFragment mapTourBottomFragment = new MapTourBottomFragment();
        new LogManager().LogManager("MapTourBottomPagerAdapter","itemList.get(position).getName(): "+itemList.get(position).getName());
        mapTourBottomFragment.newInstanceMapBootomFragment(itemList.get(position),main);
        return mapTourBottomFragment;
    }



    @Override
    public int getCount() {
        return itemList.size();
    }
}

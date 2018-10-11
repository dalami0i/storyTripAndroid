package com.tripkorea.on.ontripkorea.tabs.list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;

/**
 * Created by Edward Won on 2018-09-12.
 */

public class ListPagerAdapter extends FragmentStatePagerAdapter {

    private int count;
    private double lat;
    private double lon;
    private int language;
    private int page;
    private AttractionSimpleList totalList;
    private AttractionSimpleList tourList;
    private AttractionSimpleList foodList;
    private MainActivity main;

    public ListPagerAdapter(FragmentManager fm, int count, double lat, double lon, int language, int page, MainActivity main,
                            AttractionSimpleList totalList,
                            AttractionSimpleList tourList,
                            AttractionSimpleList foodList) {
        super(fm);
        this.count = count;
        this.lat = lat;
        this.lon = lon;
        this.language = language;
        this.page = page;
        this.main = main;
        this.totalList = totalList;
        this.tourList = tourList;
        this.foodList = foodList;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ListTotalFragment totalFragment = new ListTotalFragment();
                totalFragment.newInstanceListTotalFragment(totalList, lat, lon, language, page, main);
                return totalFragment;
            case 1:
                ListTourFragment tourFragment = new ListTourFragment();
                tourFragment.newInstanceListTourFragment(tourList, lat, lon, language, page, main);
                return tourFragment;
            case 2:
                ListFoodFragment foodFragment = new ListFoodFragment();
                foodFragment.newInstanceListFoodFragment(foodList, lat, lon, language, page, main);
                return foodFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}

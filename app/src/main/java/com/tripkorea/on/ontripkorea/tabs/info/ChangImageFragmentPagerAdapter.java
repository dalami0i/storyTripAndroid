package com.tripkorea.on.ontripkorea.tabs.info;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by YangHC on 2018-06-18.
 */

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//디테일 상단 이미지를 위한 view pager
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ChangImageFragmentPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<String> detailedImageFragments = new ArrayList<>();

    public void addChangImage(String detailedImage){
        detailedImageFragments.add(detailedImage);
    }


    public ChangImageFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public float getPageWidth(int position) {
        return 1.0f;
    }

    @Override
    public Fragment getItem(int position) {
        return new DetailedImageListFragment().newInstance(
                detailedImageFragments.get(position), detailedImageFragments.size(), position);
    }

    @Override
    public int getCount() {
        return detailedImageFragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}

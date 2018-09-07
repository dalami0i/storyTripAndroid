package com.tripkorea.on.ontripkorea.tabs.list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by YangHC on 2018-06-18.
 */

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//디테일 상단 이미지를 위한 ViewPagerAdapter
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class ListDetailedImageFragmentPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<String> detailedImageFragments = new ArrayList<>();
    int popular;

    public void addDetailedImage(String detailedImage) {
        detailedImageFragments.add(detailedImage);
    }


    public ListDetailedImageFragmentPagerAdapter(FragmentManager fm, int popular) {
        super(fm);
        this.popular = popular;
    }

    @Override
    public float getPageWidth(int position) {
        return 1.0f;
    }

    @Override
    public Fragment getItem(int position) {
        return new ListDetailedImageListFragment().newInstance(
                detailedImageFragments.get(position), detailedImageFragments.size(), position, popular);
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
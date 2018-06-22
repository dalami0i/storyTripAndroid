package com.tripkorea.on.ontripkorea.tabs.intro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.tripkorea.on.ontripkorea.tabs.intro.IntroFragment;

import java.util.ArrayList;

/**
 * Created by YangHC on 2018-06-18.
 */

public class MainImageFragmentPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<String> mainImageFragments = new ArrayList<>();
//        ViewPager detailedImageVP;

    public void addMainImage(String mainImage){
        mainImageFragments.add(mainImage);
    }


    public MainImageFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
//            this.detailedImageVP = detailedImageVP;
    }

    @Override
    public float getPageWidth(int position) {
        return 1.0f;
    }

    @Override
    public Fragment getItem(int position) {
        return new MainImageListFragment().newInstance(
                mainImageFragments.get(position), mainImageFragments.size(), position);
    }

    @Override
    public int getCount() {
        return mainImageFragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
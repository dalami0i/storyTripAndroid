package com.tripkorea.on.ontripkorea.tabs.mypage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;

/**
 * Created by Edward Won on 2018-09-12.
 */

public class MypagePagerAdapter extends FragmentStatePagerAdapter {

    private int language;
    private AttractionSimpleList likeList;
    private AttractionSimpleList visiteList;
    private MainActivity main;

    public MypagePagerAdapter(FragmentManager fm, int language,  MainActivity main,
                              AttractionSimpleList likeList,
                              AttractionSimpleList visiteList) {
        super(fm);
        this.language = language;
        this.main = main;
        this.likeList = likeList;
        this.visiteList = visiteList;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MypageLikeFragment mypageLikeFragment = new MypageLikeFragment();
                mypageLikeFragment.newInstanceMypageLikeFragment(language, main, likeList);
                return mypageLikeFragment;
            case 1:
                MypageVisitedFragment mypageVisitedFragment = new MypageVisitedFragment();
                mypageVisitedFragment.newInstanceMypageVisitedFragment(language, main, visiteList);
                if(visiteList.getItems().size() < 1) Alert.makeText(main.getString(R.string.visit_no_history));
                return mypageVisitedFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}

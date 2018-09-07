package com.tripkorea.on.ontripkorea.util;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

import com.tripkorea.on.ontripkorea.R;

/**
 * Created by YangHC on 2018-06-05.
 */

public class MyTabLayout extends TabLayout {
    public static final int RES_ICON[] = {R.drawable.appbar_home_off, R.drawable.appbar_map, R.drawable.appbar_toon, R.drawable.appbar_my_off};
    public static final int TAB_TOTAL = 0;
    public static final int TAB_MAP = 1;
    public static final int TAB_GUIDE = 2;
    public static final int TAB_INFO = 3;

    public MyTabLayout(Context context) {
        super(context);
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}

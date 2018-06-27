package com.tripkorea.on.ontripkorea.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by YangHC on 2018-06-20.
 */

//RecyclerView의 Scroll을 막기위한 Class
public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = false;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}

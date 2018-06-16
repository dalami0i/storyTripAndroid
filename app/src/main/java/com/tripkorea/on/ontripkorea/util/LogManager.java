package com.tripkorea.on.ontripkorea.util;

import android.util.Log;

/**
 * Created by Edward Won on 2018-06-11.
 */

public class LogManager {
    boolean develope = true;

    public void LogManager(String title, String content){
        if(develope) {
            Log.e(title, content);
        }
    }
}
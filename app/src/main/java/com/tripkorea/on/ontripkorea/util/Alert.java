package com.tripkorea.on.ontripkorea.util;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by YangHC on 2017-07-29.
 */

public class Alert {
    public static void makeText(String s){
        try {
            Toast.makeText(MyApplication.getContext(), s, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e("ALERT_EXCEPTION",e.toString());
        }
    }
}

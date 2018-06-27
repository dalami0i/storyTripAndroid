package com.tripkorea.on.ontripkorea.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

///**
// * Created by Edward Won on 2017-04-19.
// */

public class NetworkUtil {
    private static int TYPE_WIFI = 1;
    private static int TYPE_MOBILE = 2;

    private static int getConnectivityStatus(Context context) {
        int TYPE_NOT_CONNECTED = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                    && networkInfo.getState() == NetworkInfo.State.CONNECTED) {

                return TYPE_WIFI;

            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE
                    && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                return TYPE_MOBILE;
            }
        }
        return TYPE_NOT_CONNECTED;
    }

    public static boolean isNetworkConnected(Context context) {
        int networkStatus = getConnectivityStatus(context);
        if (networkStatus == TYPE_WIFI || networkStatus == TYPE_MOBILE) {
            return true;
        } else {
            return false;
        }
    }
}


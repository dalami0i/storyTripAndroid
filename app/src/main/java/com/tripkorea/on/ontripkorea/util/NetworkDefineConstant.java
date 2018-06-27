package com.tripkorea.on.ontripkorea.util;

import com.tripkorea.on.ontripkorea.R;

/**
 * Created by Edward Won on 2018-06-11.
 */

public class NetworkDefineConstant {

    public static final String HOST_URL = MyApplication.getContext().getString(R.string.serverip);//http://192.168.0.8
    //    public static final int PORT_NUMBER= 8080;
    public static final int PORT_NUMBER= 8512;

    public static final String SERVER_CONTENT_TOTAL;

    static{

        SERVER_CONTENT_TOTAL = HOST_URL +PORT_NUMBER+MyApplication.getContext().getString(R.string.SERVER_CONTENT_TOTAL);//retrofit으로 변경

    }
}

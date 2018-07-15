package com.tripkorea.on.ontripkorea.util;

import android.widget.Toast;

import com.tripkorea.on.ontripkorea.R;

/**
 * Created by Edward Won on 2018-07-13.
 */

public class InternetTotalCheck {

    public static boolean checkInternet(){
        boolean internet = false;
        if(NetworkUtil.isNetworkConnected(MyApplication.getContext())){
            WifiCheck.CheckConnect cc = new WifiCheck.CheckConnect(WifiCheck.CONNECTION_CONFIRM_CLIENT_URL);
            cc.start();
            try {
                cc.join();
                if (WifiCheck.wificheck == 1) {
                    return internet = true;
                } else {  Toast.makeText(MyApplication.getContext(), R.string.securedWifi, Toast.LENGTH_LONG).show(); }
            }catch (Exception e){  Toast.makeText(MyApplication.getContext(), R.string.waitWifi, Toast.LENGTH_LONG).show();    e.printStackTrace();     }
        }else { Toast.makeText(MyApplication.getContext(),R.string.requireInternet, Toast.LENGTH_LONG).show(); }
        return internet;
    }

}

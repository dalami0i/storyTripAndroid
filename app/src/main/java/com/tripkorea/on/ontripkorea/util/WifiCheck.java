package com.tripkorea.on.ontripkorea.util;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Edward Won on 2017-04-19.
 */

public class WifiCheck {
    public static final int WIFI_OFF = 0;
    public static final int WIFI_ON = 1;

    public static final String CONNECTION_CONFIRM_CLIENT_URL = "http://clients3.google.com/generate_204";
    public static int wificheck = 0;

    public static class CheckConnect extends Thread {
        boolean success;
        String host;

        public CheckConnect(String host){
            this.host = host;
        }

        @Override
        public void run() {

            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection)new URL(host).openConnection();
                conn.setRequestProperty("User-Agent","Android");
                conn.setConnectTimeout(100);
                conn.connect();
                int responseCode = conn.getResponseCode();
                if(responseCode == 204){
                    success = true;
                    wificheck = WIFI_ON ;}
                else success = false;
            }
            catch (Exception e) {
                e.printStackTrace();
                success = false;
            }
            if(conn != null){
                conn.disconnect();
            }
        }

        /*public boolean isSuccess(){
            return success;
        }*/

    }
}

package com.tripkorea.on.ontripkorea.util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by YangHC on 2017-07-29.
 */

public class Preference {
    private static String SHARED_PREFERNECE_NAME = "beongae";
    public static String SIGN_IN_ID = "signinID";

    private static Preference instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Preference(){
        build();
    }

    public void build(){
        sharedPreferences = MyApplication.getContext().getSharedPreferences(SHARED_PREFERNECE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static Preference getInstance(){
        if(instance == null){
            instance = new Preference();
        }
        return instance;
    }

    public void putString(String key, String value){
        editor.putString(key,value);
        editor.apply();
    }

    public void putInt(String key, int value){
        editor.putInt(key,value);
        editor.apply();
    }

    public String getString(String key){
        return sharedPreferences.getString(key,null);
    }

    public int getInt(String key){
        return sharedPreferences.getInt(key,-1);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    /**
     * Created by YangHC on 2018-06-11.
     */

    public static class Coord {
        private double lat;
        private double lon;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }
    }
}

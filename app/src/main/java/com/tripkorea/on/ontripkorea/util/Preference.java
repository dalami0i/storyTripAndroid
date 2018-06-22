package com.tripkorea.on.ontripkorea.util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by YangHC on 2017-07-29.
 */

public class Preference {
    private static String PREFERNECE_NAME = "beongae";
    public static String SIGN_IN_ID = "signinID";

    private static Preference instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Preference(){
        build();
    }

    public void build(){
        sharedPreferences = MyApplication.getContext().getSharedPreferences(PREFERNECE_NAME, Context.MODE_PRIVATE);
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
}

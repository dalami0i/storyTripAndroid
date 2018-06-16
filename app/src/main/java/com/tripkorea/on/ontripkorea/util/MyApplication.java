package com.tripkorea.on.ontripkorea.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tsengvn.typekit.Typekit;

/**
 * Created by KUvH on 2017-02-06.
 */

public class MyApplication extends MultiDexApplication {
    public static int APP_VERSION;
    public static boolean WILL_BE_DESTROYED = false;
    private static Context context; //Edward added 2018.06.11

    public void onCreate() {
        super.onCreate();

        context = this; //Edward added 2018.06.11

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/NanumGothic.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/NanumGothic.ttf"));

        ApiClient.getInstance().create();
    }

    public static void build(Context context) {
        MultiDex.install(context.getApplicationContext());
        PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        APP_VERSION = pi.versionCode;
    }

    public static Context getContext(){
        return context;
    }  //Edward added 2018.06.11
}

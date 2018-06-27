package com.tripkorea.on.ontripkorea.util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Edward Won on 2018-06-11.
 */

public class OkHttpInitSingtonManager {
    private static OkHttpClient okHttpClient;
    private static final int OKHTTP_INIT_VALUE = 15;
    static{
        okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(OKHTTP_INIT_VALUE, TimeUnit.SECONDS)
                .addInterceptor(new RetryInterceptor())
                .readTimeout(OKHTTP_INIT_VALUE, TimeUnit.SECONDS)
                .build();
    }
    public static OkHttpClient getOkHttpClient(){
        if(okHttpClient != null){
            return okHttpClient;
        }else{
            okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(OKHTTP_INIT_VALUE, TimeUnit.SECONDS)
                    .readTimeout(OKHTTP_INIT_VALUE, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }
}

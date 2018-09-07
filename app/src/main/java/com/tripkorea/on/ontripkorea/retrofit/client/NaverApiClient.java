package com.tripkorea.on.ontripkorea.retrofit.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by YangHC on 2017-08-08.
 */

public class NaverApiClient {
    private NaverApiService apiService;
    private static NaverApiClient instance;
    private static final String PROD_IP = "https://openapi.naver.com/v1/nid/";

    public void create(){
        /**
         * Gson 컨버터 이용
         */
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getNaverApiServer())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(NaverApiService.class);
    }

    public static NaverApiClient getInstance() {
        if (instance == null) {
            synchronized (NaverApiClient.class) {
                if (instance == null) {
                    instance = new NaverApiClient();
                }
            }
        }
        return instance;
    }

    public static String getNaverApiServer() {
        return PROD_IP;
    }

    public NaverApiService getNaverApiService() {
        return apiService;
    }
}

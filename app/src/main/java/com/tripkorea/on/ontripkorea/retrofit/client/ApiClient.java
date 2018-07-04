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

public class ApiClient {
    private ApiService apiService;
    private static ApiClient instance;
    private static final String PROD_IP = "13.209.61.27";
    public static final int PORT_NUMBER= 8080;

    public void create(){
        /**
         * Gson 컨버터 이용
         */
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getApiServer(PROD_IP))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static ApiClient getInstance() {
        if (instance == null) {
            synchronized (ApiClient.class) {
                if (instance == null) {
                    instance = new ApiClient();
                }
            }
        }
        return instance;
    }

    public static String getApiServer(String hostIp) {
        return "http://" + hostIp + ":" + PORT_NUMBER+ "/";
    }

    public ApiService getApiService() {
        return apiService;
    }
}

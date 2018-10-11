package com.tripkorea.on.ontripkorea.retrofit.client;


import com.tripkorea.on.ontripkorea.vo.user.NaverResponse;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface NaverApiService {

    @POST("me")
    Call<NaverResponse> getPersonalInfo(@Header("Authorization") String AccessToken);


}
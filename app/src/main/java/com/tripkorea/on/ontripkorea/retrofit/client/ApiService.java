package com.tripkorea.on.ontripkorea.retrofit.client;


import com.tripkorea.on.ontripkorea.retrofit.message.ApiMessasge;
import com.tripkorea.on.ontripkorea.vo.attraction.Attraction;
import com.tripkorea.on.ontripkorea.vo.attraction.Restaurant;
import com.tripkorea.on.ontripkorea.vo.dto.LikeDTO;
import com.tripkorea.on.ontripkorea.vo.dto.VisitDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    // 좋아요
    @POST("/storytour/api/{version}/user/like")
    Call<ApiMessasge> like(@Path("version") int version, LikeDTO likeDTO);
    // 좋아요 해제
    @DELETE("/storytour/api/{version}/user/like")
    Call<ApiMessasge> cancelLike(@Path("version") int version, LikeDTO likeDTO);
    // 내 좋아요 리스트
    @GET("/storytour/api/{version}/user/like/{userIdx}")
    Call<Attraction> getMyLikeList(@Path("version") int version, @Path("userIdx") int userIdx);

    // 방문
    @POST("/storytour/api/{version}/user/like")
    Call<ApiMessasge> visit(@Path("version") int version, VisitDTO likeDTO);
    // 방문 해제
    @DELETE("/storytour/api/{version}/user/visit")
    Call<ApiMessasge> cancelVisit(@Path("version") int version, VisitDTO likeDTO);
    // 내 방문 리스트
    @GET("/storytour/api/{version}/user/visit/{userIdx}")
    Call<List<Attraction>> getMyVisitList(@Path("version") int version, @Path("userIdx") int userIdx);

    // 주변 교통정보 리스트
    @GET("/storytour/api/{version}/user/attr/route/list/{lat}/{lon}/{page}")
    Call<List<Attraction>> getAroundRoutes(@Path("version") int version, @Path("lat") double lat, @Path("lon") double lon, @Path("page") int page);
    // 주변 식당 리스트
    @GET("/storytour/api/{version}/user/attr/restaurant/list/{lat}/{lon}/{page}")
    Call<List<Restaurant>> getAroundRestaurants(@Path("version") int version, @Path("lat") double lat, @Path("lon") double lon, @Path("page") int page);
    // 주변 놀거리 리스트
    @GET("/storytour/api/{version}/user/attr/tour/{lat}/{lon}/{page}")
    Call<List<Attraction>> getAroundTours(@Path("version") int version, @Path("lat") double lat, @Path("lon") double lon, @Path("page") int page);

//    @POST("/storytour/api/{version}/user/signup")
//    Call<ApiMessasge> userSignUp(@Path("version") int version, @Body UserSignUpForm request);
//
//    @GET("/storytour/api/{version}/user/signup/{phoneNum}")
//    Call<ApiMessasge> checkDuplicatePhoneNum(@Path("version") int version, @Path("phoneNum") String phoneNum);
//
//    @POST("/storytour/api/{version}/user/signin")
//    Call<User> userSignIn(@Path("version") int version, @Body UserSignInForm request);
//
//    @GET("/storytour/api/{version}/user/{userIdx}")
//    Call<User> getUserInfo(@Path("version") int version, @Path("userIdx") int idx);
//
//    @GET("/storytour/api/{version}/user/order")
//    Call<ApiMessasge> getPrice(@Path("version") int version, @Body OrderForm order);
//
//    @POST("/storytour/api/{version}/user/order")
//    Call<ApiMessasge> order(@Path("version") int version, @Body OrderForm order);
//
//    @GET("/storytour/api/{version}/user/drivers/{lat}/{lon}/{distance}")
//    Call<List<Driver>> getDriverList(@Path("version") int version, @Path("lat") double lat
//            , @Path("lon") double lon, @Path("distance") double distance);

}

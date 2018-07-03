package com.tripkorea.on.ontripkorea.retrofit.client;


import com.tripkorea.on.ontripkorea.retrofit.message.ApiMessasge;
import com.tripkorea.on.ontripkorea.vo.attraction.Attraction;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionDetail;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.dto.LikeDTO;
import com.tripkorea.on.ontripkorea.vo.dto.VisitDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    // 좋아요
    @POST("/storytour/api/{version}/user/like")
    Call<ApiMessasge> like(@Path("version") int version, @Body LikeDTO likeDTO);
    // 좋아요 해제
    @HTTP(method = "POST", path = "/storytour/api/{version}/user/like/cancel", hasBody = true)
    Call<ApiMessasge> cancelLike(@Path("version") int version, @Body LikeDTO likeDTO);
    // 내 좋아요 리스트
    @GET("/storytour/api/{version}/user/like/list/{userIdx}")
    Call<List<AttractionSimple>> getMyLikeList(@Path("version") int version, @Path("userIdx") int userIdx);

     // 방문
    @POST("/storytour/api/{version}/user/visit")
    Call<ApiMessasge> visit(@Path("version") int version,@Body VisitDTO likeDTO);
    // 방문 해제
    @HTTP(method = "POST", path = "/storytour/api/{version}/user/visit/cancel", hasBody = true)
    Call<ApiMessasge> cancelVisit(@Path("version") int version,@Body VisitDTO likeDTO);
    // 내 방문 리스트
    @GET("/storytour/api/{version}/user/visit/list/{userIdx}")
    Call<List<AttractionSimple>> getMyVisitList(@Path("version") int version, @Path("userIdx") int userIdx);


    // 주변 교통정보 리스트
    //@GET("/storytour/api/{version}/user/attr/route/list/{lat}/{lon}/{page}")
    //Call<List<Attraction>> getAroundRoutes(@Path("version") int version, @Path("lat") double lat, @Path("lon") double lon, @Path("page") int page);
    @GET("/storytour/api/{version}/user/attr/route/list/{index}")
    Call<List<AttractionSimple>> getAroundRoutes(@Path("version") int version, @Path("index") int idx);
    // 주변 식당 리스트
    @GET("/storytour/api/{version}/user/attr/restaurant/list/{lat}/{lon}/{page}")
    Call<List<AttractionSimple>> getAroundRestaurants(@Path("version") int version, @Path("lat") double lat, @Path("lon") double lon, @Path("page") int page);
    // 주변 놀거리 리스트
    @GET("/storytour/api/{version}/user/attr/tour/list/{lat}/{lon}/{page}")
    Call<List<AttractionSimple>> getAroundTours(@Path("version") int version, @Path("lat") double lat, @Path("lon") double lon, @Path("page") int page);

    //어트랙션 상세 정보
    @GET("/storytour/api/{version}/user/attr/detail/{attractionIdx}/{userIdx}")
    Call<AttractionDetail> getAttractionDetail(@Path("version") int version, @Path("attractionIdx") int attractionIdx, @Path("userIdx") int userIdx);

}
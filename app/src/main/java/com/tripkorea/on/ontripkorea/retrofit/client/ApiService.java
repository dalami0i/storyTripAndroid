package com.tripkorea.on.ontripkorea.retrofit.client;


import com.tripkorea.on.ontripkorea.retrofit.message.ApiMessasge;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionDetail;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.dto.LikeDTO;
import com.tripkorea.on.ontripkorea.vo.dto.VisitDTO;
import com.tripkorea.on.ontripkorea.vo.toonguide.Toon;
import com.tripkorea.on.ontripkorea.vo.user.User;
import com.tripkorea.on.ontripkorea.vo.voiceguide.Guide;
import com.tripkorea.on.ontripkorea.vo.voiceguide.GuidePhoto;
import com.tripkorea.on.ontripkorea.vo.voiceguide.VoiceGuideLog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    //로그인
    @POST("/storytour/api/{version}/user/sign/up")
    Call<ApiMessasge> signup(@Path("version") int version, @Body User user);

    // 좋아요
    @POST("/storytour/api/{version}/user/like")
    Call<ApiMessasge> like(@Path("version") int version, @Body LikeDTO likeDTO);
    // 좋아요 해제
    @HTTP(method = "POST", path = "/storytour/api/{version}/user/like/cancel", hasBody = true)
    Call<ApiMessasge> cancelLike(@Path("version") int version, @Body LikeDTO likeDTO);
    // 내 좋아요 리스트
    @GET("/storytour/api/{version}/user/like/list/{userIdx}/{language}")
    Call<List<AttractionSimple>> getMyLikeList(@Path("version") int version, @Path("userIdx") int userIdx, @Path("language") int languag);

     // 방문
    @POST("/storytour/api/{version}/user/visit")
    Call<ApiMessasge> visit(@Path("version") int version,@Body VisitDTO likeDTO);
    // 방문 해제
    @HTTP(method = "POST", path = "/storytour/api/{version}/user/visit/cancel", hasBody = true)
    Call<ApiMessasge> cancelVisit(@Path("version") int version,@Body VisitDTO likeDTO);
    // 내 방문 리스트
    @GET("/storytour/api/{version}/user/visit/list/{userIdx}/{language}")
    Call<List<AttractionSimple>> getMyVisitList(@Path("version") int version, @Path("userIdx") int userIdx, @Path("language") int languag);


    // 주변 교통정보 리스트
    //@GET("/storytour/api/{version}/user/attr/route/list/{lat}/{lon}/{page}")
    //Call<List<Attraction>> getAroundRoutes(@Path("version") int version, @Path("lat") double lat, @Path("lon") double lon, @Path("page") int page);
    @GET("/storytour/api/{version}/user/attr/route/list/{index}")
    Call<List<AttractionSimple>> getAroundRoutes(@Path("version") int version, @Path("index") int idx);
    // 주변 식당 리스트
    @GET("/storytour/api/{version}/user/attr/restaurant/dozens/{lat}/{lon}/{language}/{page}")
    Call<List<AttractionSimple>> getAroundRestaurants(@Path("version") int version, @Path("lat") double lat, @Path("lon") double lon,@Path("language") int language, @Path("page") int page);
    // 주변 놀거리 리스트
    @GET("/storytour/api/{version}/user/attr/tour/dozens/{lat}/{lon}/{language}/{page}")
    Call<List<AttractionSimple>> getAroundTours(@Path("version") int version, @Path("lat") double lat, @Path("lon") double lon, @Path("language") int language, @Path("page") int page);
    // 주변 전체 리스트
    @GET("/storytour/api/{version}/user/attr/attraction/dozens/{lat}/{lon}/{language}/{page}")
    Call<List<AttractionSimple>> getAroundAttractions(@Path("version") int version, @Path("lat") double lat, @Path("lon") double lon, @Path("language") int language, @Path("page") int page);

    // guide list for photo
    @GET("/storytour/api/{version}/user/attr/new/guide/dozens/{lat}/{lon}/{size}/{page}")
    Call <ArrayList<GuidePhoto>> getGuidePhotos(@Path("version") int version, @Path("lat") double lat, @Path("lon") double lon, @Path("size") int size, @Path("page") int page);
    // total list for photo
    @GET("/storytour/api/{version}/user/attr/new/attraction/dozens/{lat}/{lon}/{size}/{page}")
    Call<List<AttractionSimple>> getTotalPhotos(@Path("version") int version, @Path("lat") double lat, @Path("lon") double lon,@Path("size") int size, @Path("page") int page);
    // restaurant list for photo
    @GET("/storytour/api/{version}/user/attr/new/restaurant/dozens/{lat}/{lon}/{size}/{page}")
    Call<List<AttractionSimple>> getRestaurantPhotos(@Path("version") int version, @Path("lat") double lat, @Path("lon") double lon,@Path("size") int size, @Path("page") int page);
    // attraction list for photo
    @GET("/storytour/api/{version}/user/attr/new/tour/dozens/{lat}/{lon}/{size}/{page}")
    Call<List<AttractionSimple>> getTourPhotos(@Path("version") int version, @Path("lat") double lat, @Path("lon") double lon, @Path("size") int size, @Path("page") int page);

    //놀거리 상세 정보
    @GET("/storytour/api/{version}/user/attr/tour/detail/{attractionIdx}/{language}/{userIdx}")
    Call<AttractionDetail> getTourDetail(@Path("version") int version, @Path("attractionIdx") int attractionIdx, @Path("language") int language, @Path("userIdx") int userIdx);
    //먹거리 상세 정보
    @GET("/storytour/api/{version}/user/attr/restaurant/detail/{attractionIdx}/{language}/{userIdx}")
    Call<AttractionDetail> getFoodDetail(@Path("version") int version, @Path("attractionIdx") int attractionIdx, @Path("language") int language, @Path("userIdx") int userIdx);


    //어트랙션 보이스 가이드
    @GET("/storytour/api/{version}/user/attr/guide/list/{attractionIdx}/{language}")
    Call<ArrayList<Guide>> getGuide(@Path("version") int version, @Path("attractionIdx") int attractionIdx, @Path("language") int language);

    //어트랙션 가이드툰
    @GET("/storytour/api/{version}/user/attr/cartoon/list/{attractionIdx}/{language}")
    Call<List<Toon>> getCartoon(@Path("version") int version, @Path("attractionIdx") int attractionIdx, @Path("language") int language);


    //보이스가이드 로그 전송
    @POST("/storytour/api/{version}/logging/voiceGuide")
    Call<ApiMessasge> voiceGuideLog (@Path("version") int version,@Body ArrayList<VoiceGuideLog> voiceGuideLogList);


}
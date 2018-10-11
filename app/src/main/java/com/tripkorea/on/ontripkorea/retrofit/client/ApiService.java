package com.tripkorea.on.ontripkorea.retrofit.client;


import com.tripkorea.on.ontripkorea.retrofit.message.ApiMessage;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionDetail;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.dto.LikeDTO;
import com.tripkorea.on.ontripkorea.vo.dto.VisitDTO;
import com.tripkorea.on.ontripkorea.vo.toonguide.Toon;
import com.tripkorea.on.ontripkorea.vo.user.LoginResponse;
import com.tripkorea.on.ontripkorea.vo.user.LoginUser;
import com.tripkorea.on.ontripkorea.vo.user.SignupResponsecode;
import com.tripkorea.on.ontripkorea.vo.user.UpdateUser;
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
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    //회원체크
    @POST("/storytour/api/{version}/user/sign/isExistedAccount")
    Call<SignupResponsecode> signupCheck(@Path("version") int version, @Body User user);
    //회원가입
    @POST("/storytour/api/{version}/user/sign/up")
    Call<SignupResponsecode> signup(@Path("version") int version, @Body User user);
    //로그인
    @POST("/storytour/api/{version}/user/sign/in")
    Call<LoginResponse> signin(@Path("version") int version, @Body LoginUser user);
    //회원정보수정
    @PUT("/storytour/api/{version}/user/update")
    Call<ApiMessage> updateUser(@Path("version") int version, @Body UpdateUser user);

    // 좋아요
    @POST("/storytour/api/{version}/user/like")
    Call<ApiMessage> like(@Path("version") int version, @Body LikeDTO likeDTO);
    // 좋아요 해제
    @HTTP(method = "DELETE", path = "/storytour/api/{version}/user/like/cancel", hasBody = true)
    Call<ApiMessage> cancelLike(@Path("version") int version, @Body LikeDTO likeDTO);
    // 내 좋아요 리스트
    @GET("/storytour/api/{version}/user/like/list/{language}")
    Call<ArrayList<AttractionSimple>> getMyLikeList(@Path("version") int version, @Path("language") int languag);//, @Path("userIdx") int userIdx

     // 방문
    @POST("/storytour/api/{version}/user/visit")
    Call<ApiMessage> visit(@Path("version") int version, @Body VisitDTO likeDTO);
    // 방문 해제
    @HTTP(method = "DELETE", path = "/storytour/api/{version}/user/visit/cancel", hasBody = true)
    Call<ApiMessage> cancelVisit(@Path("version") int version, @Body VisitDTO likeDTO);
    // 내 방문 리스트
    @GET("/storytour/api/{version}/user/visit/list/{language}")///{userIdx}
    Call<ArrayList<AttractionSimple>> getMyVisitList(@Path("version") int version, @Path("language") int languag);//, @Path("userIdx") int userIdx


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
    // 메인 추천 리스트
    @GET("/storytour/api/{version}/user/attr/rec/dozens/{language}")
    Call<List<AttractionSimple>> getRecommendations(@Path("version") int version,  @Path("language") int language);

    //놀거리 상세 정보
    @GET("/storytour/api/{version}/user/attr/tour/detail/{attractionIdx}/{language}")///{userIdx}
    Call<AttractionDetail> getTourDetail(@Path("version") int version, @Path("attractionIdx") int attractionIdx, @Path("language") int language);//, @Path("userIdx") int userIdx
    //먹거리 상세 정보
    @GET("/storytour/api/{version}/user/attr/restaurant/detail/{attractionIdx}/{language}")
    Call<AttractionDetail> getFoodDetail(@Path("version") int version, @Path("attractionIdx") int attractionIdx, @Path("language") int language);//, @Path("userIdx") int userIdx


    //어트랙션 보이스 가이드
    @GET("/storytour/api/{version}/user/attr/guide/list/{attractionIdx}/{language}")
    Call<ArrayList<Guide>> getGuide(@Path("version") int version, @Path("attractionIdx") int attractionIdx, @Path("language") int language);

    //어트랙션 가이드툰
    @GET("/storytour/api/{version}/user/attr/cartoon/list/{attractionIdx}/{language}")
    Call<List<Toon>> getCartoon(@Path("version") int version, @Path("attractionIdx") int attractionIdx, @Path("language") int language);

    //검색
    @GET("/storytour/api/{version}/user/attr/search/dozens/{language}/{page}")
    Call<List<AttractionSimple>> search(@Path("version") int version, @Path("language") int language, @Path("page") int page,
                                        @Query("lat") String lat,@Query("lon") String lon,@Query("query") String query,@Query("category") String categoty,@Query("tag") String tag);

    //보이스가이드 로그 전송
    @POST("/storytour/api/{version}/logging/voiceGuide")
    Call<ApiMessage> voiceGuideLog (@Path("version") int version, @Body ArrayList<VoiceGuideLog> voiceGuideLogList);


    //로그 보내기
    //상세페이지 나가기
    @GET("/storytour/api/{version}/log/attr/attaction/detail/close/{attractionIdx}/{language}")
    Call <ApiMessage> exitDetailPage(@Path("version") int version, @Path("attractionIdx") int attractionIdx, @Path("language") int language);
    //보이스 마커 터치
    @GET("/storytour/api/{version}/log/voice/marker/{guideIdx}/{language}")
    Call <ApiMessage> touchVoiceMarker(@Path("version") int version, @Path("guideIdx") int guideIdx, @Path("language") int language);
    //보이스 재생
    @GET("/storytour/api/{version}/log/voice/play/{guideIdx}/{language}")
    Call <ApiMessage> playVoice(@Path("version") int version, @Path("guideIdx") int guideIdx, @Path("language") int language);
    //보이스 정지
    @GET("/storytour/api/{version}/log/voice/stop/{guideIdx}/{language}")
    Call <ApiMessage> stopVoice(@Path("version") int version, @Path("guideIdx") int guideIdx, @Path("language") int language);
    //보이스 페이지 종료
    @GET("/storytour/api/{version}/log/voice/close/{guideIdx}/{language}")
    Call <ApiMessage> exitVoice(@Path("version") int version, @Path("guideIdx") int guideIdx, @Path("language") int language);
    //여행툰 마커 터치
    @GET("/storytour/api/{version}/log/toon/marker/{guideIdx}/{language}")
    Call <ApiMessage> touchToonMarker(@Path("version") int version, @Path("guideIdx") int guideIdx, @Path("language") int language);
    //여행툰 보기
    @GET("/storytour/api/{version}/log/toon/show/{guideIdx}/{language}")
    Call <ApiMessage> watchToon(@Path("version") int version, @Path("guideIdx") int guideIdx, @Path("language") int language);
    //여행툰 그만 보기
    @GET("/storytour/api/{version}/log/toon/stop/{guideIdx}/{language}")
    Call <ApiMessage> stopToon(@Path("version") int version, @Path("guideIdx") int guideIdx, @Path("language") int language);
    //여행툰 그만 보기
    @GET("/storytour/api/{version}/log/toon/stop/{guideIdx}/{language}")
    Call <ApiMessage> exitToon(@Path("version") int version, @Path("guideIdx") int guideIdx, @Path("language") int language);


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



}
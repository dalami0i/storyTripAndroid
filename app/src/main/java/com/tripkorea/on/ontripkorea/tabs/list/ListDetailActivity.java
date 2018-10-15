package com.tripkorea.on.ontripkorea.tabs.list;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.retrofit.message.ApiMessage;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.tabs.guide.GuideActivity;
import com.tripkorea.on.ontripkorea.tabs.info.InfoFragment;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.util.ReviewDialog;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionDetail;
import com.tripkorea.on.ontripkorea.vo.dto.LikeDTO;
import com.tripkorea.on.ontripkorea.vo.dto.VisitDTO;
import com.tripkorea.on.ontripkorea.vo.user.Me;
import com.tripkorea.on.ontripkorea.vo.youtube.YoutubeDetail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

///**
// * Created by Edward Won on 2018-06-12.
// */

public class ListDetailActivity extends AppCompatActivity implements
        OnMapReadyCallback, LocationListener, View.OnClickListener {

    private ImageView detailMain;//
    private TextView tvTitle;//여행지 타이틀
    private TextView tvSubwayLocation;//여행지 지하철역 위치
    private TextView tvBizHour;//여행지 영업시간
    private TextView tvDayOff;//여행지 휴무일
    private TextView tvRating;//여행지 별점
    private MaterialRatingBar detailRatingbar;
    private LinearLayout likeLayout;
    private ImageView likeImg;//여행지-유저 좋아요
    private LinearLayout footprintLayout;
    private TextView foodprintTv;
    private ImageView visitImg;//여행지-유저 발자국
    private TextView likeTv;
    private TextView contentTv;//여행지 설명
    private TextView feeTitle;
    private TextView fee_content; //여행지 이용금액
    private TextView operatinghoursTitle;
    private TextView operatinghours_content; //여행지 운영시간
    private TextView tag1;
    private TextView tag2;
    private TextView tag3;
    private TextView tag4;
    private TextView tag5;




    //locale
    String usinglanguage;
    Locale locale;

    //map
    @BindView(R.id.around_detail_map)
    MapView guideMap;
    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    Location currentLocation;
    double currentLat, currentLong;

//    net.daum.mf.map.api.MapView kakaoMap;

    //youtube
    @BindView(R.id.links_list_rv)
    RecyclerView youtubeLinkRV;

    //authority
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    AttractionDetail thisAttraction;//선택된 item의 공용 객체

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    private int attractionIdx;
    private int attrType;

    public static ArrayList<YoutubeDetail> youtubeDetails = new ArrayList<>();

    ProgressDialog detailProgress = null;

    int language;

    private boolean isLiked;
    private boolean isSoso;
    private boolean isHate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailProgress = ProgressDialog.show(ListDetailActivity.this, "", "Loading...", true);

        new LogManager().LogManager("ListDetailActivity","진입");
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if(savedInstanceState != null){
            Bundle bundle = new Bundle();
            attractionIdx = bundle.getInt("attractionIdx");
            attrType = bundle.getInt("attractionType");
        }else {
            attractionIdx = getIntent().getIntExtra("attractionIdx", 8344);
            attrType = getIntent().getIntExtra("attractionType", 100);
        }
        new LogManager().LogManager("ListDetailActivity: attrType",attrType+"");

        feeTitle = findViewById(R.id.fee_title);
        operatinghoursTitle = findViewById(R.id.operatinghours_title);

        if(attrType == 100 ) feeTitle.setText(getString(R.string.detail_entrance_fee));
        else feeTitle.setText(getString(R.string.detail_menu));


        //사용자 언어 확인
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        usinglanguage = locale.getDisplayLanguage();

        switch (usinglanguage){
            case "한국어":
                language = 1;
                break;
            case "中文":
                language = 2;
                break;
            case "日本言":
                language = 3;
                break;
            default:
                language = 0;
                break;

        }

        detailMain = findViewById(R.id.iv_detail_main);
        tvTitle = findViewById(R.id.tv_detail_title);
        tvSubwayLocation = findViewById(R.id.tv_detail_top_location);
        tvBizHour = findViewById(R.id.tv_detail_top_bizhour_detail);
        tvDayOff = findViewById(R.id.tv_detail_top_dayoff_detail);
        tvRating = findViewById(R.id.tv_detail_top_rating_detail);
        likeLayout = findViewById(R.id.item_like_layout);
        likeImg = findViewById(R.id.img_detail_like);
        footprintLayout = findViewById(R.id.item_footprint_layout);
        foodprintTv = findViewById(R.id.tv_item_footprint);
        visitImg = findViewById(R.id.img_detail_footprint);
        likeTv = findViewById(R.id.tv_item_like);
        contentTv = findViewById(R.id.tv_detail_con);
        fee_content = findViewById(R.id.fee_content);
        operatinghours_content = findViewById(R.id.operatinghours_content);
        tag1 = findViewById(R.id.tv_detail_tag1);
        tag2 = findViewById(R.id.tv_detail_tag2);
        tag3 = findViewById(R.id.tv_detail_tag3);
        tag4 = findViewById(R.id.tv_detail_tag4);
        tag5 = findViewById(R.id.tv_detail_tag5);
        detailRatingbar = findViewById(R.id.detail_ratingbar);



        guideMap.onCreate(savedInstanceState);
        guideMap.getMapAsync(this);
        guideMap.onStart();



        switch (attrType){
            case 100:
                getAttrData();
                break;
            case 200:
                getFoodData();
                break;
        }





    }


    private void getAttrData(){
        new LogManager().LogManager("ListDetailActivity getAttrData","진입");
        new LogManager().LogManager("ListDetailActivity getAttrData","MyApplication.APP_VERSION: "+MyApplication.APP_VERSION+" | attractionIdx: "+attractionIdx+" | language: "+language+" | Me.getInstance().getIdx(): "+Me.getInstance().getIdx());
        ApiClient.getInstance().getApiService()
                .getTourDetail(MyApplication.APP_VERSION, attractionIdx, language)
                .enqueue(new Callback<AttractionDetail>() {
                    @Override
                    public void onResponse(Call<AttractionDetail> call, Response<AttractionDetail> response) {
                        new LogManager().LogManager("ListDetailActivity getAttraData",response.body()+"");
                        if (response.body() != null) {
                            thisAttraction = response.body();
                            if (thisAttraction.getThumnailAddr() != null && thisAttraction.getThumnailAddr().length() > 5) {
                                ListDetailedImageFragmentPagerAdapter detailedImageFragmentPagerAdapter
                                        = new ListDetailedImageFragmentPagerAdapter(getSupportFragmentManager(), 3);
                                String thumnailAddr = thisAttraction.getThumnailAddr();
                                detailedImageFragmentPagerAdapter.addDetailedImage(thumnailAddr);


                                Glide.with(MyApplication.getContext()).load(thisAttraction.getThumnailAddr()).into(detailMain);
                                tvTitle.setText(thisAttraction.getName());
                                String subway = thisAttraction.getSubway();
                                tvSubwayLocation.setText(subway);
                                String tmpHoliday = thisAttraction.getHoliday();
                                if(tmpHoliday.length() > 15) tmpHoliday = "see details";
                                tvDayOff.setText(tmpHoliday);
                                contentTv.setText(Html.fromHtml(thisAttraction.getSummary()));
                                double tmpScore = thisAttraction.getScore()/20;
                                String tempScore = Double.toString(tmpScore);
                                tempScore = tempScore.substring(0,3);
                                tvRating.setText(tempScore);
                                detailRatingbar.setRating((float)tmpScore);
                                new LogManager().LogManager("별점비교","tmpScore: "+tmpScore+" | (float)tmpScore: "+(float)tmpScore+" | detailRatingbar.getRating(): "+detailRatingbar.getRating());


                                makeTextViewResizable(contentTv, 4, "View More>", true);
                                fee_content.setText(Html.fromHtml(thisAttraction.getDetail()));
                                new LogManager().LogManager("관광지 디테일","getDetail(): "+thisAttraction.getDetail());
                                operatinghours_content.setText(Html.fromHtml(thisAttraction.getUseTime()));
                                new LogManager().LogManager("관광지 시간","getUseTime(): "+thisAttraction.getUseTime());

                                String tmpTag = "";
                                if(thisAttraction.getTagSet() != null && thisAttraction.getTagSet().size() > 0){
                                    for(int i=0; i<thisAttraction.getTagSet().size(); i++) {
                                        switch (i){
                                            case 0:
                                                tmpTag = "#" + thisAttraction.getTagSet().get(i);
                                                tag1.setText(tmpTag);
                                                tag2.setVisibility(View.GONE);
                                                tag3.setVisibility(View.GONE);
                                                tag4.setVisibility(View.GONE);
                                                tag5.setVisibility(View.GONE);
                                                break;
                                            case 1:
                                                tmpTag = "#" + thisAttraction.getTagSet().get(i);
                                                tag2.setVisibility(View.VISIBLE);
                                                tag2.setText(tmpTag);
                                                tag3.setVisibility(View.GONE);
                                                tag4.setVisibility(View.GONE);
                                                tag5.setVisibility(View.GONE);
                                                break;
                                            case 2:
                                                tmpTag = "#" + thisAttraction.getTagSet().get(i);
                                                tag3.setVisibility(View.VISIBLE);
                                                tag3.setText(tmpTag);
                                                tag4.setVisibility(View.GONE);
                                                tag5.setVisibility(View.GONE);
                                                break;
                                            case 3:
                                                tmpTag = "#" + thisAttraction.getTagSet().get(i);
                                                tag4.setVisibility(View.VISIBLE);
                                                tag4.setText(tmpTag);
                                                tag5.setVisibility(View.GONE);
                                                break;
                                            case 4:
                                                tmpTag = "#" + thisAttraction.getTagSet().get(i);
                                                tag5.setVisibility(View.VISIBLE);
                                                tag5.setText(tmpTag);
                                                break;
                                        }
                                    }
                                }else{
                                    tag1.setText(thisAttraction.getName());
                                    tag2.setVisibility(View.GONE);
                                    tag3.setVisibility(View.GONE);
                                    tag4.setVisibility(View.GONE);
                                    tag5.setVisibility(View.GONE);
                                }


//                dayoffContentTv.setText(thisAttraction.dayOff);

                            }

                            new LogManager().LogManager(thisAttraction.getName(),thisAttraction.isLiked()+" | checkAttraction.isLiked() "+thisAttraction.getIdx());
                            if(thisAttraction.isLiked()){
                                likeImg.setImageResource(R.drawable.icon_heart_empty_image);
                                likeTv.setTextColor(getResources().getColor(R.color.white));
                                likeLayout.setBackground(getDrawable(R.drawable.round_background_main_tab_selected));
                            }else{
                                likeImg.setImageResource(R.drawable.z_heart_empty_s);
                                likeTv.setTextColor(getResources().getColor(R.color.pointedGrayColor));
                                likeLayout.setBackground(getDrawable(R.drawable.round_background_main_tabmenu));
                            }

                            new LogManager().LogManager(thisAttraction.getName(),thisAttraction.isVisited()+" | checkAttraction.isVisited() "+thisAttraction.getIdx());

                            if(thisAttraction.isVisited()){
                                visitImg.setImageResource(R.drawable.icon_visit_on);
                                foodprintTv.setTextColor(getResources().getColor(R.color.white));
                                footprintLayout.setBackground(getDrawable(R.drawable.round_background_main_tab_selected));
                            }else{
                                visitImg.setImageResource(R.drawable.icon_footprint_s);
                                foodprintTv.setTextColor(getResources().getColor(R.color.pointedGrayColor));
                                footprintLayout.setBackground(getDrawable(R.drawable.round_background_main_tabmenu));
                            }

                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(thisAttraction.getLat(), thisAttraction.getLon()))
                                    .title(thisAttraction.getName())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_s)))
                                    .setTag(thisAttraction.getIdx());
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(thisAttraction.getLat(), thisAttraction.getLon())));

//                            kakaoMap.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(thisAttraction.getLat(), thisAttraction.getLon()), 2,true);
//
//                            MapPOIItem marker = new MapPOIItem();
//                            marker.setItemName(thisAttraction.getName());
//                            marker.setTag(0);
//                            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(thisAttraction.getLat(), thisAttraction.getLon()));
//                            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
//                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
//
//                            kakaoMap.addPOIItem(marker);


                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    /*Intent intent = new Intent(ListDetailActivity.this, AroundDetailMapActivity.class);
                                    intent.putExtra("attractionMap", thisAttraction);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                    startActivity(intent);*/
                                }
                            });
                        } else {
                            Alert.makeText("상세 정보 받아오던 중 에러 발생");
                            try {
                                Log.e("API_FAIL", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        initViews(attractionIdx);
                        if(detailProgress.isShowing()) {
                            detailProgress.dismiss();
                            new LogManager().LogManager("디테일","디엔드");
//                            dialogShowing = false;
                        }

                    }

                    @Override
                    public void onFailure(Call<AttractionDetail> call, Throwable t) {
                        Alert.makeText(getResources().getString(R.string.network_error));
                    }
                });
//        Intent intent = getIntent();
//        if (intent != null) {
//            thisAttraction = (Attraction) intent.getSerializableExtra("attraction");
//            Log.e("디테일", "thisAttraction: " + thisAttraction.getIdx());
//        }
    }


    private void getFoodData(){
        new LogManager().LogManager("ListDetailActivity getFoodData","MyApplication.APP_VERSION: "+MyApplication.APP_VERSION+" | attractionIdx: "+attractionIdx+" | language: "+language+" | Me.getInstance().getIdx(): "+Me.getInstance().getIdx());
        ApiClient.getInstance().getApiService()
                .getFoodDetail(MyApplication.APP_VERSION, attractionIdx, language)
                .enqueue(new Callback<AttractionDetail>() {
                    @Override
                    public void onResponse(Call<AttractionDetail> call, Response<AttractionDetail> response) {
                        new LogManager().LogManager("ListDetailActivity getFoodData",response.body()+"");
                        if (response.body() != null) {
                            thisAttraction = response.body();
                            if (thisAttraction.getThumnailAddr() != null && thisAttraction.getThumnailAddr().length() > 5) {
                                ListDetailedImageFragmentPagerAdapter detailedImageFragmentPagerAdapter
                                        = new ListDetailedImageFragmentPagerAdapter(getSupportFragmentManager(), 3);
                                String thumnailAddr = thisAttraction.getThumnailAddr();
                                detailedImageFragmentPagerAdapter.addDetailedImage(thumnailAddr);
                                try {
                                    int size = Integer.parseInt(thumnailAddr.split("_")[1]);
                                    String path = thumnailAddr.split("_")[0];
                                    if (size < 11) {
                                        for (int i = 1; i < size; i++) {
                                            detailedImageFragmentPagerAdapter.addDetailedImage(path + "_" + size + "_" + i + ".png");
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    Log.e("DETAIL_IMAGE", "NumberFormatException!! : " + e.getMessage());
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    Log.e("DETAIL_IMAGE", "ArrayIndexOutOfBoundsException!! : " + e.getMessage());
                                }

                                Glide.with(MyApplication.getContext()).load(thisAttraction.getThumnailAddr()).into(detailMain);
                                tvTitle.setText(thisAttraction.getName());
                                String subway = thisAttraction.getSubway();
                                tvSubwayLocation.setText(subway);
                                String tmp = thisAttraction.getOperationTime().trim();
                                if(tmp.length() > 12){
                                    tmp = "will be update";
                                }
                                tvBizHour.setText(tmp);
                                tvDayOff.setText(thisAttraction.getHoliday());
                                double tmpScore = thisAttraction.getScore()/20;
                                String tempScore = Double.toString(tmpScore);
                                tempScore = tempScore.substring(0,3);
                                new LogManager().LogManager("별점비교","tmpScore: "+tmpScore+" | (float)tmpScore: "+(float)tmpScore);
                                tvRating.setText(tempScore);
                                float tmpFloatScore = Math.round(thisAttraction.getScore()*100)/100;
                                detailRatingbar.setRating(tmpFloatScore/20);
                                new LogManager().LogManager("별점비교","tmpScore: "+tmpScore+" | tmpFloatScore: "+tmpFloatScore+" | tmpFloatScore/20: "+tmpFloatScore/20+" | detailRatingbar.getRating(): "+detailRatingbar.getRating());

                                String tmpTag = "";
                                if(thisAttraction.getTagSet() != null && thisAttraction.getTagSet().size() > 0){
                                    for(int i=0; i<thisAttraction.getTagSet().size(); i++) {
                                        switch (i){
                                            case 0:
                                                tmpTag = "#" + thisAttraction.getTagSet().get(i);
                                                tag1.setText(tmpTag);
                                                tag2.setVisibility(View.GONE);
                                                tag3.setVisibility(View.GONE);
                                                tag4.setVisibility(View.GONE);
                                                tag5.setVisibility(View.GONE);
                                                break;
                                            case 1:
                                                tmpTag = "#" + thisAttraction.getTagSet().get(i);
                                                tag2.setVisibility(View.VISIBLE);
                                                tag2.setText(tmpTag);
                                                tag3.setVisibility(View.GONE);
                                                tag4.setVisibility(View.GONE);
                                                tag5.setVisibility(View.GONE);
                                                break;
                                            case 2:
                                                tmpTag = "#" + thisAttraction.getTagSet().get(i);
                                                tag3.setVisibility(View.VISIBLE);
                                                tag3.setText(tmpTag);
                                                tag4.setVisibility(View.GONE);
                                                tag5.setVisibility(View.GONE);
                                                break;
                                            case 3:
                                                tmpTag = "#" + thisAttraction.getTagSet().get(i);
                                                tag4.setVisibility(View.VISIBLE);
                                                tag4.setText(tmpTag);
                                                tag5.setVisibility(View.GONE);
                                                break;
                                            case 4:
                                                tmpTag = "#" + thisAttraction.getTagSet().get(i);
                                                tag5.setVisibility(View.VISIBLE);
                                                tag5.setText(tmpTag);
                                                break;
                                        }
                                    }
                                }else{
                                    tag1.setText(thisAttraction.getName());
                                    tag2.setVisibility(View.GONE);
                                    tag3.setVisibility(View.GONE);
                                    tag4.setVisibility(View.GONE);
                                    tag5.setVisibility(View.GONE);
                                }

                                contentTv.setText(Html.fromHtml(thisAttraction.getSummary()));
                                makeTextViewResizable(contentTv, 4, "View More>", true);

                                String menuString = thisAttraction.getMenu();
                                if(menuString.startsWith("/")){
                                    menuString = menuString.replaceFirst("/","");
                                    menuString = menuString.replaceAll("/",System.getProperty("line.separator"));
                                    menuString = menuString.replace("|","    ");
                                    fee_content.setText(menuString);
                                }else if(menuString.length() > 2){
                                    fee_content.setText(Html.fromHtml(menuString));
                                }else{
                                    fee_content.setText("Will be update");
                                }



                                new LogManager().LogManager("음식점 메뉴","getMainMenu(): "+thisAttraction.getMenu());
                                operatinghours_content.setText(Html.fromHtml(thisAttraction.getOperationTime()));
                                new LogManager().LogManager("음식점 시간","getOperationTime(): "+thisAttraction.getOperationTime());

//                        fee_content.setText(thisAttraction.fee);
//                dayoffContentTv.setText(thisAttraction.dayOff);
//                operatinghours_content.setText(thisAttraction.operatingHours);
                            }

                            new LogManager().LogManager(thisAttraction.getName(),thisAttraction.isLiked()+" | checkAttraction.isLiked() "+thisAttraction.getIdx());
                            if(thisAttraction.isLiked()){
                                likeImg.setImageResource(R.drawable.icon_heart_empty_image);
                                likeTv.setTextColor(getResources().getColor(R.color.white));
                                likeLayout.setBackground(getDrawable(R.drawable.round_background_main_tab_selected));
                            }else{
                                likeImg.setImageResource(R.drawable.icon_heart_image);
                                likeTv.setTextColor(getResources().getColor(R.color.pointedGrayColor));
                                likeLayout.setBackground(getDrawable(R.drawable.round_background_main_tabmenu));
                            }

                            new LogManager().LogManager(thisAttraction.getName(),thisAttraction.isVisited()+" | checkAttraction.isVisited() "+thisAttraction.getIdx());
                            if(thisAttraction.isVisited()){
                                visitImg.setImageResource(R.drawable.icon_visit_on);
                                foodprintTv.setTextColor(getResources().getColor(R.color.white));
                                footprintLayout.setBackground(getDrawable(R.drawable.round_background_main_tab_selected));
                            }else{
                                visitImg.setImageResource(R.drawable.icon_footprint_s);
                                foodprintTv.setTextColor(getResources().getColor(R.color.pointedGrayColor));
                                footprintLayout.setBackground(getDrawable(R.drawable.round_background_main_tabmenu));
                            }

                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(thisAttraction.getLat(), thisAttraction.getLon()))
                                    .title(thisAttraction.getName())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_s)))
                                    .setTag(thisAttraction.getIdx());
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(thisAttraction.getLat(), thisAttraction.getLon())));

//                            kakaoMap.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(thisAttraction.getLat(), thisAttraction.getLon()), 2,true);
//
//                            MapPOIItem marker = new MapPOIItem();
//                            marker.setItemName(thisAttraction.getName());
//                            marker.setTag(0);
//                            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(thisAttraction.getLat(), thisAttraction.getLon()));
//                            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
//                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
//
//                            kakaoMap.addPOIItem(marker);


                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    /*Intent intent = new Intent(ListDetailActivity.this, AroundDetailMapActivity.class);
                                    intent.putExtra("attractionMap", thisAttraction);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                    startActivity(intent);*/
                                }
                            });
                        } else {
                            Alert.makeText("상세 정보 받아오던 중 에러 발생");
                            try {
                                Log.e("API_FAIL", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        initViews(attractionIdx);
                        if(detailProgress.isShowing()) {
                            detailProgress.dismiss();
                            new LogManager().LogManager("디테일","디엔드");
//                            dialogShowing = false;
                        }

                    }

                    @Override
                    public void onFailure(Call<AttractionDetail> call, Throwable t) {
                        Alert.makeText(getResources().getString(R.string.network_error));
                    }
                });
//        Intent intent = getIntent();
//        if (intent != null) {
//            thisAttraction = (Attraction) intent.getSerializableExtra("attraction");
//            Log.e("디테일", "thisAttraction: " + thisAttraction.getIdx());
//        }
    }


    private void initViews(int attractionIdx) {
        likeImg.setOnClickListener(this);
        visitImg.setOnClickListener(this);



    }

    private void checkMyLocation() {
        checkLocationPermission();
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        currentLocation = locationManager.getLastKnownLocation(provider);
        if (currentLocation != null) {
            currentLong = currentLocation.getLongitude();
            currentLat = currentLocation.getLatitude();
        } else {
            Toast.makeText(this, R.string.failtoloadlocation, Toast.LENGTH_LONG).show();
            currentLong = 126.989511;
            currentLat = 37.577401;
        }
//        double dist = distance(latitude, longitude, currentLat, currentLong, "meter");

    }

    private void checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ListDetailActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {

                        return false;
                    }
                });
            } else {
                ActivityCompat.requestPermissions(ListDetailActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {

                    return false;
                }
            });
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        currentLocation = location;
        currentLong = location.getLongitude(); //경도
        currentLat = location.getLatitude(); //위도

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMapOptions options = new GoogleMapOptions().liteMode(true);

        mMap = googleMap;
//        mMap.moveCamera(CameraUpdateFactory.zoomTo(18));

//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(thisAttraction.getLat(), thisAttraction.getLon()))
//                .title(this.thisAttraction.getName())
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_s)))
//                .setTag(this.thisAttraction.getIdx());

        checkLocationPermission();
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                checkLocationPermission();
                checkMyLocation();
                LocationManager locationManager = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);
                if (currentLocation == null) {
                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                    String provider = locationManager.getBestProvider(criteria, true);
                    currentLocation = locationManager.getLastKnownLocation(provider);
                    final double latitude;
                    final double longitude;
                    if (currentLocation == null) {
                        currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }

                    if (currentLocation != null) {
                        latitude = currentLocation.getLatitude();
                        currentLat = latitude;
                        longitude = currentLocation.getLongitude();
                        currentLong = longitude;
                    }
                }



                return false;
            }
        });

        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.e("showdetailactivity", ListDetailActivity.this.thisAttraction.getName() + "맵클릭됨");
                /*Intent intent = new Intent(ListDetailActivity.this, AroundDetailMapActivity.class);
                intent.putExtra("attractionMap", ListDetailActivity.this.thisAttraction);
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(intent);*/
            }
        });




    }

    protected synchronized void buildGoogleApiClient() {
//        Log.e("빌드에이피아이","비이이이이이틀");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        guideMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        stopAudio();
//        guideMap.onPause();
        ApiClient.getInstance().getApiService()
                .exitDetailPage(MyApplication.APP_VERSION,attractionIdx,language)
                .enqueue(new Callback<ApiMessage>() {
                    @Override
                    public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                        if (response.body() != null) {
                            ApiMessage apiMessage = response.body();
                            new LogManager().LogManager("리스트디테일","apiMessage.getMessage(): "+apiMessage.getMessage());

                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("리스트디테일엑티비티", "exitDetailPage: error  " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiMessage> call, Throwable t) {
                        Alert.makeText(getString(R.string.network_error));
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbinder = ButterKnife.bind(this, view);
//        guideMap.onDestroy();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }



    @Override
    public void onClick(View v) {
        new LogManager().LogManager("ListDatail onClick",v.getId()+" clicked");
        ImageView ivLike = v.findViewById(R.id.iv_review_like);
        ImageView ivSoso = v.findViewById(R.id.iv_review_soso);
        ImageView ivHate = v.findViewById(R.id.iv_review_hate);
        TextView tvLike = v.findViewById(R.id.tv_review_like);
        TextView tvSoso = v.findViewById(R.id.tv_review_soso);
        TextView tvHate = v.findViewById(R.id.tv_review_hate);
        Button btnLeaveReview = v.findViewById(R.id.btn_leave_review);

        switch (v.getId()) {


            case R.id.item_like_layout:
                if(thisAttraction.isLiked()){
                    ApiClient.getInstance().getApiService()
                            .cancelLike(MyApplication.APP_VERSION, new LikeDTO(attractionIdx))
                            .enqueue(new Callback<ApiMessage>(){
                                @Override
                                public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                                    if (response.body() != null) {
                                        Alert.makeText("좋아요 취소!");
                                        new LogManager().LogManager("리스트디테일",thisAttraction.getName()+" cancelLike response.body().getMessage():"+response.body().getMessage());
                                        likeImg.setImageResource(R.drawable.z_heart_empty_s);
                                        likeTv.setTextColor(getResources().getColor(R.color.pointedGrayColor));
                                        likeLayout.setBackground(getDrawable(R.drawable.round_background_main_tabmenu));
                                        thisAttraction.setLiked(false);
                                        updateList("total", thisAttraction.getIdx(), false);
                                        updateList("tour", thisAttraction.getIdx(), false);
                                        updateList("food", thisAttraction.getIdx(), false);
                                    } else {
                                        Alert.makeText("좋아요 취소 에러 발생" + response.errorBody().toString());
                                        try {
                                            Log.e("LIKE", "error : " + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiMessage> call, Throwable t) {
                                    Alert.makeText(getResources().getString(R.string.network_error));
                                }
                            });
                }else{
                    ApiClient.getInstance().getApiService()
                            .like(MyApplication.APP_VERSION, new LikeDTO(attractionIdx))
                            .enqueue(new Callback<ApiMessage>(){
                                @Override
                                public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                                    if (response.body() != null) {
                                        new LogManager().LogManager("리스트디테일",thisAttraction.getName()+" like response.body().getMessage():"+response.body().getMessage());
                                        Alert.makeText("좋아요!");
                                        likeImg.setImageResource(R.drawable.icon_heart_empty_image);
                                        likeTv.setTextColor(getResources().getColor(R.color.white));
                                        likeLayout.setBackground(getDrawable(R.drawable.round_background_main_tab_selected));
                                        thisAttraction.setLiked(true);
                                        updateList("total", thisAttraction.getIdx(), true);
                                        updateList("tour", thisAttraction.getIdx(), true);
                                        updateList("food", thisAttraction.getIdx(), true);

                                    } else {
                                        Alert.makeText("좋아요 에러 발생" + response.errorBody().toString());
                                        try {
                                            Log.e("LIKE", "error : " + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiMessage> call, Throwable t) {
                                    Alert.makeText(getResources().getString(R.string.network_error));
                                }
                            });
                }
                //likeList에 변동이 생김을 알림 -> InfoFragment에선 이 변수를 기준으로 likeList를 업데이트 할지 결정함
                InfoFragment.LIKE_LIST_CHANGED = true;

                break;
            case R.id.item_footprint_layout:
                if(thisAttraction.isVisited()){
                    ApiClient.getInstance().getApiService()
                            .cancelVisit(MyApplication.APP_VERSION, new VisitDTO(attractionIdx))
                            .enqueue(new Callback<ApiMessage>(){
                                @Override
                                public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                                    if (response.body() != null) {
                                        Alert.makeText("방문한 것 취소!");
                                        visitImg.setImageResource(R.drawable.icon_footprint_s);
                                        foodprintTv.setText(R.string.detail_visit);
                                        foodprintTv.setTextColor(getResources().getColor(R.color.pointedGrayColor));
                                        footprintLayout.setBackground(getDrawable(R.drawable.round_background_main_tabmenu));
                                        thisAttraction.setVisited(false);

                                    } else {
                                        Alert.makeText("방문 취소 에러 발생" + response.errorBody().toString());
                                        try {
                                            Log.e("VISIT", "error : " + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiMessage> call, Throwable t) {
                                    Alert.makeText(getResources().getString(R.string.network_error));
                                }
                            });
                }else{
                    showupReviewDialog();

                    ApiClient.getInstance().getApiService()
                            .visit(MyApplication.APP_VERSION, new VisitDTO(attractionIdx))
                            .enqueue(new Callback<ApiMessage>(){
                                @Override
                                public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                                    if (response.body() != null) {
                                        Alert.makeText("방문!");

                                        visitImg.setImageResource(R.drawable.icon_visit_on);
                                        foodprintTv.setTextColor(getResources().getColor(R.color.white));
                                        footprintLayout.setBackground(getDrawable(R.drawable.round_background_main_tab_selected));
                                        thisAttraction.setVisited(true);

                                    } else {
                                        Alert.makeText("방문 에러 발생" + response.errorBody().toString());
                                        try {
                                            Log.e("VISIT", "error : " + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiMessage> call, Throwable t) {
                                    Alert.makeText(getResources().getString(R.string.network_error));
                                }
                            });
                }
                //visitList에 변동이 생김을 알림 -> InfoFragment에선 이 변수를 기준으로 visitList를 업데이트 할지 결정함
                InfoFragment.VISITED_LIST_CHANGED = true;


                break;
            case R.id.item_guidebtn_layout:
                Intent guideIntent = new Intent(ListDetailActivity.this, GuideActivity.class);
                guideIntent.putExtra("guideIdx",attractionIdx);
                startActivity(guideIntent);
                break;
            case R.id.rating_best_layout:
                Alert.makeText(getResources().getString(R.string.image_select_toast));
                break;

            case R.id.rating_mid_layout:
                Alert.makeText(getResources().getString(R.string.image_select_toast));
                break;

            case R.id.rating_low_layout:
                Alert.makeText(getResources().getString(R.string.image_select_toast));
                break;


            case R.id.btn_leave_review:
                new LogManager().LogManager("ListDetailActivity","R.id.btn_leave_review");
                if(isLiked || isSoso || isHate) {
                    reviewDialog.cancel();
                    if(isLiked){
                        visitImg.setImageResource(R.drawable.icon_good_white);
                        foodprintTv.setText("Like it!");
                    }else if(isSoso){
                        visitImg.setImageResource(R.drawable.icon_fine_white);
                        foodprintTv.setText("Not bad.");
                    }else{
                        visitImg.setImageResource(R.drawable.icon_bad_white);
                        foodprintTv.setText("Terrible....");
                    }
                }else{
                    Alert.makeText("Please choose one emoticon as a review.");
                }
                break;
            case R.id.btn_review_like:
                new LogManager().LogManager("Review Dialog",v.getId()+" clicked 클릭드");
                if(isLiked && !isSoso && !isHate){
                    isLiked = false;
                    ivLike.setImageResource(R.drawable.icon_good_off);
                    tvLike.setTextColor(getResources().getColor( R.color.gnbSelectedColor) );
                }else if( isSoso || isHate ){
                    Alert.makeText("Please clear with other button and check it again.");
                }else{
                    isLiked = true;
                    Alert.makeText("You've clicked Like.");
                    ivLike.setImageResource(R.drawable.icon_good_on);
                    tvLike.setTextColor(getResources().getColor( R.color.pointTextColor) );
                }

                break;
            case R.id.btn_review_soso:
                new LogManager().LogManager("Review Dialog",v.getId()+" clicked 클릭드");
                if(!isLiked && isSoso && !isHate){
                    isSoso = false;
                    ivSoso.setImageResource(R.drawable.icon_fine_off);
                    tvSoso.setTextColor(getResources().getColor( R.color.gnbSelectedColor) );
                }else if( isLiked || isHate ){
                    Alert.makeText("Please clear with other button and check it again.");
                }else{
                    isSoso = true;
                    Alert.makeText("You've clicked Soso.");
                    ivSoso.setImageResource(R.drawable.icon_fine_on);
                    tvSoso.setTextColor(getResources().getColor( R.color.pointTextColor) );
                }
                break;
            case R.id.btn_review_hate:
                new LogManager().LogManager("Review Dialog",v.getId()+" clicked 클릭드");
                if(!isLiked && !isSoso && isHate){
                    isHate = false;
                    ivHate.setImageResource(R.drawable.icon_bad_off);
                    tvHate.setTextColor(getResources().getColor( R.color.gnbSelectedColor) );
                }else if( isLiked || isSoso ){
                    Alert.makeText("Please clear with other button and check it again.");
                }else{
                    isHate = true;
                    Alert.makeText("You've clicked Hate.");
                    ivHate.setImageResource(R.drawable.icon_bad_on);
                    tvHate.setTextColor(getResources().getColor( R.color.pointTextColor) );
                }
                break;
        }



    }

    ReviewDialog reviewDialog;

    private void showupReviewDialog(){
        reviewDialog = new ReviewDialog(this);
        reviewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        reviewDialog.setCancelable(false);
        reviewDialog.create();
        reviewDialog.show();


    }

    private void updateList(String whichList, int idx, boolean likeCheck){
        switch(whichList){
            case "total":
                for(int i=0;i< MainActivity.totalList.getItems().size();i++){
                    if(idx == MainActivity.totalList.getItems().get(i).getIdx()){
                        MainActivity.totalList.getItems().get(i).setLiked(likeCheck);
                        ListTotalFragment.listRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case "tour":
                for(int i=0;i< MainActivity.attractionList.getItems().size();i++){
                    if(idx == MainActivity.attractionList.getItems().get(i).getIdx()){
                        MainActivity.attractionList.getItems().get(i).setLiked(likeCheck);
                        ListTourFragment.listRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case "food":
                for(int i=0;i< MainActivity.foodList.getItems().size();i++){
                    if(idx == MainActivity.foodList.getItems().get(i).getIdx()){
                        MainActivity.foodList.getItems().get(i).setLiked(likeCheck);
                        ListFoodFragment.listRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }

    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                new LogManager().LogManager("상세정보: tv.getLineCount() :  ",tv.getLineCount()+" | "+maxLine);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() > maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + "..."+" " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(
                        addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);
        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        makeTextViewResizable(tv, 3, "View More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle = new Bundle();
        bundle.putInt("attractionIdx",attractionIdx);
        bundle.putInt("attractionType", attrType);
        outState.putBundle("savedItem",bundle);

    }

}


//        if (thisAttraction.youtubekey != null) {
//            LinearLayoutManager linkLayoutManager
//                    = new LinearLayoutManager(AroundDetailActivity.this, LinearLayoutManager.VERTICAL, false);
//            youtubeLinkRV.setLayoutManager(linkLayoutManager);
//            YoutubeItemRVAdapter linkItemRecyclerViewAdapter = new YoutubeItemRVAdapter(this);
//            DividerItemDecoration dividerItemDecorationLinkTransportation
//                    = new DividerItemDecoration(youtubeLinkRV.getContext(), linkLayoutManager.getOrientation());
//            youtubeLinkRV.addItemDecoration(dividerItemDecorationLinkTransportation);
//            youtubeLinkRV.setAdapter(linkItemRecyclerViewAdapter);
//            youtubeLinkRV.setNestedScrollingEnabled(false);
//        }


//Glide.with(AroundDetailActivity.this).asDrawable().into(likeImg) == getResources().getDrawable(z_heart_empty_s)
//TODO: 토글버튼으로 대체 -> 이미지 일일이 변경할 필요 없음
//TODO: 현재 이미지가 무엇인지를 조건으로 주고있는데 이것보단 liked, visited같은 boolean 변수들을 두고 그것들을 조건으로 주는 것이 좋음
                /*if (likeImg.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.z_heart_empty_s).getConstantState()) {
                    likeImg.setImageResource(R.drawable.icon_heart_empty_image);
//                    Glide.with(AroundDetailActivity.this).load(R.drawable.icon_heart_empty_image).into(likeImg);
                    Log.e("LIKE", "userIdx : " + Me.getInstance().getIdx() + ", " + attractionIdx);

                    ApiClient.getInstance().getApiService()
                            .like(MyApplication.APP_VERSION, new LikeDTO(Me.getInstance().getIdx(), attractionIdx))
                            .enqueue(new Callback<ApiMessage>() {
                                @Override
                                public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                                    //response != null
                                    //response.body() == null
                                    if (response.body() != null) {
                                        Alert.makeText("좋아요!");
                                    } else {
                                        Alert.makeText("좋아요 에러 발생" + response.errorBody().toString());
                                        try {
                                            Log.e("LIKE", "error : " + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiMessage> call, Throwable t) {
                                    Alert.makeText(getResources().getString(R.string.network_error));
                                }
                            });
                } else {
                    likeImg.setImageResource(R.drawable.z_heart_empty_s);
//                    Glide.with(AroundDetailActivity.this).load(R.drawable.z_heart_empty_s).into(likeImg);
                    Log.e("LIKE", "userIdx : " + Me.getInstance().getIdx() + ", " + attractionIdx);
                    ApiClient.getInstance().getApiService()
                            .cancelLike(MyApplication.APP_VERSION
                                    , new LikeDTO(Me.getInstance().getIdx(), attractionIdx))
                            .enqueue(new Callback<ApiMessage>() {
                                @Override
                                public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                                    if (response.body() != null) {
                                        Alert.makeText("좋아요 취소!");
                                    } else {
                                        Alert.makeText("좋아요 취소 에러 발생" + response.errorBody().toString());
                                        try {
                                            Log.e("LIKE", "error : " + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiMessage> call, Throwable t) {
                                    Alert.makeText(getResources().getString(R.string.network_error));
                                }
                            });
                }*/

//                if (visitImg.getDrawable().getConstantState()
//                        == getResources().getDrawable(R.drawable.icon_footprint_s).getConstantState()) {
//                    visitImg.setImageResource(R.drawable.z_footprint_s);
//                    ApiClient.getInstance().getApiService()
//                            .visit(MyApplication.APP_VERSION, new VisitDTO(Me.getInstance().getIdx()
//                                    , thisAttraction.getIdx()))
//                            .enqueue(new Callback<ApiMessage>() {
//                                @Override
//                                public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
//                                    if (response.body() != null) {
//                                        Alert.makeText("방문!");
//                                    } else {
//                                        Alert.makeText("방문 에러 발생");
//                                        try {
//                                            Log.e("VISIT", "error : " + response.errorBody().string());
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<ApiMessage> call, Throwable t) {
//                                    Alert.makeText(getResources().getString(R.string.network_error));
//                                }
//                            });
//                } else {
//                    visitImg.setImageResource(R.drawable.icon_footprint_s);
//                    ApiClient.getInstance().getApiService()
//                            .cancelVisit(MyApplication.APP_VERSION
//                                    , new VisitDTO(Me.getInstance().getIdx(), attractionIdx))
//                            .enqueue(new Callback<ApiMessage>() {
//                                @Override
//                                public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
//                                    if (response.body() != null) {
//                                        Alert.makeText("방문 취소!");
//                                    } else {
//                                        Alert.makeText("방문 취소 에러 발생" + response.errorBody().toString());
//                                        try {
//                                            Log.e("LIKE", "error : " + response.errorBody().string());
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<ApiMessage> call, Throwable t) {
//                                    Alert.makeText(getResources().getString(R.string.network_error));
//                                }
//                            });
//                }


/*private void mapImageLoad(double longitude, double latitude) {
        Log.e("현재 위치: mapImageLoad", longitude + " | " + latitude);
        //지도 이미지를 기본 지도위에 덮어 씌우기 위한 객체
        GroundOverlayOptions cdgMap = new GroundOverlayOptions();

        //창덕궁 동-126.9942 서-126.9890825 남-37.5773492 북-37.5806767
        if (longitude <= 126.9942 && longitude >= 126.9890825
                && latitude <= 37.5806767 && latitude >= 37.5773492) {
            LatLngBounds cdgBounds = new LatLngBounds(
                    new LatLng(37.5776092, 126.9891875), //south west corner  37.5776092 | 126.9891875
                    new LatLng(37.5806567, 126.9941050)); //north east corner 37.5806567 | 126.9941050

//            LatLngBounds cdgLargeBounds = new LatLngBounds(
//                    new LatLng(37.574759, 126.987255 ), //south west corner
//                    new LatLng(37.582699, 126.996911)); //north east corner

//            switch (usinglanguage) {
//                case "한국어":
//                    cdgMap.image(BitmapDescriptorFactory.fromResource(R.drawable.z_cdg_map_old));
//                    mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
//                    break;
//                default:
//                    cdgMap.image(BitmapDescriptorFactory.fromResource(R.drawable.z_cdg_map_e_old));
//                    mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
//                    break;
//            }
//            cdgMap.positionFromBounds(cdgBounds)
//                    .transparency(0.0f);
//            mMap.addGroundOverlay(cdgMap);
//            mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
//            mMap.moveCamera(CameraUpdateFactory.zoomTo(20));

        } else {
//            mMap.clear();
//            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }*/
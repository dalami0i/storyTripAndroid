package com.tripkorea.on.ontripkorea.tabs.list;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.retrofit.message.ApiMessasge;
import com.tripkorea.on.ontripkorea.tabs.around.detail.AroundDetailMapActivity;
import com.tripkorea.on.ontripkorea.tabs.guide.GuideActivity;
import com.tripkorea.on.ontripkorea.tabs.info.InfoFragment;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

///**
// * Created by Edward Won on 2018-06-12.
// */

public class ListDetailActivity extends AppCompatActivity implements
        OnMapReadyCallback, LocationListener, View.OnClickListener {

    @BindView(R.id.iv_detail_main)
    ImageView detailMain;//

    @BindView(R.id.tv_detail_title)
    TextView tvTitle;//여행지 타이틀
    @BindView(R.id.tv_detail_top_location)
    TextView tvSubwayLocation;//여행지 지하철역 위치
    @BindView(R.id.tv_detail_top_bizhour_detail)
    TextView tvBizHour;//여행지 영업시간
    @BindView(R.id.tv_detail_top_dayoff_detail)
    TextView tvDayOff;//여행지 휴무일
    @BindView(R.id.tv_detail_top_rating_detail)
    TextView tvRating;//여행지 별점

    @BindView(R.id.item_footprint_layout)
    LinearLayout footprintLayout;
    @BindView(R.id.img_detail_like)
    ImageView likeImg;//여행지-유저 좋아요
    @BindView(R.id.tv_item_footprint)
    TextView foodprintTv;
    @BindView(R.id.item_like_layout)
    LinearLayout likeLayout;
    @BindView(R.id.img_detail_footprint)
    ImageView visitImg;//여행지-유저 발자국
    @BindView(R.id.tv_item_like)
    TextView likeTv;

    @BindView(R.id.tv_detail_con)
    TextView contentTv;//여행지 설명
    @BindView(R.id.fee_content)
    TextView fee_content; //여행지 이용금액
    @BindView(R.id.operatinghours_content)
    TextView operatinghours_content; //여행지 운영시간



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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        detailProgress = ProgressDialog.show(ListDetailActivity.this, "", "Loading...", true);

        attractionIdx = getIntent().getIntExtra("attractionIdx", 3466);
        attrType = getIntent().getIntExtra("attractionType",100);
        new LogManager().LogManager("ListDetailActivity: attrType",attrType+"");




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

        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();

        guideMap.onCreate(savedInstanceState);
        guideMap.getMapAsync(this);
        guideMap.onStart();

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


    }


    private void getAttrData(){
        new LogManager().LogManager("ListDetailActivity getAttraData","진입");
        new LogManager().LogManager("ListDetailActivity MyApplication.APP_VERSION",MyApplication.APP_VERSION+"");
        new LogManager().LogManager("ListDetailActivity attractionIdx",attractionIdx+"");
        new LogManager().LogManager("ListDetailActivity language",language+"");
        new LogManager().LogManager("ListDetailActivity Me.getInstance().getIdx()",Me.getInstance().getIdx()+"");
        ApiClient.getInstance().getApiService()
                .getTourDetail(MyApplication.APP_VERSION, attractionIdx, language, Me.getInstance().getIdx())
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
                                tvSubwayLocation.setText(thisAttraction.getSubway()+" 5번 출구");
                                String dayOff = thisAttraction.getDayoff();
                                tvDayOff.setText(dayOff+"");
                                contentTv.setText(thisAttraction.getSummary());
                                makeTextViewResizable(contentTv, 4, "View More>", true);

//                        fee_content.setText(thisAttraction.fee);
//                dayoffContentTv.setText(thisAttraction.dayOff);
//                operatinghours_content.setText(thisAttraction.operatingHours);
                            }

                            new LogManager().LogManager(thisAttraction.getName(),thisAttraction.isLiked()+" | checkAttraction.isLiked() "+thisAttraction.getIdx());
                            if(thisAttraction.isLiked()){
                                likeImg.setImageResource(R.drawable.z_heart_image_s);
                            }else{
                                likeImg.setImageResource(R.drawable.z_heart_empty_s);
                            }

                            new LogManager().LogManager(thisAttraction.getName(),thisAttraction.isVisited()+" | checkAttraction.isVisited() "+thisAttraction.getIdx());
                            if(thisAttraction.isVisited()){
                                visitImg.setImageResource(R.drawable.z_footprint_s);
                            }else{
                                visitImg.setImageResource(R.drawable.z_footprint_empty_s);
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
                                    Intent intent = new Intent(ListDetailActivity.this, AroundDetailMapActivity.class);
                                    intent.putExtra("attractionMap", thisAttraction);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                    startActivity(intent);
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
        ApiClient.getInstance().getApiService()
                .getFoodDetail(MyApplication.APP_VERSION, attractionIdx, language, Me.getInstance().getIdx())
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
                                    //최대 10장이라고 가정  (만약 '1228_3_0.png'와 같은 형식이 아닐 때
                                    //                      이상한 숫자(ex. 135172486)가 와서 for문을
                                    //                      너무 오래 도는 것을 방지)
                                    //근데 그렇게되면 밑에 catch가 잡아줄 것 같긴한데 일단 넣어놓자. 후에 필요없으면 빼는걸로.
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
                                tvSubwayLocation.setText(thisAttraction.getSubway()+" 5번 출구");
                                String dayOff = thisAttraction.getDayoff();
                                tvDayOff.setText(dayOff+"");
                                contentTv.setText(thisAttraction.getSummary());
                                makeTextViewResizable(contentTv, 4, "View More>", true);

//                        fee_content.setText(thisAttraction.fee);
//                dayoffContentTv.setText(thisAttraction.dayOff);
//                operatinghours_content.setText(thisAttraction.operatingHours);
                            }

                            new LogManager().LogManager(thisAttraction.getName(),thisAttraction.isLiked()+" | checkAttraction.isLiked() "+thisAttraction.getIdx());
                            if(thisAttraction.isLiked()){
                                likeImg.setImageResource(R.drawable.z_heart_image_s);
                            }else{
                                likeImg.setImageResource(R.drawable.z_heart_empty_s);
                            }

                            new LogManager().LogManager(thisAttraction.getName(),thisAttraction.isVisited()+" | checkAttraction.isVisited() "+thisAttraction.getIdx());
                            if(thisAttraction.isVisited()){
                                visitImg.setImageResource(R.drawable.z_footprint_s);
                            }else{
                                visitImg.setImageResource(R.drawable.z_footprint_empty_s);
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
                                    Intent intent = new Intent(ListDetailActivity.this, AroundDetailMapActivity.class);
                                    intent.putExtra("attractionMap", thisAttraction);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                    startActivity(intent);
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

        if(attractionIdx == 3466){
            LinearLayout guidebtnLayout = findViewById(R.id.item_guidebtn_layout);
            guidebtnLayout.setVisibility(View.VISIBLE);
            guidebtnLayout.setOnClickListener(this);
        }

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
            currentLong = 37.577401;
            currentLat = 126.989511;
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
//                Location mylocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(thisAttraction.getLat(), thisAttraction.getLon())));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.e("showdetailactivity", ListDetailActivity.this.thisAttraction.getName() + "맵클릭됨");
                Intent intent = new Intent(ListDetailActivity.this, AroundDetailMapActivity.class);
                intent.putExtra("attractionMap", ListDetailActivity.this.thisAttraction);
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(intent);
            }
        });

        switch (attrType){
            case 100:
                getAttrData();
                break;
            case 200:
                getFoodData();
                break;
        }


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

    private void mapImageLoad(double longitude, double latitude) {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_detail_like:
                if(thisAttraction.isLiked()){
                    ApiClient.getInstance().getApiService()
                            .cancelLike(MyApplication.APP_VERSION, new LikeDTO(Me.getInstance().getIdx(), attractionIdx))
                            .enqueue(new Callback<ApiMessasge>(){
                                @Override
                                public void onResponse(Call<ApiMessasge> call, Response<ApiMessasge> response) {
                                    if (response.body() != null) {
                                        Alert.makeText("좋아요 취소!");
                                        likeImg.setImageResource(R.drawable.z_heart_empty_s);
                                        thisAttraction.setLiked(false);
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
                                public void onFailure(Call<ApiMessasge> call, Throwable t) {
                                    Alert.makeText(getResources().getString(R.string.network_error));
                                }
                            });
                }else{
                    ApiClient.getInstance().getApiService()
                            .like(MyApplication.APP_VERSION, new LikeDTO(Me.getInstance().getIdx(), attractionIdx))
                            .enqueue(new Callback<ApiMessasge>(){
                                @Override
                                public void onResponse(Call<ApiMessasge> call, Response<ApiMessasge> response) {
                                    if (response.body() != null) {
                                        Alert.makeText("좋아요!");
                                        likeImg.setImageResource(R.drawable.z_heart_image_s);
                                        thisAttraction.setLiked(true);
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
                                public void onFailure(Call<ApiMessasge> call, Throwable t) {
                                    Alert.makeText(getResources().getString(R.string.network_error));
                                }
                            });
                }
                //likeList에 변동이 생김을 알림 -> InfoFragment에선 이 변수를 기준으로 likeList를 업데이트 할지 결정함
                InfoFragment.LIKE_LIST_CHANGED = true;
                //Glide.with(AroundDetailActivity.this).asDrawable().into(likeImg) == getResources().getDrawable(z_heart_empty_s)
                //TODO: 토글버튼으로 대체 -> 이미지 일일이 변경할 필요 없음
                //TODO: 현재 이미지가 무엇인지를 조건으로 주고있는데 이것보단 liked, visited같은 boolean 변수들을 두고 그것들을 조건으로 주는 것이 좋음
                /*if (likeImg.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.z_heart_empty_s).getConstantState()) {
                    likeImg.setImageResource(R.drawable.z_heart_image_s);
//                    Glide.with(AroundDetailActivity.this).load(R.drawable.z_heart_image_s).into(likeImg);
                    Log.e("LIKE", "userIdx : " + Me.getInstance().getIdx() + ", " + attractionIdx);

                    ApiClient.getInstance().getApiService()
                            .like(MyApplication.APP_VERSION, new LikeDTO(Me.getInstance().getIdx(), attractionIdx))
                            .enqueue(new Callback<ApiMessasge>() {
                                @Override
                                public void onResponse(Call<ApiMessasge> call, Response<ApiMessasge> response) {
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
                                public void onFailure(Call<ApiMessasge> call, Throwable t) {
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
                            .enqueue(new Callback<ApiMessasge>() {
                                @Override
                                public void onResponse(Call<ApiMessasge> call, Response<ApiMessasge> response) {
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
                                public void onFailure(Call<ApiMessasge> call, Throwable t) {
                                    Alert.makeText(getResources().getString(R.string.network_error));
                                }
                            });
                }*/
                break;
            case R.id.img_detail_footprint:
                if(thisAttraction.isVisited()){
                    ApiClient.getInstance().getApiService()
                            .cancelVisit(MyApplication.APP_VERSION, new VisitDTO(Me.getInstance().getIdx(), attractionIdx))
                            .enqueue(new Callback<ApiMessasge>(){
                                @Override
                                public void onResponse(Call<ApiMessasge> call, Response<ApiMessasge> response) {
                                    if (response.body() != null) {
                                        Alert.makeText("방문한 것 취소!");
                                        visitImg.setImageResource(R.drawable.z_footprint_empty_s);
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
                                public void onFailure(Call<ApiMessasge> call, Throwable t) {
                                    Alert.makeText(getResources().getString(R.string.network_error));
                                }
                            });
                }else{
                    ApiClient.getInstance().getApiService()
                            .visit(MyApplication.APP_VERSION, new VisitDTO(Me.getInstance().getIdx(), attractionIdx))
                            .enqueue(new Callback<ApiMessasge>(){
                                @Override
                                public void onResponse(Call<ApiMessasge> call, Response<ApiMessasge> response) {
                                    if (response.body() != null) {
                                        Alert.makeText("방문!");
                                        visitImg.setImageResource(R.drawable.z_footprint_s);
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
                                public void onFailure(Call<ApiMessasge> call, Throwable t) {
                                    Alert.makeText(getResources().getString(R.string.network_error));
                                }
                            });
                }
                //visitList에 변동이 생김을 알림 -> InfoFragment에선 이 변수를 기준으로 visitList를 업데이트 할지 결정함
                InfoFragment.VISITED_LIST_CHANGED = true;

//                if (visitImg.getDrawable().getConstantState()
//                        == getResources().getDrawable(R.drawable.z_footprint_empty_s).getConstantState()) {
//                    visitImg.setImageResource(R.drawable.z_footprint_s);
//                    ApiClient.getInstance().getApiService()
//                            .visit(MyApplication.APP_VERSION, new VisitDTO(Me.getInstance().getIdx()
//                                    , thisAttraction.getIdx()))
//                            .enqueue(new Callback<ApiMessasge>() {
//                                @Override
//                                public void onResponse(Call<ApiMessasge> call, Response<ApiMessasge> response) {
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
//                                public void onFailure(Call<ApiMessasge> call, Throwable t) {
//                                    Alert.makeText(getResources().getString(R.string.network_error));
//                                }
//                            });
//                } else {
//                    visitImg.setImageResource(R.drawable.z_footprint_empty_s);
//                    ApiClient.getInstance().getApiService()
//                            .cancelVisit(MyApplication.APP_VERSION
//                                    , new VisitDTO(Me.getInstance().getIdx(), attractionIdx))
//                            .enqueue(new Callback<ApiMessasge>() {
//                                @Override
//                                public void onResponse(Call<ApiMessasge> call, Response<ApiMessasge> response) {
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
//                                public void onFailure(Call<ApiMessasge> call, Throwable t) {
//                                    Alert.makeText(getResources().getString(R.string.network_error));
//                                }
//                            });
//                }
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
                    new LogManager().LogManager("상세정보: lineEndIndex",lineEndIndex+"");
                    new LogManager().LogManager("상세정보",
                            ": +tv.getText() : "
                                    +tv.getText());
                    new LogManager().LogManager("상세정보",
                            ": +tv.getText() + 5) : "
                                    +tv.getText().subSequence(0, lineEndIndex - expandText.length() + 5)+"");
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
}

/*
package com.tripkorea.on.ontripkorea.tabs.around.detail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.retrofit.message.ApiMessasge;
import com.tripkorea.on.ontripkorea.tabs.guide.GuideActivity;
import com.tripkorea.on.ontripkorea.tabs.info.InfoFragment;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttrClient;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionDetail;
import com.tripkorea.on.ontripkorea.vo.dto.LikeDTO;
import com.tripkorea.on.ontripkorea.vo.dto.VisitDTO;
import com.tripkorea.on.ontripkorea.vo.user.Me;
import com.tripkorea.on.ontripkorea.vo.youtube.YoutubeDetail;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Created by Edward Won on 2018-06-12.



public class AroundDetailMapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        LocationListener,
        View.OnClickListener {
    @BindView(R.id.detail_image_vp)
    ViewPager detailImageVp;
    @BindView(R.id.detail_title1)
    TextView titleTv;//여행지 title
    @BindView(R.id.detail_con1)
    TextView contentTv;//여행지 설명
    @BindView(R.id.detail_direction)
    TextView detailDirectionTv;//여행지 교통
    @BindView(R.id.address_content)
    TextView addressTv; //여행지 주소
    //    @BindView(R.id.duration_title)      TextView duration_title; //festival 해당
//    @BindView(R.id.duration_content)    TextView duration_content; //여행지의 운영일자 festival 해당
    @BindView(R.id.dayoff_content)
    TextView dayoffContentTv; //여행지 쉬는 날
    @BindView(R.id.operatinghours_content)
    TextView operatinghours_content; //여행지 운영시간
    @BindView(R.id.fee_content)
    TextView fee_content; //여행지 이용금액
    @BindView(R.id.img_detail_like)
    ImageView likeImg;//여행지-유저 좋아요
    @BindView(R.id.img_detail_footprint)
    ImageView visitImg;//여행지-유저 발자국
    @BindView(R.id.rating_best_layout)
    RelativeLayout ratingBestLayout;
    @BindView(R.id.rating_mid_layout)
    RelativeLayout ratingMidLayout;
    @BindView(R.id.rating_low_layout)
    RelativeLayout ratingLowLayout;

    //locale
    String usinglanguage;
    Locale locale;

    //map
    @BindView(R.id.around_detail_map)
    MapView aroundDetailMap;
    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    Location currentLocation;
    double currentLat, currentLong;

    //authority
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    AttractionDetail seletedAttr;//선택된 item의 공용 객체
    static public ArrayList<AttrClient> aroundList = new ArrayList<>();

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    private int attractionIdx;

    public static ArrayList<YoutubeDetail> youtubeDetails = new ArrayList<>();

    ProgressDialog detailProgressbar = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showaroundmap);
        ButterKnife.bind(this);

        aroundDetailMap.onCreate(savedInstanceState);
        aroundDetailMap.getMapAsync(this);

        new LogManager().LogManager("AroundDetailMapActivity","AroundDetailMapActivity ");

        detailProgressbar = ProgressDialog.show(AroundDetailMapActivity.this, "", "Loading...", true);

        attractionIdx = getIntent().getIntExtra("attractionIdx", 3466);
        new LogManager().LogManager("AroundDetailMapActivity detail: idx from around","thisAttraction.getIdx(); "+attractionIdx);



        //사용자 언어 확인
        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        usinglanguage = locale.getDisplayLanguage();

        int language = 0;
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
        new LogManager().LogManager("AroundDetailMapActivity","usinglanguage; "+usinglanguage);
        ApiClient.getInstance().getApiService()
                .getTourDetail(MyApplication.APP_VERSION, attractionIdx, language)
                .enqueue(new Callback<AttractionDetail>() {
                    @Override
                    public void onResponse(Call<AttractionDetail> call, Response<AttractionDetail> response) {
                        new LogManager().LogManager("AroundDetailMapActivity response",response.body()+"");
                        if (response.body() != null) {
                            seletedAttr = response.body();
                            if (seletedAttr.getThumnailAddr() != null && seletedAttr.getThumnailAddr().length() > 5) {
//                final MainImageRecyclerViewAdapter imgRvAdapter = new MainImageRecyclerViewAdapter();
//                for (int i = 0; i < MainActivity.attrImgURLArrayList.size(); i++) {
//                    if (thisAttraction.contentID.equals(MainActivity.attrImgURLArrayList.get(i).getContentID())) {
//                        imgRvAdapter.addImgList(MainActivity.attrImgURLArrayList.get(i));
//                    }
//                }
                                com.tripkorea.on.ontripkorea.tabs.around.detail.DetailedImageFragmentPagerAdapter detailedImageFragmentPagerAdapter
                                        = new com.tripkorea.on.ontripkorea.tabs.around.detail.DetailedImageFragmentPagerAdapter(getSupportFragmentManager(), 3);
//                String[] images = thisAttraction.firstimage2.split(",");
                                String thumnailAddr = seletedAttr.getThumnailAddr();
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
//                        fee_content.setText(thisAttraction.fee);

                                detailImageVp.setAdapter(detailedImageFragmentPagerAdapter);

                                titleTv.setText(seletedAttr.getName());
                                contentTv.setText(seletedAttr.getDetail());
                                detailDirectionTv.setText(seletedAttr.getRoute());
                                addressTv.setText(seletedAttr.getAddr());
//                dayoffContentTv.setText(thisAttraction.dayOff);
//                operatinghours_content.setText(thisAttraction.operatingHours);
                            } else {
                                detailImageVp.setVisibility(View.GONE);
                            }

                            new LogManager().LogManager(seletedAttr.getName(),seletedAttr.isLiked()+" | checkAttraction.isLiked() "+seletedAttr.getIdx());
                            if(seletedAttr.isLiked()){
                                likeImg.setImageResource(R.drawable.icon_heart_empty_image);
                            }else{
                                likeImg.setImageResource(R.drawable.z_heart_empty_s);
                            }

                            new LogManager().LogManager(seletedAttr.getName(),seletedAttr.isVisited()+" | checkAttraction.isVisited() "+seletedAttr.getIdx());
                            if(seletedAttr.isVisited()){
                                visitImg.setImageResource(R.drawable.z_footprint_s);
                            }else{
                                visitImg.setImageResource(R.drawable.icon_footprint_s);
                            }

                            if(mMap != null) {
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(seletedAttr.getLat(), seletedAttr.getLon()))
                                        .title(seletedAttr.getName())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_s)))
                                        .setTag(seletedAttr.getIdx());
                                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                new LogManager().LogManager("맵테스트_ApiClient", "mMap.getMapType(): " + mMap.getMapType());
                                new LogManager().LogManager("맵테스트_ApiClient", "moveCamera: " + seletedAttr.getLat() + " | " + seletedAttr.getLon());
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(seletedAttr.getLat(), seletedAttr.getLon())));
                                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng) {
                                        Intent intent = new Intent(AroundDetailMapActivity.this, AroundDetailedActivity.class);
                                        new LogManager().LogManager("showdetailactivity_맵클릭됨", "getName(): "+seletedAttr.getName());
                                        new LogManager().LogManager("showdetailactivity_맵클릭됨", "getLat(): "+seletedAttr.getLat());
                                        new LogManager().LogManager("showdetailactivity_맵클릭됨", "getLon(): "+seletedAttr.getLon());
                                        intent.putExtra("attractionMap", seletedAttr);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                        startActivity(intent);
                                    }
                                });
                            }

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



                        } else {
                            Alert.makeText("상세 정보 받아오던 중 에러 발생");
                            try {
                                Log.e("API_FAIL", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        new LogManager().LogManager("AroundDetailMap",seletedAttr.getName()+" | "+seletedAttr.getThumnailAddr());
                        new LogManager().LogManager("AroundDetailMap","값 받아옴");
                        initViews(attractionIdx);
                        if(detailProgressbar.isShowing()) {
                            detailProgressbar.dismiss();
                            new LogManager().LogManager("AroundDetailMap progress bar","디엔드");
//                            dialogShowing = false;
                        }

                    }

                    @Override
                    public void onFailure(Call<AttractionDetail> call, Throwable t) {
                        Alert.makeText(getResources().getString(R.string.network_error));
                    }
                });

////////////////////////////////////////////////////////////////////////////YHC 수정으로 불필요
//        AroundGuideGenerator aroundGuideGenerator = new AroundGuideGenerator();
//        aroundList =aroundGuideGenerator.aroundGuideGenerator();
//        for(int i=0; i<aroundList.size(); i++){
//            if(aroundList.get(i).contentID.equals(seletedAttr.contentID)){
//                aroundList.remove(aroundList.get(i));
//            }
//        }
////////////////////////////////////////////////////////////////////////////YHC 수정으로 불필요





    }


    private void initViews(int attractionIdx) {
        likeImg.setOnClickListener(this);
        visitImg.setOnClickListener(this);
        ratingBestLayout.setOnClickListener(this);
        ratingMidLayout.setOnClickListener(this);
        ratingLowLayout.setOnClickListener(this);

        if(attractionIdx == 3466){
            LinearLayout guidebtnLayout = findViewById(R.id.item_guidebtn_layout);
            guidebtnLayout.setVisibility(View.VISIBLE);
            guidebtnLayout.setOnClickListener(this);
        }

        new LogManager().LogManager("디테일","뷰완성");
    }

    private void checkMyLocation(){
        checkLocationPermission();
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        currentLocation = locationManager.getLastKnownLocation(provider);
        if(currentLocation != null) {
            currentLong = currentLocation.getLongitude();
            currentLat = currentLocation.getLatitude();
        }else{
            Toast.makeText(this, R.string.failtoloadlocation, Toast.LENGTH_LONG).show();
            currentLong = 37.577401;
            currentLat = 126.989511;
        }
//        double dist = distance(latitude, longitude, currentLat, currentLong, "meter");

    }

    private void checkLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(AroundDetailMapActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
//                        checkAround(mMap);
                        return false;
                    }
                });
            }else{
                ActivityCompat.requestPermissions(AroundDetailMapActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
//                    checkAround(mMap);
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
        Log.e("onLocationChanged","현재 위치: " + currentLong+" | "+currentLat);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
//        mMap.clear();
        Log.e("AroundDetailMapActivity","onMapReady 온맵레뒤");
        if(seletedAttr != null) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(seletedAttr.getLat(), seletedAttr.getLon()))
                    .title(seletedAttr.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_s)))
                    .setTag(seletedAttr.getIdx());
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            new LogManager().LogManager("맵테스트_ApiClient", "mMap.getMapType(): " + mMap.getMapType());
            new LogManager().LogManager("맵테스트_ApiClient", "moveCamera: " + seletedAttr.getLat() + " | " + seletedAttr.getLon());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(seletedAttr.getLat(), seletedAttr.getLon())));
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Intent intent = new Intent(AroundDetailMapActivity.this, AroundDetailedActivity.class);
                    new LogManager().LogManager("showdetailactivity_맵클릭됨", "getName(): "+seletedAttr.getName());
                    new LogManager().LogManager("showdetailactivity_맵클릭됨", "getLat(): "+seletedAttr.getLat());
                    new LogManager().LogManager("showdetailactivity_맵클릭됨", "getLon(): "+seletedAttr.getLon());
                    intent.putExtra("attractionMap", seletedAttr);
                    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(intent);
                }
            });
        }
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
                    if(currentLocation == null){
                        currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        latitude = currentLocation.getLatitude();
                        currentLat = latitude;
                        longitude = currentLocation.getLongitude();
                        currentLong = longitude;
                    }else{
                        latitude = currentLocation.getLatitude();
                        currentLat = latitude;
                        longitude = currentLocation.getLongitude();
                        currentLong = longitude;


                    }
                }

//                checkAround(mMap);

                return false;
            }
        });

//        checkAround(mMap);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        if(seletedAttr != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(seletedAttr.getLat(), seletedAttr.getLon())));
        }else{
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37, 127)));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.e("디테일","맵 눌려짐");
                Intent intentDetailed = new Intent(AroundDetailMapActivity.this, AroundDetailedActivity.class);
                startActivity(intentDetailed);
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.e("map InfoWindowClick","marker.getTitle(): "+marker.getTitle());
                for(int i=0; i<aroundList.size(); i++) {
                    if(marker.getTitle().equals(aroundList.get(i).title)) {
                        try{
                            int tempTest = Integer.parseInt(aroundList.get(i).contentID);
                            Intent intent = new Intent(AroundDetailMapActivity.this, AroundDetailedActivity.class);
                            intent.putExtra("thisAttraction", aroundList.get(i));
                            startActivity(intent);
                            finish();
                        }catch(NumberFormatException e){
                            AlertDialog dialog = createDialogBoxHome(aroundList.get(i));
                            dialog.show();
                        }
                    }
                }
            }


            private Drawable LoadImageFromWebOperations(String url){
                try
                {
                    InputStream is = (InputStream) new URL(url).getContent();
                    return Drawable.createFromStream(is, "src name");
                }catch (Exception e) {
                    System.out.println("dialog drawable Exc="+e);
                    return null;
                }
            }

            private AlertDialog createDialogBoxHome(AttrClient attrClient) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AroundDetailMapActivity.this);
                builder.setTitle(attrClient.title);
                builder.setMessage(attrClient.description);
                builder.setIcon(LoadImageFromWebOperations(attrClient.firstimage));
//            builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton(R.string.dialogyestitle, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGoogleApiClient.disconnect();
                    }

                });
                return builder.create();
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


    private void checkAround(GoogleMap mMap){
        Log.e("checkAround","주변점검 몇 개? "+aroundList.size());
        switch (seletedAttr.getCategoriyList().get(0)){
            case 1:
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(seletedAttr.getLat(), seletedAttr.getLon()))
                        .title(seletedAttr.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_activity)))
                        .setTag(seletedAttr.getIdx());
                break;
            case 3:
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(seletedAttr.getLat(), seletedAttr.getLon()))
                        .title(seletedAttr.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_food)))
                        .setTag(seletedAttr.getIdx());
                break;
            case 4:
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(seletedAttr.getLat(), seletedAttr.getLon()))
                        .title(seletedAttr.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_history)))
                        .setTag(seletedAttr.getIdx());
                break;
            case 6:
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(seletedAttr.getLat(), seletedAttr.getLon()))
                        .title(seletedAttr.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_museum)))
                        .setTag(seletedAttr.getIdx());
                break;
            default:
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(seletedAttr.getLat(), seletedAttr.getLon()))
                        .title(seletedAttr.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_bus)))
                        .setTag(seletedAttr.getIdx());
                break;
        }


        for(int i=0; i< aroundList.size(); i++) {
//            if(i != 0 && i != 9 && i!=58) {
            String tmpY = aroundList.get(i).mapy.trim();
            String tmpX = aroundList.get(i).mapx.trim();

            LatLng location =
                    new LatLng(Double.parseDouble(tmpY), Double.parseDouble(tmpX));
            switch (aroundList.get(i).categoryNum) {
                case "4":
                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(aroundList.get(i).title)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_history_s_black)))
                            .setTag(aroundList.get(i).contentID);
                    break;
                case "6":
                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(aroundList.get(i).title)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_museum_s_black)))
                            .setTag(aroundList.get(i).contentID);
                    break;
                case "1":
                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(aroundList.get(i).title)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_activity_s_black)))
                            .setTag(aroundList.get(i).contentID);
                    break;
                case "3":
                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(aroundList.get(i).title)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_food_s_black)))
                            .setTag(aroundList.get(i).contentID);
                    break;
                default:
                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(aroundList.get(i).title)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_bus)))
                            .setTag(aroundList.get(i).contentID);
                    break;
            }
//            }

//            String id = String.valueOf(aroundList.get(i).contentID);
//            new YoutubeAsyncTask().execute(id, new GetYoutubeKey().takeYoutubekey(id));
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        aroundDetailMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        stopAudio();
        aroundDetailMap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aroundDetailMap.onDestroy();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_detail_like:
                if(seletedAttr.isLiked()){
                    ApiClient.getInstance().getApiService()
                            .cancelLike(MyApplication.APP_VERSION, new LikeDTO(Me.getInstance().getIdx(), attractionIdx))
                            .enqueue(new Callback<ApiMessasge>(){
                                @Override
                                public void onResponse(Call<ApiMessasge> call, Response<ApiMessasge> response) {
                                    if (response.body() != null) {
                                        Alert.makeText("좋아요 취소!");
                                        likeImg.setImageResource(R.drawable.z_heart_empty_s);
                                        seletedAttr.setLiked(false);
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
                                        likeImg.setImageResource(R.drawable.icon_heart_empty_image);
                                        seletedAttr.setLiked(true);
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
if (likeImg.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.z_heart_empty_s).getConstantState()) {
                    likeImg.setImageResource(R.drawable.icon_heart_empty_image);
//                    Glide.with(AroundDetailActivity.this).load(R.drawable.icon_heart_empty_image).into(likeImg);
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
                }

                break;
            case R.id.img_detail_footprint:
                if(seletedAttr.isVisited()){
                    ApiClient.getInstance().getApiService()
                            .cancelVisit(MyApplication.APP_VERSION, new VisitDTO(Me.getInstance().getIdx(), attractionIdx))
                            .enqueue(new Callback<ApiMessasge>(){
                                @Override
                                public void onResponse(Call<ApiMessasge> call, Response<ApiMessasge> response) {
                                    if (response.body() != null) {
                                        Alert.makeText("방문한 것 취소!");
                                        visitImg.setImageResource(R.drawable.icon_footprint_s);
                                        seletedAttr.setVisited(false);
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
                                        seletedAttr.setVisited(true);
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
//                        == getResources().getDrawable(R.drawable.icon_footprint_s).getConstantState()) {
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
//                    visitImg.setImageResource(R.drawable.icon_footprint_s);
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
                Intent guideIntent = new Intent(AroundDetailMapActivity.this, GuideActivity.class);
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


 @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList("likeList", (ArrayList<AttrClient>)MainActivity.likeEntities);
//        outState.putParcelableArrayList("traceList",(ArrayList<AttrClient>)MainActivity.traceEntities);
        outState.putParcelableArrayList("aroundList",aroundList);
        outState.putParcelable("Attr",seletedAttr);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        MainActivity.likeEntities = savedInstanceState.getParcelableArrayList("likeList");
//        MainActivity.traceEntities = savedInstanceState.getParcelableArrayList("traceList");
        aroundList = savedInstanceState.getParcelableArrayList("aroundList");
        seletedAttr = savedInstanceState.getParcelable("Attr");

    }


}
*/

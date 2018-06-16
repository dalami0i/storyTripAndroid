package com.tripkorea.on.ontripkorea.tabs.around;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.vo.attraction.AttrClient;
import com.tripkorea.on.ontripkorea.vo.attraction.AttrClientList;
import com.tripkorea.on.ontripkorea.vo.youtube.YoutubeDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Edward Won on 2018-06-12.
 */

public class AroundDetailActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        LocationListener {
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
    @BindView(R.id.item_like)
    ImageView iv_item_like;//여행지-유저 좋아요
    @BindView(R.id.item_footprint)
    ImageView iv_item_footprint;//여행지-유저 발자국
    @BindView(R.id.rating_best_layout)
    RelativeLayout rating_best_layout;
    @BindView(R.id.rating_mid_layout)
    RelativeLayout rating_mid_layout;
    @BindView(R.id.rating_low_layout)
    RelativeLayout rating_low_layout;

    //locale
    String usinglanguage;
    Locale locale;

    //map
    @BindView(R.id.around_map)
    MapView guideMap;
    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    Location currentLocation;
    double currentLat, currentLong;

    //youtube
    @BindView(R.id.links_list_rv)
    RecyclerView youtubeLinkRV;

    //authority
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    AttrClient savedobj;//선택된 item의 공용 객체
    public List<AttrClient> aroundList = new ArrayList<>();


    SharedPreferences setting;
    SharedPreferences.Editor editor;


    public static ArrayList<YoutubeDetail> youtubeDetails = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showarounddetailactivity);
        ButterKnife.bind(this);

//        guideMap.onCreate(savedInstanceState);


        //사용자 언어 확인
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        usinglanguage = locale.getDisplayLanguage();

        guideMap.getMapAsync(this);


        Intent intent = getIntent();
        if (intent != null) {
            savedobj = intent.getParcelableExtra("attraction");
            aroundList = intent.getParcelableExtra("aroundList");
            Log.e("디테일", "savedobj: " + savedobj.contentID);
        }

        if (savedobj != null) {
            if (savedobj.firstimage != null && savedobj.firstimage.length() > 5) {
//                final MainImageRecyclerViewAdapter imgRvAdapter = new MainImageRecyclerViewAdapter();
//                for (int i = 0; i < MainActivity.attrImgURLArrayList.size(); i++) {
//                    if (savedobj.contentID.equals(MainActivity.attrImgURLArrayList.get(i).getContentID())) {
//                        imgRvAdapter.addImgList(MainActivity.attrImgURLArrayList.get(i));
//                    }
//                }
                DetailedImageFragmentPagerAdapter detailedImageFragmentPagerAdapter
                        = new DetailedImageFragmentPagerAdapter(getSupportFragmentManager(), savedobj.popular);
//                String[] images = savedobj.firstimage2.split(",");
                switch (savedobj.categoryNum) {
                    case "3":
                        for (String tmpImg : savedobj.firstimage.split(",")) {
                            detailedImageFragmentPagerAdapter.addDetailedImage(tmpImg.trim());
                        }

                        fee_content.setText(savedobj.fee);
                        break;
                    default:
                        for (String tmpImg : savedobj.firstimage2.split(",")) {
                            detailedImageFragmentPagerAdapter.addDetailedImage(tmpImg.trim());
                        }
                        fee_content.setVisibility(View.GONE);
                        break;
                }

                detailImageVp.setAdapter(detailedImageFragmentPagerAdapter);

                titleTv.setText(savedobj.title);
                contentTv.setText(savedobj.description);
                detailDirectionTv.setText(savedobj.direction);
                addressTv.setText(savedobj.addr);
                dayoffContentTv.setText(savedobj.dayOff);
                operatinghours_content.setText(savedobj.operatingHours);
                detailDirectionTv.setText(savedobj.direction);
            } else {
                detailImageVp.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(AroundDetailActivity.this, R.string.wrong_access, Toast.LENGTH_LONG).show();
            finish();
        }

        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();


        for (int i = 0; i < MainActivity.traceEntities.size(); i++) {
            if (savedobj.contentID.equals(MainActivity.traceEntities.get(i).contentID)
                    && MainActivity.traceEntities.get(i).traceCheck == 2) { //&& user.getUserid().equals(traceEntities.get(i).getUserID())
                iv_item_footprint.setImageResource(R.drawable.z_footprint_s);
//                Glide.with(AroundDetailActivity.this).load(R.drawable.z_footprint_s).into(iv_item_footprint);
                break;
            } else {
                iv_item_footprint.setImageResource(R.drawable.z_footprint_empty_s);
//                Glide.with(AroundDetailActivity.this).load(R.drawable.z_footprint_empty_s).into(iv_item_footprint);
            }
        }
        if (MainActivity.traceEntities.size() == 0) {
            iv_item_footprint.setImageResource(R.drawable.z_footprint_empty_s);
        }

        iv_item_footprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iv_item_footprint.getDrawable().getConstantState()
                        == getResources().getDrawable(R.drawable.z_footprint_empty_s).getConstantState()) {
                    Log.e("풋프린트 체크", "아이 비지티드");
                    iv_item_footprint.setImageResource(R.drawable.z_footprint_s);

                    MainActivity.traceEntities.add(savedobj);

                    for (int i = 0; i < aroundList.size(); i++) {
                        if (savedobj.contentID.equals(savedobj.contentID)) {
                            savedobj.traceCheck = 2;
                        }
                    }
                    for (int i = 0; i < MainActivity.traceEntities.size(); i++) {
                        if (savedobj.contentID.equals(MainActivity.traceEntities.get(i).contentID)) {
                            MainActivity.traceEntities.get(i).traceCheck = 2;
                            Log.e("풋프린트 체크", MainActivity.traceEntities.get(i).title + " | " + MainActivity.traceEntities.get(i).traceCheck + " | ");
                        }
                    }
                    AttrClientList attrTotalClientList = new AttrClientList();
                    attrTotalClientList.setItems(aroundList);
                    Gson gson = new Gson();
                    String totalList = gson.toJson(attrTotalClientList);
                    editor.putString("totalList", totalList);
                    editor.commit();

                    AttrClientList attrTraceClientList = new AttrClientList();
                    attrTraceClientList.setItems(MainActivity.traceEntities);
                    Gson gsonTrace = new Gson();
                    String traceList = gsonTrace.toJson(attrTraceClientList);
                    editor.putString("traceList", traceList);
                    editor.commit();

                } else {
                    Log.e("풋프린트 언체크", "아이 돈 비지티드");
                    iv_item_footprint.setImageResource(R.drawable.z_footprint_empty_s);
//                    Glide.with(AroundDetailActivity.this).load(R.drawable.z_footprint_empty_s).into(iv_item_footprint);

                    for (int i = 0; i < MainActivity.traceEntities.size(); i++) {
                        if (savedobj.contentID.equals(MainActivity.traceEntities.get(i).contentID)) {
                            MainActivity.traceEntities.remove(i);
                        }
                    }

                    savedobj.traceCheck = 1;

                    AttrClientList attrTotalClientList = new AttrClientList();
                    attrTotalClientList.setItems(aroundList);
                    Gson gson = new Gson();
                    String totalList = gson.toJson(attrTotalClientList);
                    editor.putString("totalList", totalList);
                    editor.commit();

                    AttrClientList attrTraceClientList = new AttrClientList();
                    attrTraceClientList.setItems(MainActivity.traceEntities);
                    Gson gsonTrace = new Gson();
                    String traceList = gsonTrace.toJson(attrTraceClientList);
                    editor.putString("traceList", traceList);
                    editor.commit();
                    //인터넷으로 전송
//                    if(InternetTotalCheck.checkInternet()) {
//                        new TraceContentActivity(1).execute(savedobj.contentID);
//                    }else{
//                        Toast.makeText(MyApplication.getGuideContext(),
//                                R.string.internet_failed,Toast.LENGTH_LONG).show();
//                    }
                }
            }
        });

        for (int i = 0; i < MainActivity.likeEntities.size(); i++) {
            if (savedobj.title.equals(MainActivity.likeEntities.get(i).title)
                    && MainActivity.likeEntities.get(i).likeCheck == 2) { //&& user.getUserid().equals(likeEntities.get(i).getUserID())
                Log.e("라이크상태 체크", "savedobj.title: " + savedobj.title + " | " + savedobj.likeCheck + " | ");
                Log.e("라이크상태 체크", "MainActivity.likeEntities.get(i).title: " + MainActivity.likeEntities.get(i).title + " | " + MainActivity.likeEntities.get(i).likeCheck + " | i:" + i);
                iv_item_like.setImageResource(R.drawable.z_heart_image_s);
//                Glide.with(AroundDetailActivity.this).load(R.drawable.z_heart_image_s).into(iv_item_like);
                break;
            } else {
                Log.e("라이크언상태 체크", "savedobj.title: " + savedobj.title + " | " + savedobj.likeCheck + " | ");
                Log.e("라이크언상태 체크", "MainActivity.likeEntities.get(i).title: " + MainActivity.likeEntities.get(i).title + " | " + MainActivity.likeEntities.get(i).likeCheck + " | i:" + i);
                iv_item_like.setImageResource(R.drawable.z_heart_empty_s);
//                Glide.with(AroundDetailActivity.this).load(R.drawable.z_heart_empty_s).into(iv_item_like);
            }
        }
        if (MainActivity.likeEntities.size() == 0) {
            iv_item_like.setImageResource(R.drawable.z_heart_empty_s);
        }
        iv_item_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Glide.with(AroundDetailActivity.this).asDrawable().into(iv_item_like) == getResources().getDrawable(z_heart_empty_s)
                if (iv_item_like.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.z_heart_empty_s).getConstantState()) {
                    Log.e("라이크 체크", "아이 라이킷");
                    iv_item_like.setImageResource(R.drawable.z_heart_image_s);
//                    Glide.with(AroundDetailActivity.this).load(R.drawable.z_heart_image_s).into(iv_item_like);

                    MainActivity.likeEntities.add(savedobj);

                    for (int i = 0; i < aroundList.size(); i++) {
                        if (savedobj.contentID.equals(savedobj.contentID)) {
                            savedobj.likeCheck = 2;
                            Log.e("라이크 체크", savedobj.title + " | " + savedobj.likeCheck + " | ");
                        }
                    }

                    for (int i = 0; i < MainActivity.likeEntities.size(); i++) {
                        if (savedobj.contentID.equals(MainActivity.likeEntities.get(i).contentID)) {
                            MainActivity.likeEntities.get(i).likeCheck = 2;
                            Log.e("라이크 체크", MainActivity.likeEntities.get(i).title + " | " + MainActivity.likeEntities.get(i).likeCheck + " | ");
                        }
                    }

                    AttrClientList attrTotalClientList = new AttrClientList();
                    attrTotalClientList.setItems(aroundList);
                    Gson gson = new Gson();
                    String totalList = gson.toJson(attrTotalClientList);
                    editor.putString("totalList", totalList);
                    editor.commit();

                    AttrClientList attrLikeClientList = new AttrClientList();
                    attrLikeClientList.setItems(MainActivity.likeEntities);
                    Gson gsonLike = new Gson();
                    String likeList = gsonLike.toJson(attrLikeClientList);
                    Log.e("ShowDetailActivity", "likeList" + likeList);
                    editor.putString("likeList", likeList);
                    editor.commit();

                    //internet check
//                    if(InternetTotalCheck.checkInternet()) {
//                        new LikeContentActivity(2).execute(savedobj.contentID);
//                    }else{
//                        Toast.makeText(MyApplication.getGuideContext(),
//                                R.string.internet_failed,Toast.LENGTH_LONG).show();
//                    }

                } else {
                    Log.e("라이크 언체크", "아이 돈 라이킷");
                    iv_item_like.setImageResource(R.drawable.z_heart_empty_s);
//                    Glide.with(AroundDetailActivity.this).load(R.drawable.z_heart_empty_s).into(iv_item_like);


                    for (int i = 0; i < MainActivity.likeEntities.size(); i++) {
                        if (savedobj.contentID.equals(MainActivity.likeEntities.get(i).contentID)) {
                            MainActivity.likeEntities.remove(i);
                        }
                    }

                    for (int i = 0; i < aroundList.size(); i++) {
                        if (savedobj.contentID.equals(savedobj.contentID)) {
                            savedobj.likeCheck = 1;
                            Log.e("라이크 언체크", savedobj.title + " | " + savedobj.likeCheck + " | ");
                        }
                    }

                    AttrClientList attrTotalClientList = new AttrClientList();
                    attrTotalClientList.setItems(aroundList);
                    Gson gson = new Gson();
                    String totalList = gson.toJson(attrTotalClientList);
                    editor.putString("totalList", totalList);
                    editor.commit();

                    AttrClientList attrLikeClientList = new AttrClientList();
                    attrLikeClientList.setItems(MainActivity.likeEntities);
                    Gson gsonLike = new Gson();
                    String likeList = gsonLike.toJson(attrLikeClientList);
                    editor.putString("likeList", likeList);
                    editor.commit();
                    //internet check
//                    if(InternetTotalCheck.checkInternet()) {
//                        new LikeContentActivity(1).execute(savedobj.contentID);
//                    }else{
//                        Toast.makeText(MyApplication.getGuideContext(),
//                                R.string.internet_failed,Toast.LENGTH_LONG).show();
//                    }

                }
            }
        });

        rating_best_layout.setOnClickListener(bestReviewBtn);
        rating_mid_layout.setOnClickListener(midReviewBtn);
        rating_low_layout.setOnClickListener(lowReviewBtn);

        if (savedobj.youtubekey != null) {
            LinearLayoutManager linkLayoutManager
                    = new LinearLayoutManager(AroundDetailActivity.this, LinearLayoutManager.VERTICAL, false);
            youtubeLinkRV.setLayoutManager(linkLayoutManager);
            YoutubeItemRVAdapter linkItemRecyclerViewAdapter = new YoutubeItemRVAdapter();
            DividerItemDecoration dividerItemDecorationLinkTransportation
                    = new DividerItemDecoration(youtubeLinkRV.getContext(), linkLayoutManager.getOrientation());
            youtubeLinkRV.addItemDecoration(dividerItemDecorationLinkTransportation);
            youtubeLinkRV.setAdapter(linkItemRecyclerViewAdapter);
            youtubeLinkRV.setNestedScrollingEnabled(false);
        }
    }

    View.OnClickListener bestReviewBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(AroundDetailActivity.this, R.string.image_select_toast, Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(Intent.ACTION_PICK).setType(MediaStore.Images.Media.CONTENT_TYPE);
//            startActivityForResult(intent,BEST_REVIEW);
        }
    };
    View.OnClickListener midReviewBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(AroundDetailActivity.this, R.string.image_select_toast, Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(Intent.ACTION_PICK).setType(MediaStore.Images.Media.CONTENT_TYPE);
//            startActivityForResult(intent,MID_REVIEW);
        }
    };
    View.OnClickListener lowReviewBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(AroundDetailActivity.this, R.string.image_select_toast, Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(Intent.ACTION_PICK).setType(MediaStore.Images.Media.CONTENT_TYPE);
//            startActivityForResult(intent,LOW_REVIEW);
        }
    };

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
            if (ContextCompat.checkSelfPermission(AroundDetailActivity.this,
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
                ActivityCompat.requestPermissions(AroundDetailActivity.this,
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
        Log.e("onLocationChanged", "현재 위치: " + currentLong + " | " + currentLat);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
//        GoogleMapOptions options = new GoogleMapOptions().liteMode(true);

        mMap = googleMap;

        LatLng attraction = new LatLng(Double.parseDouble(savedobj.mapy), Double.parseDouble(savedobj.mapx));
        mMap.addMarker(new MarkerOptions()
                .position(attraction)
                .title(savedobj.title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_s)))
                .setTag(savedobj.contentID);

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
                        latitude = currentLocation.getLatitude();
                        currentLat = latitude;
                        longitude = currentLocation.getLongitude();
                        currentLong = longitude;
                    } else {
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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(attraction));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.e("showdetailactivity", savedobj.title + "맵클릭됨");
                Intent intent = new Intent(AroundDetailActivity.this, AroundDetailMapActivity.class);
                intent.putExtra("attractionMap", savedobj);
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(intent);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("aroundAttr", savedobj);
        outState.putParcelableArrayList("likeList", (ArrayList<AttrClient>)MainActivity.likeEntities);
        outState.putParcelableArrayList("traceList", (ArrayList<AttrClient>)MainActivity.traceEntities);
        outState.putParcelableArrayList("aroundList", (ArrayList<AttrClient>) aroundList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedobj = savedInstanceState.getParcelable("aroundAttr");
        MainActivity.likeEntities = savedInstanceState.getParcelableArrayList("likeList");
        MainActivity.traceEntities = savedInstanceState.getParcelableArrayList("traceList");
        aroundList = savedInstanceState.getParcelableArrayList("aroundList");

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
            mMap.clear();
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //디테일 상단 이미지를 위한 view pager
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class DetailedImageFragmentPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<String> detailedImageFragments = new ArrayList<>();
        int popular;

        private void addDetailedImage(String detailedImage) {
            detailedImageFragments.add(detailedImage);
        }


        private DetailedImageFragmentPagerAdapter(FragmentManager fm, int popular) {
            super(fm);
            this.popular = popular;
        }

        @Override
        public float getPageWidth(int position) {
            return 1.0f;
        }

        @Override
        public Fragment getItem(int position) {
            return new DetailedImageListFragment().newInstance(
                    detailedImageFragments.get(position), detailedImageFragments.size(), position, popular);
        }

        @Override
        public int getCount() {
            return detailedImageFragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    public static class DetailedImageListFragment extends Fragment {
        private String detailedImgAddr;
        AroundDetailActivity owner;
        RoundedImageView detailed_img;
        int size;
        int position;
        int width;
        int height;
        int popular;
        Unbinder unbinder;

        public DetailedImageListFragment() {
        }

        public static DetailedImageListFragment newInstance(
                String detailedImgAddr, int size, int position, int popular) {
            Bundle bundle = new Bundle();
            bundle.putInt("totalsize", size);
            bundle.putInt("position", position);
            bundle.putInt("popular", popular);
            bundle.putString("detailedImage", detailedImgAddr);
            DetailedImageListFragment detailedImageListFragment = new DetailedImageListFragment();
            detailedImageListFragment.setArguments(bundle);
            return detailedImageListFragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                size = getArguments().getInt("totalsize");
                position = getArguments().getInt("position");
                width = getArguments().getInt("width");
                height = getArguments().getInt("height");
                popular = getArguments().getInt("popular");
                detailedImgAddr = getArguments().getString("detailedImage");
            }
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            LinearLayout view = (LinearLayout) inflater.inflate(R.layout.detaileditem_image, container, false);
            unbinder = ButterKnife.bind(this, view);
            owner = (AroundDetailActivity) getActivity();
            detailed_img = view.findViewById(R.id.detailed_img);
            TextView right_arrow = view.findViewById(R.id.right_arrow);
            TextView left_arrow = view.findViewById(R.id.left_arrow);


            Log.e("길이와 위치:", "길이: " + size + " 위치: " + detailedImgAddr);

            if (owner != null) {
                Glide.with(owner).load(detailedImgAddr).into(detailed_img);
                if (size == 1) {
                    right_arrow.setVisibility(View.GONE);
                    left_arrow.setVisibility(View.GONE);
                } else if (position == 0) {
                    right_arrow.setVisibility(View.GONE);
                    left_arrow.setVisibility(View.VISIBLE);
                } else if (position == (size - 1)) {
                    right_arrow.setVisibility(View.VISIBLE);
                    left_arrow.setVisibility(View.GONE);
                } else {
                    right_arrow.setVisibility(View.VISIBLE);
                    left_arrow.setVisibility(View.VISIBLE);
                }
            }


            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //유투브 링크 adapter
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class YoutubeItemRVAdapter extends RecyclerView.Adapter<YoutubeItemRVAdapter.ViewHolder> {

        private YoutubeItemRVAdapter() {
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final RoundedImageView iv_youtube_thumbnail;
            private final TextView tv_youtube_title;
            private final TextView tv_youtube_content;
            //            private final TextView  tv_youtube_intro;
//            private final TextView  tv_youtube_distance;
            private final View mView;

            private ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                iv_youtube_thumbnail = mView.findViewById(R.id.around_youtube_thumbnail);
                tv_youtube_title = mView.findViewById(R.id.around_youtube_title);
                tv_youtube_content = mView.findViewById(R.id.around_youtube_detail);
//                tv_youtube_intro = mView.findViewById(R.id.around_youtube_intro);
//                tv_youtube_distance = mView.findViewById(R.id.around_youtube_distance);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.showaroundyoutubeitem, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final YoutubeDetail youtubeDetail = AroundDetailActivity.youtubeDetails.get(position);
            holder.tv_youtube_title.setText(youtubeDetail.title);
            holder.tv_youtube_content.setText(youtubeDetail.description);
            holder.iv_youtube_thumbnail.setCornerRadius((float) 10);
            String[] youtubeKeys = youtubeDetail.youtubeKey.split(",");
            String youtubeAddr = getString(R.string.youtube_img_former) + youtubeKeys[position] + getString(R.string.youtube_img_later);
            Glide.with(AroundDetailActivity.this).load(youtubeAddr).into(holder.iv_youtube_thumbnail);
//            Log.e("youtube",youtubeKey+"| title: "+linkEntity.youtubetitle);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(getString(R.string.youtube_connect) + youtubeDetail.youtubeKey);
                    Intent goyoutube = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goyoutube);
                }
            });
        }

        @Override
        public int getItemCount() {
            return AroundDetailActivity.youtubeDetails.size();
        }
    }

}

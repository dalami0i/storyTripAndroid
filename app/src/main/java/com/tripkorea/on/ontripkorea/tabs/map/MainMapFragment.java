package com.tripkorea.on.ontripkorea.tabs.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.tabs.list.ListDetailActivity;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.util.OnNetworkErrorListener;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

///**
// * Created by YangHC on 2018-06-05.
// */

public class MainMapFragment extends Fragment  implements
        OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnInfoWindowClickListener {

    int lastTab;

    MapView mainMap;
    ImageView currentMapViewCenter;

    MainActivity main;
    int language;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    ProgressDialog loadingGuide = null;
    TabLayout mapTablayout;

    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    Location currentLocation;
    double currentLat, currentLong;
    double latitude, longitude;
    ArrayList<Marker> markers = new ArrayList<>();
    AttractionSimpleList tourList;
    AttractionSimpleList foodList;
    ViewPager mapViewPager;
    MapTourBottomPagerAdapter mapTourBottomViewAdapter;
    MapFoodBottomPagerAdapter mapFoodBottomViewAdapter;

    public OnNetworkErrorListener onNetworkErrorListener;

    public Fragment mainMapFragmentNewInstance(MainActivity main, int lastTab, AttractionSimpleList tourList, AttractionSimpleList foodList, int language){//GoogleMap mMap,
        this.lastTab = lastTab;
        this.main = main;
        this.tourList = tourList;
        this.foodList = foodList;
        this.language = language;
        return new MainMapFragment();
    }

    public void setOnNetworkErrorListener(OnNetworkErrorListener onNetworkErrorListener) {
        this.onNetworkErrorListener = onNetworkErrorListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadingGuide = ProgressDialog.show(main, "", "Loading map...", true);
        View view = inflater.inflate(R.layout.fragment_mapbase, container, false);

        mainMap = view.findViewById(R.id.map_gnb);
        currentMapViewCenter = view.findViewById(R.id.iv_map_current_map);



        mainMap.onCreate(savedInstanceState);
        mainMap.getMapAsync(this);
        currentMapViewCenter.setOnClickListener(currentMapLocationAround);

        initViews(view);
        //getCartoon(language);




        return view;
    }

    View.OnClickListener currentMapLocationAround = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LatLng tmpLatLng = mMap.getCameraPosition().target;
            currentLat = tmpLatLng.latitude;
            currentLong = tmpLatLng.longitude;
            setRestaurants(currentLat,currentLong,1);

        }
    };


    private void initViews(View view) {
        new LogManager().LogManager("메인맵프레그먼트","initViews 진입");

        if(tourList == null || tourList.getItems().size() < 1){
            List<AttractionSimple> tmpList = new ArrayList<>();
            AttractionSimple tmpAttraction = new AttractionSimple();
            tmpAttraction.setThumnailAddr("http://tong.visitkorea.or.kr/cms/resource/79/883179_image3_1.jpg");
            tmpAttraction.setIdx(3497);
            tmpAttraction.setName("Jongmyo");
            tmpAttraction.setLat(37.57312044);
            tmpAttraction.setLon(126.994836);
            tmpList.add(tmpAttraction);

            AttractionSimple tmpAttraction2 = new AttractionSimple();
            tmpAttraction2.setThumnailAddr("http://tong.visitkorea.or.kr/cms/resource/31/654531_image2_1.jpg");
            tmpAttraction2.setIdx(3497);
            tmpAttraction2.setName("Changdeokgung");
            tmpAttraction2.setLat(37.58095837);
            tmpAttraction2.setLon(126.9919888);
            tmpList.add(tmpAttraction2);

            tourList.setItems(tmpList);
        }

        mapViewPager = view.findViewById(R.id.vp_map_bottom);
        mapTourBottomViewAdapter = new MapTourBottomPagerAdapter(main.getSupportFragmentManager(),main,tourList);
        mapFoodBottomViewAdapter = new MapFoodBottomPagerAdapter(main.getSupportFragmentManager(),main,foodList);
        mapViewPager.setAdapter(mapTourBottomViewAdapter);
        mapViewPager.setClipToPadding(false);
        mapViewPager.setPadding(40,0,40,0);
        mapViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {            }

            @Override
            public void onPageSelected(int position) {
                switch (mapTablayout.getSelectedTabPosition()){
                    case 0:
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(
                                new LatLng(tourList.getItems().get(position).getLat(), tourList.getItems().get(position).getLon())));
                        break;
                    case 1:
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(
                                new LatLng(foodList.getItems().get(position).getLat(), foodList.getItems().get(position).getLon())));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {            }
        });

        mapTablayout = view.findViewById(R.id.map_tablayout);
        mapTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        mapTourBottomViewAdapter.changeList(tourList);
                        mapTablayout.getTabAt(mapTablayout.getSelectedTabPosition()).select();
                        mMap.clear();
                        Bitmap placeMarker = resizeMarker(((BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker_place)).getBitmap());
                        checkAround(tourList, placeMarker);
                        mapTourBottomViewAdapter.notifyDataSetChanged();
                        mapViewPager.setAdapter(mapTourBottomViewAdapter);

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(
                                new LatLng(tourList.getItems().get(0).getLat(), tourList.getItems().get(0).getLon())));
                        break;
                    case 1:
                        mapFoodBottomViewAdapter.changeList(foodList);
                        mapTablayout.getTabAt(mapTablayout.getSelectedTabPosition()).select();
                        mMap.clear();
                        Bitmap resMarker = resizeMarker(((BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker_res)).getBitmap());
                        int resizeWidth = 100;
                        double aspectRatio = (double) resMarker.getHeight() / (double) resMarker.getWidth();
                        int targetHeight = (int) (resizeWidth * aspectRatio);
                        resMarker = Bitmap.createScaledBitmap(resMarker, resizeWidth, targetHeight, false);
                        checkAround(foodList, resMarker);
                        mapFoodBottomViewAdapter.notifyDataSetChanged();
                        mapViewPager.setAdapter(mapFoodBottomViewAdapter);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(
                                new LatLng(foodList.getItems().get(0).getLat(), foodList.getItems().get(0).getLon())));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private Bitmap resizeMarker(Bitmap marker){
        int resizeWidth = 200;
        double aspectRatio = (double) marker.getHeight() / (double) marker.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);
        return Bitmap.createScaledBitmap(marker, resizeWidth, targetHeight, false);
    }


    private void checkMyLocation(){
        checkLocationPermission();
        LocationManager locationManager = (LocationManager)main.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        currentLocation = locationManager.getLastKnownLocation(provider);
        if(currentLocation != null) {
            currentLong = currentLocation.getLongitude();
            currentLat = currentLocation.getLatitude();
        }else{
            Toast.makeText(main, R.string.failtoloadlocation, Toast.LENGTH_LONG).show();
            currentLong = 37.577401;
            currentLat = 126.989511;
        }
//        double dist = distance(latitude, longitude, currentLat, currentLong, "meter");

    }

    private void checkLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(main,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);

            }else{
                ActivityCompat.requestPermissions(main,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            buildGoogleApiClient();
            Bitmap placeMarker = ((BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker_place)).getBitmap();
            checkAround(tourList, placeMarker);
        }
    }

    protected synchronized void buildGoogleApiClient() {
//        new LogManager().printLogManager("빌드에이피아이","비이이이이이틀");
        mGoogleApiClient = new GoogleApiClient.Builder(main)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        longitude = location.getLongitude(); //경도
        latitude = location.getLatitude(); //위도
        Log.e("onLocationChanged","현재 위치: " + longitude+" | "+latitude);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        new LogManager().LogManager("MainmapFragment","onMapReady");
        mMap = googleMap;
        checkLocationPermission();
        Bitmap placeMarker = resizeMarker(((BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker_place)).getBitmap());
        checkAround(tourList, placeMarker);
        checkMyLocation();


        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        if(longitude <1){
            longitude = 126.989297;
            latitude = 37.577395;
        }
        if(tourList.getItems().size() >= 1){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(tourList.getItems().get(0).getLat(), tourList.getItems().get(0).getLon())));
        }else {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        }
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e("map markerClick","marker.getTitle(): "+marker.getTitle());
                marker.showInfoWindow();
                return false;
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                switch (mapTablayout.getSelectedTabPosition()){
                    case 0:
                        for(int i=0; i<tourList.getItems().size(); i++){
                            if(marker.getTitle().equals(tourList.getItems().get(i).getName())){
                                mapViewPager.setCurrentItem(i);
                            }
                        }
                        break;
                    case 1:
                        for(int i=0; i<foodList.getItems().size(); i++){
                            if(marker.getTitle().equals(foodList.getItems().get(i).getName())){
                                mapViewPager.setCurrentItem(i);
                            }
                        }
                        break;
                }

                return false;
            }
        });
        new LogManager().LogManager("mainMapFragment","지도완성");
        loadingGuide.dismiss();

    }


    private void checkAround(AttractionSimpleList list, Bitmap marker){
        Log.e("MainMapFragment","콘텐츠 몇 개? "+list.getItems().size());
        for(int i=0; i< list.getItems().size(); i++) {
            LatLng location =  new LatLng(list.getItems().get(i).getLat(), list.getItems().get(i).getLon());
            Marker tempMarker = mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(list.getItems().get(i).getName())
                    .icon(BitmapDescriptorFactory.fromBitmap(marker))
            );//.icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_bg))

            markers.add(tempMarker);
        }
    }

    // TODO : 서버에서 주변 맛집 정보 가져오기
    private void setRestaurants(final double lat,final double lon, final int page) {
        ApiClient.getInstance().getApiService()
                .getAroundRestaurants(MyApplication.APP_VERSION,lat, lon, language, page)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        new LogManager().LogManager("list엑티비티","setRestaurant : "+lat+" | "+lon);
                        if (response.body() != null) {
                            foodList = new AttractionSimpleList();
                            foodList.setItems(response.body());
                            new LogManager().LogManager("list엑티비티","setRestaurant size: "+foodList.getItems().size());
                            setTours(lat, lon, page);

                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("list엑티비티 RESTAURANTS", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AttractionSimple>> call, Throwable t) {
                        Alert.makeText(getString(R.string.network_error));

                    }
                });
    }

    // TODO : 서버에서 주변 관광지 정보 가져오기
    private void setTours(final double lat, final double lon, int page) {
        ApiClient.getInstance().getApiService()
                .getAroundTours(MyApplication.APP_VERSION,lat, lon, language, page)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        new LogManager().LogManager("list엑티비티","setTour : "+lat+" | "+lon);
                        if (response.body() != null) {
                            tourList = new AttractionSimpleList();
                            tourList.setItems(response.body());
                            new LogManager().LogManager("list엑티비티","setTour size: "+tourList.getItems().size());
                            listChange();
                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("list엑티비티 Tours", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AttractionSimple>> call, Throwable t) {
                        Alert.makeText(getString(R.string.network_error));

                    }
                });
    }

    private void listChange(){
        new LogManager().LogManager("ListItemFragment 위치","upsideTabLayout.getSelectedTabPosition(): "+mapTablayout.getSelectedTabPosition());
        switch(mapTablayout.getSelectedTabPosition()){
            case 0:

                mapTourBottomViewAdapter.clearMapList();
                mapTourBottomViewAdapter.changeList(tourList);
                mMap.clear();
                markers.clear();
                Bitmap placeMarker = resizeMarker(((BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker_place)).getBitmap());
                for(int i=0; i<tourList.getItems().size(); i++){
                    LatLng location =  new LatLng(tourList.getItems().get(i).getLat(), tourList.getItems().get(i).getLon());
                    Marker tempMarker = mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(tourList.getItems().get(i).getName())
                            .icon(BitmapDescriptorFactory.fromBitmap(placeMarker))
                    );//.icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_bg))
                    markers.add(tempMarker);
                }
                mapTablayout.getTabAt(mapTablayout.getSelectedTabPosition()).select();
                mapTourBottomViewAdapter.notifyDataSetChanged();
                mapViewPager.setAdapter(mapTourBottomViewAdapter);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(
                        new LatLng(tourList.getItems().get(0).getLat(), tourList.getItems().get(0).getLon())));
                break;
            case 1:
                mapFoodBottomViewAdapter.clearMapList();
                mapFoodBottomViewAdapter.changeList(foodList);
                mMap.clear();
                markers.clear();
                Bitmap resMarker = resizeMarker(((BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker_res)).getBitmap());
                int resizeWidth = 100;
                double aspectRatio = (double) resMarker.getHeight() / (double) resMarker.getWidth();
                int targetHeight = (int) (resizeWidth * aspectRatio);
                resMarker = Bitmap.createScaledBitmap(resMarker, resizeWidth, targetHeight, false);
                for(int i=0; i<foodList.getItems().size(); i++){
                    LatLng location =  new LatLng(foodList.getItems().get(i).getLat(), foodList.getItems().get(i).getLon());
                    Marker tempMarker = mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(foodList.getItems().get(i).getName())
                            .icon(BitmapDescriptorFactory.fromBitmap(resMarker))
                    );//.icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_bg))
                    markers.add(tempMarker);
                }
                mapTablayout.getTabAt(mapTablayout.getSelectedTabPosition()).select();
                mapFoodBottomViewAdapter.notifyDataSetChanged();
                mapViewPager.setAdapter(mapFoodBottomViewAdapter);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(
                        new LatLng(foodList.getItems().get(0).getLat(), foodList.getItems().get(0).getLon())));
                break;

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mainMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mainMap.onPause();
    }



    @Override
    public void onInfoWindowClick(Marker marker) {
        switch (mapTablayout.getSelectedTabPosition()){
            case 0:
                for(int i=0; i<tourList.getItems().size(); i++){
                    if(marker.getTitle().equals(tourList.getItems().get(i).getName())){
                        Intent intent = new Intent(main, ListDetailActivity.class);
                        intent.putExtra("attractionIdx",tourList.getItems().get(i).getIdx());
                        main.startActivity(intent);
                    }
                }
                break;
            case 1:
                for(int i=0; i<foodList.getItems().size(); i++){
                    if(marker.getTitle().equals(foodList.getItems().get(i).getName())){
                        Intent intent = new Intent(main, ListDetailActivity.class);
                        intent.putExtra("attractionIdx",foodList.getItems().get(i).getIdx());
                        main.startActivity(intent);
                    }
                }
                break;
        }

    }


}


/*locationManager = (LocationManager) main.getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                locationManager.requestSingleUpdate(criteria, new android.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        currentLat = location.getLatitude();
                        currentLong = location.getLongitude();
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLat, currentLong)));

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(currentLat, currentLong))
                                .icon(BitmapDescriptorFactory.
                                        fromBitmap(
                                                ((BitmapDrawable)getResources().
                                                        getDrawable(R.drawable.z_mycurrentlocation)).getBitmap())));
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                }, null);*/

/* @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("GuideFragment",guideEntities);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            guideEntities = savedInstanceState.getParcelableArrayList("GuideFragment");
        }
    }*/

/*Toon tempToon = new Toon();
        for(int i=0; i<toons.size(); i++){
            if(marker.getTitle().equals(toons.get(i).getTitle())){
                tempToon = toons.get(i);
            }
        }
        new LogManager().LogManager("ToonFragment","인텐트 보내기 실행 전-tempToonSize: "+tempToon.getTitle()+"|");
        new LogManager().LogManager("ToonFragment","인텐트 보내기 실행 전-tempToonSize: "+tempToon.getCartoonImageAddressList().size()+"|");
        ToonImageEntity toonImageEntity = new ToonImageEntity();
        toonImageEntity.setToonList(tempToon.getCartoonImageAddressList());
        Intent showToonDetail = new Intent(main, ToonDetailImageActivity.class);
        showToonDetail.putExtra("toons",toonImageEntity);
        main.startActivity(showToonDetail);*/
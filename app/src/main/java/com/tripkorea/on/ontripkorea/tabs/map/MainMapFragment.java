package com.tripkorea.on.ontripkorea.tabs.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.util.OnNetworkErrorListener;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by YangHC on 2018-06-05.
 */

public class MainMapFragment extends Fragment  implements
        OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnInfoWindowClickListener {

    int lastTab;

    MapView mainMap;
    ImageView currentMapViewCenter;
    LocationManager locationManager;

    MainActivity main;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    ProgressDialog loadingGuide = null;
    static final String CONNECTION_CONFIRM_CLIENT_URL = "http://clients3.google.com/generate_204";
    String usinglanguage;
    Locale locale;

    //map
//    private static VoiceGuide guideEntity;
//    static ArrayList<Toon> toonEntities = new ArrayList<>();

    TabLayout mapTablayout;

    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    Location currentLocation;
    double currentLat, currentLong;
    double latitude, longitude;
    ArrayList<Marker> markers = new ArrayList<>();
    AttractionSimpleList tourList;
    AttractionSimpleList foodList;

    final MapBottomRecyclerViewAdapter mapBottomRecyclerViewAdapter
            = new MapBottomRecyclerViewAdapter();


//    private static VoiceGuideLocation locEntity;
//    private List<Toon> toons = new ArrayList<>();

    public OnNetworkErrorListener onNetworkErrorListener;

    public Fragment mainMapFragmentNewInstance(MainActivity main, int lastTab, AttractionSimpleList tourList, AttractionSimpleList foodList){//GoogleMap mMap,
        this.lastTab = lastTab;
        this.main = main;
        mapBottomRecyclerViewAdapter.addContext(main);
        this.tourList = tourList;
        this.foodList = foodList;
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
        RecyclerView mapBottomRV = view.findViewById(R.id.rv_map_bottom);

        LinearLayoutManager recommendLayoutManager
                = new LinearLayoutManager(MyApplication.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mapBottomRV.setLayoutManager(recommendLayoutManager);

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
        for(int i=0; i<tourList.getItems().size(); i++){
            mapBottomRecyclerViewAdapter.addMapListView(tourList.getItems().get(i));
        }



        mapBottomRV.setAdapter(mapBottomRecyclerViewAdapter);

        mapTablayout = view.findViewById(R.id.map_tablayout);
        mapTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        mapBottomRecyclerViewAdapter.clearMapList();
                        for(int i=0; i<tourList.getItems().size(); i++){
                            mapBottomRecyclerViewAdapter.addMapListView(tourList.getItems().get(i));
                        }
                        mapBottomRecyclerViewAdapter.notifyDataSetChanged();
                        mapTablayout.getTabAt(mapTablayout.getSelectedTabPosition()).select();
                        mMap.clear();
                        checkAround(tourList);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(
                                new LatLng(tourList.getItems().get(0).getLat(), tourList.getItems().get(0).getLon())));
                        break;
                    case 1:
                        mapBottomRecyclerViewAdapter.clearMapList();
                        for(int i=0; i<foodList.getItems().size(); i++){
                            mapBottomRecyclerViewAdapter.addMapListView(foodList.getItems().get(i));
                        }
                        mapBottomRecyclerViewAdapter.notifyDataSetChanged();
                        mapTablayout.getTabAt(mapTablayout.getSelectedTabPosition()).select();
                        mMap.clear();
                        checkAround(foodList);
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
            }else{
                ActivityCompat.requestPermissions(main,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            buildGoogleApiClient();
            checkAround(tourList);
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
        checkAround(tourList);
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
        new LogManager().LogManager("mainMapFragment","지도완성");
        loadingGuide.dismiss();

    }


    private void checkAround(AttractionSimpleList list){
        Log.e("MainMapFragment","콘텐츠 몇 개? "+list.getItems().size());
        for(int i=0; i< list.getItems().size(); i++) {
            LatLng location =  new LatLng(list.getItems().get(i).getLat(), list.getItems().get(i).getLon());
            Marker tempMarker = mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(list.getItems().get(i).getName())
                    .alpha(0.5f)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
            );//.icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_bg))
            markers.add(tempMarker);
        }
    }

    // TODO : 서버에서 주변 맛집 정보 가져오기
    private void setRestaurants(final double lat,final double lon, final int page) {
        ApiClient.getInstance().getApiService()
                .getAroundRestaurants(MyApplication.APP_VERSION,lat, lon,4, page)
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
                .getAroundTours(MyApplication.APP_VERSION,lat, lon,4, page)
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
                mapBottomRecyclerViewAdapter.clearMapList();
                mMap.clear();
                markers.clear();
                for(int i=0; i<tourList.getItems().size(); i++){
                    mapBottomRecyclerViewAdapter.addMapListView(tourList.getItems().get(i));
                    LatLng location =  new LatLng(tourList.getItems().get(i).getLat(), tourList.getItems().get(i).getLon());
                    Marker tempMarker = mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(tourList.getItems().get(i).getName())
                            .alpha(0.5f)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    );//.icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_bg))
                    markers.add(tempMarker);
                }
                mapBottomRecyclerViewAdapter.notifyDataSetChanged();
                mapTablayout.getTabAt(mapTablayout.getSelectedTabPosition()).select();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(
                        new LatLng(tourList.getItems().get(0).getLat(), tourList.getItems().get(0).getLon())));
                break;
            case 1:
                mapBottomRecyclerViewAdapter.clearMapList();
                mMap.clear();
                markers.clear();
                for(int i=0; i<foodList.getItems().size(); i++){
                    mapBottomRecyclerViewAdapter.addMapListView(foodList.getItems().get(i));
                    LatLng location =  new LatLng(foodList.getItems().get(i).getLat(), foodList.getItems().get(i).getLon());
                    Marker tempMarker = mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(foodList.getItems().get(i).getName())
                            .alpha(0.5f)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    );//.icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_bg))
                    markers.add(tempMarker);
                }
                mapBottomRecyclerViewAdapter.notifyDataSetChanged();
                mapTablayout.getTabAt(mapTablayout.getSelectedTabPosition()).select();
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
    }

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
}

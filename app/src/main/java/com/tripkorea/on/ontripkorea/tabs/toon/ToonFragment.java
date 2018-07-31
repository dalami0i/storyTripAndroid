package com.tripkorea.on.ontripkorea.tabs.toon;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.tripkorea.on.ontripkorea.vo.toonguide.Toon;
import com.tripkorea.on.ontripkorea.vo.toonguide.ToonImageEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by YangHC on 2018-06-05.
 */

public class ToonFragment extends Fragment  implements
        OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnInfoWindowClickListener {

    int lastTab;

    MapView toonMap;


    MainActivity main;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    static final String CONNECTION_CONFIRM_CLIENT_URL = "http://clients3.google.com/generate_204";
    String usinglanguage;
    Locale locale;

    //map
//    private static VoiceGuide guideEntity;
//    static ArrayList<Toon> toonEntities = new ArrayList<>();


    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    Location currentLocation;
    double currentLat, currentLong;
    double latitude, longitude;
    ArrayList<Marker> markers = new ArrayList<>();

//    private static VoiceGuideLocation locEntity;
    private List<Toon> toons = new ArrayList<>();

    public OnNetworkErrorListener onNetworkErrorListener;

    public Fragment toonFragmentNewInstance(GoogleMap mMap, MainActivity main, int lastTab){//GoogleMap mMap,
        this.lastTab = lastTab;
        this.main = main;
        this.mMap = mMap;
        return new ToonFragment();
    }

    public void setOnNetworkErrorListener(OnNetworkErrorListener onNetworkErrorListener) {
        this.onNetworkErrorListener = onNetworkErrorListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toon, container, false);

        toonMap = view.findViewById(R.id.toon_map);

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

        toonMap.onCreate(savedInstanceState);
        toonMap.getMapAsync(this);

        initViews(view);
        getCartoon(language);



        return view;
    }

    private void initViews(View view) {

    }

    private void getCartoon(final int language){
        ApiClient.getInstance().getApiService()
                .getCartoon(MyApplication.APP_VERSION, 1024,language)
                .enqueue(new Callback<List<Toon>>() {
                    @Override
                    public void onResponse(Call<List<Toon>> call, Response<List<Toon>> response) {
                        if(response.body() != null){
                            toons = response.body();
                            new LogManager().LogManager("toonFragment","toons.size(): "+toons.size()+" | ");
                            checkAround();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Toon>> call, Throwable t) {
                        Alert.makeText(getResources().getString(R.string.network_error));
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
                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        checkAround();
                        return false;
                    }
                });
            }else{
                ActivityCompat.requestPermissions(main,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    checkAround();
                    return false;
                }
            });
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
        new LogManager().LogManager("ToonFragment","onMapReady");
        mMap = googleMap;
        checkLocationPermission();
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                checkLocationPermission();
                if (ContextCompat.checkSelfPermission(MyApplication.getContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MyApplication.getContext(),"위치 권한 허락 요망",Toast.LENGTH_LONG).show();
                }else {
                    LocationManager locationManager = (LocationManager)
                            MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
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
                }

                return false;
            }
        });
        checkAround();
        checkMyLocation();


        mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        if(longitude <1){
            longitude = 126.989297;
            latitude = 37.577395;
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e("map markerClick","marker.getTitle(): "+marker.getTitle());
                marker.showInfoWindow();
                return false;
            }
        });
        new LogManager().LogManager("toonFragment","지도완성");

    }


    private void checkAround(){
        Log.e("ToonFragment","콘텐츠 몇 개? "+toons.size());
        for(int i=0; i< toons.size(); i++) {
            LatLng location =  new LatLng(toons.get(i).getLat(), toons.get(i).getLon());
            new LogManager().LogManager("Toon-checkAround","Lat: "+toons.get(i).getLat()+" | Lon: "+toons.get(i).getLon());
            Marker tempMarker = mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(toons.get(i).getTitle())
                    .alpha(0.5f)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
            );//.icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_bg))
            markers.add(tempMarker);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        toonMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        toonMap.onPause();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toon tempToon = new Toon();
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
        main.startActivity(showToonDetail);
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

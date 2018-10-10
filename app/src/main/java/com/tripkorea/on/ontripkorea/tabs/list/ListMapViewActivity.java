package com.tripkorea.on.ontripkorea.tabs.list;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.vo.attraction.AttrClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Edward Won on 2018-06-12.
 */

public class ListMapViewActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        LocationListener {


    //map
    @BindView(R.id.map_main_locationchange)
    MapView mainChangeLocationMap;
    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    Location currentLocation;
    double currentLat, currentLong;
    @BindView(R.id.iv_map_current_map)
    ImageView currentMapCenterLocation;
    @BindView(R.id.tv_sendbacklocation)
    TextView sendBackBtn;

    //authority
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    static public ArrayList<AttrClient> aroundList = new ArrayList<>();

    boolean changeLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        ButterKnife.bind(this);


        mainChangeLocationMap.onCreate(savedInstanceState);
        mainChangeLocationMap.getMapAsync(this);

        currentMapCenterLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLat = mMap.getCameraPosition().target.latitude;
                currentLong = mMap.getCameraPosition().target.longitude;
                Log.e("ListMapViewActivity","currentMapCenterLocation.setOnClickListener 위치 변경 맵기준: " + currentLong+" | "+currentLat);
            }
        });

        sendBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLat = mMap.getCameraPosition().target.latitude;
                currentLong = mMap.getCameraPosition().target.longitude;
                Log.e("ListMapViewActivity","currentMapCenterLocation.setOnClickListener 위치 변경 맵기준: " + currentLong+" | "+currentLat);
                changeLocation = true;
                onBackPressed();
            }
        });

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
            currentLong = 126.989511;
            currentLat = 37.577401;
        }
//        double dist = distance(latitude, longitude, currentLat, currentLong, "meter");

        Log.e("ListMapViewActivity","checkMyLocation() 위치 확인: " + currentLong+" | "+currentLat);

    }

    private void checkLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ListMapViewActivity.this,
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
                ActivityCompat.requestPermissions(ListMapViewActivity.this,
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
//        Log.e("onMapReady","맵 청소 함");
        checkLocationPermission();
        checkMyLocation();
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                checkLocationPermission();
                checkMyLocation();
                return false;
            }
        });
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLat, currentLong)));


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
        if(changeLocation) {
            Intent sendLocation = new Intent();
            sendLocation.putExtra("lat", currentLat);
            sendLocation.putExtra("lon", currentLong);
            setResult(RESULT_OK, sendLocation);
            Log.e("ListMapViewActivity", "onBackPressed() 위치 전송 완료: " + currentLong + " | " + currentLat);
            finish();
        }else{
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainChangeLocationMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        stopAudio();
        mainChangeLocationMap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainChangeLocationMap.onDestroy();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

   /* @Override
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

    }*/

}

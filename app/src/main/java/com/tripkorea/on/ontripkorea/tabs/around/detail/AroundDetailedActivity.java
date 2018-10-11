/*
package com.tripkorea.on.ontripkorea.tabs.around.detail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.tripkorea.on.ontripkorea.vo.attraction.AttrClient;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionDetail;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

*/
/**
 * Created by Edward Won on 2018-06-12.
 *//*


public class AroundDetailedActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        LocationListener {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showaround);
        ButterKnife.bind(this);

        //사용자 언어 확인
        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        usinglanguage = locale.getDisplayLanguage();

        Intent intent = getIntent();
        seletedAttr = (AttractionDetail)intent.getSerializableExtra("attractionMap");
        if(seletedAttr != null){
            Log.e("디테일드","thisAttraction: "+seletedAttr.getName());
        }else{
            Log.e("디테일드","thisAttraction: none-----------------------------------");
        }


////////////////////////////////////////////////////////////////////////////YHC 수정으로 불필요
//        AroundGuideGenerator aroundGuideGenerator = new AroundGuideGenerator();
//        aroundList =aroundGuideGenerator.aroundGuideGenerator();
//        for(int i=0; i<aroundList.size(); i++){
//            if(aroundList.get(i).contentID.equals(seletedAttr.contentID)){
//                aroundList.remove(aroundList.get(i));
//            }
//        }
////////////////////////////////////////////////////////////////////////////YHC 수정으로 불필요

        aroundDetailMap.onCreate(savedInstanceState);
        aroundDetailMap.getMapAsync(this);

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
            if (ContextCompat.checkSelfPermission(AroundDetailedActivity.this,
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
                ActivityCompat.requestPermissions(AroundDetailedActivity.this,
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

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.e("map InfoWindowClick","marker.getTitle(): "+marker.getTitle());
                for(int i=0; i<aroundList.size(); i++) {
                    if(marker.getTitle().equals(aroundList.get(i).title)) {
                        try{
                            int tempTest = Integer.parseInt(aroundList.get(i).contentID);
                            Intent intent = new Intent(AroundDetailedActivity.this, AroundDetailActivity.class);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AroundDetailedActivity.this);
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

   */
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

    }*//*


}
*/

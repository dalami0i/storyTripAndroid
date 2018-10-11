package com.tripkorea.on.ontripkorea;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.Coordinate;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;
import com.tripkorea.on.ontripkorea.vo.user.Me;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

///**
// * Created by YangHC on 2018-06-18.
// */

public class SplashActivity extends AppCompatActivity{

    Locale locale;
    String usinglanguage;
    int language;
    double lat, lon;
    double tmpLat, tmpLon;

    @BindView(R.id.txt_splash_logo)
    TextView logoTxt;
    @BindView(R.id.txt_splash_logo2)
    TextView logo2Txt;

    private AttractionSimpleList totalList;
    private AttractionSimpleList foodList;
    private AttractionSimpleList attractionList;
    private AttractionSimpleList recommendations;
    private Coordinate coordinate;


    GoogleApiClient mGoogleApiClient;

    final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    ProgressDialog locationPermissionProgress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        locationPermissionProgress = ProgressDialog.show(SplashActivity.this, "", "Getting Location Permission&Date from server...", true);

        //사용자 언어 확인
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }

        usinglanguage = locale.getDisplayLanguage();

        language = 0;
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

        Typeface geBody = Typeface.createFromAsset(getAssets(), "fonts/GeBody.ttf");
        logoTxt.setTypeface(geBody);
        logo2Txt.setTypeface(geBody);
//        setTotals(coordinate.getLat(), coordinate.getLon(), 1);

        new LogManager().LogManager("스엑티비티","사용자 확인 "+ Me.getInstance().getIdx());
        checkLocationPermission();



    }




    private void checkLocationPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(SplashActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                locationManager.requestSingleUpdate(criteria, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
                        setTotals(coordinate.getLat(), coordinate.getLon(), 1);
                        setRecommends();
                        lat = coordinate.getLat();
                        lon = coordinate.getLon();
//                        setTotals(37.579108, 126.990957, 1);

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
                }, null);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

                    setTotals(37.579108, 126.990957, 1);
                    setRecommends();
                    lat = 37.579108;
                    lon = 126.990957;
                    Alert.makeText(getString(R.string.inform_turnedoff_location_function));
                }


            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setMessage( getResources().getString(R.string.get_location_permission))
                        .setCancelable(true)
                        .setPositiveButton( getResources().getString(R.string.get_location_permission_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(SplashActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .setNegativeButton( getResources().getString(R.string.get_location_permission_no) , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create().show();
//                coordinate = new Coordinate(37.579108, 126.990957);
//                setTotals(coordinate.getLat(), coordinate.getLon(), 1);
//                setRecommends();


            }
        } else {
            buildGoogleApiClient();
            LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            locationManager.requestSingleUpdate(criteria, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
                    setTotals(location.getLatitude(), location.getLongitude(), 1);
                    setRecommends();
                    lat = location.getLatitude();
                    lon = location.getLongitude();
//                    setTotals(37.579108, 126.990957, 1);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {   }
                @Override
                public void onProviderEnabled(String provider) {     }
                @Override
                public void onProviderDisabled(String provider) {         }
            }, null);

        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

    }


    private void setTotals(final double lat,final double lon, final int page) {
        ApiClient.getInstance().getApiService()
                .getAroundAttractions(MyApplication.APP_VERSION,lat, lon,language, page)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        new LogManager().LogManager("스엑티비티","settotal - lat: "+lat+" | lon: "+lon+" | language: "+language+" | page: "+page);
                        if (response.body() != null) {
                            totalList = new AttractionSimpleList();
                            totalList.setItems(response.body());
                            new LogManager().LogManager("total log",response.body().toString());
                            new LogManager().LogManager("스엑티비티","settotal size: "+totalList.getItems().size());
                            if(totalList.getItems().size() < 5){
                                setTotals(37.579108, 126.990957, 1);
                                tmpLat = 37.579108;
                                tmpLon = 126.990957;
                            }else{
                                setRestaurants(lat, lon, page);
                            }

                        } else {
                            if(locationPermissionProgress != null && locationPermissionProgress.isShowing()) locationPermissionProgress.dismiss();
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("스엑티비티 total", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AttractionSimple>> call, Throwable t) {
                        if(locationPermissionProgress != null && locationPermissionProgress.isShowing()) locationPermissionProgress.dismiss();
                        Alert.makeText(getString(R.string.network_error));
                    }
                });
    }


    private void setRestaurants(final double lat,final double lon, final int page) {
        ApiClient.getInstance().getApiService()
                .getAroundRestaurants(MyApplication.APP_VERSION,lat, lon,language, page)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        new LogManager().LogManager("스엑티비티","setRestaurant : "+lat+" | "+lon);
                        if (response.body() != null) {
                            foodList = new AttractionSimpleList();
                            foodList.setItems(response.body());
                            new LogManager().LogManager("스엑티비티","setRestaurant size: "+foodList.getItems().size());
                            setTours(lat, lon, page);

                        } else {
                            if(locationPermissionProgress != null && locationPermissionProgress.isShowing()) locationPermissionProgress.dismiss();
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("스엑티비티 RESTAURANTS", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AttractionSimple>> call, Throwable t) {
                        if(locationPermissionProgress != null && locationPermissionProgress.isShowing()) locationPermissionProgress.dismiss();
                        Alert.makeText(getString(R.string.network_error));

                    }
                });
    }

    private void setTours(final double lat, final double lon, int page) {
        ApiClient.getInstance().getApiService()
                .getAroundTours(MyApplication.APP_VERSION,lat, lon,language, page)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        new LogManager().LogManager("스엑티비티","setTour : "+lat+" | "+lon);
                        if (response.body() != null) {
                            attractionList = new AttractionSimpleList();
                            attractionList.setItems(response.body());
                            new LogManager().LogManager("스엑티비티","setTour size: "+attractionList.getItems().size());

                            goToNext();

                        } else {
                            if(locationPermissionProgress != null && locationPermissionProgress.isShowing()) locationPermissionProgress.dismiss();
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("스엑티비티 Tours", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AttractionSimple>> call, Throwable t) {
                        if(locationPermissionProgress != null && locationPermissionProgress.isShowing()) locationPermissionProgress.dismiss();
                        Alert.makeText(getString(R.string.network_error));

                    }
                });
    }

    private void setRecommends() {
        ApiClient.getInstance().getApiService()
                .getRecommendations(MyApplication.APP_VERSION,language)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        new LogManager().LogManager("스엑티비티","setRecommends - "+" | language: "+language);
                        if (response.body() != null) {
                            recommendations = new AttractionSimpleList();
                            recommendations.setItems(response.body());

                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("스엑티비티 setRecommends", "error : " + response.errorBody().string());
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



    public void goToNext(){
        new LogManager().LogManager("스플레쉬","goToNext");
        Intent loginIntent = new Intent(SplashActivity.this, MainActivity.class);
        MainActivity.totalList = totalList;
        MainActivity.foodList = foodList;
        MainActivity.attractionList = attractionList;
        if(lat != tmpLat){
            lat = tmpLat;
            lon = tmpLon;
        }
        loginIntent.putExtra("lat",lat);//37.579108 coordinate.getLat()
        loginIntent.putExtra("lon",lon);//126.990957  coordinate.getLon()
        loginIntent.putExtra("recommendations", recommendations);
        loginIntent.putExtra("page",1);
        startActivity(loginIntent);
        if(locationPermissionProgress != null && locationPermissionProgress.isShowing()) locationPermissionProgress.dismiss();
        finish();

    }

    @Override
    public void onBackPressed() {
        // 로딩 중에 종료하지 못하게 막음
        // super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(SplashActivity.this,
                                android.Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            buildGoogleApiClient();
                            LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                            Criteria criteria = new Criteria();
                            criteria.setAccuracy(Criteria.ACCURACY_FINE);
                            criteria.setPowerRequirement(Criteria.POWER_LOW);
                            locationManager.requestSingleUpdate(criteria, new LocationListener() {

                                @Override
                                public void onLocationChanged(Location location) {
                                    coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
                                    setTotals(coordinate.getLat(), coordinate.getLon(), 1);
                                    setRecommends();
                                    lat = coordinate.getLat();
                                    lon = coordinate.getLon();
//                        setTotals(37.579108, 126.990957, 1);

                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle extras) {   }
                                @Override
                                public void onProviderEnabled(String provider) {     }
                                @Override
                                public void onProviderDisabled(String provider) {         }
                            }, null);
                            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                                setTotals(37.579108, 126.990957, 1);
                                setRecommends();
                                lat = 37.579108;
                                lon = 126.990957;
                                Alert.makeText(getString(R.string.inform_turnedoff_location_function));
                            }
                        }else{
                            coordinate = new Coordinate(37.579108, 126.990957);
                            setTotals(coordinate.getLat(), coordinate.getLon(), 1);
                            setRecommends();
                        }
                    } else {
                        buildGoogleApiClient();
                        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                        Criteria criteria = new Criteria();
                        criteria.setAccuracy(Criteria.ACCURACY_FINE);
                        criteria.setPowerRequirement(Criteria.POWER_LOW);
                        locationManager.requestSingleUpdate(criteria, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
                                setTotals(location.getLatitude(), location.getLongitude(), 1);
                                setRecommends();
                                lat = location.getLatitude();
                                lon = location.getLongitude();
//                    setTotals(37.579108, 126.990957, 1);
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {   }
                            @Override
                            public void onProviderEnabled(String provider) {     }
                            @Override
                            public void onProviderDisabled(String provider) {         }
                        }, null);

                    }
                    if(locationPermissionProgress != null && locationPermissionProgress.isShowing()) locationPermissionProgress.dismiss();
                }else{
                    Alert.makeText(getString(R.string.get_location_request_again));
                    finish();
                }
                break;

        }
    }
}

/*new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                mainIntent.putExtra("foods",foodList);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);*/

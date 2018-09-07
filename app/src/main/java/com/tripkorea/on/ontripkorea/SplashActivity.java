package com.tripkorea.on.ontripkorea;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.Coordinate;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;
import com.tripkorea.on.ontripkorea.vo.voiceguide.GuidePhoto;

import java.io.IOException;
import java.util.ArrayList;
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

public class SplashActivity extends AppCompatActivity implements OnMapReadyCallback{
    private final int SPLASH_DISPLAY_LENGTH = 2500;

    Locale locale;
    String usinglanguage;
    int language;

    @BindView(R.id.txt_splash_logo)
    TextView logoTxt;
    @BindView(R.id.txt_splash_logo2)
    TextView logo2Txt;

    private List<AttractionSimple> restaurantList = new ArrayList<>();
    private ArrayList<GuidePhoto> guidePhotoList = new ArrayList<>();
    private AttractionSimpleList totalList;
    private AttractionSimpleList foodList;
    private AttractionSimpleList attractionList;
    private Coordinate coordinate;

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

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


        new LogManager().LogManager("스엑티비티","checkLocationPermission(); 직전");
        checkLocationPermission();



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        new LogManager().LogManager("스엑티비티","onMapReady");
        checkLocationPermission();
    }

    private void checkLocationPermission(){
        new LogManager().LogManager("스엑티비티","checkLocationPermission");
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
                        new LogManager().LogManager("스엑티비티","1setTotals(coordinate.getLat(), coordinate.getLon(), 1);");
//                        setTotals(coordinate.getLat(), coordinate.getLon(), 1);
                        setTotals(37.579108, 126.990957, 1);


                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {   }
                    @Override
                    public void onProviderEnabled(String provider) {     }
                    @Override
                    public void onProviderDisabled(String provider) {         }
                }, null);

            }else{
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
                coordinate = new Coordinate(37.579108, 126.990957);
                setTotals(coordinate.getLat(), coordinate.getLon(), 1);
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
//                    setTotals(location.getLatitude(), location.getLongitude(), 1);
                    setTotals(37.579108, 126.990957, 1);
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
        Log.e("빌드에이피아이","비이이이이이틀");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

    }


    // TODO : 서버에서 주변 관광지 전체 정보 가져오기
    private void setTotals(final double lat,final double lon, final int page) {
        ApiClient.getInstance().getApiService()
                .getAroundAttractions(MyApplication.APP_VERSION,lat, lon,language, page)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        new LogManager().LogManager("스엑티비티","settotal : "+lat+" | "+lon);
                        if (response.body() != null) {
                            totalList = new AttractionSimpleList();
                            totalList.setItems(response.body());
                            new LogManager().LogManager("스엑티비티","settotal size: "+totalList.getItems().size());
                            setRestaurants(lat, lon, page);

                        } else {
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
                        Alert.makeText(getString(R.string.network_error));

                    }
                });
    }


    // TODO : 서버에서 주변 맛집 정보 가져오기
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
                        Alert.makeText(getString(R.string.network_error));

                    }
                });
    }

    // TODO : 서버에서 주변 관광지 정보 가져오기
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
                            if(attractionList.getItems().size() > 0) {
                                goToNext();
                            }else{
                                new LogManager().LogManager("스플레쉬","setTours size error");
                            }

                        } else {
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
                        Alert.makeText(getString(R.string.network_error));

                    }
                });
    }




    public void goToNext(){
        new LogManager().LogManager("스플레쉬","goToNext");
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("guides",guidePhotoList);
//        mainIntent.putExtras(bundle);
        mainIntent.putExtra("totals",totalList);
        mainIntent.putExtra("foods",foodList);
        mainIntent.putExtra("attraction",attractionList);
        mainIntent.putExtra("lat",coordinate.getLat());
        mainIntent.putExtra("lon",coordinate.getLon());
        mainIntent.putExtra("page",1);
        startActivity(mainIntent);
        finish();
        /*new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                mainIntent.putExtra("foods",foodList);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);*/
    }

    @Override
    public void onBackPressed() {
        // 로딩 중에 종료하지 못하게 막음
        // super.onBackPressed();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////안쓰는 메소드
    // TODO : 서버에서 가이드 정보 가져오기
    private void setGuides(final double lat,final double lon, final int page) {
        new LogManager().LogManager("스플레쉬","setGuides 검색위치: "+lat+" | "+lon);
        ApiClient.getInstance().getApiService()
                .getGuidePhotos(MyApplication.APP_VERSION,lat, lon,4, page)
                .enqueue(new Callback<ArrayList<GuidePhoto>>() {
                    @Override
                    public void onResponse(Call<ArrayList<GuidePhoto>> call, Response<ArrayList<GuidePhoto>> response) {
                        if (response.body() != null) {
                            guidePhotoList = response.body();
                            setRestaurants(lat, lon, page);
                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("스엑티비티 setGuides", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<GuidePhoto>> call, Throwable t) {
                        Alert.makeText(getString(R.string.network_error));
                        new LogManager().LogManager("스플레쉬 setGuides","네트워크 에러?");
                    }
                });
    }




}

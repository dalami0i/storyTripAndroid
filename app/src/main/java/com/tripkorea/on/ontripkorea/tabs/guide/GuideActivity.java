package com.tripkorea.on.ontripkorea.tabs.guide;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.tripkorea.on.ontripkorea.tabs.toon.ToonDetailImageActivity;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.toonguide.Toon;
import com.tripkorea.on.ontripkorea.vo.toonguide.ToonImageEntity;
import com.tripkorea.on.ontripkorea.vo.voiceguide.Guide;
import com.tripkorea.on.ontripkorea.vo.voiceguide.VoiceGuideImageEntity;
import com.tripkorea.on.ontripkorea.vo.voiceguide.VoiceGuideLog;
import com.tripkorea.on.ontripkorea.vo.voiceguide.VoiceGuideLogList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Edward Won on 2018-07-30.
 */

public class GuideActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnMarkerClickListener {

    MapView guideMap;
    TextView title;
    ImageView guideToonBtn;
    ImageView guideVoiceBtn;
    CardView guideCardView;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    static final String CONNECTION_CONFIRM_CLIENT_URL = "http://clients3.google.com/generate_204";
    String usinglanguage;
    Locale locale;

    int guideIdx;

    private List<Toon> toons = new ArrayList<>();

    //map
//    private static VoiceGuide guideEntity;
    static ArrayList<Guide> guideEntities = new ArrayList<>();

    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    Location currentLocation;
    double currentLat, currentLong;
    double latitude, longitude;
    ArrayList<Marker> markers = new ArrayList<>();

    //record
    private MediaPlayer mp;
    private Runnable mUpdateTime;
    private boolean settingVoiceGuide;
    private static String voiceAddress;
    //    private static VoiceGuideLocation locEntity;
    private Guide buildingEntity;

    //log entity
    private VoiceGuideLogList logList;
    public static ArrayList<VoiceGuideLog> logs = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        guideMap = findViewById(R.id.guide_map);


        guideMap.onCreate(savedInstanceState);
        guideMap.getMapAsync(this);

        guideMap.onStart();


        title = findViewById(R.id.sub_guide_title);
        guideToonBtn = findViewById(R.id.sub_guide_toon);
        guideVoiceBtn = findViewById(R.id.sub_guide_voice);
        guideCardView = findViewById(R.id.guide_card_view);

        guideIdx = getIntent().getIntExtra("guideIdx", 1024);

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
                language = 0;
                break;
            case "日本言":
                language = 0;
                break;
            default:
                language = 0;
                break;
        }

        getVoiceGuide(language);
        getCartoon(language);

        logList = new VoiceGuideLogList();
        logList.setLogArrayList(logs);

    }

    private void getVoiceGuide(final int language){
        ApiClient.getInstance().getApiService()
                .getGuide(MyApplication.APP_VERSION, 1024,language)
                .enqueue(new Callback<ArrayList<Guide>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Guide>> call, Response<ArrayList<Guide>> response) {
                        if (response.body() != null) {
                            guideEntities = response.body();
                            //first screen setting
                            buildingEntity = guideEntities.get(0);
                            Log.e("탭선택 전","0번째: "+guideEntities.get(0).getTitle());
                            checkMarker();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Guide>> call, Throwable t) {
                        Alert.makeText(getResources().getString(R.string.network_error));
                    }
                });
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
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Toon>> call, Throwable t) {
                        Alert.makeText(getResources().getString(R.string.network_error));
                    }
                });
    }

    private void checkGuideLayout(final String markerTitle){
        new LogManager().LogManager("GuideActivity","checkGuideLayout: "+markerTitle);
        guideCardView.setVisibility(View.VISIBLE);
        boolean checkToon = false;
        boolean checkVoice = false;
        for(int i=0; i< guideEntities.size(); i++){
            if(markerTitle.equals(guideEntities.get(i).getTitle())){
                new LogManager().LogManager("GuideActivity Voice",markerTitle+" : "+guideEntities.get(i).getTitle());
                final Guide detailGuide = guideEntities.get(i);
                guideVoiceBtn.setVisibility(View.VISIBLE);
                checkVoice = true;
                final VoiceGuideImageEntity imgListEntity = new VoiceGuideImageEntity();
                for(int j=0; j<detailGuide.getGuideImageList().size(); j++){
                    imgListEntity.getVoiceGuideList().add(detailGuide.getGuideImageList().get(j));
                }

                guideVoiceBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent voiceGuideIntent = new Intent(GuideActivity.this, GuideDetailActivity.class);
                        voiceGuideIntent.putExtra("voiceGuide",detailGuide);
                        voiceGuideIntent.putExtra("voiceGuideIMG",imgListEntity);
                        startActivity(voiceGuideIntent);
                    }
                });
            }
        }

        for(int i=0; i< toons.size(); i++){
            if(markerTitle.equals(toons.get(i).getTitle())){
                new LogManager().LogManager("GuideActivity Toon",markerTitle+" : "+toons.get(i).getTitle());
                guideToonBtn.setVisibility(View.VISIBLE);
                checkToon = true;
                guideToonBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toon tempToon = new Toon();
                        for(int i=0; i<toons.size(); i++){
                            if(markerTitle.equals(toons.get(i).getTitle())){
                                tempToon = toons.get(i);
                            }
                        }
                        new LogManager().LogManager("ToonFragment","인텐트 보내기 실행 전-tempToonSize: "+tempToon.getTitle()+"|");
                        new LogManager().LogManager("ToonFragment","인텐트 보내기 실행 전-tempToonSize: "+tempToon.getCartoonImageAddressList().size()+"|");
                        ToonImageEntity toonImageEntity = new ToonImageEntity();
                        toonImageEntity.setToonList(tempToon.getCartoonImageAddressList());
                        Intent showToonDetail = new Intent(GuideActivity.this, ToonDetailImageActivity.class);
                        showToonDetail.putExtra("toons",toonImageEntity);
                        startActivity(showToonDetail);
                    }
                });
            }
        }

        if(!checkVoice) guideVoiceBtn.setVisibility(View.GONE);
        if(!checkToon) guideToonBtn.setVisibility(View.GONE);
        title.setText(markerTitle);


    }

    private void checkMarker(){
        Log.e("GuideFragment","콘텐츠 몇 개? "+guideEntities.size());
        for(int i=1; i< guideEntities.size(); i++) {
            /*double tempx =(Double.parseDouble(guideEntities.get(i).getGuideWest())
                    +Double.parseDouble(guideEntities.get(i).getGuideEast()))/2;
            double tempy =(Double.parseDouble(guideEntities.get(i).getGuideNorth())
                    +Double.parseDouble(guideEntities.get(i).getGuideSouth()))/2;*/
            LatLng location =  new LatLng(guideEntities.get(i).getLat(), guideEntities.get(i).getLon());
            Marker tempMarker = mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(guideEntities.get(i).getTitle())
                    .alpha(0.5f)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
            );//.icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_bg))
            markers.add(tempMarker);
        }
    }

    private void checkLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
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
            }else{
                ActivityCompat.requestPermissions(this,
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

    protected synchronized void buildGoogleApiClient() {
//        new LogManager().printLogManager("빌드에이피아이","비이이이이이틀");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        if(longitude <1){
            longitude = 126.989297;
            latitude = 37.577395;
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        mMap.setOnMarkerClickListener(this);


    }



    @Override
    public boolean onMarkerClick(Marker marker) {
        new LogManager().LogManager("GuideActivity","onMarkerClick - marker.getTitle(): "+marker.getTitle());
        checkGuideLayout(marker.getTitle());
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ApiClient.getInstance().getApiService()
                .voiceGuideLog(MyApplication.APP_VERSION, logs)
                .enqueue(new Callback<ApiMessasge>(){
                    @Override
                    public void onResponse(Call<ApiMessasge> call, Response<ApiMessasge> response) {
                        if (response.body() == null) {
                            Alert.makeText("보이스 로그 전송 에러 발생" + response.errorBody().toString());
                            try {
                                Log.e("Voice log", "error : " + response.errorBody().string());
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
}

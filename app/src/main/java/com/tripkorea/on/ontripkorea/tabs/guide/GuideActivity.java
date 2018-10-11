package com.tripkorea.on.ontripkorea.tabs.guide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
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
import com.tripkorea.on.ontripkorea.retrofit.message.ApiMessage;
import com.tripkorea.on.ontripkorea.tabs.toon.ToonDetailImageActivity;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.toonguide.Toon;
import com.tripkorea.on.ontripkorea.vo.toonguide.ToonImageEntity;
import com.tripkorea.on.ontripkorea.vo.voiceguide.Guide;
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
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener{

    MapView guideMap;
    TextView title;
    ImageView guideToonBtn;
    ImageView guideVoiceBtn;
    CardView guideCardView;
    ImageView guideInfoImg;
    CheckBox chkToonInfo;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    static final String CONNECTION_CONFIRM_CLIENT_URL = "http://clients3.google.com/generate_204";
    String usinglanguage;
    Locale locale;

    int attrIdx;
    int guideIdx = 0;

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
    private Guide buildingEntity;
    int language;
    LocationRequest locationRequest;

    //log entity
    private VoiceGuideLogList logList;
    public static ArrayList<VoiceGuideLog> logs = new ArrayList<>();
    boolean intentToToonimg;

    private LocationManager locationManager;

    SharedPreferences setting;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        guideMap = findViewById(R.id.guide_map);


        guideMap.onCreate(savedInstanceState);
        guideMap.getMapAsync(this);

        guideMap.onStart();

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


        title = findViewById(R.id.sub_guide_title);
        guideToonBtn = findViewById(R.id.sub_guide_toon);
        guideVoiceBtn = findViewById(R.id.sub_guide_voice);
        guideCardView = findViewById(R.id.guide_card_view);

        attrIdx = getIntent().getIntExtra("attrIdx", 8344);

        //사용자 언어 확인
        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }

        usinglanguage = locale.getDisplayLanguage();
        guideInfoImg = findViewById(R.id.guide_info_img);
        chkToonInfo = findViewById(R.id.chk_toon_info);

        language = 0;
        switch (usinglanguage){
            case "한국어":
                language = 1;
                guideInfoImg.setImageResource(R.drawable.guide_information_kr);
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

//        getVoiceGuide(language);
        getCartoon(language);

        logList = new VoiceGuideLogList();
        logList.setLogArrayList(logs);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(10000);



        setting = getSharedPreferences("information_checked", 0);
        boolean informationChecked = setting.getBoolean("information_checked", false);
        if(informationChecked){
            guideInfoImg.setVisibility(View.GONE);
            chkToonInfo.setVisibility(View.GONE);
        }else {
            chkToonInfo.setOnCheckedChangeListener(infoChecked);
        }


    }

    CompoundButton.OnCheckedChangeListener infoChecked = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            editor= setting.edit();

            if(isChecked){
                guideInfoImg.setVisibility(View.GONE);
                chkToonInfo.setVisibility(View.GONE);
            }
            editor.putBoolean("information_checked",isChecked);

            editor.apply();
        }
    };

    private void getCartoon(final int language){
        ApiClient.getInstance().getApiService()
                .getCartoon(MyApplication.APP_VERSION, 8344,language)
                .enqueue(new Callback<List<Toon>>() {
                    @Override
                    public void onResponse(Call<List<Toon>> call, Response<List<Toon>> response) {
                        if(response.body() != null){
                            toons = response.body();
                            for(int i=1 ; i<toons.size(); i++) {
                                addToonMarker(toons.get(i));
                            }
                            strengthenToonMarker(toons.get(1));
                            int checked = toons.get(1).getChecked();
                            checked++;
                            toons.get(1).setChecked(checked);
                            new LogManager().LogManager("toonFragment","toons.size(): "+toons.size()+" | ");
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Toon>> call, Throwable t) {
                        Alert.makeText(getResources().getString(R.string.network_error));
                    }
                });
    }



    private void addToonMarker(Toon currentToon){
        new LogManager().LogManager("가이드엑",currentToon.getTitle()+" | 마커 더하기");
        LatLng location =  new LatLng(currentToon.getLat(), currentToon.getLon());
        Bitmap toonMarker = ((BitmapDrawable)getResources().getDrawable(R.drawable.btn_guide_toon)).getBitmap();
        int resizeWidth = 200;
        double aspectRatio = (double) toonMarker.getHeight() / (double) toonMarker.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);
        toonMarker = Bitmap.createScaledBitmap(toonMarker, resizeWidth, targetHeight, false);
        Marker tempMarker = mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(currentToon.getTitle())
                .alpha(0.3f)
                .icon(BitmapDescriptorFactory.fromBitmap(toonMarker))
        );
        markers.add(tempMarker);
    }

    private void strengthenToonMarker(Toon currentToon){
        new LogManager().LogManager("가이드엑",currentToon.getTitle()+" | 마커 더하기");
        LatLng location =  new LatLng(currentToon.getLat(), currentToon.getLon());
        Bitmap toonMarker = ((BitmapDrawable)getResources().getDrawable(R.drawable.btn_guide_toon)).getBitmap();
        int resizeWidth = 200;
        double aspectRatio = (double) toonMarker.getHeight() / (double) toonMarker.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);
        toonMarker = Bitmap.createScaledBitmap(toonMarker, resizeWidth, targetHeight, false);
        Marker tempMarker = mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(currentToon.getTitle())
                .icon(BitmapDescriptorFactory.fromBitmap(toonMarker))
        );
        markers.add(tempMarker);
    }

    private void checkLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                new LogManager().LogManager("가이드엑","checkLocationPermission() 진입");
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        return false;
                    }
                });
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, new android.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        double currentLat = location.getLatitude();
                        double currentLon = location.getLongitude();
                        new LogManager().LogManager("가이드엑","현재 위치 확인 - 위도: "+currentLat+" | 경도: "+currentLon);
                        for(int i=2;i<toons.size(); i++){
                            if(currentLat <= toons.get(i).getNorth()
                                    && currentLat >= toons.get(i).getSouth()
                                    && currentLon <= toons.get(i).getEast()
                                    && currentLon >= toons.get(i).getWest()){
                                if(toons.get(i).getChecked() < 1) {
                                    strengthenToonMarker(toons.get(i));
                                    int checked = toons.get(i).getChecked();
                                    checked++;
                                    toons.get(i).setChecked(checked);
                                }
                            }
                        }
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double currentLat = location.getLatitude();
                    double currentLon = location.getLongitude();
                    new LogManager().LogManager("가이드엑","현재 위치 확인 - 위도: "+currentLat+" | 경도: "+currentLon);
                    for(int i=2;i<toons.size(); i++){
                        if(currentLat <= toons.get(i).getNorth()
                                && currentLat >= toons.get(i).getSouth()
                                && currentLon <= toons.get(i).getEast()
                                && currentLon >= toons.get(i).getWest()){
                            if(toons.get(i).getChecked() < 1) {
                                strengthenToonMarker(toons.get(i));
                                int checked = toons.get(i).getChecked();
                                checked++;
                                toons.get(i).setChecked(checked);
                            }
                        }
                    }
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        if(longitude <1){
            longitude = 126.989297;
            latitude = 37.577395;
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        checkLocationPermission();
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        new LogManager().LogManager("GuideActivity","onMarkerClick - marker.getTitle(): "+marker.getTitle());
//        checkGuideLayout(marker.getTitle());
        Toon tempToon = new Toon();
        for(int i=0; i<toons.size(); i++){
            if(marker.getTitle().equals(toons.get(i).getTitle())){
                tempToon = toons.get(i);
            }
        }
        guideIdx = tempToon.getGuideIdx();
        if(tempToon.getChecked() > 0) {
            clickToonMarker(guideIdx);
        }else{
            Alert.makeText("Not yet visited. Visit and try again.");
        }

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toon tempToon = null;
        for(int i=0; i<toons.size(); i++){
            if(marker.getTitle().equals(toons.get(i).getTitle()) && toons.get(i).getChecked() > 0){
                tempToon = toons.get(i);
                new LogManager().LogManager("ToonFragment","인텐트 보내기 실행 전-tempToonSize: "+tempToon.getTitle()+"|");
                new LogManager().LogManager("ToonFragment","인텐트 보내기 실행 전-tempToonSize: "+tempToon.getCartoonImageAddressList().size()+"|");
                ToonImageEntity toonImageEntity = new ToonImageEntity();
                toonImageEntity.setToonList(tempToon.getCartoonImageAddressList());
                Intent showToonDetail = new Intent(GuideActivity.this, ToonDetailImageActivity.class);
                showToonDetail.putExtra("guideIdx",tempToon.getGuideIdx());
                showToonDetail.putExtra("toons",toonImageEntity);
                startActivity(showToonDetail);
                intentToToonimg = true;
            }
        }
        if(tempToon == null){
            Alert.makeText("Not yet visited. Visit and try again.");
        }

    }

    private void clickToonMarker(int guideIdx){
        ApiClient.getInstance().getApiService()
                .touchToonMarker(MyApplication.APP_VERSION,guideIdx,language)
                .enqueue(new Callback<ApiMessage>() {
                    @Override
                    public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                        if (response.body() != null) {
                            ApiMessage apiMessage = response.body();
                            new LogManager().LogManager("가이드엑티비티","clickToonMarker  getMessage(): "+apiMessage.getMessage());

                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    new LogManager().LogManager("가이드엑티비티", "clickToonMarker: error  " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiMessage> call, Throwable t) {
                        new LogManager().LogManager("가이드엑티비티", "clickToonMarker: onFailure  " +t.getMessage());
                        Alert.makeText(getString(R.string.network_error));
                    }
                });
    }



    @Override
    protected void onResume() {
        super.onResume();
        intentToToonimg = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!intentToToonimg) {
            ApiClient.getInstance().getApiService()
                    .exitToon(MyApplication.APP_VERSION, guideIdx, language)
                    .enqueue(new Callback<ApiMessage>() {
                        @Override
                        public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                            if (response.body() != null) {
                                ApiMessage apiMessage = response.body();
                                new LogManager().LogManager("가이드엑", "onPause() exitToon getMessage(): " + apiMessage.getMessage());

                            } else {
                                if (response.errorBody() != null) {
                                    try {
                                        new LogManager().LogManager("가이드엑", "onPause() exitToon  error  " + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiMessage> call, Throwable t) {
                            new LogManager().LogManager("가이드엑", "onPause() exitToon onFailure  " + t.getMessage());
                            Alert.makeText(getString(R.string.network_error));
                        }
                    });
        }


    }
}

/*private void getVoiceGuide(final int language){
        ApiClient.getInstance().getApiService()
                .getGuide(MyApplication.APP_VERSION, 8344,language)
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
    }*/

    /*private void checkGuideLayout(final String markerTitle){
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


    }*/

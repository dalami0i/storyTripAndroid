package com.tripkorea.on.ontripkorea.tabs.guide;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
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
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.util.NetworkUtil;
import com.tripkorea.on.ontripkorea.util.WifiCheck;
import com.tripkorea.on.ontripkorea.vo.user.Me;
import com.tripkorea.on.ontripkorea.vo.voiceguide.Guide;
import com.tripkorea.on.ontripkorea.vo.voiceguide.GuideImage;
import com.tripkorea.on.ontripkorea.vo.voiceguide.VoiceGuideImageEntity;
import com.tripkorea.on.ontripkorea.vo.voiceguide.VoiceGuideLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

///**
// * Created by Edward Won on 2018-09-05.
// */

public class VoiceGuideActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener{

    @BindView(R.id.iv_voice_bg)
    ImageView ivMainBgImg;
    @BindView(R.id.tv_voice_title)
    TextView tvMainTitle;
    @BindView(R.id.map_voiceguide)
    MapView voiceGuideMap;

    @BindView(R.id.layout_bottomsheet_voice)
    LinearLayout bottomsheet;
    @BindView(R.id.thistime)
    TextView thisTime;
    @BindView(R.id.thistitle)
    TextView title;
    @BindView(R.id.totaltime)
    TextView totalTime;
    @BindView(R.id.palybtn)
    ImageView play;
    @BindView(R.id.pausebtn)
    ImageView pause;
    @BindView(R.id.seekbar)
    SeekBar seekbar;

    @BindView(R.id.voice_img_gridview)
    GridView voiceGridView;

    BottomSheetBehavior sheetBehavior;

    private ArrayList<Guide> guideEntities = new ArrayList<>();
    private Guide buildingEntity;
    ArrayList<VoiceGuideImageEntity> imageEntities = new ArrayList<>();
    ArrayList<Marker> markers = new ArrayList<>();

    private GoogleMap mMap;
    double latitude, longitude;

    private MediaPlayer mp;
    private String voiceAddress;
    static final String CONNECTION_CONFIRM_CLIENT_URL = "http://clients3.google.com/generate_204";
    private boolean settingVoiceGuide;
    private Runnable mUpdateTime;
    int guideIdx;

    int language = 0;

    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    SharedPreferences setting;
    SharedPreferences.Editor editor;
    ImageView guideInfoImg;
    CheckBox chkToonInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_guide);
        ButterKnife.bind(this);

        voiceGuideMap.onCreate(savedInstanceState);
        voiceGuideMap.getMapAsync(this);
        voiceGuideMap.onStart();

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        int attrIdx = getIntent().getIntExtra("attrIdx",8344);

        Locale locale;
        //사용자 언어 확인
        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }

        String usinglanguage = locale.getDisplayLanguage();

        guideInfoImg = findViewById(R.id.guide_info_img);
        chkToonInfo = findViewById(R.id.chk_voice_info);

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


        sheetBehavior = BottomSheetBehavior.from(bottomsheet);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAudio();
            }
        });


        setting = getSharedPreferences("information_checked", 0);
        boolean informationChecked = setting.getBoolean("information_checked", false);
        new LogManager().LogManager("보이스가이드엑","informationChecked: "+informationChecked);
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

    /**
     * manually opening / closing bottom sheet on button click
     */
//    @OnClick(R.id.btn_bottom_sheet)
//    public void toggleBottomSheet() {
//        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
//            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            btnBottomSheet.setText("Close sheet");
//        } else {
//            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            btnBottomSheet.setText("Expand sheet");
//        }
//    }

//    }

    private String getVoiceLength(double length){
        String voiceLength;
        String sec = String.valueOf((int)(length%60));
        if(sec.length() <2){
            sec = "0"+sec;
        }
        String min = String.valueOf((int)(length/60));
        voiceLength = min+":"+sec;
        return voiceLength;
    }

    private int returnVoiceLength(String time){
        String[] parts = time.split(":");
        if(parts[0] != null) {
            int firstPart = Integer.parseInt(parts[0]) * 60;
            int secondPart = Integer.parseInt(parts[0]);
            return firstPart+secondPart;
        }
        return 1;

    }


    private void setVoiceEmptyGuide(){
        thisTime.setVisibility(View.INVISIBLE);
        totalTime.setVisibility(View.INVISIBLE);
        play.setVisibility(View.INVISIBLE);
        seekbar.setVisibility(View.INVISIBLE);
    }



    public void startAudio() {
        int playBtn;
        String playTag = "btn_audio_start";
        playBtn = getResources().getIdentifier(playTag, "drawable", MyApplication.getContext().getPackageName());
        int pauseBtn;
        String pauseTag = "btn_audio_stop";
        pauseBtn = getResources().getIdentifier(pauseTag, "drawable", MyApplication.getContext().getPackageName());

        if (mp == null) {
            if(voiceAddress.length() < 40) {
                voiceAddress = "http://13.125.83.183:8080/resources/guides/voice/" + voiceAddress;
            }
            Log.e("보이스가이드","start: "+voiceAddress);
            if (NetworkUtil.isNetworkConnected(MyApplication.getContext())) {
                WifiCheck.CheckConnect cc = new WifiCheck.CheckConnect(CONNECTION_CONFIRM_CLIENT_URL);
                try {
                    cc.start();
                    if (WifiCheck.wificheck == WifiCheck.WIFI_ON ) {
                        mp = new MediaPlayer();
                        try {
                            mp.setDataSource(voiceAddress);
                            mp.prepare();
                            seekbar.setVisibility(View.VISIBLE);
                            seekbar.setOnSeekBarChangeListener(mOnSeek);
                            mProgressHandler.sendEmptyMessageDelayed(0, 200);
                            seekbar.setMax(mp.getDuration());
                            totalTime.setText(milliSecondsToTimer(mp.getDuration()));
                            settingVoiceGuide = true;


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            mUpdateTime = new Runnable() {
                                public void run() {
                                    int currentDuration;
//                                while(settingVoiceGuide) {
                                    if (settingVoiceGuide  && mp != null && mp.isPlaying()) {
                                        currentDuration = mp.getCurrentPosition();
                                        updatePlayer(currentDuration);
                                        thisTime.postDelayed(this, 1000);
                                    } else {
                                        thisTime.removeCallbacks(this);
                                    }
//                                }
                                }
                            };
                            thisTime.post(mUpdateTime);
                            play.setImageResource(pauseBtn);
                            mp.start();
                            sendPlayLog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MyApplication.getContext(), R.string.securedWifi, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MyApplication.getContext(), R.string.waitWifi, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MyApplication.getContext(), R.string.requireInternet, Toast.LENGTH_LONG).show();
            }

        } else if ( mp.isPlaying()) {
            Log.e("보이스가이드","stop: "+voiceAddress);
//            pause.setVisibility(View.VISIBLE);
            play.setImageResource(playBtn);
            mp.pause();
            sendStopLog();
        }else {
            Log.e("보이스가이드","else start: "+voiceAddress);
            if (NetworkUtil.isNetworkConnected(MyApplication.getContext())) {
                WifiCheck.CheckConnect cc = new WifiCheck.CheckConnect(CONNECTION_CONFIRM_CLIENT_URL);
                try {
                    cc.start();
                    if (WifiCheck.wificheck == WifiCheck.WIFI_ON) {
                        try {
                            settingVoiceGuide = true;
                            thisTime.post(mUpdateTime);
                            play.setImageResource(pauseBtn);
                            mp.start();
                            sendPlayLog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MyApplication.getContext(), R.string.securedWifi, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MyApplication.getContext(), R.string.waitWifi, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MyApplication.getContext(), R.string.requireInternet, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendPlayLog(){
        ApiClient.getInstance().getApiService()
                .playVoice(MyApplication.APP_VERSION,guideIdx,language)
                .enqueue(new Callback<ApiMessage>() {
                    @Override
                    public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                        if (response.body() != null) {
                            ApiMessage apiMessage = response.body();
                            new LogManager().LogManager("보이스가이드엑티비티","sendPlayLog().getMessage(): "+apiMessage.getMessage());

                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    new LogManager().LogManager("보이스가이드엑티비티", "sendPlayLog(): error  " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiMessage> call, Throwable t) {
                        new LogManager().LogManager("보이스가이드엑티비티", "sendPlayLog(): onFailure  " +t.getMessage());
                        Alert.makeText(getString(R.string.network_error));
                    }
                });
    }

    private void sendStopLog(){
        ApiClient.getInstance().getApiService()
                .stopVoice(MyApplication.APP_VERSION,guideIdx,language)
                .enqueue(new Callback<ApiMessage>() {
                    @Override
                    public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                        if (response.body() != null) {
                            ApiMessage apiMessage = response.body();
                            new LogManager().LogManager("보이스가이드엑티비티","sendStopLog().getMessage(): "+apiMessage.getMessage());

                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    new LogManager().LogManager("보이스가이드엑티비티", "sendStopLog(): error  " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiMessage> call, Throwable t) {
                        new LogManager().LogManager("보이스가이드엑티비티", "sendStopLog(): onFailure  " +t.getMessage());
                        Alert.makeText(getString(R.string.network_error));
                    }
                });
    }

    SeekBar.OnSeekBarChangeListener mOnSeek = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mp.seekTo(progress);
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            boolean wasPlaying = mp.isPlaying();
            if (wasPlaying) {
                mp.pause();
                sendStopLog();
            }
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (mp != null) {
                int playBtn;
                String playTag = "btn_audio_start";
                playBtn = getResources().getIdentifier(playTag, "drawable", MyApplication.getContext().getPackageName());
                play.setImageResource(playBtn);
            }
        }
    };

    Handler mProgressHandler = new Handler() {

        public void handleMessage(Message msg) {
            if (mp == null)
                return;
            else if (mp.isPlaying()) {
                seekbar.setProgress(mp.getCurrentPosition());
            }
            mProgressHandler.sendEmptyMessageDelayed(0, 100);
        }
    };

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString;

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    private void updatePlayer(int currentDuration) {
        String time = "" + milliSecondsToTimer((long) currentDuration);
        thisTime.setText(time);
    }

    public void stopAudio() {
        stopMP();
    }

    public void stopMP(){
        int playBtn;
        String playTag = "btn_audio_start";
        playBtn = getResources().getIdentifier(playTag, "drawable", MyApplication.getContext().getPackageName());

        Log.e("보이스가이드","stop: "+voiceAddress);
        if (mp != null && mp.isPlaying()) {
            Log.e("보이스가이드","stop: "+voiceAddress);
//            pause.setVisibility(View.VISIBLE);
            play.setImageResource(playBtn);
            mp.pause();
            mp.stop();
            sendStopLog();
        }

        if (mp != null) {
            pause.setVisibility(View.INVISIBLE);
            play.setImageResource(playBtn);
            totalTime.setText("");
            thisTime.setText("");
            title.setText("");
            mp.stop();
            sendStopLog();
            try {
                mp.prepare();
            } catch (Exception e) {
                Log.d("", "");
            }
            seekbar.setProgress(0);
            mp.seekTo(0);
            mp.release();
        }
        mp = null;
//        seekbar.setVisibility(View.INVISIBLE);
    }

    public void tempStop(){
        int playBtn;
        String playTag = "btn_audio_start";

        Log.e("보이스가이드","tempStop: "+voiceAddress);

        if (mp != null) {
            pause.setVisibility(View.INVISIBLE);
            playBtn = getResources().getIdentifier(playTag, "drawable", MyApplication.getContext().getPackageName());
            play.setImageResource(playBtn);
            if(mp.isPlaying()) {
                mp.pause();
                sendStopLog();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        tempStop();
        VoiceGuideLog log = new VoiceGuideLog();
//        log.setGuideIdx(detailGuide.getIdx());
        log.setUserIdx(Me.getInstance().getIdx());
//        log.setTotalLength(detailGuide.getLength());
        log.setListenTimeLength(returnVoiceLength(String.valueOf(thisTime.getText())));
        GuideActivity.logs.add(log);

        ApiClient.getInstance().getApiService()
                .exitVoice(MyApplication.APP_VERSION,guideIdx,language)
                .enqueue(new Callback<ApiMessage>() {
                    @Override
                    public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                        if (response.body() != null) {
                            ApiMessage apiMessage = response.body();
                            new LogManager().LogManager("보이스가이드엑티비티 나가기","onPause().getMessage(): "+apiMessage.getMessage());

                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    new LogManager().LogManager("보이스가이드엑티비티 나가기", "onPause(): error  " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiMessage> call, Throwable t) {
                        new LogManager().LogManager("보이스가이드엑티비티 나가기", "onPause(): onFailure  " +t.getMessage());
                        Alert.makeText(getString(R.string.network_error));
                    }
                });
    }



    @Override
    public boolean onMarkerClick(Marker marker) {
        for(int i=0; i<guideEntities.size(); i++){
            if(marker.getTitle().equals(guideEntities.get(i).getTitle()) && guideEntities.get(i).getChecked() > 0){
                bottomsheetViewChange(guideEntities.get(i), i);
                guideIdx = guideEntities.get(i).getGuideIdx();
                new LogManager().LogManager("보이스가이드엑티비티","onMarkerClick guideIdx: "+guideIdx);
                ApiClient.getInstance().getApiService()
                        .touchVoiceMarker(MyApplication.APP_VERSION,guideIdx,language)
                        .enqueue(new Callback<ApiMessage>() {
                            @Override
                            public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                                if (response.body() != null) {
                                    ApiMessage apiMessage = response.body();
                                    new LogManager().LogManager("보이스가이드엑티비티","onMarkerClick.getMessage(): "+apiMessage.getMessage());

                                } else {
                                    if (response.errorBody() != null) {
                                        try {
                                            new LogManager().LogManager("보이스가이드엑티비티", "onMarkerClick: error  " + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiMessage> call, Throwable t) {
                                new LogManager().LogManager("보이스가이드엑티비티", "onMarkerClick: onFailure  " +t.getMessage());
                                Alert.makeText(getString(R.string.network_error));
                            }
                        });
                return false;
            }else if(marker.getTitle().equals(guideEntities.get(i).getTitle()) && guideEntities.get(i).getChecked() == 0){
                Alert.makeText("Not yet visited. Visit and try again.");
            }
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        if(longitude <1){
            longitude = 126.989297;
            latitude = 37.577395;
        }
        mMap.setOnMarkerClickListener(this);

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

                            VoiceGuideImageEntity imgListEntity = new VoiceGuideImageEntity();
                            ArrayList<GuideImage> images = new ArrayList<>();
                            imgListEntity.setVoiceGuideList(images);
                            for(int i=1; i< guideEntities.size(); i++) {
                                addVoiceMarker(guideEntities.get(i));
                            }
                            strengthenVoiceMarker(guideEntities.get(1));
                            int checked = guideEntities.get(1).getChecked();
                            checked++;
                            guideEntities.get(1).setChecked(checked);
                            tvMainTitle.setText(buildingEntity.getTitle());

                            mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                            if(longitude <1){
                                longitude = 126.989297;
                                latitude = 37.577395;
                            }

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(guideEntities.get(1).getLat(), guideEntities.get(1).getLon())));
                            mMap.setOnMarkerClickListener(VoiceGuideActivity.this);
                            checkLocationPermission();

                            bottomsheetViewChange(buildingEntity, 0);



                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Guide>> call, Throwable t) {
                        Alert.makeText(getResources().getString(R.string.network_error));
                    }
                });


    }

    private void addVoiceMarker(Guide currentVoice){
        new LogManager().LogManager("가이드엑",currentVoice.getTitle()+" | 마커 더하기");
        LatLng location =  new LatLng(currentVoice.getLat(), currentVoice.getLon());
        Bitmap voiceMarker = ((BitmapDrawable)getResources().getDrawable(R.drawable.btn_guide_voice)).getBitmap();
        int resizeWidth = 200;
        double aspectRatio = (double) voiceMarker.getHeight() / (double) voiceMarker.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);
        voiceMarker = Bitmap.createScaledBitmap(voiceMarker, resizeWidth, targetHeight, false);
        Marker tempMarker = mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(currentVoice.getTitle())
                .alpha(0.5f)
                .icon(BitmapDescriptorFactory.fromBitmap(voiceMarker))
        );
        markers.add(tempMarker);

    }

    private void strengthenVoiceMarker(Guide currentVoice){
        new LogManager().LogManager("보이스가이드엑",currentVoice.getTitle()+" | 마커 더하기");
        LatLng location =  new LatLng(currentVoice.getLat(), currentVoice.getLon());
        Bitmap toonMarker = ((BitmapDrawable)getResources().getDrawable(R.drawable.btn_guide_voice)).getBitmap();
        int resizeWidth = 200;
        double aspectRatio = (double) toonMarker.getHeight() / (double) toonMarker.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);
        toonMarker = Bitmap.createScaledBitmap(toonMarker, resizeWidth, targetHeight, false);
        Marker tempMarker = mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(currentVoice.getTitle())
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
                        for(int i=2;i<guideEntities.size(); i++){
                            if(currentLat <= guideEntities.get(i).getNorth()
                                    && currentLat >= guideEntities.get(i).getSouth()
                                    && currentLon <= guideEntities.get(i).getEast()
                                    && currentLon >= guideEntities.get(i).getWest()){
                                if(guideEntities.get(i).getChecked() < 1) {

                                    strengthenVoiceMarker(guideEntities.get(i));
                                    int checked = guideEntities.get(i).getChecked();
                                    checked++;
                                    guideEntities.get(i).setChecked(checked);
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
                    for(int i=2;i<guideEntities.size(); i++){
                        if(currentLat <= guideEntities.get(i).getNorth()
                                && currentLat >= guideEntities.get(i).getSouth()
                                && currentLon <= guideEntities.get(i).getEast()
                                && currentLon >= guideEntities.get(i).getWest()){
                            if(guideEntities.get(i).getChecked() < 1) {
                                strengthenVoiceMarker(guideEntities.get(i));
                                int checked = guideEntities.get(i).getChecked();
                                checked++;
                                guideEntities.get(i).setChecked(checked);
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

    private void bottomsheetViewChange(Guide guide, int order){
        new LogManager().LogManager("bottomsheetViewChange","order: "+order);
        if(mp != null) {
            tempStop();
            stopAudio();
        }
        title.setText(guide.getTitle());
        totalTime.setText(getVoiceLength(guide.getLength()));
        thisTime.setText("0:00");
        voiceAddress = guide.getVoiceAddress();
        VoiceImageGridAdapter gridAdapter
                = new VoiceImageGridAdapter(VoiceGuideActivity.this,
                R.layout.item_voice_guide_grid,
                guideEntities.get(order)
        );

        voiceGridView.setAdapter(gridAdapter);


    }

    protected synchronized void buildGoogleApiClient() {
//        new LogManager().printLogManager("빌드에이피아이","비이이이이이틀");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

    }

}

//        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                switch (newState) {
//                    case BottomSheetBehavior.STATE_HIDDEN:
//                        break;
//                    case BottomSheetBehavior.STATE_EXPANDED: {
//                    }
//                    break;
//                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                    }
//                    break;
//                    case BottomSheetBehavior.STATE_DRAGGING:
//                        break;
//                    case BottomSheetBehavior.STATE_SETTLING:
//                        break;
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });
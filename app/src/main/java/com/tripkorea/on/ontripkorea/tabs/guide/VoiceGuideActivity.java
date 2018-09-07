package com.tripkorea.on.ontripkorea.tabs.guide;

import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
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

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Edward Won on 2018-09-05.
 */

public class VoiceGuideActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        LocationListener,
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

    private GoogleMap mMap;
    double latitude, longitude;

    private MediaPlayer mp;
    private String voiceAddress;
    static final String CONNECTION_CONFIRM_CLIENT_URL = "http://clients3.google.com/generate_204";
    private boolean settingVoiceGuide;
    private Runnable mUpdateTime;

    int language = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_guide);
        ButterKnife.bind(this);

        voiceGuideMap.onCreate(savedInstanceState);
        voiceGuideMap.getMapAsync(this);
        voiceGuideMap.onStart();

        int attractionID = getIntent().getIntExtra("guideIdx",8344);

        Locale locale;
        //사용자 언어 확인
        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }

        String usinglanguage = locale.getDisplayLanguage();


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



        sheetBehavior = BottomSheetBehavior.from(bottomsheet);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAudio();
            }
        });
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
    }

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
        int firstPart = Integer.parseInt(parts[0])*60;
        int secondPart = Integer.parseInt(parts[0]);

        return firstPart+secondPart;
    }


    private void setVoiceEmptyGuide(){
        thisTime.setVisibility(View.INVISIBLE);
        totalTime.setVisibility(View.INVISIBLE);
        play.setVisibility(View.INVISIBLE);
        seekbar.setVisibility(View.INVISIBLE);
    }



    public void startAudio() {
        int playBtn;
        String playTag = "z_play_button";
        playBtn = getResources().getIdentifier(playTag, "drawable", MyApplication.getContext().getPackageName());
        int pauseBtn;
        String pauseTag = "z_pause_button";
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
            }
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (mp != null) {
                int playBtn;
                String playTag = "z_play_button";
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
        String playTag = "z_play_button";
        playBtn = getResources().getIdentifier(playTag, "drawable", MyApplication.getContext().getPackageName());

        Log.e("보이스가이드","stop: "+voiceAddress);
        if (mp != null && mp.isPlaying()) {
            Log.e("보이스가이드","stop: "+voiceAddress);
//            pause.setVisibility(View.VISIBLE);
            play.setImageResource(playBtn);
            mp.pause();
            mp.stop();
        }

        if (mp != null) {
            pause.setVisibility(View.INVISIBLE);
            play.setImageResource(playBtn);
            totalTime.setText("");
            thisTime.setText("");
            title.setText("");
            mp.stop();
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
        String playTag = "z_play_button";

        Log.e("보이스가이드","tempStop: "+voiceAddress);

        if (mp != null) {
            pause.setVisibility(View.INVISIBLE);
            playBtn = getResources().getIdentifier(playTag, "drawable", MyApplication.getContext().getPackageName());
            play.setImageResource(playBtn);
            if(mp.isPlaying()) {
                mp.pause();
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
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for(int i=0; i<guideEntities.size(); i++){
            if(marker.getTitle().equals(guideEntities.get(i).getTitle())){
                bottomsheetViewChange(guideEntities.get(i), i);
                return false;
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

                            for(int i=0; i< guideEntities.size(); i++) {
                                LatLng location =  new LatLng(guideEntities.get(i).getLat(), guideEntities.get(i).getLon());
                                mMap.addMarker(new MarkerOptions()
                                        .position(location)
                                        .title(guideEntities.get(i).getTitle())
                                        .alpha(0.5f)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                                );//.icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_bg))

                            }


                            tvMainTitle.setText(buildingEntity.getTitle());

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(guideEntities.get(1).getLat(), guideEntities.get(1).getLon())));


                            bottomsheetViewChange(buildingEntity, 0);



                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Guide>> call, Throwable t) {
                        Alert.makeText(getResources().getString(R.string.network_error));
                    }
                });


    }

    private void bottomsheetViewChange(Guide guide, int order){
        new LogManager().LogManager("bottomsheetViewChange","order: "+order);
        if(mp != null) {
            tempStop();
            stopAudio();
        }
        title.setText(guide.getTitle());
        totalTime.setText(getVoiceLength(guide.getLength()));
        voiceAddress = guide.getVoiceAddress();
        VoiceImageGridAdapter gridAdapter
                = new VoiceImageGridAdapter(VoiceGuideActivity.this,
                R.layout.item_voice_guide_grid,
                guideEntities.get(order)
        );

        voiceGridView.setAdapter(gridAdapter);


    }
}

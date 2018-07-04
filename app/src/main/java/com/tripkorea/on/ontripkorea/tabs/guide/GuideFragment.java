package com.tripkorea.on.ontripkorea.tabs.guide;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.util.NetworkUtil;
import com.tripkorea.on.ontripkorea.util.OnNetworkErrorListener;
import com.tripkorea.on.ontripkorea.util.WifiCheck;
import com.tripkorea.on.ontripkorea.vo.voiceguide.Guide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by YangHC on 2018-06-05.
 */

public class GuideFragment extends Fragment  implements
        OnMapReadyCallback,
        LocationListener {

    int lastTab;

    MapView guideMap;
    TabLayout guideTabs;
    SeekBar seekbar;
    TextView thisTime;
    TextView title;
    TextView totalTime;
    ImageView pause;
    ImageView play;
    NestedScrollView voide_img_nestedscroll;
    RecyclerView guide_img_rv;


    Context main;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    static final String CONNECTION_CONFIRM_CLIENT_URL = "http://clients3.google.com/generate_204";
    String usinglanguage;
    Locale locale;

    //map
//    private static VoiceGuide guideEntity;
    static List<Guide> guideEntities;


//    GoogleApiClient mGoogleApiClient;
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

    public Fragment guideFragmentNewInstance(GoogleMap mMap, int lastTab){
        this.lastTab = lastTab;
        this.mMap = mMap;
        return new GuideFragment();
    }

    public OnNetworkErrorListener onNetworkErrorListener;

    public void setOnNetworkErrorListener(OnNetworkErrorListener onNetworkErrorListener) {
        this.onNetworkErrorListener = onNetworkErrorListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);

        guideMap = view.findViewById(R.id.guide_map);
        guideTabs = view.findViewById(R.id.guide_tabs);
        seekbar = view.findViewById(R.id.seekbar);
        title = view.findViewById(R.id.thistitle);
        thisTime = view.findViewById(R.id.thistime);
        totalTime = view.findViewById(R.id.totaltime);
        pause = view.findViewById(R.id.pausebtn);
        play = view.findViewById(R.id.palybtn);
        voide_img_nestedscroll = view.findViewById(R.id.voide_img_nestedscroll);
        guide_img_rv = view.findViewById(R.id.guide_img_rv);


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
        ApiClient.getInstance().getApiService()
                .getGuide(MyApplication.APP_VERSION, 1024,language)
                .enqueue(new Callback<List<Guide>>() {

                    @Override
                    public void onResponse(Call<List<Guide>> call, Response<List<Guide>> response) {
                        if (response.body() != null) {
                            guideEntities = response.body();
                            if(guideEntities != null){
                                for (int i = 0; i < guideEntities.size(); i++) {
                                    guideTabs.addTab(guideTabs.newTab().setText(guideEntities.get(i).getTitle()));
                                }
                            }

                            //first screen setting
                            buildingEntity = guideEntities.get(0);
                            Log.e("탭선택 전","0번째: "+guideEntities.get(0).getTitle());

                            title.setText(buildingEntity.getTitle());
                            totalTime.setText(getVoiceLength(buildingEntity.getLength()));
                            voiceAddress = buildingEntity.getGuideUrl();

                            LinearLayoutManager linkLayoutManager
                                    = new LinearLayoutManager(MyApplication.getContext(), LinearLayoutManager.HORIZONTAL, false);
                            SnapHelper snapHelper = new PagerSnapHelper();
                            guide_img_rv.setLayoutManager(linkLayoutManager);
                            snapHelper.attachToRecyclerView(guide_img_rv);
                            VoiceImageRecyclerViewAdapter voiceImageRecyclerViewAdapter
                                    = new VoiceImageRecyclerViewAdapter();
                            for(int i=0;i<buildingEntity.getGuideImageList().size(); i++){
                                voiceImageRecyclerViewAdapter.addVoiceImgList(buildingEntity.getGuideImageList().get(i));
                            }
                            guide_img_rv.setAdapter(voiceImageRecyclerViewAdapter);

                            guideTabs.addOnTabSelectedListener(tabSelectedListener);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Guide>> call, Throwable t) {
                        Alert.makeText(getResources().getString(R.string.network_error));
                    }
                });

//        guideEntity = new VoiceGuideGenerator().voiceGuideGenerator(locale);





        //voice setting
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAudio();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudio();
            }
        });

        String time = "0:00";
        thisTime.setText(time);

        seekbar.setVisibility(View.VISIBLE);




        guideMap.onCreate(savedInstanceState);
        guideMap.getMapAsync(this);

        initViews(view);


        return view;
    }

    private void initViews(View view) {

    }

    private void getGuideEntity(){

    }

    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            checkMyLocation();

            buildingEntity = new Guide();

            for (int i = 0; i < guideEntities.size(); i++) {
                if (guideEntities.get(i).getTitle().equals(tab.getText())) {
                    buildingEntity = guideEntities.get(i);
                }
            }

//            if( Double.parseDouble(buildingEntity.getGuideSouth()) < currentLat && currentLat < Double.parseDouble(buildingEntity.getGuideNorth())
//                    && Double.parseDouble(buildingEntity.getGuideWest()) < currentLong && currentLong < Double.parseDouble(buildingEntity.getGuideEast())){
//                buildingEntity.setChecked(1);
//            }


            if(true) {
                thisTime.setVisibility(View.VISIBLE);
                totalTime.setVisibility(View.VISIBLE);
                play.setVisibility(View.VISIBLE);
                seekbar.setVisibility(View.VISIBLE);

                title.setText(buildingEntity.getTitle());
                voiceAddress = buildingEntity.getGuideUrl();
                String time = "0:00";
                thisTime.setText(time);
//                new LogManager().LogManager("전체 시간: "+buildingEntity.getTitle(),buildingEntity.getLength()+" | "+getVoiceLength(buildingEntity.getLength()));
                totalTime.setText(getVoiceLength(buildingEntity.getLength()));
                LinearLayoutManager linkLayoutManager
                        = new LinearLayoutManager(MyApplication.getContext(), LinearLayoutManager.HORIZONTAL, false);

                guide_img_rv.setLayoutManager(linkLayoutManager);
                VoiceImageRecyclerViewAdapter voiceImageRecyclerViewAdapter
                        = new VoiceImageRecyclerViewAdapter();

                for (int i = 0; i < buildingEntity.getGuideImageList().size(); i++) {
                    voiceImageRecyclerViewAdapter.addVoiceImgList(buildingEntity.getGuideImageList().get(i));
                }
                guide_img_rv.setAdapter(voiceImageRecyclerViewAdapter);

//                double tempx =(Double.parseDouble(locEntity.voice_loc_west)
//                        +Double.parseDouble(locEntity.voice_loc_east))/2;
//                double tempy =(Double.parseDouble(locEntity.voice_loc_north)
//                        +Double.parseDouble(locEntity.voice_loc_south))/2;
                for(int i=0; i<markers.size(); i++){
                    if(markers.get(i).getTitle().equals(buildingEntity.getTitle())){
                        markers.get(i).showInfoWindow();
                    }
                }

//                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(tempy, tempx)));

            }else{
                setVoiceEmptyGuide();
                LinearLayoutManager linkLayoutManager
                        = new LinearLayoutManager(MyApplication.getContext(), LinearLayoutManager.HORIZONTAL, false);
                guide_img_rv.setLayoutManager(linkLayoutManager);
                VoiceImageRecyclerViewAdapter voiceImageRecyclerViewAdapter
                        = new VoiceImageRecyclerViewAdapter();
                guide_img_rv.setAdapter(voiceImageRecyclerViewAdapter);
                title.setText(MyApplication.getContext().getString(R.string.tab_near_building));
                Toast.makeText(MyApplication.getContext(), "Please tab again around near this building", Toast.LENGTH_LONG).show();
            }

//            Log.e("tabselected","완성");

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            int playBtn;
            String playTag = "z_play_button";

            if (mp != null) {
                pause.setVisibility(View.INVISIBLE);
                playBtn = getResources().getIdentifier(playTag, "drawable", MyApplication.getContext().getPackageName());
                play.setImageResource(playBtn);
                totalTime.setText("");
                thisTime.setText("");
                title.setText("");
                mp.stop();
//                try {
//                    mp.prepare();
//                } catch (Exception e) {
//                    Log.d("", "");
//                }
                seekbar.setProgress(0);
                mp.seekTo(0);
                mp.release();
            }
            mp = null;
            seekbar.setVisibility(View.INVISIBLE);

//                for(int i=0; i<guideImageFragmentPagerAdapter.getCount(); i++){
//                    guideImageFragmentPagerAdapter.destroyItem(guideImgVP, i, guideImageFragmentPagerAdapter.fragment);
//                    Log.e("언셀렉티드","guideImageFragmentPagerAdapter.destroyItem() : "+i+"번째 | text: "+guideImageFragmentPagerAdapter.fragment.voice_tv.getText());
//                }
//                guideImageFragmentPagerAdapter.fragment.onDestroyView();
            Log.e("언셀렉티드",tab.getText()+" | ");

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {


        }
    };

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

    private void checkMyLocation(){
        if (ContextCompat.checkSelfPermission(MyApplication.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = locationManager.getBestProvider(criteria, true);
            currentLocation = locationManager.getLastKnownLocation(provider);
            if(currentLocation != null) {
                currentLong = currentLocation.getLongitude();
                currentLat = currentLocation.getLatitude();
            }else{
                Toast.makeText(MyApplication.getContext(), R.string.failtoloadlocation, Toast.LENGTH_LONG).show();
                currentLong = 37.577401;
                currentLat = 126.989511;
            }
        }
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
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
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

        checkAround(mMap);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        if(longitude <1){
            longitude = 126.989297;
            latitude = 37.577395;
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e("map markerClick","marker.getTitle(): "+marker.getTitle());
                for(int i=0; i<guideEntities.size(); i++) {
                    if(marker.getTitle().equals(guideEntities.get(i).getTitle())) {

                        if (mp != null) {
                            int playBtn;
                            String playTag = "z_play_button";
                            pause.setVisibility(View.INVISIBLE);
                            playBtn = getResources().getIdentifier(playTag, "drawable",
                                    MyApplication.getContext().getPackageName());
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

                        tabSelectedListener.onTabSelected(guideTabs.getTabAt(i));
                        guideTabs.setScrollPosition(i,0,false);

                    }
                }
                return false;
            }
        });

    }


    private void checkAround(GoogleMap mMap){

        Log.e("Map-checkAround","콘텐츠 몇 개? "+guideEntities.size());
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
            voiceAddress = "http://13.209.61.27:8080/resources/guides/"+voiceAddress;
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
                                    if (settingVoiceGuide  && mp.isPlaying()) {
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

        Log.e("보이스가이드","stop: "+voiceAddress);

        if (mp != null) {
            pause.setVisibility(View.INVISIBLE);
            playBtn = getResources().getIdentifier(playTag, "drawable", MyApplication.getContext().getPackageName());
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
        guideMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        guideMap.onPause();
        tempStop();
    }

}

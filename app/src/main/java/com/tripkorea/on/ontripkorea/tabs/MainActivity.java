package com.tripkorea.on.ontripkorea.tabs;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.tabs.around.AroundFragment;
import com.tripkorea.on.ontripkorea.tabs.guide.GuideFragment;
import com.tripkorea.on.ontripkorea.tabs.info.InfoFragment;
import com.tripkorea.on.ontripkorea.tabs.intro.IntroFragment;
import com.tripkorea.on.ontripkorea.tabs.list.HomeFragment;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.BaseActivity;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.util.MyTabLayout;
import com.tripkorea.on.ontripkorea.vo.user.Me;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity  {
    @BindView(R.id.tab_main)
    MyTabLayout tabLayout;
    @BindView(R.id.frame_main)
    FrameLayout frameLayout;

    Fragment fragment = null;
    FragmentManager fragmentManager;

    SharedPreferences setting;
    public static SharedPreferences.Editor editor;

    String usinglanguage;
    Locale locale;

////////////////////////////////////////////////////////////////////////////YHC 수정으로 불필요
//    public List<AttrClient> aroundList = new ArrayList<>();
//    static public List<AttrClient> foodEntities = new ArrayList<>();
//    static public List<AttrClient> traceEntities = new ArrayList<>();
//    static public List<AttrClient> likeEntities = new ArrayList<>();
////////////////////////////////////////////////////////////////////////////YHC 수정으로 불필요

    static String weatherImg;
    static String weatherCel;
    ImageView nowW;
    TextView celcius;
    TextView nowWeather;
    private MediaPlayer click;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Log.e("GIT","VERSION_A");

        String welcomeMessage =getResources().getString(R.string.welcome_text)+Me.getInstance().getName();
        Alert.makeText(welcomeMessage);

        //////////////////////////////////////////////////////////////////////////sj
        setting = getSharedPreferences("setting",0);
        editor= setting.edit();
        Gson gsonTotal = new Gson();
        String totalListString = setting.getString("totalList", null);

////////////////////////////////////////////////////////////////////////////YHC 수정으로 불필요
//        AttrClientList totalList = gsonTotal.fromJson(totalListString, AttrClientList.class);
//        if(totalListString != null) {
//            aroundList = totalList.getItems();
//        }
//
//        String likeListString = setting.getString("likeList", null);
//        AttrClientList likeList = gsonTotal.fromJson(likeListString, AttrClientList.class);
//        if(likeListString != null) {
//            likeEntities = likeList.getItems();
//        }
//
//        String traceListString = setting.getString("traceList", null);
//        AttrClientList traceList = gsonTotal.fromJson(traceListString, AttrClientList.class);
//        if(traceListString != null) {
//            traceEntities = traceList.getItems();
//        }
////////////////////////////////////////////////////////////////////////////YHC 수정으로 불필요

        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        usinglanguage = locale.getDisplayLanguage();

////////////////////////////////////////////////////////////////////////////YHC 수정으로 불필요
//        if(com.tripkorea.on.ontripkorea.util.NetworkUtil.isNetworkConnected(this)){
//            com.tripkorea.on.ontripkorea.util.WifiCheck.CheckConnect cc = new WifiCheck.CheckConnect(WifiCheck.CONNECTION_CONFIRM_CLIENT_URL);
//            cc.start();
//            try {
//                cc.join();
//                if (WifiCheck.wificheck == WifiCheck.WIFI_ON) {
//                    Log.e("인터넷 체크", "연결됨");
//
//                    new AsyncTaskAttrClient().execute(usinglanguage);
//
//                } else {  Toast.makeText(this, R.string.securedWifi, Toast.LENGTH_LONG).show(); finish();}
//            }catch (Exception e){  Toast.makeText(this, R.string.waitWifi, Toast.LENGTH_LONG).show();    e.printStackTrace(); finish();    }
//        }else { Toast.makeText(this,R.string.requireInternet, Toast.LENGTH_LONG).show(); finish();}
////////////////////////////////////////////////////////////////////////////YHC 수정으로 불필요

        editor.apply();

////////////////////////////////////////////////////////////////////////////YHC 수정으로 불필요
        //사운드 초기화
//        if(click == null){
//            click = MediaPlayer.create(this, R.raw.z_clicksound);
//        }
////////////////////////////////////////////////////////////////////////////YHC 수정으로 불필요

        //weather
        nowWeather = findViewById(R.id.weatherText);
        celcius = findViewById(R.id.celcius);
        nowW = findViewById(R.id.nowWeather);



        //////////////////////////////////////////////////////////////////////////sj

        //////////////////////////////////////////////////////////////////////////hc
        fragmentManager = getSupportFragmentManager();

        initViews();
    }

    private void initViews() {
        replaceFragment(R.id.frame_main, new HomeFragment());

        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
    }

    MyTabLayout.OnTabSelectedListener onTabSelectedListener = new MyTabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            // Fragment fragment = null;
            switch (tab.getPosition()) {
                case MyTabLayout.TAB_HOME:
                    fragment = new HomeFragment();
                    break;
                case MyTabLayout.TAB_INTRO:
                    fragment = new IntroFragment();
                    break;
                case MyTabLayout.TAB_GUIDE:
                    checkLocationPermission();
                    GuideFragment guideFragment = new GuideFragment();
                    guideFragment.guideFragmentNewInstance(mMap);
                    fragment = guideFragment;
                    break;
                case MyTabLayout.TAB_AROUND:
                    checkLocationPermission();
                    AroundFragment aroundFragment = new AroundFragment();
                    aroundFragment.aroundFragmentNewInstance(MainActivity.this, mMap);
                    fragment = aroundFragment;
                    break;
                case MyTabLayout.TAB_INFO:
                    InfoFragment infoFragment = new InfoFragment();
                    fragment = infoFragment;
                    break;
            }

//            tab.setIcon(getResources().getDrawable(R.drawable.appbar_home_off));

            replaceFragment(R.id.frame_main, fragment);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    private void replaceFragment(int framId, Fragment fragment){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(framId, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

//        ApiClient.getInstance()
//                .getApiService()
//                .updateLocation(MyApplication.APP_VERSION, )
//                .enqueue(new Callback<ApiMessasge>() {
//                    @Override
//                    public void onResponse(Call<ApiMessasge> call, Response<ApiMessasge> response) {
//                        if(response.body().getCode()==1) {
//                            Log.e("LOCATION", "SEND");
//                        }else {
//                            Log.e("LOCATION", "SEND_ERROR");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ApiMessasge> call, Throwable t) {
//                        Log.e("LOCATION", "SEND_FAIL");
//                    }
//                });
    }

    @Override
    public void onBackPressed() {
        if(fragmentManager.getBackStackEntryCount()<1){
            showUpExitDialog();
        }
    }

    private void showUpExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.exit_text)
                .setCancelable(true)
                .setPositiveButton(R.string.exit_quit_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.exit_cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    ////////////////////////////////////////////////////////////////////////////YC 수정으로 불필요
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //AsyncTaskContentTotal check
//    public class AsyncTaskAttrClient extends AsyncTask<String, Void, List<AttrClient>> {
//
//        @Override
//        protected List<AttrClient> doInBackground(String... lang) {
//            Response response = null; //Response
//            OkHttpClient toServer;  //connection
//            try {
//                toServer = OkHttpInitSingtonManager.getOkHttpClient();
//                Request request = new Request.Builder()
//                        .url(NetworkDefineConstant.SERVER_CONTENT_TOTAL+"language="+lang[0])
//                        .build();
//                response = toServer.newCall(request).execute();
//                String responsedMessage = response.body().string();
//
//                editor.putString("attr",responsedMessage);
//                editor.putBoolean("attrBoolean",true);
//                editor.commit();
//
//                Gson gson = new Gson();
//                if (response.isSuccessful()) {
//                    Log.e("AsyncTaskAttrClient:", responsedMessage);
//                    AttrClientList contents = gson.fromJson(responsedMessage, AttrClientList.class);
//                    foodEntities = contents.getItems();
//                    Log.e("AsyncTaskAttrClient:",
//                            "MainPage.foodEntities size"+foodEntities.size()+" | ");
//
//                } else {
//                    Log.e("AsyncTaskAttrCl Req:", response.message());
//                }
//            } catch (IOException e) {
//                Log.e("AsyncTaskAttrCl Parse",  e.toString());
//            } finally {
//                if (response != null) {
//                    response.close();
//                }
//            }
//            return foodEntities;
//        }
//
//        @Override
//        protected void onPostExecute(List<AttrClient> tourEntities) {
//            super.onPostExecute(tourEntities);
//
//            AttrClient attrClient  = new AttrClient();
//            attrClient.mapx = "126.976818";
//            attrClient.mapy = "37.575844";
//
//
//        }
//    }
////////////////////////////////////////////////////////////////////////////YC 수정으로 불필요


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mMap;
    public GoogleApiClient mGoogleApiClient;

    protected synchronized void buildGoogleApiClient() {
//        )Log.e("빌드에이피아이","비이이이이이틀");
        mGoogleApiClient = new GoogleApiClient.Builder(MyApplication.getContext())
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

    }

    public void checkLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MyApplication.getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();

//                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//                    @Override
//                    public boolean onMyLocationButtonClick() {
//
//                        return false;
//                    }
//                });
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            buildGoogleApiClient();
//            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//                @Override
//                public boolean onMyLocationButtonClick() {
//
//                    return false;
//                }
//            });
        }
    }


}

package com.tripkorea.on.ontripkorea.tabs;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
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
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.tabs.around.AroundFragment;
import com.tripkorea.on.ontripkorea.tabs.guide.GuideFragment;
import com.tripkorea.on.ontripkorea.tabs.info.InfoFragment;
import com.tripkorea.on.ontripkorea.tabs.intro.IntroFragment;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.BaseActivity;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.util.MyTabLayout;
import com.tripkorea.on.ontripkorea.util.OnNetworkErrorListener;
import com.tripkorea.on.ontripkorea.vo.user.Me;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {
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

    static String weatherImg;
    static String weatherCel;
    ImageView nowW;
    TextView celcius;
    TextView nowWeather;
    private MediaPlayer click;

    static int currentTab;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mMap;
    public GoogleApiClient mGoogleApiClient;
    Location currentLocation;
    double currentLat, currentLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Log.e("GIT","VERSION_A");

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo("com.tripkorea.on.ontripkorea", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }

        String welcomeMessage =getResources().getString(R.string.welcome_text)+Me.getInstance().getName();
        Alert.makeText(welcomeMessage);

        //////////////////////////////////////////////////////////////////////////sj
        setting = getSharedPreferences("setting",0);
        editor= setting.edit();
        Gson gsonTotal = new Gson();
        String totalListString = setting.getString("totalList", null);


        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        usinglanguage = locale.getDisplayLanguage();

        currentTab = -1;

        editor.apply();

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
        replaceFragment(R.id.frame_main, new IntroFragment());

        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
    }

    TabLayout.Tab lastTab;

    MyTabLayout.OnTabSelectedListener onTabSelectedListener = new MyTabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
             fragment = null;
            switch (tab.getPosition()) {

                case MyTabLayout.TAB_INTRO:
                    IntroFragment introfragment = new IntroFragment();
                    introfragment.introFragmentNewInstance(currentTab);
                    introfragment.setOnNetworkErrorListener(new OnNetworkErrorListener() {
                        @Override
                        public void onNetWorkError() {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.requireInternet), Toast.LENGTH_LONG).show();
                            if(lastTab == null) {
                                finish();
                            }else{
                                onTabSelected(lastTab);
                            }
                        }
                    });
                    fragment = introfragment;
                    currentTab = MyTabLayout.TAB_INTRO;
                    lastTab = tab;
                    break;
                case MyTabLayout.TAB_GUIDE:
                    checkLocationPermission();
                    GuideFragment guideFragment = new GuideFragment();
                    guideFragment.guideFragmentNewInstance(mMap, MainActivity.this, currentTab);
                    guideFragment.setOnNetworkErrorListener(new OnNetworkErrorListener() {
                        @Override
                        public void onNetWorkError() {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.requireInternet), Toast.LENGTH_LONG).show();
                            if(lastTab == null) {
                                finish();
                            }else{
                                onTabSelected(lastTab);
                            }
                        }
                    });
                    fragment = guideFragment;
                    currentTab = MyTabLayout.TAB_GUIDE;
                    lastTab = tab;
                    break;
                case MyTabLayout.TAB_AROUND:
                    checkLocationPermission();
                    AroundFragment aroundFragment = new AroundFragment();
                    aroundFragment.aroundFragmentNewInstance(mMap, MainActivity.this,  currentTab);
                    fragment = aroundFragment;
                    currentTab = MyTabLayout.TAB_AROUND;
                    aroundFragment.setOnNetworkErrorListener(new OnNetworkErrorListener() {
                        @Override
                        public void onNetWorkError() {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.requireInternet), Toast.LENGTH_LONG).show();
                            if(lastTab == null) {
                                finish();
                            }else{
                                onTabSelected(lastTab);
                            }
                        }
                    });
                    lastTab = tab;
                    break;
                case MyTabLayout.TAB_INFO:
                    InfoFragment infoFragment = new InfoFragment();
                    fragment = infoFragment;
                    currentTab = MyTabLayout.TAB_INFO;
                    infoFragment.setOnNetworkErrorListener(new OnNetworkErrorListener() {
                        @Override
                        public void onNetWorkError() {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.requireInternet), Toast.LENGTH_LONG).show();
                            if(lastTab == null) {
                                finish();
                            }else{
                                onTabSelected(lastTab);
                            }
                        }
                    });
                    lastTab = tab;
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
        builder.setMessage( getResources().getString(R.string.exit_text))
                .setCancelable(true)
                .setPositiveButton( getResources().getString(R.string.exit_quit_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton( getResources().getString(R.string.exit_cancel_text) , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }






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

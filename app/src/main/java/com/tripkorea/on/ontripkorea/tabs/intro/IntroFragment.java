package com.tripkorea.on.ontripkorea.tabs.intro;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.util.NetworkUtil;
import com.tripkorea.on.ontripkorea.util.OnNetworkErrorListener;
import com.tripkorea.on.ontripkorea.util.WifiCheck;

import java.util.Locale;

import static com.tripkorea.on.ontripkorea.util.WifiCheck.CONNECTION_CONFIRM_CLIENT_URL;

/**
 * Created by YangHC on 2018-06-05.
 */

public class IntroFragment extends Fragment {
    public OnNetworkErrorListener onNetworkErrorListener;

    public void setOnNetworkErrorListener(OnNetworkErrorListener onNetworkErrorListener) {
        this.onNetworkErrorListener = onNetworkErrorListener;
    }

    int lastTab;

    String usinglanguage;
    Locale locale;

    private String weatherImg;
    private String weatherCel;
    ImageView nowW;
    TextView celcius;
    TextView nowWeather;

    public IntroFragment(){}

    public Fragment introFragmentNewInstance(int lastTab){
        this.lastTab = lastTab;
        return new IntroFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro, container, false);

        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        usinglanguage = locale.getDisplayLanguage();

        //weather
        nowWeather = view.findViewById(R.id.weatherText);
        celcius = view.findViewById(R.id.celcius);
        nowW = view.findViewById(R.id.nowWeather);

        if(NetworkUtil.isNetworkConnected(MyApplication.getContext())){
            WifiCheck.CheckConnect cc = new WifiCheck.CheckConnect(CONNECTION_CONFIRM_CLIENT_URL);
            cc.start();
            try {
                cc.join();
                if (WifiCheck.wificheck != WifiCheck.WIFI_ON) {
                    onNetworkErrorListener.onNetWorkError();
                    Toast.makeText(MyApplication.getContext(), R.string.securedWifi, Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                onNetworkErrorListener.onNetWorkError();
                Toast.makeText(MyApplication.getContext(), R.string.waitWifi, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }else {
            Toast.makeText(MyApplication.getContext(),R.string.requireInternet,Toast.LENGTH_LONG).show();
        }

        ViewPager main_image_vp = view.findViewById(R.id.main_image_vp);
        MainImageFragmentPagerAdapter mainImageFragmentPagerAdapter
                = new MainImageFragmentPagerAdapter(getChildFragmentManager() );

        main_image_vp.setAdapter(mainImageFragmentPagerAdapter);
        switch (usinglanguage) {
            case "한국어":
                mainImageFragmentPagerAdapter.addMainImage("http://13.209.61.27:8080/resources/images/catoon/changdeokgung/ko_changdeok_0(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.209.61.27:8080/resources/images/catoon/changdeokgung/ko_changdeok_00_intro(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.209.61.27:8080/resources/images/catoon/changdeokgung/ko_changdeok_01_don(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.209.61.27:8080/resources/images/catoon/changdeokgung/ko_changdeok_02_geum(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.209.61.27:8080/resources/images/catoon/changdeokgung/ko_changdeok_03_in(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.209.61.27:8080/resources/images/catoon/changdeokgung/ko_changdeok_04_seon_hee(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.209.61.27:8080/resources/images/catoon/changdeokgung/ko_changdeok_05_nak_total(810x1230).jpg");
                break;
            default:
                mainImageFragmentPagerAdapter.addMainImage("http://13.209.61.27:8080/resources/images/catoon/changdeokgung/en_changdeok_0(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.209.61.27:8080/resources/images/catoon/changdeokgung/en_changdeok_00_intro(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.209.61.27:8080/resources/images/catoon/changdeokgung/en_changdeok_01_don(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.209.61.27:8080/resources/images/catoon/changdeokgung/en_changdeok_02_geum(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.209.61.27:8080/resources/images/catoon/changdeokgung/en_changdeok_03_in(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.209.61.27:8080/resources/images/catoon/changdeokgung/en_changdeok_04_seon_hee(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.209.61.27:8080/resources/images/catoon/changdeokgung/en_changdeok_05_nak_total(810x1230).jpg");
                break;
        }
        main_image_vp.setAdapter(mainImageFragmentPagerAdapter);



        nowW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtil.isNetworkConnected(MyApplication.getContext())){
                    WifiCheck.CheckConnect cc = new WifiCheck.CheckConnect(CONNECTION_CONFIRM_CLIENT_URL);
                    cc.start();
                    try {
                        cc.join();
                        if (WifiCheck.wificheck == WifiCheck.WIFI_ON) {
                            ReceiveShortWeatherAsync receiveShortWeatherAsync = new ReceiveShortWeatherAsync();
                            receiveShortWeatherAsync.setOnPostExecuteListener(new ReceiveShortWeatherAsync.OnPostExecuteListener() {
                                @Override
                                public void onPostExecute(String weatherImg, String weatherCel) {
                                    IntroFragment.this.weatherImg =  weatherImg;
                                    IntroFragment.this.weatherCel = weatherCel;

                                    celcius.setText(weatherCel);
                                    switch(weatherImg) {
                                        case "맑음":
                                            nowW.setImageResource(R.drawable.w_sun);
                                            nowWeather.setText(R.string.sunny);
                                            break;
                                        case "구름 조금":
                                            nowW.setImageResource(R.drawable.w_partly_sunny);
                                            nowWeather.setText(R.string.littlecloud);
                                            break;
                                        case "구름 많음":
                                            nowW.setImageResource(R.drawable.w_cloud);
                                            nowWeather.setText(R.string.manycloud);
                                            break;
                                        case "흐림":
                                            nowW.setImageResource(R.drawable.w_black_cloud);
                                            nowWeather.setText(R.string.cloudy);
                                            break;
                                        case "비":
                                            nowW.setImageResource(R.drawable.w_umbreller);
                                            nowWeather.setText(R.string.rainy);
                                            break;
                                        case "눈/비":
                                            nowW.setImageResource(R.drawable.w_rainy_snowy);
                                            nowWeather.setText(R.string.snowandrain);
                                            break;
                                        case "눈":
                                            nowW.setImageResource(R.drawable.w_snow);
                                            nowWeather.setText(R.string.snow);
                                            break;
                                    }
                                }
                            });
                            receiveShortWeatherAsync.execute();
                        } else {
                            Toast.makeText(MyApplication.getContext(), R.string.securedWifi, Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(MyApplication.getContext(), R.string.waitWifi, Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(MyApplication.getContext(),R.string.requireInternet,Toast.LENGTH_LONG).show();
                }
            }
        });

        initViews(view);

        return view;
    }

    private void initViews(View view) {

    }
}

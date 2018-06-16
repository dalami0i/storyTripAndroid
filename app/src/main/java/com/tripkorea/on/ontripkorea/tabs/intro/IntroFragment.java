package com.tripkorea.on.ontripkorea.tabs.intro;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.util.NetworkUtil;
import com.tripkorea.on.ontripkorea.util.WifiCheck;
import com.tripkorea.on.ontripkorea.vo.ShortWeather;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.tripkorea.on.ontripkorea.util.WifiCheck.CONNECTION_CONFIRM_CLIENT_URL;

/**
 * Created by YangHC on 2018-06-05.
 */

public class IntroFragment extends Fragment {


    String usinglanguage;
    Locale locale;

    static String weatherImg;
    static String weatherCel;
    ImageView nowW;
    TextView celcius;
    TextView nowWeather;


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

        ViewPager main_image_vp = view.findViewById(R.id.main_image_vp);
        MainImageFragmentPagerAdapter mainImageFragmentPagerAdapter
                = new MainImageFragmentPagerAdapter(getChildFragmentManager() );

        main_image_vp.setAdapter(mainImageFragmentPagerAdapter);
        switch (usinglanguage) {
            case "한국어":
                mainImageFragmentPagerAdapter.addMainImage("http://13.124.155.173:8512/mylittleseoul/realkorea/ko_changdeok_0(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.124.155.173:8512/mylittleseoul/realkorea/ko_changdeok_00_intro(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.124.155.173:8512/mylittleseoul/realkorea/ko_changdeok_01_don(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.124.155.173:8512/mylittleseoul/realkorea/ko_changdeok_02_geum(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.124.155.173:8512/mylittleseoul/realkorea/ko_changdeok_03_in(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.124.155.173:8512/mylittleseoul/realkorea/ko_changdeok_04_seon_hee(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.124.155.173:8512/mylittleseoul/realkorea/ko_changdeok_05_nak_total(810x1230).jpg");
                break;
            default:
                mainImageFragmentPagerAdapter.addMainImage("http://13.124.155.173:8512/mylittleseoul/realkorea/en_changdeok_0(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.124.155.173:8512/mylittleseoul/realkorea/en_changdeok_00_intro(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.124.155.173:8512/mylittleseoul/realkorea/en_changdeok_01_don(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.124.155.173:8512/mylittleseoul/realkorea/en_changdeok_02_geum(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.124.155.173:8512/mylittleseoul/realkorea/en_changdeok_03_in(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.124.155.173:8512/mylittleseoul/realkorea/en_changdeok_04_seon_hee(810x1230).jpg");
                mainImageFragmentPagerAdapter.addMainImage("http://13.124.155.173:8512/mylittleseoul/realkorea/en_changdeok_05_nak_total(810x1230).jpg");
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
                        if (WifiCheck.wificheck == 1) {
                            new ReceiveShortWeather().execute();
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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 메인 이미지를 위한 view pager
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class MainImageFragmentPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<String> mainImageFragments = new ArrayList<>();
//        ViewPager detailedImageVP;

        private void addMainImage(String mainImage){
            mainImageFragments.add(mainImage);
        }


        private MainImageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
//            this.detailedImageVP = detailedImageVP;
        }

        @Override
        public float getPageWidth(int position) {
            return 1.0f;
        }

        @Override
        public Fragment getItem(int position) {
            return new MainImageListFragment().newInstance(
                    mainImageFragments.get(position), mainImageFragments.size(), position);
        }

        @Override
        public int getCount() {
            return mainImageFragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    public static class MainImageListFragment extends Fragment {
        private String mainImgAddr;
        RoundedImageView main_img;
        int size;
        int position;
        Unbinder unbinder;

        public MainImageListFragment() { }

        public static MainImageListFragment newInstance(
                String mainImgAddr, int size, int position) {
            Bundle bundle = new Bundle();
            bundle.putInt("totalsize", size);
            bundle.putInt("position", position);
            bundle.putString("mainImage",mainImgAddr);
            MainImageListFragment mainImageListFragment = new MainImageListFragment();
            mainImageListFragment.setArguments(bundle);
            return mainImageListFragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                size = getArguments().getInt("totalsize");
                position = getArguments().getInt("position");
                mainImgAddr = getArguments().getString("mainImage");
            }
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            LinearLayout view = (LinearLayout) inflater.inflate(R.layout.mainpageimage, container, false);
            unbinder = ButterKnife.bind(this, view            );
            MainActivity owner = (MainActivity) getActivity();
            main_img = view.findViewById(R.id.main_img);
            Glide.with(owner).load(mainImgAddr).into(main_img);
            TextView right_arrow = view.findViewById(R.id.right_arrow);
            TextView left_arrow = view.findViewById(R.id.left_arrow);
//            if(popular == 1) {
//                detailed_img.setLayoutParams(new LinearLayout.LayoutParams(width, height-80));
//            }
//            new org.androidtown.realchangdeokgung.LogManager()Log.e("길이와 위치:","길이: "+size+" 위치: "+mainImgAddr);

            if(owner != null) {
                Glide.with(owner).load(mainImgAddr).into(main_img);
//                )Log.e("디테일","detailedImgAddr.getImgURL(): "+detailedImgAddr.getImgURL());
                if(size == 1){
                    right_arrow.setVisibility(View.GONE);
                    left_arrow.setVisibility(View.GONE);
                }else if(position == 0){
                    right_arrow.setVisibility(View.GONE);
                    left_arrow.setVisibility(View.VISIBLE);
                }else if(position == (size-1)){
                    right_arrow.setVisibility(View.VISIBLE);
                    left_arrow.setVisibility(View.GONE);
                }else{
                    right_arrow.setVisibility(View.VISIBLE);
                    left_arrow.setVisibility(View.VISIBLE);
                }

            }





            return view;
        }
        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }
    }



    //inner class.
    private static class ReceiveShortWeather extends AsyncTask<URL, Integer, Long> {

        ArrayList<ShortWeather> shortWeathers = new ArrayList<>();

        /*TextView celcius;
        ImageView nowW;
        TextView nowWeather;*/

        public ReceiveShortWeather(){
            /*this.celcius = celcius;
            this.nowW = nowW;
            this.nowWeather = nowWeather;*/
        }

        protected Long doInBackground(URL... urls) {

            String url = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1111060000";

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                parseXML(client.newCall(request).execute().body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Long result) {
            weatherImg =  shortWeathers.get(0).getWfKor();
            weatherCel = shortWeathers.get(0).getTemp();
            //textView_shortWeather.setText(data);
        }

        void parseXML(String xml) {
            String tagName = "";
//            boolean onHour = false;
//            boolean onDay = false;
            boolean onTem = false;
            boolean onWfKor = false;
//            boolean onPop = false;
            boolean onEnd = false;
            boolean isItemTag1 = false;
            int i = 0;
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                parser.setInput(new StringReader(xml));

                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagName = parser.getName();
                        if (tagName.equals("data")) {
                            shortWeathers.add(new ShortWeather());
                            onEnd = false;
                            isItemTag1 = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT && isItemTag1) {
//                        if (tagName.equals("hour") && !onHour) {
//                            shortWeathers.get(i).setHour(parser.getText());
//                            onHour = true;
//                        }
//                        if (tagName.equals("day") && !onDay) {
//                            shortWeathers.get(i).setDay(parser.getText());
//                            onDay = true;
//                        }
                        if (tagName.equals("temp") && !onTem) {
                            shortWeathers.get(i).setTemp(parser.getText());
                            onTem = true;
                        }
                        if (tagName.equals("wfKor") && !onWfKor) {
                            shortWeathers.get(i).setWfKor(parser.getText());
                            onWfKor = true;
                        }
//                        if (tagName.equals("pop") && !onPop) {
//                            shortWeathers.get(i).setPop(parser.getText());
//                            onPop = true;
//                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (tagName.equals("s06") && !onEnd) {
                            i++;
//                            onHour = false;
//                            onDay = false;
                            onTem = false;
                            onWfKor = false;
//                            onPop = false;
                            isItemTag1 = false;
                            onEnd = true;
                        }
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

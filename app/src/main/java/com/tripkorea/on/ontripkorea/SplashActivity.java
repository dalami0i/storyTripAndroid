package com.tripkorea.on.ontripkorea;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.Coordinate;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;

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

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2500;

    Locale locale;
    String usinglanguage;
    int language;

    @BindView(R.id.txt_splash_logo)
    TextView logoTxt;
    @BindView(R.id.txt_splash_logo2)
    TextView logo2Txt;

    private List<AttractionSimple> restaurantList = new ArrayList<>();
    private AttractionSimpleList foodList;
    private AttractionSimpleList attractionList;

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

        Coordinate coordinate = new Coordinate(37.579108, 126.990957);

        setRestaurants(coordinate.getLat(), coordinate.getLon(), 1);





    }


    // TODO : 서버에서 주변 관광지 정보 가져오기
    private void setRestaurants(final double lat,final double lon, final int page) {
        ApiClient.getInstance().getApiService()
                .getAroundRestaurants(MyApplication.APP_VERSION,lat, lon,language, page)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        if (response.body() != null) {
                            foodList = new AttractionSimpleList();
                            foodList.setItems(response.body());

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
    private void setTours(double lat, double lon, int page) {
        ApiClient.getInstance().getApiService()
                .getAroundTours(MyApplication.APP_VERSION,lat, lon,language, page)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        if (response.body() != null) {
                            attractionList = new AttractionSimpleList();
                            attractionList.setItems(response.body());
                            if(attractionList.getItems().size() > 0){
                                goToNext();
                            }

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




    public void goToNext(){
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        mainIntent.putExtra("foods",foodList);
        mainIntent.putExtra("attraction",attractionList);
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
}

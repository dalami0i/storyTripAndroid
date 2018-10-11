package com.tripkorea.on.ontripkorea.tabs.toon;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.WindowManager;

import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.retrofit.message.ApiMessage;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.toonguide.ToonImageEntity;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

///**
// * Created by Edward Won on 2018-07-16.
// */

public class ToonDetailImageActivity extends AppCompatActivity {

//    ToonImageEntity toon;
    ToonImageEntity toonImages;
    int guideIdx;
    String usinglanguage;
    int language;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toon_detail_img);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        //사용자 언어 확인
        Locale locale;
        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.N) {
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
                language = 0;
                break;
            case "日本言":
                language = 0;
                break;
            default:
                language = 0;
                break;
        }

        Intent intent = getIntent();
        Bundle bundleExtra = intent.getExtras();
        toonImages = bundleExtra.getParcelable("toons");
        guideIdx = intent.getIntExtra("guideIdx",0);

        RecyclerView showImgRV = findViewById(R.id.rv_toon_image);

        LinearLayoutManager linkLayoutManager
                = new LinearLayoutManager(MyApplication.getContext(), LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        showImgRV.setLayoutManager(linkLayoutManager);
        snapHelper.attachToRecyclerView(showImgRV);
        ToonShowImageRecyclerViewAdapter showToonImageRecyclerViewAdapter
                = new ToonShowImageRecyclerViewAdapter(this);
        for(int i=0;i<toonImages.getToonList().size(); i++){
            showToonImageRecyclerViewAdapter.addToonImgList(toonImages.getToonList().get(i));
        }
        showImgRV.setAdapter(showToonImageRecyclerViewAdapter);

        ApiClient.getInstance().getApiService()
                .watchToon(MyApplication.APP_VERSION,guideIdx,language)
                .enqueue(new Callback<ApiMessage>() {
                    @Override
                    public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                        if (response.body() != null) {
                            ApiMessage apiMessage = response.body();
                            new LogManager().LogManager("툰디테일이미지엑","onCreate() watchToon getMessage(): "+apiMessage.getMessage());

                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    new LogManager().LogManager("툰디테일이미지엑", "onCreate() watchToon  error  " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiMessage> call, Throwable t) {
                        new LogManager().LogManager("툰디테일이미지엑", "onCreate() watchToon onFailure  " +t.getMessage());
                        Alert.makeText(getString(R.string.network_error));
                    }
                });

    }

    @Override
    protected void onPause() {
        super.onPause();

        ApiClient.getInstance().getApiService()
                .stopToon(MyApplication.APP_VERSION,guideIdx,language)
                .enqueue(new Callback<ApiMessage>() {
                    @Override
                    public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                        if (response.body() != null) {
                            ApiMessage apiMessage = response.body();
                            new LogManager().LogManager("툰디테일이미지엑","onPause() stopToon getMessage(): "+apiMessage.getMessage());

                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    new LogManager().LogManager("툰디테일이미지엑", "onPause() stopToon  error  " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiMessage> call, Throwable t) {
                        new LogManager().LogManager("툰디테일이미지엑", "onPause() stopToon onFailure  " +t.getMessage());
                        Alert.makeText(getString(R.string.network_error));
                    }
                });

    }
}



package com.tripkorea.on.ontripkorea;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tripkorea.on.ontripkorea.tabs.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

///**
// * Created by YangHC on 2018-06-18.
// */

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2500;

    @BindView(R.id.txt_splash_logo)
    TextView logoTxt;
    @BindView(R.id.txt_splash_logo2)
    TextView logo2Txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        Typeface geBody = Typeface.createFromAsset(getAssets(), "fonts/GeBody.ttf");
        logoTxt.setTypeface(geBody);
        logo2Txt.setTypeface(geBody);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onBackPressed() {
        // 로딩 중에 종료하지 못하게 막음
        // super.onBackPressed();
    }
}

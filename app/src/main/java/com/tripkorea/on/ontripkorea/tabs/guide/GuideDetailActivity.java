package com.tripkorea.on.ontripkorea.tabs.guide;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.util.NetworkUtil;
import com.tripkorea.on.ontripkorea.util.WifiCheck;
import com.tripkorea.on.ontripkorea.vo.user.Me;
import com.tripkorea.on.ontripkorea.vo.voiceguide.Guide;
import com.tripkorea.on.ontripkorea.vo.voiceguide.VoiceGuideImageEntity;
import com.tripkorea.on.ontripkorea.vo.voiceguide.VoiceGuideLog;

///**
// * Created by Edward Won on 2018-07-30.
// */

public class GuideDetailActivity extends AppCompatActivity{

    TextView thisTime;
    TextView totalTime;
    ImageView play, pause;
    SeekBar seekbar;
    TextView title;

    Guide detailGuide;
    VoiceGuideImageEntity imgListEntity;

    GridView voiceImgGridview;

    private MediaPlayer mp;
    private Runnable mUpdateTime;
    private boolean settingVoiceGuide;
    private static String voiceAddress;

    static final String CONNECTION_CONFIRM_CLIENT_URL = "http://clients3.google.com/generate_204";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_guide);

        Intent voiceIntent = getIntent();
        Bundle bundleExtra = voiceIntent.getExtras();
        detailGuide = bundleExtra.getParcelable("voiceGuide");
        imgListEntity = bundleExtra.getParcelable("voiceGuideIMG");


        voiceImgGridview = findViewById(R.id.voice_img_gridview);
        thisTime = findViewById(R.id.thistime);
        totalTime = findViewById(R.id.totaltime);
        title = findViewById(R.id.thistitle);
        play = findViewById(R.id.palybtn);
        pause = findViewById(R.id.pausebtn);
        seekbar = findViewById(R.id.seekbar);

        thisTime.setVisibility(View.VISIBLE);
        totalTime.setVisibility(View.VISIBLE);
        play.setVisibility(View.VISIBLE);
        seekbar.setVisibility(View.VISIBLE);

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


        title.setText(detailGuide.getTitle());
        voiceAddress = detailGuide.getVoiceAddress();
        String time = "0:00";
        thisTime.setText(time);
        String totalLength = getVoiceLength(detailGuide.getLength())+"";
        totalTime.setText(totalLength);


        VoiceImageGridAdapter gridAdapter
                = new VoiceImageGridAdapter(GuideDetailActivity.this,
                                            R.layout.item_voice_guide_grid,
                                            detailGuide);

        voiceImgGridview.setAdapter(gridAdapter);


    }

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
                voiceAddress = "http://13.209.61.27:8080/resources/guides/voice/" + voiceAddress;
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
    }

    @Override
    public void onPause() {
        super.onPause();
        tempStop();
        VoiceGuideLog log = new VoiceGuideLog();
        log.setGuideIdx(detailGuide.getIdx());
        log.setUserIdx(Me.getInstance().getIdx());
        log.setTotalLength(detailGuide.getLength());
        log.setListenTimeLength(returnVoiceLength(String.valueOf(thisTime.getText())));
        GuideActivity.logs.add(log);
    }
}

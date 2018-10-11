package com.tripkorea.on.ontripkorea.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.tripkorea.on.ontripkorea.BuildConfig;
import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.retrofit.client.NaverApiClient;
import com.tripkorea.on.ontripkorea.vo.user.LoginResponse;
import com.tsengvn.typekit.Typekit;

/**
 * Created by KUvH on 2017-02-06.
 */

public class MyApplication extends MultiDexApplication {
    public static int APP_VERSION;
    public static boolean WILL_BE_DESTROYED = false;
    public static LoginResponse me;
    private static Context context; //Edward added 2018.06.11

    private static volatile MyApplication instance = null;
    private static volatile Activity currentActivity = null;

    public void onCreate() {
        super.onCreate();
        initMpm();

        context = this; //Edward added 2018.06.11

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/NanumGothic.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/NanumGothic.ttf"));

        ApiClient.getInstance().create();
        NaverApiClient.getInstance().create();

        instance = this;

        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public static void build(Context context) {
        MultiDex.install(context.getApplicationContext());
        PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        APP_VERSION = pi.versionCode;
    }

    private void initMpm() {
        io.imqa.core.IMQAOption imqaOption = new io.imqa.core.IMQAOption();
        imqaOption.setBuildType(false);
        imqaOption.setUploadPeriod(true);
        imqaOption.setKeepFileAtUploadFail(true);
        imqaOption.setDumpInterval(1000);
        imqaOption.setFileInterval(1);

        io.imqa.mpm.IMQAMpmAgent.getInstance()
                .setOption(imqaOption) // MPM 의 동작 방식을 정하는 옵션을 설정합니다.
                .setContext(this, BuildConfig.FLAVOR) // Application Context 를 초기화합니다.
                .setProjectKey("$2a$05$MkmmPJp7zHfF55R1HOdS.udIg6PU/xEGxNc3uv0PgNnVJ8i6ii73q") // IMQA MPM Client 의 Project Key 를 설정합니다.
                .init() // 등록한 옵션을 초기화합니다.
                .startResDump(); // MPM을 실행합니다.
    }

    public static Context getContext() {
        return context;
    }  //Edward added 2018.06.11

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        MyApplication.currentActivity = currentActivity;
    }

    public static MyApplication getMyApplicationContext() {
        if (instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }


    private static class KakaoSDKAdapter extends KakaoAdapter {
        // 로그인 시 사용 될, Session의 옵션 설정을 위한 인터페이스 입니다.
        @Override
        public ISessionConfig getSessionConfig() {
            new LogManager().LogManager("Myapplication 카톡 로그인", "KakaoSDKAdapter getSessionConfig");
            return new ISessionConfig() {

                // 로그인 시에 인증 타입을 지정합니다.
                @Override
                public AuthType[] getAuthTypes() {
                    new LogManager().LogManager("Myapplication 카톡 로그인", "KakaoSDKAdapter getAuthTypes");
                /*KAKAO_TALK;*/

                    // Auth Type
                    // KAKAO_TALK  : 카카오톡 로그인 타입
                    // KAKAO_STORY : 카카오스토리 로그인 타입
                    // KAKAO_ACCOUNT : 웹뷰 다이얼로그를 통한 계정연결 타입
                    // KAKAO_TALK_EXCLUDE_NATIVE_LOGIN : 카카오톡 로그인 타입과 함께 계정생성을 위한 버튼을 함께 제공
                    // KAKAO_LOGIN_ALL : 모든 로그인 방식을 제공

                    return new AuthType[]{AuthType.KAKAO_ACCOUNT};

                }


                // 로그인 웹뷰에서 pause와 resume시에 타이머를 설정하여, CPU의 소모를 절약 할 지의 여부를 지정합니다.
                // true로 지정할 경우, 로그인 웹뷰의 onPuase()와 onResume()에 타이머를 설정해야 합니다.
                @Override
                public boolean isUsingWebviewTimer() {
                    new LogManager().LogManager("Myapplication 카톡 로그인", "KakaoSDKAdapter isUsingWebviewTimer");
                    return false;

                }


                // 로그인 시 토큰을 저장할 때의 암호화 여부를 지정합니다.
                @Override
                public boolean isSecureMode() {
                    new LogManager().LogManager("Myapplication 카톡 로그인", "KakaoSDKAdapter isSecureMode");
                    return false;

                }


                // 일반 사용자가 아닌 Kakao와 제휴 된 앱에서 사용되는 값입니다.
                // 값을 지정하지 않을 경우, ApprovalType.INDIVIDUAL 값으로 사용됩니다.
                @Override
                public ApprovalType getApprovalType() {
                    new LogManager().LogManager("Myapplication 카톡 로그인", "KakaoSDKAdapter getApprovalType");
                    return ApprovalType.INDIVIDUAL;

                }


                // 로그인 웹뷰에서 email 입력 폼의 데이터를 저장할 지 여부를 지정합니다.
                @Override
                public boolean isSaveFormData() {
                    new LogManager().LogManager("Myapplication 카톡 로그인", "KakaoSDKAdapter isSaveFormData");
                    return true;

                }

            };

        }


        // Application이 가지고 있는 정보를 얻기 위한 인터페이스 입니다.
        @Override
        public IApplicationConfig getApplicationConfig() {
            new LogManager().LogManager("Myapplication 카톡 로그인", "KakaoSDKAdapter getApplicationConfig");
            return new IApplicationConfig() {

                @Override
                public Context getApplicationContext() {

                    return MyApplication.getContext();

                }

            };

        }

    }

}

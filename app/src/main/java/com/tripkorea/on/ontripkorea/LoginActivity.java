package com.tripkorea.on.ontripkorea;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.retrofit.client.NaverApiClient;
import com.tripkorea.on.ontripkorea.retrofit.message.ApiMessasge;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.InternetTotalCheck;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;
import com.tripkorea.on.ontripkorea.vo.user.NaverResponse;
import com.tripkorea.on.ontripkorea.vo.user.NaverUser;
import com.tripkorea.on.ontripkorea.vo.user.User;
import com.tripkorea.on.ontripkorea.vo.voiceguide.GuidePhoto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.MediaType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Edward Won on 2018-07-13.*/


public class LoginActivity extends AppCompatActivity {
    private static final MediaType JSON = MediaType.parse("application/jsonarray; charset=utf-8");//전송 위해 JSON type 설정. 다른 activity에서 사용
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private AttractionSimpleList foodList;
    private AttractionSimpleList attractionList;

    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;

//    LoginButton facebookLogin;

    public static OAuthLogin mOAuthLoginModule;
    OAuthLoginButton mOAuthLoginButton;

//    private com.kakao.usermgmt.LoginButton kakaoLogin;
    private SessionCallBack mKakaoCallback;

//    EditText et_id, et_pw;
    CheckBox chk_auto;
//    Button btn_signup, btn_login;

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    SignInButton googleSigninBtn;

    ProgressDialog loginProgress = null;
    boolean dialogShowing = false;

    User loginUser;
    String usinglanguage;

    Locale locale;

    private ArrayList<GuidePhoto> guideList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        guideList = bundle.getParcelableArrayList("guides");
        foodList = intent.getParcelableExtra("foods");
        attractionList = intent.getParcelableExtra("attraction");

        new LogManager().LogManager("LoginActivity", "---------------------------");

        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        usinglanguage = locale.getCountry();

//        et_id = findViewById(R.id.et_id);
//        et_pw = findViewById(R.id.et_pw);
        chk_auto = findViewById(R.id.chk_auto);
//        btn_signup = findViewById(R.id.btn_signup);
//        btn_login = findViewById(R.id.btn_login);
//        et_id.setMaxLines(1);
//        et_pw.setMaxLines(1);

        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();
        new LogManager().LogManager("autoLogin체크 됨?",setting.getBoolean("autoLogin",true)+"");
        if(setting.getBoolean("autoLogin", false) ){

            switch (setting.getString("Service","")){
                case"Facebook":
//                    new LogManager().LogManager("auto login","Facebook");
                    loginProgress = ProgressDialog.show(LoginActivity.this, "", setting.getString("Service","")+" Login...", true);
                    dialogShowing = true;
                    User user = new User();
                    user.setName(setting.getString("Nickname",""));
                    user.setProfileAddr(setting.getString("Photo",""));
                    user.setId(setting.getString("ID",""));
                    oAuthLogin(user, "Facebook");
                    break;
                case"Google":
//                    new LogManager().LogManager("auto login","Google");
                    loginProgress = ProgressDialog.show(LoginActivity.this, "",setting.getString("Service","")+ " Login...", true);
                    dialogShowing = true;
                    if(InternetTotalCheck.checkInternet()) {
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build();
                        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                        mAuth = FirebaseAuth.getInstance();
                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//            new LogManager().LogManager("인텐트 전송","전송됨");
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                    }else{
                        Toast.makeText(MyApplication.getContext(),R.string.internet_failed,Toast.LENGTH_LONG).show();
                        loginProgress.dismiss();
                        dialogShowing = false;
                    }
                    break;
                case"Naver":
//                    new LogManager().LogManager("auto login","Naver");
                    loginProgress = ProgressDialog.show(LoginActivity.this, "",setting.getString("Service","")+ " Login...", true);
                    dialogShowing = true;
                    mOAuthLoginModule = OAuthLogin.getInstance();
                    mOAuthLoginModule.init(this, getString(R.string.naver_key), getString(R.string.naver_secret_key), "StoryTour");
                    mOAuthLoginModule.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
                    break;
                case"Kakao":
//                    new LogManager().LogManager("auto login","Kakao");
                    loginProgress = ProgressDialog.show(LoginActivity.this, "", setting.getString("Service","")+" Login...", true);
                    dialogShowing = true;
                    setKakao();
                    break;
                default:

                    break;


            }
        }

        editor.apply();


//        btn_login.setOnClickListener(login);

        googleSigninBtn = findViewById(R.id.google_sign_in_button);
        googleSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new LogManager().LogManager("사인인 버튼 클릭","클릭됨");
                loginProgress = ProgressDialog.show(LoginActivity.this, "", "Google Login...", true);
                dialogShowing = true;
                signIn();
            }
        });

        loginFacebook();

        setNaver();

        Button kakaoAccountLogin;
        kakaoAccountLogin = findViewById(R.id.temp_kakao_login);
        kakaoAccountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setKakao();
            }
        });

        /*mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                LoginActivity.this
                ,loginUser.getId()
                ,loginUser.getProfileAddr()
                ,loginUser.getName()
                //,OAUTH_CALLBACK_INTENT
                // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
        );
        naverLogin = findViewById(R.id.buttonOAuthLoginImg);
        naverLogin.setOAuthLoginHandler(mOAuthNaverLoginHandler);
        naverLogin.setBgResourceId(R.drawable.z_naver_login);*/



//        kakaoLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                new LogManager().LogManager("카톡 로그인","버튼 클릭111");
//                Session session = Session.getCurrentSession();
//                session.addCallback(new SessionCallBack());
//                session.open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
//                new LogManager().LogManager("카톡 로그인","버튼 클릭222");
//                requestMe();
////                mKakaoCallback = new SessionCallBack();
////                com.kakao.auth.Session.getCurrentSession().addCallback(mKakaoCallback);
////                com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();
////
////                com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, LoginActivity.this);
//            }
//        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();


        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/

    }

    private void setKakao(){
        mKakaoCallback = new SessionCallBack();
        Session.getCurrentSession().addCallback(mKakaoCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    private void setNaver() {
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(this, getString(R.string.naver_key), getString(R.string.naver_secret_key), "StoryTour");
        mOAuthLoginButton = findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new LogManager().LogManager("로그인","네이버 클릭");
                loginProgress = ProgressDialog.show(LoginActivity.this, "", "Naver Login...", true);
                dialogShowing = true;
                mOAuthLoginModule.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
            }
        });

        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
//        mOAuthLoginModule.startOauthLoginActivity(this, mOAuthLoginHandler);
    }
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginModule.getAccessToken(LoginActivity.this);
                String refreshToken = mOAuthLoginModule.getRefreshToken(LoginActivity.this);
                long expiresAt = mOAuthLoginModule.getExpiresAt(LoginActivity.this);
                String tokenType = mOAuthLoginModule.getTokenType(LoginActivity.this);

//                new LogManager().LogManager("네이버 로그인: accessToken",accessToken);
//                new LogManager().LogManager("네이버 로그인: refreshToken",refreshToken);
                NaverApiClient.getInstance()
                        .getNaverApiService()
                        .getPersonalInfo("Bearer "+accessToken)
                        .enqueue(new retrofit2.Callback<NaverResponse>() {
                            @Override
                            public void onResponse(Call<NaverResponse> call, Response<NaverResponse> response) {
                                if(response.body() != null){
                                    NaverUser user = response.body().getResponse();
                                    User naverUser = new User();
                                    naverUser.setId(user.getEmail());
                                    naverUser.setName(user.getNickname());
                                    naverUser.setProfileAddr(user.getProfile_image());
                                    oAuthLogin(naverUser,"Naver");
                                }
                            }

                            @Override
                            public void onFailure(Call<NaverResponse> call, Throwable t) {

                            }
                        });
            } else {
                new LogManager().LogManager("네이버 로그인","fail");
                String errorCode = mOAuthLoginModule.getLastErrorCode(LoginActivity.this).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(LoginActivity.this);
                new LogManager().LogManager("네이버 로그인","fail: "+errorCode+" | "+errorDesc);
                Toast.makeText(LoginActivity.this, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        };
    };






    private void loginFacebook(){
        callbackManager = CallbackManager.Factory.create();

        LoginButton facebookLoginBtn = findViewById(R.id.facebook_login_button);
        facebookLoginBtn.setReadPermissions("email","public_profile");
        facebookLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                new LogManager().LogManager("facebook success ",loginResult+"");
                loginProgress = ProgressDialog.show(LoginActivity.this, "", "Facebook Login...", true);
                dialogShowing = true;
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                new LogManager().LogManager("페북로그인","캔슬");
            }

            @Override
            public void onError(FacebookException error) {
                new LogManager().LogManager("페북로그인",error+"");
                Toast.makeText(LoginActivity.this,"Facebook Login failed. You may typed wrond ID/PW",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        new LogManager().LogManager("페북로그인","성공??");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete() && task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Facebook Login Success",Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            oAuthLogin(user, "Facebook");
                        }else if(task.isComplete() && !task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Facebook Login task end but failed",Toast.LENGTH_LONG).show();
                            loginProgress.dismiss();
                            dialogShowing = false;
                        }else if(!task.isComplete()  && task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Facebook Login task success but until work",Toast.LENGTH_LONG).show();
                            loginProgress.dismiss();
                            dialogShowing = false;
                        }else if(!task.isComplete()  && !task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Facebook Login task failed totally",Toast.LENGTH_LONG).show();
                            loginProgress.dismiss();
                            dialogShowing = false;
                        }
                    }
                });
    }




    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        new LogManager().LogManager("onActivityResult","호출됨");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                new LogManager().LogManager("onActivityResult 토큰 메서드 호출","호출됨");
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                new LogManager().LogManager(TAG, "Google sign in failed"+e.toString());
                Alert.makeText(getString(R.string.google_login_failed));
                // [START_EXCLUDE]
//                oAuthLogin(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        new LogManager().LogManager(TAG, "firebaseAuthWithGoogle:" + acct.getId());


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            new LogManager().LogManager(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            new LogManager().LogManager("로그인 메서드 직전","거의 다옴");
                            oAuthLogin(user, "Google");
                        } else {
                            // If sign in fails, display a message to the user.
                            new LogManager().LogManager(TAG, "signInWithCredential:failure"+ task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            oAuthLogin(null);
                        }

                        // [START_EXCLUDE]
//                        loginProgress.dismiss();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        if(InternetTotalCheck.checkInternet()) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            new LogManager().LogManager("인텐트 전송","전송됨");
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }else{
            Toast.makeText(MyApplication.getContext(),R.string.internet_failed,Toast.LENGTH_LONG).show();
        }

    }
    // [END signin]




    private void oAuthLogin(FirebaseUser user, String service){
        new LogManager().LogManager(service+" 로그인 메서드","shared preference saved");
        if(user != null) {
            if(chk_auto.isChecked()){
                new LogManager().LogManager(service+ " 로그인 메서드","shared oAuthLogin checked");
                editor.putString("Nickname",user.getDisplayName());
                editor.putString("Service",service);
                editor.putString("ID",user.getEmail());
                editor.putBoolean("autoLogin",true);
                editor.putString("Photo", user.getPhotoUrl().toString());
                editor.commit();
            }
            int serviceCode = 1;
            switch(service){
                case "Facebook":
                    serviceCode = 2;
                    break;
                case "Google":
                    serviceCode = 4;
                    break;
                case "Naver":
                    serviceCode = 3;
                    break;
                case "Kakao":
                    serviceCode = 5;
                    break;
            }
            User user1 = new User();
            user1.setName(user.getDisplayName());
            user1.setId(user.getEmail());
            user1.setService(service);
            user1.setServiceCode(serviceCode);
            user1.setNationString(usinglanguage);
            user1.setProfileAddr(user.getPhotoUrl().toString());
//                new AsyncTaskOauthLogin().execute(user1);
            throwIntent(user1);
//                new LogManager().LogManager("oAuthLogin",user1.getProfileAddr()+"/////////////");

        }else{
            Alert.makeText(getString(R.string.google_login_failed));
        }
    }

    private void oAuthLogin(@NonNull  User user, String service){
        new LogManager().LogManager(service+ " 로그인 메서드","shared preference saved");
//        new LogManager().LogManager("oAuthLogin id: ",user.getId());
//        new LogManager().LogManager("oAuthLogin name: ",user.getName());
//        new LogManager().LogManager("oAuthLogin photo: ",user.getProfileAddr());
        if(chk_auto.isChecked()){
            new LogManager().LogManager(service+ " 로그인 메서드","shared oAuthLogin checked");
            editor.putString("Nickname",user.getName());
            editor.putString("Service",service);
            editor.putString("ID",user.getId());
            editor.putString("Photo", user.getProfileAddr());
            editor.putBoolean("autoLogin",true);
            editor.commit();
        }
        int serviceCode = 1;
        switch(service){
            case "Facebook":
                serviceCode = 2;
                break;
            case "Google":
                serviceCode = 4;
                break;
            case "Naver":
                serviceCode = 3;
                break;
            case "Kakao":
                serviceCode = 5;
                break;
        }
        User user1 = new User();
        user1.setName(user.getName());
        user1.setServiceCode(serviceCode);
        user1.setId(user.getId());
        user1.setService(service);
        user1.setNationString(usinglanguage);
        user1.setProfileAddr(user.getProfileAddr());
//                new AsyncTaskOauthLogin().execute(user1);
        throwIntent(user1);
//                new LogManager().LogManager("oAuthLogin",user1.getProfileAddr()+"/////////////");

    }

    public void throwIntent(User user){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        new LogManager().LogManager("throwIntent | Userid()", user.getId()+"/////////////");
        new LogManager().LogManager("throwIntent | UserProfile()",user.getProfileAddr()+"/////////////");
        if(user.getId().length() > 0){
            new LogManager().LogManager("user in Login", user.getId());
            intent.putExtra("user",user);
        }else{
            Alert.makeText(getString(R.string.login_failed));
        }
        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList("guides",guideList);
        intent.putExtras(bundle);
        intent.putExtra("foods",foodList);
        intent.putExtra("attraction",attractionList);
        intent.putExtra("user",user);

//        else{
//            Log.e("user in Login", "admin");
//            user.userid = "admin";
//            intent.putExtra("user",user);
//        }

        sendLogin(user);
        startActivity(intent);
        finish();
        if(dialogShowing&&loginProgress.isShowing()) {
            loginProgress.dismiss();
            new LogManager().LogManager("로그인엑티비티","디엔드");
            dialogShowing = false;
        }
    }

    public void sendLogin(User user){
        ApiClient.getInstance().getApiService()
                .signup(MyApplication.APP_VERSION, user)
                .enqueue(new Callback<ApiMessasge>() {
                    @Override
                    public void onResponse(Call<ApiMessasge> call, Response<ApiMessasge> response) {
                        if (response.body() != null) {
                            int resultCode = response.body().getCode();

                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("스엑티비티 Tours", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiMessasge> call, Throwable t) {
                        Alert.makeText(getString(R.string.network_error));

                    }
                });
    }






    public class SessionCallBack implements ISessionCallback {

        // 로그인에 성공한 상태
        @Override
        public void onSessionOpened() {            requestMe();        }

        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            new LogManager().LogManager("카톡 로그인","SessionCallBack onSessionOpenFailed"+ exception.getMessage());
        }

        // 사용자 정보 요청
        public void requestMe() {
            // 사용자정보 요청 결과에 대한 Callback
            UserManagement.requestMe(new MeResponseCallback() {
                // 세션 오픈 실패. 세션이 삭제된 경우,
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    new LogManager().LogManager("카톡 로그인","SessionCallBack onSessionClosed"+ errorResult.getErrorMessage());
                }
                // 회원이 아닌 경우,
                @Override
                public void onNotSignedUp() {
                    new LogManager().LogManager("카톡 로그인","SessionCallBack onNotSignedUp");
                }
                // 사용자정보 요청에 성공한 경우,
                @Override
                public void onSuccess(UserProfile userProfile) {
                    new LogManager().LogManager("카톡 로그인","SessionCallBack 성공");
                    String nickname = userProfile.getNickname();
                    String email = userProfile.getEmail();
                    String profileImagePath = userProfile.getProfileImagePath();
                    String thumnailPath = userProfile.getThumbnailImagePath();
                    String UUID = userProfile.getUUID();
                    long id = userProfile.getId();

                    Log.e("Profile : ", nickname + "");
                    Log.e("Profile : ", email + "");
                    Log.e("Profile : ", profileImagePath  + "");
                    Log.e("Profile : ", thumnailPath + "");
                    Log.e("Profile : ", UUID + "");
                    Log.e("Profile : ", id + "");

                    User user = new User();
                    user.setId(id+"");
                    user.setName(nickname);
                    user.setProfileAddr(thumnailPath);
                    new LogManager().LogManager("kakao User id: ",user.getId());
                    new LogManager().LogManager("kakao User name: ",user.getName());
                    new LogManager().LogManager("kakao User pic",user.getProfileAddr());

                    oAuthLogin(user, "Kakao");


                }
                // 사용자 정보 요청 실패
                @Override
                public void onFailure(ErrorResult errorResult) {
                    new LogManager().LogManager("카톡 로그인","SessionCallBack onFailure"+errorResult.getErrorMessage());
                }

            });

        }

    }


    /*private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
               new LogManager().LogManager("kakao",exception+"");
            }
        }
    }*/

    /*protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }*/

    /*public void loginFailure(int type){
        switch(type){
            case 1:
                Toast.makeText(this, "Your ID id duplicated with others. please use another one.", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "You typed wrong ID or PW, Please do it again or just sign up with new ID.", Toast.LENGTH_SHORT).show();
                break;
        }
//        et_id.setText("");
//        et_pw.setText("");
    }*/

    // [START on_start_check_user]
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        oAuthLogin(currentUser);
//    }
    // [END on_start_check_user]


    /*View.OnClickListener login = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            loginUser = new User();
//            loginUser.setId(et_id.getText().toString().trim());
//            loginUser.setName(et_id.getText().toString().trim());
            loginUser.setService("On-Trip Korea");
//            throwIntent(loginUser);
            if(loginUser.getId().length() < 4 ){
                Toast.makeText(LoginActivity.this, "Your ID/PW should be longer than 4 digit.",Toast.LENGTH_LONG).show();
            }else if(loginUser.getId().length() > 100 ){
                Toast.makeText(LoginActivity.this, "Your ID/PW should be shorter than 100 digit.",Toast.LENGTH_LONG).show();
            }else{
                if(InternetTotalCheck.checkInternet()) {
//                    new AsyncTaskLogin().execute(loginUser);
                    //여기에 retrofit 생성해서 넣기
                }else{
                    Toast.makeText(MyApplication.getContext(),R.string.internet_failed,Toast.LENGTH_LONG).show();
                }

            }
        }
    };*/


}

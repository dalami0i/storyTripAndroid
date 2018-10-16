package com.tripkorea.on.ontripkorea;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
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
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.InternetTotalCheck;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;
import com.tripkorea.on.ontripkorea.vo.user.LoginResponse;
import com.tripkorea.on.ontripkorea.vo.user.LoginUser;
import com.tripkorea.on.ontripkorea.vo.user.Me;
import com.tripkorea.on.ontripkorea.vo.user.SignupResponsecode;
import com.tripkorea.on.ontripkorea.vo.user.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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


    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient; //google
    private CallbackManager callbackManager; //facebook
    private AccessToken token; //facebook


    public static OAuthLogin mOAuthLoginModule; //naver
    private OAuthLoginButton mOAuthLoginButton; //naver

    private SessionCallBack mKakaoCallback; //kakao

    CheckBox chk_auto;

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    SignInButton googleSigninBtn;

    ProgressDialog loginProgress = null;
    boolean dialogShowing = false;

    String usinglanguage;

    Locale locale;
    double lat, lon;
    int page;
    private AttractionSimpleList recommendations;

    boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        TextView logoTxt = findViewById(R.id.txt_splash_logo);
        TextView logo2Txt = findViewById(R.id.txt_splash_logo2);

        Typeface geBody = Typeface.createFromAsset(getAssets(), "fonts/GeBody.ttf");
        logoTxt.setTypeface(geBody);
        logo2Txt.setTypeface(geBody);

        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat",37.579108);
        lon = intent.getDoubleExtra("lon",126.990957);
        recommendations = intent.getParcelableExtra("recommendations");
        page = intent.getIntExtra("page",1);

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
                    new LogManager().LogManager("auto login","Facebook");
                    loginProgress = ProgressDialog.show(LoginActivity.this, "", setting.getString("Service","")+" Login...", true);
                    dialogShowing = true;
                    User user = new User();
                    user.setName(setting.getString("Nickname",""));
                    user.setProfileImgAddr(setting.getString("ProfileImgAddr",""));
                    user.setId(setting.getString("ID",""));
                    user.setSnsIdx(2);
                    isFirst = false;
                    oAuthLogin(user);
                    break;
                case"Google":
                    new LogManager().LogManager("auto login","Google");
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
                        new LogManager().LogManager("인텐트 전송","전송됨");
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                        isFirst = false;
                    }else{
                        Toast.makeText(MyApplication.getContext(),R.string.internet_failed,Toast.LENGTH_LONG).show();
                        loginProgress.dismiss();
                        dialogShowing = false;
                    }
                    break;
                case"Naver":
                    new LogManager().LogManager("auto login","Naver");
                    loginProgress = ProgressDialog.show(LoginActivity.this, "",setting.getString("Service","")+ " Login...", true);
                    dialogShowing = true;
                    mOAuthLoginModule = OAuthLogin.getInstance();
                    mOAuthLoginModule.init(this, getString(R.string.naver_key), getString(R.string.naver_secret_key), "StoryTour");
                    mOAuthLoginModule.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
                    isFirst = false;
                    break;
                case"Kakao":
                    new LogManager().LogManager("auto login","Kakao");
                    loginProgress = ProgressDialog.show(LoginActivity.this, "", setting.getString("Service","")+" Login...", true);
                    dialogShowing = true;
                    setKakao();
                    isFirst = false;
                    break;


            }
        }

        editor.apply();


//        btn_login.setOnClickListener(login);

        googleSigninBtn = findViewById(R.id.google_sign_in_button);
        googleSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogManager().LogManager("사인인 버튼 클릭","클릭됨");
                loginProgress = ProgressDialog.show(LoginActivity.this, "", "Google Login...", true);
                dialogShowing = true;
                signIn();
            }
        });

        token = AccessToken.getCurrentAccessToken();
        if (token == null) {
            //Means user is not logged in
            loginFacebook();
        }else{
            LoginManager.getInstance().logOut();
            loginFacebook();
        }


        setNaver();

        if(Session.getCurrentSession().checkAndImplicitOpen()) {
            new LogManager().LogManager("LoginActivity","onCreate if(Session.getCurrentSession().checkAndImplicitOpen())");
            mKakaoCallback = new SessionCallBack();
            Session.getCurrentSession().addCallback(mKakaoCallback);
        }else{
            new LogManager().LogManager("LoginActivity","onCreate else");
            mKakaoCallback = new SessionCallBack();
            Session.getCurrentSession().addCallback(mKakaoCallback);
        }





        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();




    }

    private void setKakao(){
        mKakaoCallback = new SessionCallBack();
        new LogManager().LogManager("LoginActivity","kakao clicked");
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
    User naverUser;
    JSONObject result;
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {

            if (success) {

                new Thread(){
                    public void run(){
                        String accessToken = mOAuthLoginModule.getAccessToken(LoginActivity.this);
                        String refreshToken = mOAuthLoginModule.getRefreshToken(LoginActivity.this);
                        long expiresAt = mOAuthLoginModule.getExpiresAt(LoginActivity.this);
                        String tokenType = mOAuthLoginModule.getTokenType(LoginActivity.this);

                        String data  = mOAuthLoginModule.requestApi(LoginActivity.this, accessToken, "https://openapi.naver.com/v1/nid/me");
                        try{
                            result = new JSONObject(data);
                            new LogManager().LogManager("네이버 로그인 회신 데이터",result.toString());
                            naverUser = new User();
                            naverUser.setProfileImgAddr(result.getJSONObject("response").getString("profile_image"));
                            //profile_image, email
//                                    naverUser.setProfileAddr(user.getProfile_image());
                            naverUser.setSnsIdx(3);
                            naverUser.setId(result.getJSONObject("response").getString("email"));
                            naverUser.setName(result.getJSONObject("response").getString("nickname"));

                            oAuthLogin(naverUser);

                        }catch (JSONException e){
                            if(naverUser.getName()==null) {
                                try {
                                    naverUser.setName("네이버 유저: " + result.getJSONObject("response").getString("id"));
                                } catch (JSONException e1) {
                                    new LogManager().LogManager("네이버로그인 JSONException 에러", e.toString());
                                    if (dialogShowing && loginProgress.isShowing()) {
                                        loginProgress.dismiss();
                                        dialogShowing = false;
                                    }
                                }
                            }
                            naverUser.setSnsIdx(3);
                            oAuthLogin(naverUser);
                        }catch (Exception e){
                            new LogManager().LogManager("네이버로그인 에러",e.toString());
                            if(dialogShowing  && loginProgress.isShowing()){
                                loginProgress.dismiss();
                                dialogShowing = false;
                            }
                        }
                    }
                }.start();



            } else {
                new LogManager().LogManager("네이버 로그인","fail");
                String errorCode = mOAuthLoginModule.getLastErrorCode(LoginActivity.this).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(LoginActivity.this);
                new LogManager().LogManager("네이버 로그인","fail: "+errorCode+" | "+errorDesc);
                Toast.makeText(LoginActivity.this, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                if(dialogShowing  && loginProgress.isShowing()){
                    loginProgress.dismiss();
                    dialogShowing = false;
                }
            }
        };
    };
/*NaverApiClient.getInstance()
                        .getNaverApiService()
                        .getPersonalInfo("Bearer "+accessToken)
                        .enqueue(new retrofit2.Callback<NaverResponse>() {
                            @Override
                            public void onResponse(Call<NaverResponse> call, Response<NaverResponse> response) {
                                if(response.body() != null){
                                    NaverResponse naverResponse = response.body();
                                    NaverUser user = response.body().getResponse();

                                }
                            }

                            @Override
                            public void onFailure(Call<NaverResponse> call, Throwable t) {
                                if(dialogShowing && loginProgress.isShowing()){
                                    loginProgress.dismiss();
                                    dialogShowing = false;
                                }
                            }
                        });*/






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
                            User userData = new User();
                            userData.setId(user.getEmail());
                            userData.setSnsIdx(2);
                            userData.setName(user.getDisplayName());
                            /*if(user.getPhotoUrl() != null) {
                                userData.setProfileImgAddr(user.getPhotoUrl().toString());
                            }*/
                            oAuthLogin(userData);
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
        new LogManager().LogManager("onActivityResult","resultCode: "+resultCode);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            new LogManager().LogManager("if (requestCode == RC_SIGN_IN)","task.toString(): "+task.toString());
            new LogManager().LogManager("if (requestCode == RC_SIGN_IN)","task.isSuccessful(): "+task.isSuccessful());
            new LogManager().LogManager("if (requestCode == RC_SIGN_IN)","task.isComplete(): "+task.isComplete());

            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                new LogManager().LogManager("onActivityResult 토큰 메서드 호출","호출됨");
                new LogManager().LogManager("onActivityResult 토큰 메서드 호출","account.getDisplayName(): "+account.getDisplayName());
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                if(task.getException() != null) {
                    new LogManager().LogManager("if (requestCode == RC_SIGN_IN)", "task.getException().toString(): " + task.getException().toString());
                    new LogManager().LogManager("if (requestCode == RC_SIGN_IN)", "task.getException().getLocalizedMessage(): " + task.getException().getLocalizedMessage());
                    new LogManager().LogManager("if (requestCode == RC_SIGN_IN)", "task.getException().getMessage(): " + task.getException().getMessage());
                    new LogManager().LogManager("if (requestCode == RC_SIGN_IN)", "task.getException(): " + task.getException());
                }
                new LogManager().LogManager(TAG, "Google sign in failed : "+e.toString());
                new LogManager().LogManager(TAG, "Google sign in failed : e.getStatusCode()"+e.getStatusCode());
                new LogManager().LogManager(TAG, "Google sign in failed : e.getCause()"+e.getCause());
                new LogManager().LogManager(TAG, "Google sign in failed : e.getLocalizedMessage()"+e.getLocalizedMessage());
                new LogManager().LogManager(TAG, "Google sign in failed : e.getMessage()"+e.getMessage());
                new LogManager().LogManager(TAG, "Google sign in failed : e.getStatusMessage()"+e.getStatusMessage());
                Alert.makeText(getString(R.string.google_login_failed));
                if(loginProgress.isShowing()){
                    loginProgress.dismiss();
                    dialogShowing= false;
                }
                // [START_EXCLUDE]
//                oAuthLogin(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        new LogManager().LogManager(TAG, "firebaseAuthWithGoogle:"+" acct.getDisplayName(): "+acct.getDisplayName());
        new LogManager().LogManager(TAG, "firebaseAuthWithGoogle:"+" acct.getPhotoUrl(): "+acct.getPhotoUrl());

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
                            User userData = new User();
                            userData.setId(user.getEmail());
                            userData.setSnsIdx(4);
                            userData.setName(user.getDisplayName());
                            new LogManager().LogManager(TAG, "firebaseAuthWithGoogle:"+" user.getDisplayName(): "+user.getDisplayName());
                            new LogManager().LogManager(TAG, "firebaseAuthWithGoogle:"+" user.getPhotoUrl(): "+user.getPhotoUrl());
                            if(user.getPhotoUrl() != null) {
                                userData.setProfileImgAddr(user.getPhotoUrl().toString());
                            }
                            oAuthLogin(userData);
                        } else {
                            // If sign in fails, display a message to the user.
                            new LogManager().LogManager(TAG, "signInWithCredential:failure"+ task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            if(loginProgress.isShowing()){
                                loginProgress.dismiss();
                                dialogShowing= false;
                            }
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
            new LogManager().LogManager("LoginActivity signIn()","인텐트 전송 전송됨");
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }else{
            Toast.makeText(MyApplication.getContext(),R.string.internet_failed,Toast.LENGTH_LONG).show();
            if(loginProgress.isShowing()){
                loginProgress.dismiss();
                dialogShowing= false;
            }
        }

    }
    // [END signin]


    private void oAuthLogin(@NonNull  User user){
        new LogManager().LogManager("로그인엑티비티","oAuthLogin(@NonNull  User user) 진입 - user.getSnsIdx(): "+user.getSnsIdx());
        switch (user.getSnsIdx()){
            case 5:
                new LogManager().LogManager("로그인엑티비티","switch (user.getSnsIdx()): "+user.getSnsIdx());
                if(!loginProgress.isShowing() && !dialogShowing && !isFinishing()){
                    new LogManager().LogManager("로그인엑티비티","if(!loginProgress.isShowing() && !dialogShowing && !isFinishing())");
                    dialogShowing = true;
                    loginProgress = ProgressDialog.show(LoginActivity.this, "", "Stoury Login...", true);
                    new LogManager().LogManager("로그인엑티비티","oAuthLogin  user.getId(): "+user.getId()
                            +" | user.getName(): "+user.getName()
                            +" | user.getProfileImgAddr(): "+user.getProfileImgAddr());
                    String serviceCode ="";
                    switch(user.getSnsIdx()){
                        case 2:
                            serviceCode = "Facebook";
                            break;
                        case 4:
                            serviceCode = "Google";
                            break;
                        case 3:
                            serviceCode = "Naver";
                            break;
                        case 5:
                            serviceCode = "Kakao";
                            break;
                    }
                    new LogManager().LogManager(serviceCode+ " 로그인 메서드","shared preference saved");
//        new LogManager().LogManager("oAuthLogin id: ",user.getId());
//        new LogManager().LogManager("oAuthLogin name: ",user.getName());
//        new LogManager().LogManager("oAuthLogin photo: ",user.getProfileAddr());
                    if(chk_auto.isChecked()){
                        new LogManager().LogManager(user.getSnsIdx()+ " 로그인 메서드","shared oAuthLogin checked");
                        if(user.getName() != null) {
                            editor.putString("Nickname", user.getName());
                        }
                        if(user.getProfileImgAddr() != null){
                            editor.putString("ProfileImgAddr", user.getProfileImgAddr());
                        }
                        editor.putString("Service",serviceCode);
                        editor.putString("ID",user.getId());
//            editor.putString("Photo", user.getProfileAddr());
                        editor.putBoolean("autoLogin",true);
                        editor.commit();
                    }
//        sendLogin(user);
                    if(isFirst) {
                        sendSignup(user);
                    }else{
                        sendLogin(user);
                    }
                }
                break;
            default:
                new LogManager().LogManager("로그인엑티비티","switch (user.getSnsIdx()): "+user.getSnsIdx());
                if(!loginProgress.isShowing() && !dialogShowing ){
                    new LogManager().LogManager("로그인엑티비티","if(!loginProgress.isShowing() && !dialogShowing && !isFinishing())");
                    dialogShowing = true;
                    loginProgress = ProgressDialog.show(LoginActivity.this, "", "Stoury Login...", true);

                }

                new LogManager().LogManager("로그인엑티비티","oAuthLogin  user.getId(): "+user.getId()
                        +" | user.getName(): "+user.getName()
                        +" | user.getProfileImgAddr(): "+user.getProfileImgAddr());
                String serviceCode ="";
                switch(user.getSnsIdx()){
                    case 2:
                        serviceCode = "Facebook";
                        break;
                    case 4:
                        serviceCode = "Google";
                        break;
                    case 3:
                        serviceCode = "Naver";
                        break;
                    case 5:
                        serviceCode = "Kakao";
                        break;
                }
                new LogManager().LogManager(serviceCode+ " 로그인 메서드","shared preference saved");
//        new LogManager().LogManager("oAuthLogin id: ",user.getId());
//        new LogManager().LogManager("oAuthLogin name: ",user.getName());
//        new LogManager().LogManager("oAuthLogin photo: ",user.getProfileAddr());
                if(chk_auto.isChecked()){
                    new LogManager().LogManager(user.getSnsIdx()+ " 로그인 메서드","shared oAuthLogin checked");
                    if(user.getName() != null) {
                        editor.putString("Nickname", user.getName());
                    }
                    if(user.getProfileImgAddr() != null){
                        editor.putString("ProfileImgAddr", user.getProfileImgAddr());
                    }
                    editor.putString("Service",serviceCode);
                    editor.putString("ID",user.getId());
//            editor.putString("Photo", user.getProfileAddr());
                    editor.putBoolean("autoLogin",true);
                    editor.commit();
                }
//        sendLogin(user);
                if(isFirst) {
                    sendSignup(user);
                }else{
                    sendLogin(user);
                }
                break;
        }



    }

    public void sendSignup(final User user){
        new LogManager().LogManager("LoginActivity","sendSignup user.getId(): "+user.getId()
                +"| user.getName(): "+user.getName()
                +"| user.getProfileImgAddr(): "+user.getProfileImgAddr());
        ApiClient.getInstance().getApiService()
                .signup(MyApplication.APP_VERSION, user)
                .enqueue(new Callback<SignupResponsecode>() {
                    @Override
                    public void onResponse(Call<SignupResponsecode> call, Response<SignupResponsecode> response) {
                        if (response.body() != null) {
                            sendLogin(user);
                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("스엑티비티 sendSignup", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SignupResponsecode> call, Throwable t) {
                        Alert.makeText(getString(R.string.network_error));

                    }
                });
    }

    public void sendLogin(final User user){

        LoginUser loginUser = new LoginUser();
        loginUser.setId(user.getId());
        loginUser.setSnsIdx(user.getSnsIdx());
        String pw = user.getSnsIdx()+user.getId()+user.getSnsIdx();
        loginUser.setPasswd(pw);
        new LogManager().LogManager("LoginActivity","sendLogin user.getId(): "+user.getId()
                +" | user.getSnsIdx(): "+user.getSnsIdx() + " | pw: "+pw);
        ApiClient.getInstance().getApiService()
                .signin(MyApplication.APP_VERSION, loginUser)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.body() != null) {
                            LoginResponse loginResponse = response.body();
                            if(loginResponse.getMessage().equals("Success")){

                                Me.instance.setUserIdx(loginResponse.getUserIdx());
                                Me.instance.setId(loginResponse.getId());
                                Me.instance.setName(loginResponse.getName());
                                if(loginResponse.getProfileImgAddr() != null){
                                    Me.instance.setProfileImgAddr(loginResponse.getProfileImgAddr());
                                }
                                new LogManager().LogManager("LoginActivity","sendLogin loginResponse.getName(): "+loginResponse.getName()
                                        +"Me.instance.getName(): "+ Me.instance.getName());

                                throwIntent(loginResponse);
                            }else{
                                Alert.makeText("Login failed. Please try again.");
                                if(loginProgress != null && loginProgress.isShowing()) loginProgress.dismiss();
                            }

                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("스엑티비티 sendLogin", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Alert.makeText(getString(R.string.network_error));

                    }
                });
    }



    public void throwIntent(LoginResponse loginResponse){
        Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
        new LogManager().LogManager("throwIntent | loginResponse.getId()", loginResponse.getId()+"/////////////");
        if(loginResponse.getId().length() > 0){
            new LogManager().LogManager("user in Login", loginResponse.getId());
            intent.putExtra("user",loginResponse);
        }else{
            Alert.makeText(getString(R.string.login_failed));
        }
        intent.putExtra("lat",lat);
        intent.putExtra("lon",lon);
        intent.putExtra("page",page);
        intent.putExtra("recommendations",recommendations);
        intent.putExtra("user",loginResponse);


        startActivity(intent);
        finish();
        if(dialogShowing&&loginProgress.isShowing()) {
            loginProgress.dismiss();
            new LogManager().LogManager("로그인엑티비티","디엔드");
            dialogShowing = false;
        }
    }








    public class SessionCallBack implements ISessionCallback {

        boolean firstKakaoLogin;

        // 로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            new LogManager().LogManager("카톡 로그인","onSessionOpened()");
            if(!dialogShowing && loginProgress == null) {
                loginProgress = ProgressDialog.show(LoginActivity.this, "", "Kakao Login...", true);
                dialogShowing = true;
                firstKakaoLogin = true;
            }
            requestMe();
        }

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
                    if(dialogShowing && loginProgress.isShowing() && firstKakaoLogin){
                        dialogShowing = false;
                        loginProgress.dismiss();
                        firstKakaoLogin = false;
                    }
                }
                // 회원이 아닌 경우,
                @Override
                public void onNotSignedUp() {
                    new LogManager().LogManager("카톡 로그인","SessionCallBack onNotSignedUp");
                    if(dialogShowing && loginProgress.isShowing()){
                        dialogShowing = false;
                        loginProgress.dismiss();
                    }
                }
                // 사용자정보 요청에 성공한 경우,
                @Override
                public void onSuccess(UserProfile userProfile) {
                    new LogManager().LogManager("카톡 로그인","SessionCallBack 성공");
                    String nickname = userProfile.getNickname().trim();
                    String email = userProfile.getEmail();
                    String profileImagePath = userProfile.getProfileImagePath();
                    String thumnailPath = userProfile.getThumbnailImagePath();
                    String UUID = userProfile.getUUID();
                    long id = userProfile.getId();

                    Log.e("Profile : ", "nickname: "+nickname + "");
                    Log.e("Profile : ", "email: "+email + "");
                    Log.e("Profile : ", "profileImagePath: "+profileImagePath  + "");
                    Log.e("Profile : ", "thumnailPath: "+thumnailPath + "");
                    Log.e("Profile : ", "UUID: "+UUID + "");
                    Log.e("Profile : ", "id: "+id + "");

                    User user = new User();
                    user.setId(id+"");
                    user.setName(nickname);
                    user.setProfileImgAddr(thumnailPath);
                    user.setSnsIdx(5);

                    new LogManager().LogManager("kakao User id: ",user.getId());
                    new LogManager().LogManager("kakao User name: ",user.getName());

                    if(dialogShowing && loginProgress.isShowing()){
                        dialogShowing = false;
                        loginProgress.dismiss();
                    }

                    oAuthLogin(user);


                }
                // 사용자 정보 요청 실패
                @Override
                public void onFailure(ErrorResult errorResult) {
                    new LogManager().LogManager("카톡 로그인","SessionCallBack onFailure"+errorResult.getErrorMessage());
                    if(dialogShowing && loginProgress.isShowing()){
                        dialogShowing = false;
                        loginProgress.dismiss();
                    }
                }

            });

        }

    }





}

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

/*Button kakaoAccountLogin;
        kakaoAccountLogin = findViewById(R.id.temp_kakao_login);
        kakaoAccountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setKakao();
            }
        });*/

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
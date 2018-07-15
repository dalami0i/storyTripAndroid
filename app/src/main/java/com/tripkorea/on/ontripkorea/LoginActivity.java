package com.tripkorea.on.ontripkorea;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.util.InternetTotalCheck;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.user.User;

import okhttp3.MediaType;


/**
 * Created by Edward Won on 2018-07-13.*/


public class LoginActivity extends AppCompatActivity {
    private static final MediaType JSON = MediaType.parse("application/jsonarray; charset=utf-8");//전송 위해 JSON type 설정. 다른 activity에서 사용
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
//    private CallbackManager callbackManager;

    EditText et_id, et_pw;
    CheckBox chk_auto;
    Button btn_signup, btn_login;

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    SignInButton googleSigninBtn;

    ProgressDialog loginProgress = null;
    boolean dialogShowing = false;

    User loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new LogManager().LogManager("LoginActivity", "---------------------------");

        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        chk_auto = findViewById(R.id.chk_auto);
        btn_signup = findViewById(R.id.btn_signup);
        btn_login = findViewById(R.id.btn_login);
        et_id.setMaxLines(1);
        et_pw.setMaxLines(1);

        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();
        User user = new User();
        if(setting.getBoolean("chk_auto", false) ){
            loginProgress = ProgressDialog.show(LoginActivity.this, "", "Login...", true);
            if(setting.getString("Service","").equals("mylittleseoul")){
                user.setId( setting.getString("ID", "") );
                user.setService(setting.getString("Service",""));

            }else{
                user.setId( setting.getString("ID", "") );
                user.setProfileAddr(setting.getString("Photo",""));
                user.setService(setting.getString("Service",""));

            }
        }

        editor.apply();


        btn_login.setOnClickListener(login);

        googleSigninBtn = findViewById(R.id.google_sign_in_button);
        googleSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogManager().LogManager("사인인 버튼 클릭","클릭됨");
                signIn();
            }
        });

//        loginFacebook();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

    }



    View.OnClickListener login = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            loginUser = new User();
            loginUser.setId(et_id.getText().toString().trim());
            loginUser.setName(et_id.getText().toString().trim());
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
    };

//    private void loginFacebook(){
//        callbackManager = CallbackManager.Factory.create();
//        LoginButton facebookLoginBtn = findViewById(R.id.facebook_login_button);
//        facebookLoginBtn.setReadPermissions("email","public_profile");
//        facebookLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                new LogManager().printLogManager("facebook success ",loginResult+"");
//                loginProgress = ProgressDialog.show(LoginActivity.this, "", "Login...", true);
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Toast.makeText(LoginActivity.this,"Facebook Login failed. You may typed wrond ID/PW",Toast.LENGTH_LONG).show();
//            }
//        });
//    }

//    private void handleFacebookAccessToken(AccessToken token){
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isComplete() && task.isSuccessful()){
//                            Toast.makeText(LoginActivity.this,"Facebook Login Success",Toast.LENGTH_LONG).show();
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            oAuthLogin(user);
//                        }else if(task.isComplete() && !task.isSuccessful()){
//                            Toast.makeText(LoginActivity.this,"Facebook Login task end but failed",Toast.LENGTH_LONG).show();
//                            loginProgress.dismiss();
//                        }else if(!task.isComplete()  && task.isSuccessful()){
//                            Toast.makeText(LoginActivity.this,"Facebook Login task success but until work",Toast.LENGTH_LONG).show();
//                            loginProgress.dismiss();
//                        }else if(!task.isComplete()  && !task.isSuccessful()){
//                            Toast.makeText(LoginActivity.this,"Facebook Login task failed totally",Toast.LENGTH_LONG).show();
//                            loginProgress.dismiss();
//                        }
//                    }
//                });
//    }


    // [START on_start_check_user]
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        oAuthLogin(currentUser);
//    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        new LogManager().LogManager("인텐트 회신","회신됨");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                new LogManager().LogManager("토큰 메서드 호출","호출됨");
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                new LogManager().LogManager(TAG, "Google sign in failed"+e.toString());
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
        // [START_EXCLUDE silent]
        loginProgress = ProgressDialog.show(LoginActivity.this, "", "Login...", true);
        dialogShowing = true;
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            new LogManager().LogManager(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            new LogManager().LogManager("로그인 메서드 직전","거의 다옴");
                            oAuthLogin(user);
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










    public void loginFailure(int type){
        switch(type){
            case 1:
                Toast.makeText(this, "Your ID id duplicated with others. please use another one.", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "You typed wrong ID or PW, Please do it again or just sign up with new ID.", Toast.LENGTH_SHORT).show();
                break;
        }
        et_id.setText("");
        et_pw.setText("");
    }

    private void oAuthLogin(FirebaseUser user){
        new LogManager().LogManager("로그인 메서드","shared preference saved");
        if(user != null) {
            if(chk_auto.isChecked()){
                editor.putString("Nickname",user.getDisplayName());
                editor.putString("Service","Google");
                editor.putString("ID",user.getEmail());
                editor.putString("Photo", user.getPhotoUrl().toString());
                editor.commit();

                User user1 = new User();
                user1.setName(user.getDisplayName());
                user1.setId(user.getEmail());
                user1.setProfileAddr(user.getPhotoUrl().toString());
//                new AsyncTaskOauthLogin().execute(user1);
                login(user1);
                new LogManager().LogManager("oAuthLogin",user1.getProfileAddr()+"/////////////");
            }else{
                User user1 = new User();
                user1.setName(user.getDisplayName());
                user1.setId(user.getEmail());
                user1.setProfileAddr(user.getPhotoUrl().toString());
//                new AsyncTaskOauthLogin().execute(user1);
                login(user1);
                new LogManager().LogManager("oAuthLogin",user1.getProfileAddr()+"/////////////");
            }

        }else{
            Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }



    public void login(User user){
        if(chk_auto.isChecked()){
            editor.putString("ID", user.getId());
            editor.putString("Service","mylittleseoul");
            editor.putBoolean("chk_auto", true);
            editor.commit();

        }else{
            editor.putBoolean("chk_auto", false);
            editor.clear();
            editor.commit();

        }

        new LogManager().LogManager("login | Userid()", user.getId()+"/////////////");
        new LogManager().LogManager("login | UserProfile()",user.getProfileAddr()+"/////////////");
        throwIntent(user);

    }

    public void throwIntent(User user){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        new LogManager().LogManager("throwIntent | Userid()", user.getId()+"/////////////");
        new LogManager().LogManager("throwIntent | UserProfile()",user.getProfileAddr()+"/////////////");
        if(user.getId().length() > 0){
            new LogManager().LogManager("user in Login", user.getId());
            intent.putExtra("user",user);
        }
//        else{
//            Log.e("user in Login", "admin");
//            user.userid = "admin";
//            intent.putExtra("user",user);
//        }
        startActivity(intent);
        finish();
        if(dialogShowing&&loginProgress.isShowing()) {
            loginProgress.dismiss();
            new LogManager().LogManager("로그인엑티비티","디엔드");
            dialogShowing = false;
        }
    }




}

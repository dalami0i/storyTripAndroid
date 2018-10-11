package com.tripkorea.on.ontripkorea.tabs.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.retrofit.message.ApiMessage;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.user.Me;
import com.tripkorea.on.ontripkorea.vo.user.UpdateUser;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editName;
    private EditText editAge;
    private EditText editGender;
    private EditText editCountry;
    private RoundedImageView imgMypageProfileEd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        imgMypageProfileEd = findViewById(R.id.img_mypage_profile_pic_ed);
        Glide.with(MyApplication.getContext())
                .load(Me.instance.getProfileImgAddr())
                .into(imgMypageProfileEd);

        TextView cancelBtn = findViewById(R.id.tv_edit_cancel);
        cancelBtn.setOnClickListener(this);
        TextView confirmBtn = findViewById(R.id.tv_edit_confirm);
        confirmBtn.setOnClickListener(this);
        editName = findViewById(R.id.edittext_mypage_profile_name_ed);
        editName.setText(Me.getInstance().getName());

        editAge = findViewById(R.id.edittext_mypage_profile_age_ed);
        editAge.setActivated(false);
        editGender = findViewById(R.id.edittext_mypage_profile_gender_ed);
        editGender.setActivated(false);
        editCountry = findViewById(R.id.edittext_mypage_profile_nationality_ed);
        editCountry.setActivated(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_edit_cancel:
                finish();
                break;
            case R.id.tv_edit_confirm:
                if(!Me.getInstance().getName().equals(editName.getText().toString())){
                    if(editName.getText().toString().length()<30) {
                        Me.getInstance().setName(editName.getText().toString());
                        UpdateUser updateUser = new UpdateUser();
                        updateUser.setName(Me.getInstance().getName());
                        updateSend(updateUser);
                    }else{
                        Alert.makeText("Please input less then 30 character at your name.");
                    }
                }else {
                    finish();
                }
                break;
        }
    }

    private void updateSend(UpdateUser updateUser){
        ApiClient.getInstance().getApiService()
                .updateUser(MyApplication.APP_VERSION, updateUser)//, Me.getInstance().getIdx()
                .enqueue(new Callback<ApiMessage>() {
                    @Override
                    public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                        if (response.body() != null) {
                           new LogManager().LogManager("ProfileEditActivity", "response.body().getMessage(): "+response.body().getMessage());
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK,resultIntent);
                            finish();
                        } else {
                            Alert.makeText("Profile update failure");
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("ProfileEditActivity", "edit error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ApiMessage> call, Throwable t) {
                        new LogManager().LogManager("ProfileEditActivity edit fail",t.getMessage());
                        Alert.makeText(getString(R.string.network_error));
                        Alert.makeText("Profile update failure");
                    }
                });
    }
}

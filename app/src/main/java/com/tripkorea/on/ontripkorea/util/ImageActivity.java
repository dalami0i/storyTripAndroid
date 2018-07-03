package com.tripkorea.on.ontripkorea.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.github.chrisbanes.photoview.PhotoView;
import com.tripkorea.on.ontripkorea.R;

/**
 * Created by YangHC on 2018-05-09.
 */

public class ImageActivity extends BaseActivity implements View.OnClickListener {
    private String imageUrl;
    private Boolean isUpdatable;
    private Button goBackBtn;
    private PhotoView imageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageUrl = getIntent().getStringExtra("imageUrl");
        isUpdatable = getIntent().getBooleanExtra("updatable",false);

        imageView = (PhotoView) findViewById(R.id.img_image);
        goBackBtn = (Button) findViewById(R.id.btn_image_back);

        goBackBtn.setOnClickListener(this);

//        Picasso.with(this)
//                .load(imageUrl)
//                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                .networkPolicy(NetworkPolicy.NO_CACHE)
//                .error(getResources().getDrawable(R.drawable.ic_non_profile_pic))
//                .into(imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_image_back:
                finish();
                break;
        }
    }

}

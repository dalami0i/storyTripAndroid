package com.tripkorea.on.ontripkorea.tabs.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.util.OnNetworkErrorListener;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;
import com.tripkorea.on.ontripkorea.vo.user.Me;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


///**
// * Created by YangHC on 2018-06-05.
// */

public class MyPageFragment extends Fragment implements OnMapReadyCallback {
    private GoogleApiClient mGoogleApiClient;
    private OnNetworkErrorListener onNetworkErrorListener;
    private static final int MYPAGE_EDIT = 199;

    ImageButton btnMypageSetting, btnMypageUpdate;
    RoundedImageView profilePicMypage;
    TextView tvMypageName, tvMypageEmail;
    MapView mapMypageFootprint;
    GoogleMap mMap;
    TabLayout mypageTabLayoutList;
    ViewPager mypageList;

    MainActivity main;
    int language;
    AttractionSimpleList likeList;
    AttractionSimpleList visitedList;


    public void setOnNetworkErrorListener(OnNetworkErrorListener onNetworkErrorListener) {
        this.onNetworkErrorListener = onNetworkErrorListener;
    }

    public Fragment mypageFragmentNewInstance(MainActivity main, int language){
        this.main = main;
        this.language = language;
        return new MyPageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        initViews(view);

        mapMypageFootprint.onCreate(savedInstanceState);
        mapMypageFootprint.getMapAsync(this);
        mapMypageFootprint.onStart();

        setLikeList();


        mypageTabLayoutList.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mypageList.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        if(likeList.getItems().size() < 1) Alert.makeText(main.getString(R.string.like_no_history));
                        break;
                    case 1:
                        if(visitedList.getItems().size() < 1) Alert.makeText(main.getString(R.string.visit_no_history));
                        break;
                }

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {            }
        });



        return view;
    }

    private void initViews(View view) {
        btnMypageSetting = view.findViewById(R.id.btn_mypage_setting);
        profilePicMypage = view.findViewById(R.id.img_mypage_profile_pic);
        tvMypageName = view.findViewById(R.id.tv_mypage_name);
        btnMypageUpdate = view.findViewById(R.id.btn_mypage_update);
        tvMypageEmail = view.findViewById(R.id.tv_mypage_email);
        mapMypageFootprint = view.findViewById(R.id.map_mypage_footprint);
        mypageTabLayoutList = view.findViewById(R.id.tab_mypage_list);
        mypageList = view.findViewById(R.id.viewpager_mypage_list);

        tvMypageName.setText(Me.getInstance().getName());
        tvMypageEmail.setText(Me.getInstance().getId());
        if(Me.instance.getProfileImgAddr() != null) {
            Glide.with(MyApplication.getContext())
                    .load(Me.instance.getProfileImgAddr())
                    .into(profilePicMypage);
        }else{
            profilePicMypage.setImageDrawable(MyApplication.getContext().getDrawable(R.drawable.ic_non_profile_pic));
        }

        btnMypageUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfile = new Intent(main, ProfileEditActivity.class);
                startActivityForResult(editProfile, MYPAGE_EDIT);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MYPAGE_EDIT){
            tvMypageName.setText(Me.getInstance().getName());
        }
    }

    private void setLikeList(){
        ApiClient.getInstance().getApiService()
                .getMyLikeList(MyApplication.APP_VERSION, language)//, Me.getInstance().getIdx()
                .enqueue(new Callback<ArrayList<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<ArrayList<AttractionSimple>> call, Response<ArrayList<AttractionSimple>> response) {
                        if (response.body() != null) {
                            likeList = new AttractionSimpleList();
                            likeList.setItems(response.body());
                            for(AttractionSimple attractionSimple : likeList.getItems()){
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(attractionSimple.getLat(), attractionSimple.getLon()))
                                        .title(attractionSimple.getName())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_s)))
                                        .setTag(attractionSimple.getIdx());
                            }

                            if(likeList != null && likeList.getItems().size() > 0) {
                                AttractionSimple firstItem = likeList.getItems().get(0);
                                if (firstItem != null) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(firstItem.getLat(), firstItem.getLon())));
                                }
                            }

                            setVisitedList();
                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("마이페이지 setLikeList()", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<AttractionSimple>> call, Throwable t) {
                        new LogManager().LogManager("mypage like fail",t.getMessage());
                        Alert.makeText(getString(R.string.network_error));
                    }
                });

    }

    private void setVisitedList(){
        ApiClient.getInstance().getApiService()
                .getMyVisitList(MyApplication.APP_VERSION, language)//, Me.getInstance().getIdx()
                .enqueue(new Callback<ArrayList<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<ArrayList<AttractionSimple>> call, Response<ArrayList<AttractionSimple>> response) {
                        if (response.body() != null) {
                            visitedList = new AttractionSimpleList();
                            visitedList.setItems(response.body());

                            final MypagePagerAdapter adapter
                                    = new MypagePagerAdapter (main.getSupportFragmentManager(), language, main, likeList, visitedList);
                            mypageList.setAdapter(adapter);
                            for(AttractionSimple attractionSimple : visitedList.getItems()){
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(attractionSimple.getLat(), attractionSimple.getLon()))
                                        .title(attractionSimple.getName())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_bg)))
                                        .setTag(attractionSimple.getIdx());
                            }

                            if(visitedList != null && visitedList.getItems().size() > 0) {
                                AttractionSimple firstItem = visitedList.getItems().get(0);
                                if (likeList.getItems().size() < 1 || firstItem != null) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(firstItem.getLat(), firstItem.getLon())));
                                }
                            }

                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("마이페이지 setVisitedList()", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<AttractionSimple>> call, Throwable t) {
                        Alert.makeText(getString(R.string.network_error));
                    }
                });

    }



    protected synchronized void buildGoogleApiClient() {
//        new LogManager().printLogManager("빌드에이피아이","비이이이이이틀");
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.57882224, 126.976993)));
    }


}

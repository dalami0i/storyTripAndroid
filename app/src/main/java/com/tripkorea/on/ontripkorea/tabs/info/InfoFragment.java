package com.tripkorea.on.ontripkorea.tabs.info;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.Coordinate;
import com.tripkorea.on.ontripkorea.util.CustomLinearLayoutManager;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.util.OnNetworkErrorListener;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.user.Me;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by YangHC on 2018-06-05.
 */

public class InfoFragment extends Fragment {
    public static boolean LIKE_LIST_CHANGED;
    public static boolean VISITED_LIST_CHANGED;
    private final static int TAB_LIKE = 0;
    private final static int TAB_VISITED = 1;

    MainActivity main;
    int lastTab;

    //locale
    private String usinglanguage;
    private Locale locale;
    private int language;

    //중심 여행지 위치 (현재는 창덕궁)
    private Coordinate coordinate = new Coordinate(37.579108, 126.990957);


    @BindView(R.id.information_image_vp)
    ViewPager informationImgVP;
    //    @BindView(R.id.info_tabs)
    TabLayout informationTab;
    @BindView(R.id.info_rv)
    RecyclerView informationRV;

    private InfoRecyclerViewAdapter likeRecyclerViewAdapter;
    private InfoRecyclerViewAdapter visitRecyclerViewAdapter;
    private List<AttractionSimple> likeList = new ArrayList<>();
    private List<AttractionSimple> visitList = new ArrayList<>();

    ProgressDialog infoDialogProgress = null;

    public Fragment infoFragment(MainActivity main,int lastTab){
        this.main = main;
        this.lastTab = lastTab;
        return new InfoFragment();
    }

    public OnNetworkErrorListener onNetworkErrorListener;

    public void setOnNetworkErrorListener(OnNetworkErrorListener onNetworkErrorListener) {
        this.onNetworkErrorListener = onNetworkErrorListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        infoDialogProgress = ProgressDialog.show(main, "","Loading", true);

        View view = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this, view);
        LIKE_LIST_CHANGED = true;
        VISITED_LIST_CHANGED = true;

        informationTab = view.findViewById(R.id.info_tabs);

        //사용자 언어 확인
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
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
                language = 2;
                break;
            case "日本言":
                language = 3;
                break;
            default:
                language = 0;
                break;

        }

        ChangImageFragmentPagerAdapter changImageFragmentPagerAdapter
                = new ChangImageFragmentPagerAdapter(getChildFragmentManager());
        changImageFragmentPagerAdapter.addChangImage("https://upload.wikimedia.org/wikipedia/commons/thumb/c/cb/Changdeokgung-Injeongjeon.jpg/1200px-Changdeokgung-Injeongjeon.jpg");
        informationImgVP.setAdapter(changImageFragmentPagerAdapter);

        informationTab.addTab(informationTab.newTab().setText("I Like"));
        informationTab.addTab(informationTab.newTab().setText("I Visited"));

        LinearLayoutManager linearLayoutManagerMy = new CustomLinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecorationLinkLike
                = new DividerItemDecoration(informationRV.getContext(), linearLayoutManagerMy.getOrientation());
        informationRV.addItemDecoration(dividerItemDecorationLinkLike);
        informationRV.setLayoutManager(linearLayoutManagerMy);

        setLikeList();



        initViews(view);

        return view;
    }
    // TODO : 서버에서 좋아요 관광지 정보 가져오기
    private void setLikeList() {
        ApiClient.getInstance().getApiService()
                .getMyLikeList(MyApplication.APP_VERSION, Me.getInstance().getIdx(), language)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        if (response.body() != null) {
                            likeList = response.body();
                            likeRecyclerViewAdapter = new InfoRecyclerViewAdapter(likeList, getContext(), coordinate);
                            likeRecyclerViewAdapter.setAttractionList(response.body());
                            likeRecyclerViewAdapter.notifyDataSetChanged();
                            new LogManager().LogManager("InfoFragment","ApiClient: likeList Size: "+likeList.size());

                            informationRV.setAdapter(likeRecyclerViewAdapter);
                            informationTab.addOnTabSelectedListener(tabSelectedListener);
                            if(infoDialogProgress.isShowing()){
                                infoDialogProgress.dismiss();
                            }
                        } else {
                            Alert.makeText("likelist 에러! : ");
                            try {
                                Log.e("INFO_FRAGMENT", "에러! : " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AttractionSimple>> call, Throwable t) {

                    }
                });

        LIKE_LIST_CHANGED = false;
    }

    // TODO : 서버에서 가봤아요 관광지 정보 가져오기
    private void setVisitedList() {
        ApiClient.getInstance().getApiService()
                .getMyVisitList(MyApplication.APP_VERSION, Me.getInstance().getIdx(), language)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        if (response.body() != null) {
                            visitList = response.body();
                            visitRecyclerViewAdapter = new InfoRecyclerViewAdapter(visitList, getContext(), coordinate);
                            visitRecyclerViewAdapter.setAttractionList(response.body());
                            visitRecyclerViewAdapter.notifyDataSetChanged();
                            new LogManager().LogManager("InfoFragment","ApiClient: visitList Size: "+visitList.size());

                            informationRV.setAdapter(visitRecyclerViewAdapter);
                            informationTab.addOnTabSelectedListener(tabSelectedListener);
                            if(infoDialogProgress.isShowing()){
                                infoDialogProgress.dismiss();
                            }
                        } else {
                            Alert.makeText("l visited list 에러! : ");
                            try {
                                Log.e("INFO_FRAGMENT", "에러! : " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AttractionSimple>> call, Throwable t) {

                    }
                });
        VISITED_LIST_CHANGED = false;
    }

    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            switch (informationTab.getSelectedTabPosition()) {

                case TAB_LIKE:
                    informationRV.setAdapter(likeRecyclerViewAdapter);
                    break;
                case TAB_VISITED:
                    informationRV.setAdapter(visitRecyclerViewAdapter);
                    break;
            }


        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    private void initViews(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();

        if (LIKE_LIST_CHANGED) {
            //TODO: likeLIST 업데이트해주기
            ApiClient.getInstance().getApiService()
                    .getMyLikeList(MyApplication.APP_VERSION, Me.getInstance().getIdx(), language)
                    .enqueue(new Callback<List<AttractionSimple>>() {
                        @Override
                        public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                            if (response.body() != null) {
                                likeList = response.body();
                                if(likeRecyclerViewAdapter == null) likeRecyclerViewAdapter = new InfoRecyclerViewAdapter(likeList, getContext(), coordinate);
                                likeRecyclerViewAdapter.setAttractionList(response.body());
                                likeRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                                Alert.makeText("likelist 에러! : ");
                                try {
                                    Log.e("INFO_FRAGMENT", "에러! : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<AttractionSimple>> call, Throwable t) {

                        }
                    });

            LIKE_LIST_CHANGED = false;
        }
        if (VISITED_LIST_CHANGED) {
            //TODO: visitLIST 업데이트해주기
            ApiClient.getInstance().getApiService()
                    .getMyVisitList(MyApplication.APP_VERSION, Me.getInstance().getIdx(), language)
                    .enqueue(new Callback<List<AttractionSimple>>() {
                        @Override
                        public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                            if (response.body() != null) {
                                visitList = response.body();
                                if(visitRecyclerViewAdapter == null) visitRecyclerViewAdapter = new InfoRecyclerViewAdapter(visitList, getContext(), coordinate);
                                visitRecyclerViewAdapter.setAttractionList(response.body());
                                visitRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                                Alert.makeText("visitlist 에러! : ");
                                try {
                                    Log.e("INFO_FRAGMENT", "에러! : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<AttractionSimple>> call, Throwable t) {

                        }
                    });

            VISITED_LIST_CHANGED = false;
        }

    }
}
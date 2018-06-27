package com.tripkorea.on.ontripkorea.tabs.info;

import android.os.Build;
import android.os.Bundle;
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
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.Coordinate;
import com.tripkorea.on.ontripkorea.util.CustomLinearLayoutManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.Attraction;
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
    //locale
    String usinglanguage;
    Locale locale;
    //중심 여행지 위치 (현재는 창덕궁)
    Coordinate coordinate = new Coordinate(37.579108, 126.990957);


    @BindView(R.id.information_image_vp)
    ViewPager informationImgVP;
    @BindView(R.id.recycler_info_like)
    RecyclerView likeRV;
    @BindView(R.id.recycler_info_visit)
    RecyclerView visitRV;

    private InfoRecyclerViewAdapter likeRecyclerViewAdapter;
    private InfoRecyclerViewAdapter visitRecyclerViewAdapter;
    private List<Attraction> likeList = new ArrayList<>();
    private List<Attraction> visitList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this,view);

        //사용자 언어 확인
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }

        usinglanguage = locale.getDisplayLanguage();

        ChangImageFragmentPagerAdapter changImageFragmentPagerAdapter
                = new ChangImageFragmentPagerAdapter(getChildFragmentManager());
        changImageFragmentPagerAdapter.addChangImage("https://upload.wikimedia.org/wikipedia/commons/thumb/c/cb/Changdeokgung-Injeongjeon.jpg/1200px-Changdeokgung-Injeongjeon.jpg");
        informationImgVP.setAdapter(changImageFragmentPagerAdapter);

        LinearLayoutManager linearLayoutManagerMy = new CustomLinearLayoutManager(getContext());
        LinearLayoutManager linearLayoutManagerMy2 = new CustomLinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecorationLinkLike
                = new DividerItemDecoration(likeRV.getContext(), linearLayoutManagerMy.getOrientation());
        DividerItemDecoration dividerItemDecorationLinkVisit
                = new DividerItemDecoration(visitRV.getContext(), linearLayoutManagerMy.getOrientation());
        likeRV.addItemDecoration(dividerItemDecorationLinkLike);
        likeRV.setLayoutManager(linearLayoutManagerMy);
        visitRV.addItemDecoration(dividerItemDecorationLinkVisit);
        visitRV.setLayoutManager(linearLayoutManagerMy2);

        likeRecyclerViewAdapter = new InfoRecyclerViewAdapter(likeList, getContext(),coordinate);
        visitRecyclerViewAdapter = new InfoRecyclerViewAdapter(visitList, getContext(),coordinate);
        likeRV.setAdapter(likeRecyclerViewAdapter);
        visitRV.setAdapter(visitRecyclerViewAdapter);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        ApiClient.getInstance().getApiService()
                .getMyLikeList(MyApplication.APP_VERSION, Me.getInstance().getIdx())
                .enqueue(new Callback<List<Attraction>>() {
                    @Override
                    public void onResponse(Call<List<Attraction>> call, Response<List<Attraction>> response) {
                        if(response.body()!=null){
                            likeList = response.body();
                            likeRecyclerViewAdapter.setAttractionList(response.body());
                            likeRecyclerViewAdapter.notifyDataSetChanged();
                        }else{
                            Alert.makeText("likelist 에러! : ");
                            try {
                                Log.e("INFO_FRAGMENT","에러! : " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Attraction>> call, Throwable t) {

                    }
                });
        ApiClient.getInstance().getApiService()
                .getMyVisitList(MyApplication.APP_VERSION, Me.getInstance().getIdx())
                .enqueue(new Callback<List<Attraction>>() {
                    @Override
                    public void onResponse(Call<List<Attraction>> call, Response<List<Attraction>> response) {
                        if(response.body()!=null){
                            visitList = response.body();
                            visitRecyclerViewAdapter.setAttractionList(response.body());
                            visitRecyclerViewAdapter.notifyDataSetChanged();
                        }else{
                            Alert.makeText("visitlist 에러! : ");
                            try {
                                Log.e("INFO_FRAGMENT","에러! : " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Attraction>> call, Throwable t) {

                    }
                });
    }
}

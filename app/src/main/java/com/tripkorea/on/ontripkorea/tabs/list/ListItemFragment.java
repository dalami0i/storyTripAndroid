package com.tripkorea.on.ontripkorea.tabs.list;


import android.content.Intent;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.tabs.search.SearchActivity;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.util.OnNetworkErrorListener;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Edward Won on 2018-08-21.
 */

public class ListItemFragment extends Fragment{

    MainActivity main;
    AttractionSimpleList totalList, foodList, tourList, showList, recommendationList;
//    RecyclerView listRV;
    double currentLat, currentLon;
    TabLayout menuHiddenTablayout;
    TabLayout upsideTabLayout;
    RecyclerView rvMainRecommendation;
    AppBarLayout mainAppbar;
    RelativeLayout underLayout;
    ImageView btnMylocation, btnHiddenMyLocation, btnHiddenSearch;
    ExtractEditText mainSearch;
    ViewPager mainVP;
    int language;

    final int CHANGE_FIND_LOCATION = 1024;



    public Fragment listFragmentNewInstance(MainActivity main,
                                            AttractionSimpleList totalList, AttractionSimpleList foodList,
                                            AttractionSimpleList tourList, AttractionSimpleList recommendationList,
                                                 double lat, double lon, int language){
        this.main = main;
        this.totalList = totalList;
        showList = totalList;
        this.foodList = foodList;
        this.tourList = tourList;
        this.recommendationList = recommendationList;
        currentLat = lat;
        currentLon = lon;
        this.language = language;
        return new ListItemFragment();
    }

    public OnNetworkErrorListener onNetworkErrorListener;

    public void setOnNetworkErrorListener(OnNetworkErrorListener onNetworkErrorListener) {
        this.onNetworkErrorListener = onNetworkErrorListener;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        new LogManager().LogManager("list fragment","리스트프레그먼트진입 ");

        mainAppbar = view.findViewById(R.id.main_appbar);
        final RelativeLayout hiddenLayout = view.findViewById(R.id.layout_main_hidden);
        final TextView tvHiddenTitle = view.findViewById(R.id.id_title_bar);
        btnHiddenMyLocation = view.findViewById(R.id.btn_hidden_mylocation);
        menuHiddenTablayout = view.findViewById(R.id.menu_hidden_tablayout);
        final RelativeLayout layoutMainRVlayout = view.findViewById(R.id.layout_main_rvlayout);
        underLayout = view.findViewById(R.id.layout_main_under);
        btnMylocation = view.findViewById(R.id.btn_mylocation);
        rvMainRecommendation = view.findViewById(R.id.rv_recommend_list);
        mainSearch = view.findViewById(R.id.edittext_main_search);
        btnHiddenSearch = view.findViewById(R.id.btn_hidden_find);
        mainVP = view.findViewById(R.id.item_vp);



        final String[] states = new String[1];

        mainAppbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                new LogManager().LogManager("Appbar State",state.name());
                switch(state.name()){
                    case"COLLAPSED":
                        hiddenLayout.setVisibility(View.VISIBLE);
                        tvHiddenTitle.setVisibility(View.VISIBLE);
                        btnHiddenMyLocation.setVisibility(View.VISIBLE);
                        menuHiddenTablayout.setVisibility(View.VISIBLE);

                        menuHiddenTablayout.clearOnTabSelectedListeners();
                        menuHiddenTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                mainVP.setCurrentItem(tab.getPosition());
                            }


                            @Override
                            public void onTabUnselected(TabLayout.Tab tab) {                            }
                            @Override
                            public void onTabReselected(TabLayout.Tab tab) {                            }
                        });

                        int newPosition = upsideTabLayout.getSelectedTabPosition();
                        menuHiddenTablayout.getTabAt(newPosition).select();
                        menuHiddenTablayout.setScrollPosition(newPosition,0,true);
                        states[0] = state.name();

                        break;

                    case "IDLE":

                        if(states[0].equals("COLLAPSED")){
                            newPosition = menuHiddenTablayout.getSelectedTabPosition();
                            upsideTabLayout.getTabAt(newPosition).select();
                            upsideTabLayout.setScrollPosition(newPosition,0,true);
                        }
                        hiddenLayout.setVisibility(View.GONE);
                        tvHiddenTitle.setVisibility(View.GONE);
                        btnHiddenMyLocation.setVisibility(View.GONE);
                        menuHiddenTablayout.setVisibility(View.GONE);
                        RelativeLayout.LayoutParams idleParams = new RelativeLayout.LayoutParams
                                (ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                        idleParams.addRule(RelativeLayout.BELOW,hiddenLayout.getId());
                        layoutMainRVlayout.setLayoutParams(idleParams);


                        break;

                    default:
                        hiddenLayout.setVisibility(View.GONE);
                        tvHiddenTitle.setVisibility(View.GONE);
                        btnHiddenMyLocation.setVisibility(View.GONE);
                        menuHiddenTablayout.setVisibility(View.GONE);
                        RelativeLayout.LayoutParams expandedParams = new RelativeLayout.LayoutParams
                                (ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                        expandedParams.addRule(RelativeLayout.BELOW,hiddenLayout.getId());
                        layoutMainRVlayout.setLayoutParams(expandedParams);
                        states[0] = state.name();

                        break;
                }
            }
        });

        upsideTabLayout = view.findViewById(R.id.menu_tablayout);
        wrapTabIndicatorToTitle(upsideTabLayout, 0, 10);

        LinearLayoutManager recommendLayoutManager
                = new LinearLayoutManager(MyApplication.getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvMainRecommendation.setLayoutManager(recommendLayoutManager);
        ListRecommendedItemRecyclerViewAdapter listRecommendedItemRecyclerViewAdapter
                = new ListRecommendedItemRecyclerViewAdapter(main);
        for(int i=0; i<recommendationList.getItems().size(); i++){
            listRecommendedItemRecyclerViewAdapter.addListView(recommendationList.getItems().get(i));
        }

        rvMainRecommendation.setAdapter(listRecommendedItemRecyclerViewAdapter);

//////////////////////////////////////////////////////////////////////////////////////////////////////////

        upsideTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainVP.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////////////
        int page = 1;

        final ListPagerAdapter adapter
                = new ListPagerAdapter (main.getSupportFragmentManager(), upsideTabLayout.getTabCount(),
                currentLat, currentLon, language, page, main, totalList, tourList, foodList );

        mainVP.setAdapter(adapter);
        mainVP.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(upsideTabLayout));
        mainVP.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(menuHiddenTablayout));




        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        btnMylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getNewLocation = new Intent(main, ListMapViewActivity.class);
                main.startActivityFromFragment(ListItemFragment.this, getNewLocation, CHANGE_FIND_LOCATION);
            }
        });

        btnHiddenMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getNewLocation = new Intent(main, ListMapViewActivity.class);
                main.startActivityFromFragment(ListItemFragment.this, getNewLocation, CHANGE_FIND_LOCATION);
            }
        });


        mainSearch.setActivated(false);
        mainSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogManager().LogManager("Search","검색으로 이동");
                Intent searchIntent = new Intent(main, SearchActivity.class);
                main.startActivity(searchIntent);
            }
        });

        btnHiddenSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogManager().LogManager("Search","검색으로 이동");
                Intent searchIntent = new Intent(main, SearchActivity.class);
                main.startActivity(searchIntent);
            }
        });



        return view;
    }



    public void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            int childCount = ((ViewGroup) tabStrip).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View tabView = tabStripGroup.getChildAt(i);
                //set minimum width to 0 for instead for small texts, indicator is not wrapped as expected
                tabView.setMinimumWidth(0);
                // set padding to 0 for wrapping indicator as title
                tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
                // setting custom margin between tabs
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    if (i == 0) {
                        // left
                        settingMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        // right
                        settingMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        // internal
                        settingMargin(layoutParams, internalMargin, internalMargin);
                    }
                }
            }


            tabLayout.requestLayout();
        }
    }

    private void settingMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
        layoutParams.leftMargin = start;
        layoutParams.rightMargin = end;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        new LogManager().LogManager("ListItemFragment 위치값 전","위도: "+currentLat+" | 경도: "+currentLon);
        if(resultCode == RESULT_OK ){
            switch (requestCode){
                case CHANGE_FIND_LOCATION:
                    currentLat = data.getDoubleExtra("lat",37.577401);
                    currentLon = data.getDoubleExtra("lon",126.989511);
                    setTotals(currentLat, currentLon, 1);
                    new LogManager().LogManager("ListItemFragment","onActivityResult 진입");


                    break;
            }
        }
        new LogManager().LogManager("ListItemFragment 위치값 후","위도: "+currentLat+" | 경도: "+currentLon);

    }


    private void setTotals(final double lat,final double lon, final int page) {
        ApiClient.getInstance().getApiService()
                .getAroundAttractions(MyApplication.APP_VERSION,lat, lon, language, page)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        new LogManager().LogManager("list엑티비티","settotal : "+lat+" | "+lon);
                        if (response.body() != null) {

                            totalList = new AttractionSimpleList();
                            totalList.setItems(response.body());
                            new LogManager().LogManager("list엑티비티 전체","settotal size: "+totalList.getItems().size());
                            setRestaurants(lat, lon, page);
                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("list엑티비티 total", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AttractionSimple>> call, Throwable t) {
                        Alert.makeText(getString(R.string.network_error));
                    }
                });
    }


    private void setRestaurants(final double lat,final double lon, final int page) {
        ApiClient.getInstance().getApiService()
                .getAroundRestaurants(MyApplication.APP_VERSION,lat, lon, language, page)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        new LogManager().LogManager("list엑티비티","setRestaurant : "+lat+" | "+lon);
                        if (response.body() != null) {

                            foodList = new AttractionSimpleList();
                            foodList.setItems(response.body());
                            new LogManager().LogManager("list엑티비티 전체","setRestaurant size: "+foodList.getItems().size());
                            setTours(lat, lon, page);
                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("list엑티비티 RESTAURANTS", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AttractionSimple>> call, Throwable t) {
                        Alert.makeText(getString(R.string.network_error));
                    }
                });
    }

    private void setTours(final double lat, final double lon, int page) {
        ApiClient.getInstance().getApiService()
                .getAroundTours(MyApplication.APP_VERSION,lat, lon, language, page)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        new LogManager().LogManager("list엑티비티","setTour : "+lat+" | "+lon);
                        if (response.body() != null) {

                            tourList = new AttractionSimpleList();
                            tourList.setItems(response.body());
                            new LogManager().LogManager("list엑티비티 전체","setTour size: "+tourList.getItems().size());
                            MainActivity.totalList = totalList;
                            MainActivity.attractionList = tourList;
                            MainActivity.foodList = foodList;
                            listChange();

                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("list엑티비티 Tours", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AttractionSimple>> call, Throwable t) {
                        Alert.makeText(getString(R.string.network_error));
                    }
                });
    }

    private void listChange(){
        new LogManager().LogManager("ListItemFragment 위치","upsideTabLayout.getSelectedTabPosition(): "+upsideTabLayout.getSelectedTabPosition());

        final ListPagerAdapter adapter
                = new ListPagerAdapter (main.getSupportFragmentManager(), upsideTabLayout.getTabCount(),
                currentLat, currentLon, language, 1, main, totalList, tourList, foodList );
        mainVP.setAdapter(adapter);
        mainVP.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(upsideTabLayout));
        mainVP.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(menuHiddenTablayout));

    }





}


//            upsideTabLayout.setScrollPosition(tab.getPosition(), 1, false);
//                LinearLayout tabStrip = ((LinearLayout)upsideTabLayout.getChildAt(0));
//                for(int i = 0; i < tabStrip.getChildCount(); i++) {
//                    tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
//                        @Override
//                        public boolean onTouch(View v, MotionEvent event) {
//                            return true;
//                        }
//                    });
//                }
//            upsideTabLayout.getTabAt(tab.getPosition());
//            upsideTabLayout.setSelected(true);


//                        slideUp(hiddenLayout);
                        /*RelativeLayout.LayoutParams expanededParams = new RelativeLayout.LayoutParams
                                (ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                        expanededParams.addRule(RelativeLayout.BELOW,appBarLayout.getId());
                        layoutMainRVlayout.setLayoutParams(expanededParams);*/
//                        LinearLayout tabStrip = ((LinearLayout)menuHiddenTablayout.getChildAt(0));
//                        for(int i = 0; i < tabStrip.getChildCount(); i++) {
//                            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
//                                @Override
//                                public boolean onTouch(View v, MotionEvent event) {
//                                    return true;
//                                }
//                            });
//                        }
//                        menuHiddenTablayout.getTabAt(upsideTabLayout.getSelectedTabPosition());
//                        menuHiddenTablayout.setSelected(true);

    // slide the view from below itself to the current position
   /* public void slideUp(View view){

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }*/

   /*switch(position){
            case 0:
                if(!from.equals("each")){
                    listTotalRecyclerViewAdapter.clearList();
                    for(int i=0; i<totalList.getItems().size(); i++){
                        listTotalRecyclerViewAdapter.addListView(totalList.getItems().get(i));
                    }
                }else{
                    totalList.getItems().addAll(totalTempList.getItems());
                    for(int i=0; i<totalList.getItems().size(); i++){
                        listTotalRecyclerViewAdapter.addListView(totalList.getItems().get(i));
                    }
                }

                listTotalRecyclerViewAdapter.notifyDataSetChanged();
                upsideTabLayout.getTabAt(upsideTabLayout.getSelectedTabPosition()).select();
                menuHiddenTablayout.getTabAt(menuHiddenTablayout.getSelectedTabPosition()).select();

                break;
            case 1:
                if(!from.equals("each")){
                    listTotalRecyclerViewAdapter.clearList();
                    for(int i=0; i<tourList.getItems().size(); i++){
                        listTotalRecyclerViewAdapter.addListView(tourList.getItems().get(i));
                    }
                }else{
                    tourList.getItems().addAll(tourTempList.getItems());
                    for(int i=0; i<tourList.getItems().size(); i++){
                        listTotalRecyclerViewAdapter.addListView(tourList.getItems().get(i));
                    }
                }

                listTotalRecyclerViewAdapter.notifyDataSetChanged();
                upsideTabLayout.getTabAt(upsideTabLayout.getSelectedTabPosition()).select();
                menuHiddenTablayout.getTabAt(menuHiddenTablayout.getSelectedTabPosition()).select();
                break;
            case 2:

                if(!from.equals("each")){
                    listTotalRecyclerViewAdapter.clearList();
                    for(int i=0; i<foodList.getItems().size(); i++){
                        listTotalRecyclerViewAdapter.addListView(foodList.getItems().get(i));
                    }
                }else{
                    foodList.getItems().addAll(foodTempList.getItems());
                    for(int i=0; i<foodList.getItems().size(); i++){
                        listTotalRecyclerViewAdapter.addListView(foodList.getItems().get(i));
                    }
                }
                listTotalRecyclerViewAdapter.notifyDataSetChanged();
                upsideTabLayout.getTabAt(upsideTabLayout.getSelectedTabPosition()).select();
                menuHiddenTablayout.getTabAt(menuHiddenTablayout.getSelectedTabPosition()).select();
                break;
        }*/
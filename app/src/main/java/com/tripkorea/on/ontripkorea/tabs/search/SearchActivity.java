package com.tripkorea.on.ontripkorea.tabs.search;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;

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
 * Created by Edward Won on 2018-09-07.
 */

public class SearchActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    @BindView(R.id.search_appbar)           AppBarLayout searchAppbar;
    @BindView(R.id.tv_search_title)         TextView searchTitle;
    @BindView(R.id.search_frame)            RelativeLayout layoutSearchTop;
    @BindView(R.id.edittext_main_search)    EditText etvSearch;
    @BindView(R.id.btn_find)                ImageView btnFind;
    @BindView(R.id.layout_search_under)     RelativeLayout rlUnder;
    @BindView(R.id.layout_first)            RelativeLayout layoutFirst;
    @BindView(R.id.tv_detail_tag1)          TextView tvTag1;
    @BindView(R.id.tv_detail_tag2)          TextView tvTag2;
    @BindView(R.id.tv_detail_tag3)          TextView tvTag3;
    @BindView(R.id.tv_detail_tag4)          TextView tvTag4;
    @BindView(R.id.tv_detail_tag5)          TextView tvTag5;
    @BindView(R.id.tv_detail_tag6)          TextView tvTag6;
    @BindView(R.id.tv_detail_tag7)          TextView tvTag7;
    @BindView(R.id.tv_detail_tag8)          TextView tvTag8;
    @BindView(R.id.tv_detail_tag9)          TextView tvTag9;
    @BindView(R.id.tv_detail_tag10)         TextView tvTag10;
    @BindView(R.id.tv_detail_location1)     TextView tvLocatoin1;
    @BindView(R.id.tv_detail_location2)     TextView tvLocatoin2;
    @BindView(R.id.tv_detail_location3)     TextView tvLocatoin3;
    @BindView(R.id.tv_detail_location4)     TextView tvLocatoin4;
    @BindView(R.id.tv_detail_location5)     TextView tvLocatoin5;
    @BindView(R.id.tv_detail_location6)     TextView tvLocatoin6;
    @BindView(R.id.tv_detail_location7)     TextView tvLocatoin7;
    @BindView(R.id.tv_detail_location8)     TextView tvLocatoin8;
    @BindView(R.id.tv_detail_location9)     TextView tvLocatoin9;
    @BindView(R.id.tv_detail_location10)    TextView tvLocatoin10;
    @BindView(R.id.layout_search_list)      LinearLayout linearLayoutList;
    @BindView(R.id.searched_list)           RecyclerView rvSearched;
    @BindView(R.id.btn_search_list_location)
    TextView btnLocation;
    @BindView(R.id.btn_search_list_category)
    TextView btnCategory;
    @BindView(R.id.btn_search_list_tag)
    TextView btnTag;


    private int language;
    private int seaerchedPage;
    private String lat = "";
    private String lon = "";
    private String searchQuery = "";
    private String categoryQuery = "";
    private String tagQuery = "";

    List<AttractionSimple> searchedList = new ArrayList<>();
    ProgressDialog searchProgress = null;
    boolean dialogShowing = false;


    final ArrayList<String> locationList = new ArrayList<>();
    final ArrayList<Boolean> locationCheckList = new ArrayList<>();
    final ArrayList<String> categoryList = new ArrayList<>();
    final ArrayList<Boolean> categoryCheckList = new ArrayList<>();
    final ArrayList<String> tagList = new ArrayList<>();
    final ArrayList<Boolean> tagCheckList = new ArrayList<>();

    String locationQuery;

    SearchedRecyclerViewAdapter searchedmRecyclerViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        new LogManager().LogManager("SearchView","SearchActivity 진입");

        Locale locale;
        String usinglanguage;
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

        etvSearch.setOnEditorActionListener(this);
        etvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                etvSearch.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etvSearch.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            }

            @Override
            public void afterTextChanged(Editable s) {
                etvSearch.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            }
        });

        locationList.add("홍대");//홍대 신촌 강남 이태원 명동 광화문 건대 custom 직접입력
        locationCheckList.add(false);
        locationList.add("신촌");
        locationCheckList.add(false);
        locationList.add("경복궁");
        locationCheckList.add(false);
        locationList.add("이태원");
        locationCheckList.add(false);
        locationList.add("강남");
        locationCheckList.add(false);

        categoryList.add("놀거리");
        categoryCheckList.add(false);
        categoryList.add("먹거리");
        categoryCheckList.add(false);

        tagList.add("전망이 좋은");
        tagCheckList.add(false);
        tagList.add("합리적인 가격");
        tagCheckList.add(false);
        tagList.add("시끌벅적한");
        tagCheckList.add(false);
        tagList.add("고급스러운");
        tagCheckList.add(false);
        tagList.add("난 몰라");
        tagCheckList.add(false);


        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rlUnder.getVisibility() == View.VISIBLE ) changeView();
                seaerchedPage = 1;
                search(seaerchedPage);
                new LogManager().LogManager("서치엑티비티","search() 실행");
            }
        });

        searchedRVSet();

        rvSearched.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;

//                new LogManager().LogManager("listRV.addOnScrollListener","lastVisibleItemPosition: "+lastVisibleItemPosition+" | itemTotalCount: "+itemTotalCount);
                if (lastVisibleItemPosition == itemTotalCount) {
                    new LogManager().LogManager("SearchActivity","rvSearched.addOnScrollListener");
                    search(seaerchedPage);

                }
            }
        });
    }



    private void changeView(){
        new LogManager().LogManager("SearchView","changeView()");
        searchTitle.setVisibility(View.GONE);
        RelativeLayout.LayoutParams expandedParams = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        expandedParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        RelativeLayout.MarginLayoutParams marginLayoutParams = new RelativeLayout.MarginLayoutParams(expandedParams);
        marginLayoutParams.setMargins(10,10,10,10);
        layoutSearchTop.setLayoutParams(expandedParams);
        etvSearch.setBackground(getDrawable(R.drawable.round_background_detail_tag));
//        linearLayoutList.setVisibility(View.VISIBLE);
        btnLocation.setOnClickListener(listListener);
        btnCategory.setOnClickListener(listListener);
        btnTag.setOnClickListener(listListener);
        rlUnder.setVisibility(View.GONE);
        RelativeLayout.LayoutParams btnFindParams = new RelativeLayout.LayoutParams(60,60);
        btnFindParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        btnFindParams.addRule(RelativeLayout.CENTER_VERTICAL);
        marginLayoutParams.setMargins(10,20,10,20);
        btnFind.setLayoutParams(btnFindParams);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rlUnder.getVisibility() == View.VISIBLE ) changeView();
                seaerchedPage = 1;
                search(seaerchedPage);
                new LogManager().LogManager("서치엑티비티","search() 실행");
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(rlUnder.getVisibility() == View.VISIBLE ) changeView();
        seaerchedPage = 1;
        search(seaerchedPage);
        new LogManager().LogManager("서치엑티비티","search() 실행");
        return true;
    }

    private void search(final int page){
        searchProgress = ProgressDialog.show(SearchActivity.this, "","Search...", true);
        dialogShowing = true;
        searchQuery = etvSearch.getText().toString();
        new LogManager().LogManager("서치액","language: "+language+" | page: "+page+" | lat: "+lat+" | lon: "+lon+" | searchQuery: "+searchQuery
                                    +" | categoryQuery: "+categoryQuery+" | tagQuery: "+tagQuery);
        ApiClient.getInstance().getApiService()
                .search(MyApplication.APP_VERSION,language, page, lat, lon, searchQuery, categoryQuery, tagQuery)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        if (response.body() != null) {
                            searchedList = response.body();
                            new LogManager().LogManager("서치액","검색결과-searchedList size: "+searchedList.size());
                            if(dialogShowing&&searchProgress.isShowing()) {
                                searchProgress.dismiss();
                                new LogManager().LogManager("서치엑티비티","서치 성공 끝");
                                dialogShowing = false;
                            }
                            if(page == 1) searchedRVSet();
                            else moreSearch();
                            seaerchedPage++;
                        } else {
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("서치엑티비티 ApiClient search", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if(dialogShowing&&searchProgress.isShowing()) {
                                searchProgress.dismiss();
                                new LogManager().LogManager("서치엑티비티","response body null 서치 실패 끝");
                                dialogShowing = false;
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<List<AttractionSimple>> call, Throwable t) {
                        Alert.makeText(getString(R.string.network_error));
                        if(dialogShowing&&searchProgress.isShowing()) {
                            searchProgress.dismiss();
                            new LogManager().LogManager("서치엑티비티","서치 실패 끝");
                            dialogShowing = false;
                        }
                    }
                });
    }

    private void searchedRVSet(){
        rvSearched.setVisibility(View.VISIBLE);
        LinearLayoutManager searchedLayoutManager
                = new LinearLayoutManager(MyApplication.getContext(), LinearLayoutManager.VERTICAL, false);
        rvSearched.setLayoutManager(searchedLayoutManager);
        searchedmRecyclerViewAdapter = new SearchedRecyclerViewAdapter(SearchActivity.this);
        for(int i=0; i<searchedList.size(); i++){
            searchedmRecyclerViewAdapter.addListView(searchedList.get(i));
        }

        rvSearched.setAdapter(searchedmRecyclerViewAdapter);
    }

    private void moreSearch(){
        for(int i=0; i<searchedList.size(); i++){
            searchedmRecyclerViewAdapter.addListView(searchedList.get(i));
        }
        searchedmRecyclerViewAdapter.notifyDataSetChanged();
    }

    View.OnClickListener listListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new LogManager().LogManager("onClick(View v) click id??",v.getId()+" ");
            int tabPosition;
            switch (v.getId()) {
                case R.id.btn_search_list_location:
                    tabPosition = 0;
                    String locationArray []   = listToArraty(locationList);
                    boolean tempCheckedList[] = checkListToArraty(locationCheckList);
                    String dialogTitle = "Check Locations";
                    setDialogBuilder(new AlertDialog.Builder(SearchActivity.this),
                            locationCheckList, locationArray, tempCheckedList, dialogTitle, tabPosition).show();
                    break;

                case R.id.btn_search_list_category:
                    tabPosition = 1;
                    String categoryArray []   = listToArraty(categoryList);
                    boolean categoryCheckedList[] = checkListToArraty(categoryCheckList);
                    String dialogCategoryTitle = "Check Category";
                    setDialogBuilder(new AlertDialog.Builder(SearchActivity.this),
                            categoryCheckList, categoryArray, categoryCheckedList, dialogCategoryTitle, tabPosition).show();
                    break;

                case R.id.btn_search_list_tag:
                    tabPosition = 2;
                    String tagArray []   = listToArraty(tagList);
                    boolean tagCheckedList[] = checkListToArraty(tagCheckList);
                    String dialogTagTitle = "Check Locations";
                    setDialogBuilder(new AlertDialog.Builder(SearchActivity.this),
                            tagCheckList, tagArray, tagCheckedList, dialogTagTitle, tabPosition).show();
                    break;

                default:
                    break;
            }
        }
    };

    private String[] listToArraty(ArrayList<String> items){
        final String locationArray []   = new String[ items.size()];
        int tempCount = 0;
        for(String checkedItem : items){
            locationArray[tempCount] = checkedItem;
            tempCount++;
        }

        return locationArray;
    }

    private boolean[] checkListToArraty( ArrayList<Boolean> checkList){

        final boolean tempCheckedList[] = new boolean[checkList.size()];
        int tmpCount = 0;
        for(boolean checked : checkList){
            tempCheckedList[tmpCount] = checked;
            tmpCount++;
        }

        return  tempCheckedList;
    }

    private AlertDialog.Builder setDialogBuilder(AlertDialog.Builder builder, final ArrayList<Boolean> itemCheckList,
                                                 final String[] itemArray, boolean[] itemCheckArray, String title, final int tabPosition){
        final ArrayList<Boolean> tmpcheckList = new ArrayList<>();
        tmpcheckList.addAll(itemCheckList);
        builder .setTitle(title)
                .setPositiveButton("선택완료",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String query="";
                                for (int i = 0; i < itemCheckList.size(); i++) {
                                    if (itemCheckList.get(i)) {
                                        itemCheckList.set(i,true);
                                        query = query.length() < 1 ? itemArray[i] : query + "_" + itemArray[i];
                                    }else{
                                        itemCheckList.set(i,false);
                                    }
                                }
                                setQuery(tabPosition, query);
                            }
                        })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < itemCheckList.size(); i++) {
                            itemCheckList.set(i,tmpcheckList.get(i));
                        }
                    }
                })
                .setMultiChoiceItems
                        (itemArray, // 체크박스 리스트 항목
                                itemCheckArray, // 초기값(선택여부) 배열
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        itemCheckList.set(which, isChecked);
                                    }
                                }); // 리스너

        return builder;
    }



    private void setQuery(int tabPosition, String query){
        switch (tabPosition){
            case 0:
                locationQuery = query;
                new LogManager().LogManager("locationQuery",locationQuery);
                break;
            case 1:
                categoryQuery = query;
                new LogManager().LogManager("categoryQuery",categoryQuery);
                break;
            case 2:
                tagQuery = query;
                new LogManager().LogManager("tagQuery",tagQuery);
                break;
        }

    }




}


/*  AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                            SearchActivity.this);
                    alertBuilder.setTitle("항목중에 하나를 선택하세요.");

                    // List Adapter 생성
                    final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            SearchActivity.this,
                            android.R.layout.select_dialog_multichoice);
                    adapter.add("사과");
                    adapter.add("딸기");
                    adapter.add("오렌지");
                    adapter.add("수박");
                    adapter.add("참외");

                    // 버튼 생성
                    alertBuilder.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });

                    // 버튼 생성
                    alertBuilder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });

                    // Adapter 셋팅
                    alertBuilder.setAdapter(adapter,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {

                                    // AlertDialog 안에 있는 AlertDialog
                                    String strName = adapter.getItem(id);
                                    AlertDialog.Builder innBuilder = new AlertDialog.Builder(
                                            SearchActivity.this);
                                    innBuilder.setMessage(strName);
                                    innBuilder.setTitle("당신이 선택한 것은 ");
                                    innBuilder
                                            .setPositiveButton(
                                                    "확인",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                    innBuilder.show();
                                }
                            });
                    alertBuilder.show();*/
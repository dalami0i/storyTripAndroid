package com.tripkorea.on.ontripkorea.tabs.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimpleList;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Edward Won on 2018-09-12.
 */

public class ListTotalFragment extends Fragment {

    private AttractionSimpleList showList, tempList;
    private double lat;
    private double lon;
    private int language;
    private int page;


    public static final ListTotalRecyclerViewAdapter listRecyclerViewAdapter
            = new ListTotalRecyclerViewAdapter();

    public Fragment newInstanceListTotalFragment(AttractionSimpleList showList, double lat, double lon, int language, int page, MainActivity main){
        this.showList = showList;
        this.lat = lat;
        this.lon = lon;
        this.language = language;
        this.page = page+1;
        listRecyclerViewAdapter.addContext(main);
        return new ListTotalFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_rv, container, false);
        RecyclerView listRV = view.findViewById(R.id.item_list);
        LinearLayoutManager linkLayoutManager
                = new LinearLayoutManager(MyApplication.getContext(), LinearLayoutManager.VERTICAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        listRV.setLayoutManager(linkLayoutManager);
//        snapHelper.attachToRecyclerView(photoFoodRV);


        listRecyclerViewAdapter.clearList();
        for(int i=0; i<showList.getItems().size(); i++){
            listRecyclerViewAdapter.addListView(MainActivity.totalList.getItems().get(i));
        }

        listRV.setAdapter(listRecyclerViewAdapter);
        listRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;

//                new LogManager().LogManager("listRV.addOnScrollListener","lastVisibleItemPosition: "+lastVisibleItemPosition+" | itemTotalCount: "+itemTotalCount);
                if (lastVisibleItemPosition == itemTotalCount) {
                    new LogManager().LogManager("ListTotalFragment","listRV.addOnScrollListener");
                    setTotals(lat, lon);

                }
            }
        });


        return view;
    }


    private void setTotals(final double lat,final double lon) {
        ApiClient.getInstance().getApiService()
                .getAroundAttractions(MyApplication.APP_VERSION,lat, lon, language, page)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        new LogManager().LogManager("listTotal엑티비티","settotal : "+lat+" | "+lon);
                        if (response.body() != null) {

                            tempList = new AttractionSimpleList();
                            tempList.setItems(response.body());
                            new LogManager().LogManager("listTotal엑티비티","tempList size: "+tempList.getItems().size());
                            MainActivity.totalList.getItems().addAll(tempList.getItems());
                            listRecyclerViewAdapter.clearList();
                            for(int i=0;i<MainActivity.totalList.getItems().size(); i++){
                                listRecyclerViewAdapter.addListView(MainActivity.totalList.getItems().get(i));
                            }
                            listRecyclerViewAdapter.notifyDataSetChanged();
                            page++;
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
}

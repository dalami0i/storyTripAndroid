package com.tripkorea.on.ontripkorea.tabs.around;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.retrofit.client.ApiClient;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.tabs.around.detail.AroundDetailActivity;
import com.tripkorea.on.ontripkorea.util.Alert;
import com.tripkorea.on.ontripkorea.util.Coordinate;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.util.OnNetworkErrorListener;
import com.tripkorea.on.ontripkorea.vo.attraction.AttrClient;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by YangHC on 2018-06-05.
 */

public class AroundFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    private final static int TAB_ROUTE = 0;
    private final static int TAB_RESTAURANT = 1;
    private final static int TAB_TOUR = 2;


    private List<AttractionSimple> aroundRouteList = new ArrayList<>();
    private List<AttractionSimple> restaurantList = new ArrayList<>();
    private List<AttractionSimple> tourList = new ArrayList<>();

    int lastTab;

    //locale
    String usinglanguage;
    Locale locale;

    //중심 여행지 위치 (현재는 창덕궁)
    Coordinate coordinate = new Coordinate(37.579108, 126.990957);

    //map
    MapView aroundMap;
    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    Location currentLocation;
    double currentLat, currentLong;
    //authority
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    //tab
    TabLayout aroundTabs;

    //rv
    RecyclerView aroundRV;

    private AroundRecyclerViewAdapter aroundRouteRecyclerViewAdapter;
    private AroundRecyclerViewAdapter aroundRestaurantsRecyclerViewAdapter;
    private AroundRecyclerViewAdapter aroundToursRecyclerViewAdapter;
    LinearLayoutManager lm;

    public ArrayList<AttrClient> aroundList = new ArrayList<>();

    MainActivity main;

    public AroundFragment aroundFragmentNewInstance(MainActivity main, GoogleMap mMap, int lastTab) {
        this.lastTab = lastTab;
        this.main = main;
        this.mMap = mMap;
        return new AroundFragment();
    }

    public OnNetworkErrorListener onNetworkErrorListener;

    public void setOnNetworkErrorListener(OnNetworkErrorListener onNetworkErrorListener) {
        this.onNetworkErrorListener = onNetworkErrorListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_around, container, false);

        initViews(view);

        if (aroundMap != null) {
            aroundMap.onCreate(savedInstanceState);
        }
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        aroundMap.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        aroundMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        aroundMap.onPause();
    }

    private void initViews(View view) {
        Log.d("initViews","start");

        aroundMap = view.findViewById(R.id.around_map);
        aroundTabs = view.findViewById(R.id.around_tabs);
        aroundRV = view.findViewById(R.id.around_rv);
        aroundMap.getMapAsync(this);

        //사용자 언어 확인
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }

        usinglanguage = locale.getDisplayLanguage();

        int language = 0;
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

        aroundTabs.addTab(aroundTabs.newTab().setText(getString(R.string.around_tab_transportaion)));
        aroundTabs.addTab(aroundTabs.newTab().setText(getString(R.string.around_tab_food)));
        aroundTabs.addTab(aroundTabs.newTab().setText(getString(R.string.around_tab_attraction)));


        lm = new LinearLayoutManager(MyApplication.getContext());
        DividerItemDecoration dividerItemDecorationLinkTransportation
                = new DividerItemDecoration(aroundRV.getContext(), lm.getOrientation());
        aroundRV.addItemDecoration(dividerItemDecorationLinkTransportation);
        aroundRV.setLayoutManager(lm);

        Log.e("showaround", "tab 위치: " + aroundTabs.getSelectedTabPosition());

        //2018 06 24 20:10 kiryun 추가
        setRoute(coordinate.getLat(), coordinate.getLon(), 1);
        Log.e("showaround", "aroundRouteList 갯수 :"+ aroundRouteList.size());

        aroundRouteRecyclerViewAdapter = new AroundRecyclerViewAdapter(aroundRouteList, main, aroundTabs.getSelectedTabPosition(), coordinate, mMap);
        aroundRV.setAdapter(aroundRouteRecyclerViewAdapter);

        aroundTabs.addOnTabSelectedListener(tabSelectedListener);
    }

    // 2018 06 24 20:06 kiryun 추가
    // TODO : 서버에서 루트 정보 가져오기
    private void setRoute(double lat, double lon, int page)
    {
        ApiClient.getInstance().getApiService()
                .getAroundRoutes(MyApplication.APP_VERSION, 1024)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        List<AttractionSimple> aroundRoutes = response.body();
                        if(response.body() != null)
                        {
                            aroundRouteList.addAll(response.body());
                            Log.e("ROUTES", "size : "+aroundRouteList.size());

                            for(AttractionSimple aroundRoute : aroundRoutes)
                            {
                                Log.e("ROUTES", aroundRoute.getName());
                            }
                            aroundRouteRecyclerViewAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Log.e("ROUTES", "실패");
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("ROUTES", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AttractionSimple>> call, Throwable t) {

                    }
                });
    }

    // TODO : 서버에서 주변 관광지 정보 가져오기
    private void setRestaurants(double lat, double lon, int page) {
        ApiClient.getInstance().getApiService()
                .getAroundRestaurants(MyApplication.APP_VERSION, lat, lon, page)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        List<AttractionSimple> restaurants = response.body();
                        if (response.body() != null) {
                            restaurantList.addAll(response.body());
                            Log.e("RESTAURANTS", "size : " + restaurantList.size());

                            for (AttractionSimple restaurant : restaurants) {
                                Log.e("RESTAURANTS", restaurant.getName());
                            }
                            aroundRestaurantsRecyclerViewAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("RESTAURANTS", "실패");
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("RESTAURANTS", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AttractionSimple>> call, Throwable t) {
                        Alert.makeText(getString(R.string.network_error));
                        Log.e("NETWORK", t.getMessage());
                        t.printStackTrace();
                    }
                });
    }

    // TODO : 서버에서 주변 관광지 정보 가져오기
    private void setTours(double lat, double lon, int page) {
        ApiClient.getInstance().getApiService()
                .getAroundTours(MyApplication.APP_VERSION, lat, lon, page)
                .enqueue(new Callback<List<AttractionSimple>>() {
                    @Override
                    public void onResponse(Call<List<AttractionSimple>> call, Response<List<AttractionSimple>> response) {
                        List<AttractionSimple> restaurants = response.body();
                        if (response.body() != null) {
                            tourList.addAll(response.body());
                            Log.e("TOURS", "size : " + tourList.size());

                            for (AttractionSimple restaurant : restaurants) {
                                Log.e("TOURS", restaurant.getName());
                            }
                            aroundToursRecyclerViewAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("TOURS", "실패");
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("TOURS", "error : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AttractionSimple>> call, Throwable t) {
                        Alert.makeText(getString(R.string.network_error));
                        Log.e("NETWORK", t.getMessage());
                        t.printStackTrace();
                    }
                });
    }

    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Log.e("showaround", "tab 위치: " + aroundTabs.getSelectedTabPosition());

            switch (aroundTabs.getSelectedTabPosition()) {
                case TAB_ROUTE:
                    if (aroundRouteRecyclerViewAdapter == null) {
                        aroundRouteRecyclerViewAdapter = new AroundRecyclerViewAdapter(aroundRouteList, main, aroundTabs.getSelectedTabPosition(), coordinate, mMap);
                    }
                    aroundRV.setAdapter(aroundRouteRecyclerViewAdapter);
                    if (aroundRouteList.size() == 0) {
//                        setRestaurants(coordinate.getLat(), coordinate.getLon(), 1);
                        setRoute(coordinate.getLat(), coordinate.getLon(), 1);

                    }
                    break;
                case TAB_RESTAURANT:
                    if (aroundRestaurantsRecyclerViewAdapter == null) {
                        aroundRestaurantsRecyclerViewAdapter = new AroundRecyclerViewAdapter(restaurantList, main, aroundTabs.getSelectedTabPosition(), coordinate, mMap);
                    }
                    aroundRV.setAdapter(aroundRestaurantsRecyclerViewAdapter);
                    if (restaurantList.size() == 0) {
                        setRestaurants(coordinate.getLat(), coordinate.getLon(), 1);
                    }
                    break;
                case TAB_TOUR:
                    if (aroundToursRecyclerViewAdapter == null) {
                        aroundToursRecyclerViewAdapter = new AroundRecyclerViewAdapter(tourList, main, aroundTabs.getSelectedTabPosition(), coordinate, mMap);
                    }
                    aroundRV.setAdapter(aroundToursRecyclerViewAdapter);
                    if (tourList.size() == 0) {
                        setTours(coordinate.getLat(), coordinate.getLon(), 1);
                    }
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            Log.e("언셀렉티드", tab.getText() + " | ");
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };


    private void checkMyLocation() {
        if (ContextCompat.checkSelfPermission(MyApplication.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager)
                    MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = locationManager.getBestProvider(criteria, true);
            currentLocation = locationManager.getLastKnownLocation(provider);
            if (currentLocation != null) {
                currentLong = currentLocation.getLongitude();
                currentLat = currentLocation.getLatitude();
            } else {
                Toast.makeText(MyApplication.getContext(), R.string.failtoloadlocation, Toast.LENGTH_LONG).show();
                currentLong = 37.577401;
                currentLat = 126.989511;
            }
//        double dist = distance(latitude, longitude, currentLat, currentLong, "meter");

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        currentLong = location.getLongitude(); //경도
        currentLat = location.getLatitude(); //위도
        Log.e("onLocationChanged", "현재위치 " + currentLong + " | " + currentLat);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
//        mMap.clear();
//        Log.e("onMapReady","맵 청소 함");
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (ContextCompat.checkSelfPermission(MyApplication.getContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MyApplication.getContext(), "위치 권한 허락 요망", Toast.LENGTH_LONG).show();
                } else {
                    checkMyLocation();
                    LocationManager locationManager = (LocationManager)
                            MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
//                Location mylocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (currentLocation == null) {
                        Criteria criteria = new Criteria();
                        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                        String provider = locationManager.getBestProvider(criteria, true);
                        currentLocation = locationManager.getLastKnownLocation(provider);
                        final double latitude;
                        final double longitude;
                        if (currentLocation == null) {
                            currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            latitude = currentLocation.getLatitude();
                            currentLat = latitude;
                            longitude = currentLocation.getLongitude();
                            currentLong = longitude;
                        } else {
                            latitude = currentLocation.getLatitude();
                            currentLat = latitude;
                            longitude = currentLocation.getLongitude();
                            currentLong = longitude;


                        }
                    }

                    checkAround(mMap);
                }

                return false;
            }
        });

        checkAround(mMap);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        if (currentLong < 1) {
            currentLong = 126.989297;
            currentLat = 37.577395;
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLat, currentLong)));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.e("map InfoWindowClick", "marker.getTitle(): " + marker.getTitle());
                for (int i = 0; i < aroundList.size(); i++) {
                    if (marker.getTitle().equals(aroundList.get(i).title)) {
                        try {
                            int tempTest = Integer.parseInt(aroundList.get(i).contentID);
                            Intent intent = new Intent(main, AroundDetailActivity.class);
                            intent.putExtra("attraction", aroundList.get(i));
                            intent.putExtra("aroundList", aroundList);
                            startActivity(intent);
                        } catch (NumberFormatException e) {
                            AlertDialog dialog = createDialogBoxHome(aroundList.get(i));
                            dialog.show();
                        }
                    }
                }
            }


            private Drawable LoadImageFromWebOperations(String url) {
                try {
                    InputStream is = (InputStream) new URL(url).getContent();
                    return Drawable.createFromStream(is, "src name");
                } catch (Exception e) {
                    System.out.println("dialog drawable Exc=" + e);
                    return null;
                }
            }

            private AlertDialog createDialogBoxHome(AttrClient attrClient) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getContext());
                builder.setTitle(attrClient.title);
                builder.setMessage(attrClient.description);
                builder.setIcon(LoadImageFromWebOperations(attrClient.firstimage));
//            builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton(R.string.dialogyestitle, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGoogleApiClient.disconnect();
                    }

                });
                return builder.create();
            }

        });

    }

    private void checkAround(GoogleMap mMap) {
        Log.e("checkAround", "주변점검 몇 개? " + aroundList.size());
        for (int i = 0; i < aroundList.size(); i++) {
//            if(i != 0 && i != 9 && i!=58) {
            String tmpY = aroundList.get(i).mapy.trim();
            String tmpX = aroundList.get(i).mapx.trim();

            LatLng location =
                    new LatLng(Double.parseDouble(tmpY), Double.parseDouble(tmpX));
            switch (aroundList.get(i).categoryNum) {
                case "4":
                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(aroundList.get(i).title)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_history)))
                            .setTag(aroundList.get(i).contentID);
                    break;
                case "6":
                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(aroundList.get(i).title)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_museum)))
                            .setTag(aroundList.get(i).contentID);
                    break;
                case "1":
                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(aroundList.get(i).title)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_activity)))
                            .setTag(aroundList.get(i).contentID);
                    break;
                case "3":
                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(aroundList.get(i).title)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_food)))
                            .setTag(aroundList.get(i).contentID);
                    break;
                default:
                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(aroundList.get(i).title)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.z_marker_bus)))
                            .setTag(aroundList.get(i).contentID);
                    break;
            }
//            }

//            String id = String.valueOf(aroundList.get(i).contentID);
//            new YoutubeAsyncTask().execute(id, new GetYoutubeKey().takeYoutubekey(id));
        }
    }
}

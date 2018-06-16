package com.tripkorea.on.ontripkorea.tabs.info;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.tabs.around.AroundDetailActivity;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttrClient;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by YangHC on 2018-06-05.
 */

public class InfoFragment extends Fragment {

    MainActivity main;

    //locale

    ViewPager informationImgVP;
    String usinglanguage;
    Locale locale;

    //rv
    RecyclerView myRV;

    InformRecyclerViewAdapter informRecyclerViewAdapter;
    ArrayList<AttrClient> myInformRV = new ArrayList<>();


    boolean onCreated;

    public InfoFragment infoFragmentNewInstance(MainActivity main){
        this.main = main;
        return new InfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        informationImgVP = view.findViewById(R.id.information_image_vp);
        myRV = view.findViewById(R.id.my_rv);


        //사용자 언어 확인
        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }

        usinglanguage = locale.getDisplayLanguage();

        ChangImageFragmentPagerAdapter changImageFragmentPagerAdapter
                = new ChangImageFragmentPagerAdapter(getChildFragmentManager());
        changImageFragmentPagerAdapter.addChangImage("https://upload.wikimedia.org/wikipedia/commons/thumb/c/cb/Changdeokgung-Injeongjeon.jpg/1200px-Changdeokgung-Injeongjeon.jpg");
        informationImgVP.setAdapter(changImageFragmentPagerAdapter);

        LinearLayoutManager linearLayoutManagerMy = new LinearLayoutManager(MyApplication.getContext());
        DividerItemDecoration dividerItemDecorationLinkLike
                = new DividerItemDecoration(myRV.getContext(), linearLayoutManagerMy.getOrientation());
        myRV.addItemDecoration(dividerItemDecorationLinkLike);
        myRV.setLayoutManager(linearLayoutManagerMy);
        onCreated = true;
        if(onCreated) {
            myInformRV.addAll(MainActivity.likeEntities);
            myInformRV.addAll(MainActivity.traceEntities);
        }
        Log.e("인폼 리스트 사이즈",myInformRV.size()+" | ");
        informRecyclerViewAdapter
                = new InformRecyclerViewAdapter(myInformRV, main);
        myRV.setAdapter( informRecyclerViewAdapter );


        onCreated = false;


        initViews(view);


        return view;
    }

    private void initViews(View view) {

    }


    public static class InformRecyclerViewAdapter extends RecyclerView.Adapter<InformRecyclerViewAdapter.ViewHolder> {
        private ArrayList<AttrClient> findingList;
        MainActivity owner;
        int secondTitle;

        private InformRecyclerViewAdapter(ArrayList<AttrClient> resources, MainActivity owner) {
            findingList = resources;
            this.owner = owner;
        }


        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final View mView;
            private final RoundedImageView aroundThumnailImg;
            private final TextView aroundName;
            private final TextView aroundIntro;
            private final TextView aroundDesc;
            private final TextView aroundDistance;
            private final TextView aroundMenuTitle;

            private ViewHolder(View view) {
                super(view);
                mView = view;
                aroundThumnailImg = view.findViewById(R.id.around_item_thumbnail);
                aroundName = view.findViewById(R.id.txt_view_show_around_title);
                aroundIntro = view.findViewById(R.id.txt_view_show_around_intro);
                aroundMenuTitle = view.findViewById(R.id.around_menu_title);
                aroundDesc = view.findViewById(R.id.txt_view_show_around_detail);
                aroundDistance = view.findViewById(R.id.txt_view_show_around_distance);
            }
        }

        @Override
        public int getItemViewType(int position) {
//            Log.e("InformMy like getItemViewType",position+" | ");
            int type = 0;
            secondTitle = MainActivity.likeEntities.size();
            if(position == 0 ){
                type=1;
            }else if(position == secondTitle){
                type=1;
            }
            return type;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
//            Log.e("InformMy like onCreateViewHolder",viewType+" | ");
            switch(viewType){
                case 1:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_show_around_title, parent, false);
                    break;
                default:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_show_around, parent, false);
                    break;
            }

            return new ViewHolder(view);
        }



        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final AttrClient findingResult = findingList.get(position);
//            Log.e("InformMy like onBindViewHolder",position+" | "+findingResult.title);
            switch(position){
                case 0:
                    holder.aroundMenuTitle.setText(findingResult.title);
                    break;
                default:
                    if(position == secondTitle){
                        holder.aroundMenuTitle.setText(findingResult.title);
                        break;
                    }
                    Glide.with(owner).load(findingResult.firstimage).into( holder.aroundThumnailImg);
                    holder.aroundThumnailImg.setCornerRadius(20);
                    holder.aroundName.setText(findingResult.title);
                    holder.aroundIntro.setText(findingResult.summary);
                    holder.aroundDesc.setText(findingResult.description);
                    String distance = (int)findingResult.distance + " m";
                    holder.aroundDistance.setText(distance);
                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            try{
                                int temp = Integer.parseInt(findingResult.contentID);
                                Intent intent = new Intent(owner, AroundDetailActivity.class);
                                intent.putExtra("attraction", findingResult);
                                owner.startActivity(intent);
                            }catch (NumberFormatException e){
                                Toast.makeText(owner,owner.getString(R.string.transportation_clicked),Toast.LENGTH_LONG).show();
                            }


                        }
                    });
                    break;
            }


        }
        @Override
        public int getItemCount() {
            return findingList.size();
        }
    }





    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //디테일 상단 이미지를 위한 view pager
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class ChangImageFragmentPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<String> detailedImageFragments = new ArrayList<>();

        private void addChangImage(String detailedImage){
            detailedImageFragments.add(detailedImage);
        }


        private ChangImageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public float getPageWidth(int position) {
            return 1.0f;
        }

        @Override
        public Fragment getItem(int position) {
            return new DetailedImageListFragment().newInstance(
                    detailedImageFragments.get(position), detailedImageFragments.size(), position);
        }

        @Override
        public int getCount() {
            return detailedImageFragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    public static class DetailedImageListFragment extends Fragment {
        private String detailedImgAddr;
        MainActivity owner;
        RoundedImageView detailed_img;
        int size;
        int position;
        int width;
        int height;
        Unbinder unbinder;

        public DetailedImageListFragment() { }

        public static DetailedImageListFragment newInstance(
                String detailedImgAddr, int size, int position) {
            Bundle bundle = new Bundle();
            bundle.putInt("totalsize", size);
            bundle.putInt("position", position);
            bundle.putString("detailedImage",detailedImgAddr);
            DetailedImageListFragment detailedImageListFragment = new DetailedImageListFragment();
            detailedImageListFragment.setArguments(bundle);
            return detailedImageListFragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                size = getArguments().getInt("totalsize");
                position = getArguments().getInt("position");
                width = getArguments().getInt("width");
                height = getArguments().getInt("height");
                detailedImgAddr = getArguments().getString("detailedImage");
            }
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            LinearLayout view = (LinearLayout) inflater.inflate(R.layout.detaileditem_image, container, false);
            unbinder = ButterKnife.bind(this, view);
            owner = (MainActivity) getActivity();
            detailed_img = view.findViewById(R.id.detailed_img);
            TextView right_arrow = view.findViewById(R.id.right_arrow);
            TextView left_arrow = view.findViewById(R.id.left_arrow);

            Log.e("길이와 위치:","길이: "+size+" 위치: "+detailedImgAddr);

            if(owner != null) {
                Glide.with(owner).load(detailedImgAddr).into(detailed_img);
                if(size == 1){
                    right_arrow.setVisibility(View.GONE);
                    left_arrow.setVisibility(View.GONE);
                }else if(position == 0){
                    right_arrow.setVisibility(View.GONE);
                    left_arrow.setVisibility(View.VISIBLE);
                }else if(position == (size-1)){
                    right_arrow.setVisibility(View.VISIBLE);
                    left_arrow.setVisibility(View.GONE);
                }else{
                    right_arrow.setVisibility(View.VISIBLE);
                    left_arrow.setVisibility(View.VISIBLE);
                }
            }



            return view;
        }
        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }
    }

}

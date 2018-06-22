package com.tripkorea.on.ontripkorea.tabs.intro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by YangHC on 2018-06-18.
 */

public class MainImageListFragment extends Fragment {
    private String mainImgAddr;
    RoundedImageView main_img;
    int size;
    int position;
    Unbinder unbinder;

    public MainImageListFragment() { }

    public static MainImageListFragment newInstance(
            String mainImgAddr, int size, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("totalsize", size);
        bundle.putInt("position", position);
        bundle.putString("mainImage",mainImgAddr);
        MainImageListFragment mainImageListFragment = new MainImageListFragment();
        mainImageListFragment.setArguments(bundle);
        return mainImageListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            size = getArguments().getInt("totalsize");
            position = getArguments().getInt("position");
            mainImgAddr = getArguments().getString("mainImage");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.mainpageimage, container, false);
        unbinder = ButterKnife.bind(this, view            );
        MainActivity owner = (MainActivity) getActivity();
        main_img = view.findViewById(R.id.main_img);
        Glide.with(owner).load(mainImgAddr).into(main_img);
        TextView right_arrow = view.findViewById(R.id.right_arrow);
        TextView left_arrow = view.findViewById(R.id.left_arrow);
//            if(popular == 1) {
//                detailed_img.setLayoutParams(new LinearLayout.LayoutParams(width, height-80));
//            }
//            new org.androidtown.realchangdeokgung.LogManager()Log.e("길이와 위치:","길이: "+size+" 위치: "+mainImgAddr);

        if(owner != null) {
            Glide.with(owner).load(mainImgAddr).into(main_img);
//                )Log.e("디테일","detailedImgAddr.getImgURL(): "+detailedImgAddr.getImgURL());
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
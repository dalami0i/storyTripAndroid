package com.tripkorea.on.ontripkorea.tabs.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tripkorea.on.ontripkorea.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by YangHC on 2018-06-18.
 */

public class ListDetailedImageListFragment extends Fragment {
    private String detailedImgAddr;
    ListDetailActivity owner;
    RoundedImageView detailed_img;
    int size;
    int position;
    int width;
    int height;
    int popular;
    Unbinder unbinder;

    public ListDetailedImageListFragment() {
    }

    public static ListDetailedImageListFragment newInstance(
            String detailedImgAddr, int size, int position, int popular) {
        Bundle bundle = new Bundle();
        bundle.putInt("totalsize", size);
        bundle.putInt("position", position);
        bundle.putInt("popular", popular);
        bundle.putString("detailedImage", detailedImgAddr);
        ListDetailedImageListFragment detailedImageListFragment = new ListDetailedImageListFragment();
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
            popular = getArguments().getInt("popular");
            detailedImgAddr = getArguments().getString("detailedImage");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.detaileditem_image, container, false);
        unbinder = ButterKnife.bind(this, view);
        owner = (ListDetailActivity) getActivity();
        detailed_img = view.findViewById(R.id.detailed_img);
        TextView right_arrow = view.findViewById(R.id.right_arrow);
        TextView left_arrow = view.findViewById(R.id.left_arrow);


        Log.e("길이와 위치:", "길이: " + size + " 위치: " + detailedImgAddr);

        if (owner != null) {
            Glide.with(owner).load(detailedImgAddr).into(detailed_img);
            if (size == 1) {
                right_arrow.setVisibility(View.GONE);
                left_arrow.setVisibility(View.GONE);
            } else if (position == 0) {
                right_arrow.setVisibility(View.GONE);
                left_arrow.setVisibility(View.VISIBLE);
            } else if (position == (size - 1)) {
                right_arrow.setVisibility(View.VISIBLE);
                left_arrow.setVisibility(View.GONE);
            } else {
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

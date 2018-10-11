package com.tripkorea.on.ontripkorea.tabs.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.tabs.MainActivity;
import com.tripkorea.on.ontripkorea.tabs.list.ListDetailActivity;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;

///**
// * Created by Edward Won on 2018-09-12.
// */

public class MapTourBottomFragment extends Fragment {

    private MainActivity main;
    AttractionSimple attraction;


    public Fragment newInstanceMapBootomFragment(AttractionSimple attraction, MainActivity main){
        this.attraction = attraction;
        new LogManager().LogManager("MapTourBottomFragment","attraction.getName(): "+attraction.getName());
        new LogManager().LogManager("MapTourBottomFragment","this.attraction.getName(): "+this.attraction.getName());
        this.main = main;
        return  new MapTourBottomFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rv_map_bottom_item, container, false);
        new LogManager().LogManager("MapTourBottomFragment","onCreateView 생성 ");
        ImageView itemImg = view.findViewById(R.id.iv_map_bottom);
        TextView itemImgName = view.findViewById(R.id.tv_map_bottom_title);
        TextView itemTag1 = view.findViewById(R.id.tv_map_bottom_tag1);
        TextView itemTag2 = view.findViewById(R.id.tv_map_bottom_tag2);
        TextView itemTag3 = view.findViewById(R.id.tv_map_bottom_tag3);

        itemImgName.setText(attraction.getName());
        String tmpTag;
        if(attraction.getTagSet() != null && attraction.getTagSet().size() > 0){
            for(int i=0; i<attraction.getTagSet().size(); i++) {
                switch (i){
                    case 0:
                        tmpTag = "#" + attraction.getTagSet().get(i);
                        itemTag1.setText(tmpTag);
                        itemTag2.setVisibility(View.GONE);
                        itemTag3.setVisibility(View.GONE);
                        break;
                    case 1:
                        tmpTag = "#" + attraction.getTagSet().get(i);
                        itemTag2.setText(tmpTag);
                        itemTag2.setVisibility(View.VISIBLE);
                        itemTag3.setVisibility(View.GONE);
                        break;
                    case 2:
                        tmpTag = "#" + attraction.getTagSet().get(i);
                        itemTag3.setText(tmpTag);
                        itemTag3.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }else{
            String nameTag = "#"+attraction.getName();
            itemTag1.setText(nameTag);
            itemTag2.setVisibility(View.GONE);
            itemTag3.setVisibility(View.GONE);
        }
//        RequestOptions myOptions = new RequestOptions().override(diviceSizeW, 900);
        Glide.with(MyApplication.getContext())
                .load(attraction.getThumnailAddr())
                .into(itemImg);
        //.apply(myOptions)

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogManager().LogManager("MapTourBottomFragment","item clicked : "+attraction.getIdx());
                Intent intent = new Intent(main, ListDetailActivity.class);
                intent.putExtra("attractionIdx",attraction.getIdx());
                main.startActivity(intent);
            }
        });


        return view;
    }





}

package com.tripkorea.on.ontripkorea.tabs.guide;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;

import java.util.ArrayList;
import java.util.Locale;

///**
// * Created by YangHC on 2018-06-18.
// */


public class GuideTabRecyclerViewAdapter extends RecyclerView.Adapter<GuideTabRecyclerViewAdapter.ViewHolder> {

    ArrayList<AttractionSimple> guidePoster = new ArrayList<>();
    int diviceSizeW;
    static Context context;

    public void addGuideTabPhotoList(AttractionSimple obj) {//, String link_content
        guidePoster.add(obj);
    }

    public GuideTabRecyclerViewAdapter() {    }

    public GuideTabRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivMainGuide;
//        private final TextView tvGuideTitle;
        private final View mView;

        private ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ivMainGuide = mView.findViewById(R.id.iv_guide_main);
//            tvGuideTitle = mView.findViewById(R.id.tv_guide_title);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_guide_main, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AttractionSimple guideItem = guidePoster.get(position);
        Log.e("Total onBindViewHolder", position + " | " + guideItem.getThumnailAddr());
        new LogManager().LogManager("guideItem.getName(): ",guideItem.getName());
        new LogManager().LogManager("guideItem.getIdx(): ",guideItem.getIdx()+"");

        Locale locale;
        //사용자 언어 확인
        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.N) {
            locale = MyApplication.getContext().getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = MyApplication.getContext().getResources().getConfiguration().locale;
        }

        String usinglanguage = locale.getDisplayLanguage();

        switch(guideItem.getGuideType() ){
            case 1000:
                if(usinglanguage.equals("한국어")) {
                    Glide.with(MyApplication.getContext())
                            .load(R.drawable.guide_intro_cartoon_ko)
                            .into(holder.ivMainGuide);
                    //guideItem.getThumnailAddr()
                }else{
                    Glide.with(MyApplication.getContext())
                            .load(R.drawable.guide_intro_cartoon_en)
                            .into(holder.ivMainGuide);
                    //guideItem.getThumnailAddr()
                }
                break;
            case 2000:
                if(usinglanguage.equals("한국어")) {
                    Glide.with(MyApplication.getContext())
                            .load(R.drawable.guide_intro_voice_ko)
                            .into(holder.ivMainGuide);
                    //guideItem.getThumnailAddr()
                }else{
                    Glide.with(MyApplication.getContext())
                            .load(R.drawable.guide_intro_voice_en)
                            .into(holder.ivMainGuide);
                    //guideItem.getThumnailAddr()
                }
                break;
        }

//        holder.tvGuideTitle.setText(guideItem.getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogManager().LogManager("guideItem.getType()",guideItem.getType()+"");
                switch(guideItem.getGuideType() ){
                    case 1000:
                        Intent toonIntent = new Intent(context, GuideActivity.class);
                        toonIntent.putExtra("guideIdx",guideItem.getIdx());
                        context.startActivity(toonIntent);
                        break;
                    case 2000:
                        Intent voicdIntent = new Intent(context, VoiceGuideActivity.class);
                        voicdIntent.putExtra("guideIdx",guideItem.getIdx());
                        context.startActivity(voicdIntent);
                        break;
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return guidePoster.size();
    }


}

///////////////////////////////////////////////////////////////////////////////////////Insta layout 참조
//    RequestOptions myOptions = new RequestOptions().override(diviceSizeW, 900);
//        Glide.with(MyApplication.getContext())
//                .load(guidePhoto.getGuideImageAddrs().get(0))
//                .apply(myOptions)
//                .into(holder.firstPhoto);
//                Glide.with(MyApplication.getContext())
//                .load(guidePhoto.getGuideImageAddrs().get(1))
//                .apply(myOptions)
//                .into(holder.secondPhoto);
//                Glide.with(MyApplication.getContext())
//                .load(guidePhoto.getGuideImageAddrs().get(2))
//                .apply(myOptions)
//                .into(holder.thirdPhoto);
//                Glide.with(MyApplication.getContext())
//                .load(guidePhoto.getGuideImageAddrs().get(3))
//                .apply(myOptions)
//                .into(holder.forthPhoto);
//                Glide.with(MyApplication.getContext())
//                .load(guidePhoto.getGuideImageAddrs().get(4))
//                .apply(myOptions)
//                .into(holder.fifthPhoto);
//                Glide.with(MyApplication.getContext())
//                .load(guidePhoto.getGuideImageAddrs().get(5))
//                .apply(myOptions)
//                .into(holder.sixthPhoto);
//
//
//
//
//                holder.firstPhoto.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        Intent intent = new Intent(context, AroundDetailMapActivity.class);//AroundDetailActivity
//        intent.putExtra("attractionIdx", guidePhoto.getIdx());
//        context.startActivity(intent);
//        }
//        });
//        holder.secondPhoto.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        Intent intent = new Intent(context, AroundDetailMapActivity.class);
//        intent.putExtra("attractionIdx",guidePhoto.getIdx());
//        context.startActivity(intent);
//        }
//        });
//        holder.thirdPhoto.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        Intent intent = new Intent(context, AroundDetailMapActivity.class);
//        intent.putExtra("attractionIdx",guidePhoto.getIdx());
//        context.startActivity(intent);
//        }
//        });
//        holder.forthPhoto.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        Intent intent = new Intent(context, AroundDetailMapActivity.class);
//        intent.putExtra("attractionIdx",guidePhoto.getIdx());
//        context.startActivity(intent);
//        }
//        });
//        holder.fifthPhoto.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        Intent intent = new Intent(context, AroundDetailMapActivity.class);
//        intent.putExtra("attractionIdx",guidePhoto.getIdx());
//        context.startActivity(intent);
//        }
//        });
//        holder.sixthPhoto.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        Intent intent = new Intent(context, AroundDetailMapActivity.class);
//        intent.putExtra("attractionIdx",guidePhoto.getIdx());
//        context.startActivity(intent);
//        }
//        });
//
//    @Override
//    public int getItemViewType(int position) {
//        if(position%2 == 1){
//            return VIEW_TYPE_ODD;
//        }else{
//            return VIEW_TYPE_EVEN;
//        }
//    }
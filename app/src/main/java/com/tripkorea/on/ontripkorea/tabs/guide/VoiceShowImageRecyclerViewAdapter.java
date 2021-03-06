package com.tripkorea.on.ontripkorea.tabs.guide;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.voiceguide.GuideImage;

import java.util.ArrayList;

///**
// * Created by YangHC on 2018-06-18.
// */

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//voice guide 이미지를 위한 RecyclerView
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class VoiceShowImageRecyclerViewAdapter extends RecyclerView.Adapter<VoiceShowImageRecyclerViewAdapter.ViewHolder> {
    ArrayList<GuideImage> voiceIMGlist = new ArrayList<>();
    int diviceSizeW;
    Context context;

    public void addVoiceImgList(GuideImage obj) {//, String link_content
        voiceIMGlist.add(obj);
    }

    public VoiceShowImageRecyclerViewAdapter() {
    }

    public VoiceShowImageRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final PhotoView voiceguide_img_item;
//        private final TextView voiceguide_img_text;
        private final TextView right_arrow;
        private final TextView left_arrow;
        private final View mView;

        private ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            voiceguide_img_item = mView.findViewById(R.id.detailed_photo_view);
//            voiceguide_img_text = mView.findViewById(R.id.txt_voiceguide);
            right_arrow = mView.findViewById(R.id.right_arrow);
            left_arrow = mView.findViewById(R.id.left_arrow);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_guide_detail_img, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GuideImage voiceIMGEntity = voiceIMGlist.get(position);
        Log.e("맵onBindViewHolder", position + " | " + voiceIMGEntity.getDescription());
        if(voiceIMGEntity.getImgAddress().length() < 50) {
            voiceIMGEntity.setImgAddress("http://13.125.83.183:8080/resources/guides/voice/img/" + voiceIMGEntity.getImgAddress() + ".jpg");
        }
        Log.e("보이스가이드이미지", voiceIMGEntity.getGuideIdx() + " | " + voiceIMGEntity.getImgAddress());
        /*holder.voiceguide_img_text.setText(voiceIMGEntity.getDescription());
        holder.voiceguide_img_item.setCornerRadius((float) 10);*/

        RequestOptions myOptions = new RequestOptions().fitCenter().override(diviceSizeW, 900);

        Glide.with(MyApplication.getContext())
                .load(voiceIMGEntity.getImgAddress())
                .apply(myOptions)
                .into(holder.voiceguide_img_item);

//            Glide.get(VoiceGuideMapActivity.this).clearMemory();
//            new AsyncTaskGlideCacheManager().doInBackground();
//            holder.voiceguide_img_item.setImageURI(Uri.parse(voiceIMGEntity.imgAddr));
        if (voiceIMGlist.size() == 1) {
            holder.right_arrow.setVisibility(View.GONE);
            holder.left_arrow.setVisibility(View.GONE);
        } else if (position == 0) {
            holder.right_arrow.setVisibility(View.GONE);
            holder.left_arrow.setVisibility(View.VISIBLE);
        } else if (position == voiceIMGlist.size() - 1) {
            holder.right_arrow.setVisibility(View.VISIBLE);
            holder.left_arrow.setVisibility(View.GONE);
        } else {
            holder.right_arrow.setVisibility(View.VISIBLE);
            holder.left_arrow.setVisibility(View.VISIBLE);
        }

    }



    @Override
    public int getItemCount() {
        return voiceIMGlist.size();
    }


}
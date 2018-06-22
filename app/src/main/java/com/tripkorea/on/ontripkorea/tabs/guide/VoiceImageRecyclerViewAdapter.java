package com.tripkorea.on.ontripkorea.tabs.guide;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.util.MyApplication;
import com.tripkorea.on.ontripkorea.vo.voiceguide.VoiceGuideImage;

import java.util.ArrayList;

/**
 * Created by YangHC on 2018-06-18.
 */

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//voice guide 이미지를 위한 RecyclerView
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class VoiceImageRecyclerViewAdapter extends RecyclerView.Adapter<VoiceImageRecyclerViewAdapter.ViewHolder> {
    ArrayList<VoiceGuideImage> voiceIMGlist = new ArrayList<>();
    int diviceSizeW;

    public void addVoiceImgList(VoiceGuideImage obj) {//, String link_content
        voiceIMGlist.add(obj);
    }

    public VoiceImageRecyclerViewAdapter() {
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final RoundedImageView voiceguide_img_item;
        private final TextView voiceguide_img_text;
        private final TextView right_arrow;
        private final TextView left_arrow;
        private final View mView;

        private ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            voiceguide_img_item = mView.findViewById(R.id.img_voiceguide);
            voiceguide_img_text = mView.findViewById(R.id.txt_voiceguide);
            right_arrow = mView.findViewById(R.id.right_arrow);
            left_arrow = mView.findViewById(R.id.left_arrow);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("맵onCreateViewHolder", viewType + " | ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voiceguideimage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VoiceGuideImage voiceIMGEntity = voiceIMGlist.get(position);
        Log.e("맵onBindViewHolder", position + " | " + voiceIMGEntity.voiceimgtext);
        Log.e("보이스가이드이미지", voiceIMGEntity.voiceguideid + " | " + voiceIMGEntity.imgAddr);
        holder.voiceguide_img_text.setText(voiceIMGEntity.voiceimgtext);
        holder.voiceguide_img_item.setCornerRadius((float) 10);

        RequestOptions myOptions = new RequestOptions().fitCenter().override(diviceSizeW, 900);

        Glide.with(MyApplication.getContext())
                .load(voiceIMGEntity.imgAddr)
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
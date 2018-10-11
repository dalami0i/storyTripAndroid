/*
package com.tripkorea.on.ontripkorea.tabs.around.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.vo.youtube.YoutubeDetail;

*/
/**
 * Created by YangHC on 2018-06-18.
 *//*


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//유투브 링크 adapter
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class YoutubeItemRVAdapter extends RecyclerView.Adapter<YoutubeItemRVAdapter.ViewHolder> {
    private Context context;

    public YoutubeItemRVAdapter(Context context) {
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final RoundedImageView iv_youtube_thumbnail;
        private final TextView tv_youtube_title;
        private final TextView tv_youtube_content;
        //            private final TextView  tv_youtube_intro;
//            private final TextView  tv_youtube_distance;
        private final View mView;

        private ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            iv_youtube_thumbnail = mView.findViewById(R.id.around_youtube_thumbnail);
            tv_youtube_title = mView.findViewById(R.id.around_youtube_title);
            tv_youtube_content = mView.findViewById(R.id.around_youtube_detail);
//                tv_youtube_intro = mView.findViewById(R.id.around_youtube_intro);
//                tv_youtube_distance = mView.findViewById(R.id.around_youtube_distance);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.showaroundyoutubeitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final YoutubeDetail youtubeDetail = AroundDetailActivity.youtubeDetails.get(position);
        holder.tv_youtube_title.setText(youtubeDetail.title);
        holder.tv_youtube_content.setText(youtubeDetail.description);
        holder.iv_youtube_thumbnail.setCornerRadius((float) 10);
        String[] youtubeKeys = youtubeDetail.youtubeKey.split(",");
        String youtubeAddr = context.getString(R.string.youtube_img_former) + youtubeKeys[position] + context.getString(R.string.youtube_img_later);
        Glide.with(context).load(youtubeAddr).into(holder.iv_youtube_thumbnail);
//            Log.e("youtube",youtubeKey+"| title: "+linkEntity.youtubetitle);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(context.getString(R.string.youtube_connect) + youtubeDetail.youtubeKey);
                Intent goyoutube = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(goyoutube);
            }
        });
    }

    @Override
    public int getItemCount() {
        return AroundDetailActivity.youtubeDetails.size();
    }
}
*/

package com.tripkorea.on.ontripkorea.tabs.info;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.tabs.around.detail.AroundDetailActivity;
import com.tripkorea.on.ontripkorea.util.Coordinate;
import com.tripkorea.on.ontripkorea.util.LocationDistance;
import com.tripkorea.on.ontripkorea.vo.attraction.Attraction;

import java.util.List;

/**
 * Created by YangHC on 2018-06-18.
 */

public class InfoRecyclerViewAdapter extends RecyclerView.Adapter<InfoRecyclerViewAdapter.ViewHolder> {
    private List<Attraction> attractionList;
    private Context context;
    private Coordinate coordinate;

    public InfoRecyclerViewAdapter(List<Attraction> attractionList, Context context, Coordinate coordinate) {
        this.attractionList = attractionList;
        this.context = context;
        this.coordinate = coordinate;
    }

    public void setAttractionList(List<Attraction> attractionList) {
        this.attractionList = attractionList;
    }

    @Override
    public int getItemViewType(int position) {
        Log.e("getItemViewType", position + " | ");
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_show_around, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Attraction thisAttraction = attractionList.get(position);
//            Log.e("InformMy like onBindViewHolder",position+" | "+findingResult.title);

        Glide.with(context).load(thisAttraction.getThumnailAddr()).into(holder.aroundThumnailImg);
        holder.aroundThumnailImg.setCornerRadius(20);
        holder.aroundName.setText(thisAttraction.getName());
        holder.aroundIntro.setText(thisAttraction.getSummary());
        holder.aroundDesc.setText(thisAttraction.getDetail());
        String distance = ((int) LocationDistance.distance(thisAttraction.getLat(), thisAttraction.getLon()
                , coordinate.getLat(), coordinate.getLon(), LocationDistance.DISTANCE_UNIT_METER)) + " m (" + context.getString(R.string.from_changdeokgung) + ")";
        holder.aroundDistance.setText(distance);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    int temp = thisAttraction.getIdx();
                    Intent intent = new Intent(context, AroundDetailActivity.class);
                    intent.putExtra("attraction", thisAttraction);
//                            intent.putExtra("attractionIdx", findingResult);
                    context.startActivity(intent);
                } catch (NumberFormatException e) {
                    Toast.makeText(context, context.getString(R.string.transportation_clicked), Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return attractionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
}


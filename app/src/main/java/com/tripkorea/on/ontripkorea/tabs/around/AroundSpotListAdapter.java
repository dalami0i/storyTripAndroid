package com.tripkorea.on.ontripkorea.tabs.around;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.util.LocationDistance;
import com.tripkorea.on.ontripkorea.vo.attraction.Attraction;

import java.util.List;

/**
 * Created by YangHC on 2017-07-03.
 */

public class AroundSpotListAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private
    List<Attraction> attractionList;

    public AroundSpotListAdapter(List<Attraction> attractionList, Context context) {
        this.context = context;
        this.attractionList = attractionList;
    }

    public void setAttractionList(List<Attraction> attractionList) {
        this.attractionList = attractionList;
    }

    @Override
    public int getCount() {
        return attractionList.size();
    }

    @Override
    public Attraction getItem(int position) {
        return attractionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AroundAttractionListViewHolder viewHolder;

        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.view_show_around, null);

            viewHolder = new AroundAttractionListViewHolder();
            viewHolder.titleTxt = (TextView) convertView.findViewById(R.id.txt_view_show_around_title);
            viewHolder.introTxt = (TextView) convertView.findViewById(R.id.txt_view_show_around_intro);
            viewHolder.detailTxt = (TextView) convertView.findViewById(R.id.txt_view_show_around_detail);
            viewHolder.distanceTxt = (TextView) convertView.findViewById(R.id.txt_view_show_around_distance);
        } else {
            viewHolder = (AroundAttractionListViewHolder) convertView.getTag();
        }
        Attraction attraction = attractionList.get(position);


        viewHolder.titleTxt.setText(attraction.getName());
//        viewHolder.introTxt.setText(attraction);
//        viewHolder.detailTxt.setText()

        double myLat=0, myLon=0;
        double thisLat = attraction.getCoord().getLat();
        double thisLon = attraction.getCoord().getLon();
        String distance = String.valueOf(LocationDistance.distance(myLat,myLon,thisLat,thisLon,"meter"));
        viewHolder.distanceTxt.setText(distance);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn......:
//
//                break;
        }
    }

    class AroundAttractionListViewHolder {
        TextView titleTxt;
        TextView introTxt;
        TextView detailTxt;
        TextView distanceTxt;
    }
}
package com.tripkorea.on.ontripkorea.tabs.around;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.tabs.around.detail.AroundDetailActivity;
import com.tripkorea.on.ontripkorea.util.Coordinate;
import com.tripkorea.on.ontripkorea.util.LocationDistance;
import com.tripkorea.on.ontripkorea.util.LogManager;
import com.tripkorea.on.ontripkorea.vo.attraction.AttractionSimple;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

///**
// * Created by YangHC on 2018-06-11.
// */

public class AroundRecyclerViewAdapter extends RecyclerView.Adapter<AroundRecyclerViewAdapter.ViewHolder> {
    private List<AttractionSimple> findingList = new ArrayList<>();
    private Context context;
    //2018 06 25 17:18 kiryun 수정
    private int tabPosition;
    private Coordinate coordinate;
    GoogleMap aroundMap;
    ArrayList<Integer> viewItemNum = new ArrayList<>();


    public AroundRecyclerViewAdapter(
            List<AttractionSimple> resources, Context context, int tabPosition, Coordinate coordinate, GoogleMap aroundMap) {
        this.findingList = resources;
        this.aroundMap = aroundMap;
        this.context = context;
        //2018 06 25 17:18 kiryun 수정
        this.tabPosition = tabPosition;
        this.coordinate = coordinate;


        /*switch (tabPosition) {
            case 0:
                for (int i = 0; i < resources.size(); i++) {
                    try {
//                        int tmpInt = Integer.parseInt(resources.get(i).categoryNum);
                    } catch (NumberFormatException e) {
                        findingList.add(resources.get(i));
                    }
                }
                break;
            case 1:
                for (int i = 0; i < resources.size(); i++) {
                    try {
//                        int tmpInt = Integer.parseInt(resources.get(i).categoryNum);
//                        if(tmpInt == 3) findingList.add(resources.get(i));
                    } catch (NumberFormatException e) {

                    }
                }
                break;
            default:
                for (int i = 0; i < resources.size(); i++) {
                    try {
//                        int tmpInt = Integer.parseInt(resources.get(i).categoryNum);
//                        if(tmpInt != 3) findingList.add(resources.get(i));
                    } catch (NumberFormatException e) {

                    }
                }
                break;

        }*/
//            findingList = resources;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final RoundedImageView thumnailImg;
        private final TextView titleTxt;
        private final TextView introTxt;
        private final TextView detailTxt;
        private final TextView distanceTxt;
        private final TextView menuTitleTxt;

        private ViewHolder(View view) {
            super(view);
            mView = view;
            thumnailImg = view.findViewById(R.id.around_item_thumbnail);
            titleTxt = view.findViewById(R.id.txt_view_show_around_title);
            introTxt = view.findViewById(R.id.txt_view_show_around_intro);
            menuTitleTxt = view.findViewById(R.id.around_menu_title);
            detailTxt = view.findViewById(R.id.txt_view_show_around_detail);
            distanceTxt = view.findViewById(R.id.txt_view_show_around_distance);

        }
    }

//        @Override
//        public int getItemViewType(int position) {
////            )Log.e("어라운드 getItemViewType",position+" | ");
//            int type = 0;
//            if(position == 0 || position == 9 || position == 58){
//                type=1;
//            }
//            return type;
//        }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
//            )Log.e("어라운드 onCreateViewHolder",viewType+" | ");
//            switch(viewType){
//                case 1:
//                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.showaroundtitleitem, parent, false);
//                    break;
//                default:
//                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.showarounditem, parent, false);
//                    break;
//            }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_show_around, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AttractionSimple thisAttraction = findingList.get(position);
        new LogManager().LogManager("aroundAdapter","title: "+thisAttraction.getName()+" | id:"+thisAttraction.getIdx());
//            )Log.e("어라운드 onBindViewHolder","firstShowPosition: "+firstShowPosition);

        //보여주기
//            viewItemNum.add(position);
//            int sum = 0;
//            for(int tmp:viewItemNum){
//                sum += tmp;
//            }
//            int viewNum = sum/viewItemNum.size();
//            AttrClient firstShowItem = findingList.get(viewNum);
//            if (aroundMap != null) {
//                aroundMap.moveCamera(
//                        CameraUpdateFactory.newLatLng(
//                                new LatLng(Double.parseDouble(firstShowItem.mapy), Double.parseDouble(firstShowItem.mapx))));
//            }

        Glide.with(context).load(thisAttraction.getThumnailAddr()).into(holder.thumnailImg);
        //Log.e("now thumnailaddr: ",thisAttraction.getThumnailAddr());
        holder.thumnailImg.setCornerRadius(20);
        holder.titleTxt.setText(thisAttraction.getName());
        holder.introTxt.setText(thisAttraction.getSummary());
        holder.detailTxt.setText(thisAttraction.getDetail());
        String distance = ((int)LocationDistance.distance(thisAttraction.getLat(), thisAttraction.getLon()
                , coordinate.getLat(), coordinate.getLon(), LocationDistance.DISTANCE_UNIT_METER))+ " m (" + context.getString(R.string.from_changdeokgung) + ")";
        holder.distanceTxt.setText(distance);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    int temp = thisAttraction.getIdx();
                    AroundDetailActivity.youtubeDetails.clear();
//                    Log.e("showaround", "show detail 직전");
//                    if(findingResult.youtubekey != null){
//                        String[] youtubeList = findingResult.youtubekey.split(",");
//                        Log.e("showaround","item youtube"+youtubeList[0]);
//                        new YoutubeAsyncTask(context, findingResult).execute(findingResult.youtubekey);
//                    }else {

                    //2018 06 25 16:59 kiryun 수정
                    //여기서 route와 나머지 분류 해서 intent를 띄울지 아니면 dialog를 띄울지 분류를 해야함.
                    switch(tabPosition)
                    {
                        case 0:
                            AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                            dialog.setTitle(holder.titleTxt.getText().toString())
                                    .setMessage(holder.detailTxt.getText().toString())
                                    .setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .create()
                                    .show();
                            //Alert.makeText("dialog");
                            break;
                        default:
                            Intent intent = new Intent(context, AroundDetailActivity.class);
                            intent.putExtra("attraction", thisAttraction);
                            intent.putExtra("attractionIdx", thisAttraction.getIdx());
                            new LogManager().LogManager("around to detail","thisAttraction.getIdx(); "+thisAttraction.getIdx());
                            context.startActivity(intent);
                            break;
                    }
//                    }
                } catch (NumberFormatException e) {
                    AlertDialog dialog = createDialogBoxHome(thisAttraction);
                    dialog.show();
//                                Toast.makeText(owner,owner.getString(R.string.transportation_clicked),Toast.LENGTH_LONG).show();
                }


            }
        });

        holder.mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (aroundMap != null) {
                    aroundMap.moveCamera(
                            CameraUpdateFactory.newLatLng(
                                    new LatLng(thisAttraction.getLat(), thisAttraction.getLon())));
                }

                return false;
            }
        });
//            switch(position){
//                case 0:
//                    holder.menuTitleTxt.setText(findingResult.title);
//                    break;
//                case 9:
//                    holder.menuTitleTxt.setText(findingResult.title);
//                    break;
//                case 58:
//                    holder.menuTitleTxt.setText(findingResult.title);
//                    break;
//                default:
//                    holder.thumnailImg.setCornerRadius((float) 20);
//                    Glide.with(owner).load(findingResult.firstimage).into( holder.thumnailImg);
//                    holder.titleTxt.setText(findingResult.title);
//                    holder.introTxt.setText(findingResult.summary);
//                    holder.detailTxt.setText(findingResult.description);
//                    String distance = (int)findingResult.distance + " m";
//                    holder.distanceTxt.setText(distance);
//                    holder.mView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            try{
//                                int temp = Integer.parseInt(findingResult.contentID);
//                                Intent intent = new Intent(owner, ShowDetailActivity.class);
//                                intent.putExtra("attraction", findingResult);
//                                owner.startActivity(intent);
//                            }catch (NumberFormatException e){
//                                AlertDialog dialog = createDialogBoxHome(findingResult);
//                                dialog.show();
////                                Toast.makeText(owner,owner.getString(R.string.transportation_clicked),Toast.LENGTH_LONG).show();
//                            }
//
//
//                        }
//                    });
//                    break;
//            }


    }

    private Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            System.out.println("dialog drawable Exc=" + e);
            return null;
        }
    }

    private AlertDialog createDialogBoxHome(AttractionSimple attrClient) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(attrClient.getName());
        builder.setMessage(attrClient.getDetail());
        builder.setIcon(LoadImageFromWebOperations(attrClient.getThumnailAddr()));
//            builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(R.string.dialogyestitle, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                context.mGoogleApiClient.disconnect();
            }

        });
        return builder.create();
    }


    @Override
    public int getItemCount() {
        return findingList.size();
    }
}

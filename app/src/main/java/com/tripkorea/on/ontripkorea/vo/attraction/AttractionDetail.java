package com.tripkorea.on.ontripkorea.vo.attraction;

import android.os.Parcel;

import java.io.Serializable;

///**
// * Created by YangHC on 2018-05-17.
// */
public class AttractionDetail extends Attraction implements Serializable{
    private boolean liked;
    private boolean visited;

    public AttractionDetail() {}

    protected AttractionDetail(Parcel in) {
        liked = in.readByte() != 0;
        visited = in.readByte() != 0;
    }



    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }


}

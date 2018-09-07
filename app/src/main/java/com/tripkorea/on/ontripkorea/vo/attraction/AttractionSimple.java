package com.tripkorea.on.ontripkorea.vo.attraction;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangHC on 2018-06-11.
 */

public class AttractionSimple implements Serializable, Parcelable{
    private int categoryIdx;
    private List<Integer> categoryList = new ArrayList<>();
    private int idx;
    private String name;
    private double lat;
    private double lon;
    private String route;
    private String summary;
    private String detail;
    private String thumnailAddr;
    private String thumnailAddr2;
    private int type;
    private int guideType;

    public  AttractionSimple(){}

    protected AttractionSimple(Parcel in) {
        categoryIdx = in.readInt();
        idx = in.readInt();
        name = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        route = in.readString();
        summary = in.readString();
        detail = in.readString();
        thumnailAddr = in.readString();
        thumnailAddr2 = in.readString();
        type = in.readInt();
        guideType = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(categoryIdx);
        dest.writeInt(idx);
        dest.writeString(name);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeString(route);
        dest.writeString(summary);
        dest.writeString(detail);
        dest.writeString(thumnailAddr);
        dest.writeString(thumnailAddr2);
        dest.writeInt(type);
        dest.writeInt(guideType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AttractionSimple> CREATOR = new Creator<AttractionSimple>() {
        @Override
        public AttractionSimple createFromParcel(Parcel in) {
            return new AttractionSimple(in);
        }

        @Override
        public AttractionSimple[] newArray(int size) {
            return new AttractionSimple[size];
        }
    };

    public List<Integer> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Integer> categoryList) {
        this.categoryList = categoryList;
    }

    public int getCategoryIdx() {
        return categoryIdx;
    }

    public void setCategoryIdx(int categoryIdx) {
        this.categoryIdx = categoryIdx;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getThumnailAddr() {
        return thumnailAddr;
    }

    public void setThumnailAddr(String thumnailAddr) {
        this.thumnailAddr = thumnailAddr;
    }

    public String getThumnailAddr2() {
        return thumnailAddr2;
    }

    public void setThumnailAddr2(String thumnailAddr2) {
        this.thumnailAddr2 = thumnailAddr2;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGuideType() {
        return guideType;
    }

    public void setGuideType(int guideType) {
        this.guideType = guideType;
    }
}

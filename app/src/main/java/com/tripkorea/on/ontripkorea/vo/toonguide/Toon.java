package com.tripkorea.on.ontripkorea.vo.toonguide;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

///**
// * Created by Edward Won on 2018-07-03.
// */

public class Toon implements Parcelable{
    private int idx;
    private int guideIdx;
    private int attractionIdx;
    private String title;
    private String description;
    private double lat;
    private double lon;
    private List<String> cartoonImageAddressList;
    private double east;
    private double west;
    private double north;
    private double south;
    private int checked;
    private String mapImgURL;

    public Toon(){}

    protected Toon(Parcel in) {
        idx = in.readInt();
        guideIdx = in.readInt();
        attractionIdx = in.readInt();
        title = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        east = in.readDouble();
        west = in.readDouble();
        north = in.readDouble();
        south = in.readDouble();
        checked = in.readInt();
        mapImgURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idx);
        dest.writeInt(guideIdx);
        dest.writeInt(attractionIdx);
        dest.writeString(title);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeDouble(east);
        dest.writeDouble(west);
        dest.writeDouble(north);
        dest.writeDouble(south);
        dest.writeInt(checked);
        dest.writeString(mapImgURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Toon> CREATOR = new Creator<Toon>() {
        @Override
        public Toon createFromParcel(Parcel in) {
            return new Toon(in);
        }

        @Override
        public Toon[] newArray(int size) {
            return new Toon[size];
        }
    };

    public int getGuideIdx() {
        return guideIdx;
    }

    public void setGuideIdx(int guideIdx) {
        this.guideIdx = guideIdx;
    }

    public String getMapImgURL() {
        return mapImgURL;
    }

    public void setMapImgURL(String mapImgURL) {
        this.mapImgURL = mapImgURL;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public double getEast() {
        return east;
    }

    public void setEast(double east) {
        this.east = east;
    }

    public double getWest() {
        return west;
    }

    public void setWest(double west) {
        this.west = west;
    }

    public double getNorth() {
        return north;
    }

    public void setNorth(double north) {
        this.north = north;
    }

    public double getSouth() {
        return south;
    }

    public void setSouth(double south) {
        this.south = south;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getAttractionIdx() {
        return attractionIdx;
    }

    public void setAttractionIdx(int attractionIdx) {
        this.attractionIdx = attractionIdx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<String> getCartoonImageAddressList() {
        return cartoonImageAddressList;
    }

    public void setCartoonImageAddressList(List<String> cartoonImageAddressList) {
        this.cartoonImageAddressList = cartoonImageAddressList;
    }
}

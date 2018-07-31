package com.tripkorea.on.ontripkorea.vo.toonguide;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

///**
// * Created by Edward Won on 2018-07-03.
// */

public class Toon implements Parcelable{
    private int idx;
    private int attractionIdx;
    private String title;
    private String description;
    private double lat;
    private double lon;
    private List<String> cartoonImageAddressList;
    private String toonSouth;
    private String toonNorth;
    private String toonEast;
    private String toonWest;
    private int checked;
    private String mapImgURL;

    public Toon(){}

    protected Toon(Parcel in) {
        idx = in.readInt();
        attractionIdx = in.readInt();
        title = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        toonSouth = in.readString();
        toonNorth = in.readString();
        toonEast = in.readString();
        toonWest = in.readString();
        checked = in.readInt();
        mapImgURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idx);
        dest.writeInt(attractionIdx);
        dest.writeString(title);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeString(toonSouth);
        dest.writeString(toonNorth);
        dest.writeString(toonEast);
        dest.writeString(toonWest);
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

    public String getToonSouth() {
        return toonSouth;
    }

    public void setToonSouth(String toonSouth) {
        this.toonSouth = toonSouth;
    }

    public String getToonNorth() {
        return toonNorth;
    }

    public void setToonNorth(String toonNorth) {
        this.toonNorth = toonNorth;
    }

    public String getToonEast() {
        return toonEast;
    }

    public void setToonEast(String toonEast) {
        this.toonEast = toonEast;
    }

    public String getToonWest() {
        return toonWest;
    }

    public void setToonWest(String toonWest) {
        this.toonWest = toonWest;
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

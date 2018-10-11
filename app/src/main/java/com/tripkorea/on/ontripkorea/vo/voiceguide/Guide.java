package com.tripkorea.on.ontripkorea.vo.voiceguide;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

///**
// * Created by Edward Won on 2018-07-03.
// */

public class Guide implements Parcelable{
    private int idx;
    private int guideIdx;
    private int attractionIdx;
    private String title;
    private String description;
    private double length;
    private double lat;
    private double lon;
    private String voiceAddress;
    private List<GuideImage> guideImageList;
    private double east;
    private double west;
    private double north;
    private double south;
    private int checked;

    public Guide(){}

    protected Guide(Parcel in) {
        idx = in.readInt();
        guideIdx = in.readInt();
        attractionIdx = in.readInt();
        title = in.readString();
        description = in.readString();
        length = in.readDouble();
        lat = in.readDouble();
        lon = in.readDouble();
        voiceAddress = in.readString();
        east = in.readDouble();
        west = in.readDouble();
        north = in.readDouble();
        south = in.readDouble();
        checked = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idx);
        dest.writeInt(guideIdx);
        dest.writeInt(attractionIdx);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeDouble(length);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeString(voiceAddress);
        dest.writeDouble(east);
        dest.writeDouble(west);
        dest.writeDouble(north);
        dest.writeDouble(south);
        dest.writeInt(checked);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Guide> CREATOR = new Creator<Guide>() {
        @Override
        public Guide createFromParcel(Parcel in) {
            return new Guide(in);
        }

        @Override
        public Guide[] newArray(int size) {
            return new Guide[size];
        }
    };

    public int getGuideIdx() {
        return guideIdx;
    }

    public void setGuideIdx(int guideIdx) {
        this.guideIdx = guideIdx;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
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

    public String getVoiceAddress() {
        return voiceAddress;
    }

    public void setVoiceAddress(String voiceAddress) {
        this.voiceAddress = voiceAddress;
    }

    public List<GuideImage> getGuideImageList() {
        return guideImageList;
    }

    public void setGuideImageList(List<GuideImage> guideImageList) {
        this.guideImageList = guideImageList;
    }
}

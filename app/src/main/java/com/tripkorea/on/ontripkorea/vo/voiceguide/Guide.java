package com.tripkorea.on.ontripkorea.vo.voiceguide;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

///**
// * Created by Edward Won on 2018-07-03.
// */

public class Guide implements Parcelable{
    private int idx;
    private int attractionIdx;
    private String title;
    private String description;
    private double length;
    private double lat;
    private double lon;
    private String voiceAddress;
    private List<GuideImage> guideImageList;
    private String guideSouth;
    private String guideNorth;
    private String guideEast;
    private String guideWest;
    private int checked;

    public Guide(){}

    protected Guide(Parcel in) {
        idx = in.readInt();
        attractionIdx = in.readInt();
        title = in.readString();
        description = in.readString();
        length = in.readDouble();
        lat = in.readDouble();
        lon = in.readDouble();
        voiceAddress = in.readString();
        guideSouth = in.readString();
        guideNorth = in.readString();
        guideEast = in.readString();
        guideWest = in.readString();
        checked = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idx);
        dest.writeInt(attractionIdx);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeDouble(length);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeString(voiceAddress);
        dest.writeString(guideSouth);
        dest.writeString(guideNorth);
        dest.writeString(guideEast);
        dest.writeString(guideWest);
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

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public String getGuideSouth() {
        return guideSouth;
    }

    public void setGuideSouth(String guideSouth) {
        this.guideSouth = guideSouth;
    }

    public String getGuideNorth() {
        return guideNorth;
    }

    public void setGuideNorth(String guideNorth) {
        this.guideNorth = guideNorth;
    }

    public String getGuideEast() {
        return guideEast;
    }

    public void setGuideEast(String guideEast) {
        this.guideEast = guideEast;
    }

    public String getGuideWest() {
        return guideWest;
    }

    public void setGuideWest(String guideWest) {
        this.guideWest = guideWest;
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

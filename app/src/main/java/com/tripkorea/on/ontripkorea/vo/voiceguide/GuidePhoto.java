package com.tripkorea.on.ontripkorea.vo.voiceguide;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Edward Won on 2018-08-09.
 */

public class GuidePhoto implements Serializable, Parcelable {

    private int idx;
    private String thumnailAddr;
    private ArrayList<String> guideImageAddrs = new ArrayList<>();

    protected GuidePhoto(Parcel in) {
        idx = in.readInt();
        thumnailAddr = in.readString();
        guideImageAddrs = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idx);
        dest.writeString(thumnailAddr);
        dest.writeStringList(guideImageAddrs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GuidePhoto> CREATOR = new Creator<GuidePhoto>() {
        @Override
        public GuidePhoto createFromParcel(Parcel in) {
            return new GuidePhoto(in);
        }

        @Override
        public GuidePhoto[] newArray(int size) {
            return new GuidePhoto[size];
        }
    };

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getThumnailAddr() {
        return thumnailAddr;
    }

    public void setThumnailAddr(String thumnailAddr) {
        this.thumnailAddr = thumnailAddr;
    }

    public ArrayList<String> getGuideImageAddrs() {
        return guideImageAddrs;
    }

    public void setGuideImageAddrs(ArrayList<String> guideImageAddrs) {
        this.guideImageAddrs = guideImageAddrs;
    }
}

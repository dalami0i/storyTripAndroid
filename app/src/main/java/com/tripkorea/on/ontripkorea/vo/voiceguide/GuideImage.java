package com.tripkorea.on.ontripkorea.vo.voiceguide;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Edward Won on 2018-07-03.
 */

public class GuideImage implements Parcelable{
    private int idx;
    private int guideIdx;
    private String description;
    private String imgAddress;

    public GuideImage(){}

    protected GuideImage(Parcel in) {
        idx = in.readInt();
        guideIdx = in.readInt();
        description = in.readString();
        imgAddress = in.readString();
    }

    public static final Creator<GuideImage> CREATOR = new Creator<GuideImage>() {
        @Override
        public GuideImage createFromParcel(Parcel in) {
            return new GuideImage(in);
        }

        @Override
        public GuideImage[] newArray(int size) {
            return new GuideImage[size];
        }
    };

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getGuideIdx() {
        return guideIdx;
    }

    public void setGuideIdx(int guideIdx) {
        this.guideIdx = guideIdx;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgAddress() {
        return imgAddress;
    }

    public void setImgAddress(String imgAddress) {
        this.imgAddress = imgAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idx);
        dest.writeInt(guideIdx);
        dest.writeString(description);
        dest.writeString(imgAddress);
    }
}

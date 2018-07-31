package com.tripkorea.on.ontripkorea.vo.toonguide;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Edward Won on 2018-07-03.
 */

public class ToonImage implements Parcelable{
    private int idx;
    private int toonIdx;
    private String description;
    private String imgUrl;

    public ToonImage(){}

    protected ToonImage(Parcel in) {
        idx = in.readInt();
        toonIdx = in.readInt();
        description = in.readString();
        imgUrl = in.readString();
    }

    public static final Creator<ToonImage> CREATOR = new Creator<ToonImage>() {
        @Override
        public ToonImage createFromParcel(Parcel in) {
            return new ToonImage(in);
        }

        @Override
        public ToonImage[] newArray(int size) {
            return new ToonImage[size];
        }
    };

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getToonIdx() {
        return toonIdx;
    }

    public void setToonIdx(int toonIdx) {
        this.toonIdx = toonIdx;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idx);
        dest.writeInt(toonIdx);
        dest.writeString(description);
        dest.writeString(imgUrl);
    }
}

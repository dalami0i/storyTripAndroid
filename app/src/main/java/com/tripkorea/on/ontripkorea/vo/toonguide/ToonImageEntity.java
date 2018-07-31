package com.tripkorea.on.ontripkorea.vo.toonguide;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

///**
// * Created by Edward Won on 2018-07-17.
// */

public class ToonImageEntity implements Parcelable {
    private List<String> toonList = new ArrayList<>();

    public ToonImageEntity(){}

    public List<String> getToonList() {
        return toonList;
    }

    public void setToonList(List<String> toonList) {
        this.toonList = toonList;
    }

    protected ToonImageEntity(Parcel in) {
        toonList = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(toonList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ToonImageEntity> CREATOR = new Creator<ToonImageEntity>() {
        @Override
        public ToonImageEntity createFromParcel(Parcel in) {
            return new ToonImageEntity(in);
        }

        @Override
        public ToonImageEntity[] newArray(int size) {
            return new ToonImageEntity[size];
        }
    };
}

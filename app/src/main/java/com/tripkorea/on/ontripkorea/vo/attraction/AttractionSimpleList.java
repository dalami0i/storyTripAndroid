package com.tripkorea.on.ontripkorea.vo.attraction;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward Won on 2018-07-24.
 */

public class AttractionSimpleList implements Parcelable{

    private List<AttractionSimple> items = new ArrayList<>();

    public AttractionSimpleList(){}

    protected AttractionSimpleList(Parcel in) {
        items = in.createTypedArrayList(AttractionSimple.CREATOR);
    }

    public static final Creator<AttractionSimpleList> CREATOR = new Creator<AttractionSimpleList>() {
        @Override
        public AttractionSimpleList createFromParcel(Parcel in) {
            return new AttractionSimpleList(in);
        }

        @Override
        public AttractionSimpleList[] newArray(int size) {
            return new AttractionSimpleList[size];
        }
    };

    public List<AttractionSimple> getItems() {
        return items;
    }

    public void setItems(List<AttractionSimple> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
    }
}

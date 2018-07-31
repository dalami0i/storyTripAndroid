package com.tripkorea.on.ontripkorea.vo.attraction;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward Won on 2018-07-24.
 */

public class AttractionSimpleListSet implements Parcelable{

    private List<AttractionSimpleList> items = new ArrayList<>();

    public AttractionSimpleListSet(){}

    public List<AttractionSimpleList> getItems() {
        return items;
    }

    public void setItems(List<AttractionSimpleList> items) {
        this.items = items;
    }

    protected AttractionSimpleListSet(Parcel in) {
        items = in.createTypedArrayList(AttractionSimpleList.CREATOR);
    }

    public static final Creator<AttractionSimpleListSet> CREATOR = new Creator<AttractionSimpleListSet>() {
        @Override
        public AttractionSimpleListSet createFromParcel(Parcel in) {
            return new AttractionSimpleListSet(in);
        }

        @Override
        public AttractionSimpleListSet[] newArray(int size) {
            return new AttractionSimpleListSet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
    }
}

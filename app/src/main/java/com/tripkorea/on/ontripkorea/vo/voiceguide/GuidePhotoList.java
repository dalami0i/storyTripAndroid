package com.tripkorea.on.ontripkorea.vo.voiceguide;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Edward Won on 2018-08-09.
 */

public class GuidePhotoList implements Parcelable {

    private ArrayList<GuidePhoto> guidePhotos = new ArrayList<>();

    protected GuidePhotoList(Parcel in) {
        guidePhotos = in.createTypedArrayList(GuidePhoto.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(guidePhotos);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GuidePhotoList> CREATOR = new Creator<GuidePhotoList>() {
        @Override
        public GuidePhotoList createFromParcel(Parcel in) {
            return new GuidePhotoList(in);
        }

        @Override
        public GuidePhotoList[] newArray(int size) {
            return new GuidePhotoList[size];
        }
    };

    public ArrayList<GuidePhoto> getGuidePhotos() {
        return guidePhotos;
    }

    public void setGuidePhotos(ArrayList<GuidePhoto> guidePhotos) {
        this.guidePhotos = guidePhotos;
    }
}

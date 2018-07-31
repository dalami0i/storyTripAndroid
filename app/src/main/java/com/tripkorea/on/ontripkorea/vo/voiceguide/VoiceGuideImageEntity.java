package com.tripkorea.on.ontripkorea.vo.voiceguide;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward Won on 2018-07-16.
 */

public class VoiceGuideImageEntity implements Parcelable{
    private List<GuideImage> voiceGuideList = new ArrayList<>();

    public List<GuideImage> getVoiceGuideList() {
        return voiceGuideList;
    }

    public void setVoiceGuideList(List<GuideImage> voiceGuideList) {
        this.voiceGuideList = voiceGuideList;
    }

    public VoiceGuideImageEntity(){}

    protected VoiceGuideImageEntity(Parcel in) {
        voiceGuideList = in.createTypedArrayList(GuideImage.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(voiceGuideList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VoiceGuideImageEntity> CREATOR = new Creator<VoiceGuideImageEntity>() {
        @Override
        public VoiceGuideImageEntity createFromParcel(Parcel in) {
            return new VoiceGuideImageEntity(in);
        }

        @Override
        public VoiceGuideImageEntity[] newArray(int size) {
            return new VoiceGuideImageEntity[size];
        }
    };
}

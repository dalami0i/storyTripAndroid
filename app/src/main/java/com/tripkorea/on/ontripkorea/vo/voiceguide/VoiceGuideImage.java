package com.tripkorea.on.ontripkorea.vo.voiceguide;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

///**
// * Created by Edward Won on 2018-05-03.
// */

public class VoiceGuideImage implements Parcelable {
    @SerializedName("contentid")
    public String contentid;
    @SerializedName("voiceguideid")
    public String voiceguideid;
    @SerializedName("voiceimgurl")
    public String imgAddr;
    @SerializedName("voiceimgtext")
    public String voiceimgtext;

    public VoiceGuideImage(){}


    protected VoiceGuideImage(Parcel in) {
        contentid = in.readString();
        voiceguideid = in.readString();
        imgAddr = in.readString();
        voiceimgtext = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contentid);
        dest.writeString(voiceguideid);
        dest.writeString(imgAddr);
        dest.writeString(voiceimgtext);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VoiceGuideImage> CREATOR = new Creator<VoiceGuideImage>() {
        @Override
        public VoiceGuideImage createFromParcel(Parcel in) {
            return new VoiceGuideImage(in);
        }

        @Override
        public VoiceGuideImage[] newArray(int size) {
            return new VoiceGuideImage[size];
        }
    };
}


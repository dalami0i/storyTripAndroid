package com.tripkorea.on.ontripkorea.vo.voiceguide;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Edward Won on 2018-06-12.
 */

public class VoiceGuide implements Parcelable {

    @SerializedName("contentid")
    public String contentID;
    @SerializedName("map_east")
    public double map_east;
    @SerializedName("map_west")
    public double map_west;
    @SerializedName("map_south")
    public double map_south;
    @SerializedName("map_north")
    public double map_north;
    @SerializedName("map_img_url")
    public String map_img_url;
    @SerializedName("user_language")
    public String user_language;
    @SerializedName("voiceGuideLoc")
    public ArrayList<VoiceGuideLocation> locList = new ArrayList<>();

    public VoiceGuide(){}

    protected VoiceGuide(Parcel in) {
        contentID = in.readString();
        map_img_url = in.readString();
        map_south = in.readDouble();
        map_north = in.readDouble();
        map_east = in.readDouble();
        map_west = in.readDouble();
        user_language = in.readString();
        locList = in.createTypedArrayList(VoiceGuideLocation.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contentID);
        dest.writeString(map_img_url);
        dest.writeDouble(map_south);
        dest.writeDouble(map_north);
        dest.writeDouble(map_east);
        dest.writeDouble(map_west);
        dest.writeString(user_language);
        dest.writeTypedList(locList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VoiceGuide> CREATOR = new Creator<VoiceGuide>() {
        @Override
        public VoiceGuide createFromParcel(Parcel in) {
            return new VoiceGuide(in);
        }

        @Override
        public VoiceGuide[] newArray(int size) {
            return new VoiceGuide[size];
        }
    };
}
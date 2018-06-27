package com.tripkorea.on.ontripkorea.vo.voiceguide;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

///**
// * Created by Edward Won on 2018-05-03.
// */

public class VoiceGuideLocation implements Parcelable {

    @SerializedName("contentid")
    public String contentid;
    @SerializedName("voiceguideid")
    public String voiceguideid;
    @SerializedName("checked")
    public int checked;
    @SerializedName("voice_loc_east")
    public String voice_loc_east;
    @SerializedName("voice_loc_west")
    public String voice_loc_west;
    @SerializedName("voice_loc_south")
    public String voice_loc_south;
    @SerializedName("voice_loc_north")
    public String voice_loc_north;
    @SerializedName("icon_addr")
    public String icon_addr;
    @SerializedName("location_img_size")
    public int location_img_size;
    @SerializedName("location_title")
    public String location_title;
    @SerializedName("voice_addr")
    public String voice_addr;
    @SerializedName("voice_title")
    public String voice_title;
    @SerializedName("user_language")
    public int user_language;
    @SerializedName("voiceGuideImg")
    public ArrayList<VoiceGuideImage> voiceGuideImg = new ArrayList<>();

    public VoiceGuideLocation(){}


    protected VoiceGuideLocation(Parcel in) {
        contentid = in.readString();
        voiceguideid = in.readString();
        checked = in.readInt();
        voice_loc_east = in.readString();
        voice_loc_west = in.readString();
        voice_loc_south = in.readString();
        voice_loc_north = in.readString();
        icon_addr = in.readString();
        location_img_size = in.readInt();
        location_title = in.readString();
        voice_addr = in.readString();
        voice_title = in.readString();
        user_language = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contentid);
        dest.writeString(voiceguideid);
        dest.writeInt(checked);
        dest.writeString(voice_loc_east);
        dest.writeString(voice_loc_west);
        dest.writeString(voice_loc_south);
        dest.writeString(voice_loc_north);
        dest.writeString(icon_addr);
        dest.writeInt(location_img_size);
        dest.writeString(location_title);
        dest.writeString(voice_addr);
        dest.writeString(voice_title);
        dest.writeInt(user_language);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VoiceGuideLocation> CREATOR = new Creator<VoiceGuideLocation>() {
        @Override
        public VoiceGuideLocation createFromParcel(Parcel in) {
            return new VoiceGuideLocation(in);
        }

        @Override
        public VoiceGuideLocation[] newArray(int size) {
            return new VoiceGuideLocation[size];
        }
    };
}

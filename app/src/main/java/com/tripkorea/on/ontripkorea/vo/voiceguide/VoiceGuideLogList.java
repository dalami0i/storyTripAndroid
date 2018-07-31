package com.tripkorea.on.ontripkorea.vo.voiceguide;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Edward Won on 2018-07-31.
 */

public class VoiceGuideLogList implements Parcelable{

    private ArrayList<VoiceGuideLog> logArrayList = new ArrayList<>();

    public VoiceGuideLogList(){}

    public VoiceGuideLogList(ArrayList<VoiceGuideLog> logArrayList){
        setLogArrayList(logArrayList);
    }

    protected VoiceGuideLogList(Parcel in) {
        logArrayList = in.createTypedArrayList(VoiceGuideLog.CREATOR);
    }

    public static final Creator<VoiceGuideLogList> CREATOR = new Creator<VoiceGuideLogList>() {
        @Override
        public VoiceGuideLogList createFromParcel(Parcel in) {
            return new VoiceGuideLogList(in);
        }

        @Override
        public VoiceGuideLogList[] newArray(int size) {
            return new VoiceGuideLogList[size];
        }
    };

    public ArrayList<VoiceGuideLog> getLogArrayList() {
        return logArrayList;
    }

    public void setLogArrayList(ArrayList<VoiceGuideLog> logArrayList) {
        this.logArrayList = logArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(logArrayList);
    }
}

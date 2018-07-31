package com.tripkorea.on.ontripkorea.vo.voiceguide;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Edward Won on 2018-07-31.
 */

public class VoiceGuideLog implements Parcelable{

    private int userIdx;
    private int guideIdx;
    private double totalLength;
    private int listenTimeLength;

    public VoiceGuideLog(){}

    protected VoiceGuideLog(Parcel in) {
        userIdx = in.readInt();
        guideIdx = in.readInt();
        totalLength = in.readInt();
        listenTimeLength = in.readInt();
    }

    public static final Creator<VoiceGuideLog> CREATOR = new Creator<VoiceGuideLog>() {
        @Override
        public VoiceGuideLog createFromParcel(Parcel in) {
            return new VoiceGuideLog(in);
        }

        @Override
        public VoiceGuideLog[] newArray(int size) {
            return new VoiceGuideLog[size];
        }
    };

    public int getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(int userIdx) {
        this.userIdx = userIdx;
    }

    public int getGuideIdx() {
        return guideIdx;
    }

    public void setGuideIdx(int guideIdx) {
        this.guideIdx = guideIdx;
    }

    public double getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(double totalLength) {
        this.totalLength = totalLength;
    }

    public int getListenTimeLength() {
        return listenTimeLength;
    }

    public void setListenTimeLength(int listenTimeLength) {
        this.listenTimeLength = listenTimeLength;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userIdx);
        dest.writeInt(guideIdx);
        dest.writeDouble(totalLength);
        dest.writeInt(listenTimeLength);
    }
}

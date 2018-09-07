package com.tripkorea.on.ontripkorea.vo.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Edward Won on 2018-08-02.
 */

public class NaverResponse implements Parcelable{
    private String resultcode;
    private String message;
    private NaverUser response;
    private String id;

    public NaverResponse(){}

    protected NaverResponse(Parcel in) {
        resultcode = in.readString();
        message = in.readString();
        id = in.readString();
        response = in.readParcelable(NaverUser.class.getClassLoader());
    }

    public static final Creator<NaverResponse> CREATOR = new Creator<NaverResponse>() {
        @Override
        public NaverResponse createFromParcel(Parcel in) {
            return new NaverResponse(in);
        }

        @Override
        public NaverResponse[] newArray(int size) {
            return new NaverResponse[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NaverUser getResponse() {
        return response;
    }

    public void setResponse(NaverUser response) {
        this.response = response;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(resultcode);
        dest.writeString(message);
        dest.writeParcelable(response, flags);
    }
}

package com.tripkorea.on.ontripkorea.vo.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Edward Won on 2018-08-02.
 */

public class NaverUser implements Parcelable{

    private int idx;            // DB에서의 index값
    private String id;
    private String nickname;
    private String profile_image;
    private String email;

    public NaverUser(){}

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    protected NaverUser(Parcel in) {
        idx = in.readInt();
        id = in.readString();
        nickname = in.readString();
        profile_image = in.readString();
        email = in.readString();
    }

    public static final Creator<NaverUser> CREATOR = new Creator<NaverUser>() {
        @Override
        public NaverUser createFromParcel(Parcel in) {
            return new NaverUser(in);
        }

        @Override
        public NaverUser[] newArray(int size) {
            return new NaverUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idx);
        dest.writeString(id);
        dest.writeString(nickname);
        dest.writeString(profile_image);
        dest.writeString(email);
    }
}

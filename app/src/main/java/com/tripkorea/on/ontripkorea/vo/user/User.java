package com.tripkorea.on.ontripkorea.vo.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by YangHC on 2018-06-11.
 */

public class User implements Parcelable {


    private int idx;
    private String id;
    private String name;
    private String profileImgAddr;
    private int gender;     // ex) 0(남성), 1(여성)
    private int age;
    private int mindAge;
    private int snsIdx;     // 2 : Facebook 3 : Naver 4 : Google 5 :
    private int nationIdx;  // ex) 82(대한민국)  ref) https://ko.wikipedia.org/wiki/%EA%B5%AD%EC%A0%9C%EC%A0%84%ED%99%94_%EB%82%98%EB%9D%BC_%EB%B2%88%ED%98%B8
    private String nationCode;
    private int countryISO;

//    private int religionCode;   // ex) 0(기독교), 1(천주교), 2(불교), 3(힌두교) ....
//    private String service;
//    private String profileImgAddr;

    public User(){}

    protected User(Parcel in) {
        idx = in.readInt();
        id = in.readString();
        name = in.readString();
        profileImgAddr = in.readString();
        gender = in.readInt();
        age = in.readInt();
        mindAge = in.readInt();
        snsIdx = in.readInt();
        nationIdx = in.readInt();
        nationCode = in.readString();
        countryISO = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getProfileImgAddr() {
        return profileImgAddr;
    }

    public void setProfileImgAddr(String profileImgAddr) {
        this.profileImgAddr = profileImgAddr;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNationCode() {
        return nationCode;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }

    public int getCountryISO() {
        return countryISO;
    }

    public void setCountryISO(int countryISO) {
        this.countryISO = countryISO;
    }

    public int getSnsIdx() {
        return snsIdx;
    }

    public void setSnsIdx(int snsIdx) {
        this.snsIdx = snsIdx;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMindAge() {
        return mindAge;
    }

    public void setMindAge(int mindAge) {
        this.mindAge = mindAge;
    }

    public int getNationIdx() {
        return nationIdx;
    }

    public void setNationIdx(int nationCode) {
        this.nationIdx = nationIdx;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idx);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(profileImgAddr);
        dest.writeInt(gender);
        dest.writeInt(age);
        dest.writeInt(mindAge);
        dest.writeInt(snsIdx);
        dest.writeInt(nationIdx);
        dest.writeString(nationCode);
        dest.writeInt(countryISO);
    }
}

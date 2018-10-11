package com.tripkorea.on.ontripkorea.vo.user;

/**
 * Created by Edward Won on 2018-09-25.
 */

public class LoginResponse extends User {
    int code;
    String message;
    int userIdx;
    int religionIdx;
    String hobyList;
    String interestList;
    boolean vegeterian;



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(int userIdx) {
        this.userIdx = userIdx;
    }

    public int getReligionIdx() {
        return religionIdx;
    }

    public void setReligionIdx(int religionIdx) {
        this.religionIdx = religionIdx;
    }

    public String getHobyList() {
        return hobyList;
    }

    public void setHobyList(String hobyList) {
        this.hobyList = hobyList;
    }

    public String getInterestList() {
        return interestList;
    }

    public void setInterestList(String interestList) {
        this.interestList = interestList;
    }

    public boolean isVegeterian() {
        return vegeterian;
    }

    public void setVegeterian(boolean vegeterian) {
        this.vegeterian = vegeterian;
    }
}

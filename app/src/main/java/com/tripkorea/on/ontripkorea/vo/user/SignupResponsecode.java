package com.tripkorea.on.ontripkorea.vo.user;

///**
// * Created by Edward Won on 2018-09-25.
// */

public class SignupResponsecode extends User{
    int code;
    String message;

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
}

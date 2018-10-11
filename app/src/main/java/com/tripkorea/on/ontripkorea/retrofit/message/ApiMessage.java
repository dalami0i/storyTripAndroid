package com.tripkorea.on.ontripkorea.retrofit.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by YangHC on 2017-09-06.
 */

public class ApiMessage {
    public static final int SUCCESS = 1;
    public static final int ERROR = -1;
    public static final int CANNOT_FIND_USER = -1512;

    @JsonProperty("code")
    private int code;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

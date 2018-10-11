package com.tripkorea.on.ontripkorea.vo.user;

/**
 * Created by Edward Won on 2018-09-25.
 */

public class LoginUser {
    private String id;
    private int snsIdx;
    private String passwd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSnsIdx() {
        return snsIdx;
    }

    public void setSnsIdx(int snsIdx) {
        this.snsIdx = snsIdx;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}

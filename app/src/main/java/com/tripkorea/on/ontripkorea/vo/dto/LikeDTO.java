package com.tripkorea.on.ontripkorea.vo.dto;

/**
 * Created by YangHC on 2018-06-16.
 */

public class LikeDTO {
    private int userIdx;
    private int attrIdx;

    public LikeDTO(int userIdx, int attrIdx) {
        this.userIdx = userIdx;
        this.attrIdx = attrIdx;
    }

    public int getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(int userIdx) {
        this.userIdx = userIdx;
    }

    public int getAttrIdx() {
        return attrIdx;
    }

    public void setAttrIdx(int attrIdx) {
        this.attrIdx = attrIdx;
    }
}

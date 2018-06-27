package com.tripkorea.on.ontripkorea.vo.dto;

/**
 * Created by YangHC on 2018-06-16.
 */

public class LikeDTO {
    private int userIdx;
    private int attractionIdx;

    public LikeDTO(int userIdx, int attractionIdx) {
        this.userIdx = userIdx;
        this.attractionIdx = attractionIdx;
    }

    public int getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(int userIdx) {
        this.userIdx = userIdx;
    }

    public int getAttrIdx() {
        return attractionIdx;
    }

    public void setAttrIdx(int attrIdx) {
        this.attractionIdx = attrIdx;
    }
}

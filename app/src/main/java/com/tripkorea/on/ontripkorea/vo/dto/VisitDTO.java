package com.tripkorea.on.ontripkorea.vo.dto;

/**
 * Created by YangHC on 2018-06-16.
 */

public class VisitDTO {
    private int userIdx;
    private int attractionIdx;

    public VisitDTO(int userIdx, int attractionIdx) {
        this.userIdx = userIdx;
        this.attractionIdx = attractionIdx;
    }

    public int getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(int userIdx) {
        this.userIdx = userIdx;
    }

    public int getAttractionIdx() {
        return attractionIdx;
    }

    public void setAttractionIdx(int attractionIdx) {
        this.attractionIdx = attractionIdx;
    }
}

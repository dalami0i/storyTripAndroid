package com.tripkorea.on.ontripkorea.vo.dto;

/**
 * Created by YangHC on 2018-06-16.
 */

public class LikeDTO {
    private int attractionIdx;

    public LikeDTO(int attractionIdx) {
        this.attractionIdx = attractionIdx;
    }


    public int getAttrIdx() {
        return attractionIdx;
    }

    public void setAttrIdx(int attrIdx) {
        this.attractionIdx = attrIdx;
    }
}

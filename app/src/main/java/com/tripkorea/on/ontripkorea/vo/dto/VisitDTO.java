package com.tripkorea.on.ontripkorea.vo.dto;

/**
 * Created by YangHC on 2018-06-16.
 */

public class VisitDTO {
    private int attractionIdx;

    public VisitDTO( int attractionIdx) {
        this.attractionIdx = attractionIdx;
    }


    public int getAttractionIdx() {
        return attractionIdx;
    }

    public void setAttractionIdx(int attractionIdx) {
        this.attractionIdx = attractionIdx;
    }
}

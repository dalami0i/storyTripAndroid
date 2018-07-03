package com.tripkorea.on.ontripkorea.vo.attraction;

/**
 * Created by YangHC on 2018-05-17.
 */
public class AttractionDetail extends Attraction{
    private boolean isLiked;
    private boolean isVisited;

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}

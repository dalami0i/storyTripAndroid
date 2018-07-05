package com.tripkorea.on.ontripkorea.vo.attraction;

/**
 * Created by YangHC on 2018-05-17.
 */
public class AttractionDetail extends Attraction{
    private boolean liked;
    private boolean visited;

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}

package com.tripkorea.on.ontripkorea.vo.attraction;

import java.util.Date;
import java.util.List;

/**
 * Created by YangHC on 2018-06-11.
 */

public class Festival extends Attraction {
    private List<Date> dates;

    public List<Date> getDates() {
        return dates;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }
}

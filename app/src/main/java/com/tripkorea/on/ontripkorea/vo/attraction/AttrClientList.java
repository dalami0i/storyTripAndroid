package com.tripkorea.on.ontripkorea.vo.attraction;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward Won on 2018-06-11.
 */

public class AttrClientList {

    @SerializedName("item")
    private List<AttrClient> items = new ArrayList<>();

    public List<AttrClient> getItems() {
        return items;
    }

    public void setItems(List<AttrClient> items) {
        this.items = items;
    }
}

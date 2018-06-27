package com.tripkorea.on.ontripkorea.vo.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

///**
// * Created by Edward Won on 2018-05-24.
// */

public class YoutubeItems {
    @SerializedName("items")
    @Expose
    public ArrayList<YoutubeSnippet> items = new ArrayList<>();
}

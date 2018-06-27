package com.tripkorea.on.ontripkorea.vo.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

///**
// * Created by Edward Won on 2018-05-24.
// */

public class YoutubeSnippet {
    @SerializedName("snippet")
    @Expose
    public YoutubeDetail snippet;
}

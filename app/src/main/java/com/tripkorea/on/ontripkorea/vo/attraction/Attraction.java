package com.tripkorea.on.ontripkorea.vo.attraction;

import com.tripkorea.on.ontripkorea.util.Preference;

import java.util.Date;
import java.util.List;

/**
 * Created by YangHC on 2018-06-11.
 */

public class Attraction {
    private List<Integer> categoriyList;   // 0(관광), 1(식당), 2(쇼핑), 3(간식) ...
    private List<Integer> keywordList;
    private List<String> tagList;
    private Preference.Coord coord;
    private String name;
    private List<Date> recommendTimeList; // 0(11:00), 1(13:00), 2(17:00), 3(19:00) ==> 11:00~13:00 & 17:00~19:00
    //etc...


    public List<Integer> getCategoriyList() {
        return categoriyList;
    }

    public void setCategoriyList(List<Integer> categoriyList) {
        this.categoriyList = categoriyList;
    }

    public List<Integer> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<Integer> keywordList) {
        this.keywordList = keywordList;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public Preference.Coord getCoord() {
        return coord;
    }

    public void setCoord(Preference.Coord coord) {
        this.coord = coord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Date> getRecommendTimeList() {
        return recommendTimeList;
    }

    public void setRecommendTimeList(List<Date> recommendTimeList) {
        this.recommendTimeList = recommendTimeList;
    }
}

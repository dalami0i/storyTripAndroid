package com.tripkorea.on.ontripkorea.vo.attraction;

import com.tripkorea.on.ontripkorea.util.Coordinate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by YangHC on 2018-06-11.
 */

public class AttractionSimple implements Serializable{
    private List<Integer> categoriyList;   // 0(관광), 1(식당), 2(쇼핑), 3(간식) ...
    private int idx;
    private String name;
    private double lat;
    private double lon;
    private String route;
    private String summary;
    private String detail;
    private String thumnailAddr;

    public List<Integer> getCategoriyList() {
        return categoriyList;
    }

    public void setCategoriyList(List<Integer> categoriyList) {
        this.categoriyList = categoriyList;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getThumnailAddr() {
        return thumnailAddr;
    }

    public void setThumnailAddr(String thumnailAddr) {
        this.thumnailAddr = thumnailAddr;
    }
}
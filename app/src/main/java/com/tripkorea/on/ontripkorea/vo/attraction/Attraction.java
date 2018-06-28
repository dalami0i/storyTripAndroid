package com.tripkorea.on.ontripkorea.vo.attraction;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangHC on 2018-06-11.
 */

public class Attraction implements Serializable{
    private List<Integer> categoriyList;   // 0(관광), 1(식당), 2(쇼핑), 3(간식) ...
    private List<Integer> keywordList;
    private List<String> tagList;
    private int idx;
    private String name;
    private String category;
    private int score;
    private String tel;
    private int likeCnt;
    private int visitCnt;
    private int reviewCnt;
    private double lat;
    private double lon;
    private String addr;
    private String route;
    private String summary;
    private String detail;
    private String homepage;
    private String thumnailAddr;


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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }

    public int getVisitCnt() {
        return visitCnt;
    }

    public void setVisitCnt(int visitCnt) {
        this.visitCnt = visitCnt;
    }

    public int getReviewCnt() {
        return reviewCnt;
    }

    public void setReviewCnt(int reviewCnt) {
        this.reviewCnt = reviewCnt;
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

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
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

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getThumnailAddr() {
        return thumnailAddr;
    }

    public void setThumnailAddr(String thumnailAddr) {
        this.thumnailAddr = thumnailAddr;
    }
}

package com.tripkorea.on.ontripkorea.util;


/**
 * Created by YangHC on 2018-06-11.
 */

public class Coordinate {
    private double lat;
    private double lon;

    public Coordinate() {
    }

    public Coordinate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
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
}

package com.tripkorea.on.ontripkorea.vo.user;

/**
 * Created by Edward Won on 2018-10-04.
 */

public class UpdateUser {

    private String name;
    private int age;
    private int nationIdx;
    private int gender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getNationIdx() {
        return nationIdx;
    }

    public void setNationIdx(int nationIdx) {
        this.nationIdx = nationIdx;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}

package com.tripkorea.on.ontripkorea.vo.user;

/**
 * Created by YangHC on 2018-06-11.
 */

public class User {
    private String name;
    private int age;
    private int nationCode;     // ex) 82(대한민국)  ref) https://ko.wikipedia.org/wiki/%EA%B5%AD%EC%A0%9C%EC%A0%84%ED%99%94_%EB%82%98%EB%9D%BC_%EB%B2%88%ED%98%B8
    private int sexCode;        // ex) 0(남성), 1(여성)
    private int religionCode;   // ex) 0(기독교), 1(천주교), 2(불교), 3(힌두교) ....

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

    public int getNationCode() {
        return nationCode;
    }

    public void setNationCode(int nationCode) {
        this.nationCode = nationCode;
    }

    public int getSexCode() {
        return sexCode;
    }

    public void setSexCode(int sexCode) {
        this.sexCode = sexCode;
    }

    public int getReligionCode() {
        return religionCode;
    }

    public void setReligionCode(int religionCode) {
        this.religionCode = religionCode;
    }
}

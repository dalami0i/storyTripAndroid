package com.tripkorea.on.ontripkorea.vo.user;

/**
 * Created by YangHC on 2018-06-18.
 */

public class Me extends LoginResponse{
    public static Me instance;

    public static Me getInstance(){
        if(instance == null){
            instance = new Me();
           /* //TODO : 로그인 구현 시 삭제
            // 로그인 구현 전 테스트 용으로 임의 값 집어넣음
            instance.setIdx(25);
            instance.setName("StoryTour");
            instance.setMindAge(20);
            instance.setNationIdx(UserConstants.NATION_KOREA);
//            instance.setReligionCode(UserConstants.RELIGION_NO);
            instance.setGender(UserConstants.SEXCODE_MAN);*/
        }
        return instance;
    }

    public Me(){    }
}

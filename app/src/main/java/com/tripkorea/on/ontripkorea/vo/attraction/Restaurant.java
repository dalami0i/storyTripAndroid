package com.tripkorea.on.ontripkorea.vo.attraction;

import java.util.List;

/**
 * Created by YangHC on 2018-06-11.
 */

public class Restaurant extends AttractionSimple {

    private List<Food> menuList;

    public List<Food> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Food> menuList) {
        this.menuList = menuList;
    }

    class Food{
        // Category Example
        public static final int MEAT = 0;
        public static final int VEGETABLE = 1;
        public static final int RICE = 2;
        public static final int SOUP = 3;
        public static final int NOODLE = 4;


        private String name;
        private List<Integer> categories;
        private int price;
        private String contents;    //이건 추후 확장을 위해 ex) 100g당 8000원, 미국산 등
    }
}

package com.tripkorea.on.ontripkorea.vo.attraction;

import java.util.List;

/**
 * Created by YangHC on 2018-06-11.
 */

public class Accomodation {
    private String name;
    private List<Room> roomList;

    class Room{
        private String name;
        private int price;
        private int minPeople;
        private int maxPeople;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getMinPeople() {
            return minPeople;
        }

        public void setMinPeople(int minPeople) {
            this.minPeople = minPeople;
        }

        public int getMaxPeople() {
            return maxPeople;
        }

        public void setMaxPeople(int maxPeople) {
            this.maxPeople = maxPeople;
        }
    }
}

package com.hjh.wequiz;

public class MyMissionVO {

    private int badge;
    private String location;
    private int star;

    public MyMissionVO(int badge, String location, int star) {
        this.badge = badge;
        this.location = location;
        this.star = star;
    }

    public int getBadge() {
        return badge;
    }

    public String getLocation() {
        return location;
    }

    public int getStar() {
        return star;
    }
}

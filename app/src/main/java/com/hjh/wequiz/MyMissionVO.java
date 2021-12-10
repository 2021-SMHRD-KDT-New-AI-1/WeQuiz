package com.hjh.wequiz;

public class MyMissionVO {

    private String badge;
    private String location;
    private int star;

    public MyMissionVO(String badge, String location, int star) {
        this.badge = badge;
        this.location = location;
        this.star = star;
    }

    public String getBadge() {
        return badge;
    }

    public String getLocation() {
        return location;
    }

    public int getStar() {
        return star;
    }
}

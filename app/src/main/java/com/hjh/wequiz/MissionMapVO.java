package com.hjh.wequiz;

public class MissionMapVO {

    private int missionId;
    private String missionType;
    private String keyword;
    private double lat;
    private double lon;

    public MissionMapVO(int missionId, String missionType, String keyword, double lat, double lon) {
        this.missionId = missionId;
        this.missionType = missionType;
        this.keyword = keyword;
        this.lat = lat;
        this.lon = lon;
    }

    public int getMissionId() {
        return missionId;
    }

    public String getMissionType() {
        return missionType;
    }

    public String getKeyword() {
        return keyword;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

}

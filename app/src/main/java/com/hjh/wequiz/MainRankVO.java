package com.hjh.wequiz;

public class MainRankVO {

    private String mainMedal; /*-메달 모양 (금,은,동)-*/
    private int mainRank; /*-랭킹 순위-*/
    private String mainNick; /*-닉네임-*/
    private String mainProfile; /*-프로필 사진-*/
    private int mainMedalNum; /*-메달 개수-*/

    public MainRankVO(int mainRank, String mainNick, String mainProfile, String mainMedal, int mainMedalNum) {
        this.mainRank = mainRank;
        this.mainNick = mainNick;
        this.mainProfile = mainProfile;
        this.mainMedal = mainMedal;
        this.mainMedalNum = mainMedalNum;
    }

    public int getMainRank() {
        return mainRank;
    }

    public String getMainNick() {
        return mainNick;
    }

    public String getMainProfile() { return mainProfile; }

    public String getMainMedal() {
        return mainMedal;
    }

    public int getMedalNum() { return mainMedalNum;}

}

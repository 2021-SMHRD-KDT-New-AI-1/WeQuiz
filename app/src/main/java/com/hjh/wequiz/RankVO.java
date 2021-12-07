package com.hjh.wequiz;

public class RankVO {

    private int rank; /*-랭킹 순위-*/
    private String nick; /*-닉네임-*/
    private int medal; /*-메달 개수-*/

    public RankVO(int rank, String nick, int profile, int medal) {
        this.rank = rank;
        this.nick = nick;
        this.medal = medal;
    }

    public int getRank() {
        return rank;
    }

    public String getNick() {
        return nick;
    }

    public int getMedal() {
        return medal;
    }
}

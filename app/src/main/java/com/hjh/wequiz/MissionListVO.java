package com.hjh.wequiz;

public class MissionListVO {

    private int mission_id;
    private String mission_type;
    private String location_name;
    private String keyword;
    private String quiz;
    private String answer;

    public MissionListVO(int mission_id, String mission_type, String location_name, String keyword, String quiz, String answer) {
        this.mission_id = mission_id;
        this.mission_type = mission_type;
        this.location_name = location_name;
        this.keyword = keyword;
        this.quiz = quiz;
        this.answer = answer;
    }

    public int getMission_id() {
        return mission_id;
    }

    public String getMission_type() {
        return mission_type;
    }

    public String getLocation_name() {
        return location_name;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getQuiz() {
        return quiz;
    }

    public String getAnswer() {
        return answer;
    }


}

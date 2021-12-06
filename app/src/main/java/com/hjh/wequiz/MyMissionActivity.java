package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MyMissionActivity extends AppCompatActivity {

    RecyclerView rv_myMission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mission);

        ArrayList<MyMissionVO> mdata = new ArrayList<>();

        // 미션 데이터 입력
        mdata.add(new MyMissionVO(1, "광주", 2));
        mdata.add(new MyMissionVO(2, "곡성", 1));
        mdata.add(new MyMissionVO(3, "목포", 1));
        mdata.add(new MyMissionVO(4, "순천", 0));
        mdata.add(new MyMissionVO(5, "여수", 3));
        mdata.add(new MyMissionVO(5, "여수", 3));
        mdata.add(new MyMissionVO(5, "여수", 3));
        mdata.add(new MyMissionVO(5, "여수", 3));
        mdata.add(new MyMissionVO(5, "여수", 3));

        rv_myMission = findViewById(R.id.rv_myMission);
        rv_myMission.setLayoutManager(new LinearLayoutManager(this));

        MyMissionAdapter adapter = new MyMissionAdapter(mdata);
        rv_myMission.setAdapter(adapter);

    }
}
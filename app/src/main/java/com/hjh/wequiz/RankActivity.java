package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class RankActivity extends AppCompatActivity {

    RecyclerView rv_Rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        ArrayList<RankVO> mdata = new ArrayList<>();

        // 미션 데이터 입력
        mdata.add(new RankVO(4, "길도", 2, 2));

        rv_Rank = findViewById(R.id.rv_Rank);
        rv_Rank.setLayoutManager(new LinearLayoutManager(this));

        RankAdapter adapter = new RankAdapter(mdata);
        rv_Rank.setAdapter(adapter);
    }
}
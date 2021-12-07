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
        mdata.add(new RankVO(4, "길도", "길도 사진", 2));
        mdata.add(new RankVO(5, "태태", "태태 사진", 5));
        mdata.add(new RankVO(6, "마르코", "마르코 사진", 5));
        mdata.add(new RankVO(7, "쿼카", "쿼카 사진", 5));
        mdata.add(new RankVO(8, "태태", "태태 사진", 5));
        mdata.add(new RankVO(9, "태태", "태태 사진", 5));
        mdata.add(new RankVO(10, "태태", "태태 사진", 5));


        rv_Rank = findViewById(R.id.rv_Rank);
        rv_Rank.setLayoutManager(new LinearLayoutManager(this));

        RankAdapter adapter = new RankAdapter(mdata);
        rv_Rank.setAdapter(adapter);
    }
}
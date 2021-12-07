package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView img_mainProfile, img_mainLocationRe;
    TextView tv_mainNick, tv_mainLocation;
    Button btn_mainLogin, btn_mainBadgeList, btn_mainMissonStart;
    RecyclerView rv_mainRank;
    private LinearLayoutManager mLayoutManager;

    // 로그인인지 아닌지를 알려줄 텍스트뷰 코드 -> 나중에 삭제
    TextView tv_login_gara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_mainProfile = findViewById(R.id.img_mainProfile);
        img_mainLocationRe = findViewById(R.id.img_mainLocationRe);
        tv_mainNick = findViewById(R.id.tv_mainNick);
        tv_mainLocation = findViewById(R.id.tv_mainLocation);
        btn_mainLogin = findViewById(R.id.btn_mainLogin);
        btn_mainBadgeList = findViewById(R.id.btn_mainBadgeList);
        btn_mainMissonStart = findViewById(R.id.btn_mainMissonStart);

        // 랭킹 Top5 리싸이클러 뷰 코드
        ArrayList<MainRankVO> mdata = new ArrayList<>();
        // 랭킹 데이터 입력
        mdata.add(new MainRankVO(1,"길도","길도사진","금메달",5));
        mdata.add(new MainRankVO(2,"태태","길도사진","금메달",5));
        mdata.add(new MainRankVO(3,"마르코","길도사진","금메달",5));
        mdata.add(new MainRankVO(4,"쿼카","길도사진","금메달",5));
        mdata.add(new MainRankVO(5,"윤지","길도사진","금메달",5));

        rv_mainRank = findViewById(R.id.rv_mainRank);
        // 가로로 리싸이클러 뷰 전환하는 코드
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        MainRankAdapter adapter = new MainRankAdapter(mdata);

        rv_mainRank.setLayoutManager(mLayoutManager);
        rv_mainRank.setAdapter(adapter); // 여기까지 리싸이클러 뷰 코드


        btn_mainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_mainBadgeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RankActivity.class);
                startActivity(intent);
            }
        });
        btn_mainMissonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QuizLoadingActivity.class);
                startActivity(intent);
            }
        });

        tv_login_gara = findViewById(R.id.tv_login_gara);
        String gara = tv_login_gara.getText().toString();

        // 로그인인 상태일 때 버튼 이벤트 변화
        if(gara.equals("로그인 상태")) {
            btn_mainLogin.setText("로그아웃");
        } else {
            btn_mainLogin.setText("로그인");
        }



    }
}
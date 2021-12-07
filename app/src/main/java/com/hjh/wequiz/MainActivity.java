package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView img_mainProfile, img_mainLocationRe;
    TextView tv_mainNick, tv_mainLocation;
    Button btn_mainLogin, btn_mainBadgeList, btn_mainMissonStart;

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

    }
}
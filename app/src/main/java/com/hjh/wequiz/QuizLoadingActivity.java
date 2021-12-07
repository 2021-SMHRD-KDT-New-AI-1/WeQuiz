package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class QuizLoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_loading);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MissionListActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500); //1.5초 후 미션리스트 액티비티로 전환
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    }

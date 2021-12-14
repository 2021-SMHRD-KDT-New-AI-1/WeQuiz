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
//                Intent getIntent = getIntent();
//                String myLocation = getIntent.getStringExtra("myLocation");
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
//                intent.putExtra("myLocation", myLocation);
                startActivity(intent);
                finish();
            }
        }, 1500); //1.5초 후 맵 액티비티로 전환

    }
}
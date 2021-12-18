package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class savemission extends AppCompatActivity {

    private static final String TAG = "savemissionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savemission);
        View btn_savemis = findViewById(R.id.btn_savemis);
        View btn_changemis = findViewById(R.id.btn_changemis);
        requestWindowFeature( Window.FEATURE_NO_TITLE );

//        TextView mission_title = findViewById(R.id.mission_title);
//        TextView mission_type = findViewById(R.id.mission_type);



        btn_changemis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"클릭성공");
            }
        });

        btn_savemis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"클릭성공");
            }
        });

//        String title;
//        String type;
//
//        Intent intent = getIntent();
//        title = intent.getStringExtra("제목");
//        type = intent.getStringExtra("유형");
//
//        System.out.println("제목 텍스트 :" + title);
//        System.out.println("유형 테스트 :" + type);


//        mission_title.setText(title);
//        mission_type.setText(type);

    }
}
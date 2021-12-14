package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class savemission extends AppCompatActivity {

    private static final String TAG = "savemissionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savemission);
        View btn_savemis = findViewById(R.id.btn_savemis);
        View btn_changemis = findViewById(R.id.btn_changemis);
        requestWindowFeature( Window.FEATURE_NO_TITLE );



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


    }
}
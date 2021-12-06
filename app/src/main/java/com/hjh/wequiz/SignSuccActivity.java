package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignSuccActivity extends AppCompatActivity {
    Button btn_signSuccReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_succ);

        btn_signSuccReturn = findViewById(R.id.btn_signSuccReturn);

        btn_signSuccReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignActivity.class);
                startActivity(intent);
            }
        });
    }
}
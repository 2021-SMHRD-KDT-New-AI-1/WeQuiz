package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SignActivity extends AppCompatActivity {

    RadioGroup rg_gender;
    RadioButton rb_woman, rb_man;
    String gender;

    private TextView tv_birth;//'생년월일'
    private DatePickerDialog.OnDateSetListener callbackMethod;//'생년월일'

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        rg_gender = findViewById(R.id.rg_gender);
        rb_woman = findViewById(R.id.rb_woman);
        rb_man = findViewById(R.id.rb_man);

        //라디오 체크 리스너
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_woman) {
                    Log.d("radiocheck", "woman");
                    gender = rb_woman.getText().toString();
                } else if (i == R.id.rb_man) {
                    Log.d("radiocheck", "man");
                    gender = rb_man.getText().toString();
                }
            }
        });

        this.InitializeView();//'생년월일'
        this.InitializeListener();//'생년월일'
    }

    public void InitializeView()//'생년월일'
    {
        tv_birth = (TextView) findViewById(R.id.tv_birth);
    }

    public void InitializeListener()//'생년월일'
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tv_birth.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        };
    }
    public void OnClickHandler(View view) //'생년월일'
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 1990, 7, 1);
        dialog.show();
    }
    }

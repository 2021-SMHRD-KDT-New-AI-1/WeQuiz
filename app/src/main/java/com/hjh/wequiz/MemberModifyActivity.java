package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

public class MemberModifyActivity extends AppCompatActivity {

    private TextView textView_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    TextView tv_birthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_modify);
        tv_birthDate = findViewById(R.id.tv_birthDate);

        this.InitializeView(); // '생년월일'
        this.InitializeListener(); // '생년월일'

    }

    private void InitializeView() {
        textView_Date = (TextView) findViewById(R.id.tv_birthDate);
    }

    private void InitializeListener() {
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear
                    , int dayOfMonth) {
                textView_Date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        };
    }
    public void OnClickHandler(View view) {
        DatePickerDialog dialog = new DatePickerDialog
                (this, callbackMethod, 1990, 7, 1);
        dialog.show();
    }
}
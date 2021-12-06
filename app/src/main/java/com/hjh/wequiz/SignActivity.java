package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    RadioGroup rg_signGender;
    RadioButton rb_signWoman, rb_signMan;
    String gender;
    Button btn_sign;
    EditText  et_signId, et_signPw, et_signChekPw, et_signNick;
    boolean registStatus;

    private TextView tv_signBirth;//'생년월일'
    private DatePickerDialog.OnDateSetListener callbackMethod;//'생년월일'

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        et_signId = findViewById(R.id.et_signId);
        et_signPw = findViewById(R.id.et_signPw);
        et_signChekPw = findViewById(R.id.et_signCheckPw);
        et_signNick = findViewById(R.id.et_signNick);

        btn_sign = findViewById(R.id.btn_sign);

        rg_signGender = findViewById(R.id.rg_signGender);
        rb_signWoman = findViewById(R.id.rb_signWoman);
        rb_signMan = findViewById(R.id.rb_signMan);

        // 서버와 통신
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        // 가입하기 버튼 클릭!
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = et_signId.getText().toString();
                String pw = et_signPw.getText().toString();
                String nick = et_signNick.getText().toString();
                String birth = tv_signBirth.getText().toString();

                // 입력값 검사 부분 ~
                boolean checkPw = pw.equals(et_signChekPw.getText().toString());
                boolean checkDuplicateId = true;
                boolean checkDuplicateNick = true;
                boolean checkBlank = true;

                if (!checkPw) {
                    // 비밀번호와 비밀번호 확인 값이 다른 경우!
                    Toast.makeText(SignActivity.this,"비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }else if(!checkDuplicateId){
                    // 아이디 중복검사를 실시하지 않았을때!
                    Toast.makeText(SignActivity.this,"아이디 중복 검사를 해주세요.", Toast.LENGTH_SHORT).show();
                }else if(!checkDuplicateNick){
                    // 닉네임 중복검사를 실시하지 않았을때!
                    Toast.makeText(SignActivity.this,"닉네임 중복 검사를 해주세요.", Toast.LENGTH_SHORT).show();
                }else if(!checkBlank){
                    // 입력칸에 공란이 있을 경우~
                    Toast.makeText(SignActivity.this,"모든 입력란에 입력을 완료해주세요.",Toast.LENGTH_SHORT).show();
                }else {
                    postRegist(id, pw, nick, birth, gender);
                }
            }
        });

        //라디오 체크 리스너
        rg_signGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_signWoman) {
                    Log.d("radiocheck", "woman");
                    gender = rb_signWoman.getText().toString();
                } else if (i == R.id.rb_signMan) {
                    Log.d("radiocheck", "man");
                    gender = rb_signMan.getText().toString();
                }
            }
        });

        this.InitializeView();//'생년월일'
        this.InitializeListener();//'생년월일'
    }

    // Json 파일을 만들어 웹 서버로 보내기!!
    public void postRegist(String id, String pw, String nick, String birth, String gender){
        String url = "~~~ 웹서버 주소 자리~~~";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 응답 성공
                        try {
                            JSONObject jsonObject = (JSONObject) (new JSONArray(response).get(0));
                            Log.d("status : ", jsonObject.getString("status"));
                            registStatus = true;
                            Intent intent = new Intent(SignActivity.this, SignSuccActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("mem_id", id);
                params.put("pw",pw);
                params.put("nick", nick);
                params.put("birth", birth);
                params.put("gender",gender);

                return params;
            }
        };
        requestQueue.add(request);
    }

    public void InitializeView()//'생년월일'
    {
        tv_signBirth = (TextView) findViewById(R.id.tv_signBirth);
    }

    public void InitializeListener()//'생년월일'
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tv_signBirth.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        };
    }
    public void OnClickHandler(View view) //'생년월일'
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 1990, 7, 1);
        dialog.show();
    }


    }

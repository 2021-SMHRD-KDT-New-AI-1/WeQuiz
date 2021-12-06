package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
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

public class MemberModifyActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    EditText et_memberModifyNick, et_memberModifyPw, et_memberModifyChangePw,et_memberModifyCheckChangePw;
    Button btn_memberModify;
    TextView tv_memberModifyNick, tv_memberModifyBirthDate;

    // '생년월일'
    private TextView textView_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    TextView tv_birthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_modify);
        tv_birthDate = findViewById(R.id.tv_memberModifyBirthDate);
        tv_memberModifyBirthDate = findViewById(R.id.tv_memberModifyBirthDate);
        tv_memberModifyNick = findViewById(R.id.tv_memberModifyNick);
        et_memberModifyNick = findViewById(R.id.et_memberModifyNick);
        et_memberModifyPw = findViewById(R.id.et_memberModifyPw);
        et_memberModifyChangePw = findViewById(R.id.et_memberModifyChangePw);
        et_memberModifyCheckChangePw = findViewById(R.id.et_memberModifyChangePw);

        btn_memberModify = findViewById(R.id.btn_memberModify);

        // 서버와 통신!
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        // 로그인 유지기능 추가후 작성예정~!~!~!~!~!~!
        //String mem_id = PreferenceManager.getString(this,"mem_id");
        // 후루꾸~~~
        String mem_id = "999";
        getMemberInfo(mem_id);

        // '수정하기' 버튼 클릭 리스너! ( 수정 완료 후 메인페이지로 이동)
        btn_memberModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nick = et_memberModifyNick.getText().toString();
                String currentPw = et_memberModifyPw.getText().toString();
                String changePw = et_memberModifyChangePw.getText().toString();
                String birth = textView_Date.getText().toString();

                // 입력값 검사 부분
                boolean checkPw = changePw.equals(et_memberModifyCheckChangePw.getText().toString());
                boolean checkDuplicateNick = true;
                boolean checkBlank = true;

                if (!checkPw){
                    // 변경할 비밀번호와 재확인 비밀번호의 값이 다른 경우
                    Toast.makeText(MemberModifyActivity.this,
                            "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }else if (!checkDuplicateNick){
                    // 닉네임 중복검사
                    Toast.makeText(MemberModifyActivity.this,
                            "닉네임 중복검사를 실시해주세요.",Toast.LENGTH_SHORT).show();
                }else if (!checkBlank){
                    // 공란 검사
                    Toast.makeText(MemberModifyActivity.this,
                            "모든 입력란에 입력을 완료해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    // 입력완료 후
                    Toast.makeText(MemberModifyActivity.this,
                            "회원정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    // id와 입력받은 값을 매개변수로 하여 modify 메소드 호출
                    modify(mem_id, currentPw, changePw, nick, birth);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        this.InitializeView(); // '생년월일'
        this.InitializeListener(); // '생년월일'

        // 탈퇴하기~누르기~~~~?


    }

    // Json 파일을 만들어 웹 서버로 보내기~!
    public void postModify(String id, String nick, String pw, String birth){
        String url = "~~~~~~~~ 주소 ~~~~~~~~";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) (new JSONArray(response).get(0));
                            Log.d("status : ", jsonObject.getString("status"));
                            Intent intent = new Intent(MemberModifyActivity.this, MainActivity.class);
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
                params.put("id", id);
                params.put("nick",nick);
                params.put("pw",pw);
                params.put("birth",birth);

                return params;
            }
        };
        requestQueue.add(request);
    }

    // 회원삭제 웹 서버 보내기~?

    public void modify(String id, String currentPw, String nick, String changePw, String birth){
        // 입력한 현재 비밀번호가 일치하는지 확인, id와 매치되는 비밀번호인지 로그인 요청으로 확인가능하다.
        String url = "~~~~~~~ 로그인 주소~~~~~~~~~"; //(로그인 요청! - 회원테이블에서 확인)
        // 로그인 요청 후 -> 로그인 여부에 따라 status에 success 혹은 fail을 담아서 전달
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) (new JSONArray(response).get(0));
                            Log.d("json ???", jsonObject.toString());
                            Log.d("result : ", jsonObject.getString("result"));
                            String status = jsonObject.getString("result");

                            if (status.equals("success")) {
                                // 로그인 성공 (비밀번호가 일치)
                                postModify(id, nick, changePw, birth); // postModify 메소드 호출
                            } else {
                                // 로그인 실패 (비밀번호 불일치)
                                Toast.makeText(MemberModifyActivity.this, "현재 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }
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
                params.put("id", id);
                params.put("pw",currentPw);

                return params;
            }
        };
        requestQueue.add(request);
    }

    // 회원 정보를 서버에 요청하여 받아오는 메소드~
    public void getMemberInfo(String id){
        String url = "~~~~~~ 회원정보 주소~~~~~~";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 응답 성공!
                        try {
                            Log.v("abcd", response);
                            JSONObject jsonObject = (JSONObject) (new JSONArray(response)).get(0);
                            String nick = jsonObject.getString("nick"); // 닉네임 받아오기
                            tv_memberModifyNick.setText(nick);

                            String birth = jsonObject.getString("birth"); // 생년월일 받아오기
                            String birthdate = birth.substring(0, 10); // 받아온 날짜 중 년월일만 표시!
                            tv_memberModifyBirthDate.setText(birthdate);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            @Override
          protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("mem_id", id);

                return params;
            }
        };
        requestQueue.add(request);
    }





    // '생년월일'
    private void InitializeView() {
        textView_Date = (TextView) findViewById(R.id.tv_memberModifyBirthDate);
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
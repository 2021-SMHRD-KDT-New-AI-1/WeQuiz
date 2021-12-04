package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class LoginActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    EditText et_loginId, et_loginPw;
    Button btn_login;


    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        et_loginId = findViewById(R.id.et_loginId);
        et_loginPw = findViewById(R.id.et_loginPw);
        btn_login = findViewById(R.id.btn_login);


        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }



        //로그인 버튼 누르면 로그인 이동
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = et_loginId.getText().toString();
                String pw = et_loginPw.getText().toString();

                postLogin(id, pw);
            }
        });
    }

    // Json파일을 만들어 웹 서버로 보내기
    public void postLogin(String id, String pw) {
        String url = "http://172.30.1.34:3003/Member/Login";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 응답 성공 응답 성공 이야후~~!!
                        try {
                            JSONObject jsonObject = (JSONObject) (new JSONArray(response).get(0));
                            Log.d("result : ", jsonObject.getString("result"));
                            String status = jsonObject.getString("result");
                            if (status.equals("success")) {

                                Toast.makeText(mContext, "로그인 성공", Toast.LENGTH_SHORT).show();

//                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                startActivity(intent);
                            }else{
                                Toast.makeText(LoginActivity.this, "아이디나 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
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
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("pw", pw);

                return params;
            }
        };
        requestQueue.add(request);
    }
}
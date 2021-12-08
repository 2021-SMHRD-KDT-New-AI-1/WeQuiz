package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    RequestQueue requestQueue;
    Context mContext;
    ArrayList<RankVO> mData;

    MainRankAdapter adapter;

    ImageView img_mainProfile, img_mainLocationRe;
    TextView tv_mainNick, tv_mainLocation;
    Button btn_mainLogin, btn_mainBadgeList, btn_mainMissonStart;
    RecyclerView rv_mainRank;
    //private LinearLayoutManager mLayoutManager;

    // 로그인인지 아닌지를 알려줄 텍스트뷰 코드 -> 나중에 삭제
    TextView tv_login_gara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        img_mainProfile = findViewById(R.id.img_mainProfile);
        img_mainLocationRe = findViewById(R.id.img_mainLocationRe);
        tv_mainNick = findViewById(R.id.tv_mainNick);
        tv_mainLocation = findViewById(R.id.tv_mainLocation);
        btn_mainLogin = findViewById(R.id.btn_mainLogin);
        btn_mainBadgeList = findViewById(R.id.btn_mainBadgeList);
        btn_mainMissonStart = findViewById(R.id.btn_mainMissonStart);

        mData = new ArrayList<>();

        rv_mainRank = findViewById(R.id.rv_mainRank);
        // 가로로 리싸이클러 뷰 전환하는 코드
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_mainRank.setLayoutManager(mLayoutManager);

        getRankInfo();

        adapter = new MainRankAdapter(mData);
        rv_mainRank.setAdapter(adapter); // 여기까지 리싸이클러 뷰 코드

        btn_mainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_mainBadgeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RankActivity.class);
                startActivity(intent);
            }
        });
        btn_mainMissonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QuizLoadingActivity.class);
                startActivity(intent);
            }
        });

        tv_login_gara = findViewById(R.id.tv_login_gara);
        String gara = tv_login_gara.getText().toString();

        // 로그인인 상태일 때 버튼 이벤트 변화
        if(gara.equals("로그인 상태")) {
            btn_mainLogin.setText("로그아웃");
        } else {
            btn_mainLogin.setText("로그인");
        }



    }

    public void getRankInfo() {
        String url = "http://172.30.1.34:3003/Rank/RankInfo";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++) {
                                if(i == 5) {
                                    break;
                                }
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String ranking = jsonObject.getString("ranking");
                                String nick = jsonObject.getString("nick");
                                String mem_image = jsonObject.getString("mem_image");
                                String badge_num = jsonObject.getString("badge_num");

                                RankVO rank_info = new RankVO(Integer.parseInt(ranking), nick, mem_image, Integer.parseInt(badge_num));
                                mData.add(rank_info);
                                adapter.notifyDataSetChanged();
                                Log.d("rankInfo : ", "info" + i);
                            }
                            System.out.println(mData);

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
                params.put("status", "200");

                return params;
            }
        };
        requestQueue.add(request);
    }

}
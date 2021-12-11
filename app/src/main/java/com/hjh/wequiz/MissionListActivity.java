package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MissionListActivity extends AppCompatActivity {

    static boolean click_r;
    // 플로팅버튼 상태
    private boolean fabMain_status = false;

    FloatingActionButton float_home, float_map, float_plus, float_plus2, float_mission, float_badge, float_my;
    TextView float_mission_text;
    ImageView transparent;
    RecyclerView rv_missionList;
    ArrayList<MissionListVO> mData;

    int num;

    // 사진 문제 카메라 접근
    Button btn_camera;
    // 객관식, 주관식 문제 이동 버튼
    Button btn_choice, btn_short;
    // intent로 액티비티 간 이동하기 위한 상수
    public static final int sub = 1001;


    RequestQueue requestQueue;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_list);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        mContext = this;


        String mem_id = PreferenceManager.getString(mContext, "mem_id");
        Intent intent = getIntent();
        String location_name = intent.getStringExtra("location_name");

        getMissionList(mem_id, location_name);

        // 플로팅 버튼 초기화
        float_plus = findViewById(R.id.float_plus);
        float_plus2 = findViewById(R.id.float_plus2);
        float_home = findViewById(R.id.float_home);
        float_map = findViewById(R.id.float_map);
        float_mission = findViewById(R.id.float_mission);
        float_badge = findViewById(R.id.float_badge);
        float_my = findViewById(R.id.float_my);
        transparent = findViewById(R.id.transparent);

        float_mission_text = findViewById(R.id.float_mission_text);


        transparent.setVisibility(View.INVISIBLE);

        // 메인플로팅 버튼 클릭
        float_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                if(num % 2 == 0){
                    transparent.clearAnimation();
                    transparent.setVisibility(View.INVISIBLE);
                }else{
                    Animation anima = AnimationUtils.loadAnimation(MissionListActivity.this, R.anim.alpha);
                    transparent.startAnimation(anima);
                    transparent.setVisibility(View.VISIBLE);
                }
                toggleFab();
            }
        });

         //플로팅 홈버튼 클릭
        float_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(MissionListActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(MissionListActivity.this, "홈 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });
         //플로팅 미션지도 버튼 클릭
        float_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(MissionListActivity.this, MapActivity.class);
                startActivity(intent);
                Toast.makeText(MissionListActivity.this, "미션지도 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

         //플로팅 마이미션 버튼 클릭
        float_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(MissionListActivity.this, MyMissionActivity.class);
                startActivity(intent);
                Toast.makeText(MissionListActivity.this, "마이미션 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

         //플로팅 전국뱃지지도 버튼 클릭
        float_badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(MissionListActivity.this, BadgeMapActivity.class);
                startActivity(intent);
                Toast.makeText(MissionListActivity.this, "전국뱃지지도 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

         //플로팅 내 정보 수정 버튼 클릭
        float_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(MissionListActivity.this, MemberModifyActivity.class);
                startActivity(intent);
                Toast.makeText(MissionListActivity.this, "내정보수정 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        // 어댑터 부착 코드
        mData = new ArrayList<>();

        mData.add(new MissionListVO(1,"choice","양림동","펭귄마을","태태", "바보"));
        mData.add(new MissionListVO(1,"choice","양림동","펭귄마을","태태", "바보"));
        mData.add(new MissionListVO(1,"choice","양림동","펭귄마을","태태", "바보"));


        rv_missionList = findViewById(R.id.rv_missionList);
        rv_missionList.setLayoutManager(new LinearLayoutManager(this));

        MissionListAdapter adapter = new MissionListAdapter(mData);
        rv_missionList.setAdapter(adapter);


    }

    public void getMissionList(String mem_id, String location_name){

        String url = "http://172.30.1.34:3003/Mission/MissionList";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                int mission_id = jsonObject.getInt("mission_id");
                                String mission_type = jsonObject.getString("mission_type");
                                String location_name = jsonObject.getString("location_name");
                                String keyword = jsonObject.getString("keyword");
                                String quiz = jsonObject.getString("quiz");
                                String answer = jsonObject.getString("answer");


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
                params.put("mem_id", mem_id);
                params.put("location_name", location_name);

                return params;
            }
        };
        requestQueue.add(request);
    }


    public void toggleFab() {
        if (fabMain_status) {
            // 플로팅 액션 버튼 닫기
            // 애니메이션 추가
            ObjectAnimator fh_animation = ObjectAnimator.ofFloat(float_home, "translationY", 0f);
            fh_animation.start();
            ObjectAnimator fmy_animation = ObjectAnimator.ofFloat(float_my, "translationY", 0f);
            fmy_animation.start();
            ObjectAnimator fb_animation = ObjectAnimator.ofFloat(float_badge, "translationY", 0f);
            fb_animation.start();
            ObjectAnimator fmi_animation = ObjectAnimator.ofFloat(float_mission, "translationY", 0f);
            fmi_animation.start();
            ObjectAnimator fmap_animation = ObjectAnimator.ofFloat(float_map, "translationY", 0f);
            fmap_animation.start();

            // 메인 플로팅 이미지 변경
            float_plus.setImageResource(R.drawable.float_plus);

        } else {
            // 플로팅 액션 버튼 열기
            ObjectAnimator fmy_animation = ObjectAnimator.ofFloat(float_my, "translationY", -180f);
            fmy_animation.start();
            ObjectAnimator fb_animation = ObjectAnimator.ofFloat(float_badge, "translationY", -360f);
            fb_animation.start();
            ObjectAnimator fmi_animation = ObjectAnimator.ofFloat(float_mission, "translationY", -540f);
            fmi_animation.start();
            ObjectAnimator fmap_animation = ObjectAnimator.ofFloat(float_map, "translationY", -720f);
            fmap_animation.start();
            ObjectAnimator fh_animation = ObjectAnimator.ofFloat(float_home, "translationY", -900f);
            fh_animation.start();

            // 메인 플로팅 이미지 변경
            float_plus.setImageResource(R.drawable.float_plus2);
        }
        // 플로팅 버튼 상태 변경
        fabMain_status = !fabMain_status;

    }

}


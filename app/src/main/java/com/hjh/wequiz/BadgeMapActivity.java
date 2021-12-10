package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

public class BadgeMapActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    Context mContext;

    ArrayList<String> myBadges;

    TextView tv_jeollanamdo1, tv_jeollanamdo2, tv_jeollanbukdo1, tv_jeollanbukdo2;
    ImageView handle;
    LinearLayout linear;
    LayoutInflater inflater;


    static boolean click_r;
    // 플로팅버튼 상태
    private boolean fabMain_status = false;

    FloatingActionButton float_home, float_map, float_plus, float_plus2, float_mission, float_badge, float_my;
    TextView float_mission_text;
    ImageView transparent;

    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge_map);

        mContext = this;

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        myBadges = new ArrayList<>();
        String mem_id = PreferenceManager.getString(mContext, "mem_id");
        getMyBadge(mem_id);

        tv_jeollanamdo1 = (TextView) findViewById(R.id.tv_jeollanamdo1);
        tv_jeollanamdo2 = (TextView) findViewById(R.id.tv_jeollanamdo2);
        tv_jeollanbukdo1 = findViewById(R.id.tv_jeollanbukdo1);
        tv_jeollanbukdo2 = findViewById(R.id.tv_jeollanbukdo2);


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

        handle = (ImageView) findViewById(R.id.handle);

        SlidingDrawer drawer = (SlidingDrawer)findViewById(R.id.slide);
        linear = findViewById(R.id.content);
        inflater = getLayoutInflater();


        tv_jeollanamdo1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout(R.layout.bc_jeollanamdo);
                System.out.println(myBadges);

                View view = inflater.inflate(R.layout.bc_jeollanamdo, linear, true);
                changeBadgeStatus(view);

                drawer.animateClose();
            }
        });

        tv_jeollanamdo2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout(R.layout.bc_jeollanamdo);

                View view = inflater.inflate(R.layout.bc_jeollanamdo, linear, true);
                changeBadgeStatus(view);

                drawer.animateClose();
            }
        });

        tv_jeollanbukdo1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout(R.layout.bc_jeollabokdo);

                View view = inflater.inflate(R.layout.bc_jeollabokdo, linear, true);
                changeBadgeStatus(view);

                drawer.animateClose();
            }
        });

        tv_jeollanbukdo2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout(R.layout.bc_jeollabokdo);

                View view = inflater.inflate(R.layout.bc_jeollabokdo, linear, true);
                changeBadgeStatus(view);

                drawer.animateClose();
            }
        });

        // 메인플로팅 버튼 클릭
        float_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                if(num % 2 == 0){
                    transparent.clearAnimation();
                    transparent.setVisibility(View.INVISIBLE);
                }else{
                    Animation anima = AnimationUtils.loadAnimation(BadgeMapActivity.this, R.anim.alpha);
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
                Intent intent = new Intent(BadgeMapActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(BadgeMapActivity.this, "홈 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });
        //플로팅 미션지도 버튼 클릭
        float_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(BadgeMapActivity.this, MapActivity.class);
                startActivity(intent);
                Toast.makeText(BadgeMapActivity.this, "미션지도 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        //플로팅 마이미션 버튼 클릭
        float_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(BadgeMapActivity.this, MyMissionActivity.class);
                startActivity(intent);
                Toast.makeText(BadgeMapActivity.this, "마이미션 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        //플로팅 전국뱃지지도 버튼 클릭
        float_badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(BadgeMapActivity.this, BadgeMapActivity.class);
                startActivity(intent);
                Toast.makeText(BadgeMapActivity.this, "전국뱃지지도 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        //플로팅 내 정보 수정 버튼 클릭
        float_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(BadgeMapActivity.this, MemberModifyActivity.class);
                startActivity(intent);
                Toast.makeText(BadgeMapActivity.this, "내정보수정 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void changeLayout(int layout){
        linear.removeAllViews();
        inflater.inflate(layout, linear, true);
    }

    public void changeBadgeStatus(View view) {
        for (String badge : myBadges) {
            int view_resId = getResources().getIdentifier(badge, "id", mContext.getPackageName());
            ImageView badge_location = (ImageView) view.findViewById(view_resId);
            int img_resId = getResources().getIdentifier(badge, "drawable", mContext.getPackageName());
            badge_location.setImageResource(img_resId);
        }
    }

    public void getMyBadge(String id) {
        String url = "http://172.30.1.34:3003/Badge/MyBadge";
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
                                String badge = jsonObject.getString("badge");
                                System.out.println(badge);
                                myBadges.add(badge);
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
                params.put("mem_id", id);

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
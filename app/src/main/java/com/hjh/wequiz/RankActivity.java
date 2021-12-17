package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class RankActivity extends AppCompatActivity {

    String ip;

    RequestQueue requestQueue;
    Context mContext;
    ArrayList<RankVO> mData;

    RankAdapter adapter;

    RecyclerView rv_Rank;
    ImageView[] img_rankProfiles;
    TextView[] tv_rankNicks, tv_rankBadges;

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
        setContentView(R.layout.activity_rank);

        ip = ((MyApplication) getApplicationContext()).getIp();
        mContext = this;

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

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        img_rankProfiles = new ImageView[3];
        tv_rankNicks = new TextView[3];
        tv_rankBadges = new TextView[3];
        for (int i = 0; i < img_rankProfiles.length; i++) {
            int profileResID = getResources().getIdentifier("img_rankProfile" + (i + 1), "id", mContext.getPackageName());
            int nickResID = getResources().getIdentifier("tv_rankNick" + (i + 1), "id", mContext.getPackageName());
            int badgeResID = getResources().getIdentifier("tv_rankBadge" + (i + 1), "id", mContext.getPackageName());

            img_rankProfiles[i] = findViewById(profileResID);
            tv_rankNicks[i] = findViewById(nickResID);
            tv_rankBadges[i] = findViewById(badgeResID);
        }


        mData = new ArrayList<>();

        rv_Rank = findViewById(R.id.rv_Rank);
        rv_Rank.setLayoutManager(new LinearLayoutManager(this));

        getRankInfo();

        System.out.println(mData);
        adapter = new RankAdapter(mData);
        rv_Rank.setAdapter(adapter);



        // 메인플로팅 버튼 클릭
        float_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                if(num % 2 == 0){
                    transparent.clearAnimation();
                    transparent.setVisibility(View.INVISIBLE);
                }else{
                    Animation anima = AnimationUtils.loadAnimation(RankActivity.this, R.anim.alpha);
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
                Intent intent = new Intent(RankActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(RankActivity.this, "홈 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });
        //플로팅 미션지도 버튼 클릭
        float_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(RankActivity.this, MapActivity.class);
                startActivity(intent);
                Toast.makeText(RankActivity.this, "미션지도 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        //플로팅 마이미션 버튼 클릭
        float_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(RankActivity.this, MyMissionActivity.class);
                startActivity(intent);
                Toast.makeText(RankActivity.this, "마이미션 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        //플로팅 전국뱃지지도 버튼 클릭
        float_badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(RankActivity.this, BadgeMapActivity.class);
                startActivity(intent);
                Toast.makeText(RankActivity.this, "전국뱃지지도 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        //플로팅 내 정보 수정 버튼 클릭
        float_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(RankActivity.this, MemberModifyActivity.class);
                startActivity(intent);
                Toast.makeText(RankActivity.this, "내정보수정 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getRankInfo() {
        String url = ip + "/Badge/RankInfo";
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
                                String ranking = jsonObject.getString("ranking");
                                String nick = jsonObject.getString("nick");
                                String mem_image = jsonObject.getString("mem_image");
                                String badge_num = jsonObject.getString("badge_num");

                                if(i < 3){
                                    img_rankProfiles[i].setImageBitmap(StringToBitmap(mem_image));
                                    tv_rankNicks[i].setText(nick);
                                    tv_rankBadges[i].setText(badge_num);
                                } else {
                                    RankVO rank_info = new RankVO(Integer.parseInt(ranking), nick, mem_image, Integer.parseInt(badge_num));
                                    mData.add(rank_info);
                                    adapter.notifyDataSetChanged();
                                }

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
                params.put("status", "200");

                return params;
            }
        };
        requestQueue.add(request);
    }

    // 서버로부터 받아온 이미지 스트링 -> 비트맵 변환
    public static Bitmap StringToBitmap(String encodedString){
        try{
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte,0,encodeByte.length);
            return bitmap;
        }catch (Exception e){
            e.getMessage();
            return null;
        }
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
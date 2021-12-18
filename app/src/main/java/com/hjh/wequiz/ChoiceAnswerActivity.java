package com.hjh.wequiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ChoiceAnswerActivity extends AppCompatActivity {

    static boolean click_r;
    // 플로팅버튼 상태
    private boolean fabMain_status = false;

    FloatingActionButton float_home, float_map, float_plus, float_plus2, float_mission, float_badge, float_my;
    TextView float_mission_text;
    ImageView transparent;

    int num;

    String ip;

    RequestQueue requestQueue;
    Context mContext;

    TextView tv_choiceLocationName, tv_choiceQuiz;
    Button[] btn_choices;
    Button btn_choiceSubmit;

    ArrayList<String> keywords;

    // 팝업창
    AlertDialog.Builder builder;
    AlertDialog ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_answer);

        ip = ((MyApplication) getApplicationContext()).getIp();
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        mContext = this;
        String mem_id = PreferenceManager.getString(mContext, "mem_id");

        keywords = new ArrayList<>();

        tv_choiceLocationName = findViewById(R.id.tv_choiceLocationName);
        tv_choiceQuiz = findViewById(R.id.tv_choiceQuiz);
        btn_choices = new Button[3];

        for(int i = 0; i < btn_choices.length; i++) {
            int resId = getResources().getIdentifier("btn_choice" + (i + 1), "id", getPackageName());
            btn_choices[i] = findViewById(resId);
        }

        Intent intent = getIntent();
        int mission_id = intent.getIntExtra("mission_id", 0);
        String location_name = intent.getStringExtra("location_name");
        String quiz = intent.getStringExtra("quiz");
        String answer = intent.getStringExtra("answer");

        tv_choiceLocationName.setText(location_name);
        tv_choiceQuiz.setText(quiz);
        makeChoiceQuiz(answer);

        // 객관식 선택
        btn_choices[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userAns = btn_choices[0].getText().toString();
                if(userAns.equals(answer)) {
                    Toast.makeText(mContext,"정답입니다!", Toast.LENGTH_SHORT).show();
                    updateSucc(mem_id, mission_id);
                    succPop();
                } else {
                    Toast.makeText(mContext,"땡!!!!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_choices[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userAns = btn_choices[1].getText().toString();
                if(userAns.equals(answer)) {
                    Toast.makeText(mContext,"정답입니다!", Toast.LENGTH_SHORT).show();
                    updateSucc(mem_id, mission_id);
                    succPop();
                } else {
                    Toast.makeText(mContext,"땡!!!!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_choices[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userAns = btn_choices[2].getText().toString();
                if(userAns.equals(answer)) {
                    Toast.makeText(mContext,"정답입니다!", Toast.LENGTH_SHORT).show();
                    updateSucc(mem_id, mission_id);
                    succPop();
                } else {
                    Toast.makeText(mContext,"땡!!!!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


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
                    Animation anima = AnimationUtils.loadAnimation(ChoiceAnswerActivity.this, R.anim.alpha);
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
                Intent intent = new Intent(ChoiceAnswerActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(ChoiceAnswerActivity.this, "홈 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });
        //플로팅 미션지도 버튼 클릭
        float_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(ChoiceAnswerActivity.this, MapActivity.class);
                startActivity(intent);
                Toast.makeText(ChoiceAnswerActivity.this, "미션지도 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        //플로팅 마이미션 버튼 클릭
        float_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(ChoiceAnswerActivity.this, MyMissionActivity.class);
                startActivity(intent);
                Toast.makeText(ChoiceAnswerActivity.this, "마이미션 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        //플로팅 전국뱃지지도 버튼 클릭
        float_badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(ChoiceAnswerActivity.this, BadgeMapActivity.class);
                startActivity(intent);
                Toast.makeText(ChoiceAnswerActivity.this, "전국뱃지지도 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        //플로팅 내 정보 수정 버튼 클릭
        float_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(ChoiceAnswerActivity.this, MemberModifyActivity.class);
                startActivity(intent);
                Toast.makeText(ChoiceAnswerActivity.this, "내정보수정 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

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

    public void makeChoiceQuiz(String answer){

        String url = ip + "/Mission/KeywordList";
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

                                String keyword = jsonObject.getString("keyword");
                                keywords.add(keyword);
                            }

                            Random rand = new Random();

                            ArrayList<Integer> randKeywords = new ArrayList<>();
                            while(randKeywords.size() < 2) {
                                int r = rand.nextInt(keywords.size());
                                Log.d("랜덤으로 키워드 인덱스 뽑히는 중 --- ", String.valueOf(r));
                                if(!randKeywords.contains(r) && r != keywords.indexOf(answer)) {
                                    randKeywords.add(r);
                                }
                            }

                            ArrayList<String> choiceKeywords = new ArrayList<>();
                            choiceKeywords.add(answer);
                            for(int i = 0; i < 2; i++) {
                                choiceKeywords.add(keywords.get(randKeywords.get(i)));
                            }

                            ArrayList<Integer> randNum = new ArrayList<>();
                            while(randNum.size() < 3) {
                                int r = rand.nextInt(3);
                                Log.d("랜덤으로 뽑히는 중 --- ", String.valueOf(r));
                                if(!randNum.contains(r)) {
                                    randNum.add(r);
                                }
                            }

                            // 객관식 버튼에 지정하기
                            for(int i = 0; i < 3; i++) {
                                btn_choices[i].setText(choiceKeywords.get(randNum.get(i)));
                                Log.d("랜덤수", String.valueOf(randNum.get(i)));
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

                return params;
            }
        };
        requestQueue.add(request);
    }

    public void updateSucc(String mem_id, int mission_id){
        String url = ip + "/Mission/UpdateSucc";
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mem_id", mem_id);
                params.put("mission_id", String.valueOf(mission_id));

                return params;
            }
        };
        requestQueue.add(request);
    }

    public void succPop() {
        builder = new AlertDialog.Builder(mContext, R.style.CustomDialog);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialoglayout = inflater.inflate(R.layout.badge_popup, null);
        builder.setView(dialoglayout);

        Button dialogButton = dialoglayout.findViewById(R.id.btn_close_badgepopup);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.dismiss();
                Intent intent = new Intent(mContext, MyMissionActivity.class);
                mContext.startActivity(intent);
            }
        });
        ad = builder.create();
        ad.show();
    }


}
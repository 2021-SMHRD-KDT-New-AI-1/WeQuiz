package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MissionListActivity extends AppCompatActivity {

    static boolean click_r;
    // 플로팅버튼 상태
    private boolean fabMain_status = false;

    FloatingActionButton float_home, float_map, float_plus, float_plus2, float_mission, float_badge, float_my;
    ExtendedFloatingActionButton mAddFab;
    TextView float_mission_text;
    ImageView transparent;

    int num;


    // 사진 문제 카메라 접근
    Button btn_camera;
    // 객관식, 주관식 문제 이동 버튼
    Button btn_choice, btn_short;
    // intent로 액티비티 간 이동하기 위한 상수
    public static final int sub = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_list);

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

        // 홈 플로팅 버튼 클릭

        // 카메라 미션 페이지로 이동
        btn_camera = findViewById(R.id.btn_camera); /*페이지 전환버튼*/
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivityForResult(intent, sub);//액티비티 띄우기
            }
        });

        // 객관식 미션 페이지로 이동
        btn_choice = findViewById(R.id.btn_choice);/*페이지 전환버튼*/
        btn_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChoiceAnswerActivity.class);
                startActivityForResult(intent, sub);//액티비티 띄우기
            }
        });

        // 주관식 미션 페이지로 이동
        btn_short = findViewById(R.id.btn_short);/*페이지 전환버튼*/
        btn_short.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShortAnswerActivity.class);
                startActivityForResult(intent, sub);//액티비티 띄우기
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

}


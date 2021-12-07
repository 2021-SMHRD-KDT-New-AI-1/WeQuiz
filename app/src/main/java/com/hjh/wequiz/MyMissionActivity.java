package com.hjh.wequiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class MyMissionActivity extends AppCompatActivity {

    RecyclerView rv_myMission;
    // 별이 세 개가 되면 뜰 뱃지 팝업 가버튼 변수 -> 나중에 삭제
    Button btn_gj, btn_mp;
    // 뱃지 이미지를 담아놓을 배열
    int[] imgs;
    AlertDialog.Builder builder;
    AlertDialog ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mission);

        ArrayList<MyMissionVO> mdata = new ArrayList<>();

        // 미션 데이터 입력
        mdata.add(new MyMissionVO(1, "광주", 2));
        mdata.add(new MyMissionVO(2, "곡성", 1));
        mdata.add(new MyMissionVO(3, "목포", 1));
        mdata.add(new MyMissionVO(4, "순천", 0));
        mdata.add(new MyMissionVO(5, "여수", 3));
        mdata.add(new MyMissionVO(5, "여수", 3));
        mdata.add(new MyMissionVO(5, "여수", 3));
        mdata.add(new MyMissionVO(5, "여수", 3));
        mdata.add(new MyMissionVO(5, "여수", 3));

        rv_myMission = findViewById(R.id.rv_myMission);
        rv_myMission.setLayoutManager(new LinearLayoutManager(this));

        MyMissionAdapter adapter = new MyMissionAdapter(mdata);
        rv_myMission.setAdapter(adapter);

        // 별이 세 개가 되면 뜰 뱃지 팝업 가버튼 코드 -> 나중에 삭제
        // 이미지를 담아놓는 곳
        imgs = new int[]{R.drawable.badge_gwangju, R.drawable.badge_mokpo, R.drawable.badge_koksung};

        btn_gj = findViewById(R.id.btn_gj); /*-광주일 경우-*/
        btn_gj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder = new AlertDialog.Builder(MyMissionActivity.this, R.style.CustomDialog);

                View dialoglayout = getLayoutInflater().inflate(R.layout.badge_popup, null);
                builder.setView(dialoglayout);

                ImageView image_badge = dialoglayout.findViewById(R.id.img_badge);
                image_badge.setImageResource(imgs[0]);

                Button dialogButton = dialoglayout.findViewById(R.id.btn_close_badgepopup);

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                    }
                });
                ad = builder.create();
                ad.show();
            }
        });

        btn_mp = findViewById(R.id.btn_mp); /*-목포일 경우-*/
        btn_mp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder = new AlertDialog.Builder(MyMissionActivity.this, R.style.CustomDialog);

                View dialoglayout = getLayoutInflater().inflate(R.layout.badge_popup, null);
                builder.setView(dialoglayout);

                ImageView image_badge = dialoglayout.findViewById(R.id.img_badge);
                image_badge.setImageResource(imgs[1]);

                Button dialogButton = dialoglayout.findViewById(R.id.btn_close_badgepopup);

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                    }
                });
                ad = builder.create();
                ad.show();
            }
        });




    }
}
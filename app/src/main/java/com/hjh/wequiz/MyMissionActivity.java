package com.hjh.wequiz;

import androidx.appcompat.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyMissionActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    Context mContext;

    ArrayList<MyMissionVO> mData;
    MyMissionAdapter adapter;

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

        mData = new ArrayList<>();

        mContext = this;

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        rv_myMission = findViewById(R.id.rv_myMission);
        rv_myMission.setLayoutManager(new LinearLayoutManager(this));

        String mem_id = PreferenceManager.getString(mContext, "mem_id");
        getMyMission(mem_id);

        adapter = new MyMissionAdapter(mData);
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

    public void getMyMission(String id) {
        String url = "http://172.30.1.34:3003/Mission/MyMission";
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
                                String location_name = jsonObject.getString("location_name");
                                String star = jsonObject.getString("star");
                                String badge = jsonObject.getString("badge");

                                MyMissionVO myMission = new MyMissionVO(badge, location_name, Integer.parseInt(star));
                                mData.add(myMission);
                                adapter.notifyDataSetChanged();

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



}
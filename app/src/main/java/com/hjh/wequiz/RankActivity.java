package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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

public class RankActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    Context mContext;
    ArrayList<RankVO> mData;

    RankAdapter adapter;

    RecyclerView rv_Rank;
    ImageView[] img_rankProfiles;
    TextView[] tv_rankNicks, tv_rankBadges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        mContext = this;

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
    }

    public void getRankInfo() {
        String url = "http://172.30.1.34:3003/Badge/RankInfo";
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
    
    
}
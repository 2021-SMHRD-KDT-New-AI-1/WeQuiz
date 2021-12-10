package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
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

public class BadgeMapActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    Context mContext;

    ArrayList<String> myBadges;

    TextView tv_jeollanamdo1, tv_jeollanamdo2, tv_jeollanbukdo1, tv_jeollanbukdo2;
    ImageView handle;
    LinearLayout linear;
    LayoutInflater inflater;

    ImageView badge_gwangju;

    // 뱃지 이름을 가지고 색을 변화시키자
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

}
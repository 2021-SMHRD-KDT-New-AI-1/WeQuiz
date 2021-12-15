package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String ip = "http://4603-210-223-239-152.ngrok.io";

    RequestQueue requestQueue;
    Context mContext;
    ArrayList<RankVO> mData;

    MainRankAdapter adapter;

    ImageView img_mainProfile, img_mainLocationRe;
    TextView tv_mainNick, tv_mainLocation;
    Button btn_mainLogin, btn_mainBadgeList, btn_mainMissonStart;
    RecyclerView rv_mainRank;

    static boolean click_r;
    // 플로팅버튼 상태
    private boolean fabMain_status = false;

    FloatingActionButton float_home, float_map, float_plus, float_plus2, float_mission, float_badge, float_my;
    TextView float_mission_text;
    ImageView transparent;

    int num;

    Location location;
    private static final String TAG = "MainActivity";

    String myLocation;

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


        // 현재위치 리셋
        ImageView img_mainLocationRe = findViewById(R.id.img_mainLocationRe);
        tv_mainLocation = findViewById(R.id.tv_mainLocation);
        img_mainLocationRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    return;
                }
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                //초기 좌표 값 기록
                String provider = location.getProvider();
                double lat = location.getLatitude(); //위도
                double lng = location.getLongitude(); //경도
                double alti = location.getAltitude(); //고도
                Log.d(TAG, "현재 위치: " + provider + " / " + lat + " / " + lng + " / " + alti);
                myLocation = getAddress(MainActivity.this, lat,lng);
                tv_mainLocation.setText(myLocation);
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
                    Animation anima = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
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
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "홈 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });
        //플로팅 미션지도 버튼 클릭
        float_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "미션지도 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        //플로팅 마이미션 버튼 클릭
        float_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(MainActivity.this, MyMissionActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "마이미션 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        //플로팅 전국뱃지지도 버튼 클릭
        float_badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(MainActivity.this, BadgeMapActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "전국뱃지지도 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        //플로팅 내 정보 수정 버튼 클릭
        float_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_r = true;
                Intent intent = new Intent(MainActivity.this, MemberModifyActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "내정보수정 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });







        // 메인 랭킹 표시하는 코드
        mData = new ArrayList<>();

        rv_mainRank = findViewById(R.id.rv_mainRank);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this); // 가로로 리싸이클러 뷰 전환하는 코드
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_mainRank.setLayoutManager(mLayoutManager);

        getRankInfo();

        adapter = new MainRankAdapter(mData);
        rv_mainRank.setAdapter(adapter);
        // 여기까지 메인 랭킹 표시하는 코드 //


        // 회원 정보 가져와서 상단의 환영 문구에 반영하는 코드
        String mem_id = PreferenceManager.getString(this, "mem_id");
        // 여기까지 환영 문구 변경 코드

        // 로그인인 상태일 때 버튼 이벤트 변화
        if(mem_id.equals("")) {
            btn_mainLogin.setText("로그인");
        } else {
            getMemberInfo(mem_id);
            btn_mainLogin.setText("로그아웃");
        }


        // 버튼 이벤트
        btn_mainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_mainLogin.getText().toString().equals("로그아웃")) {
                    PreferenceManager.setString(mContext,"mem_id","");
                }
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
//                intent.putExtra("myLocation", myLocation);
                startActivity(intent);
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

    // 회원 정보를 서버에 요청하여 받아오는 메소드~
    public void getMemberInfo(String id){
        String url = ip + "/Member/MemberInfo";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 응답 성공!
                        try {
                            Log.v("abcd", response);
                            JSONObject jsonObject = (JSONObject) (new JSONArray(response)).get(0);
                            String nick = jsonObject.getString("nick"); // 닉네임 받아오기
                            String welcome = "안녕하세요! " + nick + "님";
                            tv_mainNick.setText(welcome);

                            String img = jsonObject.getString("mem_image"); // 기존 프로필이미지 가져오기
                            Bitmap bitmap = StringToBitmap(img);
                            img_mainProfile.setImageBitmap(bitmap);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("mem_id", id);

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

    // 현재위치 주소 표시 메소드
    public static String getAddress(Context mContext, double lat, double lng) {
        String nowAddr ="현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        String[] nowAddr_list;
        String data = null;
        try {
            if (geocoder != null) {
                // 한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받고
                // 세번째 파라메터인 maxResults는 리턴받을 주소의 최대 갯수를 지정함
                // (여기서는 1개만 받는걸로...)
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0)
                {
                    // 주소 받아오기
                    nowAddr = address.get(0).getAddressLine(0).toString();
                    // 주소 split
                    nowAddr_list = nowAddr.split("\\s");
                    data = nowAddr_list[1];

                }
            }

        }
        catch (IOException e) {
            Toast.makeText(mContext, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return data;
    }

}
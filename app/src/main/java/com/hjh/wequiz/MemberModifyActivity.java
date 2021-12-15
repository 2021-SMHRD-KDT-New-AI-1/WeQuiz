package com.hjh.wequiz;

import static android.util.Base64.encodeToString;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MemberModifyActivity extends AppCompatActivity {

    String ip = "http://4603-210-223-239-152.ngrok.io";

    RequestQueue requestQueue;
    EditText et_memberModifyNick, et_memberModifyPw, et_memberModifyChangePw,et_memberModifyCheckChangePw;
    Button btn_memberModify;
    TextView tv_memberModifyNick;
    ImageView img_changeProfile, img_profile;
    Bitmap image;
    // '생년월일'
    private TextView tv_memberModifyBirthDate;
    private DatePickerDialog.OnDateSetListener callbackMethod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_modify);
        tv_memberModifyBirthDate = findViewById(R.id.tv_memberModifyBirthDate);
        tv_memberModifyNick = findViewById(R.id.tv_memberModifyNick);
        et_memberModifyNick = findViewById(R.id.et_memberModifyNick);
        et_memberModifyPw = findViewById(R.id.et_memberModifyPw);
        et_memberModifyChangePw = findViewById(R.id.et_memberModifyChangePw);
        et_memberModifyCheckChangePw = findViewById(R.id.et_memberModifyCheckChangePw);
        img_changeProfile = findViewById(R.id.img_changeProfile);
        img_profile = findViewById(R.id.img_profile);

        btn_memberModify = findViewById(R.id.btn_memberModify);

        // 서버와 통신!
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        // 현재 로그인된 사용자의 아이디를 가져옴
        String mem_id = PreferenceManager.getString(this,"mem_id");
        getMemberInfo(mem_id);

        // '수정하기' 버튼 클릭 리스너! ( 수정 완료 후 메인페이지로 이동)
        btn_memberModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nick = et_memberModifyNick.getText().toString();
                String currentPw = et_memberModifyPw.getText().toString();
                String changePw = et_memberModifyChangePw.getText().toString();
                String birth = tv_memberModifyBirthDate.getText().toString();

                // 입력값 검사 부분
                boolean checkPw = changePw.equals(et_memberModifyCheckChangePw.getText().toString());
                boolean checkDuplicateNick = true;
                boolean checkBlank = true;

                if (!checkPw){
                    // 변경할 비밀번호와 재확인 비밀번호의 값이 다른 경우
                    Toast.makeText(MemberModifyActivity.this,
                            "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }else if (!checkDuplicateNick){
                    // 닉네임 중복검사
                    Toast.makeText(MemberModifyActivity.this,
                            "닉네임 중복검사를 실시해주세요.",Toast.LENGTH_SHORT).show();
                }else if (!checkBlank){
                    // 공란 검사
                    Toast.makeText(MemberModifyActivity.this,
                            "모든 입력란에 입력을 완료해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    // id와 입력받은 값을 매개변수로 하여 modify 메소드 호출
                    modify(mem_id, currentPw, changePw, nick, birth);
                }
            }
        });
        this.InitializeView(); // '생년월일'
        this.InitializeListener(); // '생년월일'

        // 탈퇴하기~누르기~~~~?


        // 프로필사진 밑 새로고침 이미지 클릭 시 폰 갤러리 열기
        img_changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });

    }

    // 선택한 사진으로 프사바꾸기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        img_profile = findViewById(R.id.img_profile);

        if (requestCode == 1){
            if(resultCode == RESULT_OK){
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    image = BitmapFactory.decodeStream(in);
                    in.close();

                    img_profile.setImageBitmap(image);
                    Toast.makeText(MemberModifyActivity.this,
                            "프로필 사진이 변경되었습니다", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    // 선택한 사진(비트맵형식)을 String형태로 변환하기
    public static String BitmapToString(Bitmap bitmap){
        if (bitmap == null){
            return "디폴트 이미지";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,70,baos);
        byte[] bytes = baos.toByteArray();
        String bitString = encodeToString(bytes, Base64.DEFAULT);
        return bitString;
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



    // Json 파일을 만들어 웹 서버로 보내기~!
    public void postModify(String id, String nick, String pw, String birth, Bitmap image){
        String url = ip + "/Member/Modify";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) (new JSONArray(response).get(0));
                            Log.d("status : ", jsonObject.getString("status"));
                            Toast.makeText(MemberModifyActivity.this,
                                    "회원정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MemberModifyActivity.this, MainActivity.class);
                            startActivity(intent);
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
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("nick",nick);
                params.put("pw",pw);
                params.put("birth",birth);
                params.put("mem_image", BitmapToString(image));

                return params;
            }
        };
        requestQueue.add(request);
    }

    // 회원삭제 웹 서버 보내기~?

    public void modify(String id, String currentPw, String changePw, String nick, String birth){
        // 입력한 현재 비밀번호가 일치하는지 확인, id와 매치되는 비밀번호인지 로그인 요청으로 확인가능하다.
        String url = ip + "/Member/Login"; //(로그인 요청! - 회원테이블에서 확인)
        // 로그인 요청 후 -> 로그인 여부에 따라 status에 success 혹은 fail을 담아서 전달
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) (new JSONArray(response).get(0));
                            Log.d("json ???", jsonObject.toString());
                            Log.d("result : ", jsonObject.getString("result"));
                            String status = jsonObject.getString("result");

                            BitmapDrawable bitmapDrawable = (BitmapDrawable) img_profile.getDrawable();
                            Bitmap bitmap = bitmapDrawable.getBitmap();

                            if (status.equals("success")) {
                                // 로그인 성공 (비밀번호가 일치)
                                postModify(id, nick, changePw, birth, bitmap); // postModify 메소드 호출
                            } else {
                                // 로그인 실패 (비밀번호 불일치)
                                Toast.makeText(MemberModifyActivity.this, "현재 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
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
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("pw",currentPw);

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
                            tv_memberModifyNick.setText(nick);

                            String birth = jsonObject.getString("birth"); // 생년월일 받아오기
                            String birthdate = birth.substring(0, 10); // 받아온 날짜 중 년월일만 표시!
                            tv_memberModifyBirthDate.setText(birthdate);

                            String img = jsonObject.getString("mem_image"); // 기존 프로필이미지 가져오기
                            Bitmap bitmap = StringToBitmap(img);
                            img_profile.setImageBitmap(bitmap);

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





    // '생년월일'
    private void InitializeView() {
        tv_memberModifyBirthDate = (TextView) findViewById(R.id.tv_memberModifyBirthDate);
    }

    private void InitializeListener() {
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear
                    , int dayOfMonth) {
                tv_memberModifyBirthDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        };
    }
    public void OnClickHandler(View view) {
        DatePickerDialog dialog = new DatePickerDialog
                (this, callbackMethod, 1990, 7, 1);
        dialog.show();
    }


}
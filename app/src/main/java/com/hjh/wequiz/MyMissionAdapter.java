package com.hjh.wequiz;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

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

public class MyMissionAdapter extends RecyclerView.Adapter<MyMissionAdapter.ViewHolder> {

    String ip = "http://4603-210-223-239-152.ngrok.io";

    private ArrayList<MyMissionVO> mdata = null;
    Context mContext;
    RequestQueue requestQueue;

    // 팝업창
    AlertDialog.Builder builder;
    AlertDialog ad;

    public MyMissionAdapter(ArrayList mdata) {
        this.mdata = mdata;
    }

    // 아이템 뷰를 위한 뷰홀더 객체 생성
    @NonNull
    @Override
    public MyMissionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.my_mission_list, parent, false);
        MyMissionAdapter.ViewHolder vh = new MyMissionAdapter.ViewHolder(view);

        return vh;
    }

    // 데이터를 뷰홀더의 아이템 뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull MyMissionAdapter.ViewHolder holder, int position) {
        String badge = mdata.get(position).getBadge();
        String location = mdata.get(position).getLocation();
        int star = mdata.get(position).getStar();

        int badgeResId = mContext.getResources().getIdentifier(badge, "drawable", mContext.getPackageName());
        holder.img_myMissionBadge.setImageResource(badgeResId);
        holder.tv_myMissionLocation.setText(location);
        for(int i = 0; i < 3; i++) {
            if(i < star) {
                holder.img_stars[i].setImageResource(R.drawable.star);
            } else {
                holder.img_stars[i].setImageResource(R.drawable.star_empty);
            }

        }

    }

    // 전체 데이터의 갯수
    @Override
    public int getItemCount() {
        return mdata.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_myMissionBox,img_myMissionBadge;
        ImageView[] img_stars;
        TextView tv_myMissionLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if (requestQueue == null){
                requestQueue = Volley.newRequestQueue(mContext);
            }

            // 뷰 객체 초기화
            img_myMissionBox = itemView.findViewById(R.id.img_myMissionBox);
            img_myMissionBadge = itemView.findViewById(R.id.img_myMissionBadge);
            img_stars = new ImageView[3];
            for (int i = 0; i < img_stars.length; i++) {
                int resID = itemView.getResources().getIdentifier("img_star" + (i + 1), "id", mContext.getPackageName());
                img_stars[i] = itemView.findViewById(resID);
            }
            tv_myMissionLocation = itemView.findViewById(R.id.tv_myMissionLocation);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        MyMissionVO item = mdata.get(pos);
                        Toast.makeText(mContext, item.getLocation(), Toast.LENGTH_SHORT).show();

                        String location_name = item.getLocation();
                        // 별이 3개 미만
                        // 지역정보를 가지고 문제 목록 페이지로 이동
                        if(item.getStar() < 3) {
                            Intent intent = new Intent(mContext, MissionListActivity.class);
                            intent.putExtra("location_name", location_name);
                            mContext.startActivity(intent);
                        } else {
                            String mem_id = PreferenceManager.getString(mContext, "mem_id");
                            postBadgeGet(mem_id, location_name, item);
                            deleteMyMission(mem_id, location_name, pos);
                        }

                    }
                }
            });
        }
    }

    public void postBadgeGet(String mem_id, String location_name, MyMissionVO item) {

        String url = ip + "/Badge/Insert";
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

                            builder = new AlertDialog.Builder(mContext, R.style.CustomDialog);

                            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View dialoglayout = inflater.inflate(R.layout.badge_popup, null);
                            builder.setView(dialoglayout);

                            ImageView image_badge = dialoglayout.findViewById(R.id.img_badge);
                            int badgeResId = mContext.getResources().getIdentifier(item.getBadge(), "drawable", mContext.getPackageName());
                            image_badge.setImageResource(badgeResId);

                            Button dialogButton = dialoglayout.findViewById(R.id.btn_close_badgepopup);

                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ad.dismiss();
                                }
                            });
                            ad = builder.create();
                            ad.show();

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
                params.put("location_name", location_name);

                return params;
            }
        };
        requestQueue.add(request);
    }

    public void deleteMyMission(String mem_id, String location_name, int pos) {

        mdata.remove(pos);
        notifyDataSetChanged();

        String url = ip + "/Mission/Delete";
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
                params.put("location_name", location_name);

                return params;
            }
        };
        requestQueue.add(request);

    }

}

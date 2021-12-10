package com.hjh.wequiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class MyMissionAdapter extends RecyclerView.Adapter<MyMissionAdapter.ViewHolder> {

    private ArrayList<MyMissionVO> mdata = null;
    Context mContext;

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
        for(int i = 0; i < star; i++) {
            holder.img_stars[i].setImageResource(R.drawable.star);
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
                    }
                }
            });
        }
    }
}

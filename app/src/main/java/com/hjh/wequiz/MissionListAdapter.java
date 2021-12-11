package com.hjh.wequiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MissionListAdapter extends RecyclerView.Adapter<MissionListAdapter.ViewHolder> {

    private ArrayList<MissionListVO> mdata = null;
    Context mContext;

    public MissionListAdapter(ArrayList mdata) {
        this.mdata = mdata;

    }

    // onCreateViewHolder : 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public MissionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.mission_list, parent, false);
        MissionListAdapter.ViewHolder vh = new MissionListAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder : position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        int mission_id = mdata.get(position).getMission_id();

        String location_name = mdata.get(position).getLocation_name();
        String keyword = mdata.get(position).getKeyword();
        String quiz = mdata.get(position).getQuiz();
        String answer = mdata.get(position).getAnswer();
        String mission_type = mdata.get(position).getMission_type();

        holder.img_missionList_mission.setImageResource(R.drawable.button_ju);
        holder.tv_missionList_location.setText(location_name);


    }

    // getItemCount : 전체 데이터의 개수를 리턴
    @Override
    public int getItemCount() {
        return mdata.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_missionList_box, img_missionList_mission;
        TextView tv_missionList_location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰 객체 초기화
            img_missionList_box = itemView.findViewById(R.id.img_missionList_box);
            img_missionList_mission = itemView.findViewById(R.id.img_missionList_mission);
            tv_missionList_location = itemView.findViewById(R.id.tv_missionList_location);

        }

    }


}



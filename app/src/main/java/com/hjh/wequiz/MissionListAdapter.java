package com.hjh.wequiz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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

        if(mission_type.equals("주관식")) {
            holder.img_missionListType.setImageResource(R.drawable.button_ju);
        } else if(mission_type.equals("객관식")) {
            holder.img_missionListType.setImageResource(R.drawable.button_gack);
        } else {
            holder.img_missionListType.setImageResource(R.drawable.button_camera);
        }
        holder.tv_missionListKeyword.setText(keyword);

    }

    // getItemCount : 전체 데이터의 개수를 리턴
    @Override
    public int getItemCount() {
        return mdata.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_missionList_box, img_missionListType;
        TextView tv_missionListKeyword;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰 객체 초기화
            img_missionList_box = itemView.findViewById(R.id.img_missionList_box);
            img_missionListType = itemView.findViewById(R.id.img_missionListType);
            tv_missionListKeyword = itemView.findViewById(R.id.tv_missionListKeyword);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        MissionListVO item = mdata.get(pos);

                        int mission_id = item.getMission_id();
                        String location_name = item.getLocation_name();
                        String quiz = item.getQuiz();
                        String answer = item.getAnswer();
                        String type = item.getMission_type();

                        Intent intent = null;

                        if(type.equals("주관식")) {
                            intent = new Intent(mContext, ShortAnswerActivity.class);

                        } else if (type.equals("객관식")) {
                            intent = new Intent(mContext, ChoiceAnswerActivity.class);

                        } else {

                        }

                        intent.putExtra("mission_id", mission_id);
                        intent.putExtra("location_name", location_name);
                        intent.putExtra("quiz", quiz);
                        intent.putExtra("answer", answer);
                        mContext.startActivity(intent);


                    }
                }
            });

        }

    }


}



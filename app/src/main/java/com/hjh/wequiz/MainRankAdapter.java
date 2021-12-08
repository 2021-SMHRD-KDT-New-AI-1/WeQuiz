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
import java.util.HashMap;

public class MainRankAdapter extends RecyclerView.Adapter<MainRankAdapter.ViewHolder>  {

    private ArrayList<MainRankVO> mdata = null;
    Context mContext;

    public MainRankAdapter(ArrayList mdata) {
        this.mdata = mdata;

    }

    // onCreateViewHolder : 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public MainRankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.rank_topfive_list, parent, false);
        MainRankAdapter.ViewHolder vh = new MainRankAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder : position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int main_rank = mdata.get(position).getMainRank();
        String main_nick = mdata.get(position).getMainNick();
        String main_profile = mdata.get(position).getMainProfile();
        String main_medal = mdata.get(position).getMainMedal();
        int medal_num = mdata.get(position).getMedalNum();

        holder.tv_main_rank_num.setText(String.valueOf(main_rank));
        holder.tv_main_rank_nick.setText(main_nick);
        holder.tv_main_rank_badge.setText(String.valueOf(medal_num));

    }

    // getItemCount : 전체 데이터의 개수를 리턴
    @Override
    public int getItemCount() {
        return mdata.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_main_rank_box, img_main_rank_profile,img_main_rank_medal;
        TextView tv_main_rank_num, tv_main_rank_badge, tv_main_rank_nick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰 객체 초기화
            img_main_rank_box = itemView.findViewById(R.id.img_main_rank_box);
            img_main_rank_profile = itemView.findViewById(R.id.img_main_rank_profile);
            img_main_rank_medal = itemView.findViewById(R.id.img_main_rank_medal);
            tv_main_rank_num = itemView.findViewById(R.id.tv_main_rank_num);
            tv_main_rank_badge = itemView.findViewById(R.id.tv_main_rank_badge);
            tv_main_rank_nick = itemView.findViewById(R.id.tv_main_rank_nick);


        }

    }
}


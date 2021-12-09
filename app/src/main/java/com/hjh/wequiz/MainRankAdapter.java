package com.hjh.wequiz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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

    private ArrayList<RankVO> mdata = null;
    Context mContext;

    public MainRankAdapter(ArrayList<RankVO> mdata) {
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

        int rank = mdata.get(position).getRank();
        String nick = mdata.get(position).getNick();
        int medal = mdata.get(position).getMedal();
        String mem_image = mdata.get(position).getProfile();

        holder.tv_main_rank_num.setText(String.valueOf(rank));
        holder.tv_main_rank_nick.setText(nick);
        holder.tv_main_rank_badge.setText(String.valueOf(medal));
        holder.img_main_rank_profile.setImageBitmap(StringToBitmap(mem_image));

        if(position == 0) {
            holder.img_main_rank_medal.setImageResource(R.drawable.main_gold);
        } else if(position == 1){
            holder.img_main_rank_medal.setImageResource(R.drawable.main_silver);
        } else if(position == 2){
            holder.img_main_rank_medal.setImageResource(R.drawable.main_bronze);
        }

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


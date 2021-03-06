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

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder>  {

    private ArrayList<RankVO> mdata = null;
    Context mContext;

    public RankAdapter(ArrayList mdata) {
        this.mdata = mdata;

    }

    // onCreateViewHolder : 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public RankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.rank_list, parent, false);
        RankAdapter.ViewHolder vh = new RankAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder : position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int rank = mdata.get(position).getRank();
        String nick = mdata.get(position).getNick();
        int medal = mdata.get(position).getMedal();
        String mem_image = mdata.get(position).getProfile();

        Bitmap mem_image_bitmap = StringToBitmap(mem_image);

        holder.tv_rank_num.setText(String.valueOf(rank));
        holder.tv_rank_nick.setText(nick);
        holder.tv_rank_medal.setText(String.valueOf(medal));
        holder.img_rank_profile.setImageBitmap(mem_image_bitmap);

    }

    // getItemCount : 전체 데이터의 개수를 리턴
    @Override
    public int getItemCount() {
        return mdata.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_rank_num, tv_rank_nick, tv_rank_medal;
        ImageView img_rank_rankBox, img_rank_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰 객체 초기화
            tv_rank_num = itemView.findViewById(R.id.tv_rank_num);
            tv_rank_nick = itemView.findViewById(R.id.tv_rank_nick);
            tv_rank_medal = itemView.findViewById(R.id.tv_rank_medal);
            img_rank_rankBox = itemView.findViewById(R.id.img_rank_rankBox);
            img_rank_profile = itemView.findViewById(R.id.img_rank_profile);

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


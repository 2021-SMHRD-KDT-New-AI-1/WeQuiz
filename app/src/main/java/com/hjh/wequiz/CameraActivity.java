package com.hjh.wequiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    Button btn, btn_album, btn_close_starpopup;
    AlertDialog.Builder builder;
    AlertDialog ad;
    ImageView img_select;
    Bitmap bmp_img;

    int REQUEST_IMAGE_CODE = 1001;
    int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1002;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btn = findViewById(R.id.btn);
        btn_close_starpopup = findViewById(R.id.btn_close_starpopup);
        btn_album = findViewById(R.id.btn_album);
        img_select = findViewById(R.id.img_select);

        // 미션 획득 시 팝업창이 뜨도록 하는 가버튼 코드 -> 나중에 삭제
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                builder = new AlertDialog.Builder(CameraActivity.this, R.style.CustomDialog);

                View dialoglayout = getLayoutInflater().inflate(R.layout.star_popup, null);
                builder.setView(dialoglayout);

                Button dialogButton = dialoglayout.findViewById(R.id.btn_close_starpopup);

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                    }
                });

                ad = builder.create();
                ad.show();
            }
        });

        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 앨범에서 사진 선택
                btn_album = findViewById(R.id.btn_album);
                btn_album.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(in, REQUEST_IMAGE_CODE);

                    }
                });
            }

        });




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE) {
            Uri image = data.getData();
            try {
                // 앨범에서 가져온 사진으로 이미지뷰셋팅.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
                img_select.setImageBitmap(bitmap);

                // bmp_img 에 앨범에서 선택한 이미지 넣기.
                BitmapDrawable drawable = (BitmapDrawable) img_select.getDrawable();
                bmp_img = drawable.getBitmap();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
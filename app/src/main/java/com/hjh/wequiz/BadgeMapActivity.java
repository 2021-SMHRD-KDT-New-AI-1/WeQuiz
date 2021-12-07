package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

public class BadgeMapActivity extends AppCompatActivity {

    TextView tv_jeollanamdo1, tv_jeollanamdo2, tv_jeollanbukdo1, tv_jeollanbukdo2;
    ImageView handle;
    LinearLayout linear;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge_map);

        tv_jeollanamdo1 = (TextView) findViewById(R.id.tv_jeollanamdo1);
        tv_jeollanamdo2 = (TextView) findViewById(R.id.tv_jeollanamdo2);
        tv_jeollanbukdo1 = findViewById(R.id.tv_jeollanbukdo1);
        tv_jeollanbukdo2 = findViewById(R.id.tv_jeollanbukdo2);

        handle = (ImageView) findViewById(R.id.handle);

        SlidingDrawer drawer = (SlidingDrawer)findViewById(R.id.slide);
        linear = findViewById(R.id.content);
        inflater = getLayoutInflater();

        tv_jeollanamdo1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout(R.layout.bc_jeollanamdo);

                drawer.animateClose();
            }
        });

        tv_jeollanamdo2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout(R.layout.bc_jeollanamdo);
                drawer.animateClose();
            }
        });

        tv_jeollanbukdo1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayout(R.layout.bc_jeollabokdo);
                drawer.animateClose();
            }
        });

        tv_jeollanbukdo2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayout(R.layout.bc_jeollabokdo);
                drawer.animateClose();
            }
        });


        }

        public void changeLayout(int layout){
            linear.removeAllViews();
            inflater.inflate(layout, linear, true);
        }

}
/*
package com.hjh.wequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import net.daum.mf.map.api.MapView;


public class MapActivity<mapView> extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey("4427e253f430ade00212f26da52ad0ad");
        RelativeLayout container = (RelativeLayout) findViewById(R.id.map_view);
        container.addView(mapView);

    }
}*/

package com.hjh.wequiz;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import net.daum.mf.map.api.MapView;


public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapView mapView = new MapView(this);
//        mapView.setDaumMapApiKey("pQ48XE8QNd0N2T7rG+1KyRbsmkE=");

        ConstraintLayout mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
    }

}
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

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import com.google.common.collect.MapMaker;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";

    //생성자
    MapView mapView;
    TextView tvCurrentPosition;
    ConstraintLayout mapViewContainer;
    MapPoint mapPoint;
    MapPOIItem marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // 카카오맵 지도 띄우기
        mapView = new MapView(this); // 지도 담은 변수
        mapViewContainer = findViewById(R.id.map_view); //지도를 띄울 view
        mapViewContainer.addView(mapView); // view에 지도 추가하여 띄우기기




        //1. 사용자 위치 관련 permission 여부 및 추가
        //checkselfPermission으로 사용자에게 (context, permission 인자값)안에 있는 permission이 있는지 확인하고
        //가지고 있다면 packageManager.PERMISSION_GRANTED를 리턴, 없다면 packageManager.PERMISSION_DENIED를 리턴
       if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "앱 실행을 위해서는 권한을 설정해야 합니다.", Toast.LENGTH_LONG).show();
               //requestPermissions => 퍼미션 요청
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
               return;
       } //앱 실행시 FINE_LOCATION && COARSE_LOCATION != PackageManager.PERMISSION_GRANTED 라면 메시지 띄운 후



        //2. 위치정보 가져오기
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //위치관리자 객체 생성
        Location lastLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER); //GPS 정보 담기

        //초기 좌표 값 기록
        String provider = lastLocation.getProvider();
        double lat = lastLocation.getLatitude(); //위도
        double lng = lastLocation.getLongitude(); //경도
        double alti = lastLocation.getAltitude(); //고도

        Log.d(TAG, "현재 위치: " + provider + " / " + lat + " / " + lng + " / " + alti);

        tvCurrentPosition = findViewById(R.id.tvCurrentPosition); //현재 위치 주소
        tvCurrentPosition.setText(getAddress(MapActivity.this, lat,lng));


        mapPoint = MapPoint.mapPointWithGeoCoord(lat,lng);
        mapView.setMapCenterPoint(mapPoint, true); //중심점 변경
        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.

        mapViewContainer.addView(mapView);

        marker = new MapPOIItem();
        marker.setItemName("wequiz");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        // 기본으로 제공하는 BluePin 마커 모양.
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(marker);

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,
                1,
                getLocationListener);

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000,
                1,
                getLocationListener);
    }

    final LocationListener getLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            String provider = location.getProvider();
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            double alti = location.getAltitude();

            Log.d(TAG, "바뀐 위치: " + provider + " / "  + lat + " / " + lng + " / " + alti);


            tvCurrentPosition.setText(getAddress(MapActivity.this, lat,lng));
//            tvCurrentPosition.setText("바뀐 위치: " + provider + " / " + lat + " / " + lng + " / " + alti);

            mapPoint = MapPoint.mapPointWithGeoCoord(lat,lng);
            mapView.setMapCenterPoint(mapPoint, true);

            marker.setItemName(getAddress(MapActivity.this, lat,lng));
            marker.setTag(0);
            marker.setMapPoint(mapPoint);
            // 기본으로 제공하는 BluePin 마커 모양.
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
            // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

            //mapView.addPOIItem(marker);
        }
    };

    public static String getAddress(Context mContext, double lat, double lng) {
        String nowAddr ="현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;

        try {
            if (geocoder != null) {
                // 한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받고
                // 세번째 파라메터인 maxResults는 리턴받을 주소의 최대 갯수를 지정함
                // (여기서는 1개만 받는걸로...)
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0)
                {
                    // 주소 받아오기
                    nowAddr = address.get(0).getAddressLine(0).toString();
                }
            }
        }
        catch (IOException e) {
            Toast.makeText(mContext, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return nowAddr;
    }




}
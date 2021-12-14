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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;



public class MapActivity extends AppCompatActivity implements MapView.POIItemEventListener{

    private static final String TAG = "MapActivity";

    //생성자
    MapView mapView;
    TextView tvCurrentPosition; // 현재위치 표시 텍스트뷰
    ConstraintLayout mapViewContainer; // 이 부분 물어보기 !!!
    MapPoint mapPoint;
    MapPOIItem marker;
    MapPOIItem missionMarker1;
    MapPOIItem missionMarker2;
    MapPOIItem missionMarker3;
    Location location;
    MapPoint mission1_location;
    MapPoint mission2_location;
    MapPoint mission3_location;
    ImageView handle;
    LinearLayout linear;
    LayoutInflater inflater;
    SlidingDrawer drawer;
    ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        // 카카오맵 지도 띄우기
        mapView = new MapView(this); // 지도 담은 변수
        mapViewContainer = findViewById(R.id.map_view); //지도를 띄울 view
        mapViewContainer.addView(mapView); // view에 지도 추가하여 띄우기기
        mapView.setPOIItemEventListener(poiItemEventListener); // 마커 클릭이벤트, adapter를 set해주기
        drawer = (SlidingDrawer)findViewById(R.id.slide);

        handle = findViewById(R.id.handle);
        linear = findViewById(R.id.linear);
        inflater = getLayoutInflater();

        /*drawerToggle=new ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };*/
        //drawer



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
        // LocationManager : GPS, Network 에서 위치 정보 가져올 수 있음
        // LocationManager.getLastKnownLocation() : 가장 마지막에 기록된 위치정보
        // LocationManager.requestLocationUpdates() : Listener 를 등록하여 위치가 변경될 때마다 이벤트 받을 수 있음
        // Location_Service : GPS를 통한 위치 서비스를 제공하는 LocationManager
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //위치관리자 객체 생성
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER); //GPS 정보 담기
        //초기 좌표 값 기록
        String provider = location.getProvider();
        double lat = location.getLatitude(); //위도
        double lng = location.getLongitude(); //경도
        double alti = location.getAltitude(); //고도
        Log.d(TAG, "현재 위치: " + provider + " / " + lat + " / " + lng + " / " + alti);
        // map_xml 에서 현재위치 표시 텍스트뷰
        tvCurrentPosition = findViewById(R.id.tvCurrentPosition);
        tvCurrentPosition.setText(getAddress(MapActivity.this, lat,lng));
        //중심점으로 잡을 좌표값 변수
        mapPoint = MapPoint.mapPointWithGeoCoord(lat,lng);
        //중심점 변경 (초기 위치 설정해서 지도 띄우기)
        mapView.setMapCenterPoint(mapPoint, true); //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
        mapView.setZoomLevel(1, true); // 확대 레벨 설정 (값이 작을수록 더 확대)
//        mapViewContainer.addView(mapView); // 이게 문제였음!!!!!!!!!!!! ㅠㅜ 9시간 삽질...
        // 현재위치 마커 표시
        marker = new MapPOIItem();
        marker.setItemName("quiz");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
//        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
//        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        marker.setCustomImageResourceId(R.drawable.badge_yeosu); // 이미지 파일
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);
        // 위치 업데이트를 위한 재요청
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,
                1,
                getLocationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000,
                1,
                getLocationListener);



        // 3. 버튼 클릭시 내 주변 미션 마커
        Button btn_mission = findViewById(R.id.btn_mission);
        // Event Listener
        btn_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클릭시 메시지 뜨는거 확인하고 잘 작동되는지 확인하는 코드
                // mission 위치 변수
                // 추후 위치값 변수로 넣어주기
                // 35.141998628841115 / 126.912268377757 => 사직공원
                mission1_location = MapPoint.mapPointWithGeoCoord(35.14163185026689, 126.93044048953436); // 사직공원
                mission2_location = MapPoint.mapPointWithGeoCoord(35.146934630213075, 126.92030700163693); // 국립아시아문화전당
                mission3_location = MapPoint.mapPointWithGeoCoord(35.145588206467046, 126.90908212678009); // 광주향교

                // 마커생성
                missionMarker1 = new MapPOIItem(); // 마커 생성
                missionMarker2 = new MapPOIItem();
                missionMarker3 = new MapPOIItem();
                missionMarker1.setItemName("mission1"); // 마커 이름
                missionMarker2.setItemName("mission2");
                missionMarker3.setItemName("mission3");
                missionMarker1.setTag(1); // 마커 생성주기
                missionMarker2.setTag(2);
                missionMarker3.setTag(3);
                missionMarker1.setMapPoint(mission1_location); // 위치값 입력
                missionMarker2.setMapPoint(mission2_location);
                missionMarker3.setMapPoint(mission3_location);
                missionMarker1.setMarkerType(MapPOIItem.MarkerType.BluePin); // 마커 디자인, BluePin 기본타입
                missionMarker2.setMarkerType(MapPOIItem.MarkerType.BluePin);
                missionMarker3.setMarkerType(MapPOIItem.MarkerType.BluePin);
                missionMarker1.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                missionMarker2.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                missionMarker3.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                mapView.addPOIItem(missionMarker1); // mapView에 마커 add
                mapView.addPOIItem(missionMarker2);
                mapView.addPOIItem(missionMarker3);


            }
        });


    }


    // LocationListener 인터페이스 : 위치 정보를 위치 공급자로부터 지속적으로 받아오는 역할
    // 위치 정보를 가져올 수 있는 메소드 : 위치 이동이나 시간 경과 등을 통해 호출된다.
    // Location location 파라미터로 실시간 위치 데이터 이용
    final LocationListener getLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            String provider = location.getProvider(); // provider 개념???
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            double alti = location.getAltitude();

            Log.d(TAG, "바뀐 위치: " + provider + " / "  + lat + " / " + lng + " / " + alti);

            tvCurrentPosition.setText(getAddress(MapActivity.this, lat,lng)); //getAddress 로 현재 위치 주소 텍스트로 표시

            mapPoint = MapPoint.mapPointWithGeoCoord(lat,lng);
            mapView.setMapCenterPoint(mapPoint, true);
            marker.setItemName(getAddress(MapActivity.this, lat,lng));
            marker.setTag(0);
            marker.setMapPoint(mapPoint);
            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 기본으로 제공하는 BluePin 마커 모양, customimage는 이미지 넣을 수 있음
            marker.setCustomImageResourceId(R.drawable.badge_yeosu); // 이미지 파일
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            //mapView.addPOIItem(marker); 이건 없어도 되나???

        }

    };

    // 현재위치 주소 표시 메소드
    public static String getAddress(Context mContext, double lat, double lng) {
        String nowAddr ="현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        String[] nowAddr_list;
        String data = null;

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
                    // 주소 split
                    nowAddr_list = nowAddr.split("\\s");
                    data = nowAddr_list[1] + " " + nowAddr_list[2] + " " + nowAddr_list[3] + " " + nowAddr_list[4];
                }
            }
        }
        catch (IOException e) {
            Toast.makeText(mContext, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return data;
    }



    // 4. 미션마커 클릭 시 문제 화면 생성
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
    }
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
    }
    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }


    public MapView.POIItemEventListener poiItemEventListener = new MapView.POIItemEventListener() {
        @Override
        public void onPOIItemSelected(MapView mapView, MapPOIItem MarkerListener) {
            Log.d("아이템 이름", MarkerListener.getItemName()); // 마커 클릭 구분 Log.d

            changeLayout(R.layout.activity_savemission);

            View view = inflater.inflate(R.layout.activity_savemission, linear, true);


            //SlidingDrawer drawer = (SlidingDrawer)findViewById(R.id.slide);
            drawer.animateClose();
            //drawer.setClickable(true);

        }
        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
            Log.i("112222","진입");
        }
        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
            Log.i("13333111","진입");
        }
        @Override
        public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
            Log.i("1144444411","진입");
        }
    };



    private void changeLayout(int savemission) {

    }


}



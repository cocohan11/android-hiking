package com.example.iamhere.copy;

import static com.example.iamhere.L_login.경도;
import static com.example.iamhere.L_login.마커경도리스트;
import static com.example.iamhere.L_login.마커리스트;
import static com.example.iamhere.L_login.마커위도리스트;
import static com.example.iamhere.L_login.위도;

import android.Manifest;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.iamhere.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;

public class Map2_stickodeTest extends AppCompatActivity implements OnMapReadyCallback { // implement꼭해주기(인터페이스구현)


    public String TAG = "Map2_stickodeTest";
    public MapView mapView; //지도 뷰(보이는 것)
    public NaverMap 네이버Map;

    // 마커
    private TextView tv_markerNum; // 마커에 삽입될 view
    private int route123 = 0; // view에 보일 숫자
    private int markerMaxCount = 5;

    public ArrayList<Marker> 마커리스트 = new ArrayList<>();
    public ArrayList<Double> 마커위도리스트 = new ArrayList<>();
    public ArrayList<Double> 마커경도리스트 = new ArrayList<>();


    //ㅡㅡㅡㅡㅡㅡ
    // onCreate()
    //ㅡㅡㅡㅡㅡㅡ
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stickode_test);

        mapView = findViewById(R.id.map_stickode);
        tv_markerNum = (TextView)findViewById(R.id.tv_markerNum); // 마커에 삽입될 숫자뷰


        // 네이버 지도 초기화
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this); // 네이버지도에서 반환되는 콜백함수를 자신(this)으로 지정하는 역할
                                            // Async : 비동기(로 NaverMap객체를 얻는다)


    } //~onCreate()



    // NaverMap객체가 준비되면 콜백메소드를 호출한다.
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) { //지도의 작동을 다룸

        Log.e(TAG, "onMapReady() 입장");
        네이버Map = naverMap; //전역변수에 대입(스레드에서 쓰려고)



        //ㅡㅡㅡㅡㅡㅡㅡ
        // 지도 롱 클릭 : 마커 생성
        //ㅡㅡㅡㅡㅡㅡㅡ
        tv_markerNum.setVisibility(View.GONE); //기존 뷰를 안 숨기니까 뷰가 제멋대로 이동해버림.
        naverMap.setOnMapLongClickListener(new NaverMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull PointF pointF, @NonNull LatLng latLng) {

                double 위도 = latLng.latitude;
                double 경도 = latLng.longitude;

                     Toast.makeText(getApplicationContext()
                    , "lat : "+latLng.latitude+", \nLng : "+latLng.longitude
                    , Toast.LENGTH_SHORT).show(); //퇴장알림을 해야 확신할 듯

                    Log.e(TAG, "위도 : "+위도);
                    Log.e(TAG, "경도 : "+경도);
                    Log.e(TAG, "route123 : "+route123);


                if(route123 < markerMaxCount) { // 최대 n개까지만 마커 생성

                    route123++;
                    Log.e(TAG, "route123++ if문 안 : "+route123);
                    tv_markerNum.setText(String.valueOf(route123)); //원형 마커에 숫자 대입 /int그대로넣으니 에러남

                    //ㅡㅡㅡㅡㅡㅡ
                    // 마커 추가 : 끝수 + 1된 마커 추가
                    //ㅡㅡㅡㅡㅡㅡ
                    Marker marker = new Marker(); //여러개 추가할거라 롱클릭 안에 위치
                    marker.setPosition(new LatLng(위도, 경도)); //먼저(위치에러뜸)
                    marker.setWidth(90);
                    marker.setHeight(90);
                    marker.setMap(네이버Map); //다시 대입한다 /해야 보임
                    marker.setIcon(OverlayImage.fromView(tv_markerNum)); //숫자먼저 set하고 뷰를 삽입한다.

                    마커리스트.add(route123 - 1, marker); // 배열[0]부터하려고 -1함
                    마커위도리스트.add(위도); // 마커추가할 때 같이 위경도도 추가하고 삭제할 때도 동일하게
                    마커경도리스트.add(경도);
                    Log.e(TAG, "마커리스트[].size : "+ 마커리스트.size() +"마커위도리스트size :"+마커위도리스트.size()+"마커경도리스트size: "+마커경도리스트.size());
                    Log.e(TAG, "마커위도리스트[] : "+ 마커위도리스트); //담긴 객체 확인용
                    Log.e(TAG, "마커경도리스트[] : "+ 마커경도리스트);


                } else {
                    Toast.makeText(getApplicationContext(), "최대 경로마커 갯수는 "+markerMaxCount+"개입니다.", Toast.LENGTH_SHORT).show(); //퇴장알림을 해야 확신할 듯
                }
            }
        });
    } //~oMapReady()




    // 다음주에 다이얼로그로 삭제 구현



}
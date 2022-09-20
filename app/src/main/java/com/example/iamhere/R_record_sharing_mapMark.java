package com.example.iamhere;

import static com.example.iamhere.L_login.경도;
import static com.example.iamhere.L_login.위도;
import static com.example.iamhere.M_share_2_Map.retrofit객체;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.iamhere.Interface.Sharing;
import com.example.iamhere.Model.Sharing_room;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class R_record_sharing_mapMark extends AppCompatActivity implements OnMapReadyCallback {


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    // 마커가 찍힌 지도를 펼쳐보이는 곳 : R_record_sharing >> this
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    String TAG = "R_record_sharing_mapMark";
    MapView mapView; //지도 뷰(보이는 것)
    NaverMap 네이버Map;
    TextView routeNum; //마커1개 뷰
    FusedLocationSource locationSource; //런타임권한얻은 현재위치값
    final int LOCATION_PERMISSION_REQUEST_CODE = 1000; //런타임권한요청코드
    final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}; //대략,정확한 위치 권한
    String 방번호;
    ArrayList<Double> dbArr위도 = new ArrayList<Double>();
    ArrayList<Double> dbArr경도 = new ArrayList<Double>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rrecord_sharing_map_mark);
        Log.e(TAG, "onCreate()");
        ID();

        Intent intent = getIntent(); // R_record_sharing 리사이클러뷰 사진요소 >>> (방번호) >>> this
        방번호 = intent.getStringExtra("해당방번호");
        Log.e(TAG, "방번호 : "+방번호);

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 네이버 지도 초기화
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this); //네이버지도에서 반환되는 콜백함수를 자신(this)으로 지정하는 역할
        //Async : 비동기(로 NaverMap객체를 얻는다)
        locationSource = new FusedLocationSource(R_record_sharing_mapMark.this, LOCATION_PERMISSION_REQUEST_CODE); //권한요청객체생성(GPS)



    } //~onCreate()




    @Override //마커 + 나의 현재 위치 오버레이
    public void onMapReady(@NonNull NaverMap naverMap) { //지도의 작동을 다룸

        //시작하면 마커가 찍히고
        //내 위치대신 찍힌 1번마커를 초점맞춘다.

        Log.e(TAG, "onMapReady() 입장");
        네이버Map = naverMap; //전역변수에 대입(스레드에서 쓰려고)


        //런타임 권한
        Log.e(TAG, "런타임 권한을 맵에 지정");
        ActivityCompat.requestPermissions(R_record_sharing_mapMark.this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE); //현재위치 표시할 때 권한 확인


        //현재위치 GPS
        naverMap.setLocationSource(locationSource); //현재위치
        naverMap.addOnLocationChangeListener(location -> //로그 : 위경도
        {
            위도 = location.getLatitude();
            경도 = location.getLongitude();
//            Log.e(TAG, "위도경도 : "+location.getLatitude()+ ", " + location.getLongitude() );
        });
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow); //방향 정신없어서 위치 활성화만
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, true); //레이어 : 등산로
        Log.e(TAG, "LAYER_GROUP_MOUNTAIN 설정");


        //UI 컨트롤을 제어
        UiSettings uiSettings = naverMap.getUiSettings(); //설정객체 선언
        uiSettings.setLocationButtonEnabled(true); //현재위치 보는 방향 설정
        uiSettings.setCompassEnabled(true); //나침반. 이게 최선임. 항상 고정하고싶은데 스르륵 사라져버림
        uiSettings.setScaleBarEnabled(false); //축척바 활성화 안하는게 덜 지저분해 보일 듯(등산어플이랑 비교해봐야지)
        uiSettings.setZoomControlEnabled(false); //+-버튼 없앰


        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 서버로부터 위도경도값 받기
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        retrofit마커받기_지도에삽입(방번호);
        네이버Map = naverMap; //네이버Map 변수에 다시 대입해서 온전하게 만듦


    } //~onMapReady() 지도관련은 다 여기 넣기


    //서버에서 위도경도값들 받기
    private void retrofit마커받기_지도에삽입(String 방번호) { //리사이클러뷰에서 보내온 방번호를 서버에 키값으로 보낸다.


        Sharing retrofit위도경도 = retrofit객체().create(Sharing.class); //retrofit객체는 static메소드로 간편대입
        Call<Sharing_room> call = retrofit위도경도.markerArraylistGet(방번호); //방번호를 보내면 위도경도 배열을 받아오는 기적


        //네트워킹 시도
        call.enqueue(new Callback<Sharing_room>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Sharing_room> call, Response<Sharing_room> response) {

                Log.e(TAG, "retrofit마커받기_지도에삽입() 결과");

                Sharing_room result = response.body();
                String str위도값들 = result.getarrLat(); //["37.4827142710659","37.48222882555662","37.48086743405399"]
                String str경도값들 = result.getarrLng();

                Log.e(TAG, "result " + result); //toString을 가져와버리네?
                Log.e(TAG, "result.getLat() " + result.getarrLat()); //에러 >> 배열을 json으로 바꿔서 담음
                Log.e(TAG, "result.getarrLng() " + result.getarrLng()); //에러 >> 배열을 json으로 바꿔서 담음


                위도경도jsonToArraylist(str위도값들, str경도값들); //스태틱에 위도,경도값 대입함

                if(!str위도값들.equals("[]")) { // 서버에서 이렇게 받아옴

                    Log.e(TAG, "str위도값들 값이 있다 ");
                    마커셋팅(네이버Map); //가져온 위도경도 값으로 마커찍음 //비활성화..되어있겠지?

                } //없으면 마커를 대입하지 않는다.

            }

            @Override
            public void onFailure(Call<Sharing_room> call, Throwable t) {
                Log.e("onFailure... : ", t.getMessage());
            }
        });

    }

    //php에서 받은위도경도 데이터인 json을 java배열로 바꾸기
    private void 위도경도jsonToArraylist(String str위도값들, String str경도값들) {


        Log.e(TAG, "위도경도jsonToArraylist() 입장");

        try {

            Log.e(TAG, "Json배열을 Java배열로 변환하기 try 들어옴");
            JSONArray jsonArr위도 = new JSONArray(str위도값들);
            JSONArray jsonArr경도 = new JSONArray(str경도값들);

            if (jsonArr위도 != null) {

                Log.e(TAG, "Json배열을 Java배열로 변환하기 if 들어옴");
                for (int i=0; i<jsonArr위도.length(); i++){

                    Double 삽입할위도 = Double.valueOf(jsonArr위도.get(i).toString());
                    Double 삽입할경도 = Double.valueOf(jsonArr경도.get(i).toString());
                    dbArr위도.add(삽입할위도);
                    dbArr경도.add(삽입할경도);
                    Log.e(TAG, "위도경도 삽입 i : "+i);

                }
            }

            //마침내 위도와 경도를 담아냈다.
            Log.e(TAG, "dbArr위도 : "+dbArr위도);
            Log.e(TAG, "dbArr경도 : "+dbArr경도);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void 마커셋팅(NaverMap 네이버Map) {

        Log.e(TAG, "마커셋팅() 들어옴 ");

        //sttic변수로 담아둔 마커객체 다시 지도에 뿌리기
        for (int i=0; i<dbArr위도.size(); i++) { //마커리스트가 null인 경우가 있어서 마커위도리스트를 기준으로 삼음

            Log.e(TAG, "마커셋팅() i : "+i);

            Marker marker = new Marker(); //다시 마커객체 생성해서 다시 위치에 삽입함
            marker.setPosition(new LatLng(dbArr위도.get(i), dbArr경도.get(i))); //static변수로 액티비티가 파괴되어도 사용할 수 있게함
            marker.setWidth(90);
            marker.setHeight(90);
            marker.setMap(네이버Map); //다시 대입한다 /해야 보임
            routeNum.setText(String.valueOf(i+1)); //인덱스+1해야 자연수가 나옴
            marker.setIcon(OverlayImage.fromView(routeNum)); //textview로 마커를 만든다.

        }
        routeNum.setVisibility(View.GONE); //마커로 만들어서 다 찍고 난 다음 숨김

        //카메라 위치 변경
        Log.e(TAG, "카메라 위치 변경");
        Log.e(TAG, "dbArr위도.get(0), dbArr경도.get(0) : "+dbArr위도.get(0)+dbArr경도.get(0));
        CameraPosition cameraPosition = new CameraPosition(new LatLng(dbArr위도.get(0), dbArr경도.get(0)), 12); //zoom은 높을수록 + 라고 생각하면 됨
        네이버Map.setCameraPosition(cameraPosition); //내 위치 중점이 아닌 첫 번째 마커가 중심이 되어 전체적인 모양을 관망함.


    }

    private void ID() {

        mapView = findViewById(R.id.map3);
        routeNum = (TextView)findViewById(R.id.routeNum2);

    }


}
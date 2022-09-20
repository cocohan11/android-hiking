package com.example.iamhere;

import static com.example.iamhere.L_login.h시간m분s초;
import static com.example.iamhere.L_login.iamLeader;
import static com.example.iamhere.L_login.ip;
import static com.example.iamhere.L_login.myEmail;
import static com.example.iamhere.L_login.myRoomActive;
import static com.example.iamhere.L_login.myRoom_no;
import static com.example.iamhere.L_login.port;
import static com.example.iamhere.L_login.socket;
import static com.example.iamhere.L_login.경도;
import static com.example.iamhere.L_login.마커경도리스트;
import static com.example.iamhere.L_login.마커리스트;
import static com.example.iamhere.L_login.마커위도리스트;
import static com.example.iamhere.L_login.마커합성bitmap;
import static com.example.iamhere.L_login.방비번;
import static com.example.iamhere.L_login.방이름;
import static com.example.iamhere.L_login.소켓통신목적;
import static com.example.iamhere.L_login.위도;
import static com.example.iamhere.M_share_2_Map.createOkHttpClient;
import static com.example.iamhere.M_share_2_Map.retrofit객체;
import static com.example.iamhere.M_share_2_Map.socketClose_Exit;
import static com.example.iamhere.M_share_2_Map.마커프사null이면set하기;
import static com.example.iamhere.M_share_2_Map.방퇴장처리;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iamhere.Interface.Sharing;
import com.example.iamhere.Model.Chat;
import com.example.iamhere.Model.ClientInfo;
import com.example.iamhere.Model.Sharing_room;
import com.example.iamhere.Model.getMarker;
import com.example.iamhere.Recyclerview.chat_Adapter;
import com.example.iamhere.Recyclerview.sharingList_Adapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kakao.sdk.share.ShareClient;
import com.kakao.sdk.share.model.SharingResult;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class M_share_3_join_Map extends AppCompatActivity implements OnMapReadyCallback {


    String TAG = "M_share_3_join_Map";
    //지도
    private MapView mapView; //지도 뷰(보이는 것)
    private NaverMap 네이버Map;
    FusedLocationSource locationSource; //런타임권한얻은 현재위치값
    final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}; //대략,정확한 위치 권한
    final int LOCATION_PERMISSION_REQUEST_CODE = 1000; //런타임권한요청코드
    //나머지 뷰
    public Button btn_share_Friends3, btn_share_exit; //카톡공유, 퇴장 버튼
    private TextView fold, tv_방이름_인원, routeNum; //명단뷰를 접는 손잡이, tcp/ip 적용하고 인원 받아오기, 경로마커로쓰일 뷰
    //시간
    Chronometer chronometer; //경과시간이 보이는 위젯. 그래서 따로 핸들러를 사용하지 않았다.
    long time; //시작한시간을 기준점으로 삼고 그 차이를 뷰에 삽입한다. //지나간 시간
    final int 초 = 1000;
    int h, m, s; //쉐어드 저장
    //스레드
    private Handler handler2 = new Handler(); //메소드안에 선언하면 사용할 수가 없네(에러:루퍼...)
    private boolean isRun = true;
    //채팅
    private RecyclerView rv_chat, rv_list; //채팅, 명단
    private EditText et_chat_msg; //메시지 입력란
    private Button btn_chat_send, btn_chat_nope; //메세지 보내기, 취소 버튼
    private ConstraintLayout showRecyclerview2; // 위치공유방 참여자 목록을 담은 레이아웃
    private ImageView iv_sendMessage, iv_setting, iv_compass, marker_img; //우측초록버튼 1 : 메세지보내기, 2:설정, 3: 나침반 / 재사용위치마커
    private Dialog dialog, dialog_chat, dialog_leave; // 메세지보내는 창
    private chat_Adapter adapter; //채팅창 리사이클러뷰에 대한 어댑터
    private ArrayList<Chat> chat_items = new ArrayList<>(); //채팅정보가 이 배열에 쌓임 (유저이름,메세지,시간)
    // 명단
    private sharingList_Adapter list_adapter; // chat_Adapter와 함께 다님
    private ArrayList<ClientInfo> clientList = new ArrayList<>(); // 입장하면 명단에 모두가 담긴다.
    String 방장닉넴;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mshare3_join_map);
        Log.e(TAG, "onCreate");
        ID();


        //방장닉넴으로 채팅방 방장메세지 색상 변경
        Intent intent = getIntent();
        방장닉넴 = intent.getStringExtra("방장닉넴");
        if (방장닉넴 == null) 방장닉넴 = "";
        Log.e(TAG, "방장닉넴 : "+방장닉넴);

        //기타
        tv_방이름_인원.setText(방이름+"(nn명)"); //방이름 삽입
        myRoomActive = true; //입장했으니까 퇴장하기 전까지는 true (나갔다 들어와도 true 유지된다.)
        쉐어드스태택저장(myRoom_no, myRoomActive); //방번호, 방이유효한지 쉐어드에 스태틱 변수 저장
        chronometer.setText("경과시간 00:00:00"); //3단계에서 처음 보이는 상태


        //경과시간
        Log.e(TAG,"\n값확인 111" +
                "\n스탑와치.getBase()"+ chronometer.getBase()+
                "\nSystem.currentTimeMillis() : "+System.currentTimeMillis());

        // 코드 옮김. 이상없으면 지워도 됨
//        chronometer.setBase(System.currentTimeMillis()); //스탑와치의 시작점을 시스템현재시간으로 맞춘다. tcp/ip 이후에는 방장이 방을 시작한 시간으로 맞춘다.
//        chronometer.setText("경과시간 00:00:00"); //3단계에서 처음 보이는 상태
//        chronometer.start(); //시작을 코드로 해줘야 굴러가서 이벤트가 작동됨


        Log.e(TAG,"\n값확인 222" +
                        "\n스탑와치.getBase()"+ chronometer.getBase()+
                        "\nSystem.currentTimeMillis() : "+System.currentTimeMillis());

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 네이버 지도 초기화
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this); //네이버지도에서 반환되는 콜백함수를 자신(this)으로 지정하는 역할
        //Async : 비동기(로 NaverMap객체를 얻는다)
        locationSource = new FusedLocationSource(M_share_3_join_Map.this, LOCATION_PERMISSION_REQUEST_CODE); //권한요청객체생성(GPS)


        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // retrofit - DB 명단에 참여자 추가하기
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        retrofit_RoomUser_추가();


        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 이벤트/리스너 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ



        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 친구에게 공유하기 : 카카오톡 공유 API (방이름,비번 text + 바로 참여하기)
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        btn_share_Friends3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "btn_share_Friends 클릭");
                kakaoLink(); //카카오톡 방이름, 비번 공유하기

            }
        });



        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 퇴장 버튼 클릭 : 스태틱변수, 쉐어드 삭제, 서버DB변경(tcp/ip이후 손대기), M_main이동
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        btn_share_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "참여자가 퇴장 버튼 클릭");

                //ㅡㅡㅡㅡㅡㅡ
                // 다이얼로그 : 예, 아니요
                //ㅡㅡㅡㅡㅡㅡ
                dialog_leave = new AlertDialog.Builder(M_share_3_join_Map.this) // 현재 Activity의 이름 입력.
                        .setMessage("\n   local  정말 나가시겠습니까?\n")
                        .setNeutralButton("              예              ", new DialogInterface.OnClickListener() { //확인을 왼쪽에 해야 실수로 더블클릭하는 경우를 막을 수 있음
                            public void onClick(DialogInterface dialog, int which){

                                Toast.makeText(getApplicationContext(), "퇴장하였습니다.", Toast.LENGTH_SHORT).show(); //퇴장알림을 해야 확신할 듯

                                //명단에 추가가 안 되어있음.. 우선 tcp/ip를 하고 디비를 손대자
                                retrofit_퇴장업뎃_exit(myRoom_no, myEmail, h시간m분s초); //위치공유방 참여자 명단 변경(퇴장시간, 소요시간)
                                방퇴장처리(getApplicationContext()); //퇴장 후 뒷처리 : 변수, 쉐어드 초기화
                                socketClose_Exit();
                                isRun = false; // 자기위치 찍기 종료


                            }
                        })
                        .setPositiveButton("         아니요            ", new DialogInterface.OnClickListener() { //일부러 띄워쓰기 한거임. 간격조절
                            public void onClick(DialogInterface dialog, int which){}})
                        .show();
            }
        });

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 스톱와치(경과시간) : 1초마다 이벤트
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                time = System.currentTimeMillis() - chronometer.getBase(); //베이스는 고정
                chronometer.setText(시분초변환(time));  // "경과시간 00:00:12" 따위를 리턴받는다

                //자동퇴장은 방장쪽에서 결정한다.

            }
        });


        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 참가자 명단 뷰 접는 손잡이 : 담겨있는 뷰를 접었다 폈다 한다.
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        fold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 리사이클러뷰 보이고 안 보이게하는 버튼
                if(rv_list.getVisibility() == View.GONE) { // showRecyclerview : ConstraintLayout로 text,RV,btn 묶음
                    rv_list.setVisibility(View.VISIBLE); //펼치기

                } else {
                    rv_list.setVisibility(View.GONE); //자리까지 없앰
                }
            }
        });


    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 지도 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        Log.e(TAG, "3 onMapReady() 입장");
        네이버Map = naverMap; //전역변수에 대입(스레드에서 쓰려고)


        //ㅡㅡㅡㅡㅡㅡ
        // 경로 마커
        //ㅡㅡㅡㅡㅡㅡ
        retrofit마커get(naverMap, myRoom_no); //방번호에 대한 경로마커 찍기 (test번호 바꾸기)

        //ㅡㅡㅡㅡㅡㅡ
        // 마커 스레드 : n초마다 찍힘
        //ㅡㅡㅡㅡㅡㅡ
//        위도경도Thread(naverMap,3); //내위치 합성프사마커 찍는 스레드, 1초마다


        //런타임 권한
        Log.e(TAG, "런타임 권한을 맵에 지정");
        ActivityCompat.requestPermissions(M_share_3_join_Map.this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE); //현재위치 표시할 때 권한 확인(이미 M_main에서 통과됐기때문에 여기서는 런타임권한메소드 x)


        //현재위치 GPS
        naverMap.setLocationSource(locationSource); //현재위치
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow); //트래킹모드를 선언해야 위치오버레이가 보임
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, true); //레이어 : 등산로
        Log.e(TAG, "LAYER_GROUP_MOUNTAIN 설정");
        naverMap.addOnLocationChangeListener(location -> //로그 : 위경도
                {
                    위도 = location.getLatitude(); //실시간 스태틱 변수에 대입
                    경도 = location.getLongitude();
//                    Log.e(TAG, "위도경도(변경된) : "+location.getLatitude()+ ", " + location.getLongitude() );
                }
        );


        //위치 오버레이
        // (사용자의 위치를 나타내는 데 특화된 오버레이이로, 지도상에 단 하나만 존재)
        LocationOverlay locationOverlay = naverMap.getLocationOverlay(); //위치 인스턴스 생성x 왜냐면 유일무이..그래서 호출해옴
        locationOverlay.setPosition(new LatLng(위도, 경도)); //더 빠르게 위치잡으라고 set해줌
        locationOverlay.setVisible(true); //가시성 : 기본false


        //UI 컨트롤을 제어(M_main과동일)
        UiSettings uiSettings = naverMap.getUiSettings(); //설정객체 선언
        uiSettings.setLocationButtonEnabled(true); //현재위치 보는 방향 설정
        uiSettings.setCompassEnabled(true); //나침반. 이게 최선임. 항상 고정하고싶은데 스르륵 사라져버림
        uiSettings.setScaleBarEnabled(true); //축척바 활성화 안하는게 덜 지저분해 보일 듯(등산어플이랑 비교해봐야지)
        uiSettings.setZoomControlEnabled(false); //+-버튼 없앰


        //ㅡㅡㅡㅡㅡㅡ
        // 소켓 연결 : 서버에 연결하여 채팅, 마커를 찍는다.
        //ㅡㅡㅡㅡㅡㅡ
        // 여기 위치해야 함. 왜냐면 onCreate()에 위치하면 네이버Map 변수가 null 이다. M_shared_2_Map 에 전달하지 못한다.
        M_share_2_Map 위치공유방 = new M_share_2_Map(); // 여기서 만들어둔 메소드 재사용

        Log.e(TAG, "변수 adapter : "+adapter);
        Log.e(TAG, "chat_리사이클러뷰_adapter장착() + 소켓연결()");
        Log.e(TAG, "네이버Map : "+네이버Map);
        Log.e(TAG, "rv_list : "+rv_list);

        adapter = new chat_Adapter(getApplicationContext(), chat_items, 방장닉넴, clientList);
        list_adapter = new sharingList_Adapter(getApplicationContext(), clientList, 방장닉넴);

        위치공유방.sharingList_AND_chat_rv_Adapter장착(rv_chat, adapter, rv_list, list_adapter, getApplicationContext()); //보이기시작한 채팅창에 어댑터를 장착한다. 가독성을 위해 함수로 만들었다.
        위치공유방.소켓연결(iv_sendMessage, showRecyclerview2, fold, dialog_chat, dialog_leave, et_chat_msg, btn_chat_send, btn_chat_nope, btn_share_exit, iv_compass, // iv_compass 입력한 이유 : 참여자는 넣을 필요없어서 null 넣으니까 에러나서 임시방편으로 삽입
                handler2, adapter, chat_items, rv_chat, getApplicationContext(), M_share_3_join_Map.this, tv_방이름_인원, marker_img, isRun, 네이버Map,
                clientList, list_adapter, rv_list, chronometer);



    } //~onMapReady()


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ http 통신 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

//    @SuppressLint("LongLogTag")
//    private void retrofit_RoomUser_추가() {
//
//
//        // 명단을 추가하여 '나의 기록' - '리스트보기' 에서 같이 등산했던 멤버들을 볼 수 있다.
//        Log.e(TAG, "retrofit_RoomUser_추가() 입장!!!!!!!!!!");
//
//        //레트로핏 객체 생성, 빌드
//        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder() //Retrofit 인스턴스 생성
//                .baseUrl("http://15.164.129.103/")  //baseUrl 등록
//                .addConverterFactory(GsonConverterFactory.create())  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
//                .client(createOkHttpClient()) //네트워크 통신 로그보기(서버로 주고받는 파라미터)
//                .build();
//
//
//
//        Sharing SharingRoomCreate = retrofit.create(Sharing.class);
//        Call<Sharing_room> call = SharingRoomCreate.joinTheRoom(myRoom_no, myEmail); // 닉네임을 안 보내도 email로 찾아낼 수 있다.
//
//
//        //네트워킹 시도 2
//        call.enqueue(new Callback<Sharing_room>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onResponse(Call<Sharing_room> call, Response<Sharing_room> response) {
//
//                Sharing_room result = response.body();
//                assert result != null;
//                Log.e(TAG, "성공인가??? : "+result.getResponse()); // db에 잘 들어갔는지만 확인하기. 사용할 일은 없음
//
//            }
//
//            @Override
//            public void onFailure(Call<Sharing_room> call, Throwable t) {
//                Log.e("onFailure : ", t.getMessage());
//            }
//        });
//
//    }


    //서버에 위치공유방 참여자 명단을 변경한다. (종료시간, 소요시간을 업뎃)
    static public void retrofit_퇴장업뎃_exit(String 방번호, String 이메일, String 경과시간) {

        String TAG = "retrofit_퇴장업뎃_exit";

        //변수값 확인
        Log.e(TAG, "\nretrofit_퇴장업뎃() 메소드"+
                "\n방번호 : " + 방번호 +
                "\n이메일 : "+이메일 +
                "\n경과시간 : "+경과시간 );

        Sharing SharingRoomCreate = retrofit객체().create(Sharing.class);   // 레트로핏 인터페이스 객체 구현
        Call<Sharing_room> call = SharingRoomCreate.exitTracking(방번호, 이메일, 경과시간); //보냄 : 방번호로 특정하여 db에 현재시간 삽입하기


        //네트워킹 시도 2
        call.enqueue(new Callback<Sharing_room>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Sharing_room> call, Response<Sharing_room> response) {

                Sharing_room result = response.body();
                assert result != null;
                Log.e(TAG, "성공인가유? : "+result.getResponse()); //응답값은 db에 잘 들어갔는지 확인만하기. 사용할 일은 없음

            }

            @Override
            public void onFailure(Call<Sharing_room> call, Throwable t) {
                Log.e("onFailure : ", t.getMessage());
            }
        });
    }


    private void retrofit마커get(NaverMap naverMap, String myRoom_no) {

        Log.e(TAG, "retrofit마커get() 메소드 입장// myRoom_no : "+myRoom_no);

        Sharing 마커불러오기 = retrofit객체().create(Sharing.class);   // Sharing라는 interface에서 recyclerviewRecord()메소드로 보낸다.
        Call<ArrayList<getMarker>> call = 마커불러오기.markerArraylistGet2(myRoom_no); // Call<ArrayList<getMarker>> : 데이터를 받아오는 곳


        //네트워킹 시도
        call.enqueue(new Callback<ArrayList<getMarker>>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged", "ClickableViewAccessibility"})
            @Override
            public void onResponse(Call<ArrayList<getMarker>> call, @NonNull Response<ArrayList<getMarker>> response) {

                Log.e(TAG, "성공..? response : "+response);
                Log.e(TAG, "response.body()..? : "+response.body());

                //ㅡㅡㅡㅡㅡㅡㅡㅡ
                // 응답받은 배열
                //ㅡㅡㅡㅡㅡㅡㅡㅡ
                ArrayList<getMarker> items = response.body(); //대입하는 순간 SerializedName에 따라 이름따라 값이 들어가짐 !! 중요
                Log.e(TAG, "getMarker타입 items.get(0).getArrLat() : "+items.get(0).getArrLat()); // 37.48503839980235
                Log.e(TAG, "getMarker타입 items.get(0).getArrLng() : "+items.get(0).getArrLng());

                마커셋팅(naverMap, items); //지도뷰에 마커삽입

            }

            @Override
            public void onFailure(Call<ArrayList<getMarker>> call, Throwable t) {
                Log.e("onFailure : ", t.getMessage());
            }
        });

    }
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 기타 메소드 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    public void kakaoLink() {

        Log.e(TAG, "카카오링크 메소드 입장");

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 카톡메세지 보낼 재료
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        long 템플릿ID = 79471; //내 카카오개발자 메세지 템플릿에서 미리 만들어 둠 (https://developers.kakao.com/tool/template-builder/app/737046/template/79471/component/itl/0)
        Map<String, String> 템플릿요소 = new HashMap<>(); //데이터 타입을 먼저 지정해주는구나
        템플릿요소.put("roomName", 방이름); //바뀔 파라미터
        템플릿요소.put("roomPW", 방비번); //방이름과 방비번이 삽입되서 카톡공유된다.


        //카톡 설치여부 확인
        if (ShareClient.getInstance().isKakaoTalkSharingAvailable(getApplicationContext())) { // LinkClient --> ShareClient 버전 바뀌면서 클래스도 바뀐거였어... 공홈에서 찾음. 한참 헤맸네


            Log.e(TAG, "카카오 설치여부 ㅇㅋ");
            Log.e(TAG, "템플릿ID : "+템플릿ID);
            Log.e(TAG, "템플릿요소 : "+템플릿요소); //맵으로 감싸서 보냄. 탬플릿에서 파라미터를 인지함


            //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
            // 메세지를 보낸다. (양식이 맞으면 카톡을 열어서 친구목록을 띄운다.)
            //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
            ShareClient.getInstance().shareCustom(getApplicationContext(), 템플릿ID, 템플릿요소, new Function2<SharingResult, Throwable, Unit>() {
                @Override
                public Unit invoke(SharingResult sharingResult, Throwable throwable) {

                    Log.e(TAG, "throwable : "+throwable);
                    Log.e(TAG, "sharingResult : "+sharingResult);

                    if (throwable != null) { //예외났는지

                        Log.e(TAG, "throwable.toString() : "+throwable.toString());

                    } else if(sharingResult != null) { //결과값이 있다면 공유하기

                        Log.e(TAG, "sharingResult에 값 있어서 들어옴 ");
                        startActivity(sharingResult.getIntent()); //명시적 intent로 카톡 오픈
                    }
                    return null;
                }
            });

        } else {
            Log.e(TAG, "카카오 설치여부 ㄴㄴㄴ");
            Toast.makeText(getApplicationContext(), "카카오톡이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show(); //이동하기전에 토스트외치기
        }

    }

    //내위치를 이미지마커로 만들어서 1초마다 스레드로 다시 위치를 설정한다.
    public void 위도경도Thread(NaverMap naverMap, int sec) { //화면전환될 때 종료시킴

        //ㅡㅡㅡ
        // 마커
        //ㅡㅡㅡ
        Marker marker = new Marker(); //마커객체를 반복문 바깥에 위치
        Log.e(TAG, "marker 객체생성");


        //서브스레드 객체 -> 핸들러 -> 메세지큐 -> 루퍼 -> 핸들러 -> 메인메서드 객체 전달 완료
        new Thread(new Runnable() {

            @Override
            public void run() { //1초마다 벨류값 1씩 증가시키는 스레드임


                if(마커합성bitmap == null) { //최종형태 합성마커 bitmap가 없으면 만들기
                    마커프사null이면set하기(); //없다면 만들어준다. 한 번만. 스레드안에서.
                }


                Log.e(TAG, "위도경도Thread() isRun : "+isRun);

                while ((isRun)) { //false가 되면 멈춤 - 화면전환시

                    Log.e(TAG, "위도경도Thread() while() isRun : "+isRun);
                    handler2.post(new Runnable() { //post : 다른 스레드로 메세지(객체)를 전달하는 함수

                        @Override
                        public void run() { //마커의 위치만 변경

                            Log.e(TAG, "지도3 위도, 경도 : "+위도+ ", " + 경도);
                            Log.e(TAG, "위도경도Thread() isRun : "+isRun);

                            marker.setMap(null); //기존마커 없앤다
                            marker.setPosition(new LatLng(위도, 경도)); //먼저
                            marker.setMap(naverMap); //다시 대입한다

                            if(마커합성bitmap != null) { //최종형태 합성마커 bitmap
                                marker.setIcon(OverlayImage.fromBitmap(마커합성bitmap)); //미리 main에서 대입된 변수

                            } else { //에러표시
                                marker.setIcon(OverlayImage.fromResource(R.drawable.hiking)); //test
                            }


                            /*****************************
                             * 강제종료 & 퇴장 & 운동시작
                             ****************************/
                            if (소켓통신목적.equals("강제종료")) {

//                                Log.e(TAG, "위치스레드안에 위치시킴... 소켓통신목적 : "+소켓통신목적);
//                                Toast.makeText(getApplicationContext(), "위치공유방이 종료되었습니다.\n나의 기록에서 조회하실 수 있습니다.", Toast.LENGTH_LONG).show(); // 실행할 코드
//
//                                retrofit_퇴장업뎃_exit(myRoom_no, myEmail, h시간m분s초); //위치공유방 참여자 명단 변경(퇴장시간, 소요시간)
//                                방퇴장처리(getApplicationContext()); //퇴장 후 뒷처리 : 변수, 쉐어드 초기화
//                                socketClose_Exit();
//                                isRun = false; // 자기위치 찍기 종료
//                                소켓통신목적 = "";


                            } else if (소켓통신목적.equals("퇴장")) {

//                                Log.e(TAG, "위치스레드안에 위치시킴... 소켓통신목적 : "+소켓통신목적);
//                                Toast.makeText(getApplicationContext(), "퇴장하였습니다.", Toast.LENGTH_SHORT).show(); //퇴장알림을 해야 확신할 듯
//                                소켓통신목적 = "";

                            } else if (소켓통신목적.equals("운동시작")) {

                                Log.e(TAG, "위치스레드안에 위치시킴... 소켓통신목적 : "+소켓통신목적);
                                Toast.makeText(getApplicationContext(), "등산을 시작합니다. 안전산행 하세요.", Toast.LENGTH_SHORT).show(); //퇴장알림을 해야 확신할 듯
                                chronometer.setBase(System.currentTimeMillis()); //스탑와치의 시작점을 시스템현재시간으로 맞춘다. tcp/ip 이후에는 방장이 방을 시작한 시간으로 맞춘다.
//                                chronometer.setText("경과시간 00:00:00"); //3단계에서 처음 보이는 상태
                                chronometer.start(); //시작을 코드로 해줘야 굴러가서 이벤트가 작동됨
                                Log.e(TAG, "위치스레드안에 위치시킴... chronometer.start()");
                                소켓통신목적 = "";

                            }

                        }
                    });
                    try {
                        Thread.sleep(1000*sec); //1초마다 내 위치 reset
                    } catch (Exception e) {
                    }
                }
            }
        }).start(); //start()붙이면 바로실행시킨다.
    }


    //서버에서 받아온 위도경도를 지도에 뷰로 뿌리기
    private void 마커셋팅(NaverMap naverMap, ArrayList<getMarker> 위도경도리스트) {

        Log.e(TAG, "마커셋팅() 들어옴 ");

        for (int i=0; i<위도경도리스트.size(); i++) { //마커리스트가 null인 경우가 있어서 마커위도리스트를 기준으로 삼음

            Log.e(TAG, "마커셋팅() i : "+i);

            Marker marker = new Marker(); //다시 마커객체 생성해서 다시 위치에 삽입함
            marker.setPosition(new LatLng(위도경도리스트.get(i).getArrLat(), 위도경도리스트.get(i).getArrLng())); //static변수로 액티비티가 파괴되어도 사용할 수 있게함
            marker.setWidth(90);
            marker.setHeight(90);
            marker.setMap(naverMap); //다시 대입한다 /해야 보임
            routeNum.setText(String.valueOf(i+1)); //인덱스+1해야 자연수가 나옴
            marker.setIcon(OverlayImage.fromView(routeNum)); //숫자먼저 set하고 뷰를 삽입한다.

        }

    }


//    public void 방퇴장처리() {
//
////        isRun = false; //스레드 종료(해줘야 마커가 두개가 안 찍힘)
//        onBackPressed(); //이동 >> M_main.class
//
//        //퇴장할 때 같이 마커도 지워줘야 새로 방 만들었을 때 이전방의 경로가 찍히는 불상사를 방지한다.
//        마커리스트.clear();
//        마커위도리스트.clear();
//        마커경도리스트.clear();
//        Log.e(TAG, "마커리스트[] : "+마커리스트);
//
//        //변수도 비우기
//        방이름 = null;
//        방비번 = null;
//
//        //위치공유방에 대한 쉐어드를 비운다.
//        쉐어드비우기_스태틱_스탑와치(); //쉐어드 값을 삭제하면 재입장했을 때 시간을 이어가지 않는다.
//
//    }

    private void 쉐어드스태택저장(String 방번호, boolean 방유효한가) {

        Log.e(TAG, "\n쉐어드스태택저장() 메소드"+ //변수값 확인
                "\n방번호 : " + 방번호 +
                "\n방유효한가 : "+방유효한가 );

        SharedPreferences shared = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능
        SharedPreferences.Editor Editor = shared.edit();

        Editor.putString("myRoom_no", 방번호);
        Editor.putBoolean("myRoomActive", 방유효한가); //스태틱변수로만 있던걸 쉐어드에 저장해서 나갔다 들어와서 유효한 화면이 뜨도록 하기

        Editor.apply(); //실질 저장

    }


    private void 쉐어드비우기_스태틱_스탑와치() {

        //쉐어드 비우기 1
        SharedPreferences shared = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능
        SharedPreferences.Editor Editor = shared.edit();

        Editor.putString("myRoom_no", null);
        Editor.putBoolean("myRoomActive", false); //변수만 지우면 Login에서 다시 삽입됨
        Editor.putBoolean("iamLeader", false); //더 이상 방장아님

        Editor.apply(); //실질 저장

        //쉐어드 비우기 2
        SharedPreferences 쉐어드 = getSharedPreferences("stopwatch", Activity.MODE_PRIVATE);
        SharedPreferences.Editor 에디터 = 쉐어드.edit();
        에디터.clear(); //다 지워서 새로 방 입장한다고 했을 때 진행안되고 재입장이면 진행되게 함_
        에디터.apply(); //저장

    }

    private String 시분초변환(long time) {

        h = (int)(time /(3600*초));
        m = (int)(time - h*(3600*초))/(60*초);
        s = (int)(time - h*(3600*초)- m*(60*초))/초 ;

//        Log.e(TAG,"s : " + String.valueOf(s));
//        Log.e(TAG,"m : " + String.valueOf(m));
//        Log.e(TAG,"h : " + String.valueOf(h));

        //"00시00분14초"가 지저분해보여서 0시 0분이면 안보이기
        h시간m분s초 = (h > 0 ? h + "시간 " : "") + (m > 0 ? m + "분 " : "") + (s > 0 ? s + "초" : ""); //삼항연산자    조건 ? 참 : 거짓
        String t = "경과시간 "+(h < 10 ? "0"+h: h)+":"+(m < 10 ? "0"+m: m)+":"+ (s < 10 ? "0"+s: s); //"00:12:34" 이렇게 표기

//        Log.e(TAG,"...time" + String.valueOf(time));
//        Log.e(TAG,"...t" + t);
//        Log.e(TAG,"...h시간m분" + h시간m분s초);


        return t;
    }

    //뒤로가기를 누르면 방이름, 방비번 입력하는 액티비티로 가는 문제 -> 메인으로 가면서 다 삭제
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.e(TAG, "onBackPressed() 누름");

        //플래그사용해서 원하는 화면이동
        Intent intent = new Intent(getApplicationContext(), M_main.class); //메인이로 이동
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP ); //M_main외는 지움
        startActivity(intent);

    }
    public void ID() {
        mapView = findViewById(R.id.map3);
        chronometer = (Chronometer)findViewById(R.id.chronometer3);
        btn_share_Friends3 = (Button)findViewById(R.id.btn_share_Friends3);
        btn_share_exit = (Button)findViewById(R.id.btn_share_exit3);
        fold = (TextView)findViewById(R.id.fold3);
        tv_방이름_인원 = (TextView)findViewById(R.id.textView20);
        routeNum = (TextView)findViewById(R.id.routeNum3); //마커 순서
        rv_chat = (RecyclerView) findViewById(R.id.rv_chat_member); // 채팅방
        iv_sendMessage = (ImageView) findViewById(R.id.iv_sendMSG); // 초록버튼 1.메세지보내기
        iv_setting = (ImageView) findViewById(R.id.iv_setting); // 2.설정
        iv_compass = (ImageView) findViewById(R.id.iv_compass); // 3.나침반
        showRecyclerview2 = (ConstraintLayout)findViewById(R.id.showRecyclerview2); // 참여자 목록을 담은 하단 레이아웃

        //다이얼로그 - 채팅보내기
        dialog = new Dialog(M_share_3_join_Map.this); //다이얼로그 객체 생성

        dialog_chat = new Dialog(M_share_3_join_Map.this);
        dialog_chat.requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 제거
        dialog_chat.setContentView(R.layout.dialog_chat_send); //보여줄 xml레이아웃과 연결
        et_chat_msg = dialog_chat.findViewById(R.id.et_chat_msg); // 입력란. 위에서 inflate했으니 id를 가져올 수 있다.
        btn_chat_send = dialog_chat.findViewById(R.id.btn_chat_send); // 보내기 버튼
        btn_chat_nope = dialog_chat.findViewById(R.id.btn_chat_nope); // 취소 버튼. 창 사라지게하기

        // 다이얼로그 - 방종료창
//        dialog_leave = new Dialog(M_share_3_join_Map.this);
//        dialog_leave.requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 제거
//        dialog_leave.setContentView(R.layout.dialog_leave); //보여줄 xml레이아웃과 연결


        // 명단
        rv_list = (RecyclerView) findViewById(R.id.rv_list2);
        // 마커
        marker_img = findViewById(R.id.marker_img2);

    }


}
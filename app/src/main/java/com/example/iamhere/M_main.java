package com.example.iamhere;
import static com.example.iamhere.L_login.bitmapCapture;
import static com.example.iamhere.L_login.iamLeader;
import static com.example.iamhere.L_login.myMarkerImg;
import static com.example.iamhere.L_login.myRoomActive;
import static com.example.iamhere.L_login.myRoom_no;
import static com.example.iamhere.L_login.mySmallBitmapImg;
import static com.example.iamhere.L_login.myDate;
import static com.example.iamhere.L_login.myEmail;
import static com.example.iamhere.L_login.myPw;
import static com.example.iamhere.L_login.myName;
import static com.example.iamhere.L_login.myImg;
import static com.example.iamhere.L_login.mysnsLogin;
import static com.example.iamhere.L_login.마커경도리스트;
import static com.example.iamhere.L_login.마커리스트;
import static com.example.iamhere.L_login.마커위도리스트;
import static com.example.iamhere.L_login.마커합성bitmap;
import static com.example.iamhere.L_login.방비번;
import static com.example.iamhere.L_login.방이름;
import static com.example.iamhere.L_login.위도;
import static com.example.iamhere.L_login.경도;
import static com.example.iamhere.L_profile.BitmapToString;
import static com.example.iamhere.M_share_2_Map.isServiceRunning;
import static com.example.iamhere.socket.Constants.LOCATION_PERMISSION_REQUEST_CODE;
import static com.example.iamhere.socket.Constants.PERMISSIONS;
import static com.example.iamhere.socket.myService.socket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.iamhere.Interface.Sharing;
import com.example.iamhere.Model.Sharing_room;
import com.kakao.sdk.talk.TalkApiClient;
import com.kakao.sdk.user.UserApiClient;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationSource;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class M_main extends AppCompatActivity implements OnMapReadyCallback { //implement꼭해주기(인터페이스구현)

    //L_first >> M_main
    //L_login >> M_main
    //L_profile >> M_mail

    String TAG = "M_main";
    private long backKeyPressedTime = 0; //뒤로가기 2번 종료
    LinearLayout navi_map; //메인화면의 하단 123중 1번째 : 지도 / 클릭기능을 LinearLayout으로 한 이유:글자까지 같이 클릭할 수 있게 하려고
    LinearLayout navi_myPlace; //2번째 : 나의 공간
    LinearLayout navi_setting;  //3번째 : 설정
    MapView mapView; //지도 뷰(보이는 것)
    ImageView circle_btn_sharing;
    NaverMap 네이버Map; //null이었다가 onMapReady에서 값 대입받음 지도 api(기능)....어디에 쓰는지 잘 모르겠음 어차피 콜백함수에서 알아서 네이버맵 클래스 불러오는데?
    FusedLocationSource locationSource; //런타임권한얻은 현재위치값
    String filePath; //지도위프로필사진 넣기위해 file객체 만듦
    Handler handler2 = new Handler(); //스레드 도중 UI객체에 손대기 위해 핸들러 부름
    boolean BoolImg; //값유무확인용
    boolean isRun = true; //스레드 멈추기용도 - 화면전환시
    SharedPreferences auto, stopWatch; //변수,스탑와치 쉐어드
    SharedPreferences.Editor 스탑와치editor, 변수editor; //get해서 강제종료인지 확인 후 변수 처리
    ArrayList<Marker> arr마커1개만존재 = new ArrayList<>(); //실시간 내 위치마커가 2개이상 존재하면 없애기 위한 배열

    // 서비스


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mmain);
        Log.e(TAG, "onCreate()");
        Log.e(TAG,"UserApiClient.getInstance() : "+ UserApiClient.getInstance());
        Log.e(TAG, "TalkApiClient.getInstance() : "+ TalkApiClient.getInstance());
        Log.e(TAG, "startLocationService() isServiceRunning : "+isServiceRunning(getApplicationContext()));
        Log.e(TAG, "socket : "+socket);

//        Log.e(TAG, "서비스 종료 후 돌아왔을 때 socket 은? : "+ socket);

        // 위치관리자 객체 생성성
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        ID();
        Intent intent = getIntent();

        // sns로그인 or 일반로그인 구분
        String getL_login_kakao = intent.getStringExtra("L_login_kakao");
        Log.e(TAG, "L_login_kakao :"+getL_login_kakao);

        // 서비스 명령 받았는지
        String stopService = intent.getStringExtra("stopService");
        Log.e(TAG, "stopService :"+stopService);


        // 서비스 동작중인지 확인
        Log.e(TAG, "isServiceRunning 젼: "+isServiceRunning(getApplicationContext()));
        if (stopService != null && isServiceRunning(getApplicationContext())) { // 멈추라는 명령 + 서비스 실행중이면
            Log.e(TAG, "isServiceRunning 서비스 멈춰라");



//            new M_share_2_Map().stopLocationService(getApplicationContext()); // 서비스 종료
            Log.e(TAG, "isServiceRunning 후 : "+isServiceRunning(getApplicationContext()));
        }


        //자동로그인이 됐으면 서버로 보내서 데이터가져옴
        //지금문제가된게 자동로그인이 아닌경우에 대한 예외처리가 안됐음(인텐트로 했을 경우 에러남)
        //그냥 다시 쉐어드꺼내는게 낫겠다. 3곳에서 intent보내주느니.
        //이 코드가 있어야 위에서 오버라이드한 onSessionOpened()메소드가 돌아감

        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        // 쉐어드 파일 1 : 스태틱 변수 초기화
        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        myEmail = auto.getString("UserEmail",null); //static변수에 넣어서 사용하기 쉽게하기
        myPw = auto.getString("UserPwd",null);
        myName = auto.getString("UserNickName",null);
        myImg = auto.getString("UserImg",null); //스태틱변수에 url 저장
        myMarkerImg = auto.getString("myMarkerImg",null);
        myDate = auto.getString("CreateDate",null);
        filePath = auto.getString("filePath",null); //이 경로로 file객체 만들어서 지도위에 사진삽입
        mySmallBitmapImg = auto.getString("smallBitmapImg",null); //로그인할 때 이미지도 같이 쉐어드에 새로 저장시켜서 M_main에서 사용
        myRoomActive = auto.getBoolean("myRoomActive", false); //위치공유방 어느 단계인지(true면 3단계 이상) 어플이 꺼지더라도 쉐어드에서 꺼냄가능
        myRoom_no = auto.getString("myRoom_no", null); //방번호. 앱종료/전원꺼져도 들어오게 //값있음:방생성 후 값없음:퇴장누를 때
        방이름 = auto.getString("방이름", null); //방번호. 앱종료/전원꺼져도 들어오게 //값있음:방생성 후 값없음:퇴장누를 때
        방비번 = auto.getString("방비번", null); //카톡공유할 때 필요한 스태틱 변수
        if(mySmallBitmapImg != null) BoolImg = true;
        iamLeader = auto.getBoolean("iamLeader", false); //방장으로 참여중인지 판별



        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        // 쉐어드 파일 2 : 스탑와치변수로 위치공유방 4단계 강제종료인건지 판별해서 map2에 못들어가게 하려고 여기에 조건검
        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        stopWatch = getSharedPreferences("stopwatch", Activity.MODE_PRIVATE);
        스탑와치editor = stopWatch.edit();


        //M_main.class는 앱을 키면 무조건 거쳐가는곳이라 여기서 스태틱변수생성함
        Log.e(TAG,"스태틱에 쉐어드 삽입" +
                "\nemail : "+myEmail+
                "\npw : "+myPw+
                "\nmyName : "+myName+
                "\nmyImg : "+myImg+
                "\nmyMarkerImg : "+myMarkerImg+
                "\ndate : "+myDate+
                "\nfilePath : "+filePath+
                "\nnmySmallBitmapImg : "+BoolImg+
//                "\n마커합성bitmap : "+마커합성bitmap+
                "\n방이름 : "+방이름+
                "\n방비번 : "+방비번+
                "\nmyRoomActive : "+myRoomActive+
                "\niamLeader : "+iamLeader+
                "\nmyRoom_no : "+myRoom_no+
                "\nsnsLogin : "+mysnsLogin ); //확인용





        // 2022/05/27 07:31:46 am >> 2022/05로 만들기
        if(myDate != null) myDate = myDate.substring(0,7);
        Log.e(TAG, "ㅂ myDate : "+myDate);
        Log.e(TAG, "ㅂ myImg : "+myImg);
        Log.e(TAG, "ㅂ 마커합성bitmap : "+마커합성bitmap);


        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 로그아웃 후 마커이미지 : 로그아웃(어플살아있음), 로그아웃(어플종료후로그인), 사진등록없이 처음입장
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        if(마커합성bitmap == null && myImg != null) { // 로그아웃(어플살아있음) //myImg에는 값이 있어야 변형

            new Thread(() -> { //별개의 스레드로 해야 에러안남

                //url to bitmap
                //bitmap 합성 (마커형태커스덤)
                //bitmap to string (쉐어드저장용)

                Bitmap smallBitmap = URLtoBitmap(myImg); //network 동작, 인터넷에서 xml을 받아오는 코드
                Log.e(TAG, "\n로그아웃 후 마커 이미지" +
                                "\nsmallBitmap : "+smallBitmap+
                                "\n마커합성bitmap : " + 마커합성bitmap
                );

                마커합성bitmap = bitmapEdit(smallBitmap); //마커모양으로 편집,합성
                mySmallBitmapImg = BitmapToString(TAG, 마커합성bitmap); //쉐어드에 저장하기 위해 string으로 변환

                Log.e(TAG, "\n마커합성bitmap : "+마커합성bitmap
//                        "\nmySmallBitmapImg" + mySmallBitmapImg
                );


                //스레드에러나서 쉐어드를 별도로 여기에 선언
                SharedPreferences 변수쉐어드 = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor 변수에디터 = 변수쉐어드.edit();
                변수에디터.putString("smallBitmapImg", mySmallBitmapImg); //프사마커를 string으로 저장하기. 안 하면 카톡공유받아서 입장할 때 프사마커이미지값이 없음
                변수에디터.apply(); //실질 저장

            }).start();

        } else if(마커합성bitmap == null && myImg == null) { //사진등록한 적 없음

            new Thread(() -> {

                Bitmap smallBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_prof_first);
                Log.e(TAG, "smallBitmap *** : "+smallBitmap);
                마커합성bitmap = bitmapEdit2_기본이미지(smallBitmap);
                Log.e(TAG, "마커합성bitmap *** : "+마커합성bitmap);

                mySmallBitmapImg = BitmapToString(TAG, 마커합성bitmap); //쉐어드에 저장하기 위해 string으로 변환

                SharedPreferences 변수쉐어드 = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor 변수에디터 = 변수쉐어드.edit();
                변수에디터.putString("smallBitmapImg", mySmallBitmapImg); //프사마커를 string으로 저장하기. 안 하면 카톡공유받아서 입장할 때 프사마커이미지값이 없음
                변수에디터.apply(); //실질 저장

            }).start();

        } else { //로그아웃(어플종료후로그인)

            Log.e(TAG, "새로 생성 안 해도 됨 : "+myImg+마커합성bitmap);

        }



        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 네이버 지도 초기화
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this); //네이버지도에서 반환되는 콜백함수를 자신(this)으로 지정하는 역할
                                           //Async : 비동기(로 NaverMap객체를 얻는다)
        locationSource = new FusedLocationSource(M_main.this, LOCATION_PERMISSION_REQUEST_CODE); //권한요청객체생성(GPS)


        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 마커 이미지 대입
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        //통신때문에 대입이 느려서(추측) 에러남. 그래서 스레드 안에서 대입함
        //2,3개씩 생기네
        if(mySmallBitmapImg != null) { //

            new Thread(new Runnable() {
                public void run() {

                    try {
                        Log.e(TAG, "마커합성bitmap :"+마커합성bitmap);
//                        Log.e(TAG, "mySmallBitmapImg :"+mySmallBitmapImg);
                        Log.e(TAG, "통신run() try.. StringToBitmap() 들어갈거임");
                        마커합성bitmap = StringToBitmap(mySmallBitmapImg); //string -> bitmap (<<에러났던 코드)
                        Log.e(TAG, "통신run() try.. URLtoBitmap() try퇴장함");

                    }catch(Exception e) {
                        Log.e(TAG, "URLtoBitmap() 에러");
                    }
                }
            }).start();
        }

        Log.e(TAG, "run() start() 다음코드");


        //ㅡㅡㅡㅡㅡㅡㅡ
        // 위치공유 버튼
        //ㅡㅡㅡㅡㅡㅡㅡ
        circle_btn_sharing.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                Log.e(TAG, "circle_btn_sharing onClick() myRoomActive : "+myRoomActive);

                isRun = false; //스레드 종료
                어플종료후_강제종료했는지판별(); //강제종료했으면 >> M_share_1, 방시간이 남아있으면 >> M_share_2_Map


                //ㅡㅡㅡㅡㅡ
                // 화면전환 : 1. 위치공유중 - 지도입장
                //            2. 위치공유중X - 방생성 또는 선택
                //ㅡㅡㅡㅡㅡ
                Intent intent;
                if(myRoomActive) { // 입장중 = true 이면

                    //방장인지 참여자인지 나누기
                    intent = new Intent(getApplicationContext(), M_share_2_Map.class); //바로 지도(위치공유중이던 방)으로 이동


                    //방장인지 참여자인지 나누기
                    if (iamLeader) {
                        intent = new Intent(getApplicationContext(), M_share_2_Map.class); //바로 지도(위치공유중이던 방)으로 이동

                    } else {
                        intent = new Intent(getApplicationContext(), M_share_3_join_Map.class); //바로 지도(위치공유중이던 방)으로 이동
                    }


                    //아마 여기에 더 추가될 내용이 있을듯
                    //방 찾아가는걸 여기에 넣어야될 것 같음

                } else {
                    intent = new Intent(getApplicationContext(), M_share_1.class); //방생성 or 참여 화면 띄우는 클래스
                }
                startActivity(intent);

            }
        });

        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        // 나의 공간 버튼
        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        navi_myPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), R_mypage.class);
                startActivity(intent);
                isRun = false; //스레드 종료
                Log.e(TAG, "navi_myPlace 클릭");
                Log.e(TAG, "M_main >>> R_mypage");
            }
        });


        //ㅡㅡㅡㅡㅡㅡ
        // 설정 버튼
        //ㅡㅡㅡㅡㅡㅡ
        navi_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), S_account.class);
                startActivity(intent);
                isRun = false; //스레드 종료
                Log.e(TAG, "navi_setting 클릭");
                Log.e(TAG, "startActivity >>> S_account.class");
            }
        });


    }


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ



    // 네이버 지도
    // NaverMap객체가 준비되면 콜백메소드 호출한다.
    // 이 함수가 다 끝나야 화면에 지도가 보임(캡쳐하다 앎)
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) { //지도의 작동을 다룸

        Log.e(TAG, "onMapReady() 입장");
        네이버Map = naverMap; //전역변수에 대입(스레드에서 쓰려고)


        //런타임 권한
        Log.e(TAG, "런타임 권한을 맵에 지정");
        ActivityCompat.requestPermissions(M_main.this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE); //현재위치 표시할 때 권한 확인


        //현재위치 GPS
        naverMap.setLocationSource(locationSource); //현재위치 <<<<<<<<<<<<<<<<<<< 작동 안 하는 것 같아
                                                    //원인 : 매개변수의 naverMap와 지역변수의 naverMap이 같지않다.
                                                    //지역변수 지도에 설정을 적용해줘야한다.(this.naverMap = naverMap;)
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
        uiSettings.setZoomControlEnabled(true); //+-버튼


        //ㅡㅡㅡㅡㅡㅡ
        // 마커 스레드 : n초마다 찍힘
        //ㅡㅡㅡㅡㅡㅡ
        위도경도Thread(5); //마커찍는 스레드 //메인에서 사진마커는 필용없음(test용이었고 실제는 위치공유방에서 사용할 메소드)
        네이버Map = naverMap; //네이버Map 변수에 다시 대입해서 온전하게 만듦
        //이러고 바깥으로 나오면 스레드가 캡쳐함



    } //~onMapReady() 지도관련은 다 여기 넣기


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void 어플종료후_강제종료했는지판별() {

        long 시작한시점 = stopWatch.getLong("시작한시점",0); //시작버튼 누른 시간
        long 주어진시간 = stopWatch.getLong("주어진시간",0); //고정된 값 ex.30초.. 값:30000
        long 현재시간 = System.currentTimeMillis(); // 똑같이 milliseconds로 나타남
        long millis종료된시점 = 시작한시점 + 주어진시간; // "1656558819121"

        Log.e("시작한시점 :", String.valueOf(시작한시점));
        Log.e("주어진시간 :", String.valueOf(주어진시간));
        Log.e("현재시간 :", String.valueOf(현재시간));

        //강제종료 됐는지 알 수 있는 원리
        //시작o    여기?   주어진시간o    현재o   여기?
        //시작+주어진 시간이 현재보다 적으면 강퇴였네! 입장x
        //10분+30초  <  12분  =  입장중x


        //날짜 String으로 알아내기
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        Date date종료된시점 = new Date(millis종료된시점); // ms -> Date
        String str종료된시점 = sdf.format(date종료된시점); // Date -> String

        Log.e("date종료된시점 :", String.valueOf(date종료된시점));
        Log.e("str종료된시점 :", str종료된시점);


        //삭제되지않은 시간들이 있다 == 어플종료되어서 쉐어드에 삭제가 안되었다. 그러므로 이미끝난등산방이 있는 상태다.
        if(시작한시점 != 0 && 주어진시간 != 0 && 시작한시점 + 주어진시간 < 현재시간) { //강제퇴장된거면 다 변수들도 퇴장처리하기. 그러면 밑에서 알아서 입장할 곳 변별해줌

            Log.e(TAG, "시작한시점 + 주어진시간 < 현재시간 ");
            retrofit_퇴장업뎃(str종료된시점, 시분초변환(주어진시간)); //언제 방이 종료됐는지 모르니까, 내가 종료된 시간 = 방이 끝난 시간...으로 대입한다
            방퇴장처리();

        }

    }

    private String 시분초변환(long time) {

        long 초 = 1000;

        long h = (int) (time / (3600 * 초));
        long m = (int) (time - h * (3600 * 초)) / (60 * 초);
        long s = (int) (time - h * (3600 * 초) - m * (60 * 초)) / 초;

        //"00시00분14초"가 지저분해보여서 0시 0분이면 안보이기
        String h시간m분s초 = (h > 0 ? h + "시간 " : "") + (m > 0 ? m + "분 " : "") + (s > 0 ? s + "초" : ""); //삼항연산자    조건 ? 참 : 거짓

        return h시간m분s초;
    }


    //어플종료 후 들어갔는데 이미 주어진시간이 지나버려서 방이 없어져있을 때 서버에 종료된 시간, 소요시간 보내기
    private void retrofit_퇴장업뎃(String finishDate, String leadTime) {

        //이벤트 : 어플종료 후 들어갔는데 이미 주어진시간(1시간..12시간...등)이 지나버려서 방이 없어져있을 때
        //보낼 값 : 방번호, 이메일, 소요시간("1시간3분22초")
        //결과 : Room에서 방번호로 끝난시간 추출해서 RoomUser에 업뎃, 소요시간 업뎃
        //응답 : sucess

        Log.e(TAG, "retrofit_퇴장업뎃() 입장 &&&&&&&&&&&& ");

        //레트로핏 객체 생성, 빌드
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder() //Retrofit 인스턴스 생성
                .baseUrl("http://15.164.129.103/")  //baseUrl 등록
                .addConverterFactory(GsonConverterFactory.create())  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
                .build();

        //변수값 확인
        Log.e(TAG, "변수값 확인 : "
                    + "\n myRoom_no : " + myRoom_no
                    + "\n myEmail : " + myEmail
                    + "\n finishDate : " + finishDate
                    + "\n leadTime : " + leadTime
        );


        //ㅡㅡㅡㅡㅡ
        // 방 종료 : 끝나서 방에 입장안된다. 시간들, 명단 업뎃
        //ㅡㅡㅡㅡㅡ
        Sharing SharingRoomCreate = retrofit.create(Sharing.class);   // 레트로핏 인터페이스 객체 구현
        Call<Sharing_room> call = SharingRoomCreate.alreadyFinishTracking(myRoom_no, myEmail, finishDate ,leadTime); //보냄 : 방번호로 특정하여 db에 현재시간 삽입하기



        //네트워킹 시도 2
        call.enqueue(new Callback<Sharing_room>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Sharing_room> call, Response<Sharing_room> response) {

                Sharing_room result = response.body();
                assert result != null;
                Log.e(TAG, "성공인가 : "+result.getResponse()); //응답값은 db에 잘 들어갔는지 확인만하기. 사용할 일은 없음

            }

            @Override
            public void onFailure(Call<Sharing_room> call, Throwable t) {
                Log.e("onFailure : ", t.getMessage());
            }
        });

    }



    private void 방퇴장처리() {


        //퇴장할 때 같이 마커도 지워줘야 새로 방 만들었을 때 이전방의 경로가 찍히는 불상사를 방지한다.
        마커리스트.clear();
        마커위도리스트.clear();
        마커경도리스트.clear();
        Log.e(TAG, "마커리스트[] : "+마커리스트);

        //변수도 비우기
        방이름 = null;
        방비번 = null;

        //쉐어드 비우기 1 : 스태틱 변수
        SharedPreferences shared = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능
        SharedPreferences.Editor Editor = shared.edit();

        Editor.putBoolean("myRoomActive", false); //이러면 강제종료 후 방이 터지고 다시 못들어오겠지
        Editor.putString("myRoom_no", null);

        Editor.apply(); //실질 저장

        Boolean 불린 = shared.getBoolean("myRoomActive",false);
        myRoomActive = 불린;
        Log.e(TAG, "불린 : "+불린);
        Log.e(TAG, "myRoomActive : "+myRoomActive);


        //쉐어드 비우기 2 : 스탑와치
        스탑와치editor.clear(); //다 지워버려서 새로 입장하도록 함
        스탑와치editor.apply(); //저장

        Log.e("쉐어드 time : ", "삭제!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    }


    //실시간으로 나의 위치를 위경도를 통해 마커로 찍는 스레드
    public void 위도경도Thread(int sec) { //화면전환될 때 종료시킴

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 실시간 나의 위치 마커 : 나의 현재 위치를 이미지마커로 표현(n초마다)
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        Marker marker = new Marker(); //마커객체를 반복문 바깥에 위치


        //ㅡㅡㅡㅡㅡㅡㅡ
        // 마커중복 방지 : 왜냐면 내 위치는 1개여야 되는데 onRestart()들어갈 때 중복됨
        //ㅡㅡㅡㅡㅡㅡㅡ
        // 주의 : 배열의 맨 마지막 마커가 스레드에서 동작함

        arr마커1개만존재.add(marker); //배열에 마커 1개만 존재해야 함

        Log.e(TAG, "arr마커1개만존재 : "+arr마커1개만존재);
        Log.e(TAG, "arr마커1개만존재.size()) : "+arr마커1개만존재.size());


        if(arr마커1개만존재.size() > 1) { //배열에 2,3..개 있으면

            Marker 마지막마커 = arr마커1개만존재.get(arr마커1개만존재.size()-1); //배열의 맨 마지막 마커가 스레드에서 동작함
            Log.e(TAG, "마지막마커 : "+마지막마커);

            for (int i=0; i<arr마커1개만존재.size(); i++) { //만약 3개가 찍혀있다?

                arr마커1개만존재.get(i).setMap(null); //배열에 들어간 마커 전부에 null넣어서 안 보이게 하기 /어차피 스레드에도 1초안에 set해주니까 ㄱㅊ
                Log.e(TAG, "arr마커1개만존재.get(i).setMap(null) : " + i );
            }

            arr마커1개만존재.clear();
            arr마커1개만존재.add(마지막마커); //1개를 넣어줘야 1개 이상일 때 들어오는 if문에 안 들어옴

            Log.e(TAG, "arr마커1개만존재 : "+arr마커1개만존재);
            Log.e(TAG, "arr마커1개만존재.size() : "+arr마커1개만존재.size());

        }


        Log.e(TAG, "marker 객체생성");
//        marker.setWidth(120); //가로, 조금 더 크게하거나 유지
//        marker.setHeight(110); //세로 <<<< 이거때문에 사이즈가 정해져있었음
//                marker.setIcon(MarkerIcons.BLACK);
//                marker.setIconTintColor(Color.RED);
//                marker.setIcon(OverlayImage.fromBitmap(result)); //result값이 스레드에 의해 갱신됨
        Log.e(TAG, "marker.setIcon 저 이미지로 마커를 찍겠다/result넣음");


        //서브스레드 객체 -> 핸들러 -> 메세지큐 -> 루퍼 -> 핸들러 -> 메인메서드 객체 전달 완료
        //그러면 Runnable 객체가 Message Queue에 순서대로 전달되고
        //내가 구현하진 않았지만 루퍼가 자동으로 순서대로 꺼내어 핸들러에게 전달
        //핸들러가 메인스레드에 전달한다.(받기, 건네기 두 역할)
        //전달된 Runnable객체는 메인 스레드에서 실행된다.
        //안드로이드는 UI접근을 메인스레드에서만 가능하게 했다.
        //그러므로 Runnable객체 안에 UI접근코드를 넣으면 서브스레드에서도 접근된다.
        new Thread(new Runnable() {

            @Override
            public void run() { //1초마다 벨류값 1씩 증가시키는 스레드임

                while ((isRun)) { //false가 되면 멈춤 - 화면전환시

                    handler2.post(new Runnable() { //post : 다른 스레드로 메세지(객체)를 전달하는 함수

                       @Override
                        public void run() { //마커의 위치만 변경

                            marker.setMap(null);
                            marker.setPosition(new LatLng(위도, 경도)); //먼저
                            marker.setMap(네이버Map);

                           Log.e(TAG, "ww위도 : "+위도+", 경도 : "+경도); // << 마커와 중복방지배열에 담긴 마커가 동일해야 함
                           Log.e(TAG, "marker : "+marker); // << 마커와 중복방지배열에 담긴 마커가 동일해야 함


                            if(마커합성bitmap != null) { //비트맵 값 있으면 그대로 삽입
                                Log.e(TAG, "지도run() if(마커합성bitmap != null)");
                                marker.setIcon(OverlayImage.fromBitmap(마커합성bitmap)); //통신할 때 마커까지 업데이트됨

                            } else if(mySmallBitmapImg != null) { //위에서스레드로 마커합성bitmap대입해서 여기는 한 두번만 들어올거임
                                Log.e(TAG, "지도run() else if(mySmallBitmapImg != null)");
                                marker.setIcon(OverlayImage.fromBitmap(StringToBitmap(mySmallBitmapImg))); //통신할 때 마커까지 업데이트됨

                            } else {
                                Log.e(TAG, "지도run() else "); //에러표시
                                marker.setIcon(OverlayImage.fromResource(R.drawable.hiking)); //통신할 때 마커까지 업데이트됨
                            }
                        }

                    });
                    try {
                        Thread.sleep(1000*sec);
                    } catch (Exception e) {
                    }
                }
            }
        }).start(); //start()붙이면 바로실행시킨다.
    }



    //비트맵사진 편집(정사각, 크기조절, 원형, 합성)
    public Bitmap bitmapEdit(Bitmap bitmap2) {

        //정사각형 만들기
        assert bitmap2 != null : "null값이다";
        Bitmap 마커이미지 = BitmapFactory.decodeResource(getResources(), R.drawable.pin_red);  // first image(빨간마커이미지)
        bitmap2 = cropBitmap(bitmap2); //사용자 사진

        //가로 xxx으로 크기조절
        마커이미지 = resizeBitmap(마커이미지, 180); //마커 가로세로를 고정해놔서 사이즈가 안 커진거였음
        bitmap2 = resizeBitmap(bitmap2, 100);

        //원형으로 자르기
        bitmap2 = cropCircle(bitmap2);

        //하나의 비트맵을 만듦
        Bitmap result = Bitmap.createBitmap(180
                , 200
                , Bitmap.Config.ARGB_8888); //getConfig()?

        Log.e(TAG, "마커이미지.getHeight() + 마커이미지.getHeight() : "+마커이미지.getWidth()+", "+마커이미지.getHeight());
        Log.e(TAG, "bitmap2.getHeight() + bitmap2.getHeight() : "+bitmap2.getWidth()+", "+bitmap2.getHeight());

        //캔버스를 통해 둘을 겹친다.
        Canvas canvas = new Canvas(result); //<< result를 사용할거임
        canvas.drawBitmap(마커이미지, new Matrix(), null); // (bitmap, 매트릭스, paint)
        canvas.drawBitmap(bitmap2, 40, 8, null); //기존 55,45

        Log.e(TAG, "Canvas를 이용해 마커 이미지 합성함");

        return result;
    }


    //비트맵사진 편집(크기조절, 합성)
    public Bitmap bitmapEdit2_기본이미지(Bitmap bitmap2) { //이미 리소스가 원형이라 정사각,원형편집 제외

        //정사각형 만들기
        assert bitmap2 != null : "null값이다";
        Bitmap 마커이미지 = BitmapFactory.decodeResource(getResources(), R.drawable.pin_red);  // first image(빨간마커이미지)

        //가로 xxx으로 크기조절
        마커이미지 = resizeBitmap(마커이미지, 180); //마커 가로세로를 고정해놔서 사이즈가 안 커진거였음
        bitmap2 = resizeBitmap(bitmap2, 105); //기본이미지 사이즈


        //하나의 비트맵을 만듦
        Bitmap result = Bitmap.createBitmap(180
                , 200
                , Bitmap.Config.ARGB_8888); //getConfig()?

        Log.e(TAG, "마커이미지.getHeight() + 마커이미지.getHeight() : "+마커이미지.getWidth()+", "+마커이미지.getHeight());
        Log.e(TAG, "bitmap2.getHeight() + bitmap2.getHeight() : "+bitmap2.getWidth()+", "+bitmap2.getHeight());

        //캔버스를 통해 둘을 겹친다.
        Canvas canvas = new Canvas(result); //<< result를 사용할거임
        canvas.drawBitmap(마커이미지, new Matrix(), null); // (bitmap, 매트릭스, paint)
        canvas.drawBitmap(bitmap2, 37, 5, null); //55,55

        Log.e(TAG, "Canvas를 이용해 마커 이미지 합성함");

        return result;
    }


    //비트맵 원형으로 자르기(정사각형 선행)
    public Bitmap cropCircle(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        int size = (bitmap.getWidth() / 2);
        canvas.drawCircle(size, size, size, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    // 가로길이 200으로 맞춰 이미지 크기 조절
    static public Bitmap resizeBitmap(Bitmap original, int resizeWidth) {

        double aspectRatio = (double) original.getHeight() / (double) original.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(original, resizeWidth, targetHeight, false);
        if (result != original) {
            original.recycle();
        }
        return result;
    }

    public Bitmap cropBitmap(Bitmap original) {

        Log.e(TAG, "cropBitmap() 들어옴");
        Bitmap result = null;
//        Log.e(TAG, "original.getHeight() : "+original.getHeight());
//        Log.e(TAG, "original.getWidth() : "+original.getWidth());

        if(original.getHeight() <= original.getWidth()) { //가로가 높이보다 작거나 같다면 세로에 맞추기

            result = Bitmap.createBitmap(original
                    , original.getWidth() / 4 //X 시작위치 (원본의 4/1지점)
                    , original.getHeight() / 4 //Y 시작위치 (원본의 4/1지점)
                    , original.getHeight() / 2 // 넓이 (원본의 절반 크기)
                    , original.getHeight() / 2); // 높이 (원본의 절반 크기)

        } else { //가로에 맞추기
            result = Bitmap.createBitmap(original
                    , original.getWidth() / 4 //X 시작위치 (원본의 4/1지점)
                    , original.getHeight() / 4 //Y 시작위치 (원본의 4/1지점)
                    , original.getWidth() / 2 // 넓이 (원본의 절반 크기)
                    , original.getWidth() / 2); // 높이 (원본의 절반 크기)
        }

        if (result != original) {
            original.recycle();
        }
        return result;
    }


    //런타임 권한요청 결과를 여기에 전달
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG, "onRequestPermissionsResult() 입장");

        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            Log.e(TAG, "onRequestPermissionsResult() .. if() 입장");

            if (!locationSource.isActivated()) { //권한 거부됨
                Log.e(TAG, "onRequestPermissionsResult()의 .. if() 입장22");
//                naverMap.setLocationTrackingMode(LocationTrackingMode.None);

            } else { //권한 수락
                Log.e(TAG, "onRequestPermissionsResult()의 .. else() 입장");

            }
        }
    }


    // url -> bitmap
    // 주의!! stream사용 시 스레드 사용하기
    static public Bitmap URLtoBitmap(String url) {


        String TAG = "URLtoBitmap()";
        Log.e(TAG, "URLtoBitmap() 입장");

        try{
            Log.e(TAG, "URLtoBitmap() try입장");
            URL imgUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true); //url로 input받는 flag 허용
            connection.connect(); //연결
            InputStream is = connection.getInputStream(); // get inputstream
            Bitmap retBitmap = BitmapFactory.decodeStream(is);

            return retBitmap; //얻고자하는 값

        }catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // string -> bitmap
    static Bitmap StringToBitmap(String encodedString) { //마커에 사진삽입하려면 bitmap형태여야함
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    //                                           생명주기
    // (필수 : 네이버 지도 api에서 Mapview를 사용하려면 아래 생명주기를 생성하라 함. 이유는 말 안 함)
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");

    }

    @Override
    protected void onRestart() { //다른액티비티 back하면 여기로 옴
        super.onRestart();
        Log.e(TAG, "onRestart ~~~~");
        isRun = true; //스레드 시작\
        위도경도Thread(5); //마커찍는 스레드, 위치 다시 찾지 않는다.

        //a에서 b액티비티로 이동후 back하면 onRestart()로 들어옴
        //b에서 a로 다시 누르면 onCreate로 감
        //그래서 두 곳은 중복되지 않아서 둘 다 스레드를 위치
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onPause");
        isRun = false; //스레드 시작\
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e(TAG, "onLowMemory");
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @Override
    public void onBackPressed() {
//        super.onBackPressed(); //상속받지 말고 기존의 뒤로가기 버튼을 제거해줘야되는구나
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 1500) { //연타가 1.5초
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        // 1.5초 이내에 뒤로가기 버튼을 한번 더 클릭시 finish()(앱 종료)
        if (System.currentTimeMillis() <= backKeyPressedTime + 1500) {
            finish();
        }
    }

    private void ID() {

        navi_setting = (LinearLayout) findViewById(R.id.navi_setting);
        navi_myPlace = (LinearLayout) findViewById(R.id.navi_myPlace);
        navi_map = (LinearLayout) findViewById(R.id.navi_map);
        mapView = findViewById(R.id.map);
        circle_btn_sharing = (ImageView)findViewById(R.id.circle_btn_sharing);
    }



}
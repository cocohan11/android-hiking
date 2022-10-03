package com.example.iamhere;

import static com.example.iamhere.M_share_2_Map.retrofit객체;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iamhere.Interface.Login;
import com.example.iamhere.Interface.Sharing;
import com.example.iamhere.Interface.kakaoLogin;
import com.example.iamhere.Model.Login_find;
import com.example.iamhere.Model.Sharing_room;
import com.example.iamhere.socket.LocationService;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.AccessTokenInfo;
import com.kakao.sdk.user.model.User;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class L_login extends AppCompatActivity {


    //본인정보
    static public String myEmail; //일반, 카카오회원 둘 다 해당
    static public String myPw;  //일반회원만 있는 값
    static public String myDate; //가입일
    static public String myName; //닉네임
    static public String strNickName; //닉넴입력값(로드느려서 임시적으로 사용)
    static public String mysnsLogin; //카카오 로그인중인지
    //위치공유방(방장) 진입 단계 : 1.방이름지음  2.경로찍는화면  3.시작화면(방생성완)  4.등산중
    static public boolean myRoomActive; //위치공유중인지. 위치공유중이라면 나가더라도 방재입장, 아니라면 새로 방 입장화면 //M_share_1_1create에서 대입
    static public boolean myRoomhiking;
    static public String myRoom_no; //위치공유중 + 등산중 /재입장시 4단계로 진입하기
    static public boolean iamLeader; //방장인지
    //사진
    static public String myImg; //http... 일반,카카오 둘다
    static public String myMarkerImg; // 프로필 변경 후 대입됨
    static public String originFile; //원본사진 경로(로드느려서 임시적으로도 사용하려고 static선언) >>  /storage/emulated/0...
    static public String mySmallBitmapImg; //마커합성사진(String형태로 쉐어드 저장) >> @213#RR@FRr4VD...
    static public Bitmap 마커합성bitmap; //마커합성사진(Bitmap형태), 통신할 때 마커까지 업데이트됨(사용하려고보니 업뎃어려워서)
    static public Bitmap bitmapCapture; //경로지정후 지도캡쳐
    //지도
    static public double 위도; // 37.5670135 /M_share_2_Map에서 더 빠름
    static public double 경도; // 126.9783740
    static public String 방이름; //자주쓸건데 쉐어드나 인텐트로하기 번거로워서
    static public String 방비번; //공유하기할 때도 쓸듯
    static public ArrayList<Marker> 마커리스트 = new ArrayList<>(); //array(배열)을 쓰지 않은 이유: 삭제시 인덱스가 당겨지지 않아서 빈 자리가 생김
    static public ArrayList<Double> 마커위도리스트 = new ArrayList<>(); //재입장하면 다 날라가버려서 static변수로 넣음
    static public ArrayList<Double> 마커경도리스트 = new ArrayList<>(); //2차배열이 제일 좋긴한데 retrofit으로할거고, 할 수 있는 만큼 속도내자

    // 채팅
    static public final int 모든위치업뎃_sec = 10;
    static public String 소켓통신목적=""; // 입장/위치/채팅/강제종료/퇴장
    static public String h시간m분s초; //서버에 보낼 시간분초 문자열. 조회할 때 조작없이 바로 보여질 데이터다

//    static boolean isRun = true; // 위치공유방 마커 스레드 멈추기용도 - 화면전환시
    private String TAG = "L_login";
    private EditText et_email, et_pw;
    private Button btn_doLogin;
    private TextView tv_pw_reissue;
    private long backKeyPressedTime = 0; //뒤로가기 2번 종료
    //카카오 로그인 api
    private ImageView img_kakaoLogin;
    private TextView tv_goJoin;
    private WebView wView;      // 웹뷰
    private boolean bitmapImg; //쉐어드값 확인
    private boolean BoolImg; //값유무확인용
    private String 카톡토큰; //스플래시 판별용
    private retrofit2.Retrofit retrofit;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"onCreate");
        Log.e(TAG,"UserApiClient.getInstance() : "+UserApiClient.getInstance());


        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 시작하자마자 실행할 것들
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        쉐어드초기화();
        retrofit = retrofit객체();

        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        // 카카오 로그인 : 콜백함수
        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) { //완료를 눌러야 실행됨
                if(oAuthToken != null) {
                    //로그인이 되었을 때 처리해야할 일
                    Log.e(TAG, "oAuthToken :"+oAuthToken);
                    Log.e(TAG, "oAuthToken != null ...로그인이 되었을 때 처리해야할 일");
                }
                if( throwable != null) {
                    //오류가 나왔을 떄 처리해야할 일
                    Log.e(TAG, "throwable != null ...오류가 나왔을 떄 처리해야할 일");
                }
                Log.e(TAG, "updateKakaoLoginUi()직전 callback");
                updateKakaoLoginUi(retrofit);
                Log.e(TAG, "invoke()안의 updateKakaoLoginUi() 끝");
                return null;
            }
        };

        //카톡공유 입장 판별용 변수
        Uri uri = getIntent().getData();
        // 템플릿에서 만든 공통링크를 인텐트로 받아와서 uri형태에서 파라미터를 끄집어낸다.
        // 매니페스트에 여기로 바로 입장하라고 설정했음

        Log.e(TAG, "카톡공유 getIntent" +
                "\ngetIntent() " + getIntent() +
                "\nuri " + uri
        );

        //스플래시인지 로그인레이아웃인지 구분
        String 로그아웃했는가 = getIntent().getStringExtra("로그아웃");
        if(로그아웃했는가 == null) 로그아웃했는가 = "";



        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 2가지 레이아웃 적용
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        if(로그아웃했는가.equals("로그아웃")) { //로그아웃 한 상태면 로그인 화면 보이기


            setContentView(R.layout.activity_llogin); //기다림 없이 바로 로그인 화면


            //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
            // 경우3. 로그아웃 : 스플래시 X
            //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
            Log.e(TAG,"들어왓냐아3 경우3. 로그아웃");
            ID();  //setContentView와 한 쌍
            버튼이벤트(retrofit, callback);
            Log.e(TAG, "로그아웃했는가 : "+로그아웃했는가);



        } else { //입장하는 상황이면 스플래시 보이기


            setContentView(R.layout.activity_splash); //3초 기다리는 화면


            //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
            // 1초 뒤에 실행할 코드 : 화면전환
            //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //3초 뒤에 실행할 코드
                    //거의 전부
                    Log.e(TAG, "run() 들어오오옴");


                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                    // 자동로그인 (카톡/로컬) + (카톡공유/ X )
                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                    UserApiClient.getInstance().accessTokenInfo(new Function2<AccessTokenInfo, Throwable, Unit>() {
                        @Override
                        public Unit invoke(AccessTokenInfo accessTokenInfo, Throwable throwable) {

                            Log.e(TAG, "3 토큰 확인" +
                                    "\n3 accessTokenInfo " + accessTokenInfo +
                                    "\n3 throwable " + throwable
                            );

                            if (accessTokenInfo != null) 카톡토큰 = String.valueOf(accessTokenInfo); //스플래시 판별용

                            //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                            // 경우1. 카톡자동로그인 / 경우2. 로컬자동로그인
                            //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                            if (myEmail != null && 카톡토큰 != null) { //쉐어드 로그인 기록 있다 + 카톡

                                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                                // 경우1-1. 카톡 공유받아서 입장 : 카톡자동로그인이 된 상태
                                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                                if (uri != null) {

                                    Log.e(TAG, "들어왓냐아1 경우1-1. 카톡자동로그인 + 카톡공유 입장");

                                    String 방이름 = uri.getQueryParameter("roomName"); //key값으로 한글이 못 온다.. 주의하기
                                    String 방비번 = uri.getQueryParameter("roomPW"); //바로 스태틱에 대입 안 함. 왜냐면 유효하지 않은 방일 수도 있으니까


                                    //서버에 유효한 방인지 응답받기
                                    retrofit유효한방묻기(방이름, 방비번); //응답 : "1"이어야 함
                                    //유효한 방 O >> 쉐어드저장, 스태틱변수 대입, 지도로 이동
                                    //유효한 방 X >> M_main이동

                                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                                    // 경우1-2. 기존 카톡자동로그인
                                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                                } else { // uri == null

                                    Log.e(TAG, "들어왓냐아2 경우1-2. 카톡자동로그인");
                                    카톡moveMain(); //메인지도로 이동

                                }

                            } else if (myEmail != null && 카톡토큰 == null) { //쉐어드 로그인 기록 있다


                                // 로컬자동로그인 > 카톡 공유
                                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                                // 경우2-1. 카톡 공유받아서 입장
                                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                                if (uri != null) { //getIntent()는 항상 값이 있음. 왜냐면 첫 시작을 여기로 하겠다라고 인텐트필터선언했음. 그래서 uri로 구분

                                    Log.e(TAG, "들어왓냐아3 경우2-1. 로컬자동로그인 + 카톡공유 입장");

                                    String 방이름 = uri.getQueryParameter("roomName"); //key값으로 한글이 못 온다.. 주의하기
                                    String 방비번 = uri.getQueryParameter("roomPW"); //바로 스태틱에 대입 안 함. 왜냐면 유효하지 않은 방일 수도 있으니까


                                    //서버에 유효한 방인지 응답받기
                                    retrofit유효한방묻기(방이름, 방비번); //응답 : "1"이어야 함
                                    //유효한 방 O >> 쉐어드저장, 스태틱변수 대입, 지도로 이동
                                    //유효한 방 X >> M_main이동

                                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                                    // 경우2-2. 기존 로컬자동로그인
                                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                                } else { // uri == null : 카톡공유입장 X

                                    Log.e(TAG, "들어왓냐아4 경우2-2. 로컬자동로그인");
                                    retrofit로컬로그인(retrofit, myEmail, myPw); //서버에서 값 가져옴 + 화면전환

                                }
                            }
                            return null;
                        }
                    });


                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                    // 경우4. 일반 로그인 화면 : 로그아웃x 카톡자동x 로컬자동x
                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                    if(카톡토큰==null && myEmail == null) {


                        setContentView(R.layout.activity_llogin); //스플래시 -> 로그인화면


                        ID(); //activity_llogin와 한 쌍
                        버튼이벤트(retrofit, callback);

                        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                        // 경우4-1. 일반로그인 + 카톡공유 입장 : "로그인 후 이용"
                        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                        if (uri != null) {

                            Log.e(TAG, "들어왓냐아5 경우4-1. 일반로그인 + 카톡공유 입장");

                            dialog로그인후이용(); //"로그인 후 이용해 주시기 바랍니다."

                        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                        // 경우4-2.일반로그인 : 태초상태
                        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                        } else { // uri == null
                            Log.e(TAG, "들어왓냐아6 경우4-2. 일반로그인");
                        }

                    }



                } //~run()
            }, 1000 * 1); // 3초 정도 딜레이를 준 후 시작

        } //~3개의 경우의 수

    } //~onCreate()

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


    private void retrofit유효한방묻기(String room이름,String room비번) { //리사이클러뷰에서 보내온 방번호를 서버에 키값으로 보낸다.


        Log.e(TAG,"카톡공유 변수 확인" +
                "\nretrofit유효한방묻기() 메소드"+
                "\n방이름() "+room이름+
                "\n방비번 "+room비번
        );


        Sharing retrofit참여자입장 = retrofit객체().create(Sharing.class); //Sharing에서 구현한 인터페이스로 요청보낸다.
        Call<Sharing_room> call = retrofit참여자입장.joinRoom(room이름, room비번, myEmail); //방이름과 비번이 일치하면 입장한다.


        //네트워킹 시도
        call.enqueue(new Callback<Sharing_room>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Sharing_room> call, Response<Sharing_room> response) { //응답은 성공인지만 안다.

                Log.e(TAG, "response " + response);
                Log.e(TAG, "response.body() " + response.body());
                Log.e(TAG, "response.body().getResponse() " + response.body().getResponse());


                //유효한 방이 존재하면 위치공유방 지도로 이동(참여)
                if(!response.body().getResponse().equals("1개가아님")) { //해당하는 방 갯수가 1개 존재한다면(유일하다면)

                    Log.e(TAG, "입장 가능");
                    Toast.makeText(getApplicationContext(), "입장 가능. 지도로 이동", Toast.LENGTH_SHORT).show();

                    //쉐어드 저장 + 스태틱 변수대입
                    myRoom_no = response.body().getResponse(); //방번호 응답받음
                    방이름 = room이름;
                    방비번 = room비번;
                    shared카톡공유입장(room이름, room비번); //변수대입+쉐어드 저장은 한 쌍


                    //방장인지, 참여자인지에 따라 가는 곳이 다름
                    Log.e(TAG, "방장이면 2, 참여자면 3지도로 이동 iamLeader : "+iamLeader);
                    if (iamLeader) {

                        //방장은 M_share_2_Map 위치공유방에 입장
                        Intent intent = new Intent(L_login.this, M_share_2_Map.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //현재화면빼고 아래액티비티 지움
                        startActivity(intent);
                        Log.e(TAG, "startActivity()");

                    } else { //참여자라면 M_share_3_join_Map 위치공유방에 입장

                        //위치공유방 지도로 이동
                        Intent intent = new Intent(L_login.this, M_share_3_join_Map.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //현재화면빼고 아래액티비티 지움
                        startActivity(intent);
                        Log.e(TAG, "startActivity()");

                    }


                } else {

                    Log.e(TAG, "입장 불가능.. 없어진 방이다.");
                    Toast.makeText(getApplicationContext(), "입장 불가능.. 없어진 방", Toast.LENGTH_SHORT).show();


                    dialog없어진방입니다(); //이미 없어진 방이라 입장 못한다. 안내만.

                }

            }

            @Override
            public void onFailure(Call<Sharing_room> call, Throwable t) {
                Log.e("onFailure... : ", t.getMessage());
            }
        });

    }

    private void dialog로그인후이용() {

        //이미 없어진 방이라 입장 못한다. 안내만.
        new AlertDialog.Builder(L_login.this) // 현재 Activity의 이름 입력.
                .setMessage("로그인 후 이용해 주시기 바랍니다.")     // 해^띄어쓰기^주세요가 표준이지만 붙여도 허용한다 함
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {      // 버튼1 (직접 작성)
                    public void onClick(DialogInterface dialog, int which){

                        Log.e(TAG, "onClick누르던 안 누르던 M_main으로 이동. 그냥 안내만 함");

                    }
                })
                .setCancelable(false) //대화상자 바깥눌러도 못 닫게하기(안 그러면 빈 화면만 뜸)
                .show();

    }


    private void dialog없어진방입니다() {

        //이미 없어진 방이라 입장 못한다. 안내만.
        new AlertDialog.Builder(L_login.this) // 현재 Activity의 이름 입력.
                .setMessage("없어진 방입니다. 새로 입장해 주세요")     // 해^띄어쓰기^주세요가 표준이지만 붙여도 허용한다 함
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {      // 버튼1 (직접 작성)
                    public void onClick(DialogInterface dialog, int which){

                        Log.e(TAG, "onClick누르던 안 누르던 M_main으로 이동. 그냥 안내만 함");


                        //메인 지도로 이동(로그인 한 상태)
                        Intent intent = new Intent(L_login.this, M_main.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //현재화면빼고 아래액티비티 지움
                        startActivity(intent);
                        Log.e(TAG, "startActivity()");

                    }
                })
                .setCancelable(false) //대화상자 바깥눌러도 못 닫게하기(안 그러면 빈 화면만 뜸)
                .show();

    }


    public void shared카톡공유입장(String 방이름, String 방비번) {

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능
        SharedPreferences.Editor autoLoginEdit = auto.edit();

        autoLoginEdit.putString("방이름", 방이름);
        autoLoginEdit.putString("방비번", 방비번);

        autoLoginEdit.apply(); //실질 저장
    }

    private void 버튼이벤트(retrofit2.Retrofit retrofit, Function2<OAuthToken, Throwable, Unit> callback) {

        img_kakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //카톡 계정기입으로만 로그인 하기

                Log.e(TAG,"버튼클릭");
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(L_login.this)){ //카톡설치o
                    Log.e(TAG,"isKakaoTalkLoginAvailable()는 true");
                    UserApiClient.getInstance().loginWithKakaoTalk(L_login.this, callback); //크롬창띄우기(아마 어플다운받으라는 곳인듯?)
                    Log.e(TAG,"isKakaoTalkLoginAvailable()는 true 끝");

                } else { //카톡설치x
                    //여기가 실행안됨; 매니패스트 인가코드받는 작업수정 후 동작
                    Log.e(TAG,"isKakaoTalkLoginAvailable()는 false");
                    UserApiClient.getInstance().loginWithKakaoAccount(getApplicationContext(), callback); //계정로그인 창띄우기
                    updateKakaoLoginUi(retrofit);
                    Log.e(TAG,"isKakaoTalkLoginAvailable()는 false 끝");
                }
            }
        });

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 로컬 로그인 버튼 : 클릭시 서버에 email,pw가 요청보내지고 서버에서 지정된 응답을 받음
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        btn_doLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str_email = String.valueOf(et_email.getText()); //서버로 보낼 email
                String str_pw = String.valueOf(et_pw.getText()); //서버로 보낼 pw
                Log.e(TAG,"버튼클릭");
                Log.e(TAG, "에딧텍스트 이메일 (str_email) : "+str_email); //ok
                Log.e(TAG, "에딧텍스트 이메일 (str_pw) : "+str_pw); //ok


                retrofit로컬로그인(retrofit, str_email, str_pw); //서버에서 값 가져옴


            }
        });//~로그인 버튼

        //ㅡㅡㅡㅡㅡㅡㅡ
        // 회원가입 버튼
        //ㅡㅡㅡㅡㅡㅡㅡ
        tv_goJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "tv_goJoin 클릭");
                Intent intent = new Intent(getApplicationContext(), L_join.class);
                startActivity(intent);
                Log.e(TAG, "startActivity()");
            }
        });

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 비밀번호 재발급 버튼
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        tv_pw_reissue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "비밀번호를 잊었어요", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), L_PwReissue.class); // 로그인 >> 비밀번호분실
                startActivity(intent);
                Log.e(TAG, "startActivity()");
            }
        });
    }


//    public void splash(int sec, retrofit2.Retrofit retrofit, Function2<OAuthToken, Throwable, Unit> callback) {
//
//
//        setContentView(R.layout.activity_splash); // 이 상태에서 n초 딜레이를 준 후 run()시작
//
//        new Handler().postDelayed(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                Log.e(TAG, "run() 들어오오옴");
//                setContentView(R.layout.activity_llogin);
//                ID();
//                버튼이벤트(retrofit, callback);
//
//            }
//        }, 1000 * sec); // sec초 정도 딜레이를 준 후 시작
//
//    }

//    private void 카톡moveMain(int sec) {
//
//        Log.e(TAG, "moveMain() 시작");
//        setContentView(R.layout.activity_splash);
//
//        new Handler().postDelayed(new Runnable() //UI를 건드는거니까 핸들러 사용
//        {
//            @Override
//            public void run()
//            {
//                Intent intent = new Intent(getApplicationContext(), M_main.class); //메인 지도로 이동
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //현재화면빼고 아래액티비티 지움
//                Log.e(TAG, "Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK");
//                startActivity(intent);
//                Log.e(TAG, "startActivity()");
//                finish();
//            }
//        }, 1000 * sec); // sec초 정도 딜레이를 준 후 시작
//
//        Log.e(TAG, "moveMain() 끝");
//
//    } // 참고) thread.sleep 으로 delay를 주면 layout 로딩도 같이 멈추므로 n초 후 실행되는 postDelayed를 이용


    private void 카톡moveMain() {

        Intent intent = new Intent(getApplicationContext(), M_main.class); //메인 지도로 이동
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //현재화면빼고 아래액티비티 지움
        Log.e(TAG, "Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK");
        startActivity(intent);
        Log.e(TAG, "startActivity()");

    } // 참고) thread.sleep 으로 delay를 주면 layout 로딩도 같이 멈추므로 n초 후 실행되는 postDelayed를 이용


    private void 로컬moveMain(int sec, retrofit2.Retrofit retrofit) { // 조건

        Log.e(TAG, "로컬moveMain() 입장");

    }

//    private void 로컬moveMain(int sec, retrofit2.Retrofit retrofit) { // 조건
//
//        Log.e(TAG, "moveMain() 시작");
//
//        new Handler().postDelayed(new Runnable() //UI를 건드는거니까 핸들러 사용
//        {
//            @Override
//            public void run()
//            {
//                retrofit로컬로그인(retrofit, myEmail, myPw); //서버에서 값 가져옴 + 화면전환
//            }
//        }, 1000 * sec); // sec초 정도 딜레이를 준 후 시작
//
//        Log.e(TAG, "moveMain() 끝");
//
//    } // 참고) thread.sleep 으로 delay를 주면 layout 로딩도 같이 멈추므로 n초 후 실행되는 postDelayed를 이용



    private void retrofit로컬로그인(retrofit2.Retrofit retrofit, String str_email, String str_pw) {

        Log.e(TAG, "\n retrofit로컬로그인() 파라미터 " +
                "str_email : " + str_email +
                "str_pw : " + str_pw
        );

        Login Login_interface = retrofit.create(Login.class);   // 레트로핏 인터페이스 객체 구현
        Call<Login_find> call = Login_interface.getUser(str_email, str_pw); //인터페이스 객체를 이용해 인터페이스에서 정의한 함수를 호출하면 Call 객체를 얻을 수 있다. //에러났던 이유:DataClass를 참조하는데 interface에서 불러올 때 변수가 없어서.

        //네트워킹 시도
        call.enqueue(new Callback<Login_find>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Login_find> call, Response<Login_find> response) {

                Login_find result = response.body(); //response.isSuccessful안에 있으니까 response가 확인이 안되서 로그찍기위해 한 층 꺼냄냄
                assert result != null;

                if(response.isSuccessful()) { //정상적으로 통신이 성공한 경우

                    Log.e(TAG, "onResponse success");

                    // 서버에서 응답받은 데이터를 토스트로 띄운다
                    String 결과값 = result.getResponse();
                    String getNickName = result.getNickName();
                    String getImgUrl = result.getImgUrl();
                    String getCreateDate = result.getCreateDate();

                    myDate = getCreateDate; //가입일도 받아와야 나의공간에서 활용함 //카카오랑 일반회원이랑 각각 대입하기
                    myName = result.getNickName(); //서버에 닉네임이 있다면 바로 main가고 없으면 프로필작성하러 가기

                    Log.e(TAG, "result.getNickName() --> nickName : "+myName);
                    Log.e(TAG, "onResponse / 결과값: "+결과값);
                    Log.e(TAG, "onResponse / aa result.getNickName: "+result.getNickName());
                    Log.e(TAG, "onResponse / result.getCreateDate: "+result.getCreateDate());

                    if(결과값.equals("true")) { //로그인 성공

                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();

                        //로그인하면 앞으로 자동로그인되도록 하기(파일명 : 이메일이름)
                        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능
                        SharedPreferences.Editor autoLoginEdit = auto.edit();
                        Log.e(TAG, "str_email : "+str_email);
                        Log.e(TAG, "str_pw : "+str_pw);

                        autoLoginEdit.putString("UserEmail", str_email); //db컬럼명과 동일하게 하자
                        autoLoginEdit.putString("UserPwd", str_pw); //파라미터로 받은 pw
                        autoLoginEdit.putString("UserNickName", getNickName);
                        autoLoginEdit.putString("UserImg", getImgUrl);
                        autoLoginEdit.putString("CreateDate", getCreateDate);
//                                autoLoginEdit.putString("smallBitmapImg", mySmallBitmapImg);

                        autoLoginEdit.apply(); //실질 저장

                        if(myName != null) {

                            // 결과값 있으면 로그인 통과 / intent를 이용해 M_main으로 이동시킴
                            Intent intent = new Intent(getApplicationContext(), M_main.class); //로그인btn >> 지도첫화면
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //top빼고 아래액티비티 지움
                            Log.e(TAG, "Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK");
                            startActivity(intent);
                            Log.e(TAG, "startActivity()");
                            Log.e(TAG, ">>> M_main");

                        } else {

                            Intent intent2 = new Intent(getApplicationContext(), L_profile.class); //로그인btn >> 지도첫화면
                            intent2.putExtra("loginEmail", str_email);
                            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //top빼고 아래액티비티 지움
                            Log.e(TAG, "Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK");
                            startActivity(intent2);
                            Log.e(TAG, "startActivity()");
                            Log.e(TAG, ">>> L_profile");

                        }

                    } else { //로그인 실패

                        Log.e(TAG, "!(결과값.equals(true)");
                        //아이디와 비밀번호를 다시 확인해 달라는 토스트띄우기
                        Toast.makeText(getApplicationContext(), "이메일과 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // 통신 실패
                    Log.e("콜.enqueue : ", "onResponse : 실패");
                }
            }

            @Override
            public void onFailure(Call<Login_find> call, Throwable t) {
                Log.e("onFailure : ", t.getMessage());
            }
        });

    }




    private void 쉐어드초기화() {

        //ㅡㅡㅡㅡㅡㅡㅡ
        // 쉐어드 파일 : 첫 액티비티에서 할당하기
        //ㅡㅡㅡㅡㅡㅡㅡ
        if(mySmallBitmapImg !=null) bitmapImg = true;
        Log.e(TAG,"스태틱!" +
                "\nemail : "+myEmail+
                "\npw : "+myPw+
                "\nmyName : "+myName+
                "\nmyImg : "+myImg+
                "\nmySmallBitmapImg : "+bitmapImg+
                "\nmyRoomActive : "+myRoomActive+
                "\nmyRoom_no : "+myRoom_no+
                "\ndate : "+myDate+
                "\nsnsLogin : "+mysnsLogin ); //확인용


        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 쉐어드 'autoLogin' : 자동로그인을 쉐어드로 하려했는데 토큰값으로 해서 이름만 autoLogin파일임
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        String UserEmail = auto.getString("UserEmail",null); //없다면 null이다?
        String UserPwd = auto.getString("UserPwd",null);
        String UserNickName = auto.getString("UserNickName",null); //닉네임은 join과 따로 저장됨. 선별용 변수
        String UserImg = auto.getString("UserImg",null); //로그인할 때 이미지도 같이 쉐어드에 새로 저장시켜서 M_main에서 사용
        String UserMarkerImg = auto.getString("myMarkerImg",null); // 마커 url
        String CreateDate = auto.getString("CreateDate",null);
        String smallBitmapImg = auto.getString("smallBitmapImg",null);
        String snsLogin = auto.getString("snsLogin",null);
        Boolean RoomActive = auto.getBoolean("myRoomActive",false);
        String Room_no = auto.getString("myRoom_no",null);
        String Room이름 = auto.getString("방이름",null);
        String RoomPw = auto.getString("방비번",null);
        Boolean iam방장 = auto.getBoolean("iamLeader",false);


        //유저 정보 외 스태틱변수 삽입
        myRoomActive = RoomActive;
        myEmail = UserEmail;
        myRoom_no = Room_no;
        myMarkerImg = UserMarkerImg;
        if(smallBitmapImg != null) BoolImg = true;
        mySmallBitmapImg = smallBitmapImg; //선언만 해두고 대입을 안 해서 계속 null이었음
        myDate = CreateDate; // 자동로그인하면 서버안거쳐서 값대입이 안됨. 그래서 여기에서 대입
        방이름 = Room이름; //등산 위치공유방 중간에 어플종료되더라도 다시 들어가면 삽입
        방비번 = RoomPw;
        myName = UserNickName;
        myPw = UserPwd;
        iamLeader = iam방장;


        Log.e(TAG, "myDate: "+myDate);
        Log.e(TAG, "\n쉐어드 초기화" +
                "\nUserEmail : "+UserEmail+
                "\nUserPwd : "+UserPwd+
                "\nUserNickName : "+UserNickName+
                "\nUserImg : "+UserImg+
                "\nUserMarkerImg : "+UserMarkerImg+
                "\nCreateDate : "+CreateDate+
                "\nsmallBitmapImg (boolean) : "+BoolImg+
                "\nmyRoomActive (boolean) : "+RoomActive+
                "\nmyRoom_no : "+Room_no+
                "\nRoom이름 : "+Room이름+
                "\nRoomPw : "+RoomPw+
                "\niamLeader : "+iam방장+
                "\nsnsLogin : "+snsLogin );

    }

    public void clearCookies() {
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
    }


    //응답이 느려서 여기안에서 작동해야함
    public void retrofit응답(Call<Login_find> call) {

        Log.e(TAG, "retrofit응답() 입장");

        //네트워킹 시도
        call.enqueue(new Callback<Login_find>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Login_find> call, Response<Login_find> response) {

                Login_find result = response.body(); //response.isSuccessful안에 있으니까 response가 확인이 안되서 로그찍기위해 한 층 꺼냄냄
                assert result != null;
                String 리턴값; //insert or update
                리턴값 = result.getResponse();
                Log.e(TAG, "리턴값 : "+리턴값);
                Log.e(TAG, "getNickName : "+result.getNickName());
                Log.e(TAG, "getCreateDate : "+result.getCreateDate());
                myDate = result.getCreateDate(); //가입일도 받아와야 나의공간에서 활용함
                if(mySmallBitmapImg !=null) bitmapImg = true;

                //받기 : 응답값 insert/update
                if(리턴값.equals("insert")) { //회원가입임
                    Log.e(TAG, "insert했다");

                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                    // 카카오에서 사진,닉넴 가져오기
                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                    Log.e(TAG,"\nretrofit응답() " +
                            "\nonResponse" +
                            "\n스태틱"+
                            "\nemail : "+myEmail+
                            "\npw : "+myPw+
                            "\nmyName : "+myName+
                            "\nmyImg : "+myImg+
                            "\nmyMarkerImg : "+myMarkerImg+
                            "\ndate : "+myDate+
                            "\nmySmallBitmapImg : "+bitmapImg+
                            "\nsnsLogin : "+mysnsLogin ); //확인용
                    shared(myEmail, myPw, myName, myImg, myMarkerImg, myDate,"kakaotalk", bitmapImg, mySmallBitmapImg, myRoomActive, myRoom_no, 방이름, 방비번); //쉐어드 저장

                } else if(리턴값.equals("noQuery")) { //기존회원 로그인, 정보 db에서 가져오기

                    Log.e(TAG, "noQuery했다");
                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                    // 서버에서 사진,닉넴 가져오기
                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                    myName = result.getNickName(); //로그인하면 기존에 사용하던 사진, 닉넴이 나와야해서
                    myImg = result.getImgUrl(); //회원가입이 아닌경우 다시 서버에서 가져온 정보 대입하기

                    Log.e(TAG,"\nemail : "+myEmail+
                            "\npw : "+myPw+
                            "\nmyName : "+myName+
                            "\nmyImg : "+myImg+
                            "\nmyMarkerImg : "+myMarkerImg+
                            "\ndate : "+myDate+
                            "\nsnsLogin : "+mysnsLogin ); //확인용
                    shared(myEmail, myPw, myName, myImg, myMarkerImg, myDate,"kakaotalk",bitmapImg, mySmallBitmapImg, myRoomActive, myRoom_no, 방이름, 방비번); //쉐어드 저장

                } else {
                    Log.e(TAG, "insert도 update도 아니다");
                    finish(); //화면전환되기 전에 종료시켜버림
                }

                //화면전환
                Intent intent = new Intent(getApplicationContext(), M_main.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //현재화면빼고 아래액티비티 지움
                Log.e(TAG, "Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK");
                Toast.makeText(getApplicationContext(), "카카오 로그인 성공", Toast.LENGTH_SHORT).show(); //이동하기전에 토스트외치기
                startActivity(intent);

            }
            @Override
            public void onFailure(Call<Login_find> call, Throwable t) {
                Log.e("onFailure : ", t.getMessage());
            }
        });
    }


    //변수 청소(변수 null뜨면 여기가 범인이다)
    public void shared(String email, String pw, String nickname, String img, String markerImg, String date, String snsLogin,
                       Boolean 참거짓mySmallBitmapImg, String mySmallBitmapImg, Boolean myRoomActive, String myRoom_no, String 방이름, String 방비번) {

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능
        SharedPreferences.Editor autoLoginEdit = auto.edit();

        Log.e(TAG, "\nshared" +
                        "\nemail : "+email+
                        "\npw : "+pw+
                        "\nnickname : "+nickname+
                        "\nimg : "+img+
                        "\nmarkerImg : "+markerImg+
                        "\ndate : "+date+
                        "\nmySmallBitmapImg : "+참거짓mySmallBitmapImg+
                        "\nmyRoomActive : "+myRoomActive+
                        "\nmyRoom_no : "+myRoom_no+
                        "\n방이름 : "+방이름+
                        "\n방비번 : "+방비번+
                        "\nsnsLogin : "+snsLogin );

        autoLoginEdit.clear(); //파일은그대로 파일내용만 삭제됨. <<< 얘 때문이었어 ㅡㅡ

        autoLoginEdit.putString("UserEmail", email); //db컬럼명과 동일하게 하자
        autoLoginEdit.putString("UserPwd", pw); //파라미터로 받은 pw
        autoLoginEdit.putString("UserNickName", nickname);
        autoLoginEdit.putString("UserImg", img);
        autoLoginEdit.putString("myMarkerImg", markerImg);
        autoLoginEdit.putString("CreateDate", date); //가입일
        autoLoginEdit.putString("smallBitmapImg", mySmallBitmapImg); //프사마커
        autoLoginEdit.putString("snsLogin", snsLogin); //sns로 로그인했음을 쉐어드로 구분하기
        autoLoginEdit.putBoolean("myRoomActive", myRoomActive);
        autoLoginEdit.putString("myRoom_no", myRoom_no);
        autoLoginEdit.putString("snsLogin", snsLogin);
        autoLoginEdit.putString("방이름", 방이름);
        autoLoginEdit.putString("방비번", 방비번);

        autoLoginEdit.apply(); //실질 저장
    }

    public void updateKakaoLoginUi(retrofit2.Retrofit retrofit) {


        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                Log.e("메인액티비티", "invoke() 입장");
                Log.e("메인액티비티", "user : "+user);

                if ( user != null) {

                    Log.e(TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: "+user.getId() +
                            "\n닉네임: "+user.getKakaoAccount().getProfile().getNickname() +
                            "\n프로필사진: "+user.getKakaoAccount().getProfile().getProfileImageUrl() +
                            "\n이메일: "+user.getKakaoAccount().getEmail());

                    //우선 서버에 조회하고 없으면 insert (보내기: 이메일,사진,닉넴,회원번호(비번역할) / 받기: 응답=insert)
                    //있으면 update하기 (보내기 : 이메일,사진,닉넴,회원번호 / 받기 : 응답=update)
                    //응답이 있으면 쉐어드 & static에 저장
                    //응답에 따라 다르게 토스트 띄우기

                    //회원가입 한 초기상태는 여기서 static변수 stop
                    myEmail = user.getKakaoAccount().getEmail();
                    myPw = String.valueOf(user.getId());
                    myName = user.getKakaoAccount().getProfile().getNickname();
                    myImg = user.getKakaoAccount().getProfile().getProfileImageUrl();
//                    myDate = 가입한 날짜는 서버에서 가져오기
                    mysnsLogin = "kakaotalk";
//                    Log.e("myDate", myDate);


                    //보내기 : 이메일,회원번호,닉넴,사진
                    kakaoLogin kakaoLogin_interface = retrofit.create(kakaoLogin.class);   // 레트로핏 인터페이스 객체 구현
                    Call<Login_find> call = kakaoLogin_interface.KakaoLoginNewCheck(
                            user.getKakaoAccount().getEmail(), String.valueOf(user.getId()), user.getKakaoAccount().getProfile().getNickname(),user.getKakaoAccount().getProfile().getProfileImageUrl()
                    );
                    Log.e("메인액티비티", "retrofit응답() 직전");
                    retrofit응답(call); //서버로부터 응답받음


                } else {
                    Log.e("메인액티비티", "로그인해주세요");
                }

                return  null;
            }
        });
    }


    //카카오로그인 api를 사용하기 위해 필요한 디버그 해시키(해시키 중 디버그 해시키를 코드상에서 가져옴)
    @SuppressLint("PackageManagerGetSignatures")
    private void getHashKey(){
        Log.e(TAG, "getHashKey() 들어옴");

        PackageInfo packageInfo = null;

        try {
            Log.e(TAG, "packageInfo에 대입");
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "packageInfo : "+ packageInfo);
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null");
        Log.e(TAG, "for문 전");

        assert packageInfo != null;
        for (Signature signature : packageInfo.signatures) {
            Log.e(TAG, "for문 입장");

            try {
                Log.e(TAG, "try 입장");
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
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

    public void ID() {

        et_email = (EditText) findViewById(R.id.et_email); //이메일 입력상자
        et_pw = (EditText) findViewById(R.id.et_pw); //비밀번호 입력상자
        btn_doLogin = (Button) findViewById(R.id.btn_doLogin); //로컬 로그인 버튼
        tv_pw_reissue = (TextView) findViewById(R.id.tv_pw_reissue); //비밀번호 분실
        img_kakaoLogin = (ImageView) findViewById(R.id.img_kakaoLogin); //카카오톡 로그인 api 버튼
        tv_goJoin = (TextView) findViewById(R.id.tv_goJoin); //회원가입하러가기
        wView = (WebView) findViewById(R.id.webview); //test
    }

}
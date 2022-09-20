//package com.example.iamhere.copy;
//
//import static com.example.iamhere.L_login.bitmapCapture;
//import static com.example.iamhere.L_login.h시간m분s초;
//import static com.example.iamhere.L_login.iamLeader;
//import static com.example.iamhere.L_login.ip;
//import static com.example.iamhere.L_login.myDate;
//import static com.example.iamhere.L_login.myEmail;
//import static com.example.iamhere.L_login.myImg;
//import static com.example.iamhere.L_login.myMarkerImg;
//import static com.example.iamhere.L_login.myName;
//import static com.example.iamhere.L_login.myPw;
//import static com.example.iamhere.L_login.myRoomActive;
//import static com.example.iamhere.L_login.myRoom_no;
//import static com.example.iamhere.L_login.mySmallBitmapImg;
//import static com.example.iamhere.L_login.mysnsLogin;
//import static com.example.iamhere.L_login.port;
//import static com.example.iamhere.L_login.socket;
//import static com.example.iamhere.L_login.경도;
//import static com.example.iamhere.L_login.마커경도리스트;
//import static com.example.iamhere.L_login.마커리스트;
//import static com.example.iamhere.L_login.마커위도리스트;
//import static com.example.iamhere.L_login.마커합성bitmap;
//import static com.example.iamhere.L_login.모든위치업뎃_sec;
//import static com.example.iamhere.L_login.방비번;
//import static com.example.iamhere.L_login.방이름;
//import static com.example.iamhere.L_login.소켓통신목적;
//import static com.example.iamhere.L_login.위도;
//import static com.example.iamhere.M_main.StringToBitmap;
//import static com.example.iamhere.M_main.URLtoBitmap;
//import static com.example.iamhere.M_share_3_join_Map.retrofit_퇴장업뎃_exit;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.PointF;
//import android.location.Address;
//import android.location.Geocoder;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.Chronometer;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.core.app.ActivityCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.iamhere.Interface.Sharing;
//import com.example.iamhere.M_main;
//import com.example.iamhere.Model.Chat;
//import com.example.iamhere.Model.ClientInfo;
//import com.example.iamhere.Model.Sharing_room;
//import com.example.iamhere.R;
//import com.example.iamhere.Recyclerview.chat_Adapter;
//import com.example.iamhere.Recyclerview.sharingList_Adapter;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.kakao.sdk.share.ShareClient;
//import com.kakao.sdk.share.model.SharingResult;
//import com.naver.maps.geometry.LatLng;
//import com.naver.maps.map.LocationTrackingMode;
//import com.naver.maps.map.MapView;
//import com.naver.maps.map.NaverMap;
//import com.naver.maps.map.OnMapReadyCallback;
//import com.naver.maps.map.UiSettings;
//import com.naver.maps.map.overlay.Align;
//import com.naver.maps.map.overlay.LocationOverlay;
//import com.naver.maps.map.overlay.Marker;
//import com.naver.maps.map.overlay.Overlay;
//import com.naver.maps.map.overlay.OverlayImage;
//import com.naver.maps.map.util.FusedLocationSource;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.json.JSONTokener;
//
//import java.io.BufferedReader;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//import kotlin.Unit;
//import kotlin.jvm.functions.Function2;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.RequestBody;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.scalars.ScalarsConverterFactory;
//
//public class M_share_2_Map_copy3 extends AppCompatActivity implements OnMapReadyCallback { //implement꼭해주기(인터페이스구현)
//
//
//    String TAG = "M_share_2_Map";
//    MapView mapView; //지도 뷰(보이는 것)
//    NaverMap 네이버Map;
//    FusedLocationSource locationSource; //런타임권한얻은 현재위치값
//    final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}; //대략,정확한 위치 권한
//    final int LOCATION_PERMISSION_REQUEST_CODE = 1000; //런타임권한요청코드
//    boolean isRun = true; //스레드 멈추기용도 - 화면전환시
//    Handler handler2 = new Handler(); //메소드안에 선언하면 사용할 수가 없네(에러:루퍼...)
//    TextView roomName_num; //방이름(인원)
//    //숨겨질 뷰들
//    private TextView fold; //접고피는 ^ 버튼
//    TextView textView12; //예상종료시간
//    Button btn_share_Friends, btn_share_exit; //나중에 카톡으로 공유하기, 퇴장하면 위치공유버튼 눌러도 참가했던 방으로 안 감
//    ConstraintLayout showRecyclerview; //리사이클러뷰와 버튼,텍스트 합쳐서 한꺼번에 숨기기
//    TextView btnRouteDone; //'경로지정완료' 버튼을 누르면 gone되어서 다신 보이지 않기
//    TextView tv_trackingStart; //모든인원이 참여됐다면 운동시작버튼 누르라고 함
//    FrameLayout topLayout; //최상단 '방제(n명)'레이아웃. 경로정할 땐 숨김
//    ImageView iv_sendMessage, iv_setting, iv_compass; //우측초록버튼 1 : 메세지보내기, 2:설정, 3: 나침반
//    ImageView btn_trackingStart, marker_img; //운동시작 버튼 누르면 그 때부터 시간이 카운트 됨 / 재사용할 마커이미지
//    //마커
//    TextView routeNum; //마커 순서 123...
//    int route123 = 0; //숫자 +1씩추가되기
//    Dialog dialog, dialog_chat, dialog_leave; //마커 삭제하기위한 다이얼로그
//    Button button_del, button2_cancel; //다이얼로그 - 삭제, 취소
//    //경로
//    TextView notice; //경로마커 10개까지 찍을 수 있다는 안내
//    Marker 마커if문용;
//    //시간
//    Chronometer chronometer; //경과시간이 보이는 위젯. 그래서 따로 핸들러를 사용하지 않았다.
//    long 시작한시점, time, 주어진시간; //시작한시간을 기준점으로 삼고 그 차이를 뷰에 삽입한다. //지나간 시간
//    final int 초 = 1000;
//    int h, m, s; //쉐어드 저장
//    //채팅
//    RecyclerView rv_chat, rv_list; //채팅창
//    EditText et_chat_msg; //메시지 입력란
//    Button btn_chat_send, btn_chat_nope; //메세지 보내기, 취소 버튼
//    private Handler mHandler = new Handler(); // 수신받은걸 메인스레드에 있는 뷰에 뿌리기 위해서는 핸들러가 필요함. 없으면 에러
//    ArrayList<Chat> chat_items = new ArrayList<>(); //채팅정보가 이 배열에 쌓임 (유저이름,메세지,시간)
//    chat_Adapter chat_adapter; //채팅창 리사이클러뷰에 대한 어댑터
//    // 명단
//    ArrayList<ClientInfo> clientList = new ArrayList<>(); // 닉네임과 프사, 방장이 누구인지만 들어있음.
//    sharingList_Adapter list_adapter; // 위의 chat_adapter와 함께 움직인다.
//    //방
//
//
////    ArrayList<ClientInfo> clientInfos = new ArrayList<>(); // 채팅서버에서 가져온 정보들을 안드에서도 차곡차곡 쌓는다.
//
//
//    //방장 진입 단계(activity)
//    // 1.방이름   2.마커    3.시작버튼   4.운동중
//
//    //어느 단계인지 알아내는 키
//    // 1.상관x    2.상관x   3.myRoomActive라는 스태틱변수(쉐어드에서 꺼낸)   4.'시작한시점'이라는 변수(쉐어드)
//
//    //뷰 숨김 4가지
//    // 1.명단   2.초록버튼   3.마커   4.시작버튼
//
//
//
//    //ㅡㅡㅡㅡㅡㅡ
//    // onCreate()
//    //ㅡㅡㅡㅡㅡㅡ
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mshare2_map);
//        Log.e(TAG, "onCreate");
//        Log.e(TAG, "방이름 : "+방이름);
//        ID();
//        변수확인(); //로그
//
//
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        // 네이버 지도 초기화
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(this); //네이버지도에서 반환되는 콜백함수를 자신(this)으로 지정하는 역할
//                                            //Async : 비동기(로 NaverMap객체를 얻는다)
//        locationSource = new FusedLocationSource(M_share_2_Map_copy3.this, LOCATION_PERMISSION_REQUEST_CODE); //권한요청객체생성(GPS)
//
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        // 참여명단 숨기기 : 리사이클러뷰 및 다른뷰도 숨기기 (3번 마커 빼고 다 숨김)
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        숨김_명단(); //1번
//        숨김_초록버튼(); //2번
//        숨김_트래킹스타트(); //4번
//
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡ
//        // 소요시간 위젯 : 시작점과의 차이를 초마다 set해준다.  //기본 변수 준비
//        //ㅡㅡㅡㅡㅡㅡㅡㅡ
//        //쉐어드 불러오기
////        shared = getSharedPreferences("stopwatch", Activity.MODE_PRIVATE);
////        editor = shared.edit();
//
//        //저장해둔 값 불러오기
//        SharedPreferences shared = getSharedPreferences("stopwatch", Activity.MODE_PRIVATE);
//        시작한시점 = shared.getLong("시작한시점", 0); //뷰에 보이는 시간, 이 변수로 운동시작했는지 구분함
//        chronometer.setText("경과시간 00:00:00"); //3단계에서 처음 보이는 상태
//
////        // DB에 명단 추가 (방업뎃, 명단추가, 운동시작시간 추가)
////        retrofit_Room_RoomUser업뎃(getApplicationContext()); //등산시작했다고 신호를 보내면 현재시간을 DB에 업뎃 //방운동시작시간 =/= 개인운동시작시간 (왜냐면 도중에 들어온사람은?)
//
//
//
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        //                                    이벤트 / 리스너
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//
//        //스톱와치 '시'까지 표기(기본제공이 분:초)
//        //차이만큼 표기
//        //1초
//        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
//            @SuppressLint("LongLogTag")
//            @Override
//            public void onChronometerTick(Chronometer chronometer) {
//
//                time = System.currentTimeMillis() - chronometer.getBase(); //지나온 초는 고정
//                Log.e("time : ", String.valueOf(time));
//
//                주어진시간 = 300000; //30초라고 한다면 30000 milliseconds
//                String t = 시분초변환(); // "경과시간 00:00:12" 따위를 리턴받는다
//
//                //시간이 지나 강제종료
//                if(t.equals("종료")) { //일정시간이 지나므로 종료된다.
//
//
//                    Log.e("onChronometerTick - h시간m분s초 : ", h시간m분s초);
////                    SharedPreferences.Editor editor = shared.edit();
//                    retrofit_퇴장업뎃_removeRoom(h시간m분s초); //서버에 방 비활성화, 시간들 저장
//                    방퇴장처리(getApplicationContext()); //퇴장 후 뒷처리 : 변수, 쉐어드 초기화
//                    isRun = false;
//
//                    Toast.makeText(getApplicationContext(), "종료되었습니다.", Toast.LENGTH_SHORT).show(); // 실행할 코드
//                    t = "경과시간 00:00:00"; //종료되는 찰나에 이상하게 보여서 대입해줌
//                }
//                chronometer.setText(t);
//
//            }
//        });
//
////        //ㅡㅡㅡㅡㅡㅡㅡㅡ
////        // 등산 시작버튼 : 누르면 시간이 카운트된다.
////        //ㅡㅡㅡㅡㅡㅡㅡㅡ
////        btn_trackingStart.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////                //방장 진입 단계(activity) 1.방이름   2.마커   3.시작버튼   4.운동중
////                //中 4단계
////
////                Log.e(TAG, "btn_trackingStart 버튼 클릭");
////                Toast.makeText(getApplicationContext(), "등산을 시작합니다. 안전산행 하세요.", Toast.LENGTH_SHORT).show(); //test toast
////                숨김_트래킹스타트(); //나머지 뷰는 사용할거라 보이는 채로 냅둠
////                시작_경과시간(); //버튼누르면 스톱와치 시작
////
////                //
////                // 다른 참여자들의 스톱와치가 같이 시작된다.
////                //
////                retrofit_Room_RoomUser업뎃(getApplicationContext()); //등산시작했다고 신호를 보내면 현재시간을 DB에 업뎃 //방운동시작시간 =/= 개인운동시작시간 (왜냐면 도중에 들어온사람은?)
////
////            }
////        });
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        // 참가자 명단 뷰 접는 손잡이 : 담겨있는 뷰를 접었다 폈다 한다.
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        fold.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                // 리사이클러뷰 보이고 안 보이게하는 버튼
//                if(showRecyclerview.getVisibility() == View.GONE) { // showRecyclerview : ConstraintLayout로 text,RV,btn 묶음
//                    showRecyclerview.setVisibility(View.VISIBLE); //펼치기
//
//                } else {
//                    showRecyclerview.setVisibility(View.GONE); //자리까지 없앰
//                }
//            }
//        });
//
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        // 경로지정완료 버튼 : 경로1~20설정 후 위도경도도 같이 db에 저장, 뷰가 숨김/보임 처리된다.
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        btnRouteDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //다시 물어볼까말까>버튼이 너무커서 실수로 누를 수도 있을 것 같아. 물어보자
//                /*
//                   1. 다이얼로그 재확인
//                   2. 뷰 보일건 보이고 가릴건 가리기(메소드로 만들면 편하겠군)
//                   3. 마커 클릭 못하게하기
//                   4. 나갔다 들어와도 찍혀있기
//                   5. 퇴장 후 입장했을 때 경로 안 찍혀있기
//                   6. 레트로핏을 배열타입으로 보내기
//                   7. php로 배열타입받기
//                   8. 테이블에 어떻게 저장할건지 그려보기
//                   9. 저장잘되는지 test
//                */
//
//                //ㅡㅡㅡㅡㅡㅡ
//                // 완료 재확인 (다이얼로그)
//                //ㅡㅡㅡㅡㅡㅡ
//                new AlertDialog.Builder(M_share_2_Map_copy3.this) // 현재 Activity의 이름 입력.
//                        .setMessage("\n           이대로 방을 생성하시겠습니까?\n")
//                        .setNeutralButton("           아니요          ", new DialogInterface.OnClickListener() { //확인을 왼쪽에 해야 실수로 더블클릭하는 경우를 막을 수 있음
//                            public void onClick(DialogInterface dialog, int which){
//                                Log.e(TAG, "완료 재확인 (다이얼로그) 아니요 버튼");
//                            }
//                        })
//                        .setPositiveButton("          예              ", new DialogInterface.OnClickListener() { //일부러 띄워쓰기 한거임. 간격조절
//                            public void onClick(DialogInterface dialog, int which){
//
//                                Log.e(TAG, "완료 재확인 (다이얼로그) 예 버튼");
//                                Toast.makeText(getApplicationContext(), "방이 생성되었습니다.", Toast.LENGTH_SHORT).show(); //이동하기전에 토스트외치기
//
//
//                                //방장 진입 단계(activity) 1.방이름   2.마커   3.시작버튼   4.운동중
//                                //中 3단계
//
//                                //ㅡㅡㅡㅡㅡㅡ
//                                // 화면 캡쳐 : 비트맵으로 3등분 중 중간만 잘르기. 기록 리사이클러뷰에 넣을 거임
//                                //ㅡㅡㅡㅡㅡㅡ
//                                숨김_초록버튼(); //보기싫은 초록버튼 없애고 마커만 보이게 캡쳐한다.
//
//                                //ㅡㅡㅡㅡㅡㅡㅡㅡ
//                                // 마커위경도 저장 : 서버통신(retrofit)
//                                //ㅡㅡㅡㅡㅡㅡㅡㅡ
//                                //마커 찍은 경우, 안 찍은 경우
//                                //소켓연결 (방번호 리턴받아야되서 여기 위치)
//                                sendDataServer(); //네트워크 통신으로 insert쿼리날림
//
//                                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                                // 마커클릭 비활성화 : 클릭(삭제)못하기
//                                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                                마커비활성화();
//
//                                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                                // 운동시작버튼 누르기 : 소요시간 카운트 (3번만 숨기고 다 보이기)
//                                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ'
//                                숨김_경로(); //3번
//                                보임_명단();
//                                보임_초록버튼(); //2번
//                                보임_트래킹스타트(); //4번
//
//                                //ㅡㅡㅡㅡㅡㅡㅡㅡ
//                                // 방장인정 변수 : 카톡공유받고 들어올 때, 방장인지 참여자인지 구분하기 위해서
//                                //ㅡㅡㅡㅡㅡㅡㅡㅡ
//                                iamLeader = true; //방이 온전히 생성된 후 방장변수값 대입하기
//
//                                SharedPreferences.Editor editor = shared.edit();
//
//                                editor.putBoolean("iamLeader",true); //쉐어드에도 저장(스태틱변수와 한 쌍)
//                                editor.apply(); //실질 저장
//
//
//                                //경과시간 삽입
//                                //
//                                //
//
//                                //명단 리사이클러뷰에 '방장인 나' 추가
//
//
//
//                            }
//                        })
//                        .show();
//
//            }
//        });
//
//
//        //ㅡㅡㅡㅡ
//        // 방 퇴장 : 같은 방 자동재입장 X (비번입력하면 재입장은 가능하도록 할거임)
//        //ㅡㅡㅡㅡ
//        btn_share_exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e(TAG, "방장이 방을 종료 버튼 클릭");
//
//
//                //ㅡㅡㅡㅡㅡㅡ
//                // 다이얼로그
//                //ㅡㅡㅡㅡㅡㅡ
//                dialog_leave = new AlertDialog.Builder(M_share_2_Map_copy3.this) // 현재 Activity의 이름 입력.
////                    .setTitle("회원탈퇴")
//                    .setMessage("\n          local 2  방을 종료하시겠습니까?\n" +
//                                "\n모든 참여자가 퇴장하게 됩니다.")
//                    .setNeutralButton("모두에 대해 공유 종료", new DialogInterface.OnClickListener() { //확인을 왼쪽에 해야 실수로 더블클릭하는 경우를 막을 수 있음
//                        public void onClick(DialogInterface dialog, int which){
//
//
//                            Log.e(TAG, "방장이 방을 종료 버튼 클릭");
//                            Toast.makeText(getApplicationContext(), "퇴장하였습니다.", Toast.LENGTH_SHORT).show(); //퇴장알림을 해야 확신할 듯
//
//
//                            retrofit_퇴장업뎃_removeRoom(h시간m분s초); // 방장이 나가면 방이 터짐
//                            방퇴장처리(getApplicationContext()); // 변수, 쉐어드 초기화
//                            socketClose_Exit(); // 로그 지저분해서 메소드로 만듦
//                            isRun = false; // 자기 위치 찍기 종료
//
//
//                        }
//                    })
////                    .setNegativeButton("공유 나가기", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialogInterface, int i) {
////
////
////                            Log.e(TAG, "공유 나가기 버튼 클릭");
////                            Toast.makeText(getApplicationContext(), "퇴장하였습니다.", Toast.LENGTH_SHORT).show(); //퇴장알림을 해야 확신할 듯
////
//////                            retrofit_퇴장업뎃_removeRoom(); // 위와 같음
////                            retrofit_퇴장업뎃_exit(myRoom_no, myEmail, h시간m분s초);
////                            방퇴장처리();
////                            socketClose_Exit();
////
////
////                        }
////                    })
//                    .setPositiveButton("    취소    ", new DialogInterface.OnClickListener() { //일부러 띄워쓰기 한거임. 간격조절
//                        public void onClick(DialogInterface dialog, int which){
//                            Log.e(TAG, "취소 버튼 클릭");
//                        }})
//                    .show();
//
//            }
//        });
//
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        // 친구에게 공유하기 : 카카오톡 공유 API (방이름,비번 text + 바로 참여하기)
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        btn_share_Friends.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Log.e(TAG, "btn_share_Friends 클릭");
//                kakaoLink(); //카카오톡 방이름, 비번 공유하기
//
//            }
//        });
//
//
//    } //~onCreate()
//
//
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//    // 메시지를 전송하는 Thread
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//    class ClientSender extends Thread {
//
////        private DataOutputStream dos; // 데이터를 스트림통로를 통해 보낼 것이다.
//        private PrintWriter pw;
////        private DataOutputStream dos; // 데이터를 스트림통로를 통해 보낼 것이다.
//        private String name;
//        private ImageView iv_sendMSG, btn_trackingStart;
//        private ConstraintLayout showRV;
//        private TextView fold;
//        private Dialog dialog_chat, dialog_leave;
//        private EditText et_chat_msg;
//        private Button btn_chat_send, btn_chat_nope, btn_share_exit;
//        private Handler handler;
//        private Context context, activity;
//        private boolean isRun;
//        private ArrayList<ClientInfo> clientList;
//
//
//        // 생성자. 방장과 참여자 두 곳에서 사용 됨
//        public ClientSender(ImageView iv_sendMSG, ConstraintLayout showRV, TextView fold, Dialog dialog_chat, Dialog dialog_leave,
//                            EditText et_chat_msg, Button btn_chat_send, Button btn_chat_nope, Button btn_share_exit, ImageView btn_trackingStart,
//                            Handler handler, Context context, Context activity, boolean isRun, ArrayList<ClientInfo> clientList) {
//
//            this.iv_sendMSG = iv_sendMSG;
//            this.showRV = showRV;
//            this.fold = fold;
//            this.dialog_chat = dialog_chat;
//            this.dialog_leave = dialog_leave;
//            this.et_chat_msg = et_chat_msg;
//            this.btn_chat_send = btn_chat_send;
//            this.btn_chat_nope = btn_chat_nope;
//            this.btn_share_exit = btn_share_exit;
//            this.btn_trackingStart = btn_trackingStart;
//            this.handler = handler;
//            this.context = context;
//            this.activity = activity;
//            this.isRun = isRun;
//            this.clientList = clientList;
//
//            try {
//                pw = new PrintWriter(socket.getOutputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 소켓 통신 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        @Override
//        public void run() { //  this myName : Thread-3
//
//
//            Log.e("ClientSender.class", "run() socket : "+socket);
//            Log.e("ClientSender.class", "유저myName, myRoom_no, 방이름 : "+myName+myRoom_no+방이름);
//            Log.e("ClientSender.class", "isRun : "+isRun);
//            Log.e("ClientSender.class", "clientList : "+clientList);
//            Log.e("ClientSender.class", "위도 : "+위도+"/경도:"+경도);
//
//
//            //시작하자마자 문자3개를 서버로 전송 (1번만)
//            if (!socket.isClosed()) { // 소켓이 열려있다면
//
//                Log.e(TAG, "!socket.isClosed()"); // ok
//
//                pw.println(myEmail); // 이메일로 식별하기
//                pw.println(myName); // 1개만 보내야되네..? --> ㄴㄴ 보낸 순서대로 받으면 됨
//                pw.println(myMarkerImg); // 이 유저의 마커url을 채팅서버에 보내면 소켓으로 입장할 때 전송될거임
//                pw.println(myRoom_no);
//                pw.println(방이름);
//                pw.println(위도); // 입장할 때 위경도 보내기! (기존엔 위치n초뒤에 업뎃할 때 보냈음. 따로할 이유가 없음)
//                pw.println(경도);
//
//                pw.flush();
//
//            }
//
//
//            //ㅡㅡㅡㅡㅡ
//            // UI 작업
//            //ㅡㅡㅡㅡㅡ
//            handler.post(new Runnable() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void run() {
//
//                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                // 메세지보내기 창 : 다이얼로그 창이 뜬다.
//                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                iv_sendMSG.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//
//                    //ㅡㅡㅡㅡㅡㅡㅡㅡ
//                    // 키보드 띄우기
//                    //ㅡㅡㅡㅡㅡㅡㅡㅡ
//                    int rv_visible상태 = showRV.getVisibility(); //메세지보내기 전 상태(상태복구를 위한 변수)
//                    showRV.setVisibility(View.GONE); //지도를 가림
//                    fold.setVisibility(View.GONE); //지도를 가림
//
//                    Log.e(TAG, "메세지보내기 창 띄우기");
//                    Log.e(TAG, "메세지보내기 버튼 클릭 / rv_visible상태 : "+rv_visible상태);
//
//                    dialog_chat.show(); //다이얼로그 띄우기
//                    et_chat_msg.requestFocus(); //입력란을 대상으로
//
//
//                    et_chat_msg.postDelayed(new Runnable() { //이렇게하니까 동작됨. Runnable없이 하려니까 작동하지 않았음
//                        @Override
//                        public void run() {
//
//                            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); // InputMethodManager : 키보드제어 클래스
//                            inputMethodManager.showSoftInput(et_chat_msg, InputMethodManager.SHOW_IMPLICIT); //바로 키보드 띄우기
//
//                        }
//                    }, 10); //초는 상관없음
//
//
//                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                    // 메세지 보내기 버튼
//                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                    btn_chat_send.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//
//                            String message = String.valueOf(et_chat_msg.getText()); //친구에게 보낼 메세지가 담김
//                            Log.e(TAG, "메세지보내기 버튼 클릭 / message : "+message);
//                            Log.e(TAG, "메세지보내기 버튼 클릭 / 위도, 경도 : "+위도+"/"+경도);
//
//
//                            new Thread() { //  this name : Thread-5
//                                public void run() { // Thread in Thread 가 아니라 나란히 스레드가 실행되는 것임.
//                                    Log.e(TAG, "run()에 들어옴");
//
//
//                                    // 메세지 전송
//                                    pw.println(message); // 채팅내용
//                                    pw.flush();
//
//
//                                    dialog_chat.dismiss(); //실행끝났으면 창 꺼지기
//                                    et_chat_msg.setText(""); //빈문자열 넣기
//                                    채팅메세지배경뷰_보임숨김(rv_visible상태, handler, showRV, fold); //참여자 명단 리사이클러뷰 원상복구
//
//
//                                }
//                            }.start();
//
//                        }
//                    });
//
//                    //ㅡㅡㅡㅡㅡㅡ
//                    // 취소 버튼 : 창 사라짐
//                    //ㅡㅡㅡㅡㅡㅡ
//                    btn_chat_nope.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog_chat.dismiss(); //실행끝났으면 꺼지기
//                            et_chat_msg.setText(""); //빈문자열 넣기
//
//                            채팅메세지배경뷰_보임숨김(rv_visible상태, handler, showRV, fold); //참여자 명단 리사이클러뷰 원상복구
//                        }
//                    });
//
//
//                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                    // 다이얼로그 바깥 클릭 이벤트 : 뷰 숨김, 보임때문에
//                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                    dialog_chat.setOnCancelListener(
//                        new DialogInterface.OnCancelListener() {
//                            @Override
//                            public void onCancel(DialogInterface dialog) {
//
//                                Log.e(TAG, "dialog_chat.setOnCancelListener");
//                                채팅메세지배경뷰_보임숨김(rv_visible상태, handler, showRV, fold); //참여자 명단 리사이클러뷰 원상복구
//
//                            }
//                        }
//                    );
//
//                    }
//                });
//
//                //ㅡㅡㅡㅡ
//                // 방 퇴장 : 같은 방 자동재입장 X (비번입력하면 재입장은 가능하도록 할거임)
//                //ㅡㅡㅡㅡ
//                btn_share_exit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Log.e(TAG, "UI 작업... 퇴장 클릭");
//
//
//                        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                        // 다이얼로그   1.방장   2.참여자
//                        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                        if (clientList.get(0).getEmail().equals(myEmail)) { // 방장과 참여자 다이얼로그가 다르게 보이게하기
//                            Log.e(TAG, "방장이 퇴장 버튼 클릭");
//
//
//                            dialog_leave = new AlertDialog.Builder(activity) // 현재 Activity의 이름 입력.
//                                    .setMessage("\n                   방을 종료하시겠습니까?\n")
//                                    .setNeutralButton("모두에 대해 공유 종료", new DialogInterface.OnClickListener() { //확인을 왼쪽에 해야 실수로 더블클릭하는 경우를 막을 수 있음
//                                        public void onClick(DialogInterface dialog, int which){
//
//
//                                            Log.e(TAG, "모두에 대해 공유 종료 버튼 클릭"); // 방장이 모든 참여자를 종료시킬 수 있다.
//                                            Toast.makeText(getApplicationContext(), "위치공유방이 종료되었습니다.\n나의 기록에서 조회하실 수 있습니다.", Toast.LENGTH_LONG).show(); // 실행할 코드
//
//
//                                            new Thread() { // Thread in Thread 가 아니라 나란히 스레드가 실행되는 것임.
//                                                public void run() { // // 메세지 전송하고 소켓끊기
//
//                                                    pw.println("강제종료"); // 채팅내용
//                                                    pw.flush();
//
//                                                }
//                                            }.start();
//
//
//                                            Log.e(TAG, "ClientSender() h시간m분s초 : "+h시간m분s초);
//                                            retrofit_퇴장업뎃_removeRoom(h시간m분s초); // 서버에 방 비활성화, 시간들 저장(시간 null뜸!!)
//                                            방퇴장처리(context); //퇴장 후 뒷처리 : 변수, 쉐어드 초기화
//                                            socketClose_Exit(); // 로그 지저분해서 메소드로 만듦
//                                            isRun = false; // 자기위치 찍기 종료
//                                        }
//                                    })
//
//                                    // 주석 이유 : 방장 양도 기능 구현 취소
////                                .setNegativeButton("공유 나가기", new DialogInterface.OnClickListener() {
////                                    @Override
////                                    public void onClick(DialogInterface dialogInterface, int i) {
////
////
////                                        Log.e(TAG, "공유 나가기 버튼 클릭");
////                                        Toast.makeText(getApplicationContext(), "퇴장하였습니다.", Toast.LENGTH_SHORT).show(); //퇴장알림을 해야 확신할 듯
////
//////                                        retrofit_퇴장업뎃_removeRoom(); // 위와 같음
////                                        retrofit_퇴장업뎃_exit(myRoom_no, myEmail, h시간m분s초);
////                                        방퇴장처리();
////                                        socketClose_Exit();
////
////
////                                    }
////                                })
//                                    .setPositiveButton("       취소       ", new DialogInterface.OnClickListener() { //일부러 띄워쓰기 한거임. 간격조절
//                                        public void onClick(DialogInterface dialog, int which){
//                                            Log.e(TAG, "취소 버튼 클릭");                }})
//                                    .show();
//
//
//                        } else { // 참여자가 퇴장버튼 누를때 (방장과 별도여야 함)
//
//                            btn_share_exit.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    Log.e(TAG, "참여자가 퇴장 버튼 클릭");
//
//
//                                    dialog_leave = new androidx.appcompat.app.AlertDialog.Builder(activity) // 현재 Activity의 이름 입력.
//                                            .setMessage("\n          정말 나가시겠습니까?\n")
//                                            .setNeutralButton("              예              ", new DialogInterface.OnClickListener() { //확인을 왼쪽에 해야 실수로 더블클릭하는 경우를 막을 수 있음
//                                                public void onClick(DialogInterface dialog, int which){
//
//
//                                                    Log.e(TAG, "참여자가 나가기 버튼 클릭"); // 방장이 모든 참여자를 종료시킬 수 있다.
////                                                    Toast.makeText(getApplicationContext(), "퇴장하였습니다.", Toast.LENGTH_SHORT).show();  <<<< 에러!
//
//
//                                                    new Thread() { // Thread in Thread 가 아니라 나란히 스레드가 실행되는 것임.
//                                                        public void run() { // 메세지 전송하고 소켓끊기
//
//                                                            pw.println("퇴장"); // 채팅내용
//                                                            pw.flush();
//
//                                                        }
//                                                    }.start();
//
//                                                    //명단에 추가가 안 되어있음.. 우선 tcp/ip를 하고 디비를 손대자
//                                                    retrofit_퇴장업뎃_exit(myRoom_no, myEmail, h시간m분s초); //위치공유방 참여자 명단 변경(퇴장시간, 소요시간)
//                                                    방퇴장처리(context); //퇴장 후 뒷처리 : 변수, 쉐어드 초기화
//                                                    socketClose_Exit();
//                                                    isRun = false; // 자기위치 찍기 종료
//                                                    소켓통신목적 = "퇴장"; // M_share_3_join_Map.class에서 퇴장할 때 토스트를 사용하기 위해(2안에서 context파라미터 사용해도 안됨;;)
//                                                    Log.e(TAG, "퇴장 토스트를 띄우기 위한 변수 - 소켓통신목적:"+소켓통신목적); // M3에서 마커스레드 안에서 사용됨.
//
//
//                                                }
//                                            })
//                                            .setPositiveButton("         아니요            ", new DialogInterface.OnClickListener() { //일부러 띄워쓰기 한거임. 간격조절
//                                                public void onClick(DialogInterface dialog, int which){}})
//                                            .show();
//                                }
//                            });
//                        }
//                    }
//                });
//
//                //ㅡㅡㅡㅡㅡㅡㅡㅡ
//                // 등산 시작버튼 : 누르면 시간이 카운트된다.
//                //ㅡㅡㅡㅡㅡㅡㅡㅡ
//                btn_trackingStart.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        //방장 진입 단계(activity) 1.방이름   2.마커   3.시작버튼   4.운동중
//                        //中 4단계
//
//                        Log.e(TAG, "btn_trackingStart 버튼 클릭");
//                        Toast.makeText(getApplicationContext(), "등산을 시작합니다. 안전산행 하세요.", Toast.LENGTH_SHORT).show(); //test toast
//                        숨김_트래킹스타트(); //나머지 뷰는 사용할거라 보이는 채로 냅둠
//                        시작_경과시간(); //버튼누르면 스톱와치 시작
//
//
//                        new Thread() { // Thread in Thread 가 아니라 나란히 스레드가 실행되는 것임.
//                            public void run() { // // 메세지 전송하고 소켓끊기
//
//                                pw.println("운동시작"); // 채팅서버로 메세지 보내려고 여기에 위치함
//                                pw.flush();
//
//                            }
//                        }.start();
//
//                        //
//                        // 다른 참여자들의 스톱와치가 같이 시작된다.
//                        //
//                        retrofit_Room_RoomUser업뎃(getApplicationContext()); //등산시작했다고 신호를 보내면 현재시간을 DB에 업뎃 //방운동시작시간 =/= 개인운동시작시간 (왜냐면 도중에 들어온사람은?)
//
//                    }
//                });
//
//
//            }
//        });
//
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        // 위치 전송 (마커용)
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        while (!socket.isClosed()) { // 채팅방에서 나가도 계속 반복되어서 조건문을 소켓이 열리는경우만으로 바꿈 // 반복문이라 맨 뒤로 뺐음
//
//            try {
//
//
//                Log.e(TAG, "송신...while문"); // try를 나가면 whille문에 의해 계속 반복되어서 찍힌다
//
//                pw.println(String.valueOf(위도) ); // 소수점 7자리네
//                pw.println(String.valueOf(경도) ); // double과 string이 섞여서 서버에서 다른 타입에 담기면 아예 못 담는 문제 발생 -> string으로 변환해서 보냄
//                pw.flush();
//
//
//                Thread.sleep(모든위치업뎃_sec*1000); // n초마다 위치를 전송한다.
//                Log.e(TAG, "송신...while 위도 : "+위도+ ", 경도 : "+경도);
//
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        } // ~while()
//
////        Log.e("ClientSender.class", "run() 끝?");
//
//        }
//    } // ~inner class(송신)
//
//
//
//
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//    // 수신용 Thread 클래스
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//    class ClientReceiver extends Thread{
//
////        private DataInputStream dis;
//        private BufferedReader br;
//        private Handler handler;
//        private ArrayList<Chat> chat_items;
//        private chat_Adapter chat_adapter;
//        private RecyclerView rv_chat, rv_list;
//        private TextView roomName_num; // 방이름+인원
//        private ImageView marker_img;
//        private boolean isRun;
//        private NaverMap 네이버Map;
//        private sharingList_Adapter list_adapter;
////        private HashMap<String, Marker> thisRoomClientMarkers = new HashMap<>(); // 나를 제외한 다른 참여자들의 마커를 저장한다. 본인은 별도의 스레드로 마커를 찍는다.
//        private ArrayList<ClientInfo> clientList; // 인덱스로 이루어진 rv때문에 hashMap은 사용하지 않고 별도의 arraylist를 만들었다.
//        private Context context; // 인덱스로 이루어진 rv때문에 hashMap은 사용하지 않고 별도의 arraylist를 만들었다.
//
//
////        private JSONArray jsonArray = null; // whatPurpose_doUI()의 파라미터로 넣기위해 전역 변수로 만들었다.
////        private Marker[] arrMarkers = new Marker[30]; // arraylist로 했다가는 당겨져서 인덱스에 문제가 생길 수 있다.
//                                                        // 들락거리는 경우를 방지하면 30명도 충분하겠지?
////        private ArrayList<Marker> arrMarkers = new ArrayList<>();
////        private Marker marker; // 마커를 전역변수로 빼야 핸들러에서도 손을 댐
//
//
//        public ClientReceiver(Handler handler, ArrayList<Chat> chat_items, chat_Adapter chat_adapter, RecyclerView rv_chat, TextView roomName_num, ImageView marker_img, boolean isRun, NaverMap 네이버Map,
//                              ArrayList<ClientInfo> clientList, sharingList_Adapter list_adapter, RecyclerView rv_list, Context context) {
//
//            this.handler = handler;
//            this.chat_items = chat_items;
//            this.chat_adapter = chat_adapter;
//            this.rv_chat = rv_chat;
//            this.roomName_num = roomName_num;
//            this.marker_img = marker_img;
//            this.isRun = isRun;
//            this.네이버Map = 네이버Map;
//            this.clientList = clientList;
//            this.list_adapter = list_adapter;
//            this.rv_list = rv_list;
//            this.context = context;
//
//            try {
//                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.N)
//        @SuppressLint("SetTextI18n")
//        @Override
//        public void run() { // this name : Thread-4
//
//            Log.e("ClientReceiver.class", "isRun : "+isRun);
//
//
//            while (br!=null) {
//
//                try {
//
//                    // 서버로부터 받는건 객체 1개이다. 여러번 나눠서 받으니까 번잡하다. 이게 더 간편하고 확장하기도 좋다.
//                    String jsonString = br.readLine();
//                    Log.e(TAG, "방 참여자 정보 jsonString : "+jsonString); // 방 참여자 정보
//
//
//                    // jsonArray 인지 jsonObject 인지 모를 때
//                    Object json = new JSONTokener(jsonString).nextValue();
//
//
//                    /*********
//                     * 입장
//                    *********/
//                    if (json instanceof JSONArray) { // 여러 명에 대한 정보
//
//
//                        JSONArray jsonArray = new JSONArray(jsonString);  // Json String -> JsonArray
//                        Log.e(TAG, "이 json은 JSONArray 이다 !!");
//                        Log.e(TAG, "jsonArray : "+jsonArray);
//                        Log.e(TAG, "clientList size : "+clientList.size()+"개 "+ clientList);
//
//
//
//                        // 모든 참여자에 대한 정보 (없는 사람만 추가)
//                        clientList = returnOneClient_입장(jsonArray, clientList, 네이버Map); // 참여자 명단 업뎃
//                        Log.e(TAG, "함수 바깥 - clientList : "+clientList.size()+"개 "+clientList);
//
//
//                        // 마지막 입장한 참여자
//                        ClientInfo lastlyClient = clientList.get(clientList.size()-1); // 방금 입장한 참여자를 UI메소드에 보낸다.
//
//
//                        clientList = whatPurpose_doUI입장(lastlyClient, handler, clientList, context, 네이버Map, roomName_num, marker_img,
//                                            chat_items, rv_chat, chat_adapter, rv_list, list_adapter);
//                        Log.e(TAG, "whatPurpose_doUI입장 함수 바깥 - clientList : "+clientList.size()+"개 "+clientList);
//
////                            Log.e(TAG, "for문) '입장' 후 handler로 img view가져와서 marker에 setIcon하기// 핸들러 따로 실행 함");
////                            clientList = markerSetImg(handler, clientList, i, marker_img);
//
//
//
//
//
//                    /*******************
//                     * 채팅, 위치, 퇴장
//                    *******************/
//                    } else if (json instanceof JSONObject) { // 한 사람에 대한 정보
//
//
//                        Log.e(TAG, "이 json은 JSONObject 이다 !!");
//
//                        // 배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
//                        JSONObject jsonObject = new JSONObject(jsonString);
//                        Log.e(TAG, "jsonObject : "+jsonObject);
//
//
//                        // 한 사람의 정보. 용도 추출
//                        String StreamPurposes = (String) jsonObject.get("purposes"); // 입장/채팅/위치/퇴장 중 1
//                        String RoomFirstPerson_Email = (String) jsonObject.get("email");
//                        String RoomFirstPerson_Nickname = (String) jsonObject.get("chatFrom");
//                        String RoomFirstPerson_imgURI = (String) jsonObject.get("markerImg"); // 하나라도 key이름이 틀리면 이후가 작동 안 한다....
//                        String RoomFirstPerson_Msg = (String) jsonObject.get("msg");
//                        String RoomFirstPerson_ChatTime = (String) jsonObject.get("chatTime");
//                        double RoomFirstPerson_Lat = (double) jsonObject.get("Lat");
//                        double RoomFirstPerson_Lng = (double) jsonObject.get("Lng");
//                        Log.e(TAG, "첫 번째 사람 변수 확인 jsonObject " +
//                                "\nStreamPurposes : "+ StreamPurposes);
//
//
//                        // 마커
//                   /*     Marker marker = new Marker(); // 어디에 위치해서 새로 생성해야 될까? >> 새로 입장할 때
//                        marker.setPosition(new LatLng(RoomFirstPerson_Lat, RoomFirstPerson_Lng)); //먼저(위치에러뜸)
//                        marker.setCaptionText(RoomFirstPerson_ChatTime); // 닉네임이 보이게 하기
//                        marker.setCaptionAligns(Align.Top); // 디폴트가 마커 아래
//                        marker.setTag(RoomFirstPerson_Email); // 여기에 위치해야 함 // 값이 없어서 에러떴음
//                        marker.setIcon(OverlayImage.fromView(marker_img)); // 여기는 UI작업 못함*/
//
//                        // 서버에서 방금 보낸 딱 한 명에 대한 정보
//                        ClientInfo client = new ClientInfo(StreamPurposes, RoomFirstPerson_Email, RoomFirstPerson_Nickname, RoomFirstPerson_imgURI, RoomFirstPerson_Msg,
//                                                RoomFirstPerson_ChatTime, "chatFrom..", RoomFirstPerson_Lat, RoomFirstPerson_Lng, null,null);
//
//                        Log.e(TAG, "한 명이 들어왔다. 마커를 생성하고 client객체 하나를 만든다.");
//                        Log.e(TAG, "함수 바깥2 - clientList 추가 : "+clientList.size()+"개 "+clientList);
////                        clientList.add(client); // << 주의!! 추가하면 안 됨. 그러면 위치 업뎃할 때마다 1명씩 추가 됨
//
//
//                        Log.e(TAG, "handler로 ui작업(1~4단계 처리를 한다)");
//                        clientList = whatPurpose_doUI(client, handler, clientList, context, 네이버Map, roomName_num, marker_img,
//                                chat_items, rv_chat, chat_adapter, rv_list, list_adapter);
//
//                    }
//
//
//
//
//                } catch (JSONException | IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }
//    } // ~inner class(수신)
//
//
//
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 지도 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//
//    @Override
//    public void onMapReady(@NonNull NaverMap naverMap) { //지도의 작동을 다룸
//
//        Log.e(TAG, "onMapReady() 입장");
//        네이버Map = naverMap; //전역변수에 대입(스레드에서 쓰려고)
//
//
//        //런타임 권한
//        Log.e(TAG, "런타임 권한을 맵에 지정");
//        ActivityCompat.requestPermissions(M_share_2_Map_copy3.this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE); //현재위치 표시할 때 권한 확인(이미 M_main에서 통과됐기때문에 여기서는 런타임권한메소드 x)
//
//
//        //현재위치 GPS
//        naverMap.setLocationSource(locationSource); //현재위치
//        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow); //트래킹모드를 선언해야 위치오버레이가 보임
//        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, true); //레이어 : 등산로
//        Log.e(TAG, "LAYER_GROUP_MOUNTAIN 설정");
//        naverMap.addOnLocationChangeListener(location -> //로그 : 위경도
//                {
//                    위도 = location.getLatitude(); //실시간 스태틱 변수에 대입
//                    경도 = location.getLongitude();
////                    Log.e(TAG, "위도경도(변경된) : "+location.getLatitude()+ ", " + location.getLongitude() );
//                }
//        );
//
//
//        //위치 오버레이
//        // (사용자의 위치를 나타내는 데 특화된 오버레이이로, 지도상에 단 하나만 존재)
//        LocationOverlay locationOverlay = naverMap.getLocationOverlay(); //위치 인스턴스 생성x 왜냐면 유일무이..그래서 호출해옴
//        locationOverlay.setPosition(new LatLng(위도, 경도)); //더 빠르게 위치잡으라고 set해줌
//        locationOverlay.setVisible(true); //가시성 : 기본false
//
//
//        //UI 컨트롤을 제어(M_main과동일)
//        UiSettings uiSettings = naverMap.getUiSettings(); //설정객체 선언
//        uiSettings.setLocationButtonEnabled(true); //현재위치 보는 방향 설정
//        uiSettings.setCompassEnabled(true); //나침반. 이게 최선임. 항상 고정하고싶은데 스르륵 사라져버림
//        uiSettings.setScaleBarEnabled(false); //축척바 활성화 안하는게 덜 지저분해 보일 듯(등산어플이랑 비교해봐야지)
//        uiSettings.setZoomControlEnabled(true); //+-버튼
//
//
//        //ㅡㅡㅡㅡㅡㅡ
//        // 마커 스레드 : n초마다 찍힘
//        //ㅡㅡㅡㅡㅡㅡ
//        위도경도Thread(10); // 본인만 따로 하는 이유 : 오버레이와 싱크맞추기
//
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        // 재입장 / 단계 3 : 마커찍고 방생성
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        // 1.다른액티비티 -> this액티비티   2.어플종료 후 --> this액티비티(이 경우 마커리스트 변수가 비어있다.)
//        Log.e(TAG, "재입장인가? myRoomActive : "+myRoomActive);
//        if(myRoomActive) { // if(마커리스트.size() != 0) >> 객체가 하나라도 있다면 생성된 방에 재입장하는 것이다.  >>  찍고 나갈 수 있잖아. 완료 안 누르고
//
//            Log.e(TAG, "생성된 방에 재입장하는 것이다.");
//
//            숨김_경로(); //3번
//            보임_명단(); //1번
//            보임_초록버튼(); //2번
//            보임_트래킹스타트(); //4번
//
//            Log.e(TAG, "마커리스트 변수가 비어있다느거야?" + 마커리스트);
//            Log.e(TAG, "방이름" + 방이름);
//            roomName_num.setText(방이름+"(1명)"); //방이름+인원
//
//
//            //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//            // 재입장 / 단계 4.운동중 변별법 : '시작한시점' 변수
//            //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//            Log.e("chronometer.getBase()", String.valueOf(chronometer.getBase()));
//            Log.e("시작한시점", String.valueOf(시작한시점));
//
//            //운동 시작 후 재입장한 경우를 구분
//            if(시작한시점 != 0) { //재입장, 바로실행
//
//                숨김_트래킹스타트(); //등산중인데 버튼이 보이면 안되지
//                chronometer.setBase(시작한시점); //고정시간을 기준으로 +a해줘야 시작점이 제대로 됨
//                chronometer.start(); //재시작
//
//            } else { //처음입장, 리셋한 상태
//
//                chronometer.setText("경과시간 00:00:00");
//            }
//
//
//            //ㅡㅡㅡㅡㅡㅡ
//            // 마커 찍기 : 어플종료되어도
//            //ㅡㅡㅡㅡㅡㅡ
//            if(마커리스트.size() == 0) { //어플종료 후 들어온거라면, 통신으로 받아온 값으로 마커찍기
//
//                // 전송 : Room_no
//                // 수신 : ArrayList위도, ArrayList경도
//
//                Log.e(TAG, "retrofit마커get() 마커리스트.size() == 0");
//                retrofit마커get(); //위도,경도 값을 arraylist<double>에 대입한다. (json to arraylist) //마커셋팅()포함되어있음
//
//            } else { //스태틱변수가 살아있을 때(어플살아있을 때) 들어온거라면, 바로 마커 찍기
//
//                Log.e(TAG, "retrofit마커get()  마커리스트.size() != 0");
//                마커셋팅(네이버Map); //파라미터로 현재펼쳐진 지도를 건내줘야 인식한다.
//
//            }
//
//        } else {
//            Log.e(TAG, "이 방에 처음 입장");
//        }
//
//
//
//        //경로마커 방장이 찍기
//        //if(방장) 마커찍기
//        //else 마커불러오기(통신신)
//        //클릭이벤트 토스트로 test
//
//        //ㅡㅡㅡㅡㅡㅡㅡ
//        // 지도 롱 클릭 : 마커 생성
//        //ㅡㅡㅡㅡㅡㅡㅡ
//        routeNum.setVisibility(View.GONE); //기존 뷰를 안 숨기니까 뷰가 제멋대로 이동해버림.
//        naverMap.setOnMapLongClickListener(new NaverMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
//
//                double 위도 = latLng.latitude;
//                double 경도 = latLng.longitude;
//
//                     Toast.makeText(getApplicationContext()
//                    , "lat : "+latLng.latitude+", Lng : "+latLng.longitude
//                    , Toast.LENGTH_SHORT).show(); //퇴장알림을 해야 확신할 듯
//
//                    Log.e(TAG, "위도 : "+위도);
//                    Log.e(TAG, "경도 : "+경도);
//                    Log.e(TAG, "route123 : "+route123);
//
//
//                if(route123 < 20) { // 0~9입장 1~20배출
//
//                    route123++; //1~20
//                    Log.e(TAG, "route123++ if문 안 : "+route123);
//                    routeNum.setText(String.valueOf(route123)); //원형 마커에 숫자 대입 /int그대로넣으니 에러남
//
//                    //ㅡㅡㅡㅡㅡㅡ
//                    // 마커 추가 : 끝수 + 1된 마커 추가
//                    //ㅡㅡㅡㅡㅡㅡ
//                    Marker marker = new Marker(); //여러개 추가할거라 롱클릭 안에 위치
//                    marker.setPosition(new LatLng(위도, 경도)); //먼저(위치에러뜸)
//                    marker.setWidth(90);
//                    marker.setHeight(90);
//                    marker.setMap(네이버Map); //다시 대입한다 /해야 보임
//                    marker.setIcon(OverlayImage.fromView(routeNum)); //숫자먼저 set하고 뷰를 삽입한다.
//                    marker.setTag(String.valueOf(route123)); //태그로 특정 마커를 불러내야지...시도
//                    마커if문용 = marker; //경로완료 후 클릭안먹히게하려고 만든 변수
//
//
//                    마커리스트.add(route123 - 1, marker); //마커배열에 삽입해서 나중에 삭제할 때 꺼내서 icon변경할 거임 /배열[0]부터하려고 -1함
//                    마커위도리스트.add(위도); //마커추가할 때 같이 위경도도 추가하고 삭제할 때도 동일하게. 왜냐면 인덱스로 값을 꺼낼거니까.
//                    마커경도리스트.add(경도);
//                    Log.e(TAG, "마커리스트[] : "+ 마커리스트);
//                    Log.e(TAG, "마커리스트[].size : "+ 마커리스트.size() +"마커위도리스트size :"+마커위도리스트.size()+"마커경도리스트size: "+마커경도리스트.size());
//                    Log.e(TAG, "마커위도리스트[] : "+ 마커위도리스트); //담긴 객체 확인용
//                    Log.e(TAG, "마커경도리스트[] : "+ 마커경도리스트);
//
//
//                    //ㅡㅡㅡㅡㅡㅡㅡ
//                    // 경로마커삭제 (다이얼로그) : 삭제 or 취소
//                    //ㅡㅡㅡㅡㅡㅡㅡ
//                    marker.setOnClickListener(new Overlay.OnClickListener() { //여기서 선언해야 해당 좌표가 삭제된다.(4번삭제눌렀는데 1번 삭제되버림)
//                        @Override
//                        public boolean onClick(@NonNull Overlay overlay) {
//
//                            dialog.show(); //다이얼로그 띄우기
//
//                            Log.e(TAG, "marker setOnClickListener 1111 : "+marker);
//                            Log.e(TAG, "marker.getTag() 1111 : "+marker.getTag()); //생성할 때 숫자를 태그로 붙였다.
//                            Log.e(TAG, "marker.setOnClickListener()222");
//
//
//                            //마커클릭 -> 다이얼로그 -> [취소] 선택
//                            button2_cancel.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//
//                                    dialog.dismiss(); //다이얼로그만 사라짐
//                                }
//                            });
//
//                            //마커클릭 -> 다이얼로그 -> [삭제] 선택
//                            button_del.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//
//                                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                                    //클릭한 마커의 인덱스 찾아내기 : ArrayList.indexOf()는 인자를 객체로 받고 존재하면 해당인덱스 리턴, 아니라면 -1을 리턴함
//                                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                                    int 클릭한마커num = 마커리스트.indexOf(marker)+1; //인덱스+1해야 num이 나옴
//
//                                    Log.e(TAG, "클릭한마커num(값이 없으면 -1을 리턴한다.) : "+클릭한마커num);
//                                    Log.e(TAG, "route123  : "+route123);
//                                    Log.e(TAG, "button_del.setOnClickListener()");
//                                    Log.e(TAG, "marker 2222 : "+marker);
//                                    Log.e(TAG, "마커리스트[].size 삭제 전 : "+ 마커리스트.size());
//
//                                    //ㅡㅡㅡㅡㅡ
//                                    // 마커삭제
//                                    //ㅡㅡㅡㅡㅡ
//                                    marker.setMap(null); //마지막 추가된 마커인지 방금 선택된 마커인지
//                                    마커리스트.remove(클릭한마커num-1); //어레이리스트에 들어간 마커객체도 삭제해줘서 인덱스 변경해주기(당겨주기)
//                                    마커위도리스트.remove(클릭한마커num-1);
//                                    마커경도리스트.remove(클릭한마커num-1); //마커와 동일하게 추가삭제함
//                                    dialog.dismiss(); //사라짐 <<추가 안 했더니 삭제하고 가만히 있는다.
//
//                                    Log.e(TAG, " 삭제 후) 마커리스트[].size : "+ 마커리스트.size()+", 마커위도리스트size :"+마커위도리스트.size()+", 마커경도리스트size: "+마커경도리스트.size());
//
//
//                                    //ㅡㅡㅡㅡㅡㅡㅡ
//                                    // 마커 뷰 변경
//                                    //ㅡㅡㅡㅡㅡㅡㅡ
//                                    if(클릭한마커num <route123) { //끝값만 아니라면 전체 icon다시 세팅한다.
//
//                                        for (int i=0; i<route123-1; i++) { //전체 돌려서 icon다시 세팅팅 /삭제한 후니까 -1
//
//                                            Log.e(TAG, "for문 i  : "+i);
//                                            routeNum.setText(String.valueOf(i+1)); //1부터 7까지 숫자를 삽입한 뷰만들기 > 마커가 될 예정
//                                            마커리스트.get(i).setIcon(OverlayImage.fromView(routeNum)); //0번째 인덱스 마커의 뷰를 변경
//                                            마커리스트.get(i).setTag(String.valueOf(i)); //태그도 변경된 숫자로 세팅
//                                       }
//                                    }
//
//                                    route123--; //마커 1개 삭제 후 앞으로 당겨서 끝수를 -1함!!
//                                    Log.e(TAG, "route123-- 1줄임 : "+route123);
//                                    Log.e(TAG, "마커리스트[].size : "+ 마커리스트.size());
//                                    Log.e(TAG, "마커리스트[] : "+ 마커리스트);
//                                    Log.e(TAG, "마커위도리스트[] : "+ 마커위도리스트); //담긴 객체 확인용
//                                    Log.e(TAG, "마커경도리스트[] : "+ 마커경도리스트);
//                                }
//                            });
//                            return true;
//                        }
//                    }); //~marker.setOnClickListener()
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "최대 경로마커 갯수는 20개입니다.", Toast.LENGTH_SHORT).show(); //퇴장알림을 해야 확신할 듯
//                }
//
//
//            }
//        }); //~지도 롱 클릭
//
//
//
//
//    } //~oMapReady()
//
//
////    private int 위치메세지인가(String read4) {
////
////        try { // 3.위치
////            double parseDouble = Double.parseDouble(read4); // str -> dou 했을 때
////            Log.e(TAG, "parseDouble 성공...위치메세지다 parseDouble : "+parseDouble);
////            return 3;
////
////        } catch (NumberFormatException e) { // 실패한다면 여기로
////            Log.e(TAG, "parseDouble 실패... 입장/채팅/퇴장");
////            return 0;
////        }
////
////    }
//
//
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ http통신 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//
//    @SuppressLint("LongLogTag")
//    private void retrofit_퇴장업뎃_removeRoom(String h시간m분s초) {
//
//
//        //이벤트 : 퇴장버튼 클릭/강제종료
//        //보낼 값 : 방번호, 유저이멜, 소요시간
//        //결과 : 룸테이블 - 액티=0, 끝시간, 소요시간 업뎃
//        //응답 : sucess
//
//        String TAG = "retrofit_퇴장업뎃_removeRoom()";
////        Log.e("retrofit_퇴장업뎃_removeRoom() h시간m분s초 : ", h시간m분s초); 에러남..
//
//        //레트로핏 객체 생성, 빌드
//        Retrofit retrofit = new Retrofit.Builder() //Retrofit 인스턴스 생성
//                .baseUrl("http://15.164.129.103/")  //baseUrl 등록
//                .addConverterFactory(GsonConverterFactory.create())  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
//                .client(createOkHttpClient()) //네트워크 통신 로그보기(서버로 주고받는 파라미터)
//                .build();
//
//        //변수값 확인
//        Log.e(TAG, "h시간m분s초 : " + h시간m분s초); // null 뜨네??
//
//
//        //ㅡㅡㅡㅡㅡ
//        // 방 종료 : 끝나서 방에 입장안된다. 시간들, 명단 업뎃
//        //ㅡㅡㅡㅡㅡ
//        Sharing SharingRoomCreate = retrofit.create(Sharing.class);   // 레트로핏 인터페이스 객체 구현
//        Call<Sharing_room> call = SharingRoomCreate.finishTracking(myRoom_no, myEmail, h시간m분s초); //보냄 : 방번호로 특정하여 db에 현재시간 삽입하기
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
//                Log.e(TAG, "성공인가유? : "+result.getResponse()); //응답값은 db에 잘 들어갔는지 확인만하기. 사용할 일은 없음
//
//            }
//
//            @Override
//            public void onFailure(Call<Sharing_room> call, Throwable t) {
//                Log.e("onFailure : ", t.getMessage());
//            }
//        });
//
//
//    }
//
//    private void retrofit_썸네일업뎃() {
//
//        Log.e(TAG, "retrofit_썸네일업뎃() 입장 &&&&&&&&&&&& ");
//
//        //레트로핏 객체 생성, 빌드
//        Retrofit retrofit = new Retrofit.Builder() //Retrofit 인스턴스 생성
//                .baseUrl("http://15.164.129.103/")  //baseUrl 등록
//                .addConverterFactory(GsonConverterFactory.create())  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
//                .client(createOkHttpClient()) //네트워크 통신 로그보기(서버로 주고받는 파라미터)
//                .build();
//
//
//        //사진 bitmap to body
//        MultipartBody.Part body = 사진_body(bitmapCapture, myRoom_no+방이름); // 파일명 : 방번호+방이름(234ㄷㄱㅁㅁ)
//
//
//        //ㅡㅡㅡㅡㅡ
//        // 방 종료 : 끝나서 방에 입장안된다. 시간들, 명단 업뎃
//        //ㅡㅡㅡㅡㅡ
//        Sharing SharingRoomCreate = retrofit.create(Sharing.class);   // 레트로핏 인터페이스 객체 구현
//        Call<Sharing_room> call = SharingRoomCreate.mapCapture(body, myRoom_no); //body가 최종형태 데이터이다...
//
//
//        //네트워킹 시도
//        call.enqueue(new Callback<Sharing_room>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onResponse(Call<Sharing_room> call, Response<Sharing_room> response) {
//
//                Sharing_room result = response.body();
//                assert result != null;
//                Log.e(TAG, "성공이에유? : "+result.getResponse()); //응답값은 db에 잘 들어갔는지 확인만하기. 사용할 일은 없음
//
//            }
//
//            @Override
//            public void onFailure(Call<Sharing_room> call, Throwable t) {
//                Log.e("onFailure : ", t.getMessage());
//            }
//        });
//
//
//    }
//
//    //시작버튼 누르면 db에 운동시작한 시간이 저장된다. 보냈다는걸 알 수 있는 의미없는 데이터를 보낸다.
//    @SuppressLint("LongLogTag")
//    private void retrofit_Room_RoomUser업뎃(Context context) {
//
//        //이벤트 : 시작버튼 클릭
//        //보낼 값 : 시작했다는 아무 값
//        //결과 : 룸테이블 - 시작시간, 룸유저테이블 시작시간 업뎃
//        //응답 : sucess
//
//        Log.e(TAG, "retrofit_Room_RoomUser업뎃() 입장!!!!!!!!!!");
//
//        //레트로핏 객체 생성, 빌드
//        Retrofit retrofit = new Retrofit.Builder() //Retrofit 인스턴스 생성
//                .baseUrl("http://15.164.129.103/")  //baseUrl 등록
//                .addConverterFactory(GsonConverterFactory.create())  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
//                .client(createOkHttpClient()) //네트워크 통신 로그보기(서버로 주고받는 파라미터)
//                .build();
//
//
//
//        //ㅡㅡㅡㅡㅡ
//        // 시간업뎃 : 방 생성시간 X 운동시작시간 O
//        //ㅡㅡㅡㅡㅡ
//        Sharing SharingRoomCreate = retrofit.create(Sharing.class);   // 레트로핏 인터페이스 객체 구현
//        Log.e(TAG, "retrofit_Room_RoomUser업뎃()... 위도 :"+위도+"/경도:"+경도);
//        String 추출한주소 = 위경도to주소(위도,경도, context);
//        Log.e(TAG, "추출한주소? : "+추출한주소); //응답값은 db에 잘 들어갔는지 확인만하기. 사용할 일은 없음
//
//        Call<Sharing_room> call = SharingRoomCreate.startTracking(myRoom_no, myEmail, 추출한주소); //보냄 : 방번호로 특정, email로 방 참여자 명단 만들기(명단은 '등산중'에 참여한 사람만 해당된다.)
//
//        // 주
//        // 소
//
//        //네트워킹 시도 2
//        call.enqueue(new Callback<Sharing_room>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onResponse(Call<Sharing_room> call, Response<Sharing_room> response) {
//
//                Sharing_room result = response.body();
//                assert result != null;
//                Log.e(TAG, "성공인가? : "+result.getResponse()); //응답값은 db에 잘 들어갔는지 확인만하기. 사용할 일은 없음
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
//
//
//
//    private void retrofit마커get() {
//
//        //에러 뜨길래 gson추가 //Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 1 path $
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        //레트로핏 객체 생성, 빌드
//        Retrofit retrofit = new Retrofit.Builder() //Retrofit 인스턴스 생성
//                .baseUrl("http://15.164.129.103/")  //baseUrl 등록
//                .addConverterFactory(GsonConverterFactory.create(gson))  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
//                .client(createOkHttpClient()) //네트워크 통신 로그보기(서버로 주고받는 파라미터)
//                .build();
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        // 마커 위도경도값 가져오기
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        //보내기 : 방번호
//        Sharing SharingRoomCreate = retrofit.create(Sharing.class);   // 레트로핏 인터페이스 객체 구현
//        Call<Sharing_room> call = SharingRoomCreate.markerArraylistGet(myRoom_no); //방번호만 보내도 테이블에서 위도경도값 찾아옴
//
//
//        //네트워킹 시도
//        call.enqueue(new Callback<Sharing_room>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onResponse(Call<Sharing_room> call, Response<Sharing_room> response) {
//
//                Log.e(TAG, "retrofit마커get() 결과");
//
//                Sharing_room result = response.body();
//                String str위도값들 = result.getarrLat(); //["37.4827142710659","37.48222882555662","37.48086743405399"]
//                String str경도값들 = result.getarrLng();
//
//                Log.e(TAG, "result " + result); //toString을 가져와버리네?
//                Log.e(TAG, "result.getLat() " + result.getarrLat()); //에러 >> 배열을 json으로 바꿔서 담음
//                Log.e(TAG, "result.getarrLng() " + result.getarrLng()); //에러 >> 배열을 json으로 바꿔서 담음
//
//
//                위도경도jsonToArraylist(str위도값들, str경도값들); //스태틱에 위도,경도값 대입함
//                마커셋팅(네이버Map); //가져온 위도경도 값으로 마커찍음 //비활성화..되어있겠지?
//
//            }
//
//            @Override
//            public void onFailure(Call<Sharing_room> call, Throwable t) {
//                Log.e("onFailure... : ", t.getMessage());
//            }
//        });
//
//    }
//
//    // 1. 방을 생성한다.
//    // 2. 마커의 위경도를 RouteMarker테이블에 저장한다.
//    private void sendDataServer() {
//        Log.e(TAG, "sendDataServer() 입장!!!!!!!!!!");
//
//        //레트로핏 객체 생성, 빌드
//        Retrofit retrofit = new Retrofit.Builder() //Retrofit 인스턴스 생성
//                .baseUrl("http://15.164.129.103/")  //baseUrl 등록
//                .addConverterFactory(GsonConverterFactory.create())  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
//                .client(createOkHttpClient()) //네트워크 통신 로그보기(서버로 주고받는 파라미터)
//                .build();
//
//        //변수확인
//        Log.e(TAG, "sendDataServer() 방이름 : "+방이름);
//
//
//        //ㅡㅡㅡㅡㅡ
//        // 방 생성 : 경로마커완료 이후에 방 생성하기. 안 그러면 도중에 나갔다 돌아오면 마커가 찍힘
//        //ㅡㅡㅡㅡㅡ
//        //보내기 2 : 방이름, 방비번, 이메일
//        Sharing SharingRoomCreate = retrofit.create(Sharing.class);   // 레트로핏 인터페이스 객체 구현
//        Call<Sharing_room> call = SharingRoomCreate.createRoom(방이름, 방비번, myEmail); //방이름, 방비번
//
//
//        //네트워킹 시도 2
//        call.enqueue(new Callback<Sharing_room>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onResponse(Call<Sharing_room> call, Response<Sharing_room> response) {
//
//                Sharing_room result = response.body(); //response.isSuccessful안에 있으니까 response가 확인이 안되서 로그찍기위해 한 층 꺼냄냄
//                assert result != null;
//
//                if(result.getActivate().equals("1")) myRoomActive = true; //쉐어드에 들어갈 스태틱 변수 변경
//                myRoom_no = result.getRoom_no();
//
//                Log.e(TAG, "리턴값1 myRoom_no : " + myRoom_no);
//                Log.e(TAG, "리턴값2 myRoomActive : " + myRoomActive);
//
//
//                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                // 순서 주의!! [ myRoom_no / 방장닉넴 ] 필요한 함수들 모음
//                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                chat_adapter = new chat_Adapter(getApplicationContext(), chat_items, myName, clientList);
//                list_adapter = new sharingList_Adapter(getApplicationContext(), clientList, myName);
//
//
//                쉐어드저장(); //통신결과가 바로 변수에 대입이 안됨
//                retrofit마커저장(); //비슷하게 통신을 2번 보내니 방이 생성이 안될 때가 있어서 결과받고 실행
//                캡쳐Thread(); //전체화면 캡쳐 후 3등분의 중간부분만 static변수인 bitmapCapture에 담기
//
//                Log.e(TAG, "소켓연결() 입장전 h시간m분s초... null인가? : " + h시간m분s초);
//                Log.e(TAG, "소켓연결() 입장전 변수 확인 : "+ip+port+myName); //실질 방 생성은 여기이므로 소켓을 방입장하자마자 연결하는게 아니라 여기서 연결함
//                sharingList_AND_chat_rv_Adapter장착(rv_chat, chat_adapter, rv_list, list_adapter, getApplicationContext()); //보이기시작한 채팅창에 어댑터를 장착한다. 가독성을 위해 함수로 만들었다.
//                소켓연결(iv_sendMessage, showRecyclerview, fold, dialog_chat, dialog_leave, et_chat_msg, btn_chat_send, btn_chat_nope, btn_share_exit, btn_trackingStart,
//                        mHandler, chat_adapter, chat_items, rv_chat, getApplicationContext(),  M_share_2_Map_copy3.this, roomName_num, marker_img, isRun, 네이버Map,
//                        clientList, list_adapter, rv_list); //서버와 소켓을 연결한다.
//
//
//            }
//
//            @Override
//            public void onFailure(Call<Sharing_room> call, Throwable t) {
//                Log.e("onFailure : ", t.getMessage());
//            }
//        });
//
//        Log.e(TAG, "call 탈출 myRoom_no : " + myRoom_no);
//
//
//    }
//
//    private void retrofit마커저장() {
//
//        //레트로핏 객체 생성, 빌드
//        Retrofit retrofit = new Retrofit.Builder() //Retrofit 인스턴스 생성
//                .baseUrl("http://15.164.129.103/")  //baseUrl 등록
//                .addConverterFactory(GsonConverterFactory.create())  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
//                .client(createOkHttpClient()) //네트워크 통신 로그보기(서버로 주고받는 파라미터)
//                .build();
//
//        //ㅡㅡㅡㅡㅡㅡ
//        // 위경도 저장 : 경로마커완료 이후에 방 생성하기. 안 그러면 도중에 나갔다 돌아오면 마커가 찍힘
//        //ㅡㅡㅡㅡㅡㅡ
//        //보내기 : 위도 리스트, 경도 리스트, 방이름
//        Log.e(TAG, "마커리스트.size() : "+마커리스트.size());
//
//        if(마커리스트.size() > 0) { //마커가 1개라도 찍혔다면 마커의 위경도도 같이 저장하기
//
//            Log.e(TAG,"마커 위도경도 삽입하는 통신" +
//                    "\n마커리스트 : "+마커리스트+
//                    "\n마커위도리스트 : "+마커위도리스트+
//                    "\n마커경도리스트 : "+마커경도리스트+
//                    "\n방이름 : "+방이름
//            ); //확인용
//
//            Sharing markerLatLng = retrofit.create(Sharing.class);   // 레트로핏 인터페이스 객체 구현
//            Call<Sharing_room> call2 = markerLatLng.markerArraylistSend(마커위도리스트, 마커경도리스트, 방이름); //php에 arraylist째로 보내기, 파라미터니까 순서주의
//
//
//            //네트워킹 시도
//            call2.enqueue(new Callback<Sharing_room>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
//                @SuppressLint("SetTextI18n")
//                @Override
//                public void onResponse(Call<Sharing_room> call, Response<Sharing_room> response) {
//
//                    Sharing_room result = response.body(); //response.isSuccessful안에 있으니까 response가 확인이 안되서 로그찍기위해 한 층 꺼냄냄
//                    assert result != null;
//                    String 리턴값 = result.getResponse();
//                    Log.e(TAG, "리턴값~@ : "+리턴값);
//
//                    if(!리턴값.equals("0")) { //1개이상 저장했다면
//                        Log.e(TAG, "경로마커 위경도값 저장 성공 ");
//                    }
//
//                }
//                @Override
//                public void onFailure(Call<Sharing_room> call, Throwable t) {
//                    Log.e("onFailure : ", t.getMessage());
//                }
//            });
//
//        } else {
//
//            Log.e(TAG, "마커 안 찍음~");
//        }
//
//    }
//
//    static Retrofit retrofit객체() {
//
//        Retrofit retrofit = new Retrofit.Builder() //Retrofit 인스턴스 생성
//                .baseUrl("http://15.164.129.103/")  //baseUrl 등록
//                .client(createOkHttpClient()) //네트워크 통신 로그보기(서버로 주고받는 파라미터)
//                .addConverterFactory(GsonConverterFactory.create())  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();
//
//        return retrofit;
//    }
//
//    // 네트워크 통신 로그(서버로 보내는 파라미터 및 받는 파라미터) 보기
//    static OkHttpClient createOkHttpClient() {
//
//        Log.e("createOkHttpClient ()", "로그찍는 메소드");
//
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//            @Override
//            public void log(@NonNull String s) {
//                Log.e("로그찍는다 ",  " s : " +s);
//            }
//        });
//
//
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        builder.interceptors().add(interceptor); //추가함
//        builder.addNetworkInterceptor(interceptor); //추가함
//        builder.addInterceptor(interceptor);
//
//        return builder.build();
//    }
//
//
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 소켓 통신 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//
//
//    // 입장 후 ~ (1명씩 데이터 담기) ~ 입장UI처리
//    public ArrayList<ClientInfo> returnOneClient_입장(JSONArray jsonArray, ArrayList<ClientInfo> clientList, NaverMap 네이버Map) throws JSONException { String TAG = "returnOneClient_입장() ";
//
//
//            ClientInfo client = null;
//
//            for(int i=0; i<jsonArray.length(); i++) { // 내가 이 방에 처음 입장했을 때 실행. 현재 참여중인 방참여자 모두 가져옴. 마커로 비교해서 없는 사람만 추가하기
//
//                // 배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
//                JSONObject object = (JSONObject) jsonArray.get(i);
//
//                // JSON name으로 추출
//                String purposes = (String) object.get("purposes");
//                String email = (String) object.get("email");
//                String markerImg = (String) object.get("markerImg");
//                String msg = (String) object.get("msg");
//                String chatTime = (String) object.get("chatTime");
//                String chatFrom = (String) object.get("chatFrom");
//                double Lat = (double) object.get("Lat");
//                double Lng = (double) object.get("Lng");
//
//                Log.e(TAG,  "\n변수 확인 jsonObject.. " +
//                        "\njsonArray.length() : " + jsonArray.length() +
//                        "\npurposes : " + purposes +
//                        "\nemail : " + email +
//                        "\nmarkerImg : " + markerImg +
//                        "\nmsg : " + msg +
//                        "\nchatTime : " + chatTime +
//                        "\nchatFrom : " + chatFrom +
//                        "\nLat : " + Lat +
//                        "\nLng : " + Lng);
//
//
//                // 안드에서 가지고있는 명단 vs 채팅서버에서 가져온 이멜 비교
//                if (clientList.size() <= i) { // 인덱스로 하니까 에러나서 size로 함. 어차피 서버에서 순서대로 받아오기 때문
//
//                    Bitmap markerImgBitmap = URLtoBitmap(markerImg);
//                    Log.e(TAG, "url to bitmap / markerImgBitmap : "+markerImgBitmap);
//
//
////                    Marker marker = new Marker(); // 어디에 위치해서 새로 생성해야 될까? >> 새로 입장할 때
////                    Log.e(TAG, "안드 명단에 없는 사람이라면 추가하기");
////                    marker.setPosition(new LatLng(Lat, Lng)); //먼저(위치에러뜸)
////                    marker.setCaptionText(chatFrom); // 닉네임이 보이게 하기
////                    marker.setCaptionAligns(Align.Top); // 디폴트가 마커 아래
////                    marker.setTag(email); // 여기에 위치해야 함 // 값이 없어서 에러떴음
////                    Log.e(TAG, "marker가 왜 계속 null이 나오지?? : "+marker); // 값 있는데 client에 삽입만 하면 null이 뜸;;
////
////                    assert markerImgBitmap != null;
////                    marker.setIcon(OverlayImage.fromBitmap(markerImgBitmap));
//
////                    marker.setIcon(OverlayImage.fromView(marker_img)); // UI작업 못함
////                    marker.setMap(네이버Map); // 여기에 위치해야 함 // 값이 없어서 에러떴음
//
//
//
//                    // 클라이언트 한 명 정보에 저장하기
//                    client = new ClientInfo(purposes, email, chatFrom, markerImg, msg, chatTime, chatFrom, Lat, Lng, null, markerImgBitmap); // 마커는 다음 메소드에서 적용
//                    clientList.add(client);
//                    Log.e(TAG, "client 한 명 : "+client);
//                    Log.e(TAG, "클라1명 생성(마커포함) / clientList size : "+clientList.size() +clientList);
//
//                } else {
//                    Log.e(TAG, "클라 추가X 이미 가진 명단임/ clientList size: "+clientList.size() +clientList);
//                }
//
//            } // ~for()
////                    thisRoomClientMarkers.put(email, marker); // 없어야만 들어갈 수 있음
////                    Log.e(TAG, "marker 요소 보이나? : "+marker);
////                    Log.e(TAG, "thisRoomClientMarkers : "+thisRoomClientMarkers);
//
//
//                // 이걸 왜 생성하고 추가한 거지?? 리턴도 안 되는데;;
//                // 명단 추가
////                    ClientInfo client_info = new ClientInfo(email, chatFrom, markerImg); // 입장한사람닉네임과 프사만 객체로 만든다.
////                    clientList.add(client_info);
////                    Log.e(TAG, "client_info : "+client_info.toStringOnly2());
////                    Log.e(TAG, "clientList add 완료: "+clientList);
////
////                    Log.e(TAG, "clientList : "+clientList.size()+"개 "+clientList);
////                    Log.e(TAG, "thisRoomClientMarkers : "+thisRoomClientMarkers.size()+"개 "+thisRoomClientMarkers);
//
////                    Log.e(TAG, "handler로 ui작업(1~4단계 처리를 한다)");
////                    Markers_Players markers_players = whatPurpose_doUI입장(client, handler, jsonArray, thisRoomClientMarkers, clientList, context,
////                            네이버Map, roomName_num, chat_items, rv_chat, rv_list, list_adapter);
////
////                    // 변경된 마커와 명단 자료구조
////                    clientList = markers_players.getClientList();
////                    thisRoomClientMarkers = markers_players.getThisRoomClientMarkers();
////                    Log.e(TAG, "함수 바깥 - thisRoomClientMarkers : "+thisRoomClientMarkers.size()+"개 "+thisRoomClientMarkers);
////                    Log.e(TAG, "함수 바깥 - clientList : "+clientList.size()+"개 "+clientList);
////
//
//                // 본인 명단도 추가한다.
//    //            if (clientList.) { // 여기서 추가해야 참여자가 입장할 때 모든 사람의 명단을 볼 수 있다. (for문)
//    //
//    //                ClientInfo client_info = new ClientInfo(chatFrom, imgURI); // 입장한사람닉네임과 프사만 객체로 만든다.
//    //                clientList.add(client_info);
//    //                Log.e(TAG, "client_info : "+client_info);
//    //                Log.e(TAG, "clientList add 완료: "+clientList);
//    //
//    //            }
//
//                // 마커 객체를 위한 정보가 담긴 HashMap
//
//
//        return clientList; // 한 사람. 마지막으로 들어온 당사자 정보
//    }
//
//
//    public ArrayList<ClientInfo> markerSetImg(Handler handler, ArrayList<ClientInfo> clientList, int i, ImageView marker_img) {
//
//        // UI 작업은 핸들러로 처리해야 한다
//        handler.post(new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void run() {
//
//                try {
//
//                    Thread.sleep(1000);
//                    if (!clientList.get(i).getEmail().equals(myEmail)) { // 나를 제외한 나머지 사람을 마커로 찍는다. 본인은 스레드로 따로 돌린다.
//
//                        Marker entryMarker = clientList.get(i).getMarker(); // for문을 메소드 바깥에 두고 index만 가져옴 // 이렇게 하는 이유 : glide, marker에 핸들러를 따로 적용하기 위해서
//
//
//                        Log.e(TAG, "\nentryMarker : "+entryMarker+
//                                        "\nentryMarker getCaptionText : "+entryMarker.getCaptionText()+
//                                        "\nentryMarker getIcon : "+entryMarker.getIcon());
//
//                        entryMarker.setMap(null); // 마커 삭제
//                        Log.e(TAG, "마커 삭제");
//                        entryMarker.setPosition(new LatLng(clientList.get(i).getLat(), clientList.get(i).getLng()));
//                        Log.e(TAG, "마커 위경도 set");
//                        entryMarker.setCaptionAligns(Align.Top); // 디폴트가 마커 아래
//                        Log.e(TAG, "마커 entryMarker.getIcon() 전 : "+entryMarker.getIcon());
//                        entryMarker.setIcon(OverlayImage.fromView(marker_img)); // UI작업 여기서 함
//                        Log.e(TAG, "마커 entryMarker.getIcon() 후 : "+entryMarker.getIcon());
//                        Log.e(TAG, "마커 setIcon : "+marker_img);
//                        entryMarker.setMap(네이버Map);
//                        Log.e(TAG, "마커 setMap"); // 맨 마지막에 위치해야 함
//                //                                entryMarker.setIcon(OverlayImage.fromBitmap()); // UI작업 여기서 함
//                //                            entryMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_setting)); // UI작업 여기서 함
//
//
//                        clientList.get(i).setMarker(entryMarker); // list에 마커 업뎃
//
//                    } //~나는 제외한 마커찍기
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//        return clientList;
//    }
//
//
//    // *********** 수신 ***********
//    // 입장 후 UI 변경사항
//    // 파라미터가 많은 이유 : M_share_3_join_Map 에서도 이 함수를 쓰니까 뷰들을 파라미터로 뚫어놨다.
//    // 리턴이 2개라서 1개의 객체로 받기 위해 모델을 만들었음
//    // ****************************
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
//    public ArrayList<ClientInfo> whatPurpose_doUI(ClientInfo lastlyClient, Handler handler, ArrayList<ClientInfo> clientList, Context context,
//                                               NaverMap 네이버Map, TextView roomName_num, ImageView marker_img, ArrayList<Chat> chat_items, RecyclerView rv_chat, chat_Adapter adapter,
//                                               RecyclerView rv_list, sharingList_Adapter list_adapter) { String TAG = "whatPurpose_doUI() ";
//
//
//        // UI 작업은 핸들러로 처리해야 한다
//        handler.post(new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void run() {
//
//
//                Log.e(TAG, "\n"+
//                        "\nUI 입장 외 변수 확인.. " +
//                        "\nclientList : " + clientList +
//                        "\nclientList size : " + clientList.size() +
//                        "\npurposes : " + lastlyClient.getPurposes() +
//                        "\nemail : " + lastlyClient.getEmail() +
//                        "\nnickName : " + lastlyClient.getName() +
//                        "\nmarkerImg : " + lastlyClient.getMarkerImg() +
//                        "\nmarker null이겠지: " + lastlyClient.getMarker() +
//                        "\nmsg : " + lastlyClient.getMsg() +
//                        "\nchatTime : " + lastlyClient.getChatTime() +
//                        "\nLat : " + lastlyClient.getLat() +
//                        "\nLng : " + lastlyClient.getLng());
//
////                Marker pickThisMarker = thisRoomClientMarkers.get(lastlyClient.getEmail()); // 위치, 퇴장에서 같이 사용
//
//
////                /*********
////                 * 입장
////                 *********/
////                if (lastlyClient.getPurposes().equals("입장")) { Log.e(TAG, "if문 - 입장"); // 누군가 입장할 때 실행
////
////
////                    // 방참여자수
////                    roomName_num.setText(방이름+"("+clientList.size()+"명)"); //방이름+인원
////
////
////                    // 채팅창 업뎃뎃
////                    Chat chat = new Chat("", lastlyClient.getMsg(), ""); // 파라미터 3개 중 2개 비워두기
////                    chat_items.add(chat); //리사이클러뷰와 연결된 배열에 추가. 결과적으로 리사이클러뷰에 보임
////
/////*
////                    // 마커이미지를 view로 만든다. 왜냐면 url로는 marker.setIcon이 안 되기 때문문
////                    marker_img.setVisibility(View.VISIBLE);
////                    Glide.with(context).load(markerImg).into(marker_img);
////                    Log.e(TAG, "marker_img 보이게하기 // url to view");
////
////
////                    // 직전에 넣었던 마커를 다시 꺼내서 setIcon view 한다.
////                    Marker marker = thisRoomClientMarkers.get(email); // 입장한 사람의 email인지 확인하기
////                    marker.setPosition(new LatLng(Lat, Lng)); //먼저(위치에러뜸)
////                    marker.setIcon(OverlayImage.fromView(marker_img));
////                    marker.setMap(네이버Map);
////                    Log.e(TAG, "입장한 사람의 email로 마커보관함에서 마커 꺼내기. 거기에 marker.setIcon(view)하기");
////*/
////
////                    for(int i=0; i<clientList.size(); i++) { // 내가 이 방에 처음 입장했을 때 실행. 현재 참여중인 방참여자 모두 가져옴. 마커로 비교해서 없는 사람만 추가하기
////
////
////                        // 여기 위치한 이유 : 직전 메소드에서 반복문 할 때 같이 하면 좋겠지만 ui 작업은 여기서 하기로 했기 때문
////                        String email = clientList.get(i).getEmail();
////                        String markerImg = clientList.get(i).getMarkerImg();
////
////                        Log.e(TAG, "email:"+email+
////                                        "\n markerImg:"+markerImg+
////                                        "\n clientList.size():"+clientList.size());
////
////
////                        if (!email.equals(myEmail)) { // 나를 제외한 나머지 사람을 마커로 찍는다. 본인은 스레드로 따로 돌린다.
////
//////                                          marker_img.setVisibility(View.VISIBLE);
////                            Glide.with(context).load(markerImg).into(marker_img); // 반복문을 통해 그 사람의 마커에 그 사람의 img가 삽입된다.
////                            Log.e(TAG, "url to view");
////
/////*                            Glide.with(context)
////                                    .load(markerImg)
////                                    .listener(new RequestListener<Drawable>() {
////                                        @Override
////                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
////                                            Log.e(TAG, "onLoadFailed()");
////                                            return false;
////                                        }
////
////                                        @Override
////                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
////
////                                            Log.e(TAG, "onResourceReady() 성공");
////                                            return false;
////                                        }
////                                    })
////                                    .into(marker_img);*/
////                        } //~나는 제외한 마커찍기
////                        // 직전에 넣었던 마커를 다시 꺼내서 setIcon view 한다.
//////                            Marker marker = thisRoomClientMarkers.get(email); // 입장한 사람의 email인지 확인하기
//////                            marker.setPosition(new LatLng(Lat, Lng)); // 순서 중요! setPosition이 제일 앞에 와야 함
//////                            marker.setIcon(OverlayImage.fromView(marker_img));
//////                            marker.setMap(네이버Map);
//////                            Log.e(TAG, "입장한 사람의 email로 마커보관함에서 마커 꺼내기. 거기에 marker.setIcon(view)하기");
////                    } // ~for()
////
////
////                    // NO : 여기서 rv 추가하면 중복된다.
////                    // returnOneClient_입장() 에서 for문 돌려서 명단 모두 출력함
//                /*********
//                 * 채팅
//                 *********/
////                } else
//                    if (lastlyClient.getPurposes().equals("채팅")) {
//
//
//                    Log.e(TAG, "if문 - 채팅");
//
//                    Chat chat = new Chat(lastlyClient.getName(), lastlyClient.getMsg(), lastlyClient.getChatTime());
//                    chat_items.add(chat); //리사이클러뷰와 연결된 배열에 추가. 결과적으로 리사이클러뷰에 보임
//
//
//                /**********************
//                 * 위치 : n초마다 실행
//                 **********************/
//                } else if (lastlyClient.getPurposes().equals("위치")) { Log.e(TAG, "if문 - 위치");
//
//
//
//                    for(int i=0; i<clientList.size(); i++) { // 내가 이 방에 처음 입장했을 때 실행. 현재 참여중인 방참여자 모두 가져옴. 마커로 비교해서 없는 사람만 추가하기
//                        Log.e(TAG, "목적 : 위치/ 마커 반복문에 들어왔다 / i : "+i);
//
//
//                        // 명단에 있는 이메일과 방금 막 위치정보를 보낸 이메일이 동일하면 명단을 업뎃한다.
//                        if (clientList.get(i).getEmail().equals(lastlyClient.getEmail()) && !lastlyClient.getEmail().equals(myEmail)) { // 본인 마커 제외
//
//
//                            ClientInfo client = clientList.get(i);
//                            Marker oneMarkerInAll = client.getMarker(); // 여러 명 중에 한 명의 마커(반복문)
//                            Log.e(TAG, "oneMarkerInAll : "+oneMarkerInAll);
//                            Log.e(TAG, "\n명단 중에서 위치를 보낸 사람만 업뎃한다."+
//                                            "\noneMarkerInAll : "+oneMarkerInAll+
//                                            "\noneMarkerInAll getCaptionText : "+oneMarkerInAll.getCaptionText()+
//                                            "\noneMarkerInAll getIcon : "+oneMarkerInAll.getIcon());
//
//
//                            // 코드는 길지만 새로 만든 변수에 대입해서 적용하면 마커만 더 생김.
//                            // clientList를 리턴받을 거니까 이 변수 자체에서 변경이 있어야 함
//                            clientList.get(i).getMarker().setMap(null); // 지도에서 삭제
//                            clientList.get(i).getMarker().setPosition(new LatLng(lastlyClient.getLat(), lastlyClient.getLng())); // 위치만 찍는다고 지도에 보이는 게 아님
//                            clientList.get(i).getMarker().setIcon(OverlayImage.fromBitmap(client.getBitmap()));
//                            clientList.get(i).getMarker().setCaptionText(client.getName());
//                            clientList.get(i).getMarker().setMap(네이버Map); // 네이버 지도에 다시 대입해야 보임
//                            Log.e(TAG, "clientList.get(i).getBitmap() : "+clientList.get(i).getBitmap());
//
//
//                        }
//                    }
//
//
//                    // 마커를 전부 꺼내 보이게 하기
////                    thisRoomClientMarkers.forEach((key, value)->{
////
////                        Log.e(TAG, "thisRoomPlayersInfo.forEach - key : "+key+"/value : "+value);
////
////                        // ** 마커 생성, 보관 **
////                        Marker m = value; // 입장하자마자 만들어 놨던 마커를 다시 꺼내서 핸들러를 통해 보인다.
////
////                        m.setCaptionText(nickName); // 닉네임이 보이게 하기
////                        m.setCaptionAligns(Align.Top); // 디폴트가 마커 아래
////                        m.setPosition(new LatLng(Lat, Lng)); // 상시 변경될 예정
////                        m.setMap(네이버Map); // 다시 대입해야 보임
////
////
////                        Log.e(TAG, "마커 보이기 / value.getCaptionText() 닉넴 : "+value.getCaptionText()); // 잘 꺼내왔는데 왜 화면에 보이지 않지?
////                        // 입장에서 안됐던 이유 : 어차피 위경도가 0인데... 입장할 때는 보이게하지 않는다. 위치받으면 setMap하기
////
////
////                    });
//
//
//
//                /*********
//                 * 퇴장
//                 *********/
//                } else if (lastlyClient.getPurposes().equals("퇴장")) { // 나말고 누가 퇴장했다.
//
//
//                    Chat chat = new Chat("", lastlyClient.getMsg(), ""); // msg 는 모두에게 동일해서 인덱스 0도 ok
//                    chat_items.add(chat); // 리사이클러뷰와 연결된 배열에 추가
//
//
//                    // 퇴장한 사람의 마커 삭제
//                    Marker pickThisMarker = lastlyClient.getMarker(); // 퇴장에서 같이 사용
//                    pickThisMarker.setMap(null); // if : 지도에서 삭제 안되면 then : 네이버Map에 setMap하기
//                    Log.e(TAG, "pickThisMarker : "+pickThisMarker);
//                    Log.e(TAG, "pickThisMarker getCaptionText : "+pickThisMarker.getCaptionText());
//                    Log.e(TAG, "pickThisMarker getIcon : "+pickThisMarker.getIcon());
//
//
//                    clientList.remove(lastlyClient); // 자료구조에서 삭제
//
//
//                    // 퇴장한 사람의 명단 삭제
//                    for (int i=0; i<clientList.size(); i++) {
//
//                        Log.e(TAG, "명단 삭제 i : "+i);
//                        Log.e(TAG, "clientList : "+clientList);
//
//                        if (clientList.get(i).getName().equals(lastlyClient.getName())) { // 명단 중에 퇴장하는 닉네임이라면
//                            Log.e(TAG, "clientList.remove 직전 : "+clientList);
//                            clientList.remove(i); // 해당 인덱스 삭제. 아래에서 업뎃
//                            Log.e(TAG, "clientList.remove 직후 : "+clientList);
//                        }
//                    }
//
//                    // 방이름+남은인원
//                    roomName_num.setText(방이름+"("+clientList.size()+"명)"); // 여기 위치! 명단 삭제 후 인원 setText
//
//
//
//
//                /***********
//                 * 강제종료
//                 ***********/
//                } else if (lastlyClient.getPurposes().equals("강제종료")) {
//
//                    // 방퇴장처리를 여기말고 위치스레드안에 위치시킨 이유 : activity.class를 파라미터로 전달이 안 되서.
//                    소켓통신목적 = lastlyClient.getPurposes(); // M_share_3_join_Map.class에서 강제종료할 때 사용하기 위해
//                    Log.e(TAG, "소켓 수신... '강제종료' 한다는 메세지를 받았다/ 소켓통신목적:"+소켓통신목적);
//
//
//                /***********
//                 * 운동시작
//                 ***********/
//                } else if (lastlyClient.getPurposes().equals("운동시작")) {
//
//                    // 방퇴장처리를 여기말고 위치스레드안에 위치시킨 이유 : activity.class를 파라미터로 전달이 안 되서.
//                    소켓통신목적 = lastlyClient.getPurposes(); // M_share_3_join_Map.class에서 강제종료할 때 사용하기 위해
//                    Log.e(TAG, "소켓 수신... '운동시작' 한다는 메세지를 받았다/ 소켓통신목적:"+소켓통신목적);
//
//                }
//
//
//                adapter.notifyDataSetChanged(); // 채팅 업데이트
//                list_adapter.notifyDataSetChanged(); // 명단 업데이트
//                rv_chat.scrollToPosition(chat_items.size()-1);
//                rv_list.scrollToPosition(chat_items.size()-1);
//
//
//                // 위치 이유 : adapter, view가 아래에 코드가 있다면 에러날 것 같아서 어느 if문 안에도 안 들어가더라도 저 뷰와 관련된 것의 아래에 작성한다.
////                if (purpose.equals("방종료")) { // 방장은 여기 올 일이 없다. 이미 나간 상태이고 참여자만이 여길 올 것 이다.
////
////                    Log.e(TAG, "방종료, 방장이 퇴장했으니 방이 터진다!");
////
////                    // 실제 퇴장하는 함수들
////                    //
////                    socketClose_Exit(); // 로그 지저분해서 메소드로 만듦
////
////
////                }
//
//
//                Log.e(TAG, "chat+list rv 업뎃, 스크롤 최하단");
//                Log.e(TAG, "adapter.notifyDataSetChanged()");
////                Log.e(TAG, "thisRoomClientMarkers : "+thisRoomClientMarkers);
//                Log.e(TAG, "clientList : "+clientList);
//
//
//            }
//        });
//
////        Markers_Players markers_players = new Markers_Players(clientList, thisRoomClientMarkers); // 수정 되거나 그대로인 명단과 마커
////        return markers_players; // 객체로 리턴하는 이유 : 리턴을 2개 받아야되서 객체 하나로 만듦
//        return clientList; // 객체로 리턴하는 이유 : 리턴을 2개 받아야되서 객체 하나로 만듦
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
//    public ArrayList<ClientInfo> whatPurpose_doUI입장(ClientInfo lastlyClient, Handler handler, ArrayList<ClientInfo> clientList, Context context,
//                                                  NaverMap 네이버Map, TextView roomName_num, ImageView marker_img, ArrayList<Chat> chat_items, RecyclerView rv_chat, chat_Adapter adapter,
//                                                  RecyclerView rv_list, sharingList_Adapter list_adapter) { String TAG = "whatPurpose_doUI입장() ";
//
//
//        // UI 작업은 핸들러로 처리해야 한다
//        handler.post(new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void run() {
//
//
//                Log.e(TAG, "\n"+
//                        "\nUI입장 변수 확인.. " +
//                        "\nclientList : " + clientList +
//                        "\nclientList size : " + clientList.size() +
//                        "\npurposes : " + lastlyClient.getPurposes() +
//                        "\nemail : " + lastlyClient.getEmail() +
//                        "\nnickName : " + lastlyClient.getName() +
//                        "\nmarkerImg : " + lastlyClient.getMarkerImg() +
//                        "\nmarker null이겠지 : " + lastlyClient.getMarker() +
//                        "\nmsg : " + lastlyClient.getMsg() +
//                        "\nchatTime : " + lastlyClient.getChatTime() +
//                        "\nLat : " + lastlyClient.getLat() +
//                        "\nLng : " + lastlyClient.getLng());
//
//
////                Marker pickThisMarker = lastlyClient.getMarker(); // 위치, 퇴장에서 같이 사용
////                Log.e(TAG, "pickThisMarker : "+pickThisMarker);
////                Log.e(TAG, "pickThisMarker getCaptionText : "+pickThisMarker.getCaptionText());
////                Log.e(TAG, "pickThisMarker getIcon : "+pickThisMarker.getIcon());
////                Marker pickThisMarker = thisRoomClientMarkers.get(lastlyClient.getEmail()); // 위치, 퇴장에서 같이 사용
//
//
//                /*********
//                 * 입장
//                 *********/
//                if (lastlyClient.getPurposes().equals("입장")) { Log.e(TAG, "if문 - 입장"); // 누군가 입장할 때 실행
//
//
//                    // 방참여자수
//                    roomName_num.setText(방이름+"("+clientList.size()+"명)"); //방이름+인원
//
//
//                    // 채팅창 업뎃뎃
//                    Chat chat = new Chat("", lastlyClient.getMsg(), ""); // 파라미터 3개 중 2개 비워두기
//                    chat_items.add(chat); //리사이클러뷰와 연결된 배열에 추가. 결과적으로 리사이클러뷰에 보임
//
//
//                    // 프사마커 삽입
//                    for (int i=0; i<clientList.size(); i++) {
//                        Log.e(TAG, "clientList.size() : "+clientList.size()+" / i :"+i);
//
//                        if (clientList.get(i).getEmail().equals(myEmail)) { // 본인프사는 삽입x
//
//                            Marker marker = new Marker(); // 어디에 위치해서 새로 생성해야 될까? >> 새로 입장할 때
//                            Log.e(TAG, "안드 명단에 없는 사람이라면 추가하기");
//                            marker.setPosition(new LatLng(clientList.get(i).getLat(), clientList.get(i).getLng())); //먼저(위치에러뜸)
//                            marker.setCaptionText(clientList.get(i).getName()); // 닉네임이 보이게 하기
//                            marker.setCaptionAligns(Align.Top); // 디폴트가 마커 아래
//                            marker.setTag(clientList.get(i).getEmail()); // 여기에 위치해야 함 // 값이 없어서 에러떴음
////                            marker.setIcon(OverlayImage.fromBitmap(clientList.get(i).getBitmap())); // 직전 메소드에서 만들어둔 bitmap
//                            marker.setMap(네이버Map); // << handler필요
//                            Log.e(TAG, "marker : "+marker);
//
//                            // 마커 null로 냅두지 않고 업뎃
//                            ClientInfo client = clientList.get(i);
//                            client.setMarker(marker);
//                            clientList.set(i, client);
//                            Log.e(TAG, "마커 null로 냅두지 않고 업뎃 clientList.size() : "+clientList.size()+" / clientList :"+clientList);
//
//                        }
//                    }
//                    // 여기 위치한 이유 : 직전 메소드에서 반복문 할 때 같이 하면 좋겠지만 ui 작업은 여기서 하기로 했기 때문
////                    for(int i=0; i<clientList.size(); i++) { // 내가 이 방에 처음 입장했을 때 실행. 현재 참여중인 방참여자 모두 가져옴. 마커로 비교해서 없는 사람만 추가하기
////                    }
////                        String email = clientList.get(i).getEmail();
////                    String markerImg = clientList.get(i).getMarkerImg();
////
////                    Log.e(TAG, "\nemail:"+email+
////                                    "\n markerImg:"+markerImg+
////                                    "\n clientList.size():"+clientList.size());
//
//
////                    if (!email.equals(myEmail)) { // 나를 제외한 나머지 사람을 마커로 찍는다. 본인은 스레드로 따로 돌린다.
////
////                        Glide.with(context).load(markerImg).into(marker_img); // 반복문을 통해 그 사람의 마커에 그 사람의 img가 삽입된다.
////                        Log.e(TAG, "url to view");
////
////                    } //~나는 제외한 마커찍기
//                }
//
//
//                adapter.notifyDataSetChanged(); // 채팅 업데이트
//                list_adapter.notifyDataSetChanged(); // 명단 업데이트
//                rv_chat.scrollToPosition(chat_items.size()-1);
//                rv_list.scrollToPosition(chat_items.size()-1);
//
//
//                Log.e(TAG, "clientList : "+clientList);
//
//            }
//        });
//
//        return clientList;
//    }
//
//
//    public void 소켓연결(ImageView iv_sendMSG, ConstraintLayout showRV, // 발신
//                     TextView fold, Dialog dialog_chat, Dialog dialog_leave, EditText et_chat_msg, Button btn_chat_send, Button btn_chat_nope, Button btn_share_exit, ImageView btn_trackingStart,
//                     Handler handler, chat_Adapter adapter, ArrayList<Chat> chat_items, RecyclerView rv_chat, Context context, Context activity, TextView roomName_num, ImageView marker_img, boolean isRun,
//                     NaverMap 네이버Map, ArrayList<ClientInfo> clientList, sharingList_Adapter list_adapter, RecyclerView rv_list) { // 수신
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        // 안드로이드 클라이언트의 소켓과 서버의 소켓이 연결한다.
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        new Thread() { //error : android.os.NetworkOnMainThreadException 나기 때문에 스레드로 빼줘야함
//            public void run() { //  this name : Thread-2
//                try {
//
//                    Log.e(TAG, "소켓연결() Thread run");
//                    Log.e(TAG, "서버 주소 : "+ip+" 포트 : "+port);
//
//                    socket = new Socket(ip, port); // 전역변수로 뺐구나. 소켓생성을 왜 여기서하나했네
//                    Log.e(TAG, "소켓 : "+socket);
//
//
//                    // 채팅 송신용 쓰레드 생성
//                    ClientSender sender = new ClientSender(iv_sendMSG, showRV, fold, dialog_chat, dialog_leave, et_chat_msg, btn_chat_send, btn_chat_nope, btn_share_exit, btn_trackingStart,
//                                                           handler, context, activity, isRun, clientList); //방이름도 같이 서버에 전달(어느방에 입장했는지)
//                    sender.start();
//
//
//                    // 채팅 수신용 쓰레드 생성
//                    ClientReceiver receiver = new ClientReceiver(handler, chat_items, adapter, rv_chat, roomName_num, marker_img, isRun, 네이버Map, clientList, list_adapter, rv_list, context);
//                    receiver.start(); //만약 스레드로 안 만들면 전송할 때까지 채팅 못 받음
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } }}.start();
//
//    }
//
//    public void sharingList_AND_chat_rv_Adapter장착(RecyclerView rv_chat, chat_Adapter chat_adapter, RecyclerView rv_list, sharingList_Adapter sharingList_adapter, Context context) {
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        // 리사이클러뷰 어댑터 장착
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
//        rv_chat.setLayoutManager(layoutManager); //보이는 형식, 아래로 추가됨
//        rv_chat.setAdapter(chat_adapter); //최종모습의 recyclerView에 어댑터를 장착
//        Log.e(TAG, "layoutManager : "+layoutManager);
//        Log.e(TAG, "rv_chat : "+rv_chat);
//        Log.e(TAG, "rv_list : "+rv_list); // << error! null 이었음
//
//
//        LinearLayoutManager layoutManager2 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
//        rv_list.setLayoutManager(layoutManager2); //보이는 형식, 아래로 추가됨
//        rv_list.setAdapter(sharingList_adapter); //최종모습의 recyclerView에 어댑터를 장착
//
//
//
////        adapter = new chat_Adapter(getApplicationContext(), chat_items); //생성자에 arraylist 자체를 넣을 수도 있고, adapter안에 존재하는 arr에 추가할 수도 있네.
////
////        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() { //tv_notFound를 커스덤어댑터에서 수정은 되지만 UI업데이트가 안되는 에러가 있었음. 그래서 메인스레드에서 조작하기로 함
////
////            @Override //어댑터 관찰 : 너..삭제했네?
////            public void onItemRangeRemoved(int positionStart, int itemCount) {
////                super.onItemRangeRemoved(positionStart, itemCount);
////
////                Log.e(TAG, "onItemRangeRemoved() adapter.getItemCount() : "+adapter.getItemCount());
////            }
////        });
//
//    }
//
//    public void 채팅메세지배경뷰_보임숨김(int rv_visible상태, Handler mHandler, ConstraintLayout showRecyclerview, TextView fold) {
//
//        Log.e(TAG, "rv_visible상태2 : "+rv_visible상태);
//
//        mHandler.postDelayed(new Runnable() { // error : Only the original thread that created a view hierarchy can touch its views.
//            @Override
//            public void run() {
//                if (rv_visible상태 == 0) { //int 그대로 set이 안돼서 조건문으로 나눔
//                    Log.e(TAG, "VISIBLE로 원상복구");
//                    showRecyclerview.setVisibility(View.VISIBLE); //만약 펴놨으면 다시 펴놓기
//                    fold.setVisibility(View.VISIBLE); //손잡이도
//                } else {
//                    Log.e(TAG, "GONE으로 원상복구");
//                    showRecyclerview.setVisibility(View.GONE); //만약 접혀있었으면 다시 접기
//                    fold.setVisibility(View.VISIBLE);
//                }
//            }
//        }, 100);
//
//    }
//
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 기타 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//
//
//    // 카카오톡으로 방이름, 비번 공유하기
//    // 2단계
//    // 1.메세지 만들기   2.메세지 보내기
//    public void kakaoLink() {
//
//        Log.e(TAG, "카카오링크 메소드 입장");
//
//        //메세지 만들기
//        //피드 탬플릿인데 내가 원하는대로 변경할 예정. 사용자지정으로 하면 간편할 듯
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        // 카톡메세지 보낼 재료
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        long 템플릿ID = 79471; //카카오개발자 메세지 템플릿에서 미리 만들어 둠 (https://developers.kakao.com/tool/template-builder/app/737046/template/79471/component/itl/0)
//        Map<String, String> 템플릿요소 = new HashMap<>(); //데이터 타입을 먼저 지정해주는구나
//        템플릿요소.put("roomName", 방이름); //바뀔 파라미터
//        템플릿요소.put("roomPW", 방비번); //방이름과 방비번이 삽입되서 카톡공유된다.
//
//
//        //카톡 설치여부 확인
//        if (ShareClient.getInstance().isKakaoTalkSharingAvailable(getApplicationContext())) { // LinkClient --> ShareClient 버전 바뀌면서 클래스도 바뀐거였어... 공홈에서 찾음. 한참 헤맸네
//
//            Log.e(TAG, "카카오 설치여부 ㅇㅋ");
//            Log.e(TAG, "템플릿ID : "+템플릿ID);
//            Log.e(TAG, "템플릿요소 : "+템플릿요소);
//
//
//            //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//            // 메세지를 보낸다. (양식이 맞으면 카톡을 열어서 친구목록을 띄운다.)
//            //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//            ShareClient.getInstance().shareCustom(getApplicationContext(), 템플릿ID, 템플릿요소, new Function2<SharingResult, Throwable, Unit>() {
//                @Override
//                public Unit invoke(SharingResult sharingResult, Throwable throwable) {
//
//                    Log.e(TAG, "throwable : "+throwable);
//                    Log.e(TAG, "sharingResult : "+sharingResult);
//
//                    if (throwable != null) { //예외났는지
//
//                        Log.e(TAG, "throwable.toString() : "+throwable.toString());
//
//                    } else if(sharingResult != null) { //결과값이 있다면 공유하기
//
//                        Log.e(TAG, "sharingResult에 값 있어서 들어옴 ");
//                        startActivity(sharingResult.getIntent()); //명시적 intent로 카톡 오픈
//                    }
//                    return null;
//                }
//            });
//
////
////
////            //메세지를 보낸다.(카카오톡을 열어서 보낸다)
////            ShareClient.getInstance().shareDefault(getApplicationContext(), feedTemplate, new Function2<SharingResult, Throwable, Unit>() {
////                @Override
////                public Unit invoke(SharingResult sharingResult, Throwable throwable) {
////
////
////                    Log.e(TAG, "throwable : "+throwable);
////                    Log.e(TAG, "sharingResult : "+sharingResult);
////
////                    if (throwable != null) { //예외났는지
////
////                        Log.e(TAG, "throwable.toString() : "+throwable.toString());
////
////                    } else if(sharingResult != null) { //결과값이 있다면 공유하기
////
////                        Log.e(TAG, "sharingResult에 값 있어서 들어옴 ");
////                        startActivity(sharingResult.getIntent()); //명시적 intent로 카톡 오픈
////                    }
////                    return null;
////
////                }
////            });
//
//        } else {
//            Log.e(TAG, "카카오 설치여부 ㄴㄴㄴ");
//            Toast.makeText(getApplicationContext(), "카카오톡이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show(); //이동하기전에 토스트외치기
//        }
//
//    }
//
//    static String 위경도to주소(double lat, double lng, Context context) { //long은 안되나?
//
//        String TAG = "위경도to주소() ";
//        Log.e(TAG, "위경도to주소() lat : "+lat+" / lng : "+lng);
//        String 추출된주소 = null;
//
//        Geocoder g = new Geocoder(context, Locale.getDefault());
//        try {
//            List<Address> address = g.getFromLocation(lat, lng, 10);
//            String 주소 = address.get(0).getAddressLine(0);
//
//            Log.e(TAG, "address : "+address);
//            Log.e(TAG, "주소 : "+주소);
//
//            추출된주소 = 주소추출(주소); // 원하는 주소만 추출 >> 서울특별시 동작구 사당4동
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e(TAG, "입출력오류");
//        }
//        return 추출된주소;
//    }
//
//
//    static String 주소추출(String 주소) {
//
//        //대한민국 서울특별시 동작구 사당4동 300-73
//        // >> 서울특별시 동작구 사당4동
//        //대한민국 전라남도 장흥군 용산면 덕암리 산10
//        // >> 전라남도 장흥군 용산면
//
//        String target동 = "동 "; // 동 인곳을 타겟으로 삼아서
//        String target면 = "면 "; //지방의 경우 군/면/리
//        int target_num동 = 주소.indexOf(target동); //인덱스를 알아낸다.
//        int target_num면 = 주소.indexOf(target면);
//
//
//        String 리턴받는수정주소; //추출한 주소를 이 변수에 담아 리턴할 것임
//
//        if(target_num동 != -1) {//"동"이라면
//
//            리턴받는수정주소 = 주소.substring(5, target_num동+1); //서울 ~ 동까지 추출
//            return 리턴받는수정주소;
//
//        } else if(target_num면 != -1) { //"면"이라면
//
//            리턴받는수정주소 = 주소.substring(5, target_num면+1);//면으로 편집한 주소
//            return 리턴받는수정주소;
//
//        } else { //동도 면도 아니라면
//
//            return 주소; //else로 나머지 다 때려넣어야지. 여기때문에 에러났음
//        }
//
//    }
//
//
//    static public void socketClose_Exit() {
//
//        String TAG = "socketClose_Exit()";
//        try {
//            if (socket != null) {
//
//                Log.e(TAG, "* socket.isClosed() : "+socket.isClosed());
//                socket.close();
//                Log.e(TAG, "** socket.isClosed() : "+socket.isClosed());
//                Log.e(TAG, "*** socket : "+socket); // 소켓을 close해도 객체가 null인건 아니다다
//
//            }                            } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    //직접퇴장, 시간지나 강제퇴장 두 가지라서 메소드를 따로 만듦
//    static public void 방퇴장처리(Context context) {
//
//        String TAG = "방퇴장처리()";
//        onBackPressed(context); //이동 >> M_main.class
//
//
//        //퇴장할 때 같이 마커도 지워줘야 새로 방 만들었을 때 이전방의 경로가 찍히는 불상사를 방지한다.
//        마커리스트.clear();
//        마커위도리스트.clear();
//        마커경도리스트.clear();
//        Log.e(TAG, "마커리스트[] : "+마커리스트);
//
//
//        //변수도 비우기
//        방이름 = null;
//        방비번 = null;
//        iamLeader = false;
//
//
//        //쉐어드 비우기 1
//        SharedPreferences shared = context.getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능
//        SharedPreferences.Editor Editor = shared.edit();
//
//        Editor.putString("myRoom_no", null);
//        Editor.putBoolean("myRoomActive", false); //변수만 지우면 Login에서 다시 삽입됨
//        Editor.putBoolean("iamLeader", false); //더 이상 방장아님
//
//        Editor.apply(); //실질 저장
//
//        //쉐어드 비우기 2
//        SharedPreferences sharedWatch = context.getSharedPreferences("stopwatch", Activity.MODE_PRIVATE); //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능
//        SharedPreferences.Editor Editor_watch = sharedWatch.edit();
//        stopWatch쉐어드삭제(Editor_watch); //쉐어드 값을 삭제하면 재입장했을 때 시간을 이어가지 않는다.
//
//    }
//
//
//    @SuppressLint("LongLogTag")
//    private void 시작_경과시간() {
//
//        Log.e(TAG, "시작_경과시간()");
//        Log.e(TAG, "currentTimeMillis : "+String.valueOf(System.currentTimeMillis()));
//        Log.e(TAG, "chronometer.getBase() : "+String.valueOf(chronometer.getBase())); //뷰에 나타난 시간?
//
//
//        //처음 or 리셋 후
//        시작한시점 = System.currentTimeMillis();
//        chronometer.setBase(시작한시점); //시작버튼 누른 순간이 베이스가 됨, 쉐어드에 저장
//        chronometer.start(); //기준점을 시작으로 뷰에 보이기 시작
//        stopWatch쉐어드저장();
//
//    }
//
//    //퇴장, 강제퇴장일 경우 실행
//    public static void stopWatch쉐어드삭제(SharedPreferences.Editor editor) {
//
//        //리셋
//        editor.clear(); //다 지워서 새로 방 입장한다고 했을 때 진행안되고 재입장이면 진행되게 함_
//        editor.apply(); //저장
//        Log.e("쉐어드 time : ", "삭제!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//
//    }
//
//    //시작버튼을 누를 경우 실행
//    private void stopWatch쉐어드저장() {
//
//        //값 삽입
//        Log.e("쉐어드저장() 함수 들어옴 /시작한시점 :", String.valueOf(시작한시점));
//
//        SharedPreferences shared = getSharedPreferences("stopwatch", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = shared.edit();
//
//        editor.putLong("시작한시점", 시작한시점);  //현재시간
//        editor.putLong("주어진시간", 주어진시간);  //방이 끝나도록 정해진 시간
//
//        //값 저장
//        editor.apply();
//        Log.e("쉐어드 time : ", "저장++++++++++++++++++++++++++++++++++");
//
//    }
//
//
//
//    //php에서 받은위도경도 데이터인 json을 java배열로 바꾸기
//    private void 위도경도jsonToArraylist(String str위도값들, String str경도값들) {
//
//
//        ArrayList<Double> dbArr위도 = new ArrayList<Double>();
//        ArrayList<Double> dbArr경도 = new ArrayList<Double>();
//        Log.e(TAG, "Json배열을 Java배열로 변환하기 ");
//
//        try {
//
//            Log.e(TAG, "Json배열을 Java배열로 변환하기 try 들어옴");
//            JSONArray jsonArr위도 = new JSONArray(str위도값들);
//            JSONArray jsonArr경도 = new JSONArray(str경도값들);
//
//            if (jsonArr위도 != null) {
//
//                Log.e(TAG, "Json배열을 Java배열로 변환하기 if 들어옴");
//                for (int i=0; i<jsonArr위도.length(); i++){
//                    Double 삽입할위도 = Double.valueOf(jsonArr위도.get(i).toString());
//                    Double 삽입할경도 = Double.valueOf(jsonArr경도.get(i).toString());
//                    dbArr위도.add(삽입할위도);
//                    dbArr경도.add(삽입할경도);
//                }
//            }
//            Log.e(TAG, "Json배열을 Java배열로 변환하기 if 나옴");
//            Log.e(TAG, "Json배열을 Java배열로 변환하기 dbArr위도 : "+dbArr위도);
//
//
//            //마침내 위도와 경도를 담아냈다.
//            마커위도리스트 = dbArr위도;
//            마커경도리스트 = dbArr경도;
//            Log.e(TAG, "Json배열을 Java배열로 변환하기 마커위도리스트 : "+마커위도리스트);
//            Log.e(TAG, "Json배열을 Java배열로 변환하기 마커경도리스트 : "+마커경도리스트);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    private void 마커셋팅(NaverMap 네이버Map) {
//
//        Log.e(TAG, "마커셋팅() 들어옴 ");
//
//        //sttic변수로 담아둔 마커객체 다시 지도에 뿌리기
//        for (int i=0; i<마커위도리스트.size(); i++) { //마커리스트가 null인 경우가 있어서 마커위도리스트를 기준으로 삼음
//
//            Log.e(TAG, "마커셋팅() i : "+i);
//
//            Marker marker = new Marker(); //다시 마커객체 생성해서 다시 위치에 삽입함
//            marker.setPosition(new LatLng(마커위도리스트.get(i), 마커경도리스트.get(i))); //static변수로 액티비티가 파괴되어도 사용할 수 있게함
////            marker.setPosition(new LatLng(마커위도리스트.get(i), 마커경도리스트.get(i))); //static변수로 액티비티가 파괴되어도 사용할 수 있게함
//            marker.setWidth(90);
//            marker.setHeight(90);
//            marker.setMap(네이버Map); //다시 대입한다 /해야 보임
//            routeNum.setText(String.valueOf(i+1)); //인덱스+1해야 자연수가 나옴
//            marker.setIcon(OverlayImage.fromView(routeNum)); //숫자먼저 set하고 뷰를 삽입한다.
//        }
//
//    }
//
//
//
//    public String 시분초변환() {
//
//        h = (int)(time /(3600*초));
//        m = (int)(time - h*(3600*초))/(60*초);
//        s = (int)(time - h*(3600*초)- m*(60*초))/초 ;
//
//        //"00시00분14초"가 지저분해보여서 0시 0분이면 안보이기
//        h시간m분s초 = (h > 0 ? h + "시간 " : "") + (m > 0 ? m + "분 " : "") + (s > 0 ? s + "초" : ""); //삼항연산자    조건 ? 참 : 거짓
//        String t = "경과시간 "+(h < 10 ? "0"+h: h)+":"+(m < 10 ? "0"+m: m)+":"+ (s < 10 ? "0"+s: s); //"00:12:34" 이렇게 표기
//
//        Log.e("시분초변환() ...time", String.valueOf(time));
//        Log.e("시분초변환() ...t", t);
//        Log.e("시분초변환() ...h시간m분", h시간m분s초);
//        Log.e("시분초변환() s : ", String.valueOf(s));
//        Log.e("시분초변환() m : ", String.valueOf(m));
//        Log.e("시분초변환() h : ", String.valueOf(h));
//
//
//        if(time > 주어진시간 - 5000) { //강제종료 전 미리 고지
//
//            Log.e("시분초변환() ", "s == 10");
//            Toast.makeText(getApplicationContext(), "위치공유방 종료까지 "+5+"초가 남았습니다.", Toast.LENGTH_LONG).show(); // 실행할 코드
//            //토스트 외에도 소리나는 알림으로 말해주면 좋을 듯
//            //우선 넘어가고 보충하고 싶으면 여기 업그레이드하자
//        }
//
//        //강제종료 시간 조건
//        if(time > 주어진시간) { //20초가 지나면 강제종료 된다.
//
//            Log.e("시분초변환() ", "s == 20");
//            return "종료";
//        }
//
//
//        return t;
//    }
//
//    private void 쉐어드저장() {
//
//        Log.e(TAG, "쉐어드저장() 입장!!!!!!!!!!");
//
//
//        //쉐어드에 어느 단계에 진입했는지 저장
//        SharedPreferences shared = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능
//        SharedPreferences.Editor Editor = shared.edit();
//
//        Log.e(TAG, "쉐어드저장() 변수확인" +
//                "\nmyRoomActive : "+myRoomActive+
//                "\nmyRoom_no : "+myRoom_no+
//                "\n방이름 : "+방이름+
//                "\n방비번 : "+방비번
//        );
//
//        Editor.putBoolean("myRoomActive", myRoomActive);
//        Editor.putString("myRoom_no", myRoom_no);
//        Editor.putString("방이름", 방이름);
//        Editor.putString("방비번", 방비번);
//        Editor.putBoolean("iamLeader", true);
//
//        Editor.apply(); //실질 저장
//
//        String test = shared.getString("myRoom_no", null);
//
//        Log.e(TAG, "쉐어드저장() test : "+test);
//
//        Log.e(TAG,"스태틱 확인용" +
//                "\nemail : "+myEmail+
//                "\npw : "+myPw+
//                "\nmyName : "+myName+
//                "\nmyImg : "+myImg+
//                "\ndate : "+myDate+
//                "\nmyRoomActive : "+myRoomActive+
//                "\n방이름 : "+방이름+
//                "\n방비번 : "+방비번+
//                "\nmyRoom_no : "+myRoom_no+
//                "\niamLeader : "+iamLeader+
//                "\nsnsLogin : "+mysnsLogin ); //확인용
//        Log.e(TAG, "쉐어드저장() 저정완료");
//
//    }
//
//
//    //경로설정을 끝냈으면 이제 마커를 누르더라도 반응(삭제다이얼로그)이 없도록 이벤트 덮어씌우기
//    private void 마커비활성화() {
//
//        // 리스트안에 있는 마커객체들을 하나하나 이벤트를 준다.
//        int 마커비활성화 = 마커리스트.indexOf(마커if문용); //아무객체라도 상관없이 객체가 하나라도 있으면 비활성화시키기
//        Log.e(TAG, "마커비활성화 : "+마커비활성화); //객체있으면 0~ 없으면 -1리턴
//
//
//        if(마커비활성화 >= 0) { //객체가 하나라도 있다면
//            Log.e(TAG, "if(마커비활성화 >= 0)");
//
//            for (int i=0; i<마커리스트.size(); i++) { //마커가 들어있는 리스트만큼 반복한다. (전부 비활성화시키기 위해)
//
//                Log.e(TAG, "i"+i);
//                Marker 찍은마커들 = 마커리스트.get(i); //0 1 2
//
//                //객체 하나하나에 마커클릭이벤트를 false로 리턴해준다.
//                찍은마커들.setOnClickListener(new Overlay.OnClickListener() {
//                    @Override
//                    public boolean onClick(@NonNull Overlay overlay) {
//                        Log.e(TAG, "덮어버릴까?");
//                        return false;
//                    }
//                });
//            }
//        } //~if()
//    }
//
//
//    //경로설정 중
//    public void 숨김_명단() { //gone이 먼저 실행되어야 중복되서 안 보인다.
//
//        fold.setVisibility(View.GONE); //시작할 때 숨겨진 상태. 왜냐면 '경로지정완료' 버튼을 눌러야 방이 활성화되고 참가자명단을 볼 수 있음
//        showRecyclerview.setVisibility(View.GONE); //리사이클러뷰 숨김
//        topLayout.setVisibility(View.GONE); //방제목 숨김
//
//    }
//
//    //경로설정 중
//    public void 보임_명단() { //gone이 먼저 실행되어야 중복되서 안 보인다.
//
//        fold.setVisibility(View.VISIBLE); //화살표도 보이기
//        showRecyclerview.setVisibility(View.VISIBLE); //리사이클러뷰 보이기
//        topLayout.setVisibility(View.VISIBLE); //화살표도 보이기
//
//    }
//
//
//    //경로완료 후 or 재입장
//    public void 숨김_경로() {
//
//        notice.setVisibility(View.GONE); //경로마커 안내문 공간까지 없애기
//        btnRouteDone.setVisibility(View.GONE); // 경로지정완료 버튼 없애기
//
//    }
//
//
//    //캡쳐할 때 숨겨놨던 뷰를 다시 보이게 하기(캡쳐 직후에 위치)
//    public void 보임_초록버튼() {
//
//        iv_sendMessage.setVisibility(View.VISIBLE);
//        iv_setting.setVisibility(View.VISIBLE);
////        iv_compass.setVisibility(View.VISIBLE);
//        rv_chat.setVisibility(View.VISIBLE); //초록버튼들과 채팅방은 셋트
//
//    }
//
//    public void 숨김_초록버튼() {
//
//        iv_sendMessage.setVisibility(View.GONE); //허리부분만 쓸거라 상하단 뷰는 안 숨김
//        iv_setting.setVisibility(View.GONE);
//        iv_compass.setVisibility(View.GONE);
//        rv_chat.setVisibility(View.GONE); //초록버튼들과 채팅방은 셋트
//
//    }
//
//
//    //캡쳐할 때 숨겨놨던 뷰를 다시 보이게 하기(캡쳐 직후에 위치)
//    public void 숨김_트래킹스타트() {
//
//        btn_trackingStart .setVisibility(View.GONE);
//        tv_trackingStart.setVisibility(View.GONE);
//
//    }
//
//    public void 보임_트래킹스타트() {
//
//        btn_trackingStart .setVisibility(View.VISIBLE);
//        tv_trackingStart.setVisibility(View.VISIBLE);
//
//    }
//
//
//    //네이버에서 제공하는 캡쳐함수를 이용해 bitmap에 담는다.
//    private void 캡쳐Thread() {
//
//        Log.e(TAG, "캡쳐() 입장 ");
//        Log.e(TAG, "네이버Map  : "+네이버Map);
//
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//
//                Log.e(TAG, "1  캡쳐() run() 입장 ");
//
//                handler2.post(new Runnable() { //post : 다른 스레드로 메세지(객체)를 전달하는 함수
//
//                    @Override
//                    public void run() { //마커의 위치만 변경
//
//                        Log.e(TAG, "2  캡쳐() run() handler2 run() 입장 ");
//                        Log.e(TAG, "3  네이버Map (여기는 null이 아니겠지) : "+네이버Map);
//                        Log.e(TAG, "4  snapshot 콜백함수 전  ");
//
//                        //콜백(캡쳐함수사용 시 여기를 부른다)
//                        NaverMap.SnapshotReadyCallback snapshot = new NaverMap.SnapshotReadyCallback() {
//                            @Override
//                            public void onSnapshotReady(@NonNull Bitmap bitmap) {
//
//                                Log.e(TAG, "7  onSnapshotReady () 입장 bitmap : "+bitmap);
//                                Log.e(TAG, "8  onSnapshotReady () 입장 bitmapCapture  : "+bitmapCapture); // ==null
//                                bitmapCapture = cropBitmap(bitmap); //3등분 중 허리만 가져오기
//                                Log.e(TAG, "8  onSnapshotReady () 입장 bitmapCapture  : "+bitmapCapture); // !=null
//                                보임_초록버튼(); //캡쳐할 때 거슬렸던 버튼을 숨겨놨고 이제 캡쳐했으니 보이게 하기
//
//                                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                                // 캡쳐한 지도 저장
//                                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//                                retrofit_썸네일업뎃(); //서버에 저장하기
//                                Log.e(TAG, "retrofit_썸네일업뎃() 바깥");
//
//
//                            }
//                        };
//
//                        Log.e(TAG, "5  snapshot 콜백 함수 후");
//                        네이버Map.takeSnapshot(snapshot); //콜백함수(필요한 파라미터가 여기있어서 여기에 기능넣음)를 불러오는 네이버지도 캡쳐 함수
//                        Log.e(TAG, "6  takeSnapshot() 후 ");
//
//                    }
//                });
//
//            }
//        }).start(); //start()붙이면 바로실행시킨다.
//
//    }
//
//    //bitmap to file
//    private MultipartBody.Part 사진_body(Bitmap bitmap, String filename) { // FileOutputStream.write(byte[])' on a null
//
//        //create a file to write bitmap data
//        Log.e(TAG, "getCacheDir().toString() : "+getCacheDir().toString()); // /data/user/0/com.example.iamhere/cache
//        Log.e(TAG, "filename : "+filename); // /data/user/0/com.example.iamhere/cache
//        File f = new File(getCacheDir().toString(), filename);
//        Log.e(TAG, "f : "+f); // /data/user/0/com.example.iamhere/cache
//        Log.e(TAG, "f.getName() : "+f.getName()); // cache
//
//        //Convert bitmap to byte array
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(); //ByteArrayOutputStream은내부적으로 저장공간이 있어 해당메소드를 이용해서 출력하게되면
//                                                                // 출력되는 모든 내용들이 내부적인 저장 공간에 쌓이게 된다.
//        Log.e(TAG, "bos : "+ bos); // 빈값
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 40 /*ignored for PNG*/, bos); // quality : 00% 압축한다.
//        byte[] bitmapdata = bos.toByteArray(); //ByteArrayOutputStream의 내용을 바이트 배열로 반환
//
//        Log.e(TAG, "bitmapdata : "+ bitmapdata); //[B@5e06b45
//
//        //write the bytes in file
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(f); // fos : null --> 파일이름만드니까 값 대입 됨!!
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            assert fos != null;
//            fos.write(bitmapdata);
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //import okhttp3.RequestBody / 타입지정
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f); //실제파일객체에 타입정한다.
//        Log.e(TAG, "*/* requestFile객체 생성 : "+requestFile);
//
//        //import okhttp3.MultipartBody / 요청바디이름, 파일명, 파일객체를 body라는 객체 안에 담음
//        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", f.getName(), requestFile); //body안에 여러개 집어넣음
//        Log.e(TAG, "333 body 생성 : "+body);
//
//        return body; //리턴받은 body를 레트로핏을 통해 서버에 전송
//    }
//
//
//
//    //비트맵 자르기(원하는 위치와 사이즈로) : 3토막 중 중간토막을 쓸거임
//    static public Bitmap cropBitmap(Bitmap original) {
//
//        Bitmap result = Bitmap.createBitmap(original
//                , 0 //X 시작위치
//                , original.getHeight() / 3 //Y 시작위치 (원본의 3/1지점)
//                , original.getWidth() //넓이 (원본의 기존 넓이)
//                , original.getHeight() / 3); //높이 (원본의 3/1 크기)
//
//        if (result != original) {
//            original.recycle();
//        }
//
//        return result;
//    }
//
//
//    //위경도 마커 찍는 스레드(본인 위치)
//    public void 위도경도Thread(int sec) { //화면전환될 때 종료시킴
//
//        //ㅡㅡㅡ
//        // 마커
//        //ㅡㅡㅡ
//        Marker marker = new Marker(); //마커객체를 반복문 바깥에 위치
//        Log.e(TAG, "marker 객체생성");
//        Log.e(TAG, "marker.setIcon 저 이미지로 마커를 찍겠다/result넣음");
//
//
//        //서브스레드 객체 -> 핸들러 -> 메세지큐 -> 루퍼 -> 핸들러 -> 메인메서드 객체 전달 완료
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() { //1초마다 벨류값 1씩 증가시키는 스레드임
//
//                if(마커합성bitmap == null) { //마커합성이미지 없어?
//
//                    마커프사null이면set하기(); //없다면 만들어준다. 한 번만. 스레드안에서.
//                }
//
//                while ((isRun)) { //false가 되면 멈춤 - 화면전환시
//
//                    Log.e(TAG, "마커합성bitmap 333 : "+마커합성bitmap);
//
//                    handler2.post(new Runnable() { //post : 다른 스레드로 메세지(객체)를 전달하는 함수
//
//                        @Override
//                        public void run() { //마커의 위치만 변경
//
////                            Log.e(TAG, "2위도, 경도 : "+위도+ ", " + 경도);
//
//                            marker.setMap(null); //기존마커 없앤다
//                            marker.setPosition(new LatLng(위도, 경도)); //먼저
//                            marker.setMap(네이버Map); //다시 대입한다
//
//                            if(마커합성bitmap != null) { //비트맵 값 있으면 그대로 삽입
//                                marker.setIcon(OverlayImage.fromBitmap(마커합성bitmap)); //미리 main에서 대입된 변수
//
//                            } else { //에러표시
//                                marker.setIcon(OverlayImage.fromResource(R.drawable.hiking)); //test
//
//                            }
//                        }
//                    });
//                    try {
//                        Thread.sleep(1000*sec); //1초마다 내 위치 reset
//                    } catch (Exception e) {
//                    }
//                }
//            }
//        }).start(); //start()붙이면 바로실행시킨다.
//    }
//
//
//    public static void onBackPressed(Context context) {
//
//        String TAG = "onBackPressed()";
//        Log.e(TAG, "onBackPressed() 누름");
//
//
//        //플래그사용해서 원하는 화면이동
//        Intent intent = new Intent(context, M_main.class); //메인이로 이동
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP ); //M_main외는 지움
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//        //  M_main              (가는곳)
//        //  M_share_1           (삭제할곳)
//        //  M_share_1_1create   (삭제할곳)
//        //  M_share_2_Map       (현재)
//        //  ABCD  A call        (Task)
//        //  A                   (Task)
//    }
//
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//    //                                           생명주기
//    // (필수 : 네이버 지도 api에서 Mapview를 사용하려면 아래 생명주기를 생성하라 함. 이유는 말 안 함)
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.e(TAG, "onStart");
//
//    }
//
//    @Override
//    protected void onRestart() { //다른액티비티 back하면 여기로 옴
//        super.onRestart();
//        Log.e(TAG, "onRestart ~~~~");
////        위도경도Thread(); //마커찍는 스레드, 위치 다시 찾지 않는다.
//
//        //a에서 b액티비티로 이동후 back하면 onRestart()로 들어옴
//        //b에서 a로 다시 누르면 onCreate로 감
//        //그래서 두 곳은 중복되지 않아서 둘 다 스레드를 위치
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.e(TAG, "onResume");
//
//        if(네이버Map != null) {
//            Log.e(TAG, "onResume /네이버Map != null");
//            네이버Map.setLocationTrackingMode(LocationTrackingMode.Follow); //마이페이지같은 곳 들어갔다가 다시 위치공유방 들어갈 때 내 위치를 못 잡음
//            //맵을 띄우고 onResume을 한 번 더 띄우길래 여기에 초점맞추는 보조기능을 가진 코드를 추가함
//        }
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.e(TAG, "onPause");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.e(TAG, "onStop");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.e(TAG, "onDestroy... isRun=false");
//        isRun = false; //스레드 종료
//    }
//
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//    public void ID() {
//
//        mapView = findViewById(R.id.map2);
//        roomName_num = (TextView)findViewById(R.id.roomName_num);
//
//        //접힘(숨김)
//        fold = (TextView)findViewById(R.id.fold);
//        chronometer = (Chronometer)findViewById(R.id.chronometer);
//        textView12 = (TextView)findViewById(R.id.textView12);
//        btn_share_Friends = (Button)findViewById(R.id.btn_share_Friends); //카카오톡 공유하기+@
//        btn_share_exit = (Button)findViewById(R.id.btn_share_exit);
//        showRecyclerview = (ConstraintLayout)findViewById(R.id.showRecyclerview);
//        routeNum = (TextView)findViewById(R.id.routeNum); //마커 순서
//        btnRouteDone = (TextView)findViewById(R.id.textView14); //마커 순서
//        topLayout = (FrameLayout)findViewById(R.id.frameLayout4); //최상단 방이름(n명)적힌 레이아웃
//
//        //숨김(우측초록버튼)
//        iv_sendMessage = (ImageView)findViewById(R.id.imageView);
//        iv_setting = (ImageView)findViewById(R.id.imageView3);
//        iv_compass = (ImageView)findViewById(R.id.imageView4);
//
//        //숨김(시작버튼, 시작textview)
//        btn_trackingStart = (ImageView)findViewById(R.id.btn_roomStart);
//        tv_trackingStart = (TextView)findViewById(R.id.textView16); //최상단 방이름(n명)적힌 레이아웃
//
//        //다이얼로그
//        dialog = new Dialog(M_share_2_Map_copy3.this); //다이얼로그 객체 생성
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 제거
//        dialog.setContentView(R.layout.marker_delete); //보여줄 xml레이아웃과 연결
//        button_del = dialog.findViewById(R.id.button_del); //다이얼로그 객체이름을 앞에 붙여야한다.
//        button2_cancel = dialog.findViewById(R.id.button2_cancel); //다이얼로그 > 마커삭제 > 취소
//
//        //다이얼로그 - 채팅보내기
//        dialog_chat = new Dialog(M_share_2_Map_copy3.this);
//        dialog_chat.requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 제거
//        dialog_chat.setContentView(R.layout.dialog_chat_send); //보여줄 xml레이아웃과 연결
//        et_chat_msg = dialog_chat.findViewById(R.id.et_chat_msg); // 입력란. 위에서 inflate했으니 id를 가져올 수 있다.
//        btn_chat_send = dialog_chat.findViewById(R.id.btn_chat_send); // 보내기 버튼
//        btn_chat_nope = dialog_chat.findViewById(R.id.btn_chat_nope); // 취소 버튼. 창 사라지게하기
//
//        // 다이얼로그 - 방종료창
////        dialog_leave = new Dialog(M_share_2_Map.this);
////        dialog_leave.requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 제거
////        dialog_leave.setContentView(R.layout.dialog_leave); //보여줄 xml레이아웃과 연결
//
//        //경로안내 없어짐(숨김)
//        notice = (TextView)findViewById(R.id.textView13); //마커 순서
//
//        //마커
//        marker_img = findViewById(R.id.marker_img);
//        //소켓
//        rv_chat = (RecyclerView) findViewById(R.id.rv_chat); // 채팅
//        rv_list = (RecyclerView) findViewById(R.id.rv_list); // 명단
//
//
//
//    }
//
//    public void 변수확인() {
//
//        boolean bitmapImg = false;
//        if(mySmallBitmapImg !=null) bitmapImg = true;
//        Log.e(TAG,"스태틱 변수확인() " +
//                "\nemail : "+myEmail+
//                "\npw : "+myPw+
//                "\nmyName : "+myName+
//                "\nmyImg : "+myImg+
//                "\nmySmallBitmapImg 값있는지: "+bitmapImg+
//                "\nmyRoomActive : "+myRoomActive+
//                "\nmyRoom_no : "+myRoom_no+
//                "\n마커합성bitmap  : "+마커합성bitmap +
//                "\ndate : "+myDate+
//                "\nsnsLogin : "+mysnsLogin ); //확인용
//
//    }
//
//    @SuppressLint("LongLogTag")
//    static void 마커프사null이면set하기() {
//
//        String TAG = "static 함수 마커프사null이면set하기()";
//
//        Log.e(TAG, "시작");
//        마커합성bitmap = StringToBitmap(mySmallBitmapImg);
//        Log.e(TAG, "끝");
//
//    }
//
//
//}
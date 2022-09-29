package com.example.iamhere.socket;

import static com.example.iamhere.L_login.h시간m분s초;
import static com.example.iamhere.L_login.myEmail;
import static com.example.iamhere.L_login.myImg;
import static com.example.iamhere.L_login.myMarkerImg;
import static com.example.iamhere.L_login.myName;
import static com.example.iamhere.L_login.myRoom_no;
import static com.example.iamhere.socket.LocationService.pw;
import static com.example.iamhere.socket.LocationService.socket;
import static com.example.iamhere.L_login.경도;
import static com.example.iamhere.L_login.모든위치업뎃_sec;
import static com.example.iamhere.L_login.방이름;
import static com.example.iamhere.L_login.소켓통신목적;
import static com.example.iamhere.L_login.위도;
import static com.example.iamhere.M_share_2_Map.createOkHttpClient;
import static com.example.iamhere.M_share_2_Map.방퇴장처리;
import static com.example.iamhere.M_share_3_join_Map.retrofit_퇴장업뎃_exit;
import static com.example.iamhere.socket.ClientReceiver.socketClose_Exit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.iamhere.Interface.Sharing;
import com.example.iamhere.M_share_2_Map;
import com.example.iamhere.Model.ClientInfo;
import com.example.iamhere.Model.Sharing_room;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
// 메시지를 전송하는 Thread
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
public class ClientSender extends Thread {

    //        private DataOutputStream dos; // 데이터를 스트림통로를 통해 보낼 것이다.
//        private PrintWriter pw;
//        private DataOutputStream dos; // 데이터를 스트림통로를 통해 보낼 것이다.
    String TAG = "ClientSender.class";
    private String name;
    private ImageView iv_sendMSG, btn_trackingStart;
    private ConstraintLayout showRV;
    private TextView fold, tv_trackingStart;
    private Dialog dialog_chat, dialog_leave;
    private EditText et_chat_msg;
    private Button btn_chat_send, btn_chat_nope, btn_share_exit;
    private Handler handler;
    private Context context, activity;
    private boolean isRun;
    private ArrayList<ClientInfo> clientList;


    // 생성자. 방장과 참여자 두 곳에서 사용 됨
    public ClientSender(ImageView iv_sendMSG, ConstraintLayout showRV, TextView fold, TextView tv_trackingStart, Dialog dialog_chat, Dialog dialog_leave,
                        EditText et_chat_msg, Button btn_chat_send, Button btn_chat_nope, Button btn_share_exit, ImageView btn_trackingStart,
                        Handler handler, Context context, Context activity, boolean isRun, ArrayList<ClientInfo> clientList) {

        this.iv_sendMSG = iv_sendMSG;
        this.showRV = showRV;
        this.fold = fold;
        this.dialog_chat = dialog_chat;
        this.dialog_leave = dialog_leave;
        this.et_chat_msg = et_chat_msg;
        this.btn_chat_send = btn_chat_send;
        this.btn_chat_nope = btn_chat_nope;
        this.btn_share_exit = btn_share_exit;
        this.btn_trackingStart = btn_trackingStart;
        this.tv_trackingStart = tv_trackingStart;
        this.handler = handler;
        this.context = context;
        this.activity = activity;
        this.isRun = isRun;
        this.clientList = clientList;

        try {
            pw = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /***************************************** 소켓 통신 ******************************************/
    @Override
    public void run() { //  this myName : Thread-3


        Log.e(TAG, "run() socket : "+socket);
        Log.e(TAG, "유저myName, myRoom_no, 방이름 : "+myName+myRoom_no+방이름);
        Log.e(TAG, "isRun : "+isRun);
        Log.e(TAG, "clientList : "+clientList);
        Log.e(TAG, "위도 : "+위도+"/경도:"+경도);


        //시작하자마자 문자3개를 서버로 전송 (1번만)
        if (!socket.isClosed()) { // 소켓이 열려있다면

            Log.e(TAG, "!socket.isClosed()"); // ok

            pw.println(myEmail); // 이메일로 식별하기
            pw.println(myName); // 1개만 보내야되네..? --> ㄴㄴ 보낸 순서대로 받으면 됨
            pw.println(myImg);
            pw.println(myMarkerImg); // 이 유저의 마커url을 채팅서버에 보내면 소켓으로 입장할 때 전송될거임
            pw.println(myRoom_no);
            pw.println(방이름);
            pw.println(위도); // 입장할 때 위경도 보내기! (기존엔 위치n초뒤에 업뎃할 때 보냈음. 따로할 이유가 없음)
            pw.println(경도);

            pw.flush();

        }


        //ㅡㅡㅡㅡㅡ
        // UI 작업
        //ㅡㅡㅡㅡㅡ
        handler.post(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {

                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                // 메세지보내기 창 : 다이얼로그 창이 뜬다.
                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                iv_sendMSG.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        //ㅡㅡㅡㅡㅡㅡㅡㅡ
                        // 키보드 띄우기
                        //ㅡㅡㅡㅡㅡㅡㅡㅡ
                        int rv_visible상태 = showRV.getVisibility(); //메세지보내기 전 상태(상태복구를 위한 변수)
                        showRV.setVisibility(View.GONE); //지도를 가림
                        fold.setVisibility(View.GONE); //지도를 가림

                        Log.e(TAG, "메세지보내기 창 띄우기");
                        Log.e(TAG, "메세지보내기 버튼 클릭 / rv_visible상태 : "+rv_visible상태);

                        dialog_chat.show(); //다이얼로그 띄우기
                        et_chat_msg.requestFocus(); //입력란을 대상으로


                        et_chat_msg.postDelayed(new Runnable() { //이렇게하니까 동작됨. Runnable없이 하려니까 작동하지 않았음
                            @Override
                            public void run() {

                                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); // InputMethodManager : 키보드제어 클래스
                                inputMethodManager.showSoftInput(et_chat_msg, InputMethodManager.SHOW_IMPLICIT); //바로 키보드 띄우기

                            }
                        }, 10); //초는 상관없음


                        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                        // 메세지 보내기 버튼
                        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                        btn_chat_send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                String message = String.valueOf(et_chat_msg.getText()); //친구에게 보낼 메세지가 담김
                                Log.e(TAG, "메세지보내기 버튼 클릭 / message : "+message);
                                Log.e(TAG, "메세지보내기 버튼 클릭 / 위도, 경도 : "+위도+"/"+경도);


                                new Thread() { //  this name : Thread-5
                                    public void run() { // Thread in Thread 가 아니라 나란히 스레드가 실행되는 것임.
                                        Log.e(TAG, "run()에 들어옴");


                                        // 메세지 전송
                                        pw.println(message); // 채팅내용
                                        pw.flush();


                                        dialog_chat.dismiss(); //실행끝났으면 창 꺼지기
                                        et_chat_msg.setText(""); //빈문자열 넣기
                                        채팅메세지배경뷰_보임숨김(rv_visible상태, handler, showRV, fold); //참여자 명단 리사이클러뷰 원상복구

                                    }
                                }.start();

                            }
                        });

                        //ㅡㅡㅡㅡㅡㅡ
                        // 취소 버튼 : 창 사라짐
                        //ㅡㅡㅡㅡㅡㅡ
                        btn_chat_nope.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog_chat.dismiss(); //실행끝났으면 꺼지기
                                et_chat_msg.setText(""); //빈문자열 넣기

                                채팅메세지배경뷰_보임숨김(rv_visible상태, handler, showRV, fold); //참여자 명단 리사이클러뷰 원상복구
                            }
                        });


                        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                        // 다이얼로그 바깥 클릭 이벤트 : 뷰 숨김, 보임때문에
                        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                        dialog_chat.setOnCancelListener(
                                new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {

                                        Log.e(TAG, "dialog_chat.setOnCancelListener");
                                        채팅메세지배경뷰_보임숨김(rv_visible상태, handler, showRV, fold); //참여자 명단 리사이클러뷰 원상복구

                                    }
                                }
                        );

                    }
                });

                //ㅡㅡㅡㅡ
                // 방 퇴장 : 같은 방 자동재입장 X (비번입력하면 재입장은 가능하도록 할거임)
                //ㅡㅡㅡㅡ
                btn_share_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG, "UI 작업... 퇴장 클릭");


                        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                        // 다이얼로그   1.방장   2.참여자
                        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                        if (clientList.get(0).getEmail().equals(myEmail)) { // 방장과 참여자 다이얼로그가 다르게 보이게하기
                            Log.e(TAG, "방장이 퇴장 버튼 클릭");


                            dialog_leave = new AlertDialog.Builder(activity) // 현재 Activity의 이름 입력.
                                    .setMessage("\n                   방을 종료하시겠습니까?\n")
                                    .setNeutralButton("모두에 대해 공유 종료", new DialogInterface.OnClickListener() { //확인을 왼쪽에 해야 실수로 더블클릭하는 경우를 막을 수 있음
                                        public void onClick(DialogInterface dialog, int which){


                                            Log.e(TAG, "모두에 대해 공유 종료 버튼 클릭"); // 방장이 모든 참여자를 종료시킬 수 있다.
                                            Toast.makeText(context, "위치공유방이 종료되었습니다.\n나의 기록에서 조회하실 수 있습니다.", Toast.LENGTH_LONG).show(); // 실행할 코드


                                            new Thread() { // Thread in Thread 가 아니라 나란히 스레드가 실행되는 것임.
                                                public void run() { // // 메세지 전송하고 소켓끊기

                                                    pw.println("강제종료"); // 채팅내용
                                                    pw.flush();

                                                }
                                            }.start();


                                            Log.e(TAG, "ClientSender() h시간m분s초 : "+h시간m분s초);
                                            new M_share_2_Map().retrofit_퇴장업뎃_removeRoom(h시간m분s초); // 서버에 방 비활성화, 시간들 저장(시간 null뜸!!)
                                            방퇴장처리(context); //퇴장 후 뒷처리 : 변수, 쉐어드 초기화
                                            socketClose_Exit(); // 로그 지저분해서 메소드로 만듦
                                            isRun = false; // 자기위치 찍기 종료
                                        }
                                    })
                                    .setPositiveButton("       취소       ", new DialogInterface.OnClickListener() { //일부러 띄워쓰기 한거임. 간격조절
                                        public void onClick(DialogInterface dialog, int which){
                                            Log.e(TAG, "취소 버튼 클릭");                }})
                                    .show();


                        } else { // 참여자가 퇴장버튼 누를때 (방장과 별도여야 함)

                            btn_share_exit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.e(TAG, "참여자가 퇴장 버튼 클릭");


                                    dialog_leave = new androidx.appcompat.app.AlertDialog.Builder(activity) // 현재 Activity의 이름 입력.
                                            .setMessage("\n          정말 나가시겠습니까?\n")
                                            .setNeutralButton("              예              ", new DialogInterface.OnClickListener() { //확인을 왼쪽에 해야 실수로 더블클릭하는 경우를 막을 수 있음
                                                public void onClick(DialogInterface dialog, int which){


                                                    Log.e(TAG, "참여자가 나가기 버튼 클릭"); // 방장이 모든 참여자를 종료시킬 수 있다.
//                                                    if ()
                                                    Toast.makeText(context, "위치공유방이 종료되었습니다.\n나의 기록에서 조회하실 수 있습니다.", Toast.LENGTH_SHORT).show(); // 에러 원인 : context를 전달받지 못했음 getapplicationContext쓰면 에러남


                                                    new Thread() { // Thread in Thread 가 아니라 나란히 스레드가 실행되는 것임.
                                                        public void run() { // 메세지 전송하고 소켓끊기

                                                            pw.println("퇴장"); // 채팅내용
                                                            pw.flush();

                                                        }
                                                    }.start();

                                                    //명단에 추가가 안 되어있음.. 우선 tcp/ip를 하고 디비를 손대자
                                                    retrofit_퇴장업뎃_exit(myRoom_no, myEmail, h시간m분s초); //위치공유방 참여자 명단 변경(퇴장시간, 소요시간)
                                                    방퇴장처리(context); //퇴장 후 뒷처리 : 변수, 쉐어드 초기화
                                                    socketClose_Exit();
                                                    isRun = false; // 자기위치 찍기 종료
                                                    소켓통신목적 = "퇴장"; // M_share_3_join_Map.class에서 퇴장할 때 토스트를 사용하기 위해(2안에서 context파라미터 사용해도 안됨;;)
                                                    Log.e(TAG, "퇴장 토스트를 띄우기 위한 변수 - 소켓통신목적:"+소켓통신목적); // M3에서 마커스레드 안에서 사용됨.



                                                }
                                            })
                                            .setPositiveButton("         아니요            ", new DialogInterface.OnClickListener() { //일부러 띄워쓰기 한거임. 간격조절
                                                public void onClick(DialogInterface dialog, int which){}})
                                            .show();
                                }
                            });
                        }
                    }
                });

                //ㅡㅡㅡㅡㅡㅡㅡㅡ
                // 등산 시작버튼 : 누르면 시간이 카운트된다.
                //ㅡㅡㅡㅡㅡㅡㅡㅡ
                btn_trackingStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //방장 진입 단계(activity) 1.방이름   2.마커   3.시작버튼   4.운동중
                        //中 4단계

                        Log.e(TAG, "btn_trackingStart 버튼 클릭");
                        // 숨김_트래킹스타트
                        btn_trackingStart .setVisibility(View.GONE);
                        tv_trackingStart.setVisibility(View.GONE);


                        new Thread() { // Thread in Thread 가 아니라 나란히 스레드가 실행되는 것임.
                            public void run() { // // 메세지 전송하고 소켓끊기

                                pw.println("운동시작"); // 초시계 시작점을 방장과 참여자가 동일하게 하기위해 전송
                                pw.flush();

                            }
                        }.start();

                        //
                        // 다른 참여자들의 스톱와치가 같이 시작된다.
                        //
                        retrofit_Room_RoomUser업뎃(context); //등산시작했다고 신호를 보내면 현재시간을 DB에 업뎃 //방운동시작시간 =/= 개인운동시작시간 (왜냐면 도중에 들어온사람은?)

                    }
                });


            }
        });


        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 위치 전송 (마커용)
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        while (!socket.isClosed()) { // 채팅방에서 나가도 계속 반복되어서 조건문을 소켓이 열리는경우만으로 바꿈 // 반복문이라 맨 뒤로 뺐음

            try {


                Log.e(TAG, "송신...while문"); // try를 나가면 whille문에 의해 계속 반복되어서 찍힌다

                pw.println(String.valueOf(위도) ); // 소수점 7자리네
                pw.println(String.valueOf(경도) ); // double과 string이 섞여서 서버에서 다른 타입에 담기면 아예 못 담는 문제 발생 -> string으로 변환해서 보냄
                pw.flush();


                Thread.sleep(모든위치업뎃_sec*1000); // n초마다 위치를 전송한다.
                Log.e(TAG, "송신...while 위도 : "+위도+ ", 경도 : "+경도);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } // ~while()

    }

    /*********************************************************************************************/


    //시작버튼 누르면 db에 운동시작한 시간이 저장된다. 보냈다는걸 알 수 있는 의미없는 데이터를 보낸다.
    @SuppressLint("LongLogTag")
    private void retrofit_Room_RoomUser업뎃(Context context) {

        //이벤트 : 시작버튼 클릭
        //보낼 값 : 시작했다는 아무 값
        //결과 : 룸테이블 - 시작시간, 룸유저테이블 시작시간 업뎃
        //응답 : sucess

        Log.e(TAG, "retrofit_Room_RoomUser업뎃() 입장!!!!!!!!!!");

        //레트로핏 객체 생성, 빌드
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder() //Retrofit 인스턴스 생성
                .baseUrl("http://15.164.129.103/")  //baseUrl 등록
                .addConverterFactory(GsonConverterFactory.create())  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
                .client(createOkHttpClient()) //네트워크 통신 로그보기(서버로 주고받는 파라미터)
                .build();



        //ㅡㅡㅡㅡㅡ
        // 시간업뎃 : 방 생성시간 X 운동시작시간 O
        //ㅡㅡㅡㅡㅡ
        Sharing SharingRoomCreate = retrofit.create(Sharing.class);   // 레트로핏 인터페이스 객체 구현
        Log.e(TAG, "retrofit_Room_RoomUser업뎃()... 위도 :"+위도+"/경도:"+경도);
        String 추출한주소 = 위경도to주소(위도,경도, context);
        Log.e(TAG, "추출한주소? : "+추출한주소); //응답값은 db에 잘 들어갔는지 확인만하기. 사용할 일은 없음

        Call<Sharing_room> call = SharingRoomCreate.startTracking(myRoom_no, myEmail, 추출한주소); //보냄 : 방번호로 특정, email로 방 참여자 명단 만들기(명단은 '등산중'에 참여한 사람만 해당된다.)

        // 주
        // 소

        //네트워킹 시도 2
        call.enqueue(new Callback<Sharing_room>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Sharing_room> call, Response<Sharing_room> response) {

                Sharing_room result = response.body();
                assert result != null;
                Log.e(TAG, "성공인가? : "+result.getResponse()); //응답값은 db에 잘 들어갔는지 확인만하기. 사용할 일은 없음

            }

            @Override
            public void onFailure(Call<Sharing_room> call, Throwable t) {
                Log.e("onFailure : ", t.getMessage());
            }
        });

    }


    private String 위경도to주소(double lat, double lng, Context context) { //long은 안되나?

        String TAG = "위경도to주소() ";
        Log.e(TAG, "위경도to주소() lat : "+lat+" / lng : "+lng);
        String 추출된주소 = null;

        Geocoder g = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> address = g.getFromLocation(lat, lng, 10);
            String 주소 = address.get(0).getAddressLine(0);

            Log.e(TAG, "address : "+address);
            Log.e(TAG, "주소 : "+주소);

            추출된주소 = 주소추출(주소); // 원하는 주소만 추출 >> 서울특별시 동작구 사당4동

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "입출력오류");
        }
        return 추출된주소;
    }

    private String 주소추출(String 주소) {

        //대한민국 서울특별시 동작구 사당4동 300-73
        // >> 서울특별시 동작구 사당4동
        //대한민국 전라남도 장흥군 용산면 덕암리 산10
        // >> 전라남도 장흥군 용산면

        String target동 = "동 "; // 동 인곳을 타겟으로 삼아서
        String target면 = "면 "; //지방의 경우 군/면/리
        int target_num동 = 주소.indexOf(target동); //인덱스를 알아낸다.
        int target_num면 = 주소.indexOf(target면);


        String 리턴받는수정주소; //추출한 주소를 이 변수에 담아 리턴할 것임

        if(target_num동 != -1) {//"동"이라면

            리턴받는수정주소 = 주소.substring(5, target_num동+1); //서울 ~ 동까지 추출
            return 리턴받는수정주소;

        } else if(target_num면 != -1) { //"면"이라면

            리턴받는수정주소 = 주소.substring(5, target_num면+1);//면으로 편집한 주소
            return 리턴받는수정주소;

        } else { //동도 면도 아니라면

            return 주소; //else로 나머지 다 때려넣어야지. 여기때문에 에러났음
        }

    }



    public void 채팅메세지배경뷰_보임숨김(int rv_visible상태, Handler mHandler, ConstraintLayout showRecyclerview, TextView fold) {

        Log.e(TAG, "rv_visible상태2 : "+rv_visible상태);

        mHandler.postDelayed(new Runnable() { // error : Only the original thread that created a view hierarchy can touch its views.
            @Override
            public void run() {
                if (rv_visible상태 == 0) { //int 그대로 set이 안돼서 조건문으로 나눔
                    Log.e(TAG, "VISIBLE로 원상복구");
                    showRecyclerview.setVisibility(View.VISIBLE); //만약 펴놨으면 다시 펴놓기
                    fold.setVisibility(View.VISIBLE); //손잡이도
                } else {
                    Log.e(TAG, "GONE으로 원상복구");
                    showRecyclerview.setVisibility(View.GONE); //만약 접혀있었으면 다시 접기
                    fold.setVisibility(View.VISIBLE);
                }
            }
        }, 100);

    }


} // ~inner class(송신)

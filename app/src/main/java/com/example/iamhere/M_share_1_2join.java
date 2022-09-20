package com.example.iamhere;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.iamhere.L_login.myEmail;
import static com.example.iamhere.L_login.myName;
import static com.example.iamhere.L_login.myRoom_no;
import static com.example.iamhere.L_login.방이름;
import static com.example.iamhere.L_login.방비번;
import static com.example.iamhere.M_share_2_Map.retrofit객체;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iamhere.Interface.Sharing;
import com.example.iamhere.Model.Sharing_room;
import com.kakao.sdk.share.ShareClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class M_share_1_2join extends AppCompatActivity {


    String TAG = "M_share_1_2join";
    EditText et_roomName, et_roomPW; //방장이 설정한 방이름, 비번을 기입한다. 서버에 입장가능한지 물어보고 '성공'이면 지도3으로 전환한다.
    Button btn_JoinRoomDone;  //방만드는 버튼, 정규식확인, 서버에 요청보내기


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    // 참여자가 만들어진 방에 입장하기위해 방이름과 비밀번호를 입력하는 액티비티
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mshare1_2join);
        Log.e(TAG, "onCreate()");
        ID();

        //버튼 이벤트
            //서버 요청
                //지도3 입장

        //ㅡㅡㅡㅡㅡㅡ
        // 입장 버튼
        //ㅡㅡㅡㅡㅡㅡ
        btn_JoinRoomDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "btn_JoinRoomDone 클릭");

                //요청 send : 이름, 비번
                //mysql : 해당하는 row를 찾음, 1개 찾으면 [방번호] 응답
                //응답 : [방번호]

                retrofit입장가능묻기(et_roomName.getText().toString(), et_roomPW.getText().toString()); //입력한 방이름, 방비번을 서버전송

            }
        });




    } //~onCreate()


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    private void retrofit입장가능묻기(String room이름,String room비번) { //리사이클러뷰에서 보내온 방번호를 서버에 키값으로 보낸다.


        Log.e(TAG,"위치공유방 참여자 변수 확인" +
                "\nretrofit입장가능묻기() 메소드"+
                "\nmyRoom_no : "+myRoom_no+  //null
                "\nroom이름 : "+room이름+
                "\nroom비번 : "+room비번
        );


        Sharing retrofit참여자입장 = retrofit객체().create(Sharing.class); //Sharing에서 구현한 인터페이스로 요청보낸다.
        Call<Sharing_room> call = retrofit참여자입장.joinRoom(room이름, room비번, myEmail); //방이름과 비번이 일치하면 입장한다.


        //네트워킹 시도
        call.enqueue(new Callback<Sharing_room>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Sharing_room> call, Response<Sharing_room> response) { //응답은 성공인지만 안다.

                Log.e(TAG, "response : " + response);
                Log.e(TAG, "response.body() : " + response.body());
                Log.e(TAG, "response.body().getResponse() : " + response.body().getResponse());
                Log.e(TAG, "response.body().getUserNickName() : " + response.body().getUserNickName());


                //유효한 방이 존재하면 위치공유방 지도로 이동(참여)
                if(!response.body().getResponse().equals("1개가아님")) { //해당하는 방 갯수가 1개 존재한다면(유일하다면)

                    Log.e(TAG, "입장 가능");
                    Toast.makeText(getApplicationContext(), "해당 위치공유방에 입장합니다.", Toast.LENGTH_SHORT).show();


                    //스태틱 변수대입 + 쉐어드 저장
                    스태틱변수대입(room이름, room비번, response);
                    shared참여자방입장저장(방이름, 방비번, myRoom_no); //스태틱 변수대입+쉐어드 저장은 한 쌍


                    //위치공유방 지도로 이동
                    Intent intent = new Intent(M_share_1_2join.this, M_share_3_join_Map.class); //참여자는 M_share_3_join_Map 위치공유방에 입장
                    intent.putExtra("방장닉넴", response.body().getUserNickName());
                    Log.e(TAG, "입장 가능");

                    startActivity(intent); //플래그로 스택정리 안 한다. 지도3에서 뒤로가기 누를 때 플래그로 처리할 예정
                    Log.e(TAG, "startActivity()");


                } else {

                    Log.e(TAG, "입장 불가능.. 없어진 방이다.");
                    Toast.makeText(getApplicationContext(), "유효한 방이 아닙니다.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Sharing_room> call, Throwable t) {
                Log.e("onFailure... : ", t.getMessage());
            }
        });

    }

    //스태틱변수를 대입하고 로그로 값을 확인한다.
    private void 스태틱변수대입(String room이름, String room비번, Response<Sharing_room> response) {

        방이름 = room이름;
        방비번 = room비번;
        myRoom_no = response.body().getResponse(); //서버에서 응답으로 [방번호]를 보냄

        Log.e(TAG,"위치공유방 참여자 입장 변수 확인" +
                "\nretrofit입장가능묻기() 메소드... 응답값이 방번호다"+
                "\nmyRoom_no : "+myRoom_no+
                "\n방이름 : "+방이름+
                "\n방비번 : "+방비번
        );
    }

    //스태틱이 변동이 있으면 쉐어드 파일도 변경
    public void shared참여자방입장저장(String 방이름, String 방비번, String myRoom_no) {

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능
        SharedPreferences.Editor autoLoginEdit = auto.edit();

        autoLoginEdit.putString("방이름", 방이름);
        autoLoginEdit.putString("방비번", 방비번);
        autoLoginEdit.putString("myRoom_no", myRoom_no);

        autoLoginEdit.apply(); //실질 저장
    }


    //ID선언 보기좋게 정리
    private void ID() {

        et_roomName = (EditText)findViewById(R.id.editText5); //방이름 입력
        et_roomPW = (EditText)findViewById(R.id.editText6); //비번입력
        btn_JoinRoomDone = (Button)findViewById(R.id.btn_JoinRoomDone); //'입장'버튼

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
    }
}



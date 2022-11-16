package com.example.iamhere;

import static com.example.iamhere.L_login.bitmapCapture;
import static com.example.iamhere.L_login.myRoomActive;
import static com.example.iamhere.M_share_2_Map.isServiceRunning;
import static com.example.iamhere.socket.myService.socket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class M_share_1 extends AppCompatActivity {


    String TAG = "M_share_1";
    Button btn_createRoom;  //'위치공유 방 만들기' 버튼
    Button btn_joinRoom;  //'방에 참여하기' 버튼


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mshare1);
        Log.e(TAG, "onCreate()");
        ID();
        Log.e(TAG, "3 M_share_1.class isServiceRunning true : "+isServiceRunning(getApplicationContext()));
        Log.e(TAG, "3 socket : "+socket);


        //이미 위치공유방에 입장해있다면
        //지도로 이동 --> M_share_2_Map


        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 위치공유방 만들기 버튼 : 방이름,비번 설정
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        btn_createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "btn_createRoom onClick()");
                Log.e(TAG, "btn_createRoom onClick() myRoomActive : "+myRoomActive);

                Intent intent = new Intent(getApplicationContext(), M_share_1_1create.class); //방이름, 비번 설정하러 이동
                startActivity(intent); //화면전환

            }
        });


        btn_joinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "btn_joinRoom onClick()");
                Intent intent = new Intent(getApplicationContext(), M_share_1_2join.class);
                startActivity(intent); //방목록 보임
            }
        });

    } //~onCreate()


    //onCreate에 쓸 ID변수들
    public void ID() {

        btn_createRoom = (Button) findViewById(R.id.btn_createRoom); //btn방만들기, btn방참여 중에서 전자
        btn_joinRoom = (Button) findViewById(R.id.btn_joinRoom); //btn방만들기, btn방참여 중에서 후자
    }

}
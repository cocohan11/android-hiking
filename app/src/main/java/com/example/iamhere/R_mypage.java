package com.example.iamhere;

import static com.example.iamhere.L_login.myDate;
import static com.example.iamhere.L_login.myEmail;
import static com.example.iamhere.L_login.myPw;
import static com.example.iamhere.L_login.myName;
import static com.example.iamhere.L_login.myImg;
import static com.example.iamhere.L_login.mySmallBitmapImg;
import static com.example.iamhere.L_login.mysnsLogin;
import static com.example.iamhere.L_login.originFile;
import static com.example.iamhere.L_login.strNickName;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class R_mypage extends AppCompatActivity {

    String TAG = "R_mypage"; //log.e()의 첫 번째 파라미터로 클래스명 넣기
    LinearLayout navi_map2; //지도. 뒤로가면 다시 나의공간으로 돌아감
    LinearLayout navi_myPlace2; //현재들어온 페이지. 클릭리스너x
    LinearLayout navi_setting2; //설정
    CircleImageView img_R_prof; //원형 프로필사진. 서버에서 저장해둔 사진 불러오기, static으로 변수 저장해두기(자주불러올거니까)
    TextView tv_R_nickname; //쉐어드에 저장해둔 값 가져와서 로그인할 때 static으로 대입. 변수 그대로 가져오기
    TextView tv_R_joinDate; //위와같음
    Button btn_R_edit; // R_mypage.class >> L_profile.class또는 새로 생성해서 layout재사용하기
    ImageView img_R_back; //배경사진
    boolean BoolImg; //값유무확인용
    LinearLayout 위치공유참여기록; //위치공유가 끝나고 난 뒤에 데이터를 리사이클러뷰로 볼 수 있는 곳으로 들어가는 버튼



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rmypage);
        Log.e(TAG, "onCreate");
        ID();




        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 프로필사진, 닉네임, 가입일 삽입
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        if(mySmallBitmapImg != null) BoolImg = true;
        Log.e(TAG,
                "\nemail : "+myEmail+
                        "\npw : "+myPw+
                        "\nmyName : "+myName+
                        "\nmyImg : "+myImg+
                        "\ndate : "+myDate+
                        "\nmySmallBitmapImg (boolean) : "+BoolImg+
                        "\nsnsLogin : "+mysnsLogin ); //확인용
        //목표 1.사진삽입
        //목표 2.닉네임 static변수 바로 settext하기, 가입일은 static말고 쉐어드에서 꺼내기
        //디바이스에 사진이 없을 수 있으니 서버에서 사진을 가져와야 함.

        //사진 삽입
        if(myImg == null) { //설정한 사진이 없다면..
            Glide.with(getApplicationContext()).load(R.drawable.ic_prof_first).into(img_R_prof); //기본사진 삽입
            Log.e(TAG, "onCreate() 하자마자 myImg == null");

        } else { //설정한 사진이 있다면..
            Glide.with(getApplicationContext()).load(myImg).into(img_R_prof); //설정 or 카톡프사
            Log.e(TAG, "onCreate() 하자마자 myImg != null");
        }

        //닉네임 삽입
        tv_R_nickname.setText(myName); //static변수사용

        //가입일 삽입
        Log.e(TAG, "myDate :"+myDate);
        if(myDate != null) myDate = myDate.substring(0,7); //0~7자리까지만 추출
        tv_R_joinDate.setText(myDate); //가입일

        
        
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 나의 위치공유 참여 기록 (액티비티 이동)
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        위치공유참여기록.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), R_record_sharing.class); //나의 기록 >> 위치공유 기록
                startActivity(intent);
                Log.e(TAG, "startActivity()");
                Log.e(TAG, "getApplicationContext() : "+getApplicationContext()); //com.example.iamhere.GlobalApplication@862a246

            }
        });



        //ㅡㅡㅡㅡ
        // navi 1 : 지도
        //ㅡㅡㅡㅡ
        navi_map2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), M_main.class); // 나의 공간 >> 지도첫화면
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //지도로 갈때는 플래그 다 삭제하고 이동(홈이니까)
                Log.e(TAG, "Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK");
                startActivity(intent);

            }
        });

        //ㅡㅡㅡㅡㅡ
        // navi 2 : 설정
        //ㅡㅡㅡㅡㅡ
        navi_setting2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), S_account.class); // 나의 공간 >> 설정
                startActivity(intent);

            }
        });

        //ㅡㅡㅡㅡㅡ
        // navi 3 : 프로필 수정
        //ㅡㅡㅡㅡㅡ
        btn_R_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), L_profile.class); // 나의 공간 >> 프로필작성(회원가입직후페이지 재사용)
                Log.e(TAG, "myName : "+myName);
                intent.putExtra("UserNickName", myName);
                startActivity(intent);

            }
        });


    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
        Log.e(TAG, "onStart myImg :"+myImg);

    }

    //프로필 수정 후 가장 먼저 돌아오는 곳
    @Override
    protected void onRestart() { //onCreate가 아닌 이곳에 글라이드로 이미지 넣어줘야 변경된 사진에 적용됨
        super.onRestart();

        //사진(원본), 닉네임 수정

        Log.e(TAG, "onRestart");
        Log.e(TAG, "onRestart strNickName : "+strNickName); //고정값(변동값 : myName)
        Log.e(TAG, "onRestart originFile : "+originFile); //고정값 (변동값 : myImg)

        if(originFile != null) Glide.with(getApplicationContext()).load(originFile).into(img_R_prof); //원본삽입(서버x)
        if(strNickName != null) tv_R_nickname.setText(strNickName); //닉네임 바로 삽입

//
//        //스레드
//        new Thread(new Runnable() {
//            int value;
//            boolean isRun = true;
//
//
//            @Override
//            public void run() {
//                Log.e(TAG, "run()");
//
//                while (isRun) {
//
//                    value += 1;
//                    Log.e(TAG, "사진수정 value :"+value); //스레드 돌아가는지 확인용
//
//                    runOnUiThread(new Runnable() {
//                        @SuppressLint("NewApi")
//                        public void run() {
//                            // UI
//                            Log.e(TAG, "run() UI여기서 작업");
//                            if(!수정전img.equals(myImg)) isRun = false; //스레드 스톱
//                            Glide.with(getApplicationContext()).load(myImg).into(img_R_prof); //n초마다 load
//                            //이름도 똑같이 set해주기
//                        }
//                    });
//                    try {
//                        Thread.sleep(1000);
//                    } catch (Exception e) {
//                        Log.e(TAG, "사진수정 에러났다"); //스레드 돌아가는지 확인용
//                    }
//                }
//            }
//        }).start();
//
//        Glide.with(getApplicationContext()).load(myImg).into(img_R_prof); //내프로필사진 static변수
//        tv_R_nickname.setText(myName); //닉네임 static변수

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume myImg :"+myImg);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause myImg :"+myImg);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop myImg :"+myImg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    public void ID() {
        navi_map2 = (LinearLayout) findViewById(R.id.navi_map2); //지도
        navi_myPlace2 = (LinearLayout) findViewById(R.id.navi_myPlace2); //나의공간
        navi_setting2 = (LinearLayout) findViewById(R.id.navi_setting2); //설정
        img_R_prof = (CircleImageView) findViewById(R.id.img_R_prof); //프로필사진-원형
        tv_R_nickname = (TextView) findViewById(R.id.tv_R_nickname); //닉네임
        tv_R_joinDate = (TextView) findViewById(R.id.tv_R_joinDate); //가입일
        btn_R_edit = (Button) findViewById(R.id.btn_R_edit); //닉네임,프로필사진 수정버튼
        img_R_back = (ImageView) findViewById(R.id.img_R_back); //바탕사진(클릭하면 전체화면으로 보기)
        위치공유참여기록 = (LinearLayout) findViewById(R.id.linearLayout); //위치공유가 끝나고 난 뒤에 데이터를 리사이클러뷰로 볼 수 있는 곳으로 들어가는 버튼

    }

}
//package com.example.iamhere;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//
//public class splash extends AppCompatActivity {
//
//    String TAG = "splash";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//        Log.e(TAG, "onCreate()");
//
//        moveMain(3); //2초 후 M_main 액티비티로 넘어감
//
//    }
//
//    private void moveMain(int sec) {
//
//        Log.e(TAG, "moveMain() 시작");
//
//        new Handler().postDelayed(new Runnable() //UI를 건드는거니까 핸들러 사용
//        {
//            @Override
//            public void run()
//            {
//
//                //if문으로 다른 곳으로 향하게 하기
//
//                SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//                String UserEmail = auto.getString("UserEmail",null); //없다면 null이다?
//
//                if(UserEmail != null) { //자동로그인 - 로컬
//                    Log.e(TAG, "경우1) 자동로그인 - 로컬");
//
//                    Intent intent = new Intent(getApplicationContext(), L_login.class);
//                    startActivity(intent);	//intent 에 명시된 액티비티로 이동
//                }
//
//
//                finish();	//현재 액티비티 종료
//            }
//        }, 1000 * sec); // sec초 정도 딜레이를 준 후 시작
//
//        Log.e(TAG, "moveMain() 끝");
//
//    } // 참고) thread.sleep 으로 delay를 주면 layout 로딩도 같이 멈추므로 n초 후 실행되는 postDelayed를 이용
//
//}
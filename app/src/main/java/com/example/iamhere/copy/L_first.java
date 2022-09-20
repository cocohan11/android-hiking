//package com.example.iamhere;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.kakao.auth.Session;
//import com.kakao.util.exception.KakaoException;
//
//public class L_first extends AppCompatActivity {
//
//    String TAG = "L_first"; //log.e()의 첫 번째 파라미터로 클래스명 넣기
//    Button btn_join;
//    Button btn_login;
//    private long backKeyPressedTime = 0; //뒤로가기 2번 종료
//    ImageView img_kakao; //카카오 로그인버튼(test)
//    Session session;
//    String aa;
//
//    private SessionCallback sessionCallback = new SessionCallback() {
//        @Override
//        public void onSessionOpened() {
//            Log.e(TAG, "onSessionOpened()");
//        }
//        @Override
//        public void onSessionOpenFailed(KakaoException exception) {
//            Log.e(TAG, "onSessionOpenFailed()");
//        }
//    };
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_lfirst);
//        Log.e(TAG, "onCreate()");
//        ID(); //onCreate에 선언해서쓰기
//
//
//        session = Session.getCurrentSession(); //?
//        session.addCallback(sessionCallback); //?
//        Log.e(TAG, "getCurrentSession");
//
//
//
//
//        //자동로그인
//        //L_first.class는 통과시키는 역할
//        //autoLogin.xml파일에 UserEmail,UserPwd 을 저장해두었음.
//        //로그아웃을 한 적이 있다면 비어있을것이고 로그아웃을 한 적 없다면 값이 존재할 것이다.
//        //if(UserEmail!null)UserEmail,UserPwd를 바탕으로 로그인시도를 한다. (비번변경할 때 쉐어드에 저장된 비번도 변경하기)
//        //레트로핏으로 하려고했는데 너무 느려서 L_first화면이 떠버리는 문제가 발생
//        // >>>> 우선 화면전환하고 M_main에서 레트로핏 통신으로 로그인통신하기.
//
//        //쉐어드 1.autoLogin(로그인통과된 적 있는지)
//        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//        String UserEmail = auto.getString("UserEmail",null); //없다면 null이다?
//        String UserPwd = auto.getString("UserPwd",null);
//        String UserNickName = auto.getString("UserNickName",null); //닉네임은 join과 따로 저장됨. 선별용 변수
//        Log.e(TAG, "UserEmail : "+UserEmail);
//        Log.e(TAG, "UserPwd : "+UserPwd);
//        Log.e(TAG, "UserNickName : "+UserNickName);
//
//
//
//        //로그인 내역이 있는가? (쉐어드에)
//        if(UserEmail != null && UserPwd != null) { //로그아웃이 아니라면 자동로그인한다.
//            Log.e(TAG, "SharedPreferences 값 존재, 자동로그인 ");
//
//            //자동로그인 후 프로필작성했는가?
//                            //yes) >> M_mail이동
//                            //no) >> L_profile이동
//
//            if(UserNickName != null) { //닉네임을 등록했으면 바로 main이동 (닉네임은 필수값/ 사진은 필수값x)
//
//                Intent intent = new Intent(getApplicationContext(), M_main.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //현재화면빼고 아래액티비티 지움
//                Log.e(TAG, "Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK");
//                startActivity(intent);
//                Log.e(TAG, "startActivity()");
//                finish();
//
//            }
//
//            //이상하잖아. 로그인 하고나면 프로필 작성하기****
////            else { //닉네임 등록안하고 회원가입만 했다면 닉네임등록하러 가기
////
////                Intent intent = new Intent(getApplicationContext(), L_profile.class);
////                startActivity(intent);
////                Log.e(TAG, "startActivity()");
////            }
//
//        } //~쉐어드로 자동로그인
//
//
//        //ㅡㅡㅡㅡㅡㅡㅡ
//        // 회원가입 버튼
//        //ㅡㅡㅡㅡㅡㅡㅡ
//        btn_join.setOnClickListener(new View.OnClickListener() { //회원가입하러가기
//            @Override
//            public void onClick(View view) {
//
//                Log.e(TAG, "onClick 들어옴");
//                Intent intent = new Intent(getApplicationContext(), L_join.class); //로그인버튼>id/pw기입화면
//                startActivity(intent);
//                Log.e(TAG, "startActivity() --> L_join.class");
//
//            }
//        });
//        //ㅡㅡㅡㅡㅡㅡㅡ
//        // 로그인 버튼
//        //ㅡㅡㅡㅡㅡㅡㅡ
//        btn_login.setOnClickListener(new View.OnClickListener() { //로그인하러가기
//            @Override
//            public void onClick(View view) {
//
//                Log.e(TAG, "onClick 들어옴");
//                Intent intent = new Intent(getApplicationContext(), L_login.class); //로그인버튼>id/pw기입화면
//                startActivity(intent);
//                Log.e(TAG, "startActivity() --> L_login.class");
//
//            }
//        });
//
//    }//~onCreate()
//
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        // 세션 콜백 삭제
//        Session.getCurrentSession().removeCallback(sessionCallback);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
//        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
//            return;
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//
//    @Override
//    public void onBackPressed() {
////        super.onBackPressed(); //상속받지 말고 기존의 뒤로가기 버튼을 제거해줘야되는구나
//        // 2000 milliseconds = 2 seconds
//        if (System.currentTimeMillis() > backKeyPressedTime + 1500) { //연타가 1.5초
//            backKeyPressedTime = System.currentTimeMillis();
//            Toast.makeText(getApplicationContext(), "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        // 1.5초 이내에 뒤로가기 버튼을 한번 더 클릭시 finish()(앱 종료)
//        if (System.currentTimeMillis() <= backKeyPressedTime + 1500) {
//            finish();
//        }
//    }
//
//    //ID선언을 보기좋게 정리함
//    public void ID(){
//        btn_join = (Button) findViewById(R.id.btn_join);
//        btn_login = (Button) findViewById(R.id.btn_login);
//        img_kakao = (ImageView) findViewById(R.id.img_kakao);
//    }
//
//}
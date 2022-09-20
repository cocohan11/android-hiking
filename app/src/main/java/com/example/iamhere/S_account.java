package com.example.iamhere;

import static com.example.iamhere.L_login.bitmapCapture;
import static com.example.iamhere.L_login.myEmail;
import static com.example.iamhere.L_join.strCode;
import static com.example.iamhere.L_login.myName;
import static com.example.iamhere.L_login.myImg;
import static com.example.iamhere.L_login.mySmallBitmapImg;
import static com.example.iamhere.L_login.mysnsLogin;
import static com.example.iamhere.L_login.myPw;
import static com.example.iamhere.L_login.myDate;
import static com.example.iamhere.L_login.마커합성bitmap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iamhere.Interface.PwReissue;
import com.example.iamhere.Model.Login_find;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.AccessTokenInfo;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class S_account extends AppCompatActivity {

    String TAG = "S_account";
    Button btn_logout; //로그아웃 버튼 >> L_first
    TextView tv_accountEmail; //사용자의 이메일
    TextView s_pwChg; //비밀번호 변경 >> L_PwReissue
    View s_bar; //비번변경과 회원탈퇴 사이의 막대기 구분선
    ImageView s_snsLogo;
    TextView s_unregister; //회원탈퇴 (다이얼로그 되확인하기, 탈퇴시 잃는점 고지하기)
    String 결과값; //서버리턴값-true/false
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saccount);
        Log.e(TAG, "onCreate()");
        ID();


        if(mysnsLogin != null) { //카카오 로그인
            //카카오로그인한 경우 비번변경은 없으니까 비번변경 버튼 사라지게 하기
            s_pwChg.setVisibility(View.GONE); //회원탈퇴가 유일하니 중앙에 위치함(xml에서 gravity로 center해줌)
            s_bar.setVisibility(View.GONE); //구분선도 같이 없애주기
            s_snsLogo.setImageResource(R.drawable.kakao_talk);
        }
        tv_accountEmail.setText(myEmail); //로그인된 이메일 : ~~@.com에 삽입



        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        // 로그아웃 버튼 : >> L_first
        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //로그아웃 기능
                //1.쉐어드에 저장된 파일도 삭제된다.
                //2.로그아웃하면 화면도 처음으로 이동(스택삭제)

                //1.쉐어드 삭제
                SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //파일명, 다른앱과 공유할건지
                SharedPreferences.Editor editor = auto.edit(); //저 파일 edit하겠다

                editor.clear(); //파일까지 전부 삭제지? 아님. 파일은그대로 파일내용만 삭제됨.
                editor.apply(); //저장해줘야 함

                Toast.makeText(getApplicationContext(), "로그아웃", Toast.LENGTH_SHORT).show(); //이동하기전에 토스트외치기

                //카카오 로그아웃
                Log.e(TAG, "카카오 로그아웃한다");
                kakaoLogout();
                Log.e(TAG, "카카오 로그아웃한다 끝");

                //2.처음화면으로 이동(L_first.class)
                Intent intent = new Intent(getApplicationContext(), L_login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //현재화면빼고 아래액티비티 지움
                intent.putExtra("로그아웃", "로그아웃");
                Log.e(TAG, "Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK");
                startActivity(intent);
            }
        });

        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        // 비밀번호 변경 : >> L_PwReissue
        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        s_pwChg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //비밀번호 변경하러가기
                Intent intent = new Intent(getApplicationContext(), S_pwChg.class);
                startActivity(intent);
            }
        });

        //ㅡㅡㅡㅡㅡ
        // 회원 탈퇴 : 다이얼로그로 재확인, 잃는점 고지
        //ㅡㅡㅡㅡㅡ
        s_unregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //다이얼로그
                //잃는 점 : 회원탈퇴를 하게되면 같은 이메일로 다시 회원가입을 해도 데이터는 남아있지 않습니다.
                //정말 회원 탈퇴를 하시겠습니까?
                //서버통신 완료되면 >> 쉐어드삭제+static초기화+첫화면으로 이동
                Log.e(TAG, "다이얼로그 누름");
                Log.e(TAG, "myEmail :"+myEmail);

                new AlertDialog.Builder(S_account.this) // 현재 Activity의 이름 입력.
                    .setTitle("회원탈퇴")
                    .setMessage("회원탈퇴시 같은 이메일로 다시 회원가입을 해도 데이터는 남아있지 않습니다.\n정말 회원탈퇴 하시겠습니까?")
                    .setNeutralButton("              확인", new DialogInterface.OnClickListener() { //확인을 왼쪽에 해야 실수로 더블클릭하는 경우를 막을 수 있음
                        public void onClick(DialogInterface dialog, int which){
                            Log.e(TAG, "다이얼로그 확인 버튼");
                            //실행할 코드

                            //통신해서 회원탈퇴 요청
                            Log.e(TAG, "통신해서 회원탈퇴요청");
                            //Retrofit 인스턴스 생성
                            retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                                    .baseUrl("http://15.164.129.103/")    // baseUrl 등록
                                    .addConverterFactory(GsonConverterFactory.create())  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
                                    .build();

                            PwReissue pwReissue_interface = retrofit.create(PwReissue.class);   //(비번변경 인터페이스 재사용/pw자리에 '탈퇴'라는 문자열 넣어서 구분하기)
                            Call<Login_find> call = pwReissue_interface.chgPW(myEmail, "탈퇴"); //reissue모델을 따로 만들지 않았으니 login모델 재사용(true값만 받으면 됨)
                            Log.e(TAG, "call 객체 생성");

                            //회원탈퇴 응답값이 true면 성공
                            통신callback(call); //실행코드 1.쉐어드삭제 2.static초기화 3.첫화면이동
                            //kakaotalk 연결끊기
                            kakao연결끊기();
                            Log.e(TAG, "kakao연결끊기() 빠져나옴");

                        }
                    })
                    .setPositiveButton("취소            ", new DialogInterface.OnClickListener() {     //일부러 띄워쓰기 한거임. 간격조절
                        public void onClick(DialogInterface dialog, int which){
                            Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show(); // 실행할 코드
                        }
                    })
                .show();

            }
        });



    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    //카카오 플랫폼 안에서 앱과 사용자 카카오계정의 연결 상태를 해제 + 로그아웃(토큰삭제됨)
    public void kakao연결끊기() { //토큰삭제됨

        Log.e(TAG, "kakao연결끊기() 입장");

        UserApiClient.getInstance().accessTokenInfo(new Function2<AccessTokenInfo, Throwable, Unit>() {
            @Override
            public Unit invoke(AccessTokenInfo accessTokenInfo, Throwable throwable) {

                Log.e(TAG, "1 토큰 확인");
                Log.e(TAG, "1 accessTokenInfo : "+accessTokenInfo);
                Log.e(TAG, "1 throwable : "+throwable);

                return null;
            }
        });

        UserApiClient.getInstance().unlink(new Function1<Throwable, Unit>() {
            @Override
            public Unit invoke(Throwable throwable) {
                Log.e(TAG, "throwable 연결끊기 : "+throwable);

                UserApiClient.getInstance().accessTokenInfo(new Function2<AccessTokenInfo, Throwable, Unit>() {
                    @Override
                    public Unit invoke(AccessTokenInfo accessTokenInfo, Throwable throwable) {

                        Log.e(TAG, "2 토큰 확인");
                        Log.e(TAG, "2 accessTokenInfo : "+accessTokenInfo);
                        Log.e(TAG, "2 throwable : "+throwable);

                        return null;
                    }
                });

                return null;
            }
        });



    }



        //db에서만 회원삭제
    public void 통신callback(Call call) {

        //네트워킹 시도
        call.enqueue(new Callback<Login_find>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Login_find> call, Response<Login_find> response) {

                Login_find result = response.body(); //모델을 통해 받아온 값
                assert result != null;
                Log.e(TAG, "onResponse 입장");

                if(response.isSuccessful()) {
                    //정상적으로 통신이 성공한 경우
                    Log.e(TAG, "onResponse success");

                    // 서버에서 응답받은 데이터를 토스트로 띄운다
                    결과값 = result.getResponse();
                    Log.e(TAG, "onResponse / 결과값: "+결과값);

                    if(결과값.equals("true")) { //회원탈퇴 성공

                        Toast.makeText(getApplicationContext(), "회원탈퇴 완료", Toast.LENGTH_SHORT).show(); // 실행할 코드

                        //1.쉐어드삭제
                        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //파일명, 다른앱과 공유할건지
                        SharedPreferences.Editor editor = auto.edit(); //저 파일 edit하겠다
                        editor.clear(); //파일까지 전부 삭제지? 아님. 파일은그대로 파일내용만 삭제됨.
                        editor.apply(); //저장해줘야 함

                        //2.static초기화
                        strCode = null;
                        myEmail = null;
                        myPw = null;
                        myName = null;
                        myImg = null;
                        mySmallBitmapImg = null;
                        마커합성bitmap = null;
                        myDate = null;
                        mysnsLogin = null;

                        //3.첫화면으로 이동
                        Intent intent = new Intent(getApplicationContext(), L_login.class);
                        startActivity(intent);
                        Log.e(TAG, "startActivity >>> L_first.class");

                    } else { //회원탈퇴 실패
                        Toast.makeText(getApplicationContext(), "회원탈퇴 실패", Toast.LENGTH_SHORT).show();
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

    public void ID() {

        btn_logout = (Button) findViewById(R.id.btn_logout); //로그아웃 버튼
        tv_accountEmail = (TextView) findViewById(R.id.tv_accountEmail); //사용자의 이메일
        s_pwChg = (TextView) findViewById(R.id.s_pwChg); //비밀번호 변경
        s_unregister = (TextView) findViewById(R.id.s_unregister); //비밀번호 변경
        s_bar = (View) findViewById(R.id.s_bar); //비번변경과 회원탈퇴 구분선
        s_snsLogo = (ImageView) findViewById(R.id.s_snsLogo); //sns로그인할 때만 로고 표기하기d

    }

    //카카오 로그아웃
    public void kakaoLogout() {
        Log.e(TAG, "kakaoLogout() 메소드 입장 ");

        //user 불러와지네
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {

                Log.e(TAG, "invoke user1 : "+user);
                return null;
            }
        });

        //실제 구현되는 곳
        UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() { //토큰삭제
            @Override
            public Unit invoke(Throwable throwable) {

                myEmail = null;
                myPw = null;
                myName = null;
                myImg = null;
                myDate = null;
                mysnsLogin = null;
                마커합성bitmap = null;

                Log.e(TAG,"\nUserApiClient"+
                        "\nemail : "+myEmail+
                        "\npw : "+myPw+
                        "\nmyName : "+myName+
                        "\nmyImg : "+myImg+
                        "\nmyDate : "+myDate+
                        "\n마커합성bitmap : "+마커합성bitmap+
                        "\nmysnsLogin : "+mysnsLogin ); //확인용

                UserApiClient.getInstance().accessTokenInfo(new Function2<AccessTokenInfo, Throwable, Unit>() {
                    @Override
                    public Unit invoke(AccessTokenInfo accessTokenInfo, Throwable throwable) {

                        Log.e(TAG, "4 토큰 확인");
                        Log.e(TAG, "4 accessTokenInfo : "+accessTokenInfo);
                        Log.e(TAG, "4 throwable : "+throwable);

                        return null;
                    }
                });
                
                Log.e(TAG, "kakaoLogout() 메소드의 invoke 안 ");
                return null;
            }
        });

        //user 불러와지네
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {

                Log.e(TAG, "invoke user2 : "+user);
                return null;
            }
        });

        Log.e(TAG, "kakaoLogout() 메소드 퇴장 ");
    }



}
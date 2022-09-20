package com.example.iamhere;

import static com.example.iamhere.L_login.myEmail;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iamhere.Interface.PwReissue;
import com.example.iamhere.Model.Login_find;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class S_pwChg extends AppCompatActivity {

    String TAG = "S_pwChg";
    Button btn_S_pwchg;
    EditText et_S_pw1;
    EditText et_S_pw2;
    EditText et_S_pw3;
    String et_pw1;
    String et_pw2;
    String et_pw3;
    String 결과값;

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    //빈값, 길이, 적합성 통과 >> 한꺼번에 서버에 통신
    //응답1. 변경완료되었습니다
    //응답2. 비밀번호를 다시 확인하여 주십시오
    //응답3. 에러
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spw_chg);
        Log.e(TAG, "onCreate()");
        Log.e(TAG, "strEmail :"+myEmail);
        ID();

        btn_S_pwchg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_pw1 = String.valueOf(et_S_pw1.getText()); //전역변수로 사용해야 따로 shared메소드 에서도 사용가능함
                et_pw2 = String.valueOf(et_S_pw2.getText());
                et_pw3 = String.valueOf(et_S_pw3.getText());

                //1.비밀번호 et에 전부 값이 들어있니?
                if(!et_pw1.equals("") && !et_pw2.equals("") && !et_pw3.equals("")) {
                    Log.e(TAG, "1.비밀번호 et에 전부 값이 들어있니?");

                    //2.변경할 비밀번호가 6자 이상이니? (최대값은 maxlength12자로 막아둠)
                    if(et_pw2.length() >= 6 && et_pw3.length() >= 6) {
                        Log.e(TAG, "2.변경할 비밀번호가 6자 이상이니?");

                        //3.변경할 비번, 변경할 비번 재입력 동일하니?
                        if(et_pw2.equals(et_pw3)) {
                            Log.e(TAG, "3.변경할 비번, 변경할 비번 재입력 동일하니?");

                            //4.정규식 통과하니? (영문,숫자/특수문자안됨)
                            if(patternPW(et_pw3)) {
                                Log.e(TAG, "4.정규식 통과하니?");

                                //통신해서 변경요청함
                                Log.e(TAG, "통신해서 변경요청함");
                                //Retrofit 인스턴스 생성
                                retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                                        .baseUrl("http://15.164.129.103/")    // baseUrl 등록
                                        .addConverterFactory(GsonConverterFactory.create())  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
                                        .build();

                                PwReissue pwReissue_interface = retrofit.create(PwReissue.class);   //(로그인 인터페이스 재사용)
                                Call<Login_find> call = pwReissue_interface.chgPW(myEmail, et_pw3); //reissue모델을 따로 만들지 않았으니 login모델 재사용(true값만 받으면 됨)

                                //통신&결과
                                Log.e(TAG, "통신callback(call) :"+통신callback(call)); //로그겸 메소드

                            } else { //~4
                                Toast.makeText(getApplicationContext(), "비밀번호 형식에 맞게 입력해주세요", Toast.LENGTH_SHORT).show(); //이동하기전에 토스트외치기
                            }
                        } else { //~3
                            Toast.makeText(getApplicationContext(), "비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show(); //이동하기전에 토스트외치기
                        }
                    } else { //~2
                        Toast.makeText(getApplicationContext(), "비밀번호 형식에 맞게 입력해주세요", Toast.LENGTH_SHORT).show(); //이동하기전에 토스트외치기
                    }
                } else { //~1
                    Toast.makeText(getApplicationContext(), "비어있는 값을 입력해주세요", Toast.LENGTH_SHORT).show(); //이동하기전에 토스트외치기
                }

            }
        });


    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    //비번변경 레트로핏 통신
    public String 통신callback(Call call) {
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

                    if(결과값.equals("true")) { //비번 변경 성공
                        Toast.makeText(getApplicationContext(), "비밀번호 변경 성공", Toast.LENGTH_SHORT).show();

                        //쉐어드변경
                        Log.e(TAG, "쉐어드변경");
                        shared(myEmail, et_pw3); //email,pw 자동저장
                        Log.e(TAG, "shared()");

                        finish(); //화면꺼짐

                    } else {
                        Toast.makeText(getApplicationContext(), "비밀번호 변경 실패", Toast.LENGTH_SHORT).show();
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
        return 결과값;
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    public void shared(String email, String strCode) {

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능
        SharedPreferences.Editor autoLoginEdit = auto.edit();
        Log.e(TAG, "email : "+email);
        Log.e(TAG, "strCode : "+strCode);

        autoLoginEdit.putString("UserEmail", email); //db컬럼명과 동일하게 하자
        autoLoginEdit.putString("UserPwd", strCode); //파라미터로 받은 pw

        autoLoginEdit.commit(); //실질 저장
    }


    //비밀번호 유효성 검사(정규식)
    public boolean patternPW(String PW) {

        boolean patternPW;

        if(Pattern.matches("^(?=.*[a-zA-z])(?=.*[0-9]).{6,12}$", PW)) { //숫자1개이상, 문자1개이상, 6~12자리(입력자체를 막아두긴 했지만 정규식으로 한 번더 필터)
            patternPW = true;
            Log.e(TAG, "ㅍㅍ패턴검사 patternPW = true");

        } else {
            patternPW = false;
            Log.e(TAG, "ㅍㅍ패턴검사 patternPW = false");
        }
        return patternPW;
    }

    public void ID() {

        et_S_pw1 = (EditText) findViewById(R.id.et_S_pw1); //현재 비밀번호
        et_S_pw2 = (EditText) findViewById(R.id.et_S_pw2); //변경할 비밀번호
        et_S_pw3 = (EditText) findViewById(R.id.et_S_pw3); //변경할 비밀번호 재입력
        btn_S_pwchg = (Button) findViewById(R.id.btn_S_pwchg); //비번 변경 버튼
    }
}
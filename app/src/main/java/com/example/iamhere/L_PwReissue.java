package com.example.iamhere;

import static com.example.iamhere.L_join.strCode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iamhere.Interface.PwReissue;
import com.example.iamhere.Model.Login_find;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class L_PwReissue extends AppCompatActivity {

    String TAG = "L_PwReissue";
    EditText et_re_email;
    Button btn_PwReissue;
    EditText et_email_certifyCode2;
    Button btn_email_certify2;
    String email;
    String getCode;
    String 결과값; //비번변경 리턴값 true/false(String타입)

    //다이얼로그? 잘 못 본 것 같음.. 액티비티를 하나 만들기
    //유효성 검사로 true이면 gmail 임시비번 보내기
    //임시번호를 입력하고 로그인하게되면 비밀번호로 변경된다. 그 전에는 변경x
    //인증번호 입력 show. 인증확인 누르면 로그인되게하면서 비번db에서 변경하기
    //intent로 이전플래그 삭제,이동

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpw_reissue);
        ID();
        Log.e(TAG,"onCreate()");

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 비밀번호 재발급 버튼
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        btn_PwReissue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"버튼클릭");
                email = String.valueOf(et_re_email.getText()); //사용자가 입력한 email
                Log.e(TAG,"email : "+email);

                //email 유효성검사
                L_join L_join객체 = new L_join(); //로그인할 때 만들어둔 유효성검사 재사용
                if(L_join객체.patternEmail(email)) {

                    Log.e(TAG,"email 유효성검사 true");
                    L_join객체.ThreadSendMail("[ 암히얼 ] 비밀번호 재발급해드립니다. ", "임시 비밀번호 : ","\n암히얼 어플로 돌아가서 로그인을 이어가시기 바랍니다.",email);
                    //실제 메일보내는 메소드(리턴값 : strCode : 생성된 코드)
                    // 스태틱으로 한 이유:finish되면 변수사라지니까. intent로 하기도 번거로움
                    Log.e(TAG,"생성된 strCode : "+strCode);
                    Toast.makeText(getApplicationContext(), "임시비밀번호를 발급했습니다. 해당이메일에서 확인해주세요.", Toast.LENGTH_SHORT).show();
                    chgAfterbtn(); //UI변화 메소드

                } else {
                    Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        // 인증확인 버튼
        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        btn_email_certify2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getCode = String.valueOf(et_email_certifyCode2.getText()); //사용자가 입력한 code
                Log.e(TAG,"인증확인 버튼 getCode : "+getCode);
                Log.e(TAG,"인증확인 버튼 strCode : "+strCode); //두 값이 동일해야함

                //생성된 코드와 입력한 코드가 일치한다면, 쉐어드+db에 pw업데이트+로그인
                if(getCode.equals(strCode)) {
                    Toast.makeText(getApplicationContext(), "인증번호가 일치합니다", Toast.LENGTH_SHORT).show();

                    //쉐어드 저장
                    shared(email, strCode); //쉐어드에 email,pw저장해서 자동로그인되도록하는 함수. 유저정보에 변경이있을 때 쉐어드도 꼭 변경해주기

                    //Retrofit 인스턴스 생성
                    retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                            .baseUrl("http://15.164.129.103/") // baseUrl 등록
                            .addConverterFactory(GsonConverterFactory.create()) //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
                            .build();

                    PwReissue pwReissue_interface = retrofit.create(PwReissue.class); //(로그인 인터페이스 재사용)
                    Call<Login_find> call = pwReissue_interface.chgPW(email, strCode); //reissue모델을 따로 만들지 않았으니 login모델 재사용(true값만 받으면 됨)

                    //ㅡㅡㅡㅡㅡㅡㅡ
                    // 네트워킹 시도 : DB에 pw update
                    //ㅡㅡㅡㅡㅡㅡㅡ
                    call.enqueue(new Callback<Login_find>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
                        @Override
                        public void onResponse(Call<Login_find> call, Response<Login_find> response) {

                            Log.e(TAG, "onResponse 입장"); //통신 성공
                            Login_find result = response.body(); //모델을 통해 받아온 값
                            assert result != null;

                            if(response.isSuccessful()) { //응답 잘 받은 경우
                                Log.e(TAG, "onResponse success");
                                결과값 = result.getResponse(); //data class에서 데이터타입이 지정됨
                                Log.e(TAG, "onResponse / 결과값: "+결과값);

                                if(결과값.equals("true")) { //서버에서 비번update 요청 성공이면..

                                    // 결과값 있으면 로그인 통과 / intent를 이용해 M_main으로 이동시킴
                                    Intent intent = new Intent(getApplicationContext(), M_main.class); //비번분실 >> 지도첫화면
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //top빼고 아래액티비티 지움
                                    Log.e(TAG, "Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK");
                                    startActivity(intent);
                                    Log.e(TAG, "startActivity()");

                                } else {
                                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                }

                            } else { //통신에 성공했지만 응답에 문제있는 경우
                                Log.e("콜.enqueue : ", "onResponse : 실패");
                            }
                        }
                        @Override
                        public void onFailure(Call<Login_find> call, Throwable t) {
                            Log.e("onFailure : ", t.getMessage()); //통신 실패
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "인증번호를 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


    public void shared(String email, String strCode) {

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능
        SharedPreferences.Editor autoLoginEdit = auto.edit();
        Log.e(TAG, "email : "+email);
        Log.e(TAG, "strCode : "+strCode);

        autoLoginEdit.putString("UserEmail", email); //db컬럼명과 동일하게 하자
        autoLoginEdit.putString("UserPwd", strCode); //파라미터로 받은 pw

        autoLoginEdit.commit(); //실질 저장
    }

    public void chgAfterbtn() {

        et_re_email.setFocusable(false); //껌뻑껌뻑 포커스 비활성화
        et_re_email.setClickable(false); //클릭 비활성화
        btn_PwReissue.setText("전송완료"); //입력한 코드대신 '인증완료'로 변경
        btn_PwReissue.setBackgroundColor(Color.parseColor("#13A327")); //버튼초록색으로 변경
        btn_PwReissue.setEnabled(false); //버튼 비활성화(클릭리스너 작동x)
    }

    public void ID() {

        et_re_email = (EditText) findViewById(R.id.et_re_email);
        btn_PwReissue = (Button) findViewById(R.id.btn_PwReissue);
        et_email_certifyCode2 = (EditText) findViewById(R.id.et_email_certifyCode2);
        btn_email_certify2 = (Button) findViewById(R.id.btn_email_certify2);
    }


}
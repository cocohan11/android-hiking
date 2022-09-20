package com.example.iamhere;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iamhere.Interface.Join;
import com.example.iamhere.Model.Join_create;

import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import static com.example.iamhere.L_login.myEmail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class L_join extends AppCompatActivity {
    //회원가입하는 클래스(이메일인증, 비밀번호재확인)

    String TAG = "L_join"; //log.e()의 첫 번째 파라미터로 클래스명 넣기
    EditText et_email_join; //사용자가 입력한 회원가입할 이메일
    EditText et_email_certifyCode; //(사용자가 입력한 이메일로 받은 인증번호를 보고) 사용자가 입력한 인증번호
    EditText et_email_pw; //사용자가 회원가입할 비밀번호 설정
    EditText et_email_pw2; //비번 일치해야함
    Button btn_email_send; //인증이메일 보내는 버튼
    Button btn_email_certify; //인증번호 일치되는지 확인하는 버튼. 일치해야 최종 회원가입이 된다.
    Button btn_doJoin; //최종 회원가입 제출버튼
//    static String myEmail; //사용자가 기입한 이메일을 et->string으로 변환함. 전역변수인 이유는, 회원가입최종버튼 누를 때 et값이랑 같아야 통과되고 같지않다면 이메일,인증번호,버튼상태 초기화
    static String strCode; //개발자가 생성해서 보낸 인증번호(btn_email_send.클릭리스너에서 선언되어버리면 btn_email_certify.클릭리스너에서 사용이 안되어서 전역으로 선언함)
    boolean bool_alreadySendEmail = false; //이미보냈는데 한 번 더 보내려고 하면 true로 바뀜. 재발송인가?를 물을 때 사용. 재발송이면 재확인 후 이메일, 인증상태 초기화
    boolean bool_certify = false; //초기값은 false이다. 인증확인이 되면 true로 바뀐다. 최종회원가입시 이 변수를 조건문으로 건다. /인증번호를 다시 보내면 false로 변경된다.
    boolean patternPW = false; //true가 되어야 회원가입가능. 정규식함수에서 true로 리턴한다.
    boolean patternEmail = false; //true가 되어야 이메일형식임. 이메일정규식

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ljoin);
        Log.e(TAG, "onCreate()");
        ID();
        btn_email_certify.setEnabled(false); //이메일로 인증번호보내기 전까지는 인증확인버튼 비활성화

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 이메일보내기 버튼 : 기입한 이메일로 인증번호 보내기
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        btn_email_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "btn_email_certify클릭");
                myEmail = String.valueOf(et_email_join.getText()); //사용자가 입력한 이메일을 사용하기 위해 et -> string형태로 변환
                Log.e(TAG, "사용자가 입력한 이메일 myEmail : "+ myEmail);

                //이메일 인증번호 버튼 처음과 재전송일 경우를 나눔
                if(!bool_alreadySendEmail) { //이메일 처음전송이면 스레드실행
                    Log.e(TAG, "bool_alreadySendEmail : false"); //보낸적 없음
                    SendGmail(myEmail); //메일전송 스레드

                } else { //이메일 재전송이면 재확인하기. 그래도 확인하면 이메일,인증번호 리셋하기
                    Log.e(TAG, "bool_alreadySendEmail : true"); //보낸적 있음

                    //다이얼로그 "인증번호 재전송?"
                    new AlertDialog.Builder(L_join.this) // 현재 Activity의 이름 입력.
                            .setMessage("인증번호를 다시 전송하시겠습니까?")     // 제목 부분 (직접 작성)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {      // 버튼1 (직접 작성)
                                public void onClick(DialogInterface dialog, int which){
                                    //실행할 코드
                                    et_email_certifyCode.setText("");
                                    SendGmail(myEmail); //메일전송 스레드
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {     // 버튼2 (직접 작성)
                                public void onClick(DialogInterface dialog, int which){
                                    Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show(); // 실행할 코드
                                }
                            })
                            .show();
                } //~이메일 재전송 가림
            } //~이메일전송 버튼onclick()
        }); //~기입한 이메일로 인증번호 보내는 버튼 Listener()


        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        // 인증확인 버튼 : 사용자가 입력한 인증번호와 이메일로 보내진 인증번호가 일치하는지 확인하는 버튼
        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        btn_email_certify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "aaa : btn_email_certify클릭");
                String strCertifyCode = String.valueOf(et_email_certifyCode.getText()); //사용자가 입력한 이메일을 사용하기 위해 et -> string형태로 변환
                String email_now = String.valueOf(et_email_join.getText()); //사용자가 입력한 이메일을 그 사이 변경할 수 있으니까 보낸메일주소와 일치하는지 확인할 용도의 변수

                Log.e(TAG, "bbb :strCertifyCode : "+strCertifyCode); //"" (아무것도 입력하지 않았다면 상태값)
                Log.e(TAG, "bbb :myEmail : "+myEmail); //null
                Log.e(TAG, "bbb :strCode : "+strCode); //null
                Log.e(TAG, "bbb :email_now : "+email_now); //""

                if(!strCertifyCode.equals("")) { //선언만 한게 아니라 값도 있다면 진행

                    if(myEmail.equals(email_now)) { //전송된 이메일주소와 현재입력된 이메일주소가 같다면(도중에 변경가능함) 실행한다.
                        //빈값이라 에러남
                        Log.e(TAG, "ccc: if(myEmail.equals(email_now)) 들어옴");

                        if(strCode.equals(strCertifyCode)){ //만약 strCode와 사용자가 입력한 et값이 같다면
                            //"인증되었습니다."토스트 띄우고 bool_certify값이 true가 된다. 입력할 수 없게 막힌다. 인증완료라고 바뀐다. 색상이 바뀐다.
                            Log.e(TAG, "ccc :if(strCode.equals(strCertifyCode))");
                            Toast.makeText(getApplicationContext(),"인증되었습니다", Toast.LENGTH_SHORT).show();
                            bool_certify = true;
                            //UI변경
                            chgAfterCertify(); //et,btn상태 변경

                        } else { //아니라면 "인증번호를 다시 입력해주세요."라는 토스트가 띄워진다. 입력값이 ""값으로 바뀐다.
                            Log.e(TAG, "ddd :else(strCode.equals(strCertifyCode))");
                            Toast.makeText(getApplicationContext(),"인증번호를 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),"발송된 이메일과 현재 입력된 이메일이 다릅니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "eee :else{발송된 이메일과 현재 입력된 이메일이 다릅니다. 다시 확인해주세요.}");
                    }

                } else { //""값이라면
                    //이메일이나 인증번호가 비어있으면 토스트
                    Toast.makeText(getApplicationContext(),"11값을 기입해주세요", Toast.LENGTH_SHORT).show();
                }
            } //~onclick
        }); //인증확인 버튼을 누르면 이메일과 인증번호가 비활성화 된다.


        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 최종회원가입버튼 : 5단계 통과되어야 회원가입 통과
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        btn_doJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "111111 btn_doJoin클릭");
                //필터 1.이메일인증했는가 2.빈값체크 3.비번길이 4.비밀번호 정규식통과됐는가 5.비번두개일치하는가

                //1.이메일인증통과?
                if(bool_certify) {
                    Log.e(TAG, "111111 bool_certify true");
                    String PW = String.valueOf(et_email_pw.getText()); //et였던 데이터타입을 str으로 변경해서 pw를 가져온다.
                    Log.e(TAG, "222222 PW : "+PW);
                    Log.e(TAG, "333333 et_email_pw.length() : "+et_email_pw.length());
                    String PW2 = String.valueOf(et_email_pw2.getText()); //et였던 데이터타입을 str으로 변경해서 pw를 가져온다.
                    Log.e(TAG, "333333 PW2 : "+PW2);

                    //2.비번2개 입력되었는가
                    if(PW.equals("")||PW2.equals("")) { //둘 중에 하나라도 비어있으면 비어있는 값 채우라고 토스트 띄우기
                        Toast.makeText(getApplicationContext(),"비어있는 값을 입력해주세요", Toast.LENGTH_SHORT).show();

                    } else {
                        //3.비밀번호 길이
                        if(et_email_pw.length() >= 6) { //6자이상이라면 회원가입
                            Log.e(TAG, " 6자이상 true");
                            Log.e(TAG, "555555 patternPW()정규식함수 끝");
//                            patternPW(PW);
                            Log.e(TAG, "666666 patternPW(PW) : "+patternPW(PW));

                            //4.비밀번호 정규식 통과
                            if(patternPW(PW)) { //정규식 통과

                                //5.비밀번호 재입력
                                if(PW.equals(PW2)) { //PW==PW2
                                    Log.e(TAG, "777777 PW.equals(PW2)) ...true");
//                                    Toast.makeText(getApplicationContext(),"3단계를 다 통과하셨군요 회원자격을 받았습니다. 축하하세요", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "111 PWPWPWPWPWPW : "+PW);
                                    RetrofitDoneJoin(PW); //레트로핏통신으로 db에 회원정보 삽입 후 화면이동(최종회원가입)
                                    // 회원가입 끝!


                                } else { //~5.비밀번호 미일치
                                    //비밀번호 일치시키라는 토스트 띄우기
                                    Log.e(TAG, "888888 PW.equals(PW2)) ...false");
                                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                                }

                            } else { //~4.비밀번호 정규식 불통과 >> >> 규칙안내로 통일
                                Toast.makeText(getApplicationContext(),"비밀번호 형식에 맞게 입력해주세요",Toast.LENGTH_SHORT).show();
                            }

                        } else { //~3.비번길이 : 6자미만이라면 토스트띄우기"비밀번호를 최소 6자 이상 입력해주세요" >> 규칙안내로 통일
                            Toast.makeText(getApplicationContext(),"비밀번호 형식에 맞게 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                    } //~2.비번빈값
                } else { //~1.이메일인증부터 하세요 토스트
                    Toast.makeText(getApplicationContext(),"회원가입을 위해 이메일인증을 해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    //비밀번호 유효성 검사(정규식)
    public boolean patternPW(String PW) {

        if(Pattern.matches("^(?=.*[a-zA-z])(?=.*[0-9]).{6,12}$", PW)) { //숫자1개이상, 문자1개이상, 6~12자리(입력자체를 막아두긴 했지만 정규식으로 한 번더 필터)
//            Toast.makeText(getApplicationContext(),"비밀번호 형식이 적합합니다.",Toast.LENGTH_SHORT).show();
            patternPW = true;
            Log.e(TAG, "ㅍㅍㅍㅍ1111패턴검사 patternPW = true");

        } else {
            patternPW = false;
            Log.e(TAG, "ㅍㅍㅍㅍ2222패턴검사 patternPW = false");
        }
        return patternPW;
    }

    //email 유효성 검사(정규식)
    public boolean patternEmail(String Email) {

        if(Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", Email)) { //이메일 정규식 : 숫자/문자 + @ + 숫자/문자
            patternEmail = true;
            Log.e(TAG, "ㅍ11패턴검사 patternEmail = true");
        } else {
            patternEmail = false;
            Log.e(TAG, "ㅍ22패턴검사 patternEmail = false");
        }
        return patternEmail;
    }


    //인증확인 후 일치하면 변경되는 et,btn상태
    public void chgAfterCertify() {

        et_email_certifyCode.setText("인증완료"); //입력한 코드대신 '인증완료'로 변경
        et_email_certifyCode.setTextColor(Color.parseColor("#CCCFCC")); //회색으로 변경(비활성화)
        et_email_certifyCode.setFocusable(false); //껌뻑껌뻑 포커스 비활성화
        et_email_certifyCode.setClickable(false); //클릭 비활성화
        et_email_certifyCode.setEnabled(false); //버튼 비활성화(클릭리스너 작동x)
        et_email_join.setTextColor(Color.parseColor("#CCCFCC")); //회색으로 변경(비활성화)
        et_email_join.setFocusable(false); //이메일도 비활성화
        et_email_join.setClickable(false); //이메일도 비활성화
        btn_email_certify.setBackgroundColor(Color.parseColor("#13A327")); //초록색으로 변경
        btn_email_certify.setText("인증완료"); //버튼 인증확인->인증완료
        btn_email_certify.setEnabled(false); //버튼 비활성화(클릭리스너 작동x)
        btn_email_send.setEnabled(false); //인증번호보내기 버튼도 같이 비활성화
    }

    //이메일전송 스레드
    public void SendGmail(String myEmail) {
        // 이메일송신의 경우 네트워크 통신이 필요해서 별도의 Thread에서 실행. (스레드 없이 하려고했는데 네트워크처리를 잘 못하게 되면 메인 Thread가 멈출 수 있어서 강제분리구현하도록 함)
        Log.e(TAG, "SendGmail()들어옴"); //값 제대로 들어옴?

        if(!myEmail.equals("")) { //이메일란에 뭘 입력한게 있다면 진행하기

            //Toast.makeText(getApplicationContext(),"이메일을 보내는 중입니다...", Toast.LENGTH_SHORT).show(); //스레드도중 토스트가 너무 느려서 사용자가 반복클릭할까봐 예방차 띄움
            Log.e(TAG, "ㄱ 1111 retrofitOverlap() 입력값이 있다 ");

            if(patternEmail(myEmail)) { //이메일 형식이라면

                //UI스레드와 같이 변경하려고했는데 뜨는 시간이 너무 길어서 버튼먼저 변경
                btn_email_send.setText("인증번호 전송완료"); //버튼 인증확인->인증완료
                btn_email_send.setBackgroundColor(Color.parseColor("#13A327")); //초록색으로 변경
                btn_email_certify.setEnabled(true); //이메일로 인증번호보내기 전까지는 인증확인버튼 비활성화 >> 보내고 바로 활성화
                Toast.makeText(getApplicationContext(), "기입하신 이메일로 인증번호를 보냈습니다.", Toast.LENGTH_SHORT).show();

                // 이메일 중복체크
                //서버에 이메일있는지 조회하기. 있으면 "이미 가입된 이메일입니다." 토스트, 없으면 별말없이 이메일전송 진행하기
                //Retrofit 인스턴스 생성
                retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                        .baseUrl("http://15.164.129.103/")    // baseUrl 등록
                        .addConverterFactory(GsonConverterFactory.create())  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
                        .build();
                Log.e(TAG, "ㄱ 2222 레트로핏 객체 생성 ");

                com.example.iamhere.Interface.Join Join_interface = retrofit.create(Join.class);   // 레트로핏 인터페이스 객체 구현
                Call<Join_create> call = Join_interface.getOverlap(myEmail,"","","emailCertify"); //인터페이스 객체를 이용해 인터페이스에서 정의한 함수를 호출하면 Call 객체를 얻을 수 있다. //에러났던 이유:DataClass를 참조하는데 interface에서 불러올 때 변수가 없어서.
                Log.e(TAG, "ㄱ 3333 레트로핏 객체에 모델 장착 ");

                //네트워킹 시도
                call.enqueue(new Callback<Join_create>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<Join_create> call, Response<Join_create> response) {

                        Join_create result = response.body(); //response.isSuccessful안에 있으니까 response가 확인이 안되서 로그찍기위해 한 층 꺼냄냄
                        assert result != null;
                        String resOverlap = result.getResponse();
                        Log.e(TAG, "ㄱ 4444 onResponse / resOverlap: "+resOverlap);

                        //정상적으로 통신이 성공한 경우
                        if(response.isSuccessful()) {
                            Log.e(TAG, "ㄱ 5555 onResponse success");

                            if(resOverlap.equals("false")) { //이메일중복 통과. 값없음
                                Log.e(TAG, "ㄱ 6666 resOverlap.equals(false)");
                                Log.e(TAG, "ㄱ 6666 최종으로 이메일 보내는 곳!!!");
                                ThreadSendMail("[ 암히얼 ] 이메일 인증입니다. ", "인증번호 : ","\n암히얼 어플로 돌아가서 회원가입을 이어가시기 바랍니다.", myEmail); //실제 이메일 보내는 함수

                            } else { //이메일중복임. 기존값있음.
                                Log.e(TAG, "ㄱ 7777 resOverlap.equals(true)");
                                Toast.makeText(getApplicationContext(), "이미 가입된 이메일입니다.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // 통신 실패
                            Log.e("콜.enqueue : ", "onResponse : 실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<Join_create> call, Throwable t) {
                        Log.e("onFailure : ", t.getMessage());
                    }
                }); //~네트워킹 시도

            } else { //정규식이 아니라면
                //토스트"이메일형식 아님"
                Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            }
        } else { //이메일란에 입력한게 없다면
            //토스트로 입력하라고 띄우기
            Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();

        } //~이메일란에 입력한게 있나없나
    } //~sendGmail()함수


    //DB에 회원정보 insert하기(회원가입완료)
    public void RetrofitDoneJoin(String pw){
        Log.e(TAG, "222 PWPWPWPWPWPW : "+pw);

        Log.e(TAG, "ㄴ 1111 DoneJoin()함수 들어옴 ");

        //Retrofit 인스턴스 생성
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://15.164.129.103/")    // baseUrl 등록
                .addConverterFactory(GsonConverterFactory.create())  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
                .build();
        Log.e(TAG, "ㄴ 2222 레트로핏 객체 생성 ");

        com.example.iamhere.Interface.Join Join_interface = retrofit.create(Join.class);   //레트로핏 인터페이스 객체 구현
        Call<Join_create> call = Join_interface.getOverlap(myEmail,pw,"","joinDone"); //pw:파라미터로 받아서 서버에 전달(암호는 php에서)
        Log.e(TAG, "ㄴ 3333 레트로핏 객체에 모델 장착 ");

        //네트워킹 시도
        call.enqueue(new Callback<Join_create>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Join_create> call, Response<Join_create> response) {

                Join_create result = response.body(); //response.isSuccessful안에 있으니까 response가 확인이 안되서 로그찍기위해 한 층 꺼냄냄
                assert result != null;
                String resOverlap = result.getResponse();
                Log.e(TAG, "ㄴ 4444 onResponse / resOverlap: "+resOverlap); //true OR false (회원가입성공/실패)

                //정상적으로 통신이 성공한 경우
                if(response.isSuccessful()) {
                    Log.e(TAG, "ㄴ 5555 onResponse success");

                    if(resOverlap.equals("true")) { //회원가입 성공. 화면 이동. 토스트"회원가입 성공"
                        Log.e(TAG, "ㄴ 6666 resOverlap.equals(true)");
                        Log.e(TAG, "ㄴ 6666 최종으로 회원가입 성공되는 곳!!!");

                        //앞으로 앱실행 시 자동로그인을 위해 쉐어드에 email,pw저장
                        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor autoLoginEdit = auto.edit();
                        //쉐어드프리페어런스란 : key, value와 같은 Map 형태로 해당 앱의 파일에 데이터를 저장한다.
                        //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능

                        Log.e(TAG, "ㄴ 6666 SharedPreferences UserEmail :"+myEmail); //가입한 email, pw찍어보기
                        Log.e(TAG, "ㄴ 6666 SharedPreferences UserPwd :"+pw);

                        autoLoginEdit.putString("UserEmail", myEmail); //db컬럼명과 동일하게 한다.
                        autoLoginEdit.putString("UserPwd", pw); //파라미터로 받은 pw

                        autoLoginEdit.apply(); //실질 저장
                        Log.e(TAG, "ㄴ 66666666 SharedPreferences commit()");


                        Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), L_profile.class); //회원가입 >> 프로필작성
                        intent.putExtra("UserEmail",myEmail);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //top빼고 아래액티비티 지움
                        Log.e(TAG, "FLAG_ACTIVITY_SINGLE_TOP");
                        startActivity(intent);
                        Log.e(TAG, "startActivity()");


                    } else { //이메일중복임. 기존값있음.
                        Log.e(TAG, "ㄴ 7777 resOverlap.equals(true)");
                        //Toast.makeText(getApplicationContext(), "이미 가입된 이메일입니다.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // 통신 실패
                    Log.e("콜.enqueue : ", "onResponse : 실패");
                }
            }

            @Override
            public void onFailure(Call<Join_create> call, Throwable t) {
                Log.e("onFailure : ", t.getMessage());
            }
        }); //~네트워킹 시도

    }

    public String ThreadSendMail(String subject, String body1, String body2, String email) {

        new Thread(){
            @Override
            public void run() {
                super.run();
                Log.e(TAG, "1111 Thread실행");
                GmailSender gMailSender = new GmailSender("cocohan4919", "coco4919!"); //("송신 지메일 주소", "송신 이메일 비밀 번호")
                Log.e(TAG, "2222 GmailSender객체 생성");

                try { //정상작동시 코드
                    strCode = gMailSender.getEmailCode(); //랜덤조합하여 생성된문자 5자리
                    Log.e(TAG, "3333_2 사용자가 입력한 이메일로 보낸 코드 strCode : "+ strCode);
                    Log.e(TAG, "body1+strCode+body2 "+ body1+strCode+body2);
                    String body = body1+strCode+body2;
                    gMailSender.sendMail(subject, body, email ); //GMailSender.sendMail(제목, 본문내용, 받는사람); //실제로 보내는 메소드
                    Log.e(TAG, "4444 sendMail() 실제로 보내는 메소드");

                    bool_alreadySendEmail = true; //이메일전송버튼 다시누를 때 경고하기위해 불린값을 줌
                    Log.e(TAG, "4444 bool_alreadySendEmail : "+bool_alreadySendEmail);

                    //UI스레드
//                    L_join.this.runOnUiThread(new Runnable() { //쓰레드에서는 Toast를 띄우지 못하여 runOnUiThread를 호출해야 한다.
//                        @Override
//                        public void run() {
//                            Log.e(TAG, "10(4다음 나와야함) 송신완료");
//                            Toast.makeText(getApplicationContext(), "기입하신 이메일로 인증번호를 보냈습니다.", Toast.LENGTH_SHORT).show();
//                            //위치가 gMailSender이후인 이유:try안이라도 gMailSender보다 먼저면 에러가 뜨기도 전에 색상변경됨. 제대로 보내지지도 않았는데 색상변경되면 이상함)
//                            //위치가 UI스레드안인 이유: 스레드 동작 중에 View객체는 UI스레드안에서 수행하는것이 가장좋다.(에러:Animators may only be run on Looper threads on Sherlock Action Bar)
//                        }
//                    });

                }catch(SendFailedException e) {
                    Log.e(TAG, "5555 SendFailedException 전송실패 :"+e);
                    L_join.this.runOnUiThread(new Runnable() { //쓰레드에서는 Toast를 띄우지 못하여 runOnUiThread를 호출해야 한다.
                        @Override
                        public void run() {
                            Log.e(TAG, "6666 쓰레드에서는 Toast를 띄우지 못한다. runOnUiThread에서 토스트 띄움");
                            Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }catch(MessagingException e){
                    System.out.println("인터넷 문제"+e);
                    Log.e(TAG, "7777 MessagingException 인터넷 문제"+e);

                    L_join.this.runOnUiThread(new Runnable() { //쓰레드에서는 Toast를 띄우지 못하여 runOnUiThread를 호출해야 한다.
                        @Override
                        public void run() {
                            Log.e(TAG, "8888 쓰레드에서는 Toast를 띄우지 못한다. runOnUiThread에서 토스트 띄움");
                            Toast.makeText(getApplicationContext(),"인터넷 연결을 확인 해 주십시오", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "9999 Exception : "+e);
                }
            }
        }.start();
        return strCode;
    }




    public void ID() {

        et_email_join = (EditText) findViewById(R.id.et_email_join);
        et_email_certifyCode = (EditText) findViewById(R.id.et_email_certifyCode);
        et_email_pw = (EditText) findViewById(R.id.et_email_pw);
        et_email_pw2 = (EditText) findViewById(R.id.et_email_pw2);
        btn_email_send = (Button) findViewById(R.id.btn_email_send);
        btn_email_certify = (Button) findViewById(R.id.btn_email_certify);

        btn_doJoin = (Button) findViewById(R.id.btn_doJoin);

    }

}
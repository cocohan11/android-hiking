//package com.example.iamhere;
//
//import static com.example.iamhere.L_login.myEmail;
//import static com.example.iamhere.L_login.myRoomActive;
//import static com.example.iamhere.L_login.방비번;
//import static com.example.iamhere.L_login.방이름;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.iamhere.Interface.Sharing;
//import com.example.iamhere.Model.Sharing_room;
//
//import java.util.regex.Pattern;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class M_share_1_1create_copy extends AppCompatActivity {
//
//
//    String TAG = "M_share_1_1create";
//    EditText et_roomName;  //방장이 방이름설정
//    EditText et_roomPW;  //방장이 비번설정
//    Button btn_createRoomDone;  //방만드는 버튼 /정규식확인하기 /방DB에 삽입되기
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mshare1_1create);
//        Log.e(TAG, "onCreate()");
//        ID();
//
//
//
//
//        //통신을 통해 주고받을 것 정리하자
//        //php로 보낼 것 : 방이름, 방비번, 내email, "RoomCreate"
//        //php로 저장할 것 : Room테이블의 모든 컬럼
//        //통신 결과로 받을 것 : 위치공유방활성화(t/f), 남의정보(프사,이름)-->소켓통신으로 할거니까 패스
//
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡ
//        // 방 만들기 버튼 : DB에 삽입
//        //ㅡㅡㅡㅡㅡㅡㅡㅡ
//        btn_createRoomDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e(TAG, "btn_createRoomDone onClick()");
//
//
//                String get방이름 = String.valueOf(et_roomName.getText()); //사용자가 입력한 방이름 EditText -> String
//                String get방비번 = String.valueOf(et_roomPW.getText()); //변수 위치 : 기능이 버튼뿐이라서 버튼클릭 안에 위치함
//
//
//                //static 대입
//                방이름 = get방이름; //자주쓸거라서
//                방비번 = get방비번;
//
//
//                //test
//                Toast.makeText(getApplicationContext()
//                        , "DB에 저장 /방이름 : "+get방이름+", 방비번 : "+get방비번
//                        , Toast.LENGTH_SHORT).show();
//
//                //정규식
//                //이름2~12, 비번4~8자
//                if(isPattern(get방이름, 2,12) && isPattern(get방비번, 4,8)) {
//
//                    Log.e(TAG, "get방이름,get방비번 둘 다 true");
//                    retrofit_방생성(get방이름, get방비번, myEmail); //통신 - 방이름, 방비번 서버에 전달하기
//
//                } else { //정규식
//                    Toast.makeText(getApplicationContext(), "형식에 맞게 입력해주세요", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//
//    }
//
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//
//
//    public void retrofit_방생성(String name, String pw, String email) {
//
//
//        Log.e(TAG, "retrofit_방생성()입장" +
//                "\nname :"+name+
//                "\npw : "+pw+
//                "\nemail : "+email
//        );
//
//        //ㅡㅡㅡ
//        // 통신 : 레트로핏 객체 생성
//        //ㅡㅡㅡ
//        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
//                .baseUrl("http://15.164.129.103/")    // baseUrl 등록
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build();
//
//
//        //서버 DB에 insert쿼리문 날리기 (느리면 스레드로 빼기)
//        Sharing sharing_interface = retrofit.create(Sharing.class);   // 레트로핏 인터페이스 객체 구현
//        Call<Sharing_room> call = sharing_interface.createRoom(name, pw, email); //방이름, 방비번
//
//
//        //네트워킹 시도
//        call.enqueue(new Callback<Sharing_room>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onResponse(Call<Sharing_room> call, Response<Sharing_room> response) {
//
//                Sharing_room result = response.body(); //response.isSuccessful안에 있으니까 response가 확인이 안되서 로그찍기위해 한 층 꺼냄냄
//                assert result != null;
//
//
//                //정상적으로 통신이 성공한 경우
//                if(response.isSuccessful()) {
//
//                    Log.e(TAG, "response.isSuccessful()");
//                    assert response.body() != null;
//                    Log.e(TAG, "response.body().toString() : " + response.body().toString()); //모든변수 출력
////                    Log.e(TAG, "response.body().activate : " + response.body().activate); //위치공유방 참여중인지
////                    Log.e(TAG, "response.body().activate.equals(\"1\") : " + response.body().activate.equals("1")); // 이미 true이다. 원리는 모르겠음
//
//
//                    //중복이면 Toast
//                    if(response.body().activate == null) { //방이름, 삽입완료되어야만 activate값이 1이 나옴
//                        Toast.makeText(getApplicationContext(), "중복된 방이름입니다.", Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        myRoomActive = response.body().activate.equals("1"); //입장중인지 판별용 변수(입장중true)
//                        Log.e(TAG, "myRoomActive2 : "+myRoomActive);  //true대입됐는지 확인
//
//
//                        //속도 빨라서 여기에서 액티비티 전환해도 될 것 같음
//                        Intent intent = new Intent(getApplicationContext(), M_share_2_Map.class); // -->지도
//                        startActivity(intent);
//                        Log.e(TAG, "startActivity()");
//                    }
//
//
//                } else {
//                    // 통신 실패
//                    Log.e(TAG, "response.isSuccessful() 실패");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Sharing_room> call, Throwable t) {
//                Log.e("onFailure : ", t.getMessage());
//            }
//        }); //~네트워킹 시도
//    }
//
//    //정규식 패턴 - 닉네임
//    public boolean isPattern(String text, int 최소, int 최대) {
//
//        boolean pattern; //리턴해줄 변수 선언
//        String 패턴 = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\\d~!@#$%^&*()+|=]{"+최소+","+최대+"}$"; //한(자음모음ok),영(대소) 최소~최대글자
////        String 패턴 = "^[ㄱ-ㅎ가-힣-zA-Z0-9\\d~!@#$%^&*()+|=]{"+최소+","+최대+"}$"; //한(초성ok),영 최소~최대글자
////        String 패턴 = "^[ㄱ-ㅎ가-힣-zA-Z0-9]{"+최소+","+최대+"}$"; //한(초성ok),영 최소~최대글자
//
//        Log.e(TAG, "text = "+text); // << 방이름 or 비번
//        Log.e(TAG, "최소 = "+최소);
//        Log.e(TAG, "최대 = "+최대);
//
//        if(Pattern.matches(패턴, text)) {  //최소,최대를 파라미터로 넣으려고 변수에 담음
//            pattern = true;
//            Log.e(TAG, "isPattern() true");
//        } else {
//            pattern = false;
//            Log.e(TAG, "isPattern() false");
//        }
//        return pattern; //리턴받아서 if문안에 넣을거임
//    }
//
//    public void ID() {
//
//        et_roomName = (EditText) findViewById(R.id.editText); //입력칸 : '방이름'
//        et_roomPW = (EditText) findViewById(R.id.editText2); //입력칸 : '비밀번호'
//        btn_createRoomDone = (Button) findViewById(R.id.btn_createRoomDone); //버튼 : '방만들기(최대 nn명)'
//
//    }
//
//
//}
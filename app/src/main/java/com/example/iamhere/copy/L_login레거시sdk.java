//package com.example.iamhere;
//
//import static com.example.iamhere.SessionCallback.email;
//import static com.example.iamhere.SessionCallback.id;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.Signature;
//import android.os.Bundle;
//import android.util.Base64;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.iamhere.Interface.Login;
//import com.kakao.auth.AuthType;
//import com.kakao.auth.Session;
//import com.kakao.network.ErrorResult;
//import com.kakao.sdk.common.KakaoSdk;
//import com.kakao.usermgmt.UserManagement;
//import com.kakao.usermgmt.callback.MeV2ResponseCallback;
//import com.kakao.usermgmt.response.MeV2Response;
//import com.kakao.usermgmt.response.model.Profile;
//import com.kakao.usermgmt.response.model.UserAccount;
//import com.kakao.util.exception.KakaoException;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class L_login레거시sdk extends AppCompatActivity {
//
//    String TAG = "L_login";
//    static String imgUrl;
//    static String nickName;
//    EditText et_email;
//    EditText et_pw;
//    Button btn_doLogin;
//    TextView tv_pw_reissue;
//    private long backKeyPressedTime = 0; //뒤로가기 2번 종료
//    //카카오 로그인 api
//    ImageView img_kakaoLogin;
//    TextView tv_goJoin;
//    Session session;//카카오로그인 세션
//    static String kakao결과값;
//    // 세션 콜백 구현
//    private ISessionCallback sessionCallback = new ISessionCallback() {
//        @Override
//        public void onSessionOpened() {
//            Log.i("KAKAO_SESSION", "로그인 성공");
//        }
//
//        @Override
//        public void onSessionOpenFailed(KakaoException exception) {
//            Log.e("KAKAO_SESSION", "로그인 실패", exception);
//        }
//    };
//
//    private SessionCallback sessionCallback = new SessionCallback() {
//        @Override
//        public void onSessionOpened() { //세션이 있다면(자동로그인,새로로그인해도 여기로 옴)
//
////            회원가입/로그인 후 onSessionOpened로
////            돌아오고 M_main으로 이동함
//            //어떻게 L_profile로 이동할 수 있을까
//            //우선 M으로 넘기고 만약 닉네임이없으면 닉네임작성하라고할까?
//
//
//            //카카오 세션로그인 되어있으면 자동로그인. M_main이동
//            //작동이 느리면 L_login화면이 보일 수 있음
//            Log.e("L_login", "onSessionOpened()111");
//            Log.e("L_login", "email: "+email);
//            Log.e("L_login", "id: "+id);
//
//            Intent intent = new Intent(getApplicationContext(), M_main.class);
//            intent.putExtra("L_login_kakao", "L_login_kakao"); //M_main에서 일반로그인과 sns로그인을 구분하기 위한 용도
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //현재화면빼고 아래액티비티 지움
//            startActivity(intent);
//            finish();
//            Log.e("L_login", "startActivity() >>> M_main");
//        }
//        @Override
//        public void onSessionOpenFailed(KakaoException exception) {
//            Log.e(TAG, "onSessionOpenFailed()"); //세션이 없다면이 아니라 세션을여는데 실패하면 여기로 들어오는 것 같음
//        }
//    };
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_llogin);
//        Log.e(TAG,"onCreate");
//        ID();
//        Log.e(TAG, "getHashKey()");
//        getHashKey();
//        Log.e(TAG, "getHashKey() 끝");
//        KakaoSdk.init(this, "45268cd1f4853c061305952aadd29a90"); //설치 및 설정 후 안드로이드 sdk를 사용하기 위해선 가장먼저 네이티브 앱 키로 초기화해야한다.(?)
//
////        // SDK 초기화(??) 에러남;
////        KakaoSDK.init(new KakaoAdapter() {
////            @Override
////            public IApplicationConfig getApplicationConfig() {
////                return new IApplicationConfig() {
////                    @Override
////                    public Context getApplicationContext() {
////                        return L_login.this;
////                    }
////                };
////            }
////        });
//        //프로필 작성했는가?
//                //yes) main고고
//                //no) L_profile 고고
//
//        //쉐어드 1.autoLogin(로그인통과된 적 있는지)
//        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//        String UserEmail = auto.getString("UserEmail",null); //없다면 null이다?
//        String UserPwd = auto.getString("UserPwd",null);
//        String UserNickName = auto.getString("UserNickName",null); //닉네임은 join과 따로 저장됨. 선별용 변수
//        String UserImg = auto.getString("UserImg",null); //로그인할 때 이미지도 같이 쉐어드에 새로 저장시켜서 M_main에서 사용하ㅣㄱ
//        Log.e(TAG, "UserEmail : "+UserEmail);
//        Log.e(TAG, "UserPwd : "+UserPwd);
//        Log.e(TAG, "UserNickName : "+UserNickName);
//        Log.e(TAG, "UserImg : "+UserImg);
//
//        if(UserEmail != null && UserPwd != null) { //로그아웃이 아니라면 자동로그인한다.
//            Log.e(TAG, "SharedPreferences 값 존재, 자동로그인 ");
//
//            //자동로그인 후 프로필작성했는가?
//            //yes) >> M_mail이동
//            //no) >> L_profile이동
//
//            if (UserNickName != null) { //닉네임을 등록했으면 바로 main이동 (닉네임은 필수값/ 사진은 필수값x)
//
//                Intent intent = new Intent(getApplicationContext(), M_main.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //현재화면빼고 아래액티비티 지움
//                Log.e(TAG, "Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK");
//                startActivity(intent);
//                Log.e(TAG, "startActivity()");
//                finish();
//
//            }
//        }
//
//
//        //Retrofit 인스턴스 생성
//        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
//                .baseUrl("http://15.164.129.103/")    // baseUrl 등록
//                .addConverterFactory(GsonConverterFactory.create())  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
//                .build();
//
//        Login Login_interface = retrofit.create(Login.class);   // 레트로핏 인터페이스 객체 구현
//
//        //카카오톡 자동로그인
//        session = Session.getCurrentSession(); //?
//        Log.e(TAG, "session 1: "+session); //세션에 어떤값이 들어갈까 (com.kakao.auth.Session@1f4070d)
//        session.addCallback(sessionCallback); //?
//        Log.e(TAG, "session 2: "+session); //세션에 어떤값이 들어갈까 (com.kakao.auth.Session@1f4070d) 같은값이네.
//
//
//        //이 코드가 있어야 위에서 오버라이드한 onSessionOpened()메소드가 돌아감
//        if (Session.getCurrentSession().checkAndImplicitOpen()) {
//            UserManagement.getInstance().me(new MeV2ResponseCallback() {
//                @Override
//                public void onSessionClosed(ErrorResult errorResult) { //세션 유지x
//                    Log.e(TAG, "onSessionClosed()");
//                    Log.e(TAG, "kakao결과값: "+kakao결과값);
//                    Log.e(TAG, "email: "+email);
//                    Log.e(TAG, "id: "+id);
//                }
//                @Override
//                public void onSuccess(MeV2Response result) { //자동로그인 시에 옴(새로로그인 후에는 여기로 안 옴)
//                    //흑흑 result파라미터로 가져오면 되는거였음
//                    Log.e(TAG, "onSuccess~~()");
//
//                    UserAccount kakaoAccount = result.getKakaoAccount(); //계정 정보
//                    Profile profile = kakaoAccount.getProfile(); //프로필 정보
//
//                    email = kakaoAccount.getEmail();
//                    id = String.valueOf(result.getId());
//                    nickName = profile.getProfileImageUrl();
//                    imgUrl = profile.getProfileImageUrl();
//
//                    Log.e(TAG, "id: "+id);
//                    Log.e(TAG, "email: "+ email);
//                    Log.e(TAG, "getNickname: "+ profile.getNickname());
//                    Log.e(TAG, "getProfileImageUrl: "+ profile.getProfileImageUrl());
//                    Log.e(TAG, "토스트");
//
//                    //카카오로그인 후 첫 이용이면..
//                    //카카오api로 가져온 프로필사진, 닉네임 저장하기
//                    if(email != null) {
//
//                        //카카오톡 프로필사진,닉네임 가져와서 서버에 저장
//                        // or 서버에서 불러오기
//
//
//
//
//                    }
//                    Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();
//
//                    //여기서 프로필사진 넣기
//                }
//            });
//        }
//
//
//        //ㅡㅡㅡㅡㅡㅡㅡ
//        // 카카오 로그인
//        //ㅡㅡㅡㅡㅡㅡㅡ
//        img_kakaoLogin.setOnClickListener(v -> {
//            Log.e(TAG, "img_kakao 클릭");
//
//            if (Session.getCurrentSession().checkAndImplicitOpen()) {
//                Log.e(TAG, "onClick: 로그인 세션살아있음");
//                // 카카오 로그인 시도 (창이 안뜬다.)
//                sessionCallback.requestMe();
//                Log.e(TAG, "requestMe()끝");
//
//            } else {
//                Log.e(TAG, "onClick: 로그인 세션끝남");
//                // 카카오 로그인 시도 (창이 뜬다.)
//                session.open(AuthType.KAKAO_LOGIN_ALL, L_login레거시sdk.this);
//                Log.e(TAG, "onClick: 로그인 세션끝남22 여기서 인텐트로 화면이동하면 안됨?");
//                //여기서 인텐트로 화면이동하면 안됨?
//
//            }
//        });
//
//
//        //ㅡㅡㅡㅡㅡㅡㅡ
//        // 로그인 버튼 : 클릭시 서버에 email,pw가 요청보내지고 서버에서 지정된 응답을 받음
//        //ㅡㅡㅡㅡㅡㅡㅡ
//        btn_doLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String str_email = String.valueOf(et_email.getText()); //서버로 보낼 email
//                String str_pw = String.valueOf(et_pw.getText()); //서버로 보낼 pw
//                Log.e(TAG,"버튼클릭");
//                Log.e(TAG, "에딧텍스트 이메일 (str_email) : "+str_email); //ok
//                Log.e(TAG, "에딧텍스트 이메일 (str_pw) : "+str_pw); //ok
//
//                Call<com.example.iamhere.Model.Login> call = Login_interface.getUser(str_email, str_pw); //인터페이스 객체를 이용해 인터페이스에서 정의한 함수를 호출하면 Call 객체를 얻을 수 있다. //에러났던 이유:DataClass를 참조하는데 interface에서 불러올 때 변수가 없어서.
//
//                //네트워킹 시도
//                call.enqueue(new Callback<com.example.iamhere.Model.Login>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    public void onResponse(Call<com.example.iamhere.Model.Login> call, Response<com.example.iamhere.Model.Login> response) {
//
//                        com.example.iamhere.Model.Login result = response.body(); //response.isSuccessful안에 있으니까 response가 확인이 안되서 로그찍기위해 한 층 꺼냄냄
//                        assert result != null;
//                        Log.e(TAG, "onResponse / result.getUserNickName: "+result.getCreateDate());
//
//                        if(response.isSuccessful()) {
//                            //정상적으로 통신이 성공한 경우
//                            Log.e(TAG, "onResponse success");
//
//                            // 서버에서 응답받은 데이터를 토스트로 띄운다
//                            String 결과값 = result.getResponse();
//                            String getNickName = result.getNickName();
//                            String getImgUrl = result.getImgUrl();
//                            Log.e(TAG, "onResponse / 결과값: "+결과값);
//                            Log.e(TAG, "onResponse / aa result.getNickName: "+result.getNickName());
//
//
//                            if(결과값.equals("true")) { //로그인 성공
//                                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
//
//                                //로그인하면 앞으로 자동로그인되도록 하기(파일명 : 이메일이름)
//                                SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능
//                                SharedPreferences.Editor autoLoginEdit = auto.edit();
//                                Log.e(TAG, "str_email : "+str_email);
//                                Log.e(TAG, "str_pw : "+str_pw);
//
//                                autoLoginEdit.putString("UserEmail", str_email); //db컬럼명과 동일하게 하자
//                                autoLoginEdit.putString("UserPwd", str_pw); //파라미터로 받은 pw
//                                autoLoginEdit.putString("UserNickName", getNickName);
//                                autoLoginEdit.putString("UserImg", getImgUrl);
//
//                                autoLoginEdit.commit(); //실질 저장
//
//
//                                //서버에 닉네임이 있다면 바로 main가고 없으면 프로필작성하러 가기
//                                nickName = result.getNickName();//서버에서 받아온 닉네임
//                                Log.e(TAG, "result.getNickName() --> nickName : "+nickName);
//
//                                if(nickName != null) { //닉네임을 작성했다면 main으로 이동
//
//                                    // 결과값 있으면 로그인 통과 / intent를 이용해 M_main으로 이동시킴
//                                    Intent intent = new Intent(getApplicationContext(), M_main.class); //로그인btn >> 지도첫화면
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //top빼고 아래액티비티 지움
//                                    Log.e(TAG, "Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK");
//                                    startActivity(intent);
//                                    Log.e(TAG, "startActivity()");
//                                    Log.e(TAG, ">>> M_main");
//
//                                } else { //닉네임 작성한 적 없다면 L_profile이동
//
//                                    Intent intent = new Intent(getApplicationContext(), L_profile.class); //로그인btn >> 지도첫화면
//                                    intent.putExtra("loginEmail", str_email);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //top빼고 아래액티비티 지움
//                                    Log.e(TAG, "Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK");
//                                    startActivity(intent);
//                                    Log.e(TAG, "startActivity()");
//                                    Log.e(TAG, ">>> L_profile");
//                                }
//
//                            } else { //로그인 실패
//                                Log.e(TAG, "!(결과값.equals(true)");
//                                //아이디와 비밀번호를 다시 확인해 달라는 토스트띄우기
//                                Toast.makeText(getApplicationContext(), "이메일과 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
//                            }
//
//                        } else {
//                            // 통신 실패
//                            Log.e("콜.enqueue : ", "onResponse : 실패");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<com.example.iamhere.Model.Login> call, Throwable t) {
//                        Log.e("onFailure : ", t.getMessage());
//                    }
//                });
//            }
//        });//~로그인 버튼
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        // 회원가입 하러가기
//        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//        tv_goJoin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e(TAG, "tv_goJoin 클릭");
//                Intent intent = new Intent(getApplicationContext(), L_join.class);
//                startActivity(intent);
//                Log.e(TAG, "startActivity()");
//            }
//        });
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡ
//        // 비밀번호 재발급
//        //ㅡㅡㅡㅡㅡㅡㅡㅡ
//        tv_pw_reissue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "비밀번호를 잊었어요", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), L_PwReissue.class); // 로그인 >> 비밀번호분실
//                startActivity(intent);
//                Log.e(TAG, "startActivity()");
//
//            }
//        });
//
//    }
//
//
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//
//
//    //카카오로그인 api를 사용하기 위해 필요한 디버그 해시키(해시키 중 디버그 해시키를 코드상에서 가져옴)
//    @SuppressLint("PackageManagerGetSignatures")
//    private void getHashKey(){
//        Log.e(TAG, "getHashKey() 들어옴");
//
//        PackageInfo packageInfo = null;
//
//        try {
//            Log.e(TAG, "packageInfo에 대입");
//            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        Log.e(TAG, "packageInfo : "+ packageInfo);
//        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null");
//        Log.e(TAG, "for문 전");
//
//        assert packageInfo != null;
//        for (Signature signature : packageInfo.signatures) {
//            Log.e(TAG, "for문 입장");
//
//            try {
//                Log.e(TAG, "try 입장");
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.e("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            } catch (NoSuchAlgorithmException e) {
//                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
//            }
//        }
//    }
//
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
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
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
//    public void ID() {
//
//        et_email = (EditText) findViewById(R.id.et_email); //이메일 입력상자
//        et_pw = (EditText) findViewById(R.id.et_pw); //비밀번호 입력상자
//        btn_doLogin = (Button) findViewById(R.id.btn_doLogin); //로그인 버튼
//        tv_pw_reissue = (TextView) findViewById(R.id.tv_pw_reissue); //비밀번호 분실
//        img_kakaoLogin = (ImageView) findViewById(R.id.img_kakaoLogin); //카카오톡 로그인 api 버튼
//        tv_goJoin = (TextView) findViewById(R.id.tv_goJoin); //회원가입하러가기
//    }
//
//}
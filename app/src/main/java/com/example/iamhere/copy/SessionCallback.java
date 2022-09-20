//package com.example.iamhere;
//
//import static com.example.iamhere.L_login.kakao결과값;
//
//import android.annotation.SuppressLint;
//import android.util.Log;
//
//import com.example.iamhere.Interface.kakaoLogin;
//import com.example.iamhere.Model.Login;
//import com.kakao.auth.ISessionCallback;
//import com.kakao.network.ErrorResult;
//import com.kakao.sdk.user.UserApiClient;
//import com.kakao.sdk.user.model.Account;
//import com.kakao.sdk.user.model.User;
//import com.kakao.usermgmt.UserManagement;
//import com.kakao.usermgmt.callback.MeV2ResponseCallback;
//import com.kakao.usermgmt.response.MeV2Response;
//import com.kakao.usermgmt.response.model.Profile;
//import com.kakao.usermgmt.response.model.UserAccount;
//import com.kakao.util.exception.KakaoException;
//
//import kotlin.Unit;
//import kotlin.jvm.functions.Function2;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class SessionCallback implements ISessionCallback {
//
//    String TAG = "카카오 SessionCallback";
//    String 결과유;
//    static String email;
//    static String id;
//
//    // 로그인에 성공한 상태
//    @Override
//    public void onSessionOpened() {
//        requestMe();
//    }
//
//    // 로그인에 실패한 상태
//    @Override
//    public void onSessionOpenFailed(KakaoException exception) {
//        Log.e(TAG, "onSessionOpenFailed : " + exception.getMessage());
//    }
//
//    // 사용자 정보 요청
//    // 회원가입x 로그인o ??
//    public void requestMe() { //스레드 동기화 / 실행 뒤 마저실행
//        UserManagement.getInstance()
//        .me(new MeV2ResponseCallback() {
//            @Override
//            public void onSessionClosed(ErrorResult errorResult) {
//                Log.e(TAG, "세션이 닫혀 있음: " + errorResult);
//            }
//
//            @Override
//            public void onFailure(ErrorResult errorResult) {
//                Log.e(TAG, "사용자 정보 요청 실패: " + errorResult);
//            }
//
//            @Override
//            public void onSuccess(MeV2Response result) {
//
//                Log.e(TAG, "사용자 아이디: " + result.getId());
//                id = String.valueOf(result.getId());
//                UserAccount kakaoAccount = result.getKakaoAccount();
//                if (kakaoAccount != null) {
//
//                    // 이메일
//                    email = kakaoAccount.getEmail();
//                    Profile profile = kakaoAccount.getProfile();
//                    if (profile ==null){
//                        Log.e(TAG, "onSuccess:profile null ");
//                    }else{
//                        Log.e(TAG, "onSuccess:getProfileImageUrl "+profile.getProfileImageUrl());
//                        Log.e(TAG, "onSuccess:getNickname "+profile.getNickname());
//                    }
//                    if (email != null) {
//
//                        Log.e(TAG, "onSuccess:email "+email);
//
//                        //여기에 카카오로그인 한 이메일을 서버에 회원가입한다.
//                        //조회된 이메일이 없다면 처음이니까 insert쿼리보내서 회원가입하고
//                        //있다면 그냥 바로 M_main으로 이동해서 내역 불러오기
//                        //pw대신 사용자아이디를 보낸다. ex)2220848388
//
//                        //Retrofit 인스턴스 생성
//                        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
//                                .baseUrl("http://15.164.129.103/")    // baseUrl 등록
//                                .addConverterFactory(GsonConverterFactory.create())  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
//                                .build();
//
//                        kakaoLogin kakaoLogin_interface = retrofit.create(kakaoLogin.class);   //레트로핏 인터페이스 객체 구현
//                        Call<Login> call = kakaoLogin_interface.getKakaoLogin(email, id); //응답
//
//                        //네트워킹 시도
//                        call.enqueue(new Callback<Login>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
//                            @SuppressLint("SetTextI18n")
//                            @Override
//                            public void onResponse(Call<Login> call, Response<Login> response) {
//
//                                com.example.iamhere.Model.Login result = response.body(); //response.isSuccessful안에 있으니까 response가 확인이 안되서 로그찍기위해 한 층 꺼냄냄
//                                assert result != null;
//                                Log.e(TAG, "onResponse / result.getUserNickName: "+result.getCreateDate());
//
//                                if(response.isSuccessful()) {
//                                    //정상적으로 통신이 성공한 경우
//                                    Log.e(TAG, "onResponse success");
//
//                                    // 서버에서 응답받은 데이터를 토스트로 띄운다
//                                    String 결과값 = result.getResponse();
//                                    kakao결과값 = 결과값;
//                                    Log.e(TAG, "onResponse / 결과값: "+결과값);
//                                    Log.e(TAG, "onResponse / kakao결과값: "+kakao결과값);
//
//                                } else {
//                                    // 통신 실패
//                                    Log.e(TAG, "onResponse : 실패");
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<com.example.iamhere.Model.Login> call, Throwable t) {
//                                Log.e(TAG, t.getMessage());
//                            }
//                        });
//                    }
//
//                }else{
//                    Log.e(TAG, "onSuccess: kakaoAccount null");
//                }
//
//            }
//        });
//
//    }
//
//
//    //카카오 사용자 정보(사진, 닉네임)
//    private void getKaKaoProfile() {
//        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
//            @Override
//            public Unit invoke(User user, Throwable throwable) {
//                if (user != null) {
//                    //계정정보를 불러 왔을 경우(요청성공)
//                    Log.d(TAG, "Kakao id =" + user.getId());
//
//                    Account kakaoAccount = user.getKakaoAccount(); //카카오계정(이메일?)
//                    Log.d(TAG, "kakaoAccount =" + user.getKakaoAccount());
//
//                    if (kakaoAccount != null) {
//                        //프로필필
//                        com.kakao.sdk.user.model.Profile profile = kakaoAccount.getProfile();
//                        if (profile != null) {
//                            String nickname = profile.getNickname();
//                            String profileImg = profile.getProfileImageUrl();
//                            Log.d(TAG, "Kakao nickname =" + nickname);
//                            Log.d(TAG, "Kakao profileImg =" + profileImg);
//                            // thumbnail image: profile.getThumbnailImageUrl()
//                        }
//                    }
//
//
//                } else {
//                    //계정정보가 없을경우(요청실패)
//                }
//                if (throwable != null) {
//                    Log.d(TAG, "invoke: " + throwable.getLocalizedMessage());
//                }
//                return null;
//            }
//        });
//    }
//}
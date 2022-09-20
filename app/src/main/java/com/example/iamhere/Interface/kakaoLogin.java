package com.example.iamhere.Interface;

import com.example.iamhere.Model.Login_find;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface kakaoLogin {

    @FormUrlEncoded //POST방식 사용시 입력해야함
    @POST("iamhere/kakaoLogin.php") //URL주소의 BaseDomain이하의 주소를 입력
    Call<Login_find> getKakaoLogin( //<반환타입>
                                    @Field("UserEmail") String UserEmail,
                                    @Field("UserPwd") String UserPwd //서버로 보낼 이름
    ); //파리미터이름, 데이터 타입, 변수명

    @FormUrlEncoded //처음 로그인시 회원가입인지 확인, 다른정보 update
    @POST("iamhere/kakaoLogin.php") //URL주소의 BaseDomain이하의 주소를 입력
    Call<Login_find> KakaoLoginNewCheck( //<반환타입>
                                         @Field("UserEmail") String UserEmail,
                                         @Field("UserPwd") String UserPwd,
                                         @Field("UserNickName") String UserNickName,
                                         @Field("UserImg") String UserImg
    );

}

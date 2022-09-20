package com.example.iamhere.Interface;
import com.example.iamhere.Model.Login_find;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PwReissue {
//비번 분실시 서버에 보내는 데이터
//모델은 따로 만들지 않을거임 받아올 건 true/false인지밖에 없으니.

    @FormUrlEncoded //Field형식 사용시 입력해야함(Form이 인코딩되어야함?)
    @POST("iamhere/pwReissue.php") //URL주소의 BaseDomain이하(마지막/)의 주소를 입력
    Call<Login_find> chgPW( //<반환타입>
                            @Field("UserEmail") String UserEmail, //@Field : post메소드인경우 주로 쓰는 형식
                            @Field("UserPwd") String UserPwd  //@Field형식이 여러개일 때 FieldMap을사용한다.
   ); //php에서 받을 이름, 데이터 타입, 변수명
}

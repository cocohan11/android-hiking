package com.example.iamhere.Interface;
import com.example.iamhere.Model.Login_find;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Login {

    @FormUrlEncoded //POST방식 사용시 입력해야함
    @POST("iamhere/login.php") //URL주소의 BaseDomain이하의 주소를 입력(EndPoint~parameter)
    Call<Login_find> getUser( //<반환타입>
                              @Field("UserEmail") String UserEmail,
                              @Field("UserPwd") String UserPwd //Field, Path(post사용), Query뭐가다르지? //서버로 보낼 상자인듯?
    ); //파리미터이름, 데이터 타입, 변수명

}

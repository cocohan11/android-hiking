package com.example.iamhere.Interface;

import com.example.iamhere.Model.Profile_ed;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Profile {

    //사진,닉네임
    //생성/수정 메소드
    @Multipart //file보낼 때 어노테이션 추가
    @POST("iamhere/Profile.php") //URL주소의 BaseDomain이하의 주소를 입력
    Call<Profile_ed> getFile( //<반환타입>
                              @Part MultipartBody.Part file,
                              @Part("ProfEmail") String ProfEmail,
                              @Part("ProfNickName") String ProfNickName,
                              @Part("ProfFunction") String ProfFunction //선별용으로 같이 기능명을 보냄. 같은 join이라도 여러번 서버에 요청하게 되어서 구분할 필요성이 있음
                              //@Part parameters can only be used with multipart encoding. @Part어노테이션은 멀티파트에서만 사용한다.
    );


    //닉네임만
    //생성/수정 메소드
    @FormUrlEncoded //POST방식 사용시 입력해야함
    @POST("iamhere/Profile.php") //URL주소의 BaseDomain이하의 주소를 입력
    Call<Profile_ed> getNickname( //<반환타입>
                                  @Field("ProfEmail") String ProfEmail,
                                  @Field("ProfNickName") String ProfNickName,
                                  @Field("ProfFunction") String ProfFunction
    );


    // 프사 변경 후 합성프사 마커를 서버에 올린다. getFile()과 같이 움직인다.
    @Multipart //file보낼 때 어노테이션 추가
    @POST("iamhere/profileMarker.php")
    Call<Profile_ed> getProfileMarker( //<반환타입>
                              @Part MultipartBody.Part file,
                              @Part("ProfEmail") String ProfEmail // key
                              //@Part parameters can only be used with multipart encoding. @Part어노테이션은 멀티파트에서만 사용한다.
    );



} //Field는 사용불가라고 함

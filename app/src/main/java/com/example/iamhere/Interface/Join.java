package com.example.iamhere.Interface;
import com.example.iamhere.Model.Join_create;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Join {

    @FormUrlEncoded //POST방식 사용시 입력해야함
    @POST("iamhere/join.php") //URL주소의 BaseDomain이하의 주소를 입력
    Call<Join_create> getOverlap( // Call<반환타입> , 파라미터:php로 보내는 데이터. php는 키값인 ""으로 구분함
                                  @Field("JoinEmail") String JoinEmail, //php의 $_POST['JoinEmail']로 가는 부분
                                  @Field("JoinPW") String JoinPW,
                                  @Field("JoinNickName") String JoinNickname,
                                  @Field("JoinFunction") String JoinFunction //선별용으로 같이 기능명을 보냄. 같은 join이라도 여러번 서버에 요청하게 되어서 구분할 필요성이 있음
    );



}

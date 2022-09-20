package com.example.iamhere.Interface;

import com.example.iamhere.Model.ClientInfo;
import com.example.iamhere.Model.Clinet_list;
import com.example.iamhere.Model.Sharing_room;
import com.example.iamhere.Model.getMarker;
import com.example.iamhere.Model.re_sharingRoom;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Sharing {


    //방 중복체크할 때
    @FormUrlEncoded
    @POST("iamhere/check_re.php")
    Call<Sharing_room> checkRepetition(
            @Field("Room_name") String Room_name //전송 : 이름, 응답 : 방이름으로 검색된 결과의 갯수
    );


    //방 생성시 보낼 데이터
    @FormUrlEncoded
    @POST("iamhere/createRoom.php")
    Call<Sharing_room> createRoom(
           //php로 보낼 데이터
           @Field("Room_name") String Room_name,
           @Field("Room_pw") String Room_pw,
           @Field("Room_president_email") String Room_president_email //파라미터 순서 실수 조심
    );

//    // 참여자가 입장하면 RoomUser에 명단이 추가된다.
//    @FormUrlEncoded
//    @POST("iamhere/createRoom.php")
//    Call<Sharing_room> joinTheRoom(
//            @Field("Room_no") String Room_no,
//            @Field("UserEmail") String UserEmail
//    );


    //ArrayList타입으로 서버에 전송
    @FormUrlEncoded
    @POST("iamhere/markerList.php") //서버 주소
    Call<Sharing_room> markerArraylistSend( //이 요청을 성공이면 단순히 성공인지만 응답받는다.(저장목적이라)

            @Field("MarkerLatList[]") ArrayList<Double> MarkerLatList, //위도 리스트
            @Field("MarkerLngList[]") ArrayList<Double> MarkerLngList, //경도 리스트 담김
            @Field("roomName") String roomName //방이름으로 방넘버 조회후 RouteMarker테이블에 추가하기

    ); //Field("list[]") 에 key 값으로 []<- 이 포함되어야 php에서 리스트로 전송받는다

    //ArrayList타입으로 서버로부터 수신
    //방 중복체크할 때
    @FormUrlEncoded
    @POST("iamhere/markerList_get.php")
    Call<Sharing_room> markerArraylistGet(

            @Field("Room_no") String Room_no //방번호(쉐어드저장)를 보내면 RouteMaarker테이블에서 row를 추출하겠지

    );

    //위치공유방 참여자가 경로마커 보기
    @FormUrlEncoded
    @POST("iamhere/markerList_get2.php")
    Call<ArrayList<getMarker>> markerArraylistGet2(

            @Field("Room_no") String Room_no //방번호(쉐어드저장)를 보내면 RouteMaarker테이블에서 row를 추출하겠지

    );

    //등산시작버튼 누르면 등산시작시간 업뎃
    @FormUrlEncoded
    @POST("iamhere/startTracking.php")
    Call<Sharing_room> startTracking(

            @Field("startTracking") String Room_no, //방번호 보내기
            @Field("UserEmail") String UserEmail, //유저이메일
            @Field("Room_address") String  Room_address //추출된 주소
    );

    //방장) 위치공유방 퇴장/강제퇴장 등산끝난시간 업뎃
    @FormUrlEncoded
    @POST("iamhere/finishTracking.php")
    Call<Sharing_room> finishTracking(

            @Field("Room_no") String Room_no, //방번호로 선별
            @Field("UserEmail") String UserEmail, //명단에 추가
            @Field("Room_leadTime") String Room_leadTime //소요시간 "ㅇㅇ시간 ㅇㅇ분"
    );

    //어플종료 후 위치공유방 들어갔는데 이미 방이 없어진 경우
    @FormUrlEncoded
    @POST("iamhere/alreadyFinishTracking.php")
    Call<Sharing_room> alreadyFinishTracking( //이미 방이 종료된 경우

            @Field("Room_no") String Room_no, //방번호로 선별
            @Field("UserEmail") String UserEmail, //명단에 추가
            @Field("Room_finishedDate") String finishDate, //명단에 추가
            @Field("Room_leadTime") String Room_leadTime //소요시간 "ㅇㅇ시간 ㅇㅇ분"
    );


    //지도 썸네일 사진저장하기
    @Multipart
    @POST("iamhere/mapCapture.php")
    Call<Sharing_room> mapCapture( //이미 방이 종료된 경우

           @Part MultipartBody.Part file, //캡쳐한 지도썸네일
           @Part("Room_no") String Room_no //방번호로 선별
    );


    //리사이클러뷰에 필요한 data 받기 ..어떤형태로?
    @FormUrlEncoded
    @POST("iamhere/recyclerviewRecord.php")
    Call<ArrayList<re_sharingRoom>> recyclerviewRecord( //re_sharingRoom 타입이다. 인터페이스 또 만들기 싫어서여기에 만듦. 에러나는지 주의

             @Field("UserEmail") String UserEmail,
             @Field("Selected_YYYY") String Selected_YYYY,
             @Field("Selected_M") String Selected_M
    );

    //리사이클러뷰에 필요한 data 받기 ..어떤형태로?
    @FormUrlEncoded
    @POST("iamhere/recyclerviewRecordGetList.php")
    Call<ArrayList<Clinet_list>> recyclerviewRecordGetList(

               @Field("Room_no") String Room_no
    );



//    //리사이클러뷰에 필요한 data 받기 ..어떤형태로?
//    @FormUrlEncoded
//    @POST("iamhere/recyclerviewRecord.php")
//    Call<re_sharingRoom> recyclerviewRecord( //re_sharingRoom 타입이다. 인터페이스 또 만들기 싫어서여기에 만듦. 에러나는지 주의
//
//                @Field("UserEmail") String UserEmail
//    );

    //나의 위치공유 참여기록에서 삭제요청 -> 컬럼에 삭제요청했다는 0/1만 표기해서, 조회할땐 안 보여주기
    @FormUrlEncoded
    @POST("iamhere/recyclerviewDelete.php")
    Call<Sharing_room> recyclerviewDelete( //기록 삭제 요청(실제데이터는 삭제x)

          @Field("Room_no") String Room_no, //방번호와
          @Field("UserEmail") String UserEmail //이메일로 row찾아냄
    );


    //위치공유방 참여자 입장가능한지 묻기
    @FormUrlEncoded
    @POST("iamhere/joinRoom.php")
    Call<Sharing_room> joinRoom(
            //php로 보낼 데이터
            @Field("Room_name") String Room_name, //방이름, 방비번 보내고 입장가능한지만 응답받기
            @Field("Room_pw") String Room_pw,
            @Field("UserEmail") String UserEmail
    );

    //위치공유방 퇴장/강제퇴장 등산끝난시간 업뎃
    @FormUrlEncoded
    @POST("iamhere/exitTracking.php")
    Call<Sharing_room> exitTracking(

            @Field("Room_no") String Room_no, //방번호와 이메일로 선별
            @Field("UserEmail") String UserEmail,
            @Field("Room_leadTime") String Room_leadTime //소요시간 "ㅇㅇ시간 ㅇㅇ분"
    );





}

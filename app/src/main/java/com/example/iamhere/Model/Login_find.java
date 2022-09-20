package com.example.iamhere.Model;
import com.google.gson.annotations.SerializedName;

//데이터 모델 클래스 생성
//목적 : 데이터를 담기 위함

public class Login_find {
    //원리 : 결과로 받는 JSON객체에서 키 값이 UserNum인 데이터를 UserNum변수 안에 넣어준다.

    private String UserEmail; //이게 없어서 에러났음 / 이유: interface에서 DataClass를 참조하면서 UserEmail를 불렀는데 존재하지 않아서 에러가 났다.
    private String UserPwd;

    @SerializedName("CreateDate") //Json객체에서 해당 이름의 데이터값을 가져올 수 있게 해주는 Annotation이다. 반드시 이름동일/ Annotation이란, 코드 사이에 주석처럼 쓰여서 특별한 의미, 기능을 수행하도록 하는 기술이다.
    private String CreateDate; //서버에서 응답받은 데이터가 이 변수에 담긴다. Retorofit이라는 클래스에서 참조할 때 불러옴.
    @SerializedName("response")
    private String response; //true/false
    @SerializedName("imgUrl")
    private String imgUrl;
    @SerializedName("nickName")
    private String nickName;

    public String getCreateDate() {
        return CreateDate;
    }
    public String getResponse() {
        return response;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public String getNickName() {
        return nickName;
    }

    @Override
    public String toString() {
        return "PostResult{" +
                "CreateDate=" + CreateDate +
                "}";
    }

}

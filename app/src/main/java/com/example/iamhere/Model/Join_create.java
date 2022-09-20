package com.example.iamhere.Model;

import com.google.gson.annotations.SerializedName;

public class Join_create {

    private String JoinEmail; //사용자가 서버로 보낼 본인 이메일
    private String JoinPW;

    private String sentence; //join시 서버에서 받을 인증번호
    private String response; //true/false

    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }
    public String getSentence() { //받아온 인증번호를 L_join에서 사용할 수 있게 getter setter로 메소드 만듦
        return sentence;
    }
    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

}

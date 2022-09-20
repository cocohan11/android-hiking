package com.example.iamhere.Model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class re_sharingRoom {

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    // 리사이클러뷰에 들어갈 데이터를 담을 클래스
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    //변수 생성
    //생성자 생성
    @Expose
    @SerializedName("Room_mapCapture")
    public String Room_mapCapture;

    @Expose
    @SerializedName("Room_name")
    public String Room_name;

    @Expose
    @SerializedName("Room_address")
    public String Room_address;

    @Expose
    @SerializedName("Room_startEndTime")
    public String Room_startEndTime;

    @Expose
    @SerializedName("Room_leadTime")
    public String Room_leadTime;

    @Expose
    @SerializedName("Room_no")
    public String Room_no; //방번호에 해당하는 마커 불러와서 찍기

    @Expose
    @SerializedName("peopleNum") //아직 가라데이터
    public String peopleNum;

    public String getRoom_no() {
        return Room_no;
    }

    public String getRoom_mapCapture() {
        return Room_mapCapture;
    }

    public String getRoom_name() {
        return Room_name;
    }

    public String getRoom_address() {
        return Room_address;
    }

    public String getRoom_startEndTime() {
        return Room_startEndTime;
    }

    public String getRoom_leadTime() {
        return Room_leadTime;
    }

    public String getPeopleNum() {
        return peopleNum;
    }

    // 이 생성자대로 어레이리스트 객체가 만들어짐
    public re_sharingRoom(String Room_mapCapture, String Room_name, String Room_address, String Room_startEndTime
                        , String Room_leadTime, String peopleNum) {

        this.Room_mapCapture = Room_mapCapture;
        this.Room_name = Room_name;
        this.Room_address = Room_address;
        this.Room_startEndTime = Room_startEndTime;
        this.Room_leadTime = Room_leadTime;
        this.peopleNum = peopleNum;

    }

}

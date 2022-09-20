package com.example.iamhere.Model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.naver.maps.map.overlay.Marker;

import java.net.Socket;
import java.util.ArrayList;

public class ClientInfo {


    // retrofit으로 받아올 명단때문에 @붙임
    public String name; // 방금 - 닉네임(중복가능성있음)
    public String Img;
    public String markerImg; // 방금 - 합성이미지 서버 주소
    public String purposes; // 방금 - 입장/채팅/위치/퇴장
    public String email; // 방금 - 식별값
    public String msg; // 방금 - 서버로부터 받은 메세지(ui로 업뎃)
    public String chatTime; // 방금
    public String chatFrom; // 방금
    public double Lat; // 방금 받아온 위도
    public double Lng; // 방금
    public Marker marker; //
    public Bitmap bitmap; //


    public ClientInfo(String purposes, String email, String name, String Img, String markerImg, String msg, String chatTime, String chatFrom, double lat, double lng,
                      Marker marker, Bitmap bitmap) {
        this.purposes = purposes;
        this.email = email;
        this.name = name;
        this.Img = Img;
        this.markerImg = markerImg;
        this.msg = msg;
        this.chatTime = chatTime;
        this.chatFrom = chatFrom;
        this.Lat = lat;
        this.Lng = lng;
        this.marker = marker;
        this.bitmap = bitmap;
    }

    // 리사이클러뷰 참여자 명단에 들어갈 닉네임, 프사(글라이드)
    public ClientInfo(String name, String Img) {
//        this.email = email;
        this.name = name;
        this.Img = Img;
    }

    public String getImg() {
        return Img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMarkerImg(String markerImg) {
        this.markerImg = markerImg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setPurposes(String purposes) {
        this.purposes = purposes;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getChatFrom() {
        return chatFrom;
    }

    public String getChatTime() {
        return chatTime;
    }

    public String getPurposes() {
        return purposes;
    }

    public String getMsg() {
        return msg;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getMarkerImg() {
        return markerImg;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "name='" + name + '\'' +
                ", Img='" + Img + '\'' +
                ", markerImg='" + markerImg + '\'' +
                ", purposes='" + purposes + '\'' +
                ", email='" + email + '\'' +
                ", msg='" + msg + '\'' +
                ", chatTime='" + chatTime + '\'' +
                ", chatFrom='" + chatFrom + '\'' +
                ", Lat=" + Lat +
                ", Lng=" + Lng +
                ", marker=" + marker +
                ", bitmap=" + bitmap +
                '}';
    }

    public String toStringOnly2() {
        return "ClientInfo{" +
                "name='" + name + '\'' +
                ", markerImg='" + markerImg + '\'' +
                '}';
    }
}

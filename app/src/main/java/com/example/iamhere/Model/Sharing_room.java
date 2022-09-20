package com.example.iamhere.Model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Sharing_room {

    @SerializedName("Room_no")
    public String Room_no; //true/false
    public String getRoom_no() {
        return Room_no;
    } //사용할 수 있게 불러내는 메소드

    @SerializedName("response")
    public String response;
    public String getResponse() {
        return response;
    }

    @SerializedName("Room_activate")
    public String activate; //true/false
    public String getActivate() {
        return activate;
    }

    @SerializedName("UserNickName")
    public String UserNickName; //true/false
    public String getUserNickName() {
        return UserNickName;
    } //사용할 수 있게 불러내는 메소드

    @SerializedName("arrLat") //앱이 종료되고 다시 들어갈 때 불러올 위도
    public String arrLat;
    public String getarrLat() {
        return arrLat;
    }

    @SerializedName("arrLng") //앱이 종료되고 다시 들어갈 때 불러올 위도
    public String arrLng;
    public String getarrLng() {
        return arrLng;
    }



}

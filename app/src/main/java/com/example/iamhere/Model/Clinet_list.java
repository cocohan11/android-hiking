package com.example.iamhere.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Clinet_list { // 나의 기록 - 같이 등산한 사람 List

    @Expose
    @SerializedName("UserNickName")
    public String ClientName;

    @Expose
    @SerializedName("UserImg")
    public String ClientImg;


    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientImg() {
        return ClientImg;
    }

    public void setClientImg(String ClientImg) {
        ClientImg = ClientImg;
    }
}

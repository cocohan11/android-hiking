package com.example.iamhere.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class getMarker {

//  [
//     {
//            "arrLat": "37.48503839980235",
//            "arrLng": "126.9683922068462"
//      },
//     {
//            "arrLat": "37.48503839980235",
//            "arrLng": "126.9683922068462"
//      }
//  ]         형태로 받아와지는 데이터를 이름으로 분류해서 배열2개로 만들기

    @Expose
    @SerializedName("arrLat")
    public double arrLat; //꼭 double타입으로 해야 에러가 안남

    @Expose
    @SerializedName("arrLng")
    public double arrLng;

    public double getArrLat() {
        return arrLat;
    }

    public double getArrLng() {
        return arrLng;
    }

    public getMarker(double arrLat, double arrLng) {
        this.arrLat = arrLat;
        this.arrLng = arrLng;
    }
}

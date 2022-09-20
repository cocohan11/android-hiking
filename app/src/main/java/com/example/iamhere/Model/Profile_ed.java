package com.example.iamhere.Model;
import com.google.gson.annotations.SerializedName;

public class Profile_ed {

    @SerializedName("response")
    public String response; //true/false

    @SerializedName("imgUrl")
    public String imgUrl; // return 값

    @SerializedName("MarkerimgUrl")
    public String MarkerimgUrl; // return 값

    @SerializedName("fileName")
    public String fileName;

    public String getFileName() {
        return fileName;
    }

    public String getResponse() {
        return response;
    }
    public String getImgUrl() {
        return imgUrl;
    }
}

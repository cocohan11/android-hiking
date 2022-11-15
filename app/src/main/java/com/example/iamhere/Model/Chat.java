package com.example.iamhere.Model;

import androidx.annotation.NonNull;

public class Chat {

    private String user_name;
    private String message;
    private String time_now;

    public Chat(String user_name, String message, String time_now) {
        this.user_name = user_name;
        this.message = message;
        this.time_now = time_now;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getTime_now() {
        return time_now;
    }

    public String getMessage() {
        return message;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}

package com.example.iamhere;
import android.app.Application;
import android.util.Log;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;

    String TAG = "GlobalApplication";


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Log.e(TAG, "onCreate()");

        // 네이티브 앱 키로 초기화
        KakaoSdk.init(this, "45268cd1f4853c061305952aadd29a90");
    }
}
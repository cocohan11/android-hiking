package com.example.iamhere.socket;
import android.Manifest;

public class Constants {

    /** 서비스에서 쓸 상수 */
//    static final int LOCATION_SERVICE_ID = 175;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1000; //런타임권한요청코드
    public static final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}; //대략,정확한 위치 권한
    public static final String ACTION_START_LOCATION_SERVICE = "startLocationService";
    public static final String ACTION_STOP_LOCATION_SERVICE = "stopLocationService";
}

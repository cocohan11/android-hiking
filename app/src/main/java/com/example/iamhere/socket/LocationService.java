package com.example.iamhere.socket;
import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.iamhere.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {

    private String TAG = "LocationService.class";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { Log.e(TAG, "onBind()");

        throw new UnsupportedOperationException("Not yet implemented");
    }

    // 실행되는 생명주기
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { Log.e(TAG, "onStartCommand()");

        if (intent != null) {
            String action = intent.getAction(); // mainActivity에서 시작버튼 클릭할 때 보내진 String값
            if (action != null) {
                if (action.equals(Constants.ACTION_START_LOCATION_SERVICE)) {
                    startLocationService();
                } else if (action.equals(Constants.ACTION_STOP_LOCATION_SERVICE)) {
                    stopLocationService();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    // 위치 업데이트 요청전에 위치 서비스에 연결하여 위치요청한다( <- ? )
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult != null && locationResult.getLastLocation() != null) {
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();
                Log.e("LOCATION_UPDATE", latitude + ", " + longitude);
            }

        }
    };


    // 위치 정기 업데이트에 필요한 알림, 시스템 옵션
    private void startLocationService() {
        String channelID = "location_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 앱 실행중 아니어도 intent가 작동
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity( // PendingIntent : 보류 인텐트. 당장 수행하진 않고 특정 시점에 수행하는 특징이 있다.
                // 보통 '앱이 구동되고 있지 않을 때' 다른 프로세스에게 권한을 허가여 intent를 마치 본인 앱에서 실행되는 것처럼 사용한다.
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT // 이미 생성된 PendingIntent 가 있다면, Extra Data 만 갈아끼움 (업데이트)
        );

        // 알림 옵션
        NotificationCompat.Builder notification = new NotificationCompat.Builder(
                getApplicationContext(),
                channelID
        );
        notification.setSmallIcon(R.drawable.hiking);
        notification.setContentTitle("Location Service");
        notification.setContentText("Running");
        notification.setAutoCancel(true);
        notification.setPriority(NotificationCompat.PRIORITY_MAX);

        // 알림 채널 만들기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null
                    && notificationManager.getNotificationChannel(channelID) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelID,
                        "Location Service..", // 사용자가 볼 수 있는 이름
                        NotificationManager.IMPORTANCE_HIGH // 전체 화면 인텐트를 사용할 수 있다.
                );
                notificationChannel.setDescription("This channel is used by location service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        // 위치 정보 변경에 대한 옵션
        LocationRequest locationRequest = new LocationRequest(); // 시스템(ex.GPS) 직접 설정X // 필요한 수준의 정확성.. 및 간격을 지정하면 기기가 시스템설정을 자동으로 적절하게 변경한다.
        locationRequest.setInterval(3000); // set the interval in which you want to get location
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // 요청의 우선순위 설정 中 가장 정확한 위치 요청

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }

        // 위치 정기 업데이트
        LocationServices.getFusedLocationProviderClient(this) // LocationServices : google play service api로 위치를 처리하려면 이 클래스가 꼭 필요함,
                // FusedLocationProviderClient : 마지막으로 확인된 위치 정보 얻기
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper()); // requestLocationUpdates : interval마다 현재 위치를 요청한다,
        // (locationRequest : 옵션, locationCallback : 콜백해줄 위치 값, 반복)
        startForeground(Constants.LOCATION_PERMISSION_REQUEST_CODE, notification.build()); // (해당알림의 고유식별정수, 알림객체)


    }

    private void stopLocationService() {
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }


}

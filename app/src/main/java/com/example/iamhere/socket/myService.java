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
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.iamhere.Model.Chat;
import com.example.iamhere.Model.ClientInfo;
import com.example.iamhere.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.naver.maps.map.NaverMap;

import static com.example.iamhere.L_login.위도;
import static com.example.iamhere.L_login.경도;
import static com.example.iamhere.M_main.URLtoBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class myService extends Service {

    // activity to service for msg.what
    private final String TAG = "myService";
    public static final int MSG_CLIENT_CONNECT = 1;
    public static final int MSG_CLIENT_DISCONNECT = 2;
    public static final int MSG_ENTRY_EXIT = 3;
    public static final int MSG_CHAT = 4;
    public static final int MSG_LOCATION = 5;
//    public static final int MSG_EXIT = 6;
    public static final int MSG_FINISH_ROOM = 7;
    public static final int MSG_START_HIKING = 8;

    // socket 통신
    static public Socket socket; //서버와 연결될 소켓
    static public BufferedReader br; // 스트림(데이터 통로)
    static public PrintWriter pw;
    private ClientSender sender;
    private final int port = 8888;
    private final String ip = "192.168.0.22"; // 2학원 ip주소
//    private final String ip = "192.168.0.155"; // 3학원 ip주소
//    private final String ip = "172.20.10.4"; // 핫스팟 ip주소
//    private final String ip = "192.168.0.15"; // 3학원 ip주소


    // 기타
    static public final int 모든위치업뎃_sec = 20;
    static public String h시간m분s초; //서버에 보낼 시간분초 문자열. 조회할 때 조작없이 바로 보여질 데이터다

    // callback arraylist
    private ArrayList<ClientInfo> clientList = new ArrayList<>(); // client명단을 담아서 Map2로 콜백한다. 거기서 UI업뎃


    Messenger mMessenger = new Messenger(new CallbackHandler());
    private LocationCallback locationCallback = new LocationCallback() {   // 위치 업데이트 요청전에 위치 서비스에 연결하여 위치요청한다
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult != null && locationResult.getLastLocation() != null) {

                위도 = locationResult.getLastLocation().getLatitude();
                경도 = locationResult.getLastLocation().getLongitude();
                Log.e("LOCATION_UPDATE", 위도 + ", " + 경도);
            }
        }
    };



    @Nullable
    @Override
    public IBinder onBind(Intent intent) { Log.e(TAG, "onBind() return mMessenger.getBinder() ");
        locationAndForeground(); // 위치업뎃 + 포그라운드 알림
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() { Log.e(TAG, "onCreate() ");
        super.onCreate();
    }

    @Override // not use
    public int onStartCommand(Intent intent, int flags, int startId) { Log.e(TAG, "onStartCommand() ");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy(){ Log.e(TAG, "onDestroy() ");
        super.onDestroy();
    }

    private class CallbackHandler extends Handler {

        @Override
        public void handleMessage( Message msg ){ Log.e(TAG, "handleMessage msg : "+msg);
            final String TAG = "CallbackHandler 서비스";
            switch( msg.what ){

                /** 입장 콜백 */
                // 서비스와 연결되면 소켓과 연결된 두 스레드가 돌아간다.
                case MSG_CLIENT_CONNECT:
                    Log.e(TAG, "ㅋ Received MSG_CLIENT_CONNECT message from client // msg.replyTo : "+msg.replyTo);
                    mMessenger = msg.replyTo; // 콜백 객체에 값 대입


                    // 위치 이유 : myRoom_no값이 null인데 여기선 값이 있어서
                    new Thread() { //error : android.os.NetworkOnMainThreadException 나기 때문에 스레드로 빼줘야함
                        public void run() { //  this name : Thread-2
                            try { Log.e(TAG, "socket Thread run ip : "+ip+" /포트 : "+port);


                                socket = new Socket(ip, port); // 전역변수로 뺐구나. 소켓생성을 왜 여기서하나했네
                                Log.e(TAG, "socket : "+socket);

                                // 발송
                                sender = new ClientSender(); // 주의) 소켓 생성 후에 sender생성자를 불러와야 함
                                sender.start(); // pw 객체 생성, while() 위치전송

                                // 수신은 thread class따로 안 만들고 여기서 thread 실행하기
                                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                ReceiveThreadRun(clientList); // 입장,채팅,위치,퇴장 시 message.send()


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }}.start();

                    Log.e(TAG, "ReceiveThreadRun() start() 직후 clientList : "+clientList);
                    break;


                case MSG_CLIENT_DISCONNECT:
                    Log.e(TAG, "ㅋㅋ Received MSG_CLIENT_DISCONNECT message from client");
                    mMessenger = null;
                    break;

            }
        }
    }

    /** 채팅 서버에서 받은 데이터를 Map2에 콜백 */
    private void ReceiveThreadRun(ArrayList<ClientInfo> clientList) {

        new Thread() { //error : android.os.NetworkOnMainThreadException 나기 때문에 스레드로 빼줘야함
            public void run() { //  this name : Thread-2


            while (br!=null) {

                try {

                    // 서버로부터 받는건 객체 1개이다. 여러번 나눠서 받으니까 번잡하다. 이게 더 간편하고 확장하기도 좋다.
                    String jsonString = br.readLine();
                    Log.e(TAG, "방 참여자 정보 jsonString : "+jsonString); // 방 참여자 정보


                    // jsonArray 인지 jsonObject 인지 모를 때
                    Object json = new JSONTokener(jsonString).nextValue();


                    /************
                     * 입장, 퇴장
                     ************/
                    if (json instanceof JSONArray) { // 여러 명에 대한 정보를 통으로 메세지로 보냄


                        JSONArray jsonArray = new JSONArray(jsonString);  // Json String -> JsonArray
                        Log.e(TAG, "이 json은 JSONArray 이다 !! jsonArray :"+jsonArray);
                        Log.e(TAG, "입장 clientList size : "+clientList.size()+"개 "+ clientList);


                        // 메세지에 jsonArray담아서 Map2로 보내기
                        Message clientList_msg = Message.obtain(null, myService.MSG_ENTRY_EXIT); // handler, what
                        clientList_msg.obj = jsonArray;

                        try {
                            Log.e(TAG, "mMessenger.send 보내지는지 확인 1 ");
                            mMessenger.send(clientList_msg); // <-- 실질 전송 메소드
                        } catch (RemoteException e) { e.printStackTrace(); }


                    /*********************************
                     * 채팅, 위치, 강제종료, 운동시작
                     *********************************/
                    } else if (json instanceof JSONObject) { // 한 사람에 대한 정보

                        // 배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
                        JSONObject jsonObject = new JSONObject(jsonString);
                        Log.e(TAG, "이 json은 JSONObject 이다 !! jsonObject : "+jsonObject);


                        String purposes = (String) jsonObject.get("purposes"); // 5가지 중 한 개
                        Log.e(TAG, "msg_5가지중한개 purposes : "+purposes);
                        Message msg_5가지중한개; // 5개 중 하나만이 이변수에 대입됨

                        switch (purposes) { // JsonObject를 통으로 안 보내고 나눈 이유 : 거기서 나눠도 되지만 여기가 나눠서 보내는게 더 깔끔하다. Map2에서는 받은것만 실행하면 됨

                            case "채팅":
                                msg_5가지중한개 = Message.obtain(null, myService.MSG_CHAT);
                                msg_5가지중한개.obj = jsonObject;
                                break;

                            case "운동시작":
                                msg_5가지중한개 = Message.obtain(null, myService.MSG_START_HIKING);
                                msg_5가지중한개.obj = "운동시작";
                                break;

                            case "위치":
                                msg_5가지중한개 = Message.obtain(null, myService.MSG_LOCATION);
                                msg_5가지중한개.obj = jsonObject;
                                break;

//                            case "퇴장":
//                                msg_5가지중한개 = Message.obtain(null, myService.MSG_EXIT);
//                                msg_5가지중한개.obj = jsonObject;
////                                socketClose_Exit(); // 소켓끊기
//                                break;

                            case "강제종료":
                                msg_5가지중한개 = Message.obtain(null, myService.MSG_FINISH_ROOM);
                                msg_5가지중한개.obj = jsonObject;
                                socketClose_Exit();
                                break;


                            default:
                                throw new IllegalStateException("Unexpected value: " + purposes); // ?
                        }

                        try {
                            Log.e(TAG, "mMessenger.send 보내지는지 확인 2");
                            mMessenger.send(msg_5가지중한개); // <-- 실질 전송 메소드
                        } catch (RemoteException e) { e.printStackTrace(); }

                    }

                } catch (JSONException | IOException e) { e.printStackTrace(); }
            }
        }}.start();
    }

    private void socketClose_Exit() { // 소켓 닫기, receive stream 닫기

        try {
            if (socket != null) {

                Log.e(TAG, "* socket.isClosed() : "+socket.isClosed());
                socket.close(); // 소켓을 close해도 객체가 null인건 아니다다
                br.close(); // sender는 어차피 일회성 객체라서 닫을 필요 없으
//                socket = null; // thread 가 멈추는게 아니라 에러남
//                br = null;

            }
        } catch (IOException e) { e.printStackTrace(); }
    }


    private void locationAndForeground() {

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
        notification.setContentTitle("등산방 참여");
        notification.setContentText("위치공유 중");
        notification.setAutoCancel(true);
        notification.setPriority(NotificationCompat.PRIORITY_MAX);

        // 알림 채널 만들기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null
                    && notificationManager.getNotificationChannel(channelID) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelID,
                        "등산방 참여", // 사용자가 볼 수 있는 이름
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
}
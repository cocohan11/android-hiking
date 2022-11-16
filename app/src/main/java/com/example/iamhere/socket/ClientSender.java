package com.example.iamhere.socket;

import static com.example.iamhere.L_login.myEmail;
import static com.example.iamhere.L_login.myImg;
import static com.example.iamhere.L_login.myMarkerImg;
import static com.example.iamhere.L_login.myName;
import static com.example.iamhere.L_login.myRoom_no;
import static com.example.iamhere.M_share_2_Map.isServiceRunning;
import static com.example.iamhere.socket.myService.pw;
import static com.example.iamhere.socket.myService.socket;
import static com.example.iamhere.L_login.경도;
import static com.example.iamhere.socket.myService.모든위치업뎃_sec;
import static com.example.iamhere.L_login.방이름;
import static com.example.iamhere.L_login.위도;
import static com.example.iamhere.M_share_2_Map.createOkHttpClient;
import static com.example.iamhere.M_share_2_Map.방퇴장처리;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.iamhere.Interface.Sharing;
import com.example.iamhere.M_share_2_Map;
import com.example.iamhere.Model.ClientInfo;
import com.example.iamhere.Model.Sharing_room;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
// 메시지를 전송하는 Thread
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
public class ClientSender extends Thread {

    String TAG = "ClientSender.class";

    // 생성자. 방장과 참여자 두 곳에서 사용 됨
    public ClientSender() {

        try {
            pw = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /***************************************** 소켓 통신 ******************************************/
    @Override
    public void run() { //  this myName : Thread-3


        Log.e(TAG, "run() socket : "+socket);
        Log.e(TAG, "변수확인 " +
                        "\nmyEmail:"+myEmail+
                        "\nmyName:"+myName+
                        "\nmyImg:"+myImg+
                        "\nmyMarkerImg:"+myMarkerImg+
                        "\nmyRoom_no:"+myRoom_no+ // null뜨네
                        "\n방이름:"+방이름+
                        "\n위도:"+위도+
                        "\n경도:"+경도
        );

        // 시작하자마자 문자열 8개를 서버로 전송 (1번만)
        if (!socket.isClosed()) { // 소켓이 열려있다면

            Log.e(TAG, "!socket.isClosed() : "+socket); // ok

            pw.println(myEmail); // 이메일로 식별하기
            pw.println(myName); // 보낸 순서대로 받기
            pw.println(myImg);
            pw.println(myMarkerImg); // 이 유저의 마커url을 채팅서버에 보내면 소켓으로 입장할 때 전송될거임
            pw.println(myRoom_no);
            pw.println(방이름);
            pw.println(위도); // 입장할 때 위경도 보내기! (기존엔 위치n초뒤에 업뎃할 때 보냈음. 따로할 이유가 없음)
            pw.println(경도);

            pw.flush();

        }


        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        // 위치 전송 (마커용)
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        while (!socket.isClosed()) { // 채팅방에서 나가도 계속 반복되어서 조건문을 소켓이 열리는경우만으로 바꿈 // 반복문이라 맨 뒤로 뺐음

            try {


                Log.e(TAG, "송신...while문"); // try를 나가면 whille문에 의해 계속 반복되어서 찍힌다

                pw.println(String.valueOf(위도) ); // 소수점 7자리네
                pw.println(String.valueOf(경도) ); // double과 string이 섞여서 서버에서 다른 타입에 담기면 아예 못 담는 문제 발생 -> string으로 변환해서 보냄
                pw.flush();


                Thread.sleep(모든위치업뎃_sec*1000); // n초마다 위치를 전송한다.
                Log.e(TAG, "송신...while 위도 : "+위도+ ", 경도 : "+경도);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } // ~while()

    }

    /*********************************************************************************************/

    private String 위경도to주소(double lat, double lng, Context context) { //long은 안되나?

        String TAG = "위경도to주소() ";
        Log.e(TAG, "lat : "+lat+" / lng : "+lng);
        String 추출된주소 = null;

        Geocoder g = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> address = g.getFromLocation(lat, lng, 10);
            String 주소 = address.get(0).getAddressLine(0);

            Log.e(TAG, "address : "+address);
            Log.e(TAG, "주소 : "+주소);

            추출된주소 = 주소추출(주소); // 원하는 주소만 추출 >> 서울특별시 동작구 사당4동

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "입출력오류");
        }
        return 추출된주소;
    }

    private String 주소추출(String 주소) {

        //대한민국 서울특별시 동작구 사당4동 300-73
        // >> 서울특별시 동작구 사당4동
        //대한민국 전라남도 장흥군 용산면 덕암리 산10
        // >> 전라남도 장흥군 용산면

        String target동 = "동 "; // 동 인곳을 타겟으로 삼아서
        String target면 = "면 "; //지방의 경우 군/면/리
        int target_num동 = 주소.indexOf(target동); //인덱스를 알아낸다.
        int target_num면 = 주소.indexOf(target면);


        String 리턴받는수정주소; //추출한 주소를 이 변수에 담아 리턴할 것임

        if(target_num동 != -1) {//"동"이라면

            리턴받는수정주소 = 주소.substring(5, target_num동+1); //서울 ~ 동까지 추출
            return 리턴받는수정주소;

        } else if(target_num면 != -1) { //"면"이라면

            리턴받는수정주소 = 주소.substring(5, target_num면+1);//면으로 편집한 주소
            return 리턴받는수정주소;

        } else { //동도 면도 아니라면

            return 주소; //else로 나머지 다 때려넣어야지. 여기때문에 에러났음
        }

    }






} // ~inner class(송신)

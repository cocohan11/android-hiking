package com.example.iamhere;

import static com.example.iamhere.L_login.myEmail;
import static com.example.iamhere.L_login.myName;
import static com.example.iamhere.L_login.myRoom_no;
import static com.example.iamhere.M_share_2_Map.retrofit객체;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.iamhere.Interface.Sharing;
import com.example.iamhere.Model.ClientInfo;
import com.example.iamhere.Model.Clinet_list;
import com.example.iamhere.Model.Sharing_room;
import com.example.iamhere.Model.re_sharingRoom;
import com.example.iamhere.Recyclerview.sharingList_Adapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class R_record_sharing_show_list extends AppCompatActivity {


    String TAG = "R_record_sharing_show_list";
    String 방번호;
    RecyclerView rv_record_list;
    Button btn_record_list_ok;
    sharingList_Adapter adapter;
    ArrayList<ClientInfo> clientList = new ArrayList<>();


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rrecord_sharing_show_list);
        ID();


        Intent intent = getIntent(); // R_record_sharing 리사이클러뷰 사진요소 >>> (방번호) >>> this
        방번호 = intent.getStringExtra("해당방번호");
        Log.e(TAG, "방번호 : "+방번호);


        retrofit명단받기(방번호); // 방번호 주고 명단 받는다.



        // 확인 버튼 누르면 액티비티 종료되서 원래 '나의 목록'으로 돌아간다
        btn_record_list_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish(); // 액티비티 종료
            }
        });

    } // ~onCreate()



    // retrofit으로 명단 array 받아오기
    private void retrofit명단받기(String 방번호) { Log.e(TAG, "retrofit명단받기() - 방번호 : "+방번호);


        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        // retrofit 통신
        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        Sharing rv_imgAndName = retrofit객체().create(Sharing.class);   // Sharing라는 interface에서 recyclerviewRecord()메소드로 보낸다.
        Log.e(TAG, "retrofit명단받기() - rv_imgAndName : "+rv_imgAndName);
        Call<ArrayList<Clinet_list>> call = rv_imgAndName.recyclerviewRecordGetList(방번호); // Call<ArrayList<re_sharingRoom>> : 데이터를 받아오는 곳
        Log.e(TAG, "retrofit명단받기() - call : "+call);


        //네트워킹 시도
        call.enqueue(new Callback<ArrayList<Clinet_list>>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged", "ClickableViewAccessibility"})
            @Override
            public void onResponse(Call<ArrayList<Clinet_list>> call, @NonNull Response<ArrayList<Clinet_list>> response) {


                Log.e(TAG, "response : "+response);
                Log.e(TAG, "response.body()..? : "+response.body());


                //ㅡㅡㅡㅡㅡㅡㅡㅡ
                // 응답받은 배열
                //ㅡㅡㅡㅡㅡㅡㅡㅡ
                ArrayList<Clinet_list> response_lists = response.body(); //대입하는 순간 SerializedName에 따라 이름따라 값이 들어가짐 !! 중요


                // 대입하는 이유 : 어댑터가 ClientInfo 타입이라서 변환해주기
                // ClinetInfo로 애초에 안 받는 이유 : 모델이 아니면.. 필드에서 몇 개만 serializedName하니까 에러남

                for (int i=0; i<response_lists.size(); i++) {

                    // Clinet_list 타입으로 받아와서
                    String responName = response_lists.get(i).getClientName();
                    String responImg = response_lists.get(i).getClientImg();
                    Log.e(TAG, "responName : "+responName+"/ responImg : "+responImg);


                    // Arraylist<ClientInfo> 타입으로 만들기
                    ClientInfo clientInfo = new ClientInfo(responName,responImg);
                    clientList.add(clientInfo);
                    Log.e(TAG, "clientList.size : "+clientList.size()+"\n clientList : "+clientList);

                }

//                ClientInfo clientInfo = new ClientInfo("test email","test name","img아직");
//                clientList.add(clientInfo);


                // 리사이클러뷰 적용
                adapter = new sharingList_Adapter(getApplicationContext(), clientList, myName);
                LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                rv_record_list.setLayoutManager(layoutManager2); //보이는 형식, 아래로 추가됨
                rv_record_list.setAdapter(adapter); //최종모습의 recyclerView에 어댑터를 장착
                adapter.notifyDataSetChanged(); //전체변경 업데이트



            }

            @Override
            public void onFailure(Call<ArrayList<Clinet_list>> call, Throwable t) {
                Log.e("onFailure : ", t.getMessage());
            }
        });

    }


    private void ID() {

        rv_record_list = findViewById(R.id.rv_record_list2);
        btn_record_list_ok = findViewById(R.id.btn_record_list_ok);

    }


}
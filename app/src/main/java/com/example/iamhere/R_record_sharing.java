package com.example.iamhere;

import static com.example.iamhere.L_login.bitmapCapture;
import static com.example.iamhere.L_login.myEmail;
import static com.example.iamhere.M_share_2_Map.retrofit객체;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.iamhere.Interface.Sharing;
import com.example.iamhere.Model.re_sharingRoom;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class R_record_sharing extends AppCompatActivity {


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    // 참여했던 방을 리사이클러뷰로 보여준다.
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    String TAG = "R_record_sharing";
    RecyclerView 위치방recyclerView;
    CustomerAdapter adapter;
    TextView tv_notFound; // "조회된 기록이 없습니다" 숨김/보임
    ArrayList<re_sharingRoom> items = new ArrayList<>(); //리사이클러뷰 요소
    Spinner spinner, spinner2; //년도, 월
    String 해당년도, 해당월; //현재시점에서 몇년도, 몇월인지 값이 대입 >> retrofit에 들어갈 전역변수
    boolean 초기아님 = false; //년도 선택시 "선택"으로 변경하려는데 시작하자마자 클릭>변경되어서 제약검
    ArrayList<String> 년도 = new ArrayList<>();
    ArrayList<String> 월 = new ArrayList<>();
    ItemTouchHelper helper; //swipe해주는 역할


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rrecord_sharing);
        Log.e(TAG, "onCreate()");
        Log.e(TAG, "bitmapCapture : "+ bitmapCapture);
        ID();
        스피너초기셋팅(); // 1~12월 스피너 셋팅


        setSpinner년도(년도, spinner); //스피너1 : [2022년, 2021년]
        setSpinner월(월, spinner2); //스피너2 : [1월, 2월...12월] --> 클릭이벤트로 retrofit통신
        스피너_선택안되게스레드(); //시작하자마자 "선택"이 눌려져서 2초뒤에 년도클릭이벤트가 시작되게끔 함


        //ㅡㅡㅡㅡㅡㅡㅡ
        // 리사이클러뷰 : retrofit으로 받아온 array를 리사이클러뷰에 뿌림
        //ㅡㅡㅡㅡㅡㅡㅡ
        // 1.레이아웃 매니저
        // 2. 데이터가 담긴 Array
        // 3. Array가 장착된 adpater
        // 4. adapter를 장착한 리사이클러뷰


        Log.e(TAG, "retrofit위치공유참여기록() 직전 ");
        retrofit위치공유참여기록(); //액티비티 시작하자마자 해당 월에 대한 기록을 조회한다. 클릭리스너로도 조회한다.



    } //~onCreate()


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    //스피너(월) 1~12월 셋팅
    private void 스피너초기셋팅() {

        년도.add("2022년");
        년도.add("2021년");
        월.add("선택");
        for (int i=1; i<=12; i++) {
            월.add(i+"월"); //1~12월
        }

    }

    
    // 지나온 달만 보여주기 (7월이면 1~7월까지만 보여주기), 다른년도면 1~12월 복구
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void 스피너월변경(int i) {

        int 해당월숫자 = 0;
        if(!해당월.equals("선택")) { //선택이 아니라면 숫자로 형변환

            해당월숫자 = Integer.parseInt(해당월); //스피너로 선택한 월의 숫자
        }
        Log.e(TAG, "i 해당월숫자 현재몇월인지 : "+i+해당월숫자+현재몇월인지());

        if(해당년도.equals("2022")) { //올해인경우 미래 월 안보이게 하기

            Log.e(TAG, "미래 안 보이게 하기");
            Log.e(TAG, "월 : "+월.get(4));

            월.clear();
            월.add("선택");

            for (int j=1; j<=현재몇월인지(); j++) { // 7월까지만 보이게하기
                월.add(j+"월"); //
            }

            if(해당월숫자 > 현재몇월인지()) { // 11 > 7 이라면 선택은 되지만 스피너상태를 변경한다.

                spinner2.setSelection(0); //"선택"으로 변경
                Log.e(TAG, "선택한 월이 더 클 때 선택으로 변경 ");

            }

        } else { //2022년도가 아닐 때

            월.clear();
            월.add("선택");
            for (int j=1; j<=12; j++) {
                월.add(j+"월"); //1~12월
            }

        }

    }

    private void 스피너_선택안되게스레드() {

        Log.e(TAG, "스피너_선택안되게스레드() 입장");

        new Thread(new Runnable() {
            public void run() {
                try {

                    Log.e(TAG, "sleep 2000");
                    Thread.sleep(2000);
                    초기아님 = true; //처음 통신까지 하고 뿌리고나면 초기가 아니다. 이제 년도를 바꿀 때 "선택"으로 바뀌어도 상관없다.
                    Log.e(TAG, "초기아님2 : "+초기아님);

                }catch(Exception e) {
                    Log.e(TAG, "스피너_선택안되게스레드() 에러");
                }
            }
        }).start();

    }

    // 현재 몇월인지 숫자리턴
    @RequiresApi(api = Build.VERSION_CODES.N)
    private int 현재몇월인지() {

        // 현재 날짜/시간
        Date now = new Date(); // "Thu Jun 17 06:57:32 KST 2021"

        // 날짜 형태를 바꾸기
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("M");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // 한국시간으로 변경
        String 해당월 = sdf.format(now); // Date -> String
        int 해당월숫자 = Integer.parseInt(해당월);

        Log.e(TAG, "해당월 : "+해당월); // "7"

        return 해당월숫자;

    }

    // 스피너로 해당 년도에 참여한 위치공유방을 조회한다.
    private void setSpinner년도(ArrayList<String> 년도, Spinner spinner) { // R.array.년도 --> 만들어둔 리소스

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                년도
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //기본 레이아웃
        spinner.setAdapter(adapter);
        spinner.setSelection(0); // 2022년도로 시작

        Log.e(TAG, "setSpinner년도() spinner.getSelectedItem() : "+spinner.getSelectedItem());
        해당년도 = String.valueOf(spinner.getSelectedItem()); //클릭보다 먼저 값이 존재
        해당년도 = 해당년도.replace("년", ""); //"년"제거


        //스피너(년도) 선택 이벤트
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                해당년도 = adapterView.getItemAtPosition(i).toString();
                해당년도 = 해당년도.replace("년", ""); //"년"제거
                Log.e(TAG, "년도~~ 클릭" +
                        "\n해당년도 : "+해당년도+
                        "\n년도 : "+년도+
                        "\n해당월 : "+해당월
                );  //"2022"
                Log.e(TAG, "초기아님1 : "+초기아님);


                if(!해당월.equals("선택") && 초기아님) { //액티비티 생성 후 2초 뒤에 들어와짐

                    retrofit위치공유참여기록(); //통신해서 뿌린다.
                    Log.e(TAG, "선택으로 바꿈");

                }

                스피너월변경(i); //지나온 달만 보여주기 (7월이면 1~7월까지만 보여주기), 다른년도면 1~12월 복구

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e(TAG, "setSpinner년도() onNothingSelected ");
            }
        });

    }


    // 스피너로 해당 월에 참여한 위치공유방을 조회한다.
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setSpinner월(ArrayList<String>  월, Spinner spinner) { // R.array.월 --> 만들어둔 리소스

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item, //스피너 컨트롤에 나타나는 방식을 정의하는 레이아웃 소스
                월
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //기본 레이아웃
        spinner.setAdapter(adapter);
        spinner.setSelection(현재몇월인지()); //0인덱스가 '선택'이라서 개월수때문에 -1안해도 됨


        Log.e(TAG, "setSpinner월() spinner.getSelectedItem() : "+spinner.getSelectedItem());
        해당월 = String.valueOf(spinner.getSelectedItem()); //클릭보다 먼저 값이 존재  //"7월"
        해당월 = 해당월.replace("월", ""); //"월"제거


        //스피너(월) 선택 이벤트
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                해당월 = adapterView.getItemAtPosition(i).toString(); //retrofit으로 보내고 나서 생기는 이벤트 //null
                해당월 = 해당월.replace("월", ""); //"월"제거
                Log.e(TAG, "월~~ 클릭" +
                        "\n해당년도 : "+해당년도+
                        "\n월 : "+월+
                        "\n해당월 : "+해당월
                );  //"2022"

                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                // 해당 년도,월 조회 : retrofit 통신, 리사이클러뷰에 장착
                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                if(!해당월.equals("선택") && 초기아님) { //해당월이 "선택"이 아닌경우, 처음통신할 때 제외한 클릭이벤트만

                    Log.e(TAG, "해당 년월에 해당하는 기록을 주이소");
                    retrofit위치공유참여기록(); //통신해서 뿌린다.

                }

                //ㅡㅡㅡㅡㅡㅡㅡㅡ
                // 미래 날짜 선택 : 스피너2 "선택"로 변경, 화면조회x
                //ㅡㅡㅡㅡㅡㅡㅡㅡ
                Log.e(TAG, "i>현재몇월인지() : "+i+현재몇월인지());
                if(i>현재몇월인지()) { //만약 미래를 선택했으면 "선택"으로 남김

                    Log.e(TAG, "i>현재몇월인지() 입장");
                    위치방recyclerView.setVisibility(View.GONE);
                    tv_notFound.setVisibility(View.VISIBLE);

                }

                스피너월변경(i); //지나온 달만 보여주기 (7월이면 1~7월까지만 보여주기), 다른년도면 1~12월 복구

/////////


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e(TAG, "setSpinner월() onNothingSelected ");
            }
        });

    }


    //내가 참여했던 위치공유방 기록을 조회한다. (시작버튼을 누르고 퇴장한 이후 기록만 조회)
    private void retrofit위치공유참여기록() {

        //보냄 : 내 이메일(->방번호), (액티브0이어야겠지)
        //과정 : 내 이메일로 RoomUser를 조회, 방번호를 찾아냄. 해당 방 번호로 Room의 정보들 get... 그러곤 어떤 데이터 형태로 만들어서 보내지?
        //받음 : 썸넬주소, 방이름, 주소, 시작~끝시간, 소요시간, 인원

        Log.e(TAG, "retrofit위치공유참여기록() 보낼 변수 " +
                "\nmyEmail : "+myEmail+
                "\n해당년도 : "+해당년도+
                "\n해당월 : "+해당월
        );


        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        // retrofit 통신
        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        Sharing 리사이클러뷰data = retrofit객체().create(Sharing.class);   // Sharing라는 interface에서 recyclerviewRecord()메소드로 보낸다.
        Call<ArrayList<re_sharingRoom>> call = 리사이클러뷰data.recyclerviewRecord(myEmail, 해당년도,해당월); // Call<ArrayList<re_sharingRoom>> : 데이터를 받아오는 곳


        //네트워킹 시도
        call.enqueue(new Callback<ArrayList<re_sharingRoom>>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged", "ClickableViewAccessibility"})
            @Override
            public void onResponse(Call<ArrayList<re_sharingRoom>> call, @NonNull Response<ArrayList<re_sharingRoom>> response) {


                Log.e(TAG, "성공..? response : "+response);
                Log.e(TAG, "response.body()..? : "+response.body());
                Log.e(TAG, "get(0).getRoom_name()..? : "+response.body().get(0).getRoom_name()); //ㄹㄹㄷ
                Log.e(TAG, "get(0).getRoom_mapCapture()..? : "+response.body().get(0).getRoom_mapCapture()); //


                //ㅡㅡㅡㅡㅡㅡㅡㅡ
                // 응답받은 배열
                //ㅡㅡㅡㅡㅡㅡㅡㅡ
                items = response.body(); //대입하는 순간 SerializedName에 따라 이름따라 값이 들어가짐 !! 중요

                Log.e(TAG, "items..? : "+items.get(0).toString()); //com.example.iamhere.Model.re_sharingRoom@4f279e7
                Log.e(TAG, "items 성공..? : "+items); // [com.example.iamhere.Model.re_sharingRoom@6cf2770, com.example.iamhere.Model.re_sharingRoom@4d81ae9, com.example.iamhere.Model.re_sharingRoom@16ca36e, com.example.iamhere.Model.re_sharingRoom@a214a0f]


                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                // 리사이클러뷰 어댑터 장착
                //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                위치방recyclerView.setLayoutManager(layoutManager); //보이는 형식, 아래로 추가됨

                adapter = new CustomerAdapter(getApplicationContext(), R_record_sharing.this, items); //생성자에 arraylist 자체를 넣을 수도 있고, adapter안에 존재하는 arr에 추가할 수도 있네.
                adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() { //tv_notFound를 커스덤어댑터에서 수정은 되지만 UI업데이트가 안되는 에러가 있었음. 그래서 메인스레드에서 조작하기로 함


                    @Override //어댑터 관찰 : 너..삭제했네?
                    public void onItemRangeRemoved(int positionStart, int itemCount) { //드디어 찾아낸 리스너... 삭제하면 여기로 온다
                        super.onItemRangeRemoved(positionStart, itemCount);

                        Log.e(TAG, "onItemRangeRemoved() adapter.items.size() : "+adapter.items.size());
                        if (adapter.items.size() == 0) {

                                tv_notFound.setVisibility(View.VISIBLE); //"조회된 기록이 없습니다." 보여주기(항상 보여주면 자리이동할 때 슬쩍 보임)

                        }
                    }
                });


                위치방recyclerView.setAdapter(adapter); //최종모습의 recyclerView에 어댑터를 장착

                helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter)); //애니메이션 삭제 인터페이스
                helper.attachToRecyclerView(위치방recyclerView); //RecyclerView에 ItemTouchHelper 붙이기

                adapter.notifyDataSetChanged(); //전체변경 업데이트


                //리사이클러뷰 보이기 숨기기
                if(response.body().get(0).getRoom_mapCapture().equals("없음")) { //조회된게 없으면 숨기기

                    위치방recyclerView.setVisibility(View.GONE);
                    tv_notFound.setVisibility(View.VISIBLE);
                    Log.e(TAG, "조회된게 없으면 rv 숨기기");

                } else {

                    Log.e(TAG, "조회된게 있으면 rv 보이기");
                    위치방recyclerView.setVisibility(View.VISIBLE);
                    tv_notFound.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<ArrayList<re_sharingRoom>> call, Throwable t) {
                Log.e("onFailure : ", t.getMessage());
            }
        });

    }


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    //                                           생명주기
    // (필수 : 네이버 지도 api에서 Mapview를 사용하려면 아래 생명주기를 생성하라 함. 이유는 말 안 함)
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");

    }

    @Override
    protected void onRestart() { //다른액티비티 back하면 여기로 옴
        super.onRestart();
        Log.e(TAG, "onRestart ~~~~");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

    private void ID() {

        위치방recyclerView = (RecyclerView) findViewById(R.id.recyclerview3);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        tv_notFound = (TextView) findViewById(R.id.tv_notFound);

    }

}
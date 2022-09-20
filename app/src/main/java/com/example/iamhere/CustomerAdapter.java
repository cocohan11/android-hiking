package com.example.iamhere;


import static com.example.iamhere.L_login.myEmail;
import static com.example.iamhere.L_login.myName;
import static com.example.iamhere.M_share_2_Map.retrofit객체;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iamhere.Interface.ItemTouchHelperListener;
import com.example.iamhere.Interface.Sharing;
import com.example.iamhere.Model.ClientInfo;
import com.example.iamhere.Model.Sharing_room;
import com.example.iamhere.Model.re_sharingRoom;
import com.example.iamhere.Recyclerview.sharingList_Adapter;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> implements ItemTouchHelperListener {

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    // 리사이클러뷰에 장착할 어댑터. ArrayList와 레이아웃을 어디자리에 연결할지 '정한다.'
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    //실제 데이터는 리사이클러뷰에 어댑터를 장착할 때 실제 값이 담긴 arraylist를 add하면 됨


    String TAG = "커스덤어댑터";
    Context context, activity;
    ArrayList<re_sharingRoom> items = new ArrayList<re_sharingRoom>(); //arraylist안에 생성자로 만든 n개의 데이터가 들어있다.
    //해당 인덱스안에 0:[김김김, 1234] 1:[박박박,4345]...이런식을 들어있는거임
    private View tv_notFound;


    //생성자 홒출 시 Context를 넘겨준다??
    public CustomerAdapter(Context context, Context activity, ArrayList<re_sharingRoom> items ){ //다른 예시에서는 arr를 파라미터로 넣던데...음... 메인가서 보자 뭘 넣는지
        this.context = context;
        this.activity = activity;
        this.items = items;
        Log.e("생성자 context ", String.valueOf(context));

    }


    //ㅡㅡㅡㅡ
    // 순서 1 : 내가 만든 한 칸의 레이아웃을 메모리에 올려 객체화함
    //ㅡㅡㅡㅡ
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //inflate시켜주는 도구
        View itemView = inflater.inflate(R.layout.re_sharing_room, parent, false); //내가 만든 한 칸 뷰를 객체화함
        Log.e("홀더 context ", String.valueOf(context));

        return new ViewHolder(itemView); //이제 re_sharing_room 레이아웃을 손댈 수 있게 됨
    }


    @Override
    public void setAdapter(CustomerAdapter adapter) {

    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        return false;
    }

    @SuppressLint("LongLogTag")
    @Override //삭제
    public void onItemSwipe(int position) {
        Log.e("커스덤어댑터 onItemSwipe()... i : ", String.valueOf(position));

    }

    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {

        retrofit삭제요청(items.get(position).getRoom_no(), myEmail, items.get(position).getRoom_name(), viewHolder); //서버에 삭제요청(실데이터 삭제x 조회만안되게 함)
        //위치!! 디비에 먼저 요청해야 함. 안 그러면 순서가 맞지 않음. 이미 당겨져 있음
        items.remove(position); //리사이클러뷰에 장착된 배열에서 아이템 삭제
        notifyItemRemoved(position); //삭제했으니 갱신
//        TextView tv_notFound2 = viewHolder.itemView.findViewById(R.id.tv_notFound2); // "조회된기록이 없다"는 뷰 삽입
//        CardView CardView = viewHolder.itemView.findViewById(R.id.CardView); //뷰홀더 하나전체
//        Log.e(TAG, "tv_notFound2 : "+tv_notFound2);
//        tv_notFound2.setVisibility(View.VISIBLE);
//        CardView.setVisibility(View.GONE);
//        Log.e(TAG, "tv_notFound2 ...VISIBLE, CardView...GONE");

    }

    //ㅡㅡㅡㅡ
    // 순서 2 : 뷰 하나하나 객체화
    //ㅡㅡㅡㅡ
    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_mapCapture; //경로마커지도 캡쳐한게 들어갈 곳
        TextView tv_roomName; //방제
        TextView tv_address; //주소
        TextView tv_startEndTime; //시작시간 ~ 끝시간
        TextView tv_sharingTime; //소요시간
        Button btn_showList; //참여자수
        protected RecyclerView recyclerView;


        //순서 1에서 뷰홀더를 리턴받았음 이제 해당 레이아웃에 손 댈 수 있게 되어서 find해주고 선언해서 사용하면 된다.
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_mapCapture = itemView.findViewById(R.id.iv_mapCapture);
            tv_roomName = itemView.findViewById(R.id.tv_roomName);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_startEndTime = itemView.findViewById(R.id.tv_startEndTime);
            tv_sharingTime = itemView.findViewById(R.id.tv_sharingTime);
            btn_showList = itemView.findViewById(R.id.btn_showList);

        }

    }

    //ㅡㅡㅡㅡ
    // 순서 3 : 뷰홀더에 들어갈 데이터만 바꿔준다.
    //ㅡㅡㅡㅡ
    //데이터가 들어간 뷰를 매번 인플레이트하고 새로 만드는게 아니라, 뷰를 그대로 쓰고(holder) arraylist에 있던 데이터만 set해준다.
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //사진 클릭 : 지도열림(마커찍힘)
        re_sharingRoom item = items.get(position); //arr의 인덱스에 해당하는 객체를 가져와서


        Glide.with(context).load(item.Room_mapCapture).into(holder.iv_mapCapture);
        holder.iv_mapCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("지도 클릭", "holder.iv_mapCapture.setOnClickListener");
                Intent intent = new Intent(view.getContext(), R_record_sharing_mapMark.class);
                Log.e("item.Room_no :", item.Room_no);
                intent.putExtra("해당방번호", item.Room_no); //방번호로 마커불러와라
                Log.e("view.getContext() ", String.valueOf(view.getContext())); //com.example.iamhere.GlobalApplication@862a246
                Log.e("context ", String.valueOf(context)); //com.example.iamhere.GlobalApplication@862a246
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); // 에러 : 활동 컨텍스트 외부에서 startActivity()를 호출하려면 FLAG_ACTivity_NEW_TASK 플래그가 필요합니다.
                                                                                       // 해결 : 플래그 추가가
            }
        });


        holder.btn_showList.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ObsoleteSdkInt")
            @Override
            public void onClick(View v) {

                Log.e("btn_showList 클릭", "holder.iv_mapCapture.setOnClickListener");
                Intent intent = new Intent(v.getContext(), R_record_sharing_show_list.class);
                Log.e("item.Room_no :", item.Room_no);
                intent.putExtra("해당방번호", item.Room_no); //방번호로 마커불러와라
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); // 에러 : 활동 컨텍스트 외부에서 startActivity()를 호출하려면 FLAG_ACTivity_NEW_TASK 플래그가 필요합니다.

                // 다이얼로그로 리사이클러뷰 띄우기
//                Log.e("btn_showList버튼 클릭", "리사이클러뷰 띄우기");
//                Dialog dialog_record_list = new Dialog(activity); // 주의! activity를 넣어야 함!!
//                dialog_record_list.requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 제거
//                dialog_record_list.setContentView(R.layout.dialog_record_list); //보여줄 xml레이아웃과 연결
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    dialog_record_list.create();
//                }
//                dialog_record_list.show(); //다이얼로그 띄우기


                // 레트로핏으로 명단 받아오기기
//                ArrayList<ClientInfo> clientList = new ArrayList<>();
//                ClientInfo client1 = new ClientInfo("test email","test name","img 아직");
//                clientList.add(client1);
//                ClientInfo client2 = new ClientInfo("test email","test name","img 아직");
//                clientList.add(client2);


                // 리사이클러뷰 안에 리사이클러뷰
//                sharingList_Adapter adapter = new sharingList_Adapter(v.getContext(), clientList, "방장이름름");
//
//                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //inflate시켜주는 도구
//                View itemView = inflater.inflate(R.layout.re_sharing_room, parent, false); //내가 만든 한 칸 뷰를 객체화함
//
//                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
//                rv_chat.setLayoutManager(layoutManager); //보이는 형식, 아래로 추가됨
//                rv_chat.setAdapter(chat_adapter); //최종모습의 recyclerView에 어댑터를 장착

            }
        });

        holder.tv_roomName.setText("방이름 : "+item.getRoom_name());
        holder.tv_address.setText(item.getRoom_address());
        holder.tv_startEndTime.setText(item.getRoom_startEndTime());
        holder.tv_sharingTime.setText("소요시간 : "+item.getRoom_leadTime());
        holder.btn_showList.setText("참여자 ("+item.getPeopleNum()+"명)");


    } //arraylist 인덱스 == 리사이클러뷰 position 이기 때문에 arr객체에 담긴 데이터를 삽입



    //아이템 수 반환
    @Override
    public int getItemCount() {
        return items.size();
    }


    // 메인에서 addItem()을 해서 arr에 추가하면 뷰바인더에서 arr에 있는 데이터를 가져와서 홀더에 셋해준다.
    public void addItem(re_sharingRoom item){
        items.add(item);
    }


    public void retrofit삭제요청(String 방번호, String 이메일, String 방이름, RecyclerView.ViewHolder viewHolder) {

        //보낼 값 : 방번호, 이메일
        //요청 : User_Delete_record = 1
        //응답 : 성공
        Log.e(TAG, "retrofit삭제요청() " +
                "\n변수 확인 "+
                "\n방번호 : "+방번호+
                "\n이메일 : "+이메일+
                "\n방이름 : "+방이름 //확인용 파라미터
                );


        Sharing 리사이클러뷰data = retrofit객체().create(Sharing.class);
        Call<Sharing_room> call = 리사이클러뷰data.recyclerviewDelete(방번호, 이메일);

        //네트워킹 시도
        call.enqueue(new Callback<Sharing_room>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged", "ClickableViewAccessibility"})
            @Override
            public void onResponse(Call<Sharing_room> call, @NonNull Response<Sharing_room> response) {

                Log.e(TAG, "성공..? response : "+response);
                Log.e(TAG, "response.body()..? : "+response.body());
                Log.e("items.size :", String.valueOf(items.size()));
                boolean isRun = true; //스레드 멈추기용도 - 화면전환시


                //전부 다 삭제하고 화면 바꾸기 위한 기능
                if(items.size() == 0) {

//                    //
//                    //
//                    // 무언가
//                    Log.e(TAG, "items.size() == 0");
//
//                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //inflate시켜주는 도구
//                    @SuppressLint("InflateParams") View itemView = inflater.inflate(R.layout.activity_rrecord_sharing, null); //내가 만든 한 칸 뷰를 객체화함
//                    tv_notFound = itemView.findViewById(R.id.tv_notFound);
//
//                    Log.e(TAG, "tv_notFound.getText() : "+tv_notFound.getText());
//                    Handler handler2 = new Handler(); //스레드 도중 UI객체에 손대기 위해 핸들러 부름
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            handler2.post(new Runnable() { //post : 다른 스레드로 메세지(객체)를 전달하는 함수
//                                @Override
//                                public void run() { //마커의 위치만 변경
//
//                                    tv_notFound.setText("하하하하하");
//                                    Log.e(TAG, "tv_notFound : "+tv_notFound);
//                                    Log.e(TAG, "tv_notFound.getText() : "+tv_notFound.getText());
//
//                                }
//                            });
//                        }
//                    }).start(); //start()붙이면 바로실행시킨다.

                } else {
                    Log.e(TAG, "items.size() != 0");

                }

            }

            @Override
            public void onFailure(Call<Sharing_room> call, Throwable t) {
                Log.e("onFailure : ", t.getMessage());
            }
        });

    }

}
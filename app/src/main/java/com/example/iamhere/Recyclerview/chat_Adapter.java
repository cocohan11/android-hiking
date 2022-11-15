package com.example.iamhere.Recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iamhere.Model.Chat;
import com.example.iamhere.Model.ClientInfo;
import com.example.iamhere.R;

import java.util.ArrayList;

public class chat_Adapter extends RecyclerView.Adapter<chat_Adapter.ViewHolder> {

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    // 리사이클러뷰에 장착할 어댑터. ArrayList와 레이아웃을 어디자리에 연결할지 '정한다.'
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    //실제 데이터는 리사이클러뷰에 어댑터를 장착할 때 실제 값이 담긴 arraylist를 add하면 됨


    String TAG = "커스덤어댑터";
    Context context;
    ArrayList<Chat> items; //arraylist안에 생성자로 만든 n개의 데이터가 들어있다.
    //해당 인덱스안에 0:[김김김, 1234] 1:[박박박,4345]...이런식을 들어있는거임
    ArrayList<ClientInfo> clientList; // 명단
//    String 방장이름; // 방장이면 글자색상을 변경해주기위해


    //생성자 홒출 시 Context를 넘겨준다??
    public chat_Adapter(Context context, ArrayList<Chat> items, String 방장이름, ArrayList<ClientInfo> clientList){ //다른 예시에서는 arr를 파라미터로 넣던데...음... 메인가서 보자 뭘 넣는지
        this.context = context;
        this.items = items;
        this.clientList = clientList;
        Log.e("생성자 context ", String.valueOf(context));
        Log.e("생성자 clientList : ", String.valueOf(clientList));
        Log.e("생성자 방장이름 : ", 방장이름);
    }


    //ㅡㅡㅡㅡ
    // 순서 1 : 내가 만든 한 칸의 레이아웃을 메모리에 올려 객체화함
    //ㅡㅡㅡㅡ
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //inflate시켜주는 도구
        View itemView = inflater.inflate(R.layout.re_chat_row, parent, false); //내가 만든 한 칸 뷰를 객체화함
        Log.e("홀더 context ", String.valueOf(context));

        return new ViewHolder(itemView); //이제 re_sharing_room 레이아웃을 손댈 수 있게 됨
    }


    //ㅡㅡㅡㅡ
    // 순서 2 : 뷰 하나하나 객체화
    //ㅡㅡㅡㅡ
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_headText; //이름
        TextView tv_message; //채팅 메세지
        TextView tv_time_now; //시간


        //순서 1에서 뷰홀더를 리턴받았음 이제 해당 레이아웃에 손 댈 수 있게 되어서 find해주고 선언해서 사용하면 된다.
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_headText = itemView.findViewById(R.id.tv_headText);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_time_now = itemView.findViewById(R.id.textView21);

        }

    }

    //ㅡㅡㅡㅡ
    // 순서 3 : 뷰홀더에 들어갈 데이터만 바꿔준다.
    //ㅡㅡㅡㅡ
    //데이터가 들어간 뷰를 매번 인플레이트하고 새로 만드는게 아니라, 뷰를 그대로 쓰고(holder) arraylist에 있던 데이터만 set해준다.
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        Log.e(TAG, "onBindViewHolder position : "+position);
        Chat item = items.get(position); //arr의 인덱스에 해당하는 객체를 가져와서

        holder.tv_headText.setText(item.getUser_name());
        holder.tv_message.setText(item.getMessage());
        holder.tv_time_now.setText(item.getTime_now());


        String ChatFrom = String.valueOf(holder.tv_headText.getText());
        Log.e(TAG, "제일 앞서서 들어온 사람이 방장이다. ChatFrom : "+ChatFrom);
        Log.e(TAG, "clientList : "+clientList);


        // 제일 앞서서 들어온 사람이 방장이 된다. (방장이 나가면 그 다음사람이 방장이 된다.)
        if (clientList.get(0).getName().equals(ChatFrom)) {

            Log.e(TAG, "방장이다. true");
            holder.tv_message.setTextColor(Color.parseColor("#FF8535"));

        } else {

            Log.e(TAG, "방장이다. false");
            holder.tv_message.setTextColor(Color.parseColor("#3A3A3A"));

        }

//        if (방장이름.equals(방참여자이름)) { // 방장이면 메세지색상 주황
//
////            Log.e(TAG, "true 방참여자이름 : "+방참여자이름);
//            holder.tv_message.setTextColor(Color.parseColor("#FF8535"));
//
//        } else { // 안내 메세지, 참여자 메세지
//            holder.tv_message.setTextColor(Color.parseColor("#3A3A3A"));
//        }

    } //arraylist 인덱스 == 리사이클러뷰 position 이기 때문에 arr객체에 담긴 데이터를 삽입



    //아이템 수 반환
    @Override
    public int getItemCount() {
        return items.size();
    }


    // 메인에서 addItem()을 해서 arr에 추가하면 뷰바인더에서 arr에 있는 데이터를 가져와서 홀더에 셋해준다.
    public void addItem(Chat item){
        items.add(item);
    }

}
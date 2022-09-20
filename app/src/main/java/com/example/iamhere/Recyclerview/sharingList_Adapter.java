package com.example.iamhere.Recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iamhere.Model.Chat;
import com.example.iamhere.Model.ClientInfo;
import com.example.iamhere.R;

import java.util.ArrayList;

public class sharingList_Adapter extends RecyclerView.Adapter<sharingList_Adapter.ViewHolder> {

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    // 리사이클러뷰에 장착할 어댑터. ArrayList와 레이아웃을 어디자리에 연결할지 '정한다.'
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    //실제 데이터는 리사이클러뷰에 어댑터를 장착할 때 실제 값이 담긴 arraylist를 add하면 됨


    String TAG = "커스덤 쉐어링리스트 어댑터";
    Context context;
    ArrayList<ClientInfo> clientList = new ArrayList<ClientInfo>(); //arraylist안에 생성자로 만든 n개의 데이터가 들어있다.
    String 방장이름; // 방장이면 닉네임에 표시한다.


    //생성자 홒출 시 Context를 넘겨준다??
    public sharingList_Adapter(Context context, ArrayList<ClientInfo> clientList, String 방장이름){ //다른 예시에서는 arr를 파라미터로 넣던데...음... 메인가서 보자 뭘 넣는지
        this.context = context;
        this.clientList = clientList;
        this.방장이름 = 방장이름;
        Log.e("생성자 context ", String.valueOf(context));
        Log.e("생성자 방장이름 : ", 방장이름);
    }



    //ㅡㅡㅡㅡ
    // 순서 1 : 내가 만든 한 칸의 레이아웃을 메모리에 올려 객체화함
    //ㅡㅡㅡㅡ
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //inflate시켜주는 도구
        View itemView = inflater.inflate(R.layout.re_sharing_list, parent, false); //내가 만든 한 칸 뷰를 객체화함
        Log.e("홀더 context ", String.valueOf(context));

        return new ViewHolder(itemView); //이제 re_sharing_list 레이아웃을 손댈 수 있게 됨
    }


    //ㅡㅡㅡㅡ
    // 순서 2 : 뷰 하나하나 객체화
    //ㅡㅡㅡㅡ
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_profList; // 프사(동그랗고 테두리가 있는 형태로 보일거임)
        TextView tv_sharing_NickName; // 닉네임. 방장은 '(방장)'이라고 뒤에 더 붙는다.
        TextView tv_roomManager;

        //순서 1에서 뷰홀더를 리턴받았음 이제 해당 레이아웃에 손 댈 수 있게 되어서 find해주고 선언해서 사용하면 된다.
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_profList = itemView.findViewById(R.id.img_profList);
            tv_sharing_NickName = itemView.findViewById(R.id.tv_sharing_NickName);
            tv_roomManager = itemView.findViewById(R.id.tv_roomManager);

        }

    }

    //ㅡㅡㅡㅡ
    // 순서 3 : 뷰홀더에 들어갈 데이터만 바꿔준다.
    //ㅡㅡㅡㅡ
    //데이터가 들어간 뷰를 매번 인플레이트하고 새로 만드는게 아니라, 뷰를 그대로 쓰고(holder) arraylist에 있던 데이터만 set해준다.
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        ClientInfo client = clientList.get(position); //arr의 인덱스에 해당하는 객체를 가져와서

        holder.tv_sharing_NickName.setText(client.getName()); // 방장이 아니라면 그냥 이름만 출력
        Glide.with(context).load(client.getImg()).circleCrop().into(holder.img_profList); // 프사
//        holder.img_profList.setImageResource(); // 수정하기! 가라로 넣어둠



        String List_oneOfNickName = String.valueOf(holder.tv_sharing_NickName.getText()); // 닉네임을 추출해서 방장인지 판별할 것이다.
        Log.e(TAG, "List_oneOfNickName : "+List_oneOfNickName); // if문 전에 확인용
        Log.e(TAG, "clientList : "+clientList); // 확인용


        // 참고! 방장은 명단의 첫 번째 사람이다.
        // 방장 양도 가능성 남겨둠
        if (clientList.get(0).getName().equals(List_oneOfNickName)) {
            holder.tv_roomManager.setText("(방장)"); // 지금 들어온 닉네임이 방장이라면 settext로 방장임을 알려주는 text를 추가하기
            holder.tv_roomManager.setTextColor(Color.parseColor("#FF8535")); // 방장채팅 색상과 동일하게 맞추기

        } else {
            holder.tv_roomManager.setText(""); // 복구
            holder.tv_roomManager.setTextColor(Color.parseColor("#3A3A3A"));
        }




    }


    //아이템 수 반환
    @Override
    public int getItemCount() {
        return clientList.size();
    }


    // 메인에서 addItem()을 해서 arr에 추가하면 뷰바인더에서 arr에 있는 데이터를 가져와서 홀더에 셋해준다.
    public void addItem(ClientInfo client){
        clientList.add(client);
    }

}
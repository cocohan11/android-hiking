package com.example.iamhere.socket;


import static com.example.iamhere.socket.LocationService.br;
import static com.example.iamhere.socket.LocationService.socket;
import static com.example.iamhere.L_login.h시간m분s초;
import static com.example.iamhere.L_login.myEmail;
import static com.example.iamhere.L_login.myRoom_no;
import static com.example.iamhere.L_login.방이름;
import static com.example.iamhere.M_main.URLtoBitmap;
import static com.example.iamhere.M_share_2_Map.방퇴장처리;
import static com.example.iamhere.M_share_3_join_Map.retrofit_퇴장업뎃_exit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iamhere.M_share_2_Map;
import com.example.iamhere.Model.Chat;
import com.example.iamhere.Model.ClientInfo;
import com.example.iamhere.Recyclerview.chat_Adapter;
import com.example.iamhere.Recyclerview.sharingList_Adapter;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
// 수신용 Thread 클래스
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
public class ClientReceiver extends Thread{

    String TAG = "ClientReceiver.class";
//        private DataInputStream dis;
//    private BufferedReader br;
    private Handler handler;
    private ArrayList<Chat> chat_items;
    private chat_Adapter chat_adapter;
    private RecyclerView rv_chat, rv_list;
    private TextView roomName_num; // 방이름+인원
    private ImageView marker_img;
    private boolean isRun;
    private NaverMap 네이버Map;
    private Chronometer chronometer;
    private sharingList_Adapter list_adapter;
    //        private HashMap<String, Marker> thisRoomClientMarkers = new HashMap<>(); // 나를 제외한 다른 참여자들의 마커를 저장한다. 본인은 별도의 스레드로 마커를 찍는다.
    private ArrayList<ClientInfo> clientList; // 인덱스로 이루어진 rv때문에 hashMap은 사용하지 않고 별도의 arraylist를 만들었다.
    private Context context; // 인덱스로 이루어진 rv때문에 hashMap은 사용하지 않고 별도의 arraylist를 만들었다.



    public ClientReceiver(Handler handler, ArrayList<Chat> chat_items, chat_Adapter chat_adapter, RecyclerView rv_chat, TextView roomName_num, ImageView marker_img, boolean isRun, NaverMap 네이버Map,
                          ArrayList<ClientInfo> clientList, sharingList_Adapter list_adapter, RecyclerView rv_list, Context context, Chronometer chronometer) {

        this.handler = handler;
        this.chat_items = chat_items;
        this.chat_adapter = chat_adapter;
        this.rv_chat = rv_chat;
        this.roomName_num = roomName_num;
        this.marker_img = marker_img;
        this.isRun = isRun;
        this.네이버Map = 네이버Map;
        this.clientList = clientList;
        this.list_adapter = list_adapter;
        this.rv_list = rv_list;
        this.context = context;
        this.chronometer = chronometer;

        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setChat_items(ArrayList<Chat> chat_items) {
        this.chat_items = chat_items;
    }

    public void setChat_adapter(chat_Adapter chat_adapter) {
        this.chat_adapter = chat_adapter;
    }

    public void setRv_chat(RecyclerView rv_chat) {
        this.rv_chat = rv_chat;
    }

    public void setRoomName_num(TextView roomName_num) {
        this.roomName_num = roomName_num;
    }

    public void setRv_list(RecyclerView rv_list) {
        this.rv_list = rv_list;
    }

    public void setMarker_img(ImageView marker_img) {
        this.marker_img = marker_img;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    public void set네이버Map(NaverMap 네이버Map) {
        this.네이버Map = 네이버Map;
    }

    public void setChronometer(Chronometer chronometer) {
        this.chronometer = chronometer;
    }

    public void setList_adapter(sharingList_Adapter list_adapter) {
        this.list_adapter = list_adapter;
    }

    public void setClientList(ArrayList<ClientInfo> clientList) {
        this.clientList = clientList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public void run() { Log.e("ClientReceiver.class", "isRun : "+isRun); // this name : Thread-4

        while (br!=null) {

            try {

                // 서버로부터 받는건 객체 1개이다. 여러번 나눠서 받으니까 번잡하다. 이게 더 간편하고 확장하기도 좋다.
                String jsonString = br.readLine();
                Log.e(TAG, "방 참여자 정보 jsonString : "+jsonString); // 방 참여자 정보


                // jsonArray 인지 jsonObject 인지 모를 때
                Object json = new JSONTokener(jsonString).nextValue();



                /*********
                 * 입장
                 *********/
                if (json instanceof JSONArray) { // 여러 명에 대한 정보


                    JSONArray jsonArray = new JSONArray(jsonString);  // Json String -> JsonArray
                    Log.e(TAG, "이 json은 JSONArray 이다 !!");
                    Log.e(TAG, "jsonArray : "+jsonArray);
                    Log.e(TAG, "clientList size : "+clientList.size()+"개 "+ clientList);



                    // 모든 참여자에 대한 정보 (없는 사람만 추가)
                    clientList = returnOneClient_입장(jsonArray, clientList, 네이버Map); // 참여자 명단 업뎃
                    Log.e(TAG, "함수 바깥 - clientList : "+clientList.size()+"개 "+clientList); // 확인!! 리턴받은 clientList인지 확인하기


                    // 마지막 입장한 참여자
                    ClientInfo lastlyClient = clientList.get(clientList.size()-1); // 방금 입장한 참여자를 UI메소드에 보낸다.


                    // 입장 후 UI 변화를 적용한다
                    clientList = whatPurpose_doUI입장(lastlyClient, handler, clientList, context, 네이버Map, roomName_num, marker_img,
                            chat_items, rv_chat, chat_adapter, rv_list, list_adapter);
                    Log.e(TAG, "whatPurpose_doUI입장 함수 바깥 - clientList : "+clientList.size()+"개 "+clientList);

//                            Log.e(TAG, "for문) '입장' 후 handler로 img view가져와서 marker에 setIcon하기// 핸들러 따로 실행 함");
//                            clientList = markerSetImg(handler, clientList, i, marker_img);





                /*************************************
                 * 채팅, 위치, 퇴장, 강제종료, 운동시작
                 ************************************/
                } else if (json instanceof JSONObject) { // 한 사람에 대한 정보


                    Log.e(TAG, "이 json은 JSONObject 이다 !!");

                    // 배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Log.e(TAG, "jsonObject : "+jsonObject);


                    // 한 사람의 정보. 용도 추출
                    String StreamPurposes = (String) jsonObject.get("purposes"); // 입장/채팅/위치/퇴장 중 1
                    String RoomFirstPerson_Email = (String) jsonObject.get("email");
                    String RoomFirstPerson_Nickname = (String) jsonObject.get("chatFrom");
                    String RoomFirstPerson_imgURI = (String) jsonObject.get("markerImg"); // 하나라도 key이름이 틀리면 이후가 작동 안 한다....
                    String RoomFirstPerson_Msg = (String) jsonObject.get("msg");
                    String RoomFirstPerson_ChatTime = (String) jsonObject.get("chatTime");
                    double RoomFirstPerson_Lat = (double) jsonObject.get("Lat");
                    double RoomFirstPerson_Lng = (double) jsonObject.get("Lng");
                    Log.e(TAG, "첫 번째 사람 변수 확인 jsonObject " +
                            "\nStreamPurposes : "+ StreamPurposes);



                    // 서버에서 방금 보낸 딱 한 명에 대한 정보
                    ClientInfo client = new ClientInfo(StreamPurposes, RoomFirstPerson_Email, RoomFirstPerson_Nickname, "img..",RoomFirstPerson_imgURI, RoomFirstPerson_Msg,
                            RoomFirstPerson_ChatTime, "chatFrom..", RoomFirstPerson_Lat, RoomFirstPerson_Lng, null,null);
//                        clientList.add(client); // << 주의!! 추가하면 안 됨. 그러면 위치 업뎃할 때마다 1명씩 추가 됨



                    Log.e(TAG, "handler로 ui작업(1~4단계 처리를 한다)");
                    clientList = whatPurpose_doUI(client, handler, clientList, context, 네이버Map, roomName_num, marker_img,
                            chat_items, rv_chat, chat_adapter, rv_list, list_adapter, chronometer);



                    Log.e(TAG, "한 명의 정보가 왔다. 마커를 생성하고 client객체 하나를 만든다.");
                    Log.e(TAG, "함수 바깥2 - clientList 추가 : "+clientList.size()+"개 "+clientList);
                }

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }


        }
    }


    // *********** 수신 ***********
    // 입장 후 UI 변경사항
    // 파라미터가 많은 이유 : M_share_3_join_Map 에서도 이 함수를 쓰니까 뷰들을 파라미터로 뚫어놨다.
    // 리턴이 2개라서 1개의 객체로 받기 위해 모델을 만들었음
    // ****************************

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    public ArrayList<ClientInfo> whatPurpose_doUI(ClientInfo lastlyClient, Handler handler, ArrayList<ClientInfo> clientList, Context context,
                                                  NaverMap 네이버Map, TextView roomName_num, ImageView marker_img, ArrayList<Chat> chat_items, RecyclerView rv_chat, chat_Adapter adapter,
                                                  RecyclerView rv_list, sharingList_Adapter list_adapter, Chronometer chronometer) { String TAG = "whatPurpose_doUI() ";


        // UI 작업은 핸들러로 처리해야 한다
        handler.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {


                Log.e(TAG, "\n"+
                        "\nUI 입장 외 변수 확인.. " +
                        "\nclientList size : " + clientList.size() +
                        "\npurposes : " + lastlyClient.getPurposes() +
                        "\nemail : " + lastlyClient.getEmail() +
                        "\nnickName : " + lastlyClient.getName() +
                        "\nmarkerImg : " + lastlyClient.getMarkerImg() +
                        "\nmarker null이겠지: " + lastlyClient.getMarker() +
                        "\nmsg : " + lastlyClient.getMsg() +
                        "\nchatTime : " + lastlyClient.getChatTime() +
                        "\nLat : " + lastlyClient.getLat() +
                        "\nLng : " + lastlyClient.getLng());


                /*********
                 * 채팅
                 *********/
                if (lastlyClient.getPurposes().equals("채팅")) { Log.e(TAG, "if문 - 채팅");



                    Chat chat = new Chat(lastlyClient.getName(), lastlyClient.getMsg(), lastlyClient.getChatTime());
                    chat_items.add(chat); //리사이클러뷰와 연결된 배열에 추가. 결과적으로 리사이클러뷰에 보임



                /**********************
                 * 위치 : n초마다 실행
                 **********************/
                } else if (lastlyClient.getPurposes().equals("위치")) { Log.e(TAG, "if문 - 위치");


                    // 채팅으로 우선 확인하기
//                    Chat chat = new Chat(lastlyClient.getName(), lastlyClient.getLat()+"/"+lastlyClient.getLng(), lastlyClient.getChatTime());
//                    chat_items.add(chat); //리사이클러뷰와 연결된 배열에 추가. 결과적으로 리사이클러뷰에 보임


                    for(int i=0; i<clientList.size(); i++) { // 내가 이 방에 처음 입장했을 때 실행. 현재 참여중인 방참여자 모두 가져옴. 마커로 비교해서 없는 사람만 추가하기
                        Log.e(TAG, "목적 : 위치/ 마커 반복문에 들어왔다 / i : "+i);


                        // 명단에 있는 이메일과 방금 막 위치정보를 보낸 이메일이 동일하면 명단을 업뎃한다.
                        if (clientList.get(i).getEmail().equals(lastlyClient.getEmail())) {


                            // clientList는 제일 중요한 참여자 정보를 담는 장소임. 주의해서 업뎃하기기
                            clientList.get(i).setPurposes(lastlyClient.getPurposes());
                            clientList.get(i).setLat(lastlyClient.getLat());
                            clientList.get(i).setLng(lastlyClient.getLng());
                            Log.e(TAG, "clientList (목적, msg, 위경도) 업뎃");


                            ClientInfo client = clientList.get(i);
                            Marker marker;

                            // 입장 후 첫 위치전송이면 null이라서 새로 생성하고
                            // 이 사람의 marker값이 있으면 기존 마커값을 삭제하고 다시 set하기
                            if (clientList.get(i).getMarker() == null) {
                                marker = new Marker();
                            } else {
                                marker = clientList.get(i).getMarker();
                                marker.setMap(null);
                            }

                            marker.setPosition(new LatLng(client.getLat(),client.getLng()));
                            marker.setCaptionText(client.getName());
                            marker.setCaptionAligns(Align.Top);
                            marker.setIcon(OverlayImage.fromBitmap(client.getBitmap()));
                            marker.setMap(네이버Map);
                            Log.e(TAG, "marker 생성 : "+marker);


                            clientList.get(i).setMarker(marker);
                            Log.e(TAG, "marker 저장 : "+marker);


                        }
                    }


                /*********
                 * 퇴장
                 *********/
                } else if (lastlyClient.getPurposes().equals("퇴장")) { // 나말고 누가 퇴장했다.


                    Chat chat = new Chat("", lastlyClient.getMsg(), ""); // msg 는 모두에게 동일해서 인덱스 0도 ok
                    chat_items.add(chat); // 리사이클러뷰와 연결된 배열에 추가
                    Log.e(TAG, "lastlyClient 목적 : "+lastlyClient.getPurposes());



                    // 명단에서 퇴장한 사람 찾아내기
                    for (int i=0; i<clientList.size(); i++) {

                        Log.e(TAG, "명단 삭제 i : "+i);
                        Log.e(TAG, "clientList : "+clientList);


                        if (clientList.get(i).getName().equals(lastlyClient.getName())) { // 명단 중에 퇴장하는 사람의 닉네임과 같다면
                            Log.e(TAG, "clientList i번재 이름 == lastlyClient i번재 이름 ");


                            Log.e(TAG, "퇴장한 사람의 마커 삭제");
                            clientList.get(i).getMarker().setMap(null);


                            // 퇴장한 사람의 명단 삭제
                            Log.e(TAG, "clientList.remove 직전 : "+clientList);
                            clientList.remove(i); // 해당 인덱스 삭제. 아래에서 업뎃
                            Log.e(TAG, "clientList.remove 직후 : "+clientList);

                        }
                    }

                    // 방이름+남은인원
                    roomName_num.setText(방이름+"("+clientList.size()+"명)"); // 여기 위치! 명단 삭제 후 인원 setText




                /***********
                 * 강제종료
                 ***********/
                } else if (lastlyClient.getPurposes().equals("강제종료")) {

                    // 방퇴장처리를 여기말고 위치스레드안에 위치시킨 이유 : activity.class를 파라미터로 전달이 안 되서.
//                    소켓통신목적 = lastlyClient.getPurposes(); // M_share_3_join_Map.class에서 강제종료할 때 사용하기 위해
                    Log.e(TAG, "소켓 수신... '강제종료' 한다는 메세지를 받았다");

                    Toast.makeText(context, "위치공유방이 종료되었습니다.\n나의 기록에서 조회하실 수 있습니다.", Toast.LENGTH_LONG).show(); // 실행할 코드
                    retrofit_퇴장업뎃_exit(myRoom_no, myEmail, h시간m분s초); //위치공유방 참여자 명단 변경(퇴장시간, 소요시간)
                    방퇴장처리(context); //퇴장 후 뒷처리 : 변수, 쉐어드 초기화
                    socketClose_Exit();


                /***********
                 * 운동시작
                 ***********/
                } else if (lastlyClient.getPurposes().equals("운동시작")) {

//                    소켓통신목적 = lastlyClient.getPurposes(); // M_share_3_join_Map.class에서 강제종료할 때 사용하기 위해
                    Log.e(TAG, "소켓 수신... '운동시작' 한다는 메세지를 받았다");


                    Toast.makeText(context, "등산을 시작합니다. 안전산행 하세요.", Toast.LENGTH_SHORT).show(); //퇴장알림을 해야 확신할 듯
                    chronometer.setBase(System.currentTimeMillis()); //스탑와치의 시작점을 시스템현재시간으로 맞춘다. tcp/ip 이후에는 방장이 방을 시작한 시간으로 맞춘다.
                    chronometer.start(); //시작을 코드로 해줘야 굴러가서 이벤트가 작동됨

                }


                adapter.notifyDataSetChanged(); // 채팅 업데이트
                list_adapter.notifyDataSetChanged(); // 명단 업데이트
                rv_chat.scrollToPosition(chat_items.size()-1);
                // rv_list는 위치변경 될 때마다 아래로 당겨지면 방장이 가려져서 입장할 때만 스크롤바 내리기


                Log.e(TAG, "chat+list rv 업뎃, 스크롤 최하단");
                Log.e(TAG, "adapter.notifyDataSetChanged()");
                Log.e(TAG, "clientList : "+clientList);


            }
        });

        return clientList; // 객체로 리턴하는 이유 : 리턴을 2개 받아야되서 객체 하나로 만듦

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    public ArrayList<ClientInfo> whatPurpose_doUI입장(ClientInfo lastlyClient, Handler handler, ArrayList<ClientInfo> clientList, Context context,
                                                    NaverMap 네이버Map, TextView roomName_num, ImageView marker_img, ArrayList<Chat> chat_items, RecyclerView rv_chat, chat_Adapter adapter,
                                                    RecyclerView rv_list, sharingList_Adapter list_adapter) { String TAG = "whatPurpose_doUI입장() ";


        // UI 작업은 핸들러로 처리해야 한다
        handler.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {


                Log.e(TAG, "\n"+
                        "\nUI입장 변수 확인.. " +
                        "\nclientList : " + clientList +
                        "\nclientList size : " + clientList.size() +
                        "\npurposes : " + lastlyClient.getPurposes() +
                        "\nemail : " + lastlyClient.getEmail() +
                        "\nnickName : " + lastlyClient.getName() +
                        "\nmarkerImg : " + lastlyClient.getMarkerImg() +
                        "\nmarker null이겠지 : " + lastlyClient.getMarker() +
                        "\nmsg : " + lastlyClient.getMsg() +
                        "\nchatTime : " + lastlyClient.getChatTime() +
                        "\nLat : " + lastlyClient.getLat() +
                        "\nLng : " + lastlyClient.getLng());


//                Marker pickThisMarker = lastlyClient.getMarker(); // 위치, 퇴장에서 같이 사용
//                Log.e(TAG, "pickThisMarker : "+pickThisMarker);
//                Log.e(TAG, "pickThisMarker getCaptionText : "+pickThisMarker.getCaptionText());
//                Log.e(TAG, "pickThisMarker getIcon : "+pickThisMarker.getIcon());
//                Marker pickThisMarker = thisRoomClientMarkers.get(lastlyClient.getEmail()); // 위치, 퇴장에서 같이 사용


                /*********
                 * 입장
                 *********/
                if (lastlyClient.getPurposes().equals("입장")) { Log.e(TAG, "if문 - 입장"); // 누군가 입장할 때 실행


                    // 방참여자수
                    roomName_num.setText(방이름+"("+clientList.size()+"명)"); //방이름+인원


                    // 채팅창 업뎃뎃
                    Chat chat = new Chat("", lastlyClient.getMsg(), ""); // 파라미터 3개 중 2개 비워두기
                    chat_items.add(chat); //리사이클러뷰와 연결된 배열에 추가. 결과적으로 리사이클러뷰에 보임


                }


                adapter.notifyDataSetChanged(); // 채팅 업데이트
                list_adapter.notifyDataSetChanged(); // 명단 업데이트
                rv_chat.scrollToPosition(chat_items.size()-1);
                rv_list.scrollToPosition(chat_items.size()-1);


                Log.e(TAG, "clientList : "+clientList);

            }
        });

        return clientList;
    }

    // 입장 후 ~ (1명씩 데이터 담기) ~ 입장UI처리
    public ArrayList<ClientInfo> returnOneClient_입장(JSONArray jsonArray, ArrayList<ClientInfo> clientList, NaverMap 네이버Map) throws JSONException { String TAG = "returnOneClient_입장() ";


        ClientInfo client = null;

        for(int i=0; i<jsonArray.length(); i++) { // 내가 이 방에 처음 입장했을 때 실행. 현재 참여중인 방참여자 모두 가져옴. 마커로 비교해서 없는 사람만 추가하기

            // 배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
            JSONObject object = (JSONObject) jsonArray.get(i);

            // JSON name으로 추출
            String purposes = (String) object.get("purposes");
            String email = (String) object.get("email");
            String Img = (String) object.get("Img");
            String markerImg = (String) object.get("markerImg");
            String msg = (String) object.get("msg");
            String chatTime = (String) object.get("chatTime");
            String chatFrom = (String) object.get("chatFrom");
            double Lat = (double) object.get("Lat");
            double Lng = (double) object.get("Lng");

            Log.e(TAG,  "\n변수 확인 jsonObject.. " +
                    "\njsonArray.length() : " + jsonArray.length() +
                    "\npurposes : " + purposes +
                    "\nemail : " + email +
                    "\nImg : " + Img +
                    "\nmarkerImg : " + markerImg +
                    "\nmsg : " + msg +
                    "\nchatTime : " + chatTime +
                    "\nchatFrom : " + chatFrom +
                    "\nLat : " + Lat +
                    "\nLng : " + Lng);


            // 안드에서 가지고있는 명단 vs 채팅서버에서 가져온 이멜 비교
            if (clientList.size() <= i) { // 인덱스로 하니까 에러나서 size로 함. 어차피 서버에서 순서대로 받아오기 때문


                // 클라이언트 한 명 정보에 저장하기
                client = new ClientInfo(purposes, email, chatFrom, Img, markerImg, msg, chatTime, chatFrom, Lat, Lng, null, URLtoBitmap(markerImg)); // 마커는 다음 메소드에서 적용
                clientList.add(client);
                Log.e(TAG, "client 한 명 : "+client);
                Log.e(TAG, "클라1명 생성(마커포함) / clientList size : "+clientList.size() +clientList);

            } else {
                Log.e(TAG, "클라 추가X 이미 가진 명단임/ clientList size: "+clientList.size() +clientList);
            }

        } // ~for()


        return clientList; // 한 사람. 마지막으로 들어온 당사자 정보
    }


    static public void socketClose_Exit() {

        String TAG = "socketClose_Exit()";
        try {
            if (socket != null) {

                Log.e(TAG, "* socket.isClosed() before: "+socket.isClosed());
                socket.close();
                Log.e(TAG, "** socket.isClosed() after: "+socket.isClosed());
                Log.e(TAG, "*** socket : "+socket); // 소켓을 close해도 객체가 null인건 아니다다

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


} // ~inner class(수신)

package com.example.iamhere;

import static com.example.iamhere.L_login.myEmail;
import static com.example.iamhere.L_login.myName;
import static com.example.iamhere.L_login.myImg;
import static com.example.iamhere.L_login.myMarkerImg;
import static com.example.iamhere.L_login.mySmallBitmapImg;
import static com.example.iamhere.L_login.originFile;
import static com.example.iamhere.L_login.strNickName;
import static com.example.iamhere.L_login.마커합성bitmap;
import static com.example.iamhere.M_main.URLtoBitmap;

import static okhttp3.MultipartBody.Part.createFormData;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.iamhere.Interface.Profile;
import com.example.iamhere.Model.Profile_ed;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class L_profile extends AppCompatActivity {
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    //1.프로필사진
    //linearlayout클릭시 사진앨범에서 가져오기

    //2.닉네임
    //규칙: 숫자,영어,한글만 가능.특수문자x/2~6자 ->정규식 ("^[가-힣ㄱ-ㅎa-zA-Z0-9._ -]{2,6}\$")
    //필수값

    //3.완료버튼
    //서버에프로필사진 저장
    //이전화면에서 받아온 email로 DB에 사진경로, 닉네임 insert하기
    //쉐어드에 이메일저장해서 자동로그인하기 /이전 화면들에 쉐어드기록있는지 확인 후 넘기기(방법은 아직)

    //4.레트로핏 클래스만들기(파라미터 뚫어서 편하게 가져다 쓸 수 있게 만들기) >>> 파라미터로 클래스이름은 못 가져와져서 그냥 복붙용으로 클래스하나 만들어 둚.

    //참고
    //만약 여기서 완료누르기 전에 어플을 꺼버린다면 로그인 후 여기화면을 띄우자.
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    String TAG = "L_profile"; //log.e()의 첫 번째 파라미터로 클래스명 넣기
    ImageView img_prof; //사용자 선택에 따라 변경되는 프로필사진
    LinearLayout Linear_profImg; //프로필사진변경하려면 누르는 뷰인데 linear전체를 클릭대상으로 정함. 꺽쇠는 너무 좁으니까.
    Button btn_prof; //완료버튼을 누르면 사진과 닉네임이 서버로 전송되어서 저장된다.
    EditText et_prof_nickName; //정규식대로 닉네임을 정한다.
    boolean patternNickName; //닉네임이 정규식 통과됬는가
    private final int GET_GALLERY_IMG = 200; //intent 결과를 구분하기위한 아무값
    String filePath; //length수정사진 경로(저장할 사진)
    String email;
    File file_prof; //전역변수로 빼야 쉐어드저장할 때 쉽게 사용
    int ramdomForMakeImgName; // 난수는 그 때 그 때 생성하면 의미없으니까 전역으로 뺌
    Handler handler = new Handler(Looper.getMainLooper());

//    Call<Profile_ed> call; //전역변수로 빼야 프로필사진 없는 경우의 call객체도 기존코드에 대입할 수 있다.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lprofile);
        ID(); //xml에서 선언한 id가져오기
        Log.e(TAG, "onCreate");

        //쉐어드로 구분하려고 했는데 그냥.. 여기로 보낼 때 intent로 값을같이 보내서 구분해야겠음
        //intent에 실리는 값 : 사진url,닉네임
        //다시 쉐어드에 저장하기

        //3곳으로부터 여기를 거침 (email변수로 통일)
        Intent intent = getIntent();

        String kakao_UserEmail = intent.getStringExtra("kakao_UserEmail"); //mypage >> this
        String name = intent.getStringExtra("UserNickName"); //mypage >> this

        email = intent.getStringExtra("loginEmail"); //login >> this
        if(kakao_UserEmail != null) email = kakao_UserEmail; //login >> this
        if(email == null) email = intent.getStringExtra("UserEmail"); //join >> this
        if(email == null) {
            SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
            email = auto.getString("UserEmail",null); 
       }
        Log.e(TAG, "email : "+email);
        Log.e(TAG, "name : "+name);

        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        //프로필 처음 작성
        //ㅡㅡㅡㅡㅡㅡㅡㅡ
        if(name == null) {
            Log.e(TAG, "name에 값이 없다");
            //사진 : 기본
            //닉네임 : 공란

        //ㅡㅡㅡㅡㅡ
        //프로필수정
        //ㅡㅡㅡㅡㅡ
        } else {
            Log.e(TAG, "name에값이 있다");
            Log.e(TAG, "myImg : "+myImg);
            //사진 : 기존사진 삽입
            //닉네임 : 기존닉네임 삽입
            //사진있는경우 없는경우
            if(myImg != null) { //myImg가 http://~형태임
                Glide.with(getApplicationContext()).load(myImg).into(img_prof); //닉네임도 있으면 myImg static변수도 있음
                Log.e(TAG, "Glide myImg != null");

            } else {
                Glide.with(getApplicationContext()).load(R.drawable.ic_prof_first).into(img_prof); //기본사진 삽입
                Log.e(TAG, "Glide myImg == null");

            }
            et_prof_nickName.setText(name); //intent로 받아온 닉네임 삽입

        }




        //ㅡㅡㅡㅡㅡㅡ
        //1.프로필사진 : add
        //ㅡㅡㅡㅡㅡㅡ
        Linear_profImg.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View view) {
                //클릭했을 때 작게 선택창이 생기고 앨범가면 권한을물어본 뒤 권한ok면 들어가서선택가능하고 권한x면 앨범창이 꺼진다.
                //선택된 이미지는 완료버튼에서 서버로 보내지고 db에 저장경로가 저장된다.
                Log.e(TAG, "Linear_profImg 클릭");

                //인텐트로 갤러리를 선택하는 창을 띄운다.ㅣ
                Intent intent = new Intent();
//                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                Log.e(TAG, "Intent 선언 (ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)");
//                Uri uri = Uri.fromParts("package:com.example.iamhere", getApplicationContext().getPackageName(), null);
//                Log.e(TAG, "uri");
//                intent.setData(uri);
                Log.e(TAG, "intent.setData(uri)");
                intent.setType("image/*"); //인텐트로 이미지형태인 뭘 할거다.
                Log.e(TAG, "Intent 이미지로 setType");
                intent.setAction(Intent.ACTION_PICK); //이미지를 가져오도록 intent에 시킨다? //ACTION_PICK을 해야 사진선택으로 가서 다운로드도 선택가능
                Log.e(TAG, "Intent setAction 인텐트야 갤러리에서 가져와라");


//                Log.e(TAG, "Intent setAction 런타임 권한");
                startActivityForResult(intent, GET_GALLERY_IMG); //전달할 인텐트와 요청코드(200)를 같이 보낸다(구분을 위한 아무값)
                Log.e(TAG, "Intent startActivityForResult() 인텐트야 너의 이름은 200이고, 받는쪽이 200번이라고 말해야 해당데이터를 주렴");
                Log.e(TAG, "Intent startActivityForResult() 과연 권한 화면을 열 것인가");

                //이렇게 해도 에러낭네~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION); //런타임 파일접근권한(여기서도 권한을 얻어야 저장소 에러가 안남)
            }
        });

        //ㅡㅡㅡㅡㅡ
        //2.닉네임
        //3.완료버튼 : 프로필사진,닉네임 db,쉐어드 저장
        //ㅡㅡㅡㅡㅡ
        btn_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //닉네임 가져오기
                strNickName = String.valueOf(et_prof_nickName.getText()); //사용자가 입력한 닉네임을 string값으로 변환
                Log.e(TAG, "111 btn_prof 클릭");
                Log.e(TAG, "222 strNickName : "+strNickName);


                retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                        .baseUrl("http://15.164.129.103/")    // baseUrl 등록
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();

                Profile file_interface = retrofit.create(Profile.class);   // 레트로핏 인터페이스 객체 구현

                //ㅡㅡㅡㅡㅡ
                // 화면전환 : 통신완료되면 이전화면으로 되돌아가려고 했는데 통신이 다 끝난 뒤에 돌아가면 너무 늦어서 먼저 돌아감.
                //ㅡㅡㅡㅡㅡ
                //ㅡㅡㅡㅡㅡㅡㅡ
                //처음프로필작성
                //ㅡㅡㅡㅡㅡㅡㅡ
                if(name == null) {

                    if(isPatternNickName(strNickName)) { //2.적합하다면 화면전환 L_profile >> M_mail

                        Intent intent = new Intent(getApplicationContext(), M_main.class); //로그인btn >> 지도첫화면
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //top빼고 아래액티비티 지움
                        startActivity(intent);
                        Log.e(TAG, "startActivity()");
                        Toast.makeText(getApplicationContext(), "반갑습니다. "+strNickName+"님\n닉네임은 나의정보에서 변경가능합니다.", Toast.LENGTH_LONG).show(); //첫 로그인시 뜨는 토스트라서 long선택


                        //ㅡㅡㅡㅡㅡㅡㅡ
                        // 사진 변경 X : 사진있고 없고로 나눈 이유 : 전송시 다르게 보내려고
                        //ㅡㅡㅡㅡㅡㅡㅡ
                        if(originFile == null) { //getNickname()메소드로 call객체 생성

                            Log.e(TAG, "222222 사진이 없다"); //사진경로값 변수에 들어있는지 확인
                            Log.e(TAG, "call 객체 생성 - getNickname()"); //콜 객체 두가지 중에 어떤건지 로그로 확인

                            Call<Profile_ed> call = file_interface.getNickname(email,strNickName,"onlyName"); //사진없이 닉네임만 서버로 보내는 call객체 생성
                            retrofit프로필(call, file_interface,false); //닉네임만 변경

                        //ㅡㅡㅡㅡㅡㅡㅡ
                        // 사진 변경 O
                        //ㅡㅡㅡㅡㅡㅡㅡ
                        } else { //getFile()메소드로 call객체 생성

                            //ㅡㅡㅡㅡ
                            // 스레드 : bitmap축소, 통신
                            //ㅡㅡㅡㅡ
                            //왜 스레드? >> 느려서 별도 스레드로 뺌
                            new Thread(new Runnable() { //filePath를 얻기까지 오래걸리는건데 통신할 때도 filePath가 필요해서 스레드 안에 축소, 통신코드가 있어야함
                                @Override
                                public void run() {
                                    Log.e(TAG, "run()..");

                                    try {

                                        // 프사(직사각형)
                                        Log.e(TAG, "처음프로필작성 프사(직사각형) originFile : "+originFile);
                                        MultipartBody.Part body = 사진_body(); //사진을 body형태로 만들어 retrofit으로 서버전송
                                        Call<Profile_ed> call = file_interface.getFile(body,email,strNickName,"imgName"); //body가 최종형태 데이터이다...
                                        Log.e(TAG, "처음프로필작성 555 call 객체 생성 - getFile()");
                                        retrofit프로필(call, file_interface, true); //사진도 같이 보냄


                                        // 에러나면 위 retrofit함수에 넣기
                                        // 프사(합성마커)
//                                        Log.e(TAG, "처음프로필작성 프사(합성마커) originFile : "+originFile);
//                                        MultipartBody.Part body_marker = 합성프사마커_body(originFile);
//                                        Call<Profile_ed> call_marker = file_interface.getProfileMarker(body_marker, myEmail); //사진없이 닉네임만 서버로 보내는 call객체 생성
//                                        retrofit_합성마커(call_marker); // true만 응답받으면 됨


                                    }catch(Exception e) {
                                        Log.e(TAG, "catch try에서 에러났나봄");
                                    }
                                }
                            }).start();

                        } //~프사변경

                    } else { //입력한 닉네임이 정규식 부적합
                        Toast.makeText(getApplicationContext(), "형식에 맞게 입력해주세요", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "444 정규식 부적합");
                    }

                //ㅡㅡㅡㅡㅡㅡ
                // 프로필수정
                //ㅡㅡㅡㅡㅡㅡ
                } else { //나의 공간으로 되돌아간다.

                    if(isPatternNickName(strNickName)) { //정규식O

                        if(originFile == null) { //사진x닉네임만 변경

                            Call<Profile_ed> call = file_interface.getNickname(email,strNickName,"onlyName"); //사진없이 닉네임만 서버로 보내는 call객체 생성
                            retrofit프로필(call, file_interface, false); // 닉네임만 변경

                        } else { //사진o닉네임o 변경

                            new Thread(new Runnable() { //filePath를 얻기까지 오래걸리는건데 통신할 때도 filePath가 필요해서 스레드 안에 축소, 통신코드가 있어야함
                                @Override
                                public void run() {
                                    Log.e(TAG, "run()..");

                                    try {

                                        // 프사(직사각형)
                                        Log.e(TAG, "프로필수정 프사(직사각형) : "+originFile);
                                        MultipartBody.Part body = 사진_body(); // 사진을 body형태로 만들어 retrofit으로 서버전송
                                        Call<Profile_ed> call = file_interface.getFile(body,email,strNickName,"imgName"); //body가 최종형태 데이터이다...
                                        retrofit프로필(call, file_interface, true); // 프로필수정 프사(직사각형),(합성마커)


                                        // 프사(합성마커)
                                        // 위 retrofit 응답 받고 실행해야 에러 안 나서 저 메소드 안으로 옮김



                                    } catch(Exception e) {
                                        Log.e(TAG, "catch try 에러");
                                    }
                                }
                            }).start();


                        }

                        finish(); //finish 후 생명주기로 되돌아간다.
                        Log.e(TAG, "finish()");

                    } else { //정규식X
                        Toast.makeText(getApplicationContext(), "형식에 맞게 입력해주세요", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "444 정규식 부적합");
                    }

                } //~프로필수정

                //사진파일 가져오기
                Log.e(TAG, "333 originFile: "+originFile); //사진경로값 변수에 들어있는지 확인 >> /storage/emulated/0/DCIM/Camera/20220524_173533.jpg

            } //~onClick
        });


    } //~onCreate()


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    private void 합성프사마커_body(String imgUrl, String fileName, Profile file_interface) { String method = "합성프사마커_body() ";

        Log.e(TAG, "합성프사마커_body() 메서드 시작");

        new Thread(new Runnable() {
            @Override public void run() {
                try { // try catch를 안 해서 에러났었음

                    Bitmap bitmap_profMarker = URLtoBitmap(imgUrl); // 서버에서 응답받은 imgUrl로 합성해야 한다. 안 그러면 원본이 합성되어버린다.
                    Log.e(TAG, method+"bitmap_profMarker : "+bitmap_profMarker);

                    // 원본으로 마커 합성함
                    bitmap_profMarker = bitmapEdit(bitmap_profMarker); // 사진_body와 다른점!! 합성한 사진으로 filePath를 만든다.
                    Log.e(TAG, method+"bitmap_profMarker : "+bitmap_profMarker);

                    MultipartBody.Part body = imgToBody2(bitmap_profMarker, fileName); // http request로 보낼 body(합성한 마커img)
                    Log.e(TAG, "합성프사마커_body() filePath : "+filePath);


                    Call<Profile_ed> call_marker = file_interface.getProfileMarker(body, myEmail); //사진없이 닉네임만 서버로 보내는 call객체 생성
                    retrofit_합성마커(call_marker); // markerUrl 응답받기, 쉐어드에 저장, static정적변수에 대입



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();






    }

    private MultipartBody.Part 사진_body() { Log.e(TAG, "사진_body() 입장");


        //원본 file path -> bitmap
        Log.e(TAG, "사진_body() 프로필사진 변경 o");
        Bitmap bitmap = BitmapFactory.decodeFile(originFile); //선택한 사진경로를 통해 비트맵으로 바꾼다.
        Log.e(TAG, "사진_body() bitmap : "+bitmap);

        //축소한 file path
        MultipartBody.Part body = imgToBody(bitmap, originFile); //비트맵으로 바꿔서 사진length를 조정한다(2MB이하)
        Log.e(TAG, "사진_body() filePath : "+filePath); // >> /storage/emulated/0/DCIM/Camera/20220524_173533.jpg

        return body;
    }

//    private MultipartBody.Part 사진_body() {
//
//        Log.e(TAG, "사진_body() 입장");
//        //원본 file path -> bitmap
//        Log.e(TAG, "사진_body() 프로필사진 변경 o");
//        Bitmap bitmap = BitmapFactory.decodeFile(originFile); //선택한 사진경로를 통해 비트맵으로 바꾼다.
//        //축소한 file path
//        filePath = getImageViewToFileResizing(bitmap, originFile); //비트맵으로 바꿔서 사진length를 조정한다(2MB이하)
//        Log.e(TAG, "사진_body() filePath : "+filePath); // >> /storage/emulated/0/DCIM/Camera/20220524_173533.jpg
//
//        //축소한 file path -> bitmap
//        Bitmap smallBitmap = BitmapFactory.decodeFile(filePath); //선택한 경로의 사진을 비트맵으로 바꾼다.
//        Log.e(TAG, "사진_body() smallBitmap : "+smallBitmap);
//
//        //마커합성(마커+프사)
//        Log.e(TAG, "사진_body() 마커합성bitmap bitmapEdit 전 : "+마커합성bitmap);
//        마커합성bitmap = bitmapEdit(smallBitmap); //쉐어드에 마커이미지 통째로 저장(string형태)
//        //이곳에서 합성하고 저장해야 쓰기 편함
//        Log.e(TAG, "사진_body() 마커합성bitmap bitmapEdit 후 : "+마커합성bitmap);
//
//        //string타입 사진
//        mySmallBitmapImg = BitmapToString(TAG,마커합성bitmap); //합성사진 대입/ 통신 후 쉐어드저장
//
//
//
//        //통신하기위한 file객체
//        file = new File(filePath); //경로를 통해 파일 객체 생성
//        Log.e(TAG, "사진_body() file : "+file); // /storage/emulated/0/Download/hiking-boots.png
//        Log.e(TAG, "사진_body() file.getName() : "+file.getName());
//        Log.e(TAG, "사진_body() file.length() : "+file.length());
//
//        //import okhttp3.RequestBody / 타입지정
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file); //실제파일객체에 타입정한다.
//        Log.e(TAG, "사진_body() requestFile객체 생성 : "+requestFile);
//
//        //import okhttp3.MultipartBody / 요청바디이름, 파일명, 파일객체를 body라는 객체 안에 담음
//        MultipartBody.Part body = createFormData("uploaded_file", file.getName()+myEmail, requestFile); //body안에 여러개 집어넣음
//        Log.e(TAG, "사진_body() body 생성 : "+body);
//
//        Log.e(TAG, "사진_body() strNickName : "+strNickName);
//        Log.e(TAG, "사진_body() body : "+body);
//
//        return body;
//    }

    private void retrofit_합성마커(Call call) {

        //네트워킹 시도
        call.enqueue(new Callback<Profile_ed>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Profile_ed> call, Response<Profile_ed> response) { String method = "retrofit_합성마커() ";

                Profile_ed result = response.body(); //response.isSuccessful안에 있으니까 response가 확인이 안되서 로그찍기위해 한 층 꺼냄냄
                myMarkerImg = result.getResponse();
                Log.e(TAG, method+"myMarkerImg : "+myMarkerImg);


                SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능
                SharedPreferences.Editor Edit = auto.edit();
                Edit.putString("myMarkerImg", myMarkerImg); // 위치공유방에 쓸 marker img url
                Edit.commit(); //실질 저장
                Log.e(TAG, method+"쉐어드에도 myMarkerImg 저장");


            }

            @Override
            public void onFailure(Call<Profile_ed> call, Throwable t) {
                Log.e("onFailure : ", t.getMessage());
            }
        });

    }

    private void retrofit프로필(Call call, Profile file_interface, boolean secondRetrofit) {


        //네트워킹 시도
        call.enqueue(new Callback<Profile_ed>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Profile_ed> call, Response<Profile_ed> response) {

                Profile_ed result = response.body(); //response.isSuccessful안에 있으니까 response가 확인이 안되서 로그찍기위해 한 층 꺼냄냄
                assert null != result;
                String 응답 = result.getResponse();
                String imgUrl = result.getImgUrl();
                String fileName = result.getFileName();
                Log.e(TAG, "2222 onResponse " +
                                "\n응답 : "+응답+
                                "\nimgUrl : "+imgUrl+
                                "\nfileName : "+fileName
                );

                //정상적으로 통신이 성공한 경우
                if(response.isSuccessful()) {
                    Log.e(TAG, "3333 onResponse success");

                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                    // 1번 째 retrofit 응답 후 2번 째 retrofit - 프사(합성마커)
                    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
                    if (secondRetrofit && imgUrl != null) { // 2번째 retrofit이 여기 위치한 이유 : 첫 번째 http request를 보내고 respone받기도 전에 또 request보내면 스트림이 안 끝났다고 에러뜬다. 그래서 응답을 받은 걸 확인하고 두 번째 request보내기

                        Log.e(TAG, "프로필수정 프사(합성마커). 합성프사마커 저장하라고 resquest를 보낸다.");
                        합성프사마커_body(imgUrl, fileName, file_interface);

                    }



                    if(응답.equals("true")) {

                        myImg = "http://15.164.129.103/profImg/"+"pofImg_"+ramdomForMakeImgName+"_"+file_prof.getName(); //위치이유: 응답성공해야 변수대입/축소사진
                        Log.e(TAG, "응답 성공 myImg : "+myImg);


                        //1.쉐어드에 저장(닉네임, 프로필사진 url)
                        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능
                        SharedPreferences.Editor autoLoginEdit = auto.edit();
                        Log.e(TAG, "UserNickName : "+strNickName);

                        autoLoginEdit.putString("UserNickName", strNickName); //db컬럼명과 동일하게 하자
                        autoLoginEdit.putString("filePath", filePath); //지도에서 쓸 filePath

                        if(file_prof != null) {

                            Log.e(TAG, "사진 UserImg : "+"http://15.164.129.103/profImg/"+"pofImg_"+ramdomForMakeImgName+"_"+file_prof.getName());
                            autoLoginEdit.putString("UserImg", "http://15.164.129.103/profImg/"+"pofImg_"+ramdomForMakeImgName+"_"+file_prof.getName()); //파라미터로 받은 pw
                            autoLoginEdit.putString("smallBitmapImg", mySmallBitmapImg); // bitmap -> string
                        }

                        autoLoginEdit.commit(); //실질 저장
                        Toast.makeText(getApplicationContext(), "프로필이 수정되었습니다.", Toast.LENGTH_LONG).show();


                        myName = strNickName; //static변수에 변경된 값 대입


                    } else if(응답.equals("false_사진8BM이상")){ //사진용량이 너무 크면 업로드가 안됨

                        Log.e(TAG, "else if(응답.equals(\"false_사진8BM이상\")) 들어옴");
                        Toast.makeText(getApplicationContext(), "사진 용량이 너무 큽니다. 8MB이하의 사진을 첨부해 주세요.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "false_ onResponse / aa : "+result.getResponse());

                    } else { //기타 false

                        Log.e(TAG, "else_서버에 전송한 값이 true가 아님");
                        Toast.makeText(getApplicationContext(), "다시 시도해주세요", Toast.LENGTH_LONG).show(); //첫 로그인시 뜨는 토스트라서 long선택
                        Log.e(TAG, "false_ onResponse / bb : "+result.getResponse());
                    }
                    originFile = null; //바로 바꿀 수도 있으니까 편집하는데 덜 오래걸리라고 비워줌

                } else {
                    // 통신 실패
                    Log.e("콜.enqueue : ", "onResponse : 실패");
                }
            }

            @Override
            public void onFailure(Call<Profile_ed> call, Throwable t) {
                Log.e("onFailure : ", t.getMessage());
            }
        }); //~네트워킹 시도

    }

    //비트맵사진 편집(정사각, 크기조절, 원형, 합성)
    public Bitmap bitmapEdit(Bitmap bitmap2) {

        //정사각형 만들기
        assert bitmap2 != null : "null값이다";
        Bitmap 마커이미지 = BitmapFactory.decodeResource(getResources(), R.drawable.pin_red);  // first image(빨간마커이미지)
        bitmap2 = cropBitmap(bitmap2); //사용자 사진

        //가로 xxx으로 크기조절
        마커이미지 = resizeBitmap(마커이미지, 180); //마커 가로세로를 고정해놔서 사이즈가 안 커진거였음
        bitmap2 = resizeBitmap(bitmap2, 100);

        //원형으로 자르기
        bitmap2 = cropCircle(bitmap2);

        //하나의 비트맵을 만듦
        Bitmap result = Bitmap.createBitmap(180
                                          , 200
                                          , Bitmap.Config.ARGB_8888); //getConfig()?

        Log.e(TAG, "마커이미지.getHeight() + 마커이미지.getHeight() : "+마커이미지.getWidth()+", "+마커이미지.getHeight());
        Log.e(TAG, "bitmap2.getHeight() + bitmap2.getHeight() : "+bitmap2.getWidth()+", "+bitmap2.getHeight());

        //캔버스를 통해 둘을 겹친다.
        Canvas canvas = new Canvas(result); //<< result를 사용할거임
        canvas.drawBitmap(마커이미지, new Matrix(), null); // (bitmap, 매트릭스, paint)
        canvas.drawBitmap(bitmap2, 40, 8, null); //기존 55,45

        Log.e(TAG, "Canvas를 이용해 마커 이미지 합성함");

        return result;
    }


    //비트맵 원형으로 자르기(정사각형 선행)
    public Bitmap cropCircle(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        int size = (bitmap.getWidth() / 2);
        canvas.drawCircle(size, size, size, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    // bitmap 이미지 잘라내기(정사각형)
    public Bitmap cropBitmap(Bitmap original) {

        Log.e(TAG, "cropBitmap() 들어옴");
        Bitmap result = null;
//        Log.e(TAG, "original.getHeight() : "+original.getHeight());
//        Log.e(TAG, "original.getWidth() : "+original.getWidth());

        if(original.getHeight() <= original.getWidth()) { //가로가 높이보다 작거나 같다면 세로에 맞추기

            Log.e(TAG, "cropBitmap() 세로에 맞추기");
            result = Bitmap.createBitmap(original
                    , original.getWidth() / 4 //X 시작위치 (원본의 4/1지점)
                    , original.getHeight() / 4 //Y 시작위치 (원본의 4/1지점)
                    , original.getHeight() / 2 // 넓이 (원본의 절반 크기)
                    , original.getHeight() / 2); // 높이 (원본의 절반 크기)

        } else { //가로에 맞추기

            Log.e(TAG, "cropBitmap() 가로에 맞추기");
            result = Bitmap.createBitmap(original
                    , original.getWidth() / 4 //X 시작위치 (원본의 4/1지점)
                    , original.getHeight() / 4 //Y 시작위치 (원본의 4/1지점)
                    , original.getWidth() / 2 // 넓이 (원본의 절반 크기)
                    , original.getWidth() / 2); // 높이 (원본의 절반 크기)
        }

        if (result != original) {
            original.recycle();
        }
        return result;
    }

    // 가로길이 200으로 맞춰 이미지 크기 조절
    static public Bitmap resizeBitmap(Bitmap original, int resizeWidth) {

        double aspectRatio = (double) original.getHeight() / (double) original.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(original, resizeWidth, targetHeight, false);
        if (result != original) {
            original.recycle();
        }
        return result;
    }

    //intent로 보낸 data를 받는 곳(이미지)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode : 내가임의로정한 식별하기위한 값(뭘가져올지)
        //resultCode : 호출된 액티비티에서설정한 성공/실패 값(잘가져와졌는지)
        //data : 데이터타입이 intent이다. 호출된 액티비티에서 저장한 인텐트 값. 여기선 갤러리니까 사진.
        Log.e(TAG, "onActivityResult 입장");

        if(requestCode == GET_GALLERY_IMG) { //갤러리 열기위해 intent에 같이보낸 요청코드가 200인데,
            Log.e(TAG, "requestCode == 200인 메소드를 호출한다");

            if(resultCode == RESULT_OK) {
                Log.e(TAG, "resultCode == 호출성공");
                Log.e(TAG, "uri: "+data.getData());

                //글라이드로 화면에 이미지를 뿌린다.(DB저장은 완료버튼에서 수행)
                Glide.with(getApplicationContext()).load(data.getData()).into(img_prof);
//                Glide.with(getApplicationContext()).load(data.getData()).into(img_prof);
                Log.e(TAG, "글라이드 삽입 끝");
                //load : 선택 이미지 정보(사진을)
                //override : 이미지 가로,세로 크기(필수x)
                //into : 화면에 보여줄 이미지뷰 객체(어디뷰에 넣을거냐)

                //file path알아내는 코드
                Uri uri = data.getData(); //파일을 서버에 보내기 위해 uri로 경로를 알아낸다
                originFile = getRealPathFromUri(uri);
                Log.e(TAG, "uri : "+uri); // content://media/external/images/media/46
                Log.e(TAG, "originFile : "+originFile); // /storage/emulated/0/DCIM/Camera/20220523_173047.jpg
//                Log.e(TAG, "path : "+path); //이게 맞는 path일까?ㄴㄴ //출력:/-1/1/content://media/external/images/media/111/ORIGINAL/NONE/image/jpeg/1093861391



                //받는 곳에서 비트맵에 손 대지 말자.
                //어차피 저장되는 사진은 한 개 뿐.
                //저장하는 곳에서 비트맵 편집하기


            }
        }

    } //~onActivityResult()


    // bitmap -> string 변환
    static String BitmapToString(String TAG, Bitmap bitmap) { //쉐어드에 저장할 용도

        Log.e(TAG, "BitmapToString() 입장");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        Log.e(TAG, "BitmapToString() 퇴장");

        return temp;
    }

    private MultipartBody.Part imgToBody2 (Bitmap bitmap, String fileName){ Log.e(TAG, "imgToBody2() 입장");


        // filePath로 MultipartBody 객체를 만들어 http통신한다.
        File file = bitmapToFile(bitmap, fileName);
        Log.e(TAG, "imgToBody2 file : "+file);
        Log.e(TAG, "imgToBody2 file.getName() : "+file.getName());

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file); // 실제파일객체에 타입정한다.
        Log.e(TAG, "imgToBody2 requestFile : "+requestFile);

        MultipartBody.Part body = createFormData("uploaded_MarkerFile", "markerImg_"+file.getName(), requestFile); // body안에 여러개 집어넣음. uploaded_MarkerFile이란 이름으로 받아온 data안에 body가 있다.
        Log.e(TAG, "imgToBody2 body : "+body);
        Log.e(TAG, "getImageViewToFileResizing() 퇴장");

        return body;
    }

    private File bitmapToFile(Bitmap bitmap, String fileName) { String method = "bitmapToFile() "; Log.e(TAG, method+" 입장");


        File filesDir = getApplicationContext().getFilesDir(); // retrofit에 파일타입을 장착해야되기 때문에 file을 생성한다.
        File imageFile = new File(filesDir, fileName); // 이미 기존 imgurl이름이 .jpg_000으로 끝나서 또 .jpg를 붙일필요 없다

        Log.e(TAG, method+" filesDir :"+filesDir +
                              "\n imageFile:"+imageFile);

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);

            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }

        Log.e(TAG, method+" imageFile:"+imageFile);
        Log.e(TAG, method+" 퇴장");

        return imageFile;
    }

    //사진 용량 줄이기...느리네
    private MultipartBody.Part imgToBody(Bitmap bitmap, String originFile){ //비트맵형태로 바꿔야함

        Log.e(TAG, "imgToBody() 입장");
        String mUri = "";

        if(bitmap != null) {
            FileOutputStream fout = null;

            try {
                int MAX_IMAGE_SIZE = 900 * 1024; // max final file size // 150 * 1024 = 150kb / 너무 작으면 용량줄이는데 오래걸리고 너무 크면 통신하는데 오래걸림. 이 정도가 적당한 듯..
                int compressQuality = 100; // quality decreasing by 5 every loop. (start from 99)
                int streamLength = MAX_IMAGE_SIZE;

                File file = new File(originFile); //디렉토리로 파일화
                Log.e(TAG, "getImageViewToFileResizing() file.length() 줄이기 전 : "+file.length()); //줄이기 전

                while(streamLength >= MAX_IMAGE_SIZE){
                    fout = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, fout);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, compressQuality, fout);
                    streamLength = (int) file.length();
                    compressQuality -= 5; //?
                }

                fout.flush(); //붉어지다, 물이 쏟아지다, 울컥 솟구침
                mUri = file.getPath();
                Log.e(TAG, "getImageViewToFileResizing() ..mUri : "+mUri); //같은 값을 가리키지만 다른 상태를 표현하려고 다른 변수 사용(/storage/emulated/0/DCIM/Camera/20220523_170119)
                Log.e(TAG, "getImageViewToFileResizing() file.length() 줄인 후 : "+file.length()); //줄인 후

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally{
                if(fout!=null){
                    try{
                        fout.close();
                    }catch(Exception e){}
                }
            }
        }

        //축소한 file path(mUri) -> bitmap
        Bitmap smallBitmap = BitmapFactory.decodeFile(mUri); //선택한 경로의 사진을 비트맵으로 바꾼다.
        Log.e(TAG, "imgToBody() smallBitmap : "+smallBitmap);

        //마커합성(마커+프사)
        Log.e(TAG, "imgToBody() 마커합성bitmap bitmapEdit 전 : "+마커합성bitmap);
        마커합성bitmap = bitmapEdit(smallBitmap); //쉐어드에 마커이미지 통째로 저장(string형태)
        Log.e(TAG, "imgToBody() 마커합성bitmap bitmapEdit 후 : "+마커합성bitmap); // 이곳에서 합성하고 저장해야 쓰기 편함

        //string타입 사진
        mySmallBitmapImg = BitmapToString(TAG,마커합성bitmap); //합성사진 대입/ 통신 후 쉐어드저장


        //통신하기위한 file객체
        file_prof = new File(mUri); //경로를 통해 파일 객체 생성
        Log.e(TAG, "imgToBody() file_prof : "+file_prof); // /storage/emulated/0/Download/hiking-boots.png
        Log.e(TAG, "imgToBody() file_prof.getName() : "+file_prof.getName());
        Log.e(TAG, "imgToBody() file_prof.length() : "+file_prof.length());

        //import okhttp3.RequestBody / 타입지정
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file_prof); //실제파일객체에 타입정한다.
        Log.e(TAG, "imgToBody() requestFile객체 생성 : "+requestFile);

        //import okhttp3.MultipartBody / 요청바디이름, 파일명, 파일객체를 body라는 객체 안에 담음
        ramdomForMakeImgName = (int)(Math.random() * 1000); // 파일 이름이 같을 수 있으니까 1~100까지 랜덤숫자 붙임
        MultipartBody.Part body = createFormData("uploaded_file", "pofImg_"+ramdomForMakeImgName+"_"+file_prof.getName(), requestFile); //body안에 여러개 집어넣음
        Log.e(TAG, "imgToBody() body 생성 : "+body);
        Log.e(TAG, "imgToBody() strNickName : "+strNickName);
        Log.e(TAG, "imgToBody() body : "+body);

        Log.e(TAG, "imgToBody() imgToBody 퇴장");

        return body;
    }


    //파일의 Uri로 경로를 찾아주는 함수(복붙)
    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), uri, projection, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column);
        cursor.close();
        return result;
    }


    //정규식 패턴 - 닉네임
    public boolean isPatternNickName(String nickName) {
        //"^[가-힣|a-z|A-Z|0-9|].{2,6}$"
        ///^[가-힣a-zA-Z0-9]+$/
        //"^[가-힣ㄱ-ㅎa-zA-Z0-9._ -]{2,6}\$"
        String 패턴 = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\\d~!@#$%^&*()+|=]{"+2+","+6+"}$"; //한(자음모음ok),영(대소) 최소~최대글자

        if(Pattern.matches(패턴, nickName)) { //가나..aA..09, 2~6자리
            patternNickName = true;
            Log.e(TAG, "isPatternNickName... patternNickName = true");
        } else {
            patternNickName = false;
            Log.e(TAG, "isPatternNickName... patternNickName = false");
        }
        return patternNickName;
    }


    //뷰를 한데 선언
    public void ID() {
        img_prof = (ImageView) findViewById(R.id.img_prof); //circleImageView 진짜 프로필 사진 삽입되는 곳
        Linear_profImg = (LinearLayout) findViewById(R.id.Linear_profImg); //프로필사진이 포함된 전체 linear layout
        btn_prof = (Button) findViewById(R.id.btn_prof); //완료버튼
        et_prof_nickName = (EditText) findViewById(R.id.et_prof_nickName); //닉네임 작성
    }

}
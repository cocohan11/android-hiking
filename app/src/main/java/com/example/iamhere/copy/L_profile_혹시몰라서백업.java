//package com.example.iamhere;
//
//import static com.example.iamhere.M_main.myImg;
//import static com.example.iamhere.M_main.myName;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.loader.content.CursorLoader;
//
//import com.bumptech.glide.Glide;
//import com.example.iamhere.Interface.Profile;
//
//import java.io.File;
//import java.util.regex.Pattern;
//
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class L_profile_혹시몰라서백업 extends AppCompatActivity {
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//    //1.프로필사진
//    //linearlayout클릭시 사진앨범에서 가져오기
//
//    //2.닉네임
//    //규칙: 숫자,영어,한글만 가능.특수문자x/2~6자 ->정규식 ("^[가-힣ㄱ-ㅎa-zA-Z0-9._ -]{2,6}\$")
//    //필수값
//
//    //3.완료버튼
//    //서버에프로필사진 저장
//    //이전화면에서 받아온 email로 DB에 사진경로, 닉네임 insert하기
//    //쉐어드에 이메일저장해서 자동로그인하기 /이전 화면들에 쉐어드기록있는지 확인 후 넘기기(방법은 아직)
//
//    //4.레트로핏 클래스만들기(파라미터 뚫어서 편하게 가져다 쓸 수 있게 만들기) >>> 파라미터로 클래스이름은 못 가져와져서 그냥 복붙용으로 클래스하나 만들어 둚.
//
//    //참고
//    //만약 여기서 완료누르기 전에 어플을 꺼버린다면 로그인 후 여기화면을 띄우자.
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//    String TAG = "L_profile"; //log.e()의 첫 번째 파라미터로 클래스명 넣기
//    ImageView img_prof; //사용자 선택에 따라 변경되는 프로필사진
//    LinearLayout Linear_profImg; //프로필사진변경하려면 누르는 뷰인데 linear전체를 클릭대상으로 정함. 꺽쇠는 너무 좁으니까.
//    Button btn_prof; //완료버튼을 누르면 사진과 닉네임이 서버로 전송되어서 저장된다.
//    EditText et_prof_nickName; //정규식대로 닉네임을 정한다.
//    boolean patternNickName; //닉네임이 정규식 통과됬는가
//    private final int GET_GALLERY_IMG = 200; //intent 결과를 구분하기위한 아무값
//    String filePath; //사진경로 전역변수로 뺌
//    String email;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_lprofile);
//        ID(); //xml에서 선언한 id가져오기
//        Log.e(TAG, "onCreate");
//
//        //쉐어드로 구분하려고 했는데 그냥.. 여기로 보낼 때 intent로 값을같이 보내서 구분해야겠음
//        //intent에 실리는 값 : 사진url,닉네임
//        //다시 쉐어드에 저장하기
//
//        //3곳으로부터 여기를 거침 (email변수로 통일)
//        Intent intent = getIntent();
//
//        String kakao_UserEmail = intent.getStringExtra("kakao_UserEmail"); //mypage >> this
//        String name = intent.getStringExtra("UserNickName"); //mypage >> this
//
//        email = intent.getStringExtra("loginEmail"); //login >> this
//        if(kakao_UserEmail != null) email = kakao_UserEmail; //login >> this
//        if(email == null) email = intent.getStringExtra("UserEmail"); //join >> this
//
//        Log.e(TAG, "email : "+email);
//        Log.e(TAG, "name : "+name);
//
//        //ㅡㅡㅡㅡㅡㅡㅡㅡ
//        //프로필 처음 작성
//        //ㅡㅡㅡㅡㅡㅡㅡㅡ
//        if(name == null) {
//            Log.e(TAG, "name에 값이 없다");
//            //사진 : 기본
//            //닉네임 : 공란
//
//        //ㅡㅡㅡㅡㅡ
//        //프로필수정
//        //ㅡㅡㅡㅡㅡ
//        } else {
//            Log.e(TAG, "name에값이 있다");
//            //사진 : 기존사진 삽입
//            //닉네임 : 기존닉네임 삽입
//            Log.e(TAG, "myImg : "+myImg);
//            Glide.with(getApplicationContext()).load(myImg).into(img_prof); //닉네임도 있으면 myImg static변수도 있음
//            et_prof_nickName.setText(name); //intent로 받아온 닉네임 삽입
//
//        }
//
//
//
//
//        //ㅡㅡㅡㅡㅡㅡ
//        //1.프로필사진 : add
//        //ㅡㅡㅡㅡㅡㅡ
//        Linear_profImg.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("IntentReset")
//            @RequiresApi(api = Build.VERSION_CODES.R)
//            @Override
//            public void onClick(View view) {
//                //클릭했을 때 작게 선택창이 생기고 앨범가면 권한을물어본 뒤 권한ok면 들어가서선택가능하고 권한x면 앨범창이 꺼진다.
//                //선택된 이미지는 완료버튼에서 서버로 보내지고 db에 저장경로가 저장된다.
//                Log.e(TAG, "Linear_profImg 클릭");
//
//                //인텐트로 갤러리를 선택하는 창을 띄운다.ㅣ
//                Intent intent = new Intent();
////                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
////                Log.e(TAG, "Intent 선언 (ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)");
////                Uri uri = Uri.fromParts("package:com.example.iamhere", getApplicationContext().getPackageName(), null);
////                Log.e(TAG, "uri");
////                intent.setData(uri);
//                Log.e(TAG, "intent.setData(uri)");
//                intent.setType("image/*"); //인텐트로 이미지형태인 뭘 할거다.
//                Log.e(TAG, "Intent 이미지로 setType");
//                intent.setAction(Intent.ACTION_PICK); //이미지를 가져오도록 intent에 시킨다? //ACTION_PICK을 해야 사진선택으로 가서 다운로드도 선택가능
//                Log.e(TAG, "Intent setAction 인텐트야 갤러리에서 가져와라");
//
//
////                Log.e(TAG, "Intent setAction 런타임 권한");
//                startActivityForResult(intent, GET_GALLERY_IMG); //전달할 인텐트와 요청코드(200)를 같이 보낸다(구분을 위한 아무값)
//                Log.e(TAG, "Intent startActivityForResult() 인텐트야 너의 이름은 200이고, 받는쪽이 200번이라고 말해야 해당데이터를 주렴");
//                Log.e(TAG, "Intent startActivityForResult() 과연 권한 화면을 열 것인가");
//
//                //이렇게 해도 에러낭네~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
////                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION); //런타임 파일접근권한(여기서도 권한을 얻어야 저장소 에러가 안남)
//            }
//        });
//
//        //ㅡㅡㅡㅡㅡ
//        //2.닉네임
//        //3.완료버튼 : 프로필사진,닉네임 db,쉐어드 저장
//        //ㅡㅡㅡㅡㅡ
//        btn_prof.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //닉네임 값이 있는지 없는지//
//                //있다면 적합성검사
//                    //적합하면 닉네임 db에 저장, M_main으로 이동
//                    //부적합이면 토스트"닉네임 형식이 잘못되었습니다."
//                //없다면 토스트"닉네임을 입력해주세요"//
//
//                Log.e(TAG, "111 btn_prof 클릭");
//                //닉네임 가져오기
//                String strNickName = String.valueOf(et_prof_nickName.getText()); //사용자가 입력한 닉네임을 string값으로 변환
//                Log.e(TAG, "222 strNickName : "+strNickName);
//
//                //사진파일 가져오기
//                Log.e(TAG, "2222 filePath: "+filePath); //사진경로값 변수에 들어있는지 확인
//                //사진없으면 로그만, 사진 있으면, 저장하기
//                if(filePath == null) {
//                    Log.e(TAG, "222222 사진이 없다"); //사진경로값 변수에 들어있는지 확인
//                    filePath = "기본사진"; //null만아니게 error나니까
//                }
//
//                    File file = new File(filePath); //경로를 통해 파일 객체 생성
//                    Log.e(TAG, "222222222 file객체 생성");
//                    Log.e(TAG, "222222222222 file.getName() : "+file.getName());
//
//                    //import okhttp3.RequestBody / 타입지정
//                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file); //실제파일객체에 타입정한다.
//                    Log.e(TAG, "333 requestFile객체 생성 : "+requestFile);
//
//                    //import okhttp3.MultipartBody / 요청바디이름, 파일명, 파일객체를 body라는 객체 안에 담음
//                    MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile); //body안에 여러개 집어넣음
//                    Log.e(TAG, "333 body 생성 : "+body);
//
//                    //내가 알아내야할 것
//                    //1.파일이름
//                    //2.imageFile에 어떤 데이터타입의 데이터를 넣는지 -> String
//                    //3.파일경로를 어떻게 가져오는지
//
//                    if(!strNickName.equals("")) { //입력된 닉네임이 있다면 적합성검사
//                        Log.e(TAG, "333 닉네임에 값 존재");
//
//                        if(isPatternNickName(strNickName)) { //입력된 닉네임이 적합한지 treu/false로 리턴받는 함수
//                            Log.e(TAG, "444 정규식 적합");
//
//                            //적합하다면 실행 1.사진,닉네임 db저장 2.화면전환
//                            //1.서버에 사진, 닉네임 저장
//
//                            retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
//                                    .baseUrl("http://15.164.129.103/")    // baseUrl 등록
//                                    .addConverterFactory(GsonConverterFactory.create())
//                                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                                    .build();
//
//                            Profile file_interface = retrofit.create(Profile.class);   // 레트로핏 인터페이스 객체 구현
////                        strEmail = "rlagksdl96@naver.com";
//                            Log.e(TAG, "55555 email : "+email);
//                            Log.e(TAG, "55555 strNickName : "+strNickName);
//                            Log.e(TAG, "55555 body : "+body);
//                            Call<com.example.iamhere.Model.Profile> call = file_interface.getFile(body,email,strNickName,"ProfFunction"); //body가 최종형태 데이터이다...
//                            Log.e(TAG, "555 call 객체 생성");
//
//
//                            //네트워킹 시도
//                            call.enqueue(new Callback<com.example.iamhere.Model.Profile>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
//                                @SuppressLint("SetTextI18n")
//                                @Override
//                                public void onResponse(Call<com.example.iamhere.Model.Profile> call, Response<com.example.iamhere.Model.Profile> response) {
//                                    Log.e(TAG, "1111 onResponse 들어옴");
//
//                                    com.example.iamhere.Model.Profile result = response.body(); //response.isSuccessful안에 있으니까 response가 확인이 안되서 로그찍기위해 한 층 꺼냄냄
//                                    Log.e(TAG, "1111 onResponse result객체 생성");
//                                    assert null != result;
//                                    String 응답 = result.getResponse();
//                                    Log.e(TAG, "2222 onResponse / 응답 : "+응답);
//
//                                    //정상적으로 통신이 성공한 경우
//                                    if(response.isSuccessful()) {
//                                        Log.e(TAG, "3333 onResponse success");
//
//                                        if(응답.equals("true")) {
//                                            Log.e(TAG, "4444 응답.equals(true)");
//
//                                            //1.쉐어드에 저장(닉네임, 프로필사진 url)
//                                            SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE); //Activity.MODE_PRIVATE : 해당데이터는 해당 앱에서만 사용가능
//                                            SharedPreferences.Editor autoLoginEdit = auto.edit();
//                                            Log.e(TAG, "UserNickName : "+strNickName);
//                                            Log.e(TAG, "UserImg : "+"http://15.164.129.103/profImg/"+file.getName());
//                                            autoLoginEdit.putString("UserNickName", strNickName); //db컬럼명과 동일하게 하자
//                                            autoLoginEdit.putString("UserImg", "http://15.164.129.103/profImg/"+file.getName()); //파라미터로 받은 pw
//
//                                            autoLoginEdit.commit(); //실질 저장
//
//                                            //static변수에 변경된 값 대입
//                                            myName = strNickName;
//                                            myImg = "http://15.164.129.103/profImg/"+file.getName();
//
//                                            //ㅡㅡㅡㅡㅡㅡㅡ
//                                            //처음프로필작성
//                                            //ㅡㅡㅡㅡㅡㅡㅡ
//                                            if(name == null) {
//
//                                                //2.적합하다면 화면전환 L_profile >> M_mail
//                                                Intent intent = new Intent(getApplicationContext(), M_main.class); //로그인btn >> 지도첫화면
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ); //top빼고 아래액티비티 지움
//                                                startActivity(intent);
//                                                Log.e(TAG, "startActivity()");
//                                                Toast.makeText(getApplicationContext(), "반갑습니다. "+strNickName+"님\n닉네임은 나의정보에서 변경가능합니다.", Toast.LENGTH_LONG).show(); //첫 로그인시 뜨는 토스트라서 long선택
//
//                                                //ㅡㅡㅡㅡㅡ
//                                                //프로필수정
//                                                //ㅡㅡㅡㅡㅡ
//                                            } else { //나의 공간으로 되돌아간다.
//                                                //finish 후 생명주기로 되돌아간다.
//                                                finish();
//                                                Log.e(TAG, "finish()");
//                                            }
//
//
//                                        } else { //서버에 전송한 값이 false이면 토스트"다시 시도해주세요"(이건 뭐 에러난거라서..)
//
//                                            Log.e(TAG, "서버에 전송한 값이 false임");
//                                            Toast.makeText(getApplicationContext(), "다시 시도해주세요", Toast.LENGTH_LONG).show(); //첫 로그인시 뜨는 토스트라서 long선택
//                                            String aa = result.getResponse();
//                                            Log.e(TAG, "aaaa onResponse / aa : "+aa);
//
//                                        }
//                                    } else {
//                                        // 통신 실패
//                                        Log.e("콜.enqueue : ", "onResponse : 실패");
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<com.example.iamhere.Model.Profile> call, Throwable t) {
//                                    Log.e("onFailure : ", t.getMessage());
//                                }
//                            }); //~네트워킹 시도
//
//                        } else { //입력한 닉네임이 정규식 부적합
//                            Toast.makeText(getApplicationContext(), "형식에 맞게 입력해주세요", Toast.LENGTH_SHORT).show();
//                            Log.e(TAG, "444 정규식 부적합");
//                        }
//
//                    } else { //없다면 토스트"닉네임을 입력해주세요"
//                        Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
//                    }
//                } //~사진이 있다면
//        });
//
//
//    } //~onCreate()
//
//
//    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//
//    //intent로 보낸 data를 받는 곳(이미지)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //requestCode : 내가임의로정한 식별하기위한 값(뭘가져올지)
//        //resultCode : 호출된 액티비티에서설정한 성공/실패 값(잘가져와졌는지)
//        //data : 데이터타입이 intent이다. 호출된 액티비티에서 저장한 인텐트 값. 여기선 갤러리니까 사진.
//        Log.e(TAG, "onActivityResult 입장");
//
//        if(requestCode == GET_GALLERY_IMG) { //갤러리 열기위해 intent에 같이보낸 요청코드가 200인데,
//            Log.e(TAG, "requestCode == 200인 메소드를 호출한다");
//
//            if(resultCode == RESULT_OK) {
//                Log.e(TAG, "resultCode == 호출성공");
//                Log.e(TAG, "uri: "+data.getData());
//
//                //글라이드로 화면에 이미지를 뿌린다.(DB저장은 완료버튼에서 수행)
//                Glide.with(getApplicationContext()).load(data.getData()).into(img_prof);
//                Log.e(TAG, "글라이드 삽입 끝");
//                //load : 선택 이미지 정보(사진을)
//                //override : 이미지 가로,세로 크기(필수x)
//                //into : 화면에 보여줄 이미지뷰 객체(어디뷰에 넣을거냐)
//
//                //file path알아내는 코드
//                Uri uri = data.getData(); //파일을 서버에 보내기 위해 uri로 경로를 알아낸다
//                filePath = getRealPathFromUri(uri);
//                Log.e(TAG, "uri : "+uri);
//                Log.e(TAG, "filePath : "+filePath);
////                Log.e(TAG, "path : "+path); //이게 맞는 path일까?ㄴㄴ //출력:/-1/1/content://media/external/images/media/111/ORIGINAL/NONE/image/jpeg/1093861391
//          }
//        }
//
//    } //~onActivityResult()
//
//    //파일의 Uri로 경로를 찾아주는 함수(복붙)
//    private String getRealPathFromUri(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), uri, projection, null, null, null);
//        Cursor cursor = cursorLoader.loadInBackground();
//        int column = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String result = cursor.getString(column);
//        cursor.close();
//        return result;
//    }
//
//
//    //정규식 패턴 - 닉네임
//    public boolean isPatternNickName(String nickName) {
//        //"^[가-힣|a-z|A-Z|0-9|].{2,6}$"
//        ///^[가-힣a-zA-Z0-9]+$/
//        //"^[가-힣ㄱ-ㅎa-zA-Z0-9._ -]{2,6}\$"
//        if(Pattern.matches("^[가-힣a-zA-Z0-9]{2,6}$", nickName)) { //이메일 정규식 : 숫자/문자 + @ + 숫자/문자
//            patternNickName = true;
//            Log.e(TAG, "isPatternNickName... patternNickName = true");
//        } else {
//            patternNickName = false;
//            Log.e(TAG, "isPatternNickName... patternNickName = false");
//        }
//        return patternNickName;
//    }
//
//
//    //뷰를 한데 선언
//    public void ID() {
//        img_prof = (ImageView) findViewById(R.id.img_prof); //circleImageView 진짜 프로필 사진 삽입되는 곳
//        Linear_profImg = (LinearLayout) findViewById(R.id.Linear_profImg); //프로필사진이 포함된 전체 linear layout
//        btn_prof = (Button) findViewById(R.id.btn_prof); //완료버튼
//        et_prof_nickName = (EditText) findViewById(R.id.et_prof_nickName); //닉네임 작성
//    }
//
//}
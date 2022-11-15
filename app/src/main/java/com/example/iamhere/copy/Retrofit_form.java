package com.example.iamhere.copy;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.Toast;

import com.example.iamhere.Interface.Join;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit_form {

    /*
    public void isRetrofit_form() {
        //Retrofit 인스턴스 생성
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://15.164.129.103/")    // baseUrl 등록
                .addConverterFactory(GsonConverterFactory.create())  //http통신시에 주고받는 데이터형태를 변환시켜주는 컨버터를 지정한다. Gson, Jackson 등이 있다. Gson 변환기 등록
                .build();
        Log.e(TAG, "ㄱ 2222 레트로핏 객체 생성 ");

        com.example.iamhere.Interface.Join Join_interface = retrofit.create(Join.class);   // 레트로핏 인터페이스 객체 구현
        Call<com.example.iamhere.Model.Join> call = Join_interface.getOverlap(strEmail,"pw"); //인터페이스 객체를 이용해 인터페이스에서 정의한 함수를 호출하면 Call 객체를 얻을 수 있다. //에러났던 이유:DataClass를 참조하는데 interface에서 불러올 때 변수가 없어서.
        Log.e(TAG, "ㄱ 3333 레트로핏 객체에 모델 장착 ");

        //네트워킹 시도
        call.enqueue(new Callback<com.example.iamhere.Model.Join>() { //enqueue : 비동기식 통신을 할 때 사용/ execute: 동기식
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<com.example.iamhere.Model.Join> call, Response<com.example.iamhere.Model.Join> response) {

                com.example.iamhere.Model.Join result = response.body(); //response.isSuccessful안에 있으니까 response가 확인이 안되서 로그찍기위해 한 층 꺼냄냄
                assert result != null;
                String resOverlap = result.getResponse();
                Log.e(TAG, "ㄱ 4444 onResponse / resOverlap: "+resOverlap);

                //정상적으로 통신이 성공한 경우
                if(response.isSuccessful()) {
                    Log.e(TAG, "ㄱ 5555 onResponse success");

                    if(resOverlap.equals("false")) { //이메일중복 통과. 값없음
                        Log.e(TAG, "ㄱ 6666 resOverlap.equals(false)");
                        Log.e(TAG, "ㄱ 6666 최종으로 이메일 보내는 곳!!!");
                        ThreadSendMail(); //실제 이메일 보내는 함수

                    } else { //이메일중복임. 기존값있음.
                        Log.e(TAG, "ㄱ 7777 resOverlap.equals(true)");
`                        Toast.makeText(getApplicationContext(), "이미 가입된 이메일입니다.", Toast.LENGTH_SHORT).show();
`                    }

                } else {
                    // 통신 실패
                    Log.e("콜.enqueue : ", "onResponse : 실패");
                }
            }

            @Override
            public void onFailure(Call<com.example.iamhere.Model.Join> call, Throwable t) {
                Log.e("onFailure : ", t.getMessage());
            }
        }); //~네트워킹 시도
    }
    */
}

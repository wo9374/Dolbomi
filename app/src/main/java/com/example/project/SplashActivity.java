package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        try{
            Thread.sleep(2500); //2.5초 시간동안 실행을 중지
        }catch (InterruptedException e){
            e.printStackTrace(); //에러 메세지의 발생 근원지를 찾아 단계별로 에러 출력
        }

        startActivity(new Intent(this, LoginActivity.class)); //로그인 액티비티 실행
        finish(); //스플래쉬 액티비티 종료
    }
}

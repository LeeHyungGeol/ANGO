package com.e.ango;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(3000);// 3초 동안 스플래시 이미지를 보여줌
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}
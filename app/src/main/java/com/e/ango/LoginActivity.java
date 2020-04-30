package com.e.ango;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

import com.e.ango.API.Play.Response;
import com.e.ango.Login.LoginTask;

import com.e.ango.Recommend.RecommendTask;
import com.e.ango.Response.ResponseDto;

public class LoginActivity extends AppCompatActivity {
    TextView textView_signUp;
    Button button_login;
    EditText editText_id, editText_password;
    Boolean flag;
    ResponseDto loginResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText_id = (EditText) findViewById(R.id.editText_userID);
        editText_password = (EditText) findViewById(R.id.editText_userPW);
        button_login = (Button) findViewById(R.id.button_login);
        textView_signUp = (TextView) findViewById(R.id.textView_signUp);

        textView_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.i("?g", e.toString());
                }
                finish();
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = editText_id.getText().toString();
                String pass = editText_password.getText().toString();
                if (ID.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        loginResponse = new LoginTask(ID, pass, "LoginAccount").execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    System.out.println("LOGINACCOUNT : " + loginResponse.getResponse_msg());
                    System.out.println("AVAILABILITY : " + loginResponse.getAvailability());

                    //availability == 0 > 회원가입은 했으나 선호도조사 안 함
                    if(loginResponse.getAvailability() == 0 && loginResponse.getResponse_msg().equals("LoginAccount_success")) {
                        Intent intent = new Intent(LoginActivity.this, SurveyActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (loginResponse.getAvailability() == 1 && loginResponse.getResponse_msg().equals("LoginAccount_success")){
                        Intent intent = new Intent(LoginActivity.this, SelectActivity.class);
                        startActivity(intent);
                        finish();
                    } else if(loginResponse.getResponse_msg().equals("LoginAccount_fail")){
                        Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });

    }

}
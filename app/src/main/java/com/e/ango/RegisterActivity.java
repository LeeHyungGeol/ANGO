package com.e.ango;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

import com.e.ango.Login.RegisterTask;
import com.e.ango.Recommend.RecommendTask;
import com.e.ango.Response.ResponseDto;

import static com.e.ango.Login.LoginTask.token;

public class RegisterActivity  extends AppCompatActivity {

    EditText editText_id, editText_password, editText_userName;
    Button button_id_check, button_register;
    Boolean flag = null;

    String toastMessage;
    ResponseDto registerResponse;

    String ID, pass, name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        editText_id = (EditText) findViewById(R.id.editText_userID);
        editText_password = (EditText) findViewById(R.id.editText_userPW);
        editText_userName = (EditText) findViewById(R.id.editText_userName);
        button_id_check = (Button) findViewById(R.id.button_idCheck);
        button_register = (Button) findViewById(R.id.button_register);

        //회원가입 버튼 비활성화
        button_register.setEnabled(false);

        try {
            button_id_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ID = editText_id.getText().toString();

                    try {

                        System.out.println("IDDDDDDDDDDDDDD : " + ID + " ? " + editText_id.getText().toString());
                        registerResponse = new RegisterTask(ID, "CheckAccount").execute().get();

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //중복 아이디 검사
                    if(registerResponse.getResponse_msg().equals("CheckAccount_success")) {
                        toastMessage = "사용 가능한 아이디 입니다.";
                        //중복되지 않은 아이디일 경우 회원가입 버튼 활성
                        button_register.setEnabled(true);
                    }
                    else if (registerResponse.getResponse_msg().equals("CheckAccount_fail"))
                        toastMessage = "중복된 아이디 입니다.";
                    else if(ID.isEmpty()) toastMessage = "아이디를 입력하세요.";
                    Toast.makeText(RegisterActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                }
            });

            button_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ID = editText_id.getText().toString();
                    pass = editText_password.getText().toString();
                    name = editText_userName.getText().toString();

                    if (ID.isEmpty() || pass.isEmpty() || name.isEmpty()) {
                        System.out.println("1111111111111111111");
                        Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        try {
                            registerResponse = new RegisterTask(ID, pass, name, "JoinAccount").execute().get();

                            if (registerResponse.getResponse_msg().equals("JoinAccount_success")) {
                                token = registerResponse.getToken();
                                if (token != null) {
                                    Intent intent = new Intent(RegisterActivity.this, SurveyActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                System.out.println("22222222222222");
                                Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            System.out.println("!");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            System.out.println("!!");
                        }

                        finish();
                    }
                }
            });
//            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
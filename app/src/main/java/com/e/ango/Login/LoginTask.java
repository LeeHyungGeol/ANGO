package com.e.ango.Login;

import android.os.AsyncTask;
import android.util.Log;

import com.e.ango.Request.RequestDto;
import com.e.ango.Request.UserDto;
import com.e.ango.Response.ResponseDto;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LoginTask extends AsyncTask<Void, Void, ResponseDto> {

    public static String ip = "192.168.1.5"; //자신의 IP번호
    public static String serverip = "http://" + ip + ":8090/final_ango/Dispacher2"; // 연결할 jsp주소
    Boolean flag = null; // 선호도 조사 했는지 안했는지
    //null = 로그인 실패
    //false = 설문조사 안함

    private RequestDto requestDto = new RequestDto();
    private ResponseDto loginResponse;
    static public String token;
    private com.e.ango.Request.UserDto userDto;

    public LoginTask(String userId, String userPass, String request_msg) {
        userDto = new UserDto(userId, userPass);
        requestDto.setUser(userDto);
        requestDto.setRequest_msg(request_msg);
    }

    @Override
    protected ResponseDto doInBackground(Void... voids) {
        try {
            String str;
            URL url = new URL(serverip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-SearchAreaWeatherType", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");

            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            Gson gson = new Gson();
            osw.write(gson.toJson(requestDto));
            osw.flush();

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }

                loginResponse = gson.fromJson(buffer.toString(), ResponseDto.class);
                token = loginResponse.getToken();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return flag;
        return loginResponse;
    }

    @Override
    protected void onPostExecute(ResponseDto s) {
        super.onPostExecute(s);
    }

    public String getToken(){ return token; }
}
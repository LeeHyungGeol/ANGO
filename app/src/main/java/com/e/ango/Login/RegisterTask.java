package com.e.ango.Login;

import android.os.AsyncTask;
import android.util.Log;

import com.e.ango.API.Play.Response;
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
import java.net.ProtocolException;
import java.net.URL;


import static com.e.ango.Login.LoginTask.serverip;
import static com.e.ango.Login.LoginTask.token;

public class RegisterTask extends AsyncTask<Void, Void, ResponseDto> {

    //public static String ip = "172.16.10.37"; //자신의 IP번호
    //String serverip = "http://" + ip + ":8090/final_ango/Dispacher2\""; // 연결할 jsp주소
    String response_msg;
    Boolean flag = true;

    RequestDto requestDto = new RequestDto();
    com.e.ango.Request.UserDto userDto;
    static public ResponseDto registerResponse;

    //Join
    public RegisterTask(String id, String pass, String name, String request_msg){
        userDto = new UserDto(id, pass, name);
        requestDto.setUser(userDto);
        requestDto.setRequest_msg(request_msg);
    }
    //Check
    public RegisterTask(String id, String request_msg){
        userDto = new UserDto(id);
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

            if(conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }

                registerResponse = gson.fromJson(buffer.toString(), ResponseDto.class);
                token = registerResponse.getToken();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return registerResponse;
    }

    @Override
    protected void onPostExecute(ResponseDto flag) {
        super.onPostExecute(flag);
    }
}

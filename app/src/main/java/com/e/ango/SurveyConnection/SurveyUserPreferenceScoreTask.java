package com.e.ango.SurveyConnection;

import android.os.AsyncTask;

import com.e.ango.Request.PreferDto;
import com.e.ango.Request.RequestDto;
import com.e.ango.Response.ResponseDto;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import static com.e.ango.Login.LoginTask.token;
//유저 선호도 점수 전송
import static com.e.ango.Login.LoginTask.ip;

public class SurveyUserPreferenceScoreTask extends AsyncTask<Void, Void, Boolean> {

    private String URL_ADDRESS = "http://" + ip + ":8090/final_ango/Dispacher2";  //주소 변경
    private String request_msg = "InsertUserPreference";
    String str;
    Boolean flag;
    RequestDto requestDto;

    public SurveyUserPreferenceScoreTask(RequestDto requestDto) {
        this.requestDto = requestDto;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            URL Url = new URL(URL_ADDRESS); // URL화 한다.
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // URL을 연결한 객체 생성.
            conn.setRequestProperty("Content-SearchAreaWeatherType", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST"); // get방식 통신


            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            requestDto.setToken(token);/////////////////////////////////////////////////////////////////////////////////
            requestDto.setRequest_msg(request_msg);/////////////////////////////////////////////////////////////////////
            Gson gson = new Gson();

            osw.write(gson.toJson(requestDto));
            osw.flush();

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                    System.out.println(str);
                }

                ResponseDto responseDto = gson.fromJson(buffer.toString(), ResponseDto.class);

                //유저 선호도 점수 전송에 성공했을때 flag를 true로 바꾸고 전송
                if (responseDto.getResponse_msg().equals("InsertUserPreference_success")) {
                    System.out.println(responseDto.getResponse_msg());
                    flag = true;
                } else {//유저 선호도 점수 전송에 실패했을때 flag를 true로 바꾸고 전송
                    System.out.println(responseDto.getResponse_msg());
                    flag = false;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }

    @Override
    protected void onPostExecute(Boolean aVoid) {
        super.onPostExecute(aVoid);
    }

}


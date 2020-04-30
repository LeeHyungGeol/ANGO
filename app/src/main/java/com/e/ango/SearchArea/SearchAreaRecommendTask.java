package com.e.ango.SearchArea;

import android.os.AsyncTask;

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

import static com.e.ango.Login.LoginTask.serverip;
import static com.e.ango.Login.LoginTask.token;


public class SearchAreaRecommendTask extends AsyncTask<Void, Void, ResponseDto> {


    String weather_type;
    ResponseDto responseDto;
    RequestDto requestDto;

    public SearchAreaRecommendTask(String weather_type) {//String request_msg, String weather_type, String token) {
        this.weather_type = weather_type;
    }

    @Override
    protected ResponseDto doInBackground(Void... voids) {
        try {
            String str;
            URL url = new URL(serverip);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-SearchAreaWeatherType", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST"); // GET??
            conn.setDoOutput(true); // 쓰기모드 지정
            conn.setDoInput(true); // 읽기모드 지정
            conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
            conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정

            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            requestDto = new RequestDto("RecommendCategory", weather_type, token);

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

                responseDto = gson.fromJson(buffer.toString(), ResponseDto.class);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("RECOMMEND NULL");
        }
        return responseDto;
    }

}



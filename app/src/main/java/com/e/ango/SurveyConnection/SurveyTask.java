package com.e.ango.SurveyConnection;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


import com.e.ango.Request.RequestDto;
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

import static com.e.ango.Login.LoginTask.token;

import static com.e.ango.Login.LoginTask.ip;

public class SurveyTask extends AsyncTask<Void, Void, ResponseDto> {
    private ProgressDialog pDialog;

    private String URL_ADDRESS = "http://" + ip + ":8090/final_ango/Dispacher2";  //서버 주소
    private String request_msg = "GetQuestionAndCategory";
    private String str;
    private ResponseDto responseDto;

    @Override
    protected ResponseDto doInBackground(Void... voids) {
        try {

            URL Url = new URL(URL_ADDRESS); // URL화 한다.
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // URL을 연결한 객체 생성.
            conn.setRequestProperty("Content-SearchAreaWeatherType", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST"); // get방식 통신

            System.out.println(URL_ADDRESS);
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            RequestDto requestDto = new RequestDto(request_msg, token);
            //////서버한테 questionlist 와 categorylist 요청
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
                if (responseDto.getResponse_msg().equals("GetQuestion_success") && responseDto.getResponse_msg().equals("GetCategory_success")) {
                    System.out.println(responseDto.getQuestion_list().size());
                    System.out.println(responseDto.getPrefer_list().size());

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }
            }

        } catch (MalformedURLException | ProtocolException exception) {
            exception.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return responseDto;
    }

    @Override
    protected void onPostExecute(ResponseDto aVoid) {
        super.onPostExecute(aVoid);

    }
}

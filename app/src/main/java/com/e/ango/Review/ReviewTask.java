package com.e.ango.Review;

import android.app.ProgressDialog;
import android.os.AsyncTask;

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

import static com.e.ango.Login.LoginTask.serverip;


public class ReviewTask extends AsyncTask<Void, Void, ResponseDto> {

    private ProgressDialog pDialog;

    //public static String ip = "172.16.15.181"; //자신의 IP번호
    //public static String serverip = "http://" + ip + ":8090/final_ango/Dispacher2"; // 연결할 jsp주소

    String str;
    ResponseDto responseDto;
    RequestDto requestDto;

    public ReviewTask(RequestDto requestDto) {
        this.requestDto = requestDto;
    }

    @Override
    protected ResponseDto doInBackground(Void... voids) {
        try {

            URL Url = new URL(serverip); // URL화 한다.
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // URL을 연결한 객체 생성.
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST"); // get방식 통신


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

                System.out.println("buffer : " + buffer);
                responseDto = gson.fromJson(buffer.toString(), ResponseDto.class);
                System.out.println("task :" + responseDto.toString());

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

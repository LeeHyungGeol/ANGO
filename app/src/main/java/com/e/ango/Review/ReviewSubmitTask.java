package com.e.ango.Review;

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

public class ReviewSubmitTask extends AsyncTask<Void, Void, Boolean> {
    // public static String ip = "172.16.15.181"; //자신의 IP번호
    //public static String serverip = "http://" + ip + ":8090/final_ango/Dispacher2"; // 연결할 jsp주소


    RequestDto requestDto;


    //ArrayList<ReviewVO> reviewVO = new ArrayList<>();
    String str;
    Boolean flag;

    public ReviewSubmitTask(RequestDto requestDto) {
        this.requestDto = requestDto;
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
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
                    System.out.println(str);
                }

                ResponseDto responseDto = gson.fromJson(buffer.toString(), ResponseDto.class);


                if (responseDto.getResponse_msg().equals("InsertUserReview_success")) {
                    System.out.println(responseDto.getResponse_msg());
                    flag = true;
                } else {
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

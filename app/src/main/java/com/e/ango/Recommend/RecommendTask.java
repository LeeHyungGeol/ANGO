package com.e.ango.Recommend;

import android.net.http.SslCertificate;
import android.os.AsyncTask;

import com.e.ango.API.Play.PlayObject;
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

import static com.e.ango.Login.LoginTask.serverip;
import static com.e.ango.Login.LoginTask.token;

public class RecommendTask extends AsyncTask<Void, Void, ArrayList> {

    //String serverip = "http://" + ip + ":8090/final_ango/Dispacher2";//"http://" + ip + ":8080/ango/Dispacher"; // 연결할 jsp주소
    ArrayList<PreferDto> preferDtos;

    String weather_type;
    ArrayList<PlayObject> originalPlayObjects;
    ArrayList<PlayObject> userPreferencePlayObjects = new ArrayList<>();

    RequestDto requestDto;

    public RecommendTask(String weather_type, ArrayList<PlayObject> originalPlayObjects){//String request_msg, String weather_type, String token) {
        this.weather_type = weather_type;
        this.originalPlayObjects = originalPlayObjects;
    }

    @Override
    protected ArrayList<PlayObject> doInBackground(Void... voids) {
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

                ResponseDto responseDto = gson.fromJson(buffer.toString(), ResponseDto.class);
                

                if (responseDto.getResponse_msg().equals("RecommendCategory_success")) {
                    preferDtos = responseDto.getPrefer_list();

                    if(originalPlayObjects != null) {
                        for (int i = 0; i < originalPlayObjects.size(); i++) {
                            if(preferDtos.size() == 0) System.out.println("RECOMMEND PREFERDTO : " + preferDtos.size());

                            for (int j = 0; j < preferDtos.size(); j++) {

                                if(originalPlayObjects.get(i).getCat2() == null||
                                        originalPlayObjects.get(i).getCat3() == null){
                                    continue;
                                }
                                else if (originalPlayObjects.get(i).getCat2().equals(preferDtos.get(j).getCg_id()) ||
                                        originalPlayObjects.get(i).getCat3().equals(preferDtos.get(j).getCg_id()))
                                {
                                    originalPlayObjects.get(i).setCategoryName(preferDtos.get(j).getCategory_nm());
                                    originalPlayObjects.get(i).setPreferCategoryId(preferDtos.get(j).getCg_id());
                                    System.out.println("preferDtos.get(j).getCg_id()" + preferDtos.get(j).getCg_id());
                                    System.out.println("originalPlayObjects.get(i).getPreferCategoryId() : " + originalPlayObjects.get(i).getPreferCategoryId());
                                    userPreferencePlayObjects.add(originalPlayObjects.get(i));
                                }
                            }
                        }
                    }
                    else System.out.println("RECOMMEND ORIGINAL : " + userPreferencePlayObjects.size());
                } else System.out.println("!!!!!!!!!!!!!!!!");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("RECOMMEND NULL");
        }
        return userPreferencePlayObjects;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
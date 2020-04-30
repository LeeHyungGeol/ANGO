package com.e.ango.SearchArea;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SearchWeatherTask extends AsyncTask<Void, Void, SearchAirWeatherObject> {
    OkHttpClient client = new OkHttpClient();
    String parsingResult = null;
    String weatherType = "";
    SearchAirWeatherObject searchAirWeatherObject;

    String date;//날짜 받아온 결과
    String time;//시간 받아온 결과
    String xCoordinate;
    String yCoordinate;
    String districtResult;

    public SearchWeatherTask(String date, String time, String xCoordinate, String yCoordinate, String districtResult) {
        this.date = date;
        this.time = time;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.districtResult = districtResult;
    }

    @Override
    protected SearchAirWeatherObject doInBackground(Void... params) {
        String result = null;
        String strUrl ="https://api.darksky.net/forecast/2d32bcfe938dc43f9f32db76ebf8c449/"
                          + xCoordinate + "," + yCoordinate + ","
                           + date + "T" + time + "?exclude=hourly,flags";

        try{
            Request request = new Request.Builder()
                    .url(strUrl)
                    .build();

            Response response = client.newCall(request).execute();

            Gson gson = new Gson();
            SearchWeatherDto searchWeatherDto = gson.fromJson(response.body().string(), SearchWeatherDto.class);
            ArrayList<Datum> datums = searchWeatherDto.daily.data;
            Datum d = datums.get(0);

            searchAirWeatherObject = new SearchAirWeatherObject(
                    districtResult, null, null, date, null,"서울",
                    searchWeatherDto.currently.time, searchWeatherDto.currently.icon, searchWeatherDto.currently.temperature, searchWeatherDto.currently.humidity,
                    d.time, d.icon, d.humidity, d.apparentTemperatureMax, d.apparentTemperatureMin, searchWeatherDto.currently.windSpeed
            );

            searchAirWeatherObject.faherenheitToCelcius();//화씨온도를 섭씨온도로
            searchAirWeatherObject.humidity();//습도 * 100해주기


            SearchAreaWeatherType searchAreaWeatherType = new SearchAreaWeatherType(searchAirWeatherObject);//날씨 타입
            weatherType = searchAreaWeatherType.WeatherType();
            searchAirWeatherObject.setWeatherType(weatherType);//날씨 타입을 object에 넣어주기
            System.out.println(weatherType);


            // 예를 들어 2019년 11월 22일 14:00:00 를 검색했을때
            //그시각의 날씨(아이콘 : 맑음, 흐림, 비 등등), 그 시각의 온도, 그 시각의 습도,
            // 그 날 전체를 통틀은 날씨(아이콘 : 맑음, 흐림, 비 등등), 그날의 최고 온도, 그날의 최저 온도, 그날의 습도
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return searchAirWeatherObject;
    }

    @Override
    protected void onPostExecute(SearchAirWeatherObject ob) {
        super.onPostExecute(ob);

        //SearchAreaActivity.textView_weatherPasredData.setText(this.parsingResult);
    }
}

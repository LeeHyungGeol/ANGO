package com.e.ango.SearchArea;

import android.os.AsyncTask;

import com.e.ango.API.Air.AirDto;
import com.e.ango.API.Air.List;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;


public class SearchAirWeatherTask extends AsyncTask<Void, Void, SearchAirWeatherObject> {
    OkHttpClient client = new OkHttpClient();
    String parsingResult = null;
    String weatherType = "";
    SearchAirWeatherObject searchAirWeatherObject;
    String date;//날짜 받아온 결과
    String time;//시간 받아온 결과
    String xCoordinate;
    String yCoordinate;
    String districtResult;

    public SearchAirWeatherTask(String date, String time, String xCoordinate, String yCoordinate, String districtResult) {
        this.date = date;
        this.time = time;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.districtResult = districtResult;
    }

    @Override
    protected SearchAirWeatherObject doInBackground(Void... voids) {
        String result = null;
        String urlA = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?sidoName=%EC%84%9C%EC%9A%B8&searchCondition=DAILY&pageNo=1&numOfRows=25&ServiceKey=w%2BvIOXxlMW05eJcl2Fw894grerR3LUGL1LepRRDEjPN1ntgk2i2%2FV00sSzbn7QZAnF5iqz2WG%2BiDxWnf2tdy4A%3D%3D&_returnType=json";
        String urlW ="https://api.darksky.net/forecast/2d32bcfe938dc43f9f32db76ebf8c449/"
                + xCoordinate + "," + yCoordinate + ","
                + date + "T" + time + "?exclude=hourly,flags";
        try{
            //clientAir.setConnectTimeout(30, TimeUnit.SECONDS); // connect timeout
            //clientAir.setReadTimeout(30, TimeUnit.SECONDS);    // socket timeout

            Request requestA = new Request.Builder()
                    .url(urlA)
                    .build();
            Request requestW = new Request.Builder()
                    .url(urlW)
                    .build();

            okhttp3.Response responseA = client.newCall(requestA).execute();
            okhttp3.Response responseW = client.newCall(requestW).execute();
            Gson gson = new Gson();

            SearchWeatherDto searchWeatherDto = gson.fromJson(responseW.body().string(), SearchWeatherDto.class);
            ArrayList<Datum> datums = searchWeatherDto.daily.data;
            Datum d = datums.get(0);

            searchAirWeatherObject = new SearchAirWeatherObject(
                    null, null, null, null, null,null,
                    searchWeatherDto.currently.time, searchWeatherDto.currently.icon, searchWeatherDto.currently.temperature, searchWeatherDto.currently.humidity,
                    d.time, d.icon, d.humidity, d.apparentTemperatureMax, d.apparentTemperatureMin, searchWeatherDto.currently.windSpeed
            );


            AirDto airGuVO= null;
            if (responseA.body() != null)
                airGuVO = gson.fromJson(responseA.body().string(), AirDto.class);
            ArrayList<List> lists = airGuVO.list;

            for(int i = 0; i < lists.size(); i ++){
                List l = lists.get(i);
                if(l.cityName.equals(districtResult)){
                    System.out.println(l.cityName);
                    searchAirWeatherObject.airCityName = l.cityName;
                    searchAirWeatherObject.aircityNameEng = l.cityNameEng;
                    searchAirWeatherObject.airDataTime = l.dataTime;
                    searchAirWeatherObject.airPm10Value = l.pm10Value;
                    searchAirWeatherObject.airSidoName = l.sidoName;
                }
            }

            searchAirWeatherObject.faherenheitToCelcius();//화씨 온도를 섭씨 온도로 바꾸기
            searchAirWeatherObject.humidity();//습도 * 100

            SearchAreaWeatherType searchAreaWeatherType = new SearchAreaWeatherType(searchAirWeatherObject);//날씨 타입
            weatherType = searchAreaWeatherType.WeatherType();
            searchAirWeatherObject.setWeatherType(weatherType);//object에 날씨 타입 넣기
            System.out.println(weatherType);

       // 예를 들어 2019년 11월 22일 14:00:00 를 검색했을때
            //그시각의 날씨(아이콘 : 맑음, 흐림, 비 등등), 그 시각의 온도, 그 시각의 습도,
            // 그 날 전체를 통틀은 날씨(아이콘 : 맑음, 흐림, 비 등등), 그날의 최고 온도, 그날의 최저 온도, 그날의 습도

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
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

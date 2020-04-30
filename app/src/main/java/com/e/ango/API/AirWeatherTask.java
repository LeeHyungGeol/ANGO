package com.e.ango.API;

import android.os.AsyncTask;

import com.e.ango.API.Air.AirDto;
import com.e.ango.API.Air.List;
import com.e.ango.API.Weather.CurWeatherDto;
import com.e.ango.API.Weather.Currently;
import com.e.ango.CurrentLocationActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;


public class AirWeatherTask extends AsyncTask<Void, Void, String> {
    OkHttpClient client = new OkHttpClient();

    String longitude; // 경도 127
    String latitude; // 위도 36.5

    private AirWeatherObject originalAirWeatherObject;
    private String weather_type;
    private String str_Humidity, str_Temperature, str_Weather, str_Air;

    private String cityName;
    double pm10Value;

    public AirWeatherTask(String latitude, String longitude, String cityName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.cityName = cityName;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String urlA = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?sidoName=%EC%84%9C%EC%9A%B8&searchCondition=DAILY&pageNo=1&numOfRows=25&ServiceKey=w%2BvIOXxlMW05eJcl2Fw894grerR3LUGL1LepRRDEjPN1ntgk2i2%2FV00sSzbn7QZAnF5iqz2WG%2BiDxWnf2tdy4A%3D%3D&_returnType=json";
        String urlW = "https://api.darksky.net/forecast/2d32bcfe938dc43f9f32db76ebf8c449/" + latitude + "," + longitude + "?exclude=hourly,daily";


        try {
            Request requestA = new Request.Builder()
                    .url(urlA)
                    .build();
            Request requestW = new Request.Builder()
                    .url(urlW)
                    .build();

            okhttp3.Response responseA = client.newCall(requestA).execute();
            okhttp3.Response responseW = client.newCall(requestW).execute();

            Gson gson = new Gson();
            AirDto airDto = gson.fromJson(responseA.body().string(), AirDto.class);
            CurWeatherDto curWeatherDto = gson.fromJson(responseW.body().string(), CurWeatherDto.class);

            ArrayList<List> lists = airDto.list;
            Currently currently = curWeatherDto.currently;

            for (int i = 0; i < lists.size(); i++) {
                List list = lists.get(i);

                if(list.cityName.equals(cityName)) {
                    System.out.println("PLZ : " + list.cityName + " ? " + cityName);
                    System.out.println("PLZZ : " + list.pm10Value);

                    currently.temperature = (currently.temperature - 32) / 1.8;
                    currently.humidity = currently.humidity * 100;

                    if(list.pm10Value == null || list.pm10Value.equals("0.0") || list.pm10Value.isEmpty()) pm10Value = 0.0;
                    else pm10Value = Double.parseDouble(list.pm10Value);

                    originalAirWeatherObject = new AirWeatherObject(
                            list.sidoName, list.cityName, list.cityNameEng, list.dataTime, pm10Value,  // air
                            currently.time, currently.summary, currently.icon, currently.temperature, currently.humidity, currently.windSpeed// weather
                    );

                    originalAirWeatherObject.humidity = (int) (originalAirWeatherObject.humidity + 0.5); // 반올림
                    if(originalAirWeatherObject.humidity < 90) str_Humidity = "상쾌";
                    else str_Humidity = "불쾌";
                    originalAirWeatherObject.temperature = (int) (originalAirWeatherObject.temperature + 0.5); // 반올림
                    str_Temperature = Double.toString(originalAirWeatherObject.getTemperature());
                    str_Weather = originalAirWeatherObject.getIcon();
                    if(80 < originalAirWeatherObject.pm10Value && originalAirWeatherObject.pm10Value <= 600)
                        str_Air = "나쁨";
                    else str_Air = "좋음";
                }

            }

            CurrentLocationWeatherType currentLocationWeatherType = new CurrentLocationWeatherType(originalAirWeatherObject);
            weather_type = currentLocationWeatherType.getWeatherType();
            System.out.println();
            System.out.println(weather_type);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("aw1");
        } catch (NumberFormatException e){
            e.printStackTrace();
            System.out.println("aw2");
        }
        return weather_type;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        CurrentLocationActivity.tv_Humidity.setText("습도 : " + str_Humidity);//Math.round(Double.parseDouble(this.str_Humidity) * 100)/100.0);
        CurrentLocationActivity.tv_Temperature.setText("온도 : " + str_Temperature);//Math.round(Double.parseDouble(this.str_Temperature) * 100) / 100.0);
        CurrentLocationActivity.tv_Weather.setText("날씨 : " + str_Weather);
        CurrentLocationActivity.tv_Air.setText("자외선 지수 : 5");

    }
}
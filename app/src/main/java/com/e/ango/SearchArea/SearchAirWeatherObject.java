package com.e.ango.SearchArea;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SearchAirWeatherObject {

    String airCityName;
    String aircityNameEng;
    String airPm25Value;
    String airDataTime;
    String airPm10Value;
    String airSidoName;

    long currentlyTime;
    String currentlyIcon;
    double currentlyTemperature;
    double currentlyHumidity;
    double currentlyWindSpeed;

    long dailyTime;
    String dailyIcon;
    double dailyHumidity;
    double dailyApparentTemperatureMin;
    double dailyApparentTemperatureMax;

    String weatherType;

    public SearchAirWeatherObject() {
    }

    public SearchAirWeatherObject(String airCityName, String aircityNameEng, String airPm25Value, String airDataTime, String airPm10Value, String airSidoName, long currentlyTime, String currentlyIcon, double currentlyTemperature, double currentlyHumidity, long dailyTime, String dailyIcon, double dailyHumidity, double dailyApparentTemperatureMax, double dailyApparentTemperatureMin, double currentlyWindSpeed) {
        this.airCityName = airCityName;
        this.aircityNameEng = aircityNameEng;
        this.airPm25Value = airPm25Value;
        this.airDataTime = airDataTime;
        this.airPm10Value = airPm10Value;
        this.airSidoName = airSidoName;
        this.currentlyTime = currentlyTime;
        this.currentlyIcon = currentlyIcon;
        this.currentlyTemperature = currentlyTemperature;
        this.currentlyHumidity = currentlyHumidity;
        this.dailyTime = dailyTime;
        this.dailyIcon = dailyIcon;
        this.dailyHumidity = dailyHumidity;
        this.dailyApparentTemperatureMax = dailyApparentTemperatureMax;
        this.dailyApparentTemperatureMin = dailyApparentTemperatureMin;
        this.currentlyWindSpeed = currentlyWindSpeed;
    }

    public String unixTimeToCurrentlytime() {
        Date date = new Date(this.currentlyTime*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        // GMT(그리니치 표준시 +9 시가 한국의 표준시
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate);
        return formattedDate;
    }

    public String unixTimeToDailytime() {
        Date date = new Date(this.dailyTime*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        // GMT(그리니치 표준시 +9 시가 한국의 표준시
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate);
        return formattedDate;
    }

    public void faherenheitToCelcius() {
        if(this.currentlyTemperature > 0) {
            this.currentlyTemperature = (int)Math.round(((this.currentlyTemperature - 32) / 1.8) + 0.5) ;
        }
        else {
            this.currentlyTemperature = (int)Math.round(((this.currentlyTemperature - 32) / 1.8) - 0.5);
        }

        if(this.dailyApparentTemperatureMax > 0) {
            this.dailyApparentTemperatureMax = (int) Math.round(((this.dailyApparentTemperatureMax - 32) / 1.8) + 0.5);
        }
        else {
            this.dailyApparentTemperatureMax = (int) Math.round(((this.dailyApparentTemperatureMax - 32) / 1.8) - 0.5);
        }

        if(this.dailyApparentTemperatureMin > 0) {
            this.dailyApparentTemperatureMin = (int) Math.round(((this.dailyApparentTemperatureMin - 32) / 1.8) + 0.5);
        }
        else {
            this.dailyApparentTemperatureMin = (int) Math.round(((this.dailyApparentTemperatureMin - 32) / 1.8) - 0.5);
        }
    }

    public void humidity() {
        this.currentlyHumidity = (int)Math.round(this.currentlyHumidity * 100);
        this.dailyHumidity = (int)Math.round(this.dailyHumidity * 100);
    }

    public double getCurrentlyWindSpeed() {
        return currentlyWindSpeed;
    }

    public void setCurrentlyWindSpeed(double currentlyWindSpeed) {
        this.currentlyWindSpeed = currentlyWindSpeed;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getAirCityName() {
        return airCityName;
    }

    public void setAirCityName(String airCityName) {
        this.airCityName = airCityName;
    }

    public String getAircityNameEng() {
        return aircityNameEng;
    }

    public void setAircityNameEng(String aircityNameEng) {
        this.aircityNameEng = aircityNameEng;
    }

    public String getAirPm25Value() {
        return airPm25Value;
    }

    public void setAirPm25Value(String airPm25Value) {
        this.airPm25Value = airPm25Value;
    }

    public String getAirDataTime() {
        return airDataTime;
    }

    public void setAirDataTime(String airDataTime) {
        this.airDataTime = airDataTime;
    }

    public String getAirPm10Value() {
        return airPm10Value;
    }

    public void setAirPm10Value(String airPm10Value) {
        this.airPm10Value = airPm10Value;
    }

    public String getAirSidoName() {
        return airSidoName;
    }

    public void setAirSidoName(String airSidoName) {
        this.airSidoName = airSidoName;
    }

    public long getCurrentlyTime() {
        return currentlyTime;
    }

    public void setCurrentlyTime(long currentlyTime) {
        this.currentlyTime = currentlyTime;
    }

    public String getCurrentlyIcon() {
        return currentlyIcon;
    }

    public void setCurrentlyIcon(String currentlyIcon) {
        this.currentlyIcon = currentlyIcon;
    }

    public double getCurrentlyTemperature() {
        return currentlyTemperature;
    }

    public void setCurrentlyTemperature(double currentlyTemperature) {
        this.currentlyTemperature = currentlyTemperature;
    }

    public double getCurrentlyHumidity() {
        return currentlyHumidity;
    }

    public void setCurrentlyHumidity(double currentlyHumidity) {
        this.currentlyHumidity = currentlyHumidity;
    }

    public long getDailyTime() {
        return dailyTime;
    }

    public void setDailyTime(long dailyTime) {
        this.dailyTime = dailyTime;
    }

    public String getDailyIcon() {
        return dailyIcon;
    }

    public void setDailyIcon(String dailyIcon) {
        this.dailyIcon = dailyIcon;
    }

    public double getDailyHumidity() {
        return dailyHumidity;
    }

    public void setDailyHumidity(double dailyHumidity) {
        this.dailyHumidity = dailyHumidity;
    }

    public double getDailyApparentTemperatureMin() {
        return dailyApparentTemperatureMin;
    }

    public void setDailyApparentTemperatureMin(double dailyApparentTemperatureMin) {
        this.dailyApparentTemperatureMin = dailyApparentTemperatureMin;
    }

    public double getDailyApparentTemperatureMax() {
        return dailyApparentTemperatureMax;
    }

    public void setDailyApparentTemperatureMax(double dailyApparentTemperatureMax) {
        this.dailyApparentTemperatureMax = dailyApparentTemperatureMax;
    }

    @Override
    public String toString() {
        return "SearchAirWeatherObject{" +
                "airCityName='" + airCityName + '\'' +
                ", aircityNameEng='" + aircityNameEng + '\'' +
                ", airPm25Value='" + airPm25Value + '\'' +
                ", airDataTime='" + airDataTime + '\'' +
                ", airPm10Value='" + airPm10Value + '\'' +
                ", airSidoName='" + airSidoName + '\'' +
                ", currentlyTime=" + currentlyTime +
                ", currentlyIcon='" + currentlyIcon + '\'' +
                ", currentlyTemperature=" + currentlyTemperature +
                ", currentlyHumidity=" + currentlyHumidity +
                ", dailyTime=" + dailyTime +
                ", dailyIcon='" + dailyIcon + '\'' +
                ", dailyHumidity=" + dailyHumidity +
                ", dailyApparentTemperatureMin=" + dailyApparentTemperatureMin +
                ", dailyApparentTemperatureMax=" + dailyApparentTemperatureMax +
                '}';
    }
}

package com.e.ango.API.Air;

public class AirGuObject {

    String sidoName; //서울
    String cityName; //구
    String cityNameEng;
    String dataTime; //정시측정시간
    String pm10Value; //미세먼지


    public AirGuObject() {

    }

    public AirGuObject(String sidoName, String cityName, String cityNameEng, String dataTime, String pm10Value) {
        this.sidoName = sidoName;
        this.cityName = cityName;
        this.cityNameEng = cityNameEng;
        this.dataTime = dataTime;
        this.pm10Value = pm10Value;
    }

    public String getSidoName() {
        return sidoName;
    }

    public void setSidoName(String sidoName) {
        this.sidoName = sidoName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityNameEng() {
        return cityNameEng;
    }

    public void setCityNameEng(String cityNameEng) {
        this.cityNameEng = cityNameEng;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getPm10Value() {
        return pm10Value;
    }

    public void setPm10Value(String pm10Value) {
        this.pm10Value = pm10Value;
    }

}

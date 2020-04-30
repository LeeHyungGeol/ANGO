package com.e.ango.API.Weather;

public class CurWeatherObject {
    long time;
    String summary;
    String icon;
    double temperature;
    double humidity;
    double windSpeed;
    CurWeatherObject curWeatherObject;

    public CurWeatherObject() { }

    public CurWeatherObject(long time, String summary, String icon, double temperature, double humidity, double windSpeed) {
        this.time = time;
        this.summary = summary;
        this.icon = icon;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }
    public CurWeatherObject getCurWeatherObject(){
        return curWeatherObject;
    }

    public long getTime() {
        return time;
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }
}

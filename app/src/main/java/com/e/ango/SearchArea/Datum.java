package com.e.ango.SearchArea;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Datum {
    protected long time;//그 날의 시간(몇 월 몇 일) 자료형 long -> double
    private String summary;
    protected String icon;//그 날의 날씨 설명


    protected double humidity;//습도

    protected double apparentTemperatureMin;////그 날의 최저 온도

    protected double apparentTemperatureMax;////그 날의 최고 온도


    @JsonProperty("time")
    public long getTime() { return time; }
    @JsonProperty("time")
    public void setTime(long value) { this.time = value; }

    @JsonProperty("summary")
    public String getSummary() { return summary; }
    @JsonProperty("summary")
    public void setSummary(String value) { this.summary = value; }

    @JsonProperty("icon")
    public String getIcon() { return icon; }
    @JsonProperty("icon")
    public void setIcon(String value) { this.icon = value; }


    @JsonProperty("humidity")
    public double getHumidity() { return humidity; }
    @JsonProperty("humidity")
    public void setHumidity(double value) { this.humidity = value; }



    @JsonProperty("apparentTemperatureMin")
    public double getApparentTemperatureMin() { return apparentTemperatureMin; }
    @JsonProperty("apparentTemperatureMin")
    public void setApparentTemperatureMin(double value) { this.apparentTemperatureMin = value; }



    @JsonProperty("apparentTemperatureMax")
    public double getApparentTemperatureMax() { return apparentTemperatureMax; }
    @JsonProperty("apparentTemperatureMax")
    public void setApparentTemperatureMax(double value) { this.apparentTemperatureMax = value; }


}

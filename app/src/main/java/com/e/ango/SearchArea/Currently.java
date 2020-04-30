package com.e.ango.SearchArea;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Currently {
    protected long time;//그시간의 날씨  long -> double
    private String summary;
    protected String icon;//그 시간의 날씨 설명

    protected double temperature;//그 시간의 온도
    protected double windSpeed;
    protected double humidity;//그 시간의 습도

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

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



    @JsonProperty("temperature")
    public double getTemperature() { return temperature; }
    @JsonProperty("temperature")
    public void setTemperature(double value) { this.temperature = value; }



    @JsonProperty("humidity")
    public double getHumidity() { return humidity; }
    @JsonProperty("humidity")
    public void setHumidity(double value) { this.humidity = value; }


}

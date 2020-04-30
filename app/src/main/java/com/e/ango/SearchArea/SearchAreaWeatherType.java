package com.e.ango.SearchArea;

public class SearchAreaWeatherType {
    SearchAirWeatherObject searchAirWeatherObject;
    String weatherType;
    double pm10Value;  //pm10Value의 String 값을 double로 값을 바꿔줌
    double temperature;
    double discomfortIndex; // 불쾌지수
    double windChillTemperature; // 체감 온도

    public SearchAreaWeatherType(SearchAirWeatherObject searchAirWeatherObject) {
        this.searchAirWeatherObject = searchAirWeatherObject;
    }

    public String WeatherType() {


        try {

            if (searchAirWeatherObject.airPm10Value == null)
                searchAirWeatherObject.airPm10Value = "0.0";
            pm10Value = Double.parseDouble(searchAirWeatherObject.airPm10Value);
            temperature = searchAirWeatherObject.getCurrentlyTemperature();
            discomfortIndex = (9 / 5 * temperature) - 0.55 * (1 - searchAirWeatherObject.getCurrentlyHumidity()) * (9 / 5 * temperature - 26) + 32;
            windChillTemperature = 13.12 + 0.6215 * temperature - 11.37 * Math.pow(searchAirWeatherObject.currentlyWindSpeed, 0.16) + 0.3965 * Math.pow(searchAirWeatherObject.currentlyWindSpeed, 0.16) * temperature;

            if (80 < pm10Value && pm10Value <= 600) {
                weatherType = "type_0";
            } else {
                switch (searchAirWeatherObject.getCurrentlyIcon()) {
                    case "snow":
                        if (temperature >= 10) {
                            if (discomfortIndex < 75) weatherType = "type_1";
                            else if (75 <= discomfortIndex) weatherType = "type_2";
                            else if (0 < windChillTemperature && windChillTemperature <= 10)
                                weatherType = "type_3";
                        } else {
                            if (-10 < windChillTemperature && windChillTemperature <= 0)
                                weatherType = "type_4";
                            else if (windChillTemperature <= -10) weatherType = "type_5";
                        }
                        break;

                    case "clear":
                        if (temperature >= 10) {
                            if (discomfortIndex < 75) weatherType = "type_11";
                            else if (75 <= discomfortIndex) weatherType = "type_12";
                            else if (0 < windChillTemperature && windChillTemperature <= 10)
                                weatherType = "type_13";
                        } else {
                            if (-10 < windChillTemperature && windChillTemperature < 0)
                                weatherType = "type_14";
                            else if (windChillTemperature <= -10) weatherType = "type_15";
                        }
                        break;
                    case "rain":
                        if (temperature >= 10) {
                            if (discomfortIndex < 75) weatherType = "type_16";
                            else if (75 <= discomfortIndex) weatherType = "type_17";
                        } else {
                            if (0 < windChillTemperature && windChillTemperature <= 10)
                                weatherType = "type_18";
                            else if (-10 < windChillTemperature && windChillTemperature <= 0)
                                weatherType = "type_19";
                            else if (windChillTemperature <= -10) weatherType = "type_20";
                        }
                        break;
                    default:
                        if (temperature >= 10) {
                            if (discomfortIndex < 75) weatherType = "type_6";
                            else if (75 <= discomfortIndex) weatherType = "type_7";
                        } else {
                            if (0 < windChillTemperature && windChillTemperature <= 10)
                                weatherType = "type_8";
                            else if (-10 < windChillTemperature && windChillTemperature <= 0)
                                weatherType = "type_9";
                            else if (windChillTemperature <= -10) weatherType = "type_10";
                        }
                        break;
                }
            }
        } catch (Exception e) {

            searchAirWeatherObject.airPm10Value = "0.0";
            weatherType = this.WeatherType();
            System.out.println("sibal : " + weatherType);
            e.printStackTrace();
        }
        return weatherType;
    }
}

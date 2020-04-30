package com.e.ango.API;

public class CurrentLocationWeatherType {

    AirWeatherObject airWeatherObject;
    String weatherType;
    double discomfortIndex; // 불쾌지수
    double windChillTemperature; // 체감 온도
    double temperature;
    double humidity;
    double windSpeed;


    public CurrentLocationWeatherType(AirWeatherObject airWeatherObject) {
        this.airWeatherObject = airWeatherObject;
        temperature = airWeatherObject.temperature;
        humidity = airWeatherObject.humidity;
        windSpeed = airWeatherObject.windSpeed;

        discomfortIndex = 0;
        windChillTemperature = 0;
    }

    public String getWeatherType(){

        discomfortIndex = (9 / 5 * temperature) - 0.55 * (1 - humidity) * (9 / 5 * temperature - 26) + 32;
        windChillTemperature = 13.12 + 0.6215 * temperature - 11.37 * Math.pow(windSpeed, 0.16) + 0.3965 * Math.pow(windSpeed, 0.16) * temperature;

        if (80 < airWeatherObject.getPm10Value() && airWeatherObject.getPm10Value() <= 600) {
            weatherType = "type_0";
        } else {
            switch(airWeatherObject.icon){
                case "snow" :
                    if(temperature < 10) {
                        if (0 < windChillTemperature && windChillTemperature <= 10)
                            weatherType = "type_3";
                        else if (-10 < windChillTemperature && windChillTemperature <= 0)
                            weatherType = "type_4";
                        else if (windChillTemperature <= -10) weatherType = "type_5";
                    }
                    break;

                case "clear" :
                case "Clear" :
                    if(temperature >= 10) {
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
                case "rain" :
                    if(temperature >= 10) {
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
                default :
                    if(temperature >= 10) {
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

        return weatherType;
    }
    //날씨 타입 계산
//    public String WeatherType() {
//        try {
//            if (80 < airWeatherObject.getPm10Value() && airWeatherObject.getPm10Value() <= 600) {
//                weatherType = "type_0";
//                //iv_weatherIcon.setImageResource(R.drawable.medical_mask);
//                //currentLocationActivity.setDrawable(R.drawable.medical_mask);
//            } else {
//                switch (airWeatherObject.getIcon()) {
//                    case "snow":
//                    case "Snow":
//                        if (airWeatherObject.getTemperature() < 5) weatherType = "type_1";
//                        break;
//
//                    case "rain":
//                        if (5 <= airWeatherObject.getTemperature() && airWeatherObject.getTemperature() < 15)
//                            weatherType = "type_2";
//                        else if (15 <= airWeatherObject.getTemperature() && airWeatherObject.getTemperature() < 27)
//                            weatherType = "type_3";
//                        else if (27 <= airWeatherObject.getTemperature()) weatherType = "type_4";
//                        break;
//
//                    case "clear":
//                    case "Clear":
//                    case "clear-night":
//                    case "clear-day":
//                        if (airWeatherObject.getTemperature() < 5) weatherType = "type_11";
//                        else if (5 <= airWeatherObject.getTemperature() && airWeatherObject.getTemperature() < 15)
//                            weatherType = "type_12";
//
//                        if (90 <= airWeatherObject.getHumidity()) { //습함
//                            if (15 <= airWeatherObject.getTemperature() && airWeatherObject.getTemperature() < 27)
//                                weatherType = "type_13";
//                            else if (27 <= airWeatherObject.getTemperature())
//                                weatherType = "type_14";
//                        } else if (0 <= airWeatherObject.getHumidity() && airWeatherObject.getHumidity() < 90) { //안습함
//                            if (15 <= airWeatherObject.getTemperature() && airWeatherObject.getTemperature() < 27) {
//                                weatherType = "type_15";
//                            } else if (27 <= airWeatherObject.getTemperature()) {
//                                weatherType = "type_16";
//                            }
//                        }
//                        break;
//
//                    default: //cloudy, mostly-cloudy, wind...
//                        if (airWeatherObject.getTemperature() < 5) weatherType = "type_5";
//                        else if (5 <= airWeatherObject.getTemperature() && airWeatherObject.getTemperature() < 15)
//                            weatherType = "type_6";
//
//                        if (90 <= airWeatherObject.getHumidity()) { //습함
//                            if (15 <= airWeatherObject.getTemperature() && airWeatherObject.getTemperature() < 27)
//                                weatherType = "type_7";
//                            else if (27 <= airWeatherObject.getTemperature())
//                                weatherType = "type_8";
//                        } else if (0 <= airWeatherObject.getHumidity() && airWeatherObject.getHumidity() < 90) { //안습함
//                            if (15 <= airWeatherObject.getTemperature() && airWeatherObject.getTemperature() < 27)
//                                weatherType = "type_9";
//                            else if (27 <= airWeatherObject.getTemperature())
//                                weatherType = "type_10";
//                        }
//                        break;
//
//                }
//            }
//        } catch(NullPointerException e){
//            e.printStackTrace();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return weatherType;
//    }
}

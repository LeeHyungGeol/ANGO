package com.e.ango.Response;

public class QuestionDto {

	private String weather_type;
	private String question_text;

	public String getWeather_type() {
		return weather_type;
	}

	public void setWeather_type(String weather_type) {
		this.weather_type = weather_type;
	}

	public String getQuestion_text() {
		return question_text;
	}

	public void setQuestion_text(String question_text) {
		this.question_text = question_text;
	}



	@Override
	public String toString() {
		return "QuestionVO [weather_type=" + weather_type + ", question_text=" + question_text
				+ "]";
	}

}

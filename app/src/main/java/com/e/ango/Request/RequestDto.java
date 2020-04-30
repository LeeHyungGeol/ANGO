package com.e.ango.Request;


import java.util.ArrayList;
import com.e.ango.Response.ReviewDto;
public class RequestDto {

	private String request_msg;
	private String weather_type;
	private String token;
	private UserDto user;
	private ArrayList<PreferDto> preference_list = new ArrayList<>();
	private ArrayList<ReviewDto> review_list = new ArrayList<>();

	public RequestDto() { }

	public RequestDto(String request_msg, String token) {
		this.request_msg = request_msg;
		this.token = token;
	}

	public RequestDto(String request_msg, String weather_type, String token) {
		this.request_msg = request_msg;
		this.weather_type = weather_type;
		this.token = token;
	}

	public RequestDto(String request_msg, String token, ArrayList<PreferDto> preferDtos) {
		this.request_msg = request_msg;
		this.token = token;
	}

	public RequestDto(String request_msg, String weather_type, String token, ArrayList<PreferDto> preference_list) {
		this.request_msg = request_msg;
		this.weather_type = weather_type;
		this.token = token;
		this.preference_list = preference_list;
	}



	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRequest_msg() {
		return request_msg;
	}

	public void setRequest_msg(String request_msg) {
		this.request_msg = request_msg;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public String getWeather_type() {
		return weather_type;
	}

	public void setWeather_type(String weather_type) {
		this.weather_type = weather_type;
	}

	public ArrayList<PreferDto> getPreference_list() {
		return preference_list;
	}

	public void setPreference_list(ArrayList<PreferDto> preference_list) {
		this.preference_list = preference_list;
	}

	public ArrayList<ReviewDto> getReview_list() {
		return review_list;
	}

	public void setReview_list(ArrayList<ReviewDto> review_list) {
		this.review_list = review_list;
	}

	@Override
	public String toString() {
		return "RequestVO [request_msg=" + request_msg + ", weather_type=" + weather_type + ", token=" + token
				+ ", user=" + user + ", preference_list=" + preference_list + ", review_list=" + review_list + "]";
	}

}

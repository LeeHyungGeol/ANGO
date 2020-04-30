package com.e.ango.Response;

import java.util.ArrayList;
import com.e.ango.Request.PreferDto;
import com.e.ango.Request.UserDto;

public class ResponseDto {

    private int availability;
    private String token;
    private String response_msg;
    private ArrayList<QuestionDto> question_list = new ArrayList<>();
    private ArrayList<ReviewDto> review_list = new ArrayList<>();
    private ArrayList<PreferDto> prefer_list;
    private UserDto user;
    private String token_msg;



    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getResponse_msg() {
        return response_msg;
    }

    public void setResponse_msg(String response_msg) {
        this.response_msg = response_msg;
    }

    public ArrayList<PreferDto> getPrefer_list() {
        return prefer_list;
    }

    public void setPrefer_list(ArrayList<PreferDto> prefer_list) {
        this.prefer_list = prefer_list;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public ArrayList<QuestionDto> getQuestion_list() {
        return question_list;
    }

    public void setQuestion_list(ArrayList<QuestionDto> question_list) {
        this.question_list = question_list;
    }

    public ArrayList<ReviewDto> getReview_list() {
        return review_list;
    }

    public void setReview_list(ArrayList<ReviewDto> review_list) {
        this.review_list = review_list;
    }

    public String getToken_msg() {
        return token_msg;
    }

    public void setToken_msg(String token_msg) {
        this.token_msg = token_msg;
    }

    @Override
    public String toString() {
        return "ResponseVO [availability=" + availability + ", token=" + token
                + ", question_list=" + question_list + ", review_list=" + review_list + "]";
    }

}

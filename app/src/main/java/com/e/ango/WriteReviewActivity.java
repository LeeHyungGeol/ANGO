package com.e.ango;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.e.ango.Request.RequestDto;
import com.e.ango.Response.ReviewDto;
import com.e.ango.Review.ReviewSubmitTask;
import com.e.ango.SearchArea.SearchAirWeatherObject;
import com.e.ango.SearchArea.SearchAirWeatherTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import static com.e.ango.Login.LoginTask.token;

public class WriteReviewActivity extends AppCompatActivity {

    RequestDto requestVO;
    ArrayList<ReviewDto> reviewVOS;
    ReviewDto reviewVO;
    String request_msg = "InsertUserReview";
    String content_id;
    String weather_type;
    String category_id;
    String title;
    //String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyX3Rva2VuIiwiYXVkIjoidXNlcklEXzEiLCJpc3MiOiJhbmdvX3NlcnZlciIsImV4cCI6MTU3NDY3NTU2NTU5Nn0.I-nApJIWZuFnMVelxiwDMtz3Lv--zvrqWicLSc7fLdU";

    public static EditText editText;
    public static RatingBar ratingBar;

    String date;//날짜 받아온 결과
    String time;//시간 받아온 결과
    String xCoordinate;
    String yCoordinate;
    String districtResult;//사용자가 선택한 지역(api 파싱할때 지역 비교를 위해 필요)
    Spinner spinner_District;//지역 설정
    TextView textView_selectDate;//날짜(년,월,일) 설정
    TextView textView_selectTime;//시간(시,분) 설정


    DatePickerDialog.OnDateSetListener setDateListener;
    TimePickerDialog.OnTimeSetListener setTimeListner;
    int inputHour = -1;//사용자가 지정한 시간
    int inputYear = -1;//사용자가 지정한 년도
    int inputMonth = -1;//사용자가 지정한 월
    int inputDay = -1;//사용자가 지정한 일


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        Intent intent = getIntent();
        content_id = intent.getExtras().getString("content_id");
        //weather_type = intent1.getExtras().getString("weather_type");
        category_id = intent.getExtras().getString("category_id");
        title = intent.getExtras().getString("title");
        xCoordinate = intent.getExtras().getString("xCoordinate", xCoordinate);
        yCoordinate = intent.getExtras().getString("xCoordinate", yCoordinate);


        //날짜(년,월,일) + 시간(시,분,초)
        textView_selectDate = findViewById(R.id.textView_selectDate);
        textView_selectTime = findViewById(R.id.textView_selectTime);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);//안드로이드 에뮬레이터의 현재 시간
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);//시간을 24시간으로
        final int minute = calendar.get(Calendar.MINUTE);
        final Calendar mCalendarMaximum = Calendar.getInstance();//최대로 선택 할 수 있는 날짜를 일주일로 설정
        mCalendarMaximum.add(Calendar.DAY_OF_MONTH, +0);

        //날짜 선택
        textView_selectDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        WriteReviewActivity.this, setDateListener, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(mCalendarMaximum.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        setDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                if (month < 10 && dayOfMonth < 10) {//파싱하기 위한 조건과 맞게 변경한 날짜 (ex)2019-11-10
                    date = year + "-0" + month + "-0" + dayOfMonth;
                    inputYear = year;//사용자가 지정한 년,월,일 삽입
                    inputMonth = month;
                    inputDay = dayOfMonth;
                    textView_selectDate.setText(date);
                } else if (dayOfMonth < 10) {
                    date = year + "-" + month + "-0" + dayOfMonth;
                    inputYear = year;//사용자가 지정한 년,월,일 삽입
                    inputMonth = month;
                    inputDay = dayOfMonth;
                    textView_selectDate.setText(date);
                } else if (month < 10) {
                    date = year + "-0" + month + "-" + dayOfMonth;
                    inputYear = year;//사용자가 지정한 년,월,일 삽입
                    inputMonth = month;
                    inputDay = dayOfMonth;
                    textView_selectDate.setText(date);
                } else {
                    date = year + "-" + month + "-" + dayOfMonth;
                    inputYear = year;//사용자가 지정한 년,월,일 삽입
                    inputMonth = month;
                    inputDay = dayOfMonth;
                    textView_selectDate.setText(date);
                }
            }
        };

        //시간 선택
        textView_selectTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        WriteReviewActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , setTimeListner, hour, minute, false);//true로 하면 24시간 //false로 하면 오전/오후 선택
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        });

        setTimeListner = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (hourOfDay < 10 && minute < 10) {//파싱하기 위한 조건과 맞게 변경한 시간 (ex)22:10:00
                    time = "0" + hourOfDay + ":0" + minute + ":" + "00";
                    textView_selectTime.setText(time);
                    inputHour = hourOfDay;//사용자가 지정한 시간 삽입
                } else if (hourOfDay < 10) {
                    time = "0" + hourOfDay + ":" + minute + ":" + "00";
                    textView_selectTime.setText(time);
                    inputHour = hourOfDay;//사용자가 지정한 시간 삽입
                } else if (minute < 10) {
                    time = hourOfDay + ":0" + minute + ":" + "00";
                    textView_selectTime.setText(time);
                    inputHour = hourOfDay;//사용자가 지정한 시간 삽입
                } else {
                    time = hourOfDay + ":" + minute + ":" + "00";
                    textView_selectTime.setText(time);
                    inputHour = hourOfDay;//사용자가 지정한 시간 삽입
                }

            }
        };

    }

    public void submitButtonClick(View view) {

        try {
            if (inputDay == -1 || inputHour == -1) {
                Toast toast = Toast.makeText(WriteReviewActivity.this, "날짜 및 시간을 선택해주세요.", Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            editText = (EditText) findViewById(R.id.editText);
            ratingBar = (RatingBar) findViewById(R.id.ratingBar2);

            requestVO = new RequestDto();
            SearchAirWeatherObject searchAirWeatherObject = new SearchAirWeatherTask(date, time, xCoordinate, yCoordinate, districtResult).execute().get();
            requestVO.setWeather_type(searchAirWeatherObject.getWeatherType());
            requestVO.setToken(token);
            requestVO.setRequest_msg(request_msg);


            reviewVO = new ReviewDto();
            reviewVO.setReview_text(editText.getText().toString());
            reviewVO.setReview_score(ratingBar.getRating());
            reviewVO.setContent_id(content_id);
            reviewVO.setTitle(title);
            reviewVO.setCategory_id(category_id);
            reviewVO.setReview_score(ratingBar.getRating());


            reviewVOS = new ArrayList<ReviewDto>();
            reviewVOS.add(reviewVO);
            requestVO.setReview_list(reviewVOS);

//            PreferDto preferVO = new PreferDto();
//            preferVO.setCg_id(category_id);
//            preferVO.setUser_score(ratingBar.getRating());
//            ArrayList<PreferDto> prefer_list = new ArrayList<PreferDto>();
//            prefer_list.add(preferVO);

//            requestVO.setPreference_list(prefer_list);


            Boolean flag_insertReview = false;

            try {
                flag_insertReview = new ReviewSubmitTask(requestVO).execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if (!flag_insertReview) {

                Toast myToast = Toast.makeText(this.getApplicationContext(), "리뷰 작성 실패", Toast.LENGTH_SHORT);
                myToast.show();
            }

            // Intent intent = new Intent(getApplicationContext(), ContentReviewActivity.class);
//            intent.putExtra("content_id", content_id);
//            intent.putExtra("category_id", category_id);
//            intent.putExtra("title", title);
//            startActivity(intent);
            finish();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
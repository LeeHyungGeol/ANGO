package com.e.ango;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.e.ango.API.Play.PlayObject;
import com.e.ango.ListView.ListAdapter;
import com.e.ango.ListView.ListData;
import com.e.ango.Response.ResponseDto;
import com.e.ango.SearchArea.SearchAirWeatherObject;
import com.e.ango.SearchArea.SearchAirWeatherTask;
import com.e.ango.SearchArea.SearchAreaPlayTask;
import com.e.ango.SearchArea.SearchAreaRecommendTask;
import com.e.ango.SearchArea.SearchWeatherTask;
import com.e.ango.SurveyConnection.LoadImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class SearchAreaActivity extends AppCompatActivity {
    private String date;//날짜 받아온 결과
    private String time;//시간 받아온 결과
    private String xCoordinate;
    private String yCoordinate;
    private String districtResult = "";//사용자가 선택한 지역(api 파싱할때 지역 비교를 위해 필요)
    private Spinner spinner_District;//지역 설정
    private Button button_search;
    private TextView textView_selectDate;//날짜(년,월,일) 설정
    private TextView textView_selectTime;//시간(시,분) 설정
    private TextView textView_air;
    private TextView textView_humidity;
    private TextView textView_icon;
    private TextView textView_temperature;
    private TextView textView_temperatureMax;
    private TextView textView_temperatureMin;

    private int temperature = 0;
    private int humidity = 0;
    private int temperatureMax = 0;
    private int temperatureMin = 0;
    private String weatherType;
    private String areaCode = "0";//searchAreaPlayTask를 수행하기 위한 지역코드(areaCode)

    private DatePickerDialog.OnDateSetListener setDateListener;
    private TimePickerDialog.OnTimeSetListener setTimeListner;
    private int inputHour = -1;//사용자가 지정한 시간 //아무것도 선택 되지 않았을 때를 위해 -1로 설정
    private int inputYear = -1;//사용자가 지정한 년도
    private int inputMonth = -1;//사용자가 지정한 월
    private int inputDay = -1;//사용자가 지정한 일

    private Boolean weathrParsingFlag = false;
    private SearchAirWeatherObject searchAirWeatherObject;
    private ArrayList<PlayObject> userPreferencePlayObjects;
    private ArrayList<ListData> arrayListData;
    private ListView printList;
    private Bitmap bitmap;
    private ImageView imageView_weather;
    private int weatherTypeInt;
    private CustomAnimationDialog customAnimationDialog; // 로딩창 화면을 띄우기 위한 변수
    private ResponseDto responseDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_area);

        //지역 -> 좌표로 받아오기 (array.xml 사용)
        final String[] districToXcoordinate = getResources().getStringArray(R.array.districtXCoordinate);
        final String[] districToYcoordinate = getResources().getStringArray(R.array.districtYcoordinate);
        final String[] districtToAreaCode = getResources().getStringArray(R.array.districtToAreaCode);

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
        final Calendar mCalendarMinimum = Calendar.getInstance();//최소로 선택 할 수 있는 날짜를 오늘로 설정
        mCalendarMaximum.add(Calendar.DAY_OF_MONTH, +7);
        mCalendarMinimum.add(Calendar.DAY_OF_MONTH, +0);

        //spinner 선언 //(지역 설정 드롭박스)
        spinner_District = findViewById(R.id.spinner_districtName);

        spinner_District.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                districtResult = parent.getItemAtPosition(position).toString();//사용자가 지정한 지역의 지역명
                xCoordinate = districToXcoordinate[position];//사용자가 지정한 지역의 x좌표 //array.xml 사용
                yCoordinate = districToYcoordinate[position];//사용자가 지정한 지역의 y좌표 //array.xml 사용
                areaCode = districtToAreaCode[position];//사용자가 지정한 지역의 areaCode(놀거리 API를 파싱 받을 때 필요) //array.xml 사용
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //날짜 선택
        textView_selectDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        SearchAreaActivity.this, setDateListener, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(mCalendarMaximum.getTimeInMillis());
                datePickerDialog.getDatePicker().setMinDate(mCalendarMinimum.getTimeInMillis());
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
                        SearchAreaActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
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

        //텍스트뷰 선언(날씨 파싱 받아오기)
        textView_humidity = findViewById(R.id.tv_humidity);
        textView_icon = findViewById(R.id.tv_weather);
        textView_temperature = findViewById(R.id.tv_temperature);
        textView_air = findViewById(R.id.tv_air);
        textView_temperatureMax = findViewById(R.id.tv_temperatureMax);
        textView_temperatureMin = findViewById(R.id.tv_temperatureMin);

        imageView_weather = findViewById(R.id.iv_WeatherIcon);

        button_search = findViewById(R.id.button_search);

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("검색 기능 시작 ! ");

                if (inputHour == -1 || inputDay == -1 || areaCode.equals("0")) {
                    Toast toast = Toast.makeText(SearchAreaActivity.this, "지역, 날짜 및 시간을 선택해주세요.", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                if (inputHour == hour && inputDay == day && inputMonth == (month + 1) && inputYear == year) {//사용자가 현재시각에서 다른 장소를 검색할때
                    try {
                        searchAirWeatherObject = new SearchAirWeatherTask(date, time, xCoordinate, yCoordinate, districtResult).execute().get();

                        System.out.println("------------------------------------------------------------------");//입력 값들이 잘들어갔는지 확인하기 위한 print
                        System.out.println(xCoordinate + "," + yCoordinate + "," + date + "T" + time + "?exclude=hourly,flags");
                        System.out.println("SearchAirWeatherTask : 날씨 타입 : " + searchAirWeatherObject.getWeatherType());

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } //if문끝

                else {//나머지(다른지역 다른시간, 현재지역 다른시간)
                    try {
                        searchAirWeatherObject = new SearchWeatherTask(date, time, xCoordinate, yCoordinate, districtResult).execute().get();
                        System.out.println("SearchWeatherTask : 날씨 타입 : " + searchAirWeatherObject.getWeatherType());

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } //else 문 끝

                if (searchAirWeatherObject.getWeatherType() == null) {
                    weathrParsingFlag = false;
                    System.out.println("날씨 파싱에 실패하였습니다.");
                } else {
                    weathrParsingFlag = true;
                    System.out.println("날씨 파싱에 성공하였습니다.");
                }

                if (weathrParsingFlag) {

                    weatherType = searchAirWeatherObject.getWeatherType();

                    setWeatherInformation(); //public void setWeatherInformation() //날씨에 대한 정보들(날씨 아이콘, 날씨 요약, 습도, 온도 등)을 TextView 및 ImageView 에 보여준다
                    // set imageView_weather, textView_icon, textView_temperature, textView_humidity...

                    try {
                        responseDto = new SearchAreaRecommendTask(weatherType).execute().get();

                        if (responseDto.getResponse_msg().equals("RecommendCategory_success")) {
                            userPreferencePlayObjects = new SearchAreaPlayTask(responseDto.getPrefer_list(), areaCode).execute().get();
                        } else {
                            Toast toast = Toast.makeText(SearchAreaActivity.this, "RecommendCategory is Null.", Toast.LENGTH_LONG);
                            toast.show();
                        }

                        if (userPreferencePlayObjects.isEmpty()) {
                            System.out.println("flag_userPreferencePlayObjects : 선호도 아이템이 없습니다.");
                            Toast toast = Toast.makeText(SearchAreaActivity.this, "선호도 아이템이 없습니다.", Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            System.out.println("flag_userPreferencePlayObjects : 선호도 아이템이 있습니다.");
                        }

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!userPreferencePlayObjects.isEmpty()) { // userPreferencePlayObjects 가 비어있지 않을때 // 선호도 아이템이 있을 때

                        arrayListData = new ArrayList<ListData>();// listView 에 보여줄 listData 를 setListView

                        setListViewData(); //public void setListViewData()
                        // set ImageView, TextView // 사진, 제목, 내용, content_id, category_id...

                        printList = (ListView) findViewById(R.id.listView);
                        ListAdapter listAdapter = new ListAdapter(arrayListData);
                        printList.setAdapter(listAdapter);

                        printList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Toast.makeText(SearchAreaActivity.this, arrayListData.get(position).getCategoryId(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SearchAreaActivity.this, DetailInfoActivity.class);
                                intent.putExtra("content_id", arrayListData.get(position).getContentId());
                                intent.putExtra("category_id", arrayListData.get(position).getCategoryId());
                                intent.putExtra("xCoordinate", xCoordinate);
                                intent.putExtra("yCoordinate", yCoordinate);

                                //비트맵 이미지의 경우 바이트 타입으로 변환하여 넘겨 줍니다.
                                // 주의할점은 넘겨줄수 있는 데이터 크기 제약이 있기 때문에 이미지 크기를 줄여서 보내야 합니다.
                                Bitmap intentBitmap = arrayListData.get(position).getBitmap();

                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                float scale = (float) (256 / (float) intentBitmap.getWidth());
                                int image_w = (int) (intentBitmap.getWidth() * scale);
                                int image_h = (int) (intentBitmap.getHeight() * scale);
                                Bitmap resize = Bitmap.createScaledBitmap(intentBitmap, image_w, image_h, true);
                                resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                byte[] byteArray = stream.toByteArray();
                                intent.putExtra("image", byteArray);

                                startActivity(intent);
                            }
                        });

                    }//if(flag_userPreferencePlayObjects)문 끝

                }//if(flag)문 끝
            }
        });
    }

    public void setWeatherInformation() {
        if (weatherType != null) { // 날씨 아이콘 이미지뷰 설정
            weatherTypeInt = setDrawableIcon(weatherType);
            Drawable drawable = getResources().getDrawable(weatherTypeInt);
            imageView_weather.setImageDrawable(drawable);
        }
        // 미세먼지에 대한 setTextView
        if (searchAirWeatherObject.getAirPm10Value() == null || searchAirWeatherObject.getAirPm10Value() == "0.0") {
            textView_air.setText("자외선 지수 : 5");
            System.out.println("자외선 지수 : 5");
        } else {
            if (80 < Integer.parseInt(searchAirWeatherObject.getAirPm10Value()) && Integer.parseInt(searchAirWeatherObject.getAirPm10Value()) <= 160)
                textView_air.setText("자외선 지수 : 5");
            else
                textView_air.setText("자외선 지수 : 5");
            System.out.println("자외선 지수 : 5");
        }
        // 습도, 온도, 최대온도, 최소온도 setTextView
        humidity = (int) searchAirWeatherObject.getCurrentlyHumidity();
        temperature = (int) searchAirWeatherObject.getCurrentlyTemperature();
        temperatureMax = (int) searchAirWeatherObject.getDailyApparentTemperatureMax();
        temperatureMin = (int) searchAirWeatherObject.getDailyApparentTemperatureMin();

        textView_humidity.setText("습도: " +Integer.toString(humidity));
        textView_icon.setText(searchAirWeatherObject.getCurrentlyIcon());
        textView_temperature.setText("온도: " +Integer.toString(temperature));
        textView_temperatureMax.setText("최고 온도: " +Integer.toString(temperatureMax));
        textView_temperatureMin.setText("최저 온도: " +Integer.toString(temperatureMin));
    }

    public int setDrawableIcon(String weather_type) { //날씨 아이콘에 대한
        int weatherInt = 0;
        if (weather_type != null) {
            switch (weather_type) {
                case "type_0":
                    weatherInt = R.drawable.medical_mask;
                    break;
                case "type_1":
                    weatherInt = R.drawable.snowflake;
                    break;
                case "type_2":
                case "type_3":
                case "type_4":
                    weatherInt = R.drawable.rain;
                    break;
                case "type_5":
                case "type_6":
                case "type_7":
                case "type_8":
                case "type_9":
                case "type_10":
                    weatherInt = R.drawable.cloud;
                    break;
                case "type_11":
                case "type_12":
                case "type_13":
                case "type_14":
                case "type_15":
                case "type_16":
                    weatherInt = R.drawable.sun;
                    break;

            }
            System.out.println("MAIN WEATHERINT : " + weatherInt);
        }
        return weatherInt;
    }

    public void setListViewData() {
        for (int i = 0; i < userPreferencePlayObjects.size(); i++) {
            ListData listData = new ListData();
            try {
                if (userPreferencePlayObjects.get(i).getFirstimage2() == null) {
                    bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.no_image);
                } else {
                    bitmap = new LoadImage().execute(userPreferencePlayObjects.get(i).getFirstimage2()).get();
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listData.setTitle(userPreferencePlayObjects.get(i).getTitle());
            listData.setAddress(userPreferencePlayObjects.get(i).getAddr1() + " " + userPreferencePlayObjects.get(i).getAddr2());
            listData.setSummary(userPreferencePlayObjects.get(i).getCategoryName());
            listData.setBitmap(bitmap);
            listData.setCategoryId(userPreferencePlayObjects.get(i).getPreferCategoryId());
            System.out.println("listData.getCategoryId() : " + listData.getCategoryId());
            System.out.println("userPreferencePlayObjects.get(i).getPreferCategoryId() : " + userPreferencePlayObjects.get(i).getPreferCategoryId());
            listData.setContentId(userPreferencePlayObjects.get(i).getContentid());
            System.out.println("listData.getContentId() : " + listData.getContentId());
            System.out.println("userPreferencePlayObjects.get(i).getContentid() : " + userPreferencePlayObjects.get(i).getContentid());
            arrayListData.add(listData);
        }
    }
}



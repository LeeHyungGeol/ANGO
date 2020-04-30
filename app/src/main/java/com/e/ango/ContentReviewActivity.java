package com.e.ango;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.ango.Request.PreferDto;
import com.e.ango.Request.RequestDto;
import com.e.ango.Response.ResponseDto;
import com.e.ango.Response.ReviewDto;
import com.e.ango.Review.ReviewDTOAdapter;
import com.e.ango.Review.ReviewTask;

import java.util.ArrayList;

import static com.e.ango.Login.LoginTask.token;

public class ContentReviewActivity extends AppCompatActivity {

    String xCoordinate;
    String yCoordinate;
    TextView textTitle;
    ResponseDto responseDto;
    RequestDto requestDto = new RequestDto();
    ReviewDto reviewDto = new ReviewDto();
    ArrayList<ReviewDto> reviewVOS = new ArrayList<ReviewDto>();
    PreferDto preferVO = new PreferDto();
    ArrayList<PreferDto> preferVOS = new ArrayList<PreferDto>();
    Boolean flag = false;
    String title;
    String request_msg = "GetContentReview";
    String content_id;
    String weather_type;
    String category_id;
    //String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyX3Rva2VuIiwiYXVkIjoidXNlcklEXzEiLCJpc3MiOiJhbmdvX3NlcnZlciIsImV4cCI6MTU3NDY3NTU2NTU5Nn0.I-nApJIWZuFnMVelxiwDMtz3Lv--zvrqWicLSc7fLdU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_review);

        Intent intent = getIntent();
        content_id = intent.getExtras().getString("content_id");
        category_id = intent.getExtras().getString("category_id");
        title = intent.getExtras().getString("title");
        xCoordinate = intent.getExtras().getString("xCoordinate",xCoordinate);
        yCoordinate = intent.getExtras().getString("xCoordinate",yCoordinate);

        textTitle = findViewById(R.id.text_title);
        textTitle.setText(title);

        getReview();

        Toast myToast = Toast.makeText(this.getApplicationContext(), "content : " + content_id, Toast.LENGTH_SHORT);
        myToast.show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        getReview();

    }

    public void getReview() {

        try {
            reviewDto.setContent_id(content_id);
            reviewVOS.add(reviewDto);

            requestDto.setReview_list(reviewVOS);
            requestDto.setToken(token);
            requestDto.setRequest_msg(request_msg);

            responseDto = new ReviewTask(requestDto).execute().get();
            if (responseDto != null) {
                flag = true;
                System.out.println(responseDto.toString());
            }

            if (flag) {

                if (responseDto.getResponse_msg().equals("GetContentReview_success")) {
                    System.out.println(responseDto.toString());


                    RecyclerView recyclerView = findViewById(R.id.recyclerView);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    ReviewDTOAdapter adapter = new ReviewDTOAdapter();

                    adapter.setItems(responseDto.getReview_list());
                    recyclerView.setAdapter(adapter);
                }// if responce meg
                else {
                    Toast myToast = Toast.makeText(this.getApplicationContext(), "리뷰가 없습니다.", Toast.LENGTH_SHORT);
                    myToast.show();
                }

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public void writeReviewClick(View view) {
        Intent intent = new Intent(getApplicationContext(), WriteReviewActivity.class);

        intent.putExtra("content_id", content_id);
        intent.putExtra("category_id", category_id);
        //intent.putExtra("weather_type", weather_type);
        intent.putExtra("title", title);
        intent.putExtra("xCoordinate",xCoordinate);
        intent.putExtra("yCoordinate",yCoordinate);


        startActivity(intent);

    }
}
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
import com.e.ango.Review.ReviewTask;
import com.e.ango.Review.UserReviewAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.e.ango.Login.LoginTask.token;

public class UserReviewActivity extends AppCompatActivity {

    ResponseDto responseDto;
    RequestDto requestDto = new RequestDto();
    Boolean flag = false;
    //String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyX3Rva2VuIiwiYXVkIjoidXNlcklEXzEiLCJpc3MiOiJhbmdvX3NlcnZlciIsImV4cCI6MTU3NDY3NTU2NTU5Nn0.I-nApJIWZuFnMVelxiwDMtz3Lv--zvrqWicLSc7fLdU";
    ArrayList<ReviewDto> review_list;
    UserReviewAdapter adapter;
    TextView textTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_review);

        try {

            textTitle = findViewById(R.id.text_user);

            requestDto.setToken(token);
            requestDto.setRequest_msg("GetUserReview");
            responseDto = new ReviewTask(requestDto).execute().get();//서버에서 놀거리 카테고리와 질문을 받아와야만 view에 날씨 질문과 놀거리를 보여준다.
            if (responseDto != null) {
                flag = true;
                //System.out.println(responseDto.toString());
            }

            if (flag) {

                textTitle.setText(responseDto.getUser().getUser_id());
                if (responseDto.getResponse_msg().equals("GetUserReview_success")) {


                    System.out.println("activity : " + responseDto.getReview_list().toString());
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new UserReviewAdapter();
                    adapter.setItems(responseDto.getReview_list());

                    recyclerView.setAdapter(adapter);
                }// if responce meg
                else {
                    Toast myToast = Toast.makeText(this.getApplicationContext(), "리뷰가 없습니다.", Toast.LENGTH_SHORT);
                    myToast.show();
                }
            }//if flag


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteReview(View view) {

        RequestDto delete_RequestDto = new RequestDto();
        ResponseDto delete_ResponseDto;
        ArrayList<ReviewDto> choose_review = new ArrayList<>();
        ArrayList<PreferDto> choose_prefer = new ArrayList<>();


        try {
            review_list = null;
            review_list = ((UserReviewAdapter) adapter).getItems();

            for (int i = 0; i < review_list.size(); i++) {
                ReviewDto reviewDto = review_list.get(i);

                if (reviewDto.isSelected() == true) {

                    Toast myToast2 = Toast.makeText(this.getApplicationContext(), "삭제 할 리뷰 : " + reviewDto.toString(), Toast.LENGTH_SHORT);
                    myToast2.show();
                    System.out.println(reviewDto.toString());
                    choose_review.add(reviewDto);


                }
            }
            delete_RequestDto.setReview_list(choose_review);
            delete_RequestDto.setToken(token);
            delete_RequestDto.setRequest_msg("DeleteUserReview");
            flag = false;
            delete_ResponseDto = new ReviewTask(delete_RequestDto).execute().get();//서버에서 놀거리 카테고리와 질문을 받아와야만 view에 날씨 질문과 놀거리를 보여준다.
            if (responseDto != null) {
                flag = true;
                //System.out.println(delete_ResponseDto.toString());


            }

            if (flag) {

                if (delete_ResponseDto.getResponse_msg().equals("DeleteUserReview_success")) {

                    Toast myToast = Toast.makeText(this.getApplicationContext(), "리뷰 삭제 완료", Toast.LENGTH_SHORT);
                    myToast.show();

                    Intent intent = new Intent(getApplicationContext(), UserReviewActivity.class);
                    startActivity(intent);
                    finish();

                }// if responce meg
                else {
                    Toast myToast = Toast.makeText(this.getApplicationContext(), "리뷰 삭제 실패", Toast.LENGTH_SHORT);
                    myToast.show();
                }

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}



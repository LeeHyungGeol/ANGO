package com.e.ango;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.e.ango.Review.DetailInfoPlayObject;
import com.e.ango.Review.DetailInfoTask;

import java.util.concurrent.ExecutionException;

public class DetailInfoActivity extends AppCompatActivity {

    String xCoordinate;
    String yCoordinate;
    String category_id;
    long content_id;
    String title;
    DetailInfoPlayObject detailInfoPlayObject;

    ImageView image_detailInfoImage;
    Bitmap bitmap;
    TextView text_Title;
    TextView text_Addr1;
    TextView text_Addr2;
    TextView text_HomePage;
    TextView text_Overview;
    TextView text_Tel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);

        Intent intent = getIntent();
        content_id = intent.getExtras().getLong("content_id", content_id);
        category_id = intent.getExtras().getString("category_id", category_id);
        xCoordinate = intent.getExtras().getString("xCoordinate",xCoordinate);
        yCoordinate = intent.getExtras().getString("xCoordinate",yCoordinate);

        byte[] byteArray = intent.getByteArrayExtra("image");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        try {
            detailInfoPlayObject = new DetailInfoTask(String.valueOf(content_id)).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        image_detailInfoImage = findViewById(R.id.imageView_detailInfoImage);
        image_detailInfoImage.setImageBitmap(bitmap);

        text_Title = findViewById(R.id.text_title);
        text_Addr1 = findViewById(R.id.text_addr1);
        text_Addr2 = findViewById(R.id.text_addr2);
        text_HomePage = findViewById(R.id.text_homePage);
        text_Overview = findViewById(R.id.text_overView);
        text_Tel = findViewById(R.id.text_tel);

        text_Title.setText(detailInfoPlayObject.getTitle());
        text_Addr1.setText(detailInfoPlayObject.getAddr1());
        text_Addr2.setText(detailInfoPlayObject.getAddr2());
        text_HomePage.setText(detailInfoPlayObject.getHomepage());
        text_Overview.setText(detailInfoPlayObject.getOverview());
        text_Tel.setText(detailInfoPlayObject.getTel());

    }

    public void reviewButtonClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ContentReviewActivity.class);

        title = detailInfoPlayObject.getTitle();
        intent.putExtra("content_id", String.valueOf(content_id));
        intent.putExtra("category_id", category_id);
        intent.putExtra("title", title);
        intent.putExtra("xCoordinate",xCoordinate);
        intent.putExtra("yCoordinate",yCoordinate);
        startActivity(intent);

    }
}

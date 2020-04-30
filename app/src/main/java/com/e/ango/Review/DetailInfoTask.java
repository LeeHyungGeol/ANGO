package com.e.ango.Review;

import android.os.AsyncTask;


import com.e.ango.API.Play.Item;
import com.e.ango.API.Play.PlayDto;

import com.e.ango.Review.DetailInfoPlay.DetailInfoPlayDto;
import com.e.ango.Review.DetailInfoPlay.Items;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailInfoTask extends AsyncTask<Void, Void, DetailInfoPlayObject> {
    OkHttpClient client = new OkHttpClient();
    String content_id;
    DetailInfoPlayObject detailInfoPlayObject;

    public DetailInfoTask(String content_id) {
        this.content_id = content_id;
    }

    @Override
    protected DetailInfoPlayObject doInBackground(Void... voids) {
        System.out.println("DetailInfoTask ContentID : " + content_id);
        String urlPlay = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?ServiceKey=VvSYrDrc4pOEEbTc61UkhlbLjj9a1JCbxq3dRy24%2BRftOs9iNCGlQ%2F5W%2FkKBsW4PA6mBS%2BQ20fBc%2BjQWV7rabg%3D%3D&" +
                "contentId=" + content_id + "&defaultYN=Y&MobileOS=ETC&MobileApp=AppTest&_type=json";
        try {

            Request request = new Request.Builder()
                    .url(urlPlay)
                    .build();

            Response response = client.newCall(request).execute();
            Gson gson = new Gson();

            DetailInfoPlayDto detailInfoPlayDto = gson.fromJson(response.body().string(), DetailInfoPlayDto.class);

            System.out.println("detailInfoPlayObject : " + detailInfoPlayObject);

            com.e.ango.Review.DetailInfoPlay.Response detailInfoPlayResponse = detailInfoPlayDto.response;
            com.e.ango.Review.DetailInfoPlay.Body body = detailInfoPlayResponse.body;
            com.e.ango.Review.DetailInfoPlay.Items items = body.items;
            Item it = items.item;

            detailInfoPlayObject = new DetailInfoPlayObject(it.title, it.homepage, it.tel, it.overview, it.addr1, it.addr2);
            System.out.println("detailInfoPlayObject : " + detailInfoPlayObject);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("play1");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.out.println("play2");
        }

        return detailInfoPlayObject;
    }
}

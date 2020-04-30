package com.e.ango.SearchArea;

import android.os.AsyncTask;

import com.e.ango.API.Play.Item;
import com.e.ango.API.Play.Items;
import com.e.ango.API.Play.PlayObject;
import com.e.ango.API.Play.PlayDto;
import com.e.ango.Request.PreferDto;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SearchAreaPlayTask extends AsyncTask<Void, Void, ArrayList<PlayObject>> {

    OkHttpClient client = new OkHttpClient();

    ArrayList<PreferDto> prefer_list;
    String areaCode;
    public ArrayList<PlayObject> userPreferencePlayObjects = null;

    public SearchAreaPlayTask(ArrayList<PreferDto> prefer_list, String areaCode) {
        this.prefer_list = prefer_list;
        this.areaCode = areaCode;
    }

    @Override
    protected ArrayList<PlayObject> doInBackground(Void... voids) {

        try {
            for (int i = 0; i < prefer_list.size(); i++) {
                String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=VvSYrDrc4pOEEbTc61UkhlbLjj9a1JCbxq3dRy24%2BRftOs9iNCGlQ%2F5W%2FkKBsW4PA6mBS%2BQ20fBc%2BjQWV7rabg%3D%3D&" +
                        "areaCode=1&sigunguCode=" + areaCode +"&" + prefer_list.get(i).getCg_id() + "&MobileOS=AND&MobileApp=AppTest&numOfRows=30&pageNo=1&_type=json";


                okhttp3.Request request = new Request.Builder()
                        .url(url)
                        .build();

                okhttp3.Response response = client.newCall(request).execute();

                Gson gson = new Gson();
                PlayDto playDto = gson.fromJson(response.body().string(), PlayDto.class);

                com.e.ango.API.Play.Response responsePlay = playDto.response;
                com.e.ango.API.Play.Body body = responsePlay.body;
                Items items = body.items;
                ArrayList<Item> item = items.item;

                userPreferencePlayObjects = new ArrayList<>();
                for (int j = 0; j < item.size(); j++) {
                    Item it = item.get(j);

                    userPreferencePlayObjects.add(new PlayObject(it.addr1, it.addr2, it.cat2, it.cat3,
                            it.contentid,it.firstimage2, it.title, it.mapx, it.mapy, prefer_list.get(j).getCategory_nm(), prefer_list.get(j).getCg_id()));

                }
            }


        } catch (Exception e) {

            e.printStackTrace();
        }
        return userPreferencePlayObjects;
    }

    @Override
    protected void onPostExecute(ArrayList<PlayObject> s) {
        super.onPostExecute(s);
    }
}
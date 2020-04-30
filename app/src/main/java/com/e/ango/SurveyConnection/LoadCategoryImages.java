package com.e.ango.SurveyConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.e.ango.API.Play.Response;
import com.e.ango.CustomAnimationDialog;
import com.e.ango.Response.ResponseDto;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import static com.e.ango.Login.LoginTask.ip;


public class LoadCategoryImages extends AsyncTask<Void, String, ArrayList<Bitmap>> {
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();
    ResponseDto responseDto;
    Context context;
    ProgressDialog asyncDialog;
    ProgressBar progressBar;
    CustomAnimationDialog customAnimationDialog;

    public LoadCategoryImages(ResponseDto responseDto, Context context) {
        this.responseDto = responseDto;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//        asyncDialog = new ProgressDialog(context);
//        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        asyncDialog.setMessage("로딩중입니다..");
//
//        // show dialog
//        asyncDialog.show();
//        progressBar = new ProgressBar(context);
//        progressBar.show();
        customAnimationDialog = new CustomAnimationDialog(context);
        customAnimationDialog.show();

    }

    @Override
    protected ArrayList<Bitmap> doInBackground(Void... voids) {
        try {
            for (int i = 0; i < responseDto.getPrefer_list().size(); i++) {
                urls.add("http://" + ip + ":8090/final_ango/images/" + responseDto.getPrefer_list().get(i).getCg_id() + ".jpg");
                bitmaps.add(BitmapFactory.decodeStream((InputStream) new URL(urls.get(i)).getContent()));
                publishProgress("" + i / responseDto.getPrefer_list().size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmaps;

    }

    @Override
    protected void onProgressUpdate(String... progress) { //4
        super.onProgressUpdate(progress);

        customAnimationDialog.show();
        // if we get here, length is known, now set indeterminate to false
//        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        asyncDialog.setMessage("로딩중입니다..");
    }


    @Override
    protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
        super.onPostExecute(bitmaps);
        //asyncDialog.dismiss();
        customAnimationDialog.dismiss();
    }
}

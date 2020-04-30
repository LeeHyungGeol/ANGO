package com.e.ango.SurveyConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

public class LoadImage extends AsyncTask<String, String, Bitmap> {
    Bitmap bitmap;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... args) {
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
    }
}

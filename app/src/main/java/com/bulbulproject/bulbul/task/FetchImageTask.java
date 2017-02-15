package com.bulbulproject.bulbul.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.io.InputStream;

public class FetchImageTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView mImage;

    public FetchImageTask(ImageView mImage) {
        this.mImage = mImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mBitmap = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mBitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        mImage.setImageBitmap(result);
    }
}


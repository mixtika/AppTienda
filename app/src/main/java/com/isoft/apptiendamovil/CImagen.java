package com.isoft.apptiendamovil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.HttpURLConnection;
import java.net.URL;

public class CImagen {
    public static Bitmap getImagen(String url)
    {
        URL imageUrl = null;
        try {
            imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            Bitmap bmp = BitmapFactory.decodeStream(conn.getInputStream());
            return bmp;
        }catch (Exception ex){}
        return null;
    }
}
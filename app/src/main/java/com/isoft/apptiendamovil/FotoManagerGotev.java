package com.isoft.apptiendamovil;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.ListPreference;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by user on 01/11/2017. 01
 */

public abstract class FotoManagerGotev {

    public abstract void onProgress(UploadInfo uploadInfo);
    public abstract void onError(UploadInfo uploadInfo, Exception exception);
    public abstract void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse);
    public abstract void onCancelled(UploadInfo uploadInfo);

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int GALERIA = 50;
    private static final int CAMERA = 100;
    private static String APP_DIRECTORY = "Temporales/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "Fotos";
    private boolean eliminarFoto = true;
    private String pathCamera = "";
    private String URL = "";
    private String idPhoto="";
    private Activity activity;
    private String parameterNamePhoto;
    //private ArrayList<CParametros> parametros;
    private Bitmap bitmap=null;
    MultipartUploadRequest start;

    public void startUpload(ArrayList<CParametros> parametros)
    {
        CParametros t;
        try
        {
            for(int i=0;i<parametros.size();i++)
            {
                t=parametros.get(i);
                start.addParameter(t.getVar(),t.getValue());
            }
            start.startUpload();
        } catch (Exception exc) {
            Toast.makeText(activity,"Error: "+exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public FotoManagerGotev(Activity activity, String URL) {
        //this.parametros=parametros;

        this.activity=activity;
        this.URL = URL;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public String getParameterNamePhoto() {
        return parameterNamePhoto;
    }

    public String getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(String idPhoto) {
        this.idPhoto = idPhoto;
    }

    public void setParameterNamePhoto(String parameterNamePhoto) {
        this.parameterNamePhoto = parameterNamePhoto;
    }

    public boolean isEliminarFoto() {
        return eliminarFoto;
    }

    public void setEliminarFoto(boolean eliminarFoto) {
        this.eliminarFoto = eliminarFoto;
    }

    public void subirFotoGaleria(){
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/jpeg");
        //i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        activity.startActivityForResult(Intent.createChooser(i,"Selecciona una foto"), GALERIA);
    }

    public void subirFotoCamara(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        pathCamera = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator +"temp.jpg";
        File newFile = new File(pathCamera);
        File directorio = new File(Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator);
        directorio.mkdirs();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
        activity.startActivityForResult(intent, CAMERA);
    }

    //resultado al momento de elegir una foto
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        try {
            String uploadId = UUID.randomUUID().toString();
            MultipartUploadRequest request = new MultipartUploadRequest(activity,uploadId, URL);
            if(requestCode == GALERIA && resultCode == Activity.RESULT_OK){
                Uri u = data.getData();
                /*mod*/
                try {
                    InputStream is = activity.getContentResolver().openInputStream(u);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bitmap = BitmapFactory.decodeStream(bis);
                    //picture.setImageBitmap(bitmap);
                }catch (Exception ex){}
                /*mod*/

                request.addFileToUpload(getPath(u), getParameterNamePhoto());
            }else if(requestCode == CAMERA && resultCode == Activity.RESULT_OK){
                /*mod*/
                File xy=new File(pathCamera);
                bitmap = BitmapFactory.decodeFile(xy.getPath());
                /*mod*/
                request.addFileToUpload(pathCamera,getParameterNamePhoto());
                request.setAutoDeleteFilesAfterSuccessfulUpload(eliminarFoto);
            }
            ///eliminado
            request.setNotificationConfig(new UploadNotificationConfig());
            request.setMaxRetries(2);
            request.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(UploadInfo uploadInfo) {
                    FotoManagerGotev.this.onProgress(uploadInfo);
                }
                @Override
                public void onError(UploadInfo uploadInfo, Exception exception) {
                    FotoManagerGotev.this.onError(uploadInfo, exception);
                }
                @Override
                public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                    FotoManagerGotev.this.onCompleted(uploadInfo,serverResponse);
                }
                @Override
                public void onCancelled(UploadInfo uploadInfo) {
                    FotoManagerGotev.this.onCancelled(uploadInfo);
                }
            });
            start=request;
            ////////request.startUpload();
        } catch (Exception exc) {
            Toast.makeText(activity,"Error: "+exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Con este metodo obtenemos la ruta de una imagen
    public String getPath(Uri uri) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = activity.getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        assert cursor != null;
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    //Verificamos los permisos de nuestra aplicacion para leer y escribir
    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}

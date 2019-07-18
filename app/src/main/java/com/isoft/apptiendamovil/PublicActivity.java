package com.isoft.apptiendamovil;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;

import java.util.ArrayList;

public class PublicActivity extends AppCompatActivity {

    ImageView picture;
    FotoManagerGotev fotoManagerGotev;
    Intent data;
    EditText titulo, descripcion, cantidad, precio;
    String scorreo;
    Context root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);
        root=this;
        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.descripcion);
        cantidad = findViewById(R.id.cantidad);
        precio = findViewById(R.id.precio);
        picture=(ImageView)findViewById(R.id.imageView);
        scorreo = getIntent().getExtras().getString("correo");


        fotoManagerGotev= new FotoManagerGotev(this, UrlConnection.URL + "/producto") {
            @Override
            public void onProgress(UploadInfo uploadInfo) {

            }

            @Override
            public void onError(UploadInfo uploadInfo, Exception exception) {
                Toast.makeText(root,"Problema al publicar.",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                Toast.makeText(root,"Producto publicado con exito.",Toast.LENGTH_LONG).show();
                titulo.setText("");
                titulo.requestFocus();
            }

            @Override
            public void onCancelled(UploadInfo uploadInfo) {

            }
        };
        fotoManagerGotev.setParameterNamePhoto("file");
        fotoManagerGotev.setEliminarFoto(true);
    }

    public void abrirGaleria(View view) {
        fotoManagerGotev.subirFotoGaleria();
    }

    public void abrirCamara(View view) {
        fotoManagerGotev.subirFotoCamara();
    }

    public void publicarProducto(View view) {
        ArrayList<CParametros> cparam = new ArrayList<CParametros>();
        if (titulo.getText().length() > 2) {
            cparam.add(new CParametros("titulo", titulo.getText()+""));
            cparam.add(new CParametros("descripcion", descripcion.getText()+""));
            cparam.add(new CParametros("cantidad", cantidad.getText()+""));
            cparam.add(new CParametros("precio", precio.getText()+""));
            cparam.add(new CParametros("correo", scorreo));
            fotoManagerGotev.startUpload(cparam);
        } else {
            titulo.setError("Titulo muy corto");
            titulo.requestFocus();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fotoManagerGotev.onActivityResult(requestCode, resultCode, data);
        picture.setImageBitmap(fotoManagerGotev.getBitmap());
    }
}

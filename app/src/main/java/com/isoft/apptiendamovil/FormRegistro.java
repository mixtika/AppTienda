package com.isoft.apptiendamovil;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FormRegistro extends AppCompatActivity {

    private EditText nombres,apellidos,correo,fono;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_registro);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        nombres = (EditText)findViewById(R.id.nombres);
        apellidos = (EditText)findViewById(R.id.apellidos);
        correo = (EditText)findViewById(R.id.correo);
        fono = (EditText)findViewById(R.id.fono);
    }

    public void btnSaveRegister(View view) {

        GetServer ob = new GetServer();
        //validación
        String params="nombres="+nombres.getText()+"&apellidos="+apellidos.getText()+"&correo="+correo.getText()+"&fono="+fono.getText();
        String cad=ob.stringQuery("/save_data?"+params,"POST");

        Toast.makeText(getApplicationContext(),"Se registor su usuario",Toast.LENGTH_LONG).show();
    }
}

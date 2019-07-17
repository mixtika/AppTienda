package com.isoft.apptiendamovil;

import android.os.Bundle;
import android.os.StrictMode;
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
        /*if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/
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

        nombres.setError("el nombre");
        nombres.requestFocus();

        GetServer ob = new GetServer();
        //validación
        //http://localhost:3000/api/v1.0/save_data?nombres=silvia&apellidos=coro&correo=asda&fono=724

        String params="nombres="+nombres.getText()+"&apellidos="+apellidos.getText()+"&correo="+correo.getText()+"&fono="+fono.getText();
        //Toast.makeText(getApplicationContext(),params,Toast.LENGTH_LONG).show();

        ///String cad=ob.stringQuery("/save_data?"+params,"POST");

        ///Toast.makeText(getApplicationContext(),"Se registor su usuario"+cad,Toast.LENGTH_LONG).show();
    }
}

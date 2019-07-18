package com.isoft.apptiendamovil;

import android.os.Build;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegistroActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MarkerOptions mko;

    private String slat, slog, scorreo;
    private EditText nombres, apellidos, correo, edad;
    private RadioButton fem,mas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_registro);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        nombres = findViewById(R.id.nombres);
        apellidos = findViewById(R.id.apellidos);
        correo = findViewById(R.id.correo);
        edad =findViewById(R.id.edad);
        mas = findViewById(R.id.sexom);
        fem = findViewById(R.id.sexom);
        scorreo = getIntent().getExtras().getString("correo");
        correo.setText(scorreo);

        /*LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mMap.setMyLocationEnabled(true);
            }
        });*/
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng potosi = new LatLng(-19.5723589,-65.7617494);
        CameraPosition cameraPosition = CameraPosition.builder().target(potosi).zoom(16).build();
        mko = new MarkerOptions().position(potosi).title("Marcador en Potosi").draggable(true);
        mMap.addMarker(mko);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void registrarDatos(View view) {
        if(nombres.getText().toString().length()>2) //validacion
        {
            GetServer ob = new GetServer();
            String sexo="F";
            scorreo=correo.getText()+"";
            if(mas.isChecked())
                sexo="M";
            String cad= ob.stringQuery("/verificar?correo="+scorreo,"POST");
            try {
                JSONObject json = new JSONObject(cad);
                JSONArray array = json.optJSONArray("users");
                if (array.length() > 0)
                    Toast.makeText(getApplicationContext(),"Usted ya se registro...",Toast.LENGTH_LONG).show();
                else
                {
                    String params="nombres="+nombres.getText()+"&apellidos="+apellidos.getText()+"&correo="+scorreo+"&sexo="+sexo+"&edad="+edad.getText()+"&lat="+ mko.getPosition().latitude+"&log="+ mko.getPosition().longitude;
                    cad=ob.stringQuery("/registro?"+params,"POST");
                    Toast.makeText(getApplicationContext(),"Se registor su datos: "+cad,Toast.LENGTH_LONG).show();
                }
            }catch (Exception ex){}

        }
        else
        {
            nombres.setError("Nombre muy corto");
            nombres.requestFocus();
        }
    }
}

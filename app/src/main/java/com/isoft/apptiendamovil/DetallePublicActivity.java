package com.isoft.apptiendamovil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class DetallePublicActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String id_producto;
    private GoogleMap mMap;
    private MarkerOptions mko;
    private String slat, slog, cad1,cad2;

    TextView nombres, correo, titulo, descripcion, cantidad, precio, fecha;
    Boolean estado;
    ImageView picture;
    int cant;
    String scorreo,vendedor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_public);
        id_producto = getIntent().getExtras().getString("id_producto");
        scorreo = getIntent().getExtras().getString("correo");
        titulo=findViewById(R.id.txtTitulo);
        descripcion=findViewById(R.id.txtDescripcion);
        cantidad=findViewById(R.id.txtCantidad);
        precio=findViewById(R.id.txtPrecio);
        fecha=findViewById(R.id.txtFecha);
        picture=(ImageView)findViewById(R.id.imageDetalle);
        nombres=findViewById(R.id.txtVendedor);
        correo=findViewById(R.id.txtCorreo);
        cant=0;
        slat=slog="";
        vendedor="";
        cargarDatos();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    private void cargarDatos()
    {
        GetServer ob=new GetServer();
        cad2=ob.stringQuery("/buscarpubid?id="+id_producto,"POST");
        try
        {
            JSONObject json=new JSONObject(cad2);
            JSONArray array=json.optJSONArray("producto");
            JSONObject hijo = array.getJSONObject(0);
            vendedor=hijo.optString("usuario");
            cad1=ob.stringQuery("/buscarusuario?id="+vendedor,"POST");
            titulo.setText(hijo.optString("titulo"));
            descripcion.setText(hijo.optString("descripcion"));
            cant=hijo.optInt("cantidad");
            cantidad.setText("Cantidad: "+ cant);
            precio.setText("Precio: "+ hijo.optDouble("precio")+" Bs.");
            fecha.setText(hijo.optString("fecha_registro"));
            picture.setImageBitmap(CImagen.getImagen(hijo.optString("foto")));

            json=new JSONObject(cad1);
            array=json.optJSONArray("users");
            hijo = array.getJSONObject(0);
            nombres.setText("Vendedor: "+hijo.optString("nombres")+" "+hijo.optString("apellidos"));
            correo.setText(hijo.optString("correo"));
            slat=hijo.optString("lat");
            slog=hijo.optString("log");

        }catch (Exception ex){}
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng usuario = new LatLng(Double.parseDouble(slat),Double.parseDouble(slog));
        CameraPosition cameraPosition = CameraPosition.builder().target(usuario).zoom(16).build();
        mko = new MarkerOptions().position(usuario).title("Ubicacion del Vendedor").draggable(true);
        mMap.addMarker(mko);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void addCarrito(View view) {
        if(cant>0) {
            cant--;
            cantidad.setText("Cantidad: "+cant);
            Toast.makeText(this,"Producto añadido con exito",Toast.LENGTH_SHORT).show();
            GetServer ob=new GetServer();

            ob.stringQuery("/editcantidad?id="+id_producto,"POST");

            String params="correo="+scorreo+"&vendedor="+vendedor+"&producto="+id_producto+"&cantidad=1";
            ob.stringQuery("/agregarfavorito?"+params,"POST");
        }
        else {
            Toast.makeText(this,"No existe producto disponible",Toast.LENGTH_LONG).show();
        }

    }
}

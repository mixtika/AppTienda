package com.isoft.apptiendamovil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListPublicActivity extends AppCompatActivity {
    private List<CProducto> items = new ArrayList<>();
    private MyListProducto adapter;
    private ListView list;
    private CProducto productob=null;
    private String cad, scorreo;
    private EditText texto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_public);

        scorreo = getIntent().getExtras().getString("correo");
        list =(ListView)findViewById(R.id.lista);
        texto=findViewById(R.id.txtBuscar);
        GetServer obs=new GetServer();
        cad=obs.stringQuery("/publicaciones","POST");
        try
        {
            JSONObject json=new JSONObject(cad);
            JSONArray array=json.optJSONArray("productos");
            for(int i=0;i<array.length();i++)
            {
                JSONObject hijo = array.getJSONObject(i);
                items.add(new CProducto(hijo.optString("titulo"),hijo.optString("descripcion"),hijo.optInt("cantidad"),hijo.optDouble("precio"),hijo.optString("foto"),hijo.optString("fecha_registro"),hijo.optBoolean("estado"),hijo.optString("_id"),hijo.optString("usuario")));
            }
        }catch (Exception ex){}

        adapter = new MyListProducto(this, items);
        list.setAdapter(adapter);
        registerForContextMenu(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),view.getId()+"", Toast.LENGTH_SHORT).show();
                CProducto item = items.get(position);
                Intent ob=new Intent(getApplicationContext(), DetallePublicActivity.class);
                ob.putExtra("id_producto",item.get_id());
                ob.putExtra("correo",scorreo);
                startActivity(ob);
                //item.setCantidad(item.getCantidad()+1);
                //adapter.notifyDataSetChanged();
            }
        });
    }

    public void Buscar(View view) {
        items.clear();
        GetServer obs=new GetServer();
        cad=obs.stringQuery("/buscarpub?texto="+texto.getText(),"POST");
        try
        {
            JSONObject json=new JSONObject(cad);
            JSONArray array=json.optJSONArray("productos");
            for(int i=0;i<array.length();i++)
            {
                JSONObject hijo = array.getJSONObject(i);
                items.add(new CProducto(hijo.optString("titulo"),hijo.optString("descripcion"),hijo.optInt("cantidad"),hijo.optDouble("precio"),hijo.optString("foto"),hijo.optString("fecha_registro"),hijo.optBoolean("estado"),hijo.optString("_id"),hijo.optString("usuario")));
            }
        }catch (Exception ex){}
        //items.add(new Fruta("A"+items.size(),"alguna descripción",0));
        adapter.notifyDataSetChanged();
    }
}

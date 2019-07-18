package com.isoft.apptiendamovil;

import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyPublicActivity extends AppCompatActivity {
    private CProducto fruta[];
    private int i;

    List<CProducto> items = new ArrayList<>();
    MyListProducto adapter;

    ListView list; // test

    private CProducto productob=null;

    String cad, scorreo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/
        setContentView(R.layout.activity_my_public);
        scorreo = getIntent().getExtras().getString("correo");
        list =(ListView)findViewById(R.id.lista);

        GetServer obs=new GetServer();
        cad=obs.stringQuery("/mispublicaciones?correo="+scorreo,"POST");
        try
        {
            JSONObject json=new JSONObject(cad);
            JSONArray array=json.optJSONArray("productos");
            //menu=new CMenu[array.length()];
            for(int i=0;i<array.length();i++)
            {
                JSONObject hijo = array.getJSONObject(i);
                items.add(new CProducto(hijo.optString("titulo"),hijo.optString("descripcion"),hijo.optInt("cantidad"),hijo.optDouble("precio"),hijo.optString("foto"),hijo.optString("fecha_registro"),hijo.optBoolean("estado"),hijo.optString("_id"),hijo.optString("usuario")));
                //menu[i]=new CMenu((i+1),hijo.optString("_id"),hijo.optString("name"),hijo.optString("description"),hijo.optDouble("price"),hijo.optString("picture"),id_restaurant);
            }
        }catch (Exception ex){}

        //items.add(new CProducto("C++", "Lenguaje de Programacion", 100, 100.0, "", "", true, "", ""));
        //items.add(new CProducto("Java", "Lenguaje de Programacion", 100, 100.0, "", "", true, "", ""));
        //items.add(new CProducto("java", "Lenguaje de Programacion", "1000", "100.0", "", "", "01-01-2019", "", ""));


        adapter = new MyListProducto(this, items);
        list.setAdapter(adapter);
        registerForContextMenu(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),view.getId()+"", Toast.LENGTH_SHORT).show();
                CProducto item = items.get(position);
                item.setCantidad(item.getCantidad()+1);
                adapter.notifyDataSetChanged();
            }
        });

    }
    public void addProducto()
    {
        //adapter.add(new Fruta("A"+items.size(),"alguna descripción",0));
        //items.add(new Fruta("A"+items.size(),"alguna descripción",0));
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        list = (ListView) v;
        AdapterContextMenuInfo adx = (AdapterContextMenuInfo) menuInfo;
        productob = (CProducto) list.getItemAtPosition(adx.position);
        menu.setHeaderTitle(productob.getTitulo());

        menu.add(0, v.getId(), 0, "Borrar");
        menu.add(0, v.getId(), 0, "Reset");
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_main, menu);




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        addProducto();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Borrar") {
            items.remove(productob);
            adapter.notifyDataSetChanged();
        }
        else {
            productob.setCantidad(0);
            adapter.notifyDataSetChanged();
        }
        return true;
    }
}

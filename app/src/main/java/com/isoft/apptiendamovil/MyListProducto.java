package com.isoft.apptiendamovil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class MyListProducto extends ArrayAdapter<CProducto> {

    //private LayoutInflater inflater;
    private List<CProducto> producto;
    //Context context;

    public MyListProducto(Context context, List<CProducto> producto) {
        super(context, R.layout.my_list_producto, producto);
        this.producto = producto;
    }

    @Override
    public View getView(int i, View cview, ViewGroup viewGroup) {

        CProducto item = producto.get(i);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.my_list_producto, null);
        ImageView picture=(ImageView) view.findViewById(R.id.picture);
        TextView titulo=view.findViewById(R.id.titulo);
        TextView description=(TextView) view.findViewById(R.id.descripcion);
        TextView cantidad=(TextView) view.findViewById(R.id.cantidad);
        TextView precio=(TextView) view.findViewById(R.id.precio);

        picture.setImageBitmap(CImagen.getImagen(item.getFoto()));
        //picture.setImageResource(R.drawable.ic_menu_camera);
        titulo.setText(item.getTitulo());
        description.setText(item.getDescription());
        cantidad.setText(item.getCantidad());
        precio.setText(item.getPrecio()+"");
        return view;
    }
}
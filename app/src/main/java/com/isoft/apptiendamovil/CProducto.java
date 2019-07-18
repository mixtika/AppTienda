package com.isoft.apptiendamovil;

public class CProducto {
    private String titulo, description;
    private int cantidad;
    private double precio;
    String foto ,fecha_registro;
    private boolean estado;
    private String _id, usuario;
    public CProducto() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public CProducto(String titulo, String description, int cantidad, double precio, String foto, String fecha_registro, boolean estado, String _id, String usuario) {
        this.titulo = titulo;
        this.description = description;
        this.cantidad = cantidad;
        this.precio = precio;
        this.foto = foto;
        this.fecha_registro = fecha_registro;
        this.estado = estado;
        this._id = _id;
        this.usuario = usuario;
    }
}

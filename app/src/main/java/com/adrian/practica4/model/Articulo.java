package com.adrian.practica4.model;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Articulo {
    public static final String NAME ="tb_articulo", CODIGO = "codigo", DESCRIPCION = "descripcion", PRECIO ="precio";
    private static final String TAG = "TAG_ARTICULO";

    private int codigo;
    private String descripcion;
    private Double precio;

    public Articulo(int codigo, String descripcion, Double precio) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public ContentValues getContentValues(boolean insert){
        ContentValues values = new ContentValues();
        if(!insert)
            values.put(CODIGO, codigo);
        values.put(DESCRIPCION, descripcion);
        values.put(PRECIO, precio);
        return values;
    }

    public static List<Articulo> getList(JSONArray array){
        List<Articulo> articulos = new ArrayList<>();
        for (int a=0;a<array.length();a++){
            try {
                JSONObject object = array.getJSONObject(a);
                articulos.add(new Articulo(object.getInt(CODIGO),object.getString(DESCRIPCION),object.getDouble(PRECIO)));
            } catch (JSONException e) {
                Log.e(TAG, "getList: ", e);
                continue;
            }
        }
        return  articulos;
    }

    public static String[] getArray(){
        return new String[] {CODIGO, DESCRIPCION, PRECIO};
    }
    public static String getSelect(){
        return MessageFormat.format("SELECT {0},{1},{2} FROM {3}",CODIGO, DESCRIPCION, PRECIO,NAME);
    }

    public static String getColumns(){
        return MessageFormat.format("{0} INTEGER PRIMARY KEY AUTOINCREMENT , {1} TEXT NOT NULL, {2} REAL NOT NULL", getArray());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Articulo)) return false;
        Articulo articulo = (Articulo) o;
        return codigo == articulo.codigo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}

package com.example.paseasistencia.model;

import android.database.Cursor;

public class TiposPermisos {
    private Integer id;
    private String nombre;

    public TiposPermisos(){

    }

    public TiposPermisos(String nombre) {
        this.nombre = nombre;
    }

    public TiposPermisos(Cursor cursor) {
        this.id = cursor.getInt(0);
        this.nombre = cursor.getString(1);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

package com.example.paseasistencia.model;

import android.database.Cursor;

public class Configuracion {
    private Long id;
    private String Url;


    public Configuracion(String url) {
        Url = url;
    }

    public Configuracion(Long id, String url) {
        this.id = id;
        Url = url;
    }

    public Configuracion(Cursor cursor) {
        this.id = Long.parseLong(cursor.getString(0));
        Url = cursor.getString(1);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    @Override
    public String toString() {
        return "Configuracion{ Url='" + Url + '\''+'}';
    }


}

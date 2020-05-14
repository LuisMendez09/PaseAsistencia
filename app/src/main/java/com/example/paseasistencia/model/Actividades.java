package com.example.paseasistencia.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Actividades implements Parcelable {
    private Long id;
    private String Nombre;

    public Actividades (){

    }

    public Actividades(Long id,String Nombre) {
        this.id=id;
        this.Nombre = Nombre;
    }

    public Actividades(String Nombre) {
        this.Nombre = Nombre;
    }

    public Actividades(Cursor cursor) {
        this.id = cursor.getLong(0);
        this.Nombre = cursor.getString(1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }


    @Override
    public String toString() {
        return Nombre;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.Nombre);
    }

    protected Actividades(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.Nombre = in.readString();
    }

    public static final Creator<Actividades> CREATOR = new Creator<Actividades>() {
        @Override
        public Actividades createFromParcel(Parcel source) {
            return new Actividades(source);
        }

        @Override
        public Actividades[] newArray(int size) {
            return new Actividades[size];
        }
    };
}

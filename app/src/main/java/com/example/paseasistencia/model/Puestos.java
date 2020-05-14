package com.example.paseasistencia.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Puestos implements Parcelable {
    private String Nombre;
    private Integer id;
    //private Integer tipoActividad;

    public Puestos(){

    }

    public Puestos(String nombre, Integer id) {
        this.Nombre = nombre;
        this.id = id;
    }

    public Puestos(String Nombre) {
        this.Nombre = Nombre;
    }

    public Puestos(Cursor cursor) {
        this.id = cursor.getInt(0);
        this.Nombre = cursor.getString(1);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    /*public void setTipoActividad(Integer tipoActividad) {
        this.tipoActividad = tipoActividad;
    }*/

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
        dest.writeString(this.Nombre);
        dest.writeValue(this.id);
    }

    protected Puestos(Parcel in) {
        this.Nombre = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Puestos> CREATOR = new Parcelable.Creator<Puestos>() {
        @Override
        public Puestos createFromParcel(Parcel source) {
            return new Puestos(source);
        }

        @Override
        public Puestos[] newArray(int size) {
            return new Puestos[size];
        }
    };
}

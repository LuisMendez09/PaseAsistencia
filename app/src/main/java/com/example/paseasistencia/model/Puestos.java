package com.example.paseasistencia.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Puestos implements Parcelable, Comparable<Puestos> {

    private String Nombre;
    private Integer id;
    private Integer orden;

    public Puestos(){

    }

 /*   public Puestos(String nombre, Integer id) {
        this.Nombre = nombre;
        this.id = id;
    }

    public Puestos(String Nombre) {
        this.Nombre = Nombre;
    }*/

    public Puestos(Cursor cursor) {
        this.id = cursor.getInt(0);
        this.Nombre = cursor.getString(1);

        if (cursor.getColumnCount() == 2) {
            switch (id) {
                case 1:
                    this.orden = 3;
                    break;
                case 2:
                    this.orden = 11;
                    break;
                case 3:
                    this.orden = 1;
                    break;
                case 4:
                    this.orden = 2;
                    break;
                case 5:
                    this.orden = 6;
                    break;
                case 6:
                    this.orden = 5;
                    break;
                case 7:
                    this.orden = 9;
                    break;
                case 8:
                    this.orden = 10;
                    break;
                case 9:
                    this.orden = 4;
                    break;
                case 10:
                    this.orden = 8;
                    break;
                case 11:
                    this.orden = 7;
                    break;
            }
        }
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


    public String toString1() {
        return "Puestos{" +
                "Nombre='" + Nombre + '\'' +
                ", id=" + id +
                '}';
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

    @Override
    public int compareTo(Puestos o) {
        return this.orden.compareTo(o.orden);
    }
}

package com.example.paseasistencia.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class TiposActividades implements Parcelable {
    Integer id;
    String descripcion;

    public TiposActividades() {
    }

    public TiposActividades(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public TiposActividades(Cursor cursor) {
        this.id = cursor.getInt(0);
        this.descripcion = cursor.getString(1);
    }

    public Integer getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.descripcion);
    }

    protected TiposActividades(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.descripcion = in.readString();
    }

    public static final Parcelable.Creator<TiposActividades> CREATOR = new Parcelable.Creator<TiposActividades>() {
        @Override
        public TiposActividades createFromParcel(Parcel source) {
            return new TiposActividades(source);
        }

        @Override
        public TiposActividades[] newArray(int size) {
            return new TiposActividades[size];
        }
    };
}

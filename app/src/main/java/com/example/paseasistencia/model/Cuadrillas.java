package com.example.paseasistencia.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.paseasistencia.complementos.Complementos;

import java.util.Date;

public class Cuadrillas implements Parcelable {
    private Integer id;
    private Integer cuadrilla;
    private String mayordomo;
    private Date fechaInicio;
    private Date fechaFin;
    private String fecha;

    public Cuadrillas(Cursor c){
        this.id = c.getInt(0);
        this.cuadrilla = c.getInt(1);
        this.mayordomo = c.getString(2);
        this.fechaInicio = new Date(c.getLong(3));
        this.fecha = c.getString(4);
        this.fechaFin = new Date(c.getLong(5));
    }

    public Cuadrillas(Integer cuadrilla,String mayordomo){
        this.cuadrilla = cuadrilla;
        this.mayordomo = mayordomo;
    }

    public Cuadrillas(Integer cuadrilla, String mayordomo,Date fechaInicio,Date fechaFin) {
        this.cuadrilla = cuadrilla;
        this.mayordomo = mayordomo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Cuadrillas(String cuadrilla, String mayordomo,Date fechaInicio,Date fechaFin) {
        this.cuadrilla = Integer.parseInt(cuadrilla);
        this.mayordomo = mayordomo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Integer getCuadrilla() {
        return cuadrilla;
    }

    public void setCuadrilla(Integer cuadrilla) {
        this.cuadrilla = cuadrilla;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMayordomo() {
        return mayordomo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setMayordomo(String mayordomo) {
        this.mayordomo = mayordomo;
    }

    public String getFecha() {
        return Complementos.obtenerFechaString(this.fechaInicio);
    }

    @Override
    public String toString() {
        return "Cuadrillas{" +
                "id=" + id +
                ", cuadrilla=" + cuadrilla +
                ", mayordomo='" + mayordomo + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", fecha='" + getFecha() + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.cuadrilla);
        dest.writeString(this.mayordomo);
    }

    protected Cuadrillas(Parcel in) {
        this.cuadrilla = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mayordomo = in.readString();
    }



    public static final Creator<Cuadrillas> CREATOR = new Creator<Cuadrillas>() {
        @Override
        public Cuadrillas createFromParcel(Parcel source) {
            return new Cuadrillas(source);
        }

        @Override
        public Cuadrillas[] newArray(int size) {
            return new Cuadrillas[size];
        }
    };
}

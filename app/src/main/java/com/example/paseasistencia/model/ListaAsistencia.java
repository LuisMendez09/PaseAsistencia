package com.example.paseasistencia.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ListaAsistencia implements Parcelable {
    private Trabajadores trabajadores;
    private Asistencia asistencia;
    private Integer edicionTrabajador=0;
    private Integer edicionPuesto=0;

    public ListaAsistencia(Trabajadores trabajadores, Asistencia asistencia) {
        this.trabajadores = trabajadores;
        this.asistencia = asistencia;
    }

    public Trabajadores getTrabajadores() {
        return trabajadores;
    }

    public void setTrabajadores(Trabajadores trabajadores) {
        this.trabajadores = trabajadores;
    }

    public Asistencia getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Asistencia asistencia) {
        this.asistencia = asistencia;
    }

    public Integer getEdicionTrabajador() {
        return edicionTrabajador;
    }

    public void setEdicionTrabajador(Integer edicion) {
        this.edicionTrabajador = edicion;
    }

    public Integer getEdicionPuesto() {
        return edicionPuesto;
    }

    public void setEdicionPuesto(Integer edicionPuesto) {
        this.edicionPuesto = edicionPuesto;
    }


    @Override
    public String toString() {
        return "ListaAsistencia{" +
                "trabajadores=" + trabajadores +
                ", asistencia=" + asistencia +
                ", edicionTrabajador=" + edicionTrabajador +
                ", edicionPuesto=" + edicionPuesto +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.trabajadores, flags);
        dest.writeParcelable(this.asistencia, flags);
        dest.writeValue(this.edicionTrabajador);
        dest.writeValue(this.edicionPuesto);
    }

    protected ListaAsistencia(Parcel in) {
        this.trabajadores = in.readParcelable(Trabajadores.class.getClassLoader());
        this.asistencia = in.readParcelable(Asistencia.class.getClassLoader());
        this.edicionTrabajador = (Integer) in.readValue(Integer.class.getClassLoader());
        this.edicionPuesto = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<ListaAsistencia> CREATOR = new Parcelable.Creator<ListaAsistencia>() {
        @Override
        public ListaAsistencia createFromParcel(Parcel source) {
            return new ListaAsistencia(source);
        }

        @Override
        public ListaAsistencia[] newArray(int size) {
            return new ListaAsistencia[size];
        }
    };
}

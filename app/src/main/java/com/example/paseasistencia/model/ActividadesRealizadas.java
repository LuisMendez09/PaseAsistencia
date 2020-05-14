package com.example.paseasistencia.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ActividadesRealizadas implements Parcelable {
    private Long id;
    private Integer cuadrlla;
    private Actividades actividad;
    private Mallas mallas;
    private String fecha;
    private Integer sended;
    private Integer tipoActividad;

    public ActividadesRealizadas(Integer cuadrlla, Actividades actividad, Mallas mallas, String fecha,Integer tipoActividad,Integer sended) {
        this.cuadrlla = cuadrlla;
        this.actividad = actividad;
        this.mallas = mallas;
        this.fecha = fecha;
        this.tipoActividad = tipoActividad;
        this.sended = sended;
    }

    public ActividadesRealizadas(Cursor cursor,Actividades actividad,Mallas mallas) {
        this.id = cursor.getLong(0);
        this.cuadrlla = cursor.getInt(1);
        this.actividad = actividad;
        this.mallas = mallas;
        this.fecha = cursor.getString(4);
        this.tipoActividad = cursor.getInt(5);
        this.sended = cursor.getInt(6);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCuadrlla() {
        return cuadrlla;
    }

    public void setCuadrlla(Integer cuadrlla) {
        this.cuadrlla = cuadrlla;
    }

    public Actividades getActividad() {
        return actividad;
    }

    public void setActividad(Actividades actividad) {
        this.actividad = actividad;
    }

    public Mallas getMalla() {
        return mallas;
    }

    public void setMalla(Mallas mallas) {
        this.mallas = mallas;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getSended() {
        return sended;
    }

    public void setSended(Integer sended) {
        this.sended = sended;
    }

    public Integer getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(Integer tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    @Override
    public String toString() {
        return "ActividadesRealizadas{" +
                "id=" + id +
                ", cuadrlla=" + cuadrlla +
                ", actividad=" + actividad +
                ", mallas=" + mallas +
                ", fecha='" + fecha + '\'' +
                ", sended=" + sended +
                ", tipoActividad=" + tipoActividad +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.cuadrlla);
        dest.writeParcelable(this.actividad, flags);
        dest.writeParcelable(this.mallas, flags);
        dest.writeString(this.fecha);
        dest.writeValue(this.sended);
        dest.writeValue(this.tipoActividad);
    }

    protected ActividadesRealizadas(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.cuadrlla = (Integer) in.readValue(Integer.class.getClassLoader());
        this.actividad = in.readParcelable(Actividades.class.getClassLoader());
        this.mallas = in.readParcelable(Mallas.class.getClassLoader());
        this.fecha = in.readString();
        this.sended = (Integer) in.readValue(Integer.class.getClassLoader());
        this.tipoActividad = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<ActividadesRealizadas> CREATOR = new Creator<ActividadesRealizadas>() {
        @Override
        public ActividadesRealizadas createFromParcel(Parcel source) {
            return new ActividadesRealizadas(source);
        }

        @Override
        public ActividadesRealizadas[] newArray(int size) {
            return new ActividadesRealizadas[size];
        }
    };
}

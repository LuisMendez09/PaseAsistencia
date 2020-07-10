package com.example.paseasistencia.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class MallasRealizadas implements Parcelable {
    private Long id;
    private Integer cuadrlla;
    private Actividades actividad;
    private String sector;
    private Mallas mallas;
    private String fecha;
    private Integer sended;
    private TiposActividades tipoActividad;

    public MallasRealizadas(Integer cuadrlla, Actividades actividad, String sector, Mallas mallas, String fecha, TiposActividades tipoActividad, Integer sended) {
        this.cuadrlla = cuadrlla;
        this.actividad = actividad;
        this.sector = sector;
        this.mallas = mallas;
        this.fecha = fecha;
        this.tipoActividad = tipoActividad;
        this.sended = sended;
    }

    public MallasRealizadas(Cursor cursor, Actividades actividad, Mallas mallas, TiposActividades tiposActividades) {
        this.id = cursor.getLong(0);
        this.cuadrlla = cursor.getInt(1);
        this.actividad = actividad;
        this.sector = mallas.getSector();
        this.mallas = mallas;
        this.fecha = cursor.getString(4);
        this.tipoActividad = tiposActividades;//cursor.getInt(5);
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

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
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

    private String getFechaServidor() {
        String[] date = this.fecha.split("/");
        String fecha = date[2] + "-" + date[1] + "-" + date[0];

        return fecha;
    }

    public Integer getSended() {
        return sended;
    }

    public void setSended(Integer sended) {
        this.sended = sended;
    }

    public TiposActividades getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(TiposActividades tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    @Override
    public String toString() {
        return "ActividadesRealizadas{" +
                "id=" + id +
                ", cuadrlla=" + cuadrlla +
                ", actividad=" + actividad +
                ", mallas=" + mallas +
                ", fecha='" + fecha +
                ", sended=" + sended +
                ", tipoActividad=" + tipoActividad +
                '}';
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("fecha", this.getFechaServidor());
        json.put("cuadrilla", this.getCuadrlla());
        json.put("idActividad", this.getActividad().getId());
        json.put("tipoActividad", this.getTipoActividad().getId());
        json.put("idMalla", this.getMalla().getId());

        return json;
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
        dest.writeString(this.sector);
        dest.writeParcelable(this.mallas, flags);
        dest.writeString(this.fecha);
        dest.writeValue(this.sended);
        dest.writeParcelable(this.tipoActividad, flags);
    }

    protected MallasRealizadas(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.cuadrlla = (Integer) in.readValue(Integer.class.getClassLoader());
        this.actividad = in.readParcelable(Actividades.class.getClassLoader());
        this.sector = in.readString();
        this.mallas = in.readParcelable(Mallas.class.getClassLoader());
        this.fecha = in.readString();
        this.sended = (Integer) in.readValue(Integer.class.getClassLoader());
        this.tipoActividad = in.readParcelable(TiposActividades.class.getClassLoader());
    }

    public static final Creator<MallasRealizadas> CREATOR = new Creator<MallasRealizadas>() {
        @Override
        public MallasRealizadas createFromParcel(Parcel source) {
            return new MallasRealizadas(source);
        }

        @Override
        public MallasRealizadas[] newArray(int size) {
            return new MallasRealizadas[size];
        }
    };
}

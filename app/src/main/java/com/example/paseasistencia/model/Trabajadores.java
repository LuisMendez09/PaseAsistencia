package com.example.paseasistencia.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.paseasistencia.complementos.Complementos;

import org.json.JSONException;
import org.json.JSONObject;

public class Trabajadores implements Parcelable {
    private Integer id;
    private Integer cuadrilla;
    private Integer consecutivo;
    private String Nombre;
    private Integer Numero;
    private Puestos puesto;
    private Integer sended;

    public Trabajadores(){

    }

    public Trabajadores(Integer cuadrilla, Integer consecutivo, String nombre, Integer numero, Puestos puesto, Integer sended) {
        this.cuadrilla = cuadrilla;
        this.consecutivo = consecutivo;
        this.Nombre = nombre;
        this.Numero = numero;
        this.puesto = puesto;
        this.sended = sended;
    }

    public Trabajadores(Cursor cursor,Puestos puesto) {
        this.id = cursor.getInt(0);
        this.cuadrilla = cursor.getInt(1);
        this.consecutivo = cursor.getInt(2);
        this.Nombre = cursor.getString(3);
        this.Numero = cursor.getInt(4);
        this.puesto = puesto;
        this.sended = cursor.getInt(6);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(Integer consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public Integer getNumero() {
        return Numero;
    }

    public void setNumero(Integer numero) {
        Numero = numero;
    }

    public Puestos getPuesto() {
        return puesto;
    }

    public void setPuesto(Puestos puesto) {
        this.puesto = puesto;
    }

    public Integer getCuadrilla() {
        return cuadrilla;
    }

    public void setCuadrilla(Integer cuadrilla) {
        this.cuadrilla = cuadrilla;
    }



    public Integer getSended() {
        return sended;
    }

    public void setSended(Integer sended) {
        this.sended = sended;
    }

    @Override
    public String toString() {
        return "Trabajadores{" +
                "id=" + id +
                ", clave=" + cuadrilla +
                ", consecutivo=" + consecutivo +
                ", Nombre='" + Nombre + '\'' +
                ", Numero=" + Numero +
                ", puestos=" + puesto +
                ", sended=" + sended +
                '}';
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("numero", this.getNumero());
        json.put("nombre", this.getNombre());
        json.put("puesto", this.getPuesto().getNombre());
        json.put("cuadrilla", this.getCuadrilla());
        json.put("consecutivo", this.getConsecutivo());
        json.put("fechaExportacion", Complementos.fechaInicioSemana());
        json.put("fechaFinSem", Complementos.fechaFinSemana());

        Log.i("enviar", toString());
        return json;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.cuadrilla);
        dest.writeValue(this.consecutivo);
        dest.writeString(this.Nombre);
        dest.writeValue(this.Numero);
        dest.writeParcelable(this.puesto, flags);
        dest.writeValue(this.sended);
    }

    protected Trabajadores(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.cuadrilla = (Integer) in.readValue(Integer.class.getClassLoader());
        this.consecutivo = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Nombre = in.readString();
        this.Numero = (Integer) in.readValue(Integer.class.getClassLoader());
        this.puesto = in.readParcelable(Puestos.class.getClassLoader());
        this.sended = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Trabajadores> CREATOR = new Parcelable.Creator<Trabajadores>() {
        @Override
        public Trabajadores createFromParcel(Parcel source) {
            return new Trabajadores(source);
        }

        @Override
        public Trabajadores[] newArray(int size) {
            return new Trabajadores[size];
        }
    };
}

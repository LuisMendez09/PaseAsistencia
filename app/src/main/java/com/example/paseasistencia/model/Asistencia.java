package com.example.paseasistencia.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.paseasistencia.complementos.Complementos;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Asistencia implements Parcelable {
    private  Long id;
    private Trabajadores trabajador;
    private Puestos puesto;
    private Date dateInicio;
    private Date dateFin;
    private Integer sended;

    public Asistencia(Trabajadores trabajador, Puestos puesto,Date dateInicio,Date dateFin,Integer sended) {
        this.trabajador = trabajador;
        this.puesto = puesto;
        this.dateInicio = dateInicio;
        this.dateFin = dateFin;
        this.sended = sended;
    }

    public Asistencia(Cursor cursor,Trabajadores trabajador,Puestos puesto) {

        this.id = cursor.getLong(0);
        this.trabajador = trabajador;
        this.puesto = puesto;
        this.dateInicio = new Date(cursor.getLong(3));
        this.dateFin = new Date(cursor.getLong(4));
        this.sended = cursor.getInt(8);

        //Log.i("inisersion",trabajador.toString()+"--"+puesto.toString());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trabajadores getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajadores trabajador) {
        this.trabajador = trabajador;
    }

    public Puestos getPuesto() {
        return puesto;
    }

    public void setPuesto(Puestos puesto) {
        this.puesto = puesto;
    }

    public Date getDateInicio() {
        return dateInicio;
    }

    public void setDateInicio(Date date) {
        this.dateInicio = date;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date date) {
        this.dateFin = date;
    }

    public String getFecha() {
        return Complementos.obtenerFechaString(this.dateInicio);
    }

    private String getFechaServidor() {
        return Complementos.obtenerFechaServidor(this.dateInicio);
    }
/*
    public void setFecha(String fecha) {
        this.fecha = Complementos.obtenerFechaString(getDateInicio());
    }
*/
    public String getHoraInicio() {

        return Complementos.obtenerHoraString(this.dateInicio);
    }

    /*public void setHoraInicio(String horaInicio) {
        HoraInicio = horaInicio;
    }*/

    public String getHoraFinal() {

        return Complementos.obtenerHoraString(this.dateFin);
    }
/*
    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }
*/
    public Integer getSended() {
        return sended;
    }

    public void setSended(Integer sended) {
        this.sended = sended;
    }


    @Override
    public String toString() {
        return "Asistencia{" +
                "id=" + id +
                ", trabajador=" + trabajador +
                ", puesto=" + puesto +
                ", dateInicio=" + dateInicio +
                ", dateFin=" + dateFin +
                ", fecha='" + (getFecha() != null ? getFecha() : "") + '\'' +
                ", HoraInicio='" + getHoraInicio() + '\'' +
                ", horaFinal='" + getHoraFinal() + '\'' +
                ", sended=" + sended +
                '}';
    }

    public String toString1() {
        return "Asistencia{" +
                ", HoraInicio='" + getHoraInicio() + '\'' +
                ", horaFinal='" + getHoraFinal() + '\'' +
                ", sended=" + sended +
                '}';
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();

        json.put("fecha", this.getFechaServidor());
        json.put("cuadrilla", this.getTrabajador().getCuadrilla());
        json.put("consecutivo", this.getTrabajador().getConsecutivo());
        json.put("numero", this.getTrabajador().getNumero());
        json.put("idPuesto", this.getPuesto().getId());
        json.put("horaInicio", getHoraInicio());
        json.put("horaFinal", getHoraFinal());

        return json;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeParcelable(this.trabajador, flags);
        dest.writeParcelable(this.puesto, flags);
        dest.writeLong(this.dateInicio != null ? this.dateInicio.getTime() : -1);
        dest.writeLong(this.dateFin != null ? this.dateFin.getTime() : -1);
        dest.writeValue(this.sended);
    }

    protected Asistencia(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.trabajador = in.readParcelable(Trabajadores.class.getClassLoader());
        this.puesto = in.readParcelable(Puestos.class.getClassLoader());
        long tmpDateInicio = in.readLong();
        this.dateInicio = tmpDateInicio == -1 ? null : new Date(tmpDateInicio);
        long tmpDateFin = in.readLong();
        this.dateFin = tmpDateFin == -1 ? null : new Date(tmpDateFin);
        this.sended = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Asistencia> CREATOR = new Parcelable.Creator<Asistencia>() {
        @Override
        public Asistencia createFromParcel(Parcel source) {
            return new Asistencia(source);
        }

        @Override
        public Asistencia[] newArray(int size) {
            return new Asistencia[size];
        }
    };
}

package com.example.paseasistencia.model;

import android.database.Cursor;

import com.example.paseasistencia.complementos.Complementos;

import java.util.Date;

public class Settings {
    private Long id;
    private Date inicio;
    private Date fin;
    /*private String fecha;
    private String horainicio;
    private String horaFinal;*/
    private Integer jornadaFinalizada;
    private Integer jornadaIniciada;
    private Integer envioDatos;

    public Settings(Date inicio, Date fin,  Integer jornadaFinalizada, Integer jornadaIniciada, Integer envioDatos) {
        this.inicio = inicio;
        this.fin = fin;
        //this.fecha = Complementos.obtenerFechaString(this.inicio);
        //this.horainicio = Complementos.obtenerHoraString(this.inicio);
        //this.horaFinal = Complementos.obtenerHoraString(this.fin);
        this.jornadaFinalizada = jornadaFinalizada;
        this.jornadaIniciada = jornadaIniciada;
        this.envioDatos = envioDatos;
    }

    public Settings(Cursor cursor) {
        this.id = cursor.getLong(0);
        this.inicio = new Date(cursor.getLong(1));
        this.fin = new Date(cursor.getLong(2));
        //this.fecha = cursor.getString(3);
        //this.horainicio = cursor.getString(4);
        //this.horaFinal = cursor.getString(5);
        this.jornadaFinalizada = cursor.getInt(6);
        this.jornadaIniciada = cursor.getInt(7);
        this.envioDatos = cursor.getInt(8);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public String getFecha() {
        return Complementos.obtenerFechaString(this.inicio);
    }

    /*public void setFecha(String fecha) {
        this.fecha = fecha;
    }*/

    public String getHorainicio() {
        return Complementos.obtenerHoraString(this.inicio);
    }

    /*public void setHorainicio(String horainicio) {
        this.horainicio = horainicio;
    }*/

    public String getHoraFinal() {
        return Complementos.obtenerHoraString(this.fin);
    }

    /*public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }*/

    public Integer getJornadaFinalizada() {
        return jornadaFinalizada;
    }

    public void setJornadaFinalizada(Integer jornadaFinalizada) {
        this.jornadaFinalizada = jornadaFinalizada;
    }

    public Integer getJornadaIniciada() {
        return jornadaIniciada;
    }

    public void setJornadaIniciada(Integer jornadaIniciada) {
        this.jornadaIniciada = jornadaIniciada;
    }

    public Integer getEnvioDatos() {
        return envioDatos;
    }

    public void setEnvioDatos(Integer envioDatos) {
        this.envioDatos = envioDatos;
    }

    @Override
    public String toString() {
        return "Settings{" +
                ", inicio=" + inicio +
                ", fin=" + fin +
                ", fecha='" + Complementos.obtenerFechaString(this.inicio) + '\'' +
                ", horainicio='" + Complementos.obtenerHoraString(this.inicio) + '\'' +
                ", horaFinal='" +  Complementos.obtenerHoraString(this.fin) + '\'' +
                ", jornadaFinalizada=" + jornadaFinalizada +
                ", jornadaIniciada=" + jornadaIniciada +
                ", envioDatos=" + envioDatos +
                '}';
    }
}

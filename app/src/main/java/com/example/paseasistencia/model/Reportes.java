package com.example.paseasistencia.model;

import android.database.Cursor;

public class Reportes {
    private Long id;
    private Trabajadores trabajadores;
    private TiposPermisos tiposPermisos;
    private String fecha;
    private String horaInicial;
    private String horaFinal;
    private Integer sended;

    public Reportes(Trabajadores trabajadores, TiposPermisos tiposPermisos, String fecha, String horaInicial, String horaFinal,Integer sended) {
        this.trabajadores = trabajadores;
        this.tiposPermisos = tiposPermisos;
        this.fecha = fecha;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
        this.sended = sended;
    }

    public Reportes(Cursor cursor,Trabajadores trabajador,TiposPermisos tiposPermiso) {
        this.id = cursor.getLong(0);
        this.trabajadores = trabajador;
        this.tiposPermisos = tiposPermiso;
        this.fecha = cursor.getString(3);
        this.horaInicial = cursor.getString(4);
        this.horaFinal = cursor.getString(5);
        this.sended = cursor.getInt(6);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trabajadores getTrabajadores() {
        return trabajadores;
    }

    public void setTrabajadores(Trabajadores trabajadores) {
        this.trabajadores = trabajadores;
    }

    public TiposPermisos getTiposPermisos() {
        return tiposPermisos;
    }

    public void setTiposPermisos(TiposPermisos tiposPermisos) {
        this.tiposPermisos = tiposPermisos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(String horaInicial) {
        this.horaInicial = horaInicial;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }

    public Integer getSended() {
        return sended;
    }

    public void setSended(Integer sended) {
        this.sended = sended;
    }

    @Override
    public String toString() {
        return "Reportes{" +
                "id=" + id +
                ", trabajadores=" + trabajadores +
                ", tiposPermisos=" + tiposPermisos +
                ", fecha='" + fecha + '\'' +
                ", horaInicial='" + horaInicial + '\'' +
                ", horaFinal='" + horaFinal + '\'' +
                ", sended=" + sended +
                '}';
    }
}

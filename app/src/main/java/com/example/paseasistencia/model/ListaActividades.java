package com.example.paseasistencia.model;

import android.util.Log;

import com.example.paseasistencia.model.Actividades;
import com.example.paseasistencia.model.MallasRealizadas;
import com.example.paseasistencia.model.Mallas;

import java.util.ArrayList;

public class ListaActividades {
    private Integer cuadrillas;
    private Actividades actividad;
    private Integer tipoActividad;
    private String sector;
    private ArrayList<MallasRealizadas> listaMallasRealizadas;//lista de mallas realizadas
    //private ArrayList<ActividadesRealizadas> listaActividadesDescartadas;//lista de mallas descartadas

    public ListaActividades() {
    }

    public ListaActividades(Integer cuadrillas, Actividades actividad, Integer tipoActividad, String sector, ArrayList<MallasRealizadas> listaMallasRealizadas) {
        this.cuadrillas = cuadrillas;
        this.actividad = actividad;
        this.tipoActividad = tipoActividad;
        this.sector = sector;
        this.listaMallasRealizadas = listaMallasRealizadas;
        //this.listaActividadesDescartadas = new ArrayList<>();
    }

    public Actividades getActividad() {
        return actividad;
    }

    public void setActividad(Actividades actividad) {
        this.actividad = actividad;
    }

    public Integer getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(Integer tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Integer getCuadrillas() {
        return cuadrillas;
    }

    public ArrayList<MallasRealizadas> getListaMallasRealizadas() {
        return listaMallasRealizadas;
    }

    public void setListaMallasRealizadas(ArrayList<MallasRealizadas> listaMallasRealizadas) {
        this.listaMallasRealizadas = null;
        this.listaMallasRealizadas = listaMallasRealizadas;
    }

    public void setCuadrillas(Integer cuadrillas) {
        this.cuadrillas = cuadrillas;
    }

    public void addActividadRealizada(MallasRealizadas mallasRealizadas) {
        for (MallasRealizadas ar : this.listaMallasRealizadas) {
            if (mallasRealizadas.getMalla().getId().equals(ar.getMalla().getId())) {
                return;
            }
        }

        this.listaMallasRealizadas.add(mallasRealizadas);
    }


    public String getMallas() {
        String r = "";

        for (MallasRealizadas ar : listaMallasRealizadas) {
            r = r.equals("") ? ar.getMalla().getId() : r + ", " + ar.getMalla().getId();
        }

        return r;
    }

    public ArrayList<Mallas> getListaMallas() {
        ArrayList<Mallas> r = new ArrayList<>();

        for (MallasRealizadas ar : listaMallasRealizadas) {
            r.add(ar.getMalla());
        }

        return r;
    }
}

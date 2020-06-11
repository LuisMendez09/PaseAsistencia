package com.example.paseasistencia.ui.actividad;

import com.example.paseasistencia.model.Actividades;
import com.example.paseasistencia.model.ActividadesRealizadas;
import com.example.paseasistencia.model.Mallas;

import java.util.ArrayList;

public class ListaActividades {
    private Integer cuadrillas;
    private Actividades actividad;
    private Integer tipoActividad;
    private String sector;
    private ArrayList<ActividadesRealizadas> listaActividadesRealizadas;//lista de mallas realizadas
    //private ArrayList<ActividadesRealizadas> listaActividadesDescartadas;//lista de mallas descartadas

    public ListaActividades() {
    }

    public ListaActividades(Integer cuadrillas, Actividades actividad, Integer tipoActividad, String sector, ArrayList<ActividadesRealizadas> listaActividadesRealizadas) {
        this.cuadrillas = cuadrillas;
        this.actividad = actividad;
        this.tipoActividad = tipoActividad;
        this.sector = sector;
        this.listaActividadesRealizadas = listaActividadesRealizadas;
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

    public ArrayList<ActividadesRealizadas> getListaActividadesRealizadas() {
        return listaActividadesRealizadas;
    }

    public void setListaActividadesRealizadas(ArrayList<ActividadesRealizadas> listaActividadesRealizadas) {
        this.listaActividadesRealizadas = listaActividadesRealizadas;
    }

    public void setCuadrillas(Integer cuadrillas) {
        this.cuadrillas = cuadrillas;
    }

    public void addActividadRealizada(ActividadesRealizadas actividadesRealizadas) {
        for (ActividadesRealizadas ar :
                this.listaActividadesRealizadas) {
            if (actividadesRealizadas.getMalla().getId().equals(ar.getMalla().getId())) {
                return;
            }
        }

        this.listaActividadesRealizadas.add(actividadesRealizadas);
    }

   /* public ArrayList<ActividadesRealizadas> getListaActividadesDescartadas() {
        return listaActividadesDescartadas;
    }

    public void setListaActividadesDescartadas(ActividadesRealizadas listaActividadesDescartadas) {
        this.listaActividadesDescartadas.add( listaActividadesDescartadas);
    }*/

    public String getMallas() {
        String r = "";

        for (ActividadesRealizadas ar : listaActividadesRealizadas) {
            r = r.equals("") ? ar.getMalla().getId() : r + ", " + ar.getMalla().getId();
        }

        return r;
    }

    public ArrayList<Mallas> getListaMallas() {
        ArrayList<Mallas> r = new ArrayList<>();

        for (ActividadesRealizadas ar : listaActividadesRealizadas) {
            r.add(ar.getMalla());
        }

        return r;
    }
}
